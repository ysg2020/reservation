package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ysg.reservation.auth.JwtToken;
import ysg.reservation.auth.MemberAuthUtil;
import ysg.reservation.auth.TokenProvider;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.SignInDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.repository.MemberRepository;
import ysg.reservation.type.ErrorCode;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider jwtTokenProvider;
    private final RedisTemplate<String,String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    // 회원 등록
    public MemberDto createMember(MemberDto memberDto) {
        log.info("[MemberService] createMember -> "+memberDto.toString());

        MemberEntity memberEntity = MemberEntity.builder()
                .MIDX(memberDto.getM_IDX())
                .USERID(memberDto.getUSER_ID())
                .USERPWD(memberDto.getUSER_PWD())
                .NAME(memberDto.getNAME())
                .PHONE(memberDto.getPHONE())
                .GENDER(memberDto.getGENDER())
                .ROLE(memberDto.getROLE())
                .build();

        return MemberDto.fromEntity(memberRepository.save(memberEntity));
    }
    // 회원 수정
    public MemberDto alterMember(MemberDto memberDto) {
        log.info("[MemberService] alterMember -> "+memberDto.toString());

        // 현재 로그인한 사용자 체크
        MemberAuthUtil.loginUserCheck(memberDto.getUSER_ID());

        MemberEntity memberEntity = MemberEntity.builder()
                .MIDX(memberDto.getM_IDX())
                .USERID(memberDto.getUSER_ID())
                .USERPWD(memberDto.getUSER_PWD())
                .NAME(memberDto.getNAME())
                .PHONE(memberDto.getPHONE())
                .GENDER(memberDto.getGENDER())
                .ROLE(memberDto.getROLE())
                .build();

        return MemberDto.fromEntity(memberRepository.save(memberEntity));
    }

    // 회원 삭제
    public void dropMember(int m_idx) {
        log.info("[MemberService] dropMember -> "+m_idx);
        memberRepository.deleteById(m_idx);
    }

    // 회원 로그인

    @Transactional
    public JwtToken signIn(SignInDto signInDto) {
        log.info("[Service] signIn");
        // 1. 회원 아이디로 회원 정보 조회
        MemberEntity member = memberRepository.findByUSERID(signInDto.getUSER_ID())
                .orElseThrow(()-> new ReservationException(ErrorCode.NOT_FOUND_USERID));
        String encodedPwd = passwordEncoder.encode(member.getUSERPWD());

        // 2. 저장되어있는 회원 정보의 비밀번호와 같은지 비교
        if (!passwordEncoder.matches(signInDto.getUSER_PWD(), encodedPwd)) {
            throw new ReservationException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.createToken(signInDto.getUSER_ID(),member.getROLE());

        // 4. RefreshToken 저장
        String key = "refreshToken::"+signInDto.getUSER_ID();
        redisTemplate.opsForValue().set(key,jwtToken.getRefreshToken());

        return jwtToken;
    }

    // 리프레시 토큰을 이용하여 액세스 토큰 재발급
    @Transactional
    public JwtToken reIssue(MemberDto memberDto, HttpServletRequest request){
        JwtToken jwtToken;
        String key = "refreshToken::"+memberDto.getUSER_ID();
        // 액세스, 리프레시 토큰
        String accessToken = request.getHeader("Authorization").substring(7);
        String refreshToken = request.getHeader("RefreshToken").substring(7);

        // 1. 액세스,리프레시 토큰 검증
        boolean validateAccessToken = jwtTokenProvider.validateToken(accessToken);
        boolean validateRefreshToken = jwtTokenProvider.validateToken(refreshToken);

        // 2. 재발급 case
        // 2-1. 액세스 토큰이 만료 되었지만 리프레시 토큰은 만료되지 않은 경우
        if (!validateAccessToken && validateRefreshToken) {
            // DB에 리프레시 토큰이 있는지 확인 (리프레시 토큰이 만료되지 않았지만 db에 없으면 로그아웃으로 간주)
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))){
                // 액세스 토큰 재발급
                jwtToken = jwtTokenProvider.createAccessToken(memberDto.getUSER_ID(), memberDto.getROLE(),refreshToken);
            } else {
                throw new ReservationException(ErrorCode.ALREADY_LOGOUT_USER);
            }

        }
        // 2-2. 리프레시 토큰이 만료 되었지만 액세스 토큰은 만료되지 않은 경우
        else if (validateAccessToken && !validateRefreshToken) {
            // 리프레시 토큰 재발급
            jwtToken = jwtTokenProvider.createRefreshToken(accessToken);
        }
        else {
            throw new ReservationException(ErrorCode.EXPIRE_TOKEN);

        }

        return jwtToken;

    }


    // 회원 로그아웃
    public String signOut(SignInDto signInDto){
        // 리프레시 토큰 삭제
        String key = "refreshToken::"+signInDto.getUSER_ID();
        redisTemplate.delete(key);
        return key;
    }



}
