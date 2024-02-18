package ysg.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.reservation.entity.ReservationEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private int R_IDX;                  // 예약 고유번호
    private int S_IDX;                  // 매장 고유번호
    private int M_IDX;                  // 이용자 고유번호

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime START_TIME;   // 요청시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime RESER_TIME;   // 예약시간

    private int TABLE_CNT;              // 예약할 테이블 수
    private String RESER_STAT;          // 예약상태

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime RESER_CHK_TIME;      // 예약확인시간

    private String END_YN;              // 도착여부

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime END_TIME;     // 도착시간

    public static ReservationDto fromEntity(ReservationEntity reservationEntity){
        return ReservationDto.builder()
                .R_IDX(reservationEntity.getRIDX())
                .S_IDX(reservationEntity.getSIDX().getSIDX())
                .M_IDX(reservationEntity.getMIDX().getMIDX())
                .START_TIME(reservationEntity.getSTART_TIME())
                .RESER_TIME(reservationEntity.getRESERTIME())
                .TABLE_CNT(reservationEntity.getTABLE_CNT())
                .RESER_STAT(reservationEntity.getRESERSTAT())
                .RESER_CHK_TIME(reservationEntity.getRESER_CHK_TIME())
                .END_YN(reservationEntity.getEND_YN())
                .END_TIME(reservationEntity.getEND_TIME())
                .build();
    }

}
