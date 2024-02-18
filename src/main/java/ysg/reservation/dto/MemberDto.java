package ysg.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.StoreEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private int M_IDX;               // 이용자 고유번호
    private String USER_ID;         // 아이디
    private String USER_PWD;        // 비밀번호
    private String NAME;            // 이름
    private String PHONE;           // 폰번호
    private String GENDER;          // 성별
    private String ROLE;            // 역할

    private List<ReservationDto> R_IDX = new ArrayList<>(); // 양방향 연관 관계 설정
    public static MemberDto fromEntity(MemberEntity memberEntity){
        return MemberDto.builder()
                .M_IDX(memberEntity.getMIDX())
                .USER_ID(memberEntity.getUSER_ID())
                .USER_PWD(memberEntity.getUSER_PWD())
                .NAME(memberEntity.getNAME())
                .PHONE(memberEntity.getPHONE())
                .GENDER(memberEntity.getGENDER())
                .ROLE(memberEntity.getROLE())
                .R_IDX(memberEntity.getRIDX()
                        .stream()
                        .map(e-> ReservationDto.fromEntity(e))
                        .collect(Collectors.toList()))
                .build();
    }

}
