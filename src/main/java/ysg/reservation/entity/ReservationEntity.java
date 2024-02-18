package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reservation")
@Getter
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @Column(name ="R_IDX")
    private int RIDX;                       // 예약 고유번호

    @ManyToOne
    @JoinColumn(name = "S_IDX")
    private StoreEntity SIDX;                       // 매장 고유번호

    @ManyToOne
    @JoinColumn(name = "M_IDX")
    private MemberEntity MIDX;              // 이용자 고유번호

    private LocalDateTime START_TIME;       // 요청시간

    @Column(name ="RESER_TIME")
    private LocalDateTime RESERTIME;       // 예약시간

    private int TABLE_CNT;                  // 예약할 테이블 수
    @Column(name = "RESER_STAT")
    private String RESERSTAT;              // 예약상태
    private LocalDateTime RESER_CHK_TIME;   // 예약확인시간
    private String END_YN;                  // 도착여부
    private LocalDateTime END_TIME;         // 도착시간


}
