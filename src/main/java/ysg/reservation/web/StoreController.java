package ysg.reservation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.service.StoreService;
import ysg.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    // 매장 등록
    @PostMapping
    public StoreDto createStore(@RequestBody StoreDto storeDto){
        return storeService.createAlterStore(storeDto);
    }

    // 매장 수정
    @PutMapping
    public StoreDto alterStore(@RequestBody StoreDto storeDto){
        return storeService.createAlterStore(storeDto);
    }

    // 매장 삭제
    @DeleteMapping
    public void dropStore(@RequestParam int s_idx){
        storeService.dropStore(s_idx);
    }

    // 특정 매장 이름으로 조회
    @GetMapping
    public StoreDto getStoreInfo(@RequestParam String storeName){
        return storeService.getStoreInfo(storeName);
    }

    // 매장 전체 조회
    @GetMapping("/all")
    public List<StoreDto> getAllStoreInfo(){
        return storeService.getAllStoreInfo();
    }

}
