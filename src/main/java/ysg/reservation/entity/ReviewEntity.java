package ysg.reservation.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "review")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEntity {
    @Id
    private int R_IDX;      // 리뷰 고유번호
    private int S_IDX;      // 매장 고유번호
    private int M_IDX;      // 이용자 고유번호
    private String TITLE;   // 제목
    private String CNT;     // 내용
    private double STAR;    // 별점




}
