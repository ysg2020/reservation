package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "review")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEntity {
    @Id
    @Column(name = "V_IDX")
    private int VIDX;               // 리뷰 고유번호

    @OneToOne
    @JoinColumn(name = "R_IDX")
    private ReservationEntity RIDX; // 예약 고유번호

    @Column(name = "S_IDX")
    private int SIDX;       // 매장 고유번호

    @Column(name = "M_IDX")
    private int MIDX;      // 사용자 고유번호

    private String TITLE;           // 제목
    private String CNT;             // 내용
    private double STAR;            // 별점
    private LocalDateTime WRITE_DATE;    // 작성일자




}
