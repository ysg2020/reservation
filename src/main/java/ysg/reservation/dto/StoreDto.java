package ysg.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ysg.reservation.entity.StoreEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {

    private int S_IDX;      // 매장 고유번호
    private String NAME;        // 이름
    private String LOC;         // 위치
    private String DES;         // 설명
    private double STAR;        // 별점
    private int TABLE_CNT;       // 매장 테이블 수


    public static StoreDto fromEntity(StoreEntity storeEntity){
        return StoreDto.builder()
                .S_IDX(storeEntity.getSIDX())
                .NAME(storeEntity.getNAME())
                .LOC(storeEntity.getLOC())
                .DES(storeEntity.getDES())
                .STAR(storeEntity.getSTAR())
                .TABLE_CNT(storeEntity.getTABLE_CNT())
                .build();
    }
}
