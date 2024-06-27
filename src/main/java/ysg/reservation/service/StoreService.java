package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.repository.StoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    // 매장 등록 및 수정
    @PreAuthorize("hasRole('OWNER')")
    public StoreDto createAlterStore(StoreDto storeDto) {
        log.info("[StoreService] createAlterStore -> "+storeDto.toString());
        StoreEntity storeEntity = StoreEntity.builder()
                .SIDX(storeDto.getS_IDX())
                .NAME(storeDto.getNAME())
                .LOC(storeDto.getLOC())
                .DES(storeDto.getDES())
                .STAR(storeDto.getSTAR())
                .build();

        return StoreDto.fromEntity(storeRepository.save(storeEntity));
    }

    // 매장 삭제
    @PreAuthorize("hasRole('OWNER')")
    public void dropStore(int s_idx) {
        log.info("[StoreService] dropStore -> "+s_idx);
        storeRepository.deleteById(s_idx);
    }

    // 특정 매장 이름으로 조회
    public StoreDto getStoreInfo(String storeName){
        log.info("[StoreService] getStoreInfo -> "+storeName);
        return StoreDto.fromEntity(storeRepository.findByNAME(storeName).orElse(new StoreEntity()));
    }


    public List<StoreDto> getAllStoreInfo() {
        log.info("[StoreService] getAllStoreInfo");
        return storeRepository.findAll().stream().map(e -> StoreDto.builder()
                .S_IDX(e.getSIDX())
                .NAME(e.getNAME())
                .LOC(e.getLOC())
                .DES(e.getDES())
                .STAR(e.getSTAR())
                .build())
                .collect(Collectors.toList());
    }
}
