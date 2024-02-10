package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "member")
@Getter
@ToString
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


}
