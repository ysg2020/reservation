package ysg.reservation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ysg.reservation.auth.JwtToken;
import ysg.reservation.auth.TokenProvider;
import ysg.reservation.dto.SignInDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.repository.MemberRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TokenProvider jwtTokenProvider;
    @Mock
    private RedisTemplate<String,String> redisTemplate;
    @Mock
    private ValueOperations valueOperations;
    @Spy
    private PasswordEncoder passwordEncoder = new MockPasswordEncoder();
    @InjectMocks
    private MemberService memberService;

    public class MockPasswordEncoder implements PasswordEncoder {

        @Override
        public String encode(CharSequence rawPassword) {
            return new StringBuilder(rawPassword).reverse().toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    }

    @Test
    @DisplayName("로그인  성공")
    void signIn() {
        //given
        SignInDto loginMember = SignInDto.builder()
                .USER_ID("user1234")
                .USER_PWD("pwd1234")
                .build();
        MemberEntity member = MemberEntity.builder()
                .MIDX(1)
                .USERID("user1234")
                .USERPWD("pwd1234")
                .NAME("test")
                .PHONE("01012341234")
                .GENDER("M")
                .ROLE("OWNER")
                .build();
        JwtToken jwtToken = JwtToken.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .grantType("Bearer")
                .build();

        given(memberRepository.findByUSERID(any()))
                .willReturn(Optional.ofNullable(member));
        given(jwtTokenProvider.createToken(anyString(),anyString()))
                .willReturn(jwtToken);
        given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        //when
        memberService.signIn(loginMember);
        //then

    }
    @Test
    @DisplayName("로그인  실패 - 비밀번호가 다른 경우")
    void signIn_fail_not_match_pwd() {
        //given
        // 로그인시 pwd : user1234
        SignInDto loginMember = SignInDto.builder()
                .USER_ID("user1234")
                .USER_PWD("pwd1234")
                .build();

        // DB에 저장되어있는 pwd : user12345
        MemberEntity member = MemberEntity.builder()
                .MIDX(1)
                .USERID("user1234")
                .USERPWD("pwd12345")
                .NAME("test")
                .PHONE("01012341234")
                .GENDER("M")
                .ROLE("OWNER")
                .build();

        given(memberRepository.findByUSERID(any()))
                .willReturn(Optional.ofNullable(member));

        //when
        //then
        Assertions.assertThrows(ReservationException.class,()-> memberService.signIn(loginMember));

    }

    @Test
    @DisplayName("로그인  실패 - 해당 아이디가 존재하지 않는 경우")
    void signIn_fail() {
        //given
        SignInDto loginMember = SignInDto.builder()
                .USER_ID("user12345")
                .USER_PWD("pwd1234")
                .build();

        //when
        given(memberRepository.findByUSERID(loginMember.getUSER_ID()))
                .willReturn(Optional.empty());
        //then
        Assertions.assertThrows(ReservationException.class,()-> memberService.signIn(loginMember));

    }

}