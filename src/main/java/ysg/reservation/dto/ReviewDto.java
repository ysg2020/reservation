package ysg.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.reservation.entity.ReviewEntity;
import ysg.reservation.entity.StoreEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private int R_IDX;              // 리뷰 고유번호
    private StoreDto S_IDX;         // 매장 고유번호
    private MemberDto M_IDX;        // 사용자 고유번호
    private String TITLE;           // 제목
    private String CNT;             // 내용
    private double STAR;            // 별점

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime WRITE_DATE;    // 작성일자

    public static ReviewDto fromEntity(ReviewEntity reviewEntity){
        return ReviewDto.builder()
                .R_IDX(reviewEntity.getRIDX())
                .S_IDX(StoreDto.fromEntity(reviewEntity.getSIDX()))
                .M_IDX(MemberDto.fromEntity(reviewEntity.getMIDX()))
                .TITLE(reviewEntity.getTITLE())
                .CNT(reviewEntity.getCNT())
                .STAR(reviewEntity.getSTAR())
                .WRITE_DATE(reviewEntity.getWRITE_DATE())
                .build();
    }
}
