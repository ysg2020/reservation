package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "store")
@Getter
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntity {
    @Id
    @Column(name = "S_IDX")
    private int SIDX;      // 매장 고유번호
    private String NAME;    // 이름
    private String LOC;     // 위치
    private String DES;     // 설명
    private double STAR;    // 별점
    private int TABLE_CNT;   // 매장 테이블 수

    @OneToMany(mappedBy = "SIDX")
    private List<ReservationEntity> RIDX = new ArrayList<>();   // 양방향 연관 관계 설정


}
