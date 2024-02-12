package ysg.reservation.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원 등록
    @PostMapping
    public MemberDto createMember(@RequestBody MemberDto memberDto){
        return memberService.createAlterMember(memberDto);
    }
    // 회원 수정
    @PutMapping
    public MemberDto alterMember(@RequestBody MemberDto memberDto){
        return memberService.createAlterMember(memberDto);
    }
    // 회원 삭제
    @DeleteMapping
    public void dropMember(@RequestParam int m_idx){
         memberService.dropMember(m_idx);
    }

}
