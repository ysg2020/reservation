package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "review")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEntity {
    @Id
    @Column(name = "R_IDX")
    private int RIDX;               // 리뷰 고유번호

    @ManyToOne
    @JoinColumn(name = "S_IDX")
    private StoreEntity SIDX;       // 매장 고유번호

    @ManyToOne
    @JoinColumn(name = "M_IDX")
    private MemberEntity MIDX;      // 사용자 고유번호

    private String TITLE;           // 제목
    private String CNT;             // 내용
    private double STAR;            // 별점
    private LocalDateTime WRITE_DATE;    // 작성일자




}
