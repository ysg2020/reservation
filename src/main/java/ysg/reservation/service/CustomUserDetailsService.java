package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userid)  {
        log.info("[CustomUserDetailsService] loadUserByUsername!!");
        return memberRepository.findByUSERID(userid)
                .map(this::createUserDetails)
                //.map(member -> this.createUserDetails(member))
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(MemberEntity member) {
        log.info("[CustomUserDetailsService] createUserDetails!!");
        return User.builder()
                .username(member.getUSERID())
                .password(passwordEncoder.encode(member.getUSERPWD()))
                .roles(member.getROLE())
                .build();
    }

}
