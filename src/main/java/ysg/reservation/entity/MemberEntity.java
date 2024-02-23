package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @Column(name = "M_IDX")
    private int MIDX;          // 이용자 고유번호
    private String USER_ID;     // 아이디
    private String USER_PWD;    // 비밀번호
    private String NAME;        // 이름
    private String PHONE;       // 폰번호
    private String GENDER;      // 성별
    private String ROLE;        // 역할

    @OneToMany(mappedBy = "MIDX")
    private List<ReservationEntity> RIDX = new ArrayList<>();   // 양방향 연관 관계 설정

}
