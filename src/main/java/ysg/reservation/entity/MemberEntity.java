package ysg.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity implements UserDetails {

    @Id
    @Column(name = "M_IDX")
    private int MIDX;          // 이용자 고유번호

    @Column(name = "USER_ID")
    private String USERID;     // 아이디

    @Column(name = "USER_PWD")
    private String USERPWD;    // 비밀번호
    private String NAME;        // 이름
    private String PHONE;       // 폰번호
    private String GENDER;      // 성별
    private String ROLE;        // 역할

    @OneToMany(mappedBy = "MIDX")
    private List<ReservationEntity> RIDX = new ArrayList<>();   // 양방향 연관 관계 설정

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE));

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
