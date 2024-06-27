package ysg.reservation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ysg.reservation.auth.JwtToken;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.SignInDto;
import ysg.reservation.service.MemberService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원 등록
    @PostMapping("/signUp")
    public MemberDto createMember(@RequestBody MemberDto memberDto){
        log.info("[Controller] 회원 등록");
        return memberService.createMember(memberDto);
    }
    // 회원 수정
    @PutMapping
    public MemberDto alterMember(@RequestBody MemberDto memberDto){
        return memberService.alterMember(memberDto);
    }
    // 회원 삭제
    @DeleteMapping
    public void dropMember(@RequestParam int m_idx){
         memberService.dropMember(m_idx);
    }

    // 회원 로그인
    @PostMapping("/signIn")
    public JwtToken signIn(@RequestBody SignInDto signInDto){
        log.info("[Controller] 로그인");
        return memberService.signIn(signInDto);
    }

    // 토큰 재발급
    @PostMapping("/reIssue")
    public JwtToken reIssue(@RequestBody MemberDto memberDto,HttpServletRequest request){
        log.info("[Controller] 토큰 재발급");
        return memberService.reIssue(memberDto, request);
    }

    // 회원 로그아웃
    @PostMapping("/signOut")
    public String signOut(@RequestBody SignInDto signInDto){
        log.info("[Controller] 로그아웃");
        return memberService.signOut(signInDto);
    }



}
