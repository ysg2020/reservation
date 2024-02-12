package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 등록 및 수정
    public MemberDto createAlterMember(MemberDto memberDto) {
        log.info("[MemberService] createAlterMember -> "+memberDto.toString());
        MemberEntity memberEntity = MemberEntity.builder()
                .MIDX(memberDto.getM_IDX())
                .USER_ID(memberDto.getUSER_ID())
                .USER_PWD(memberDto.getUSER_PWD())
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

}
