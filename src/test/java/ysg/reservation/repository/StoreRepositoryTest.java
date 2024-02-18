package ysg.reservation.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.StoreEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;


    @Test
    @DisplayName("[StoreRepository] 신규 매장 저장 (점장)")
    @Rollback(value = false)
    void 신규매장저장_점장() {
        //given
        //저장하기 전의 매장 고유번호 가져오기
        StoreEntity store = storeRepository.findTopByOrderBySIDXDesc()
                .orElse(new StoreEntity());
        //저장할 매장 데이터
        StoreEntity testStoreEntity = StoreEntity.builder()
                .NAME("테스트 매장 2")
                .LOC("테스트 매장 위치 2")
                .DES("테스트 매장 설명 2")
                .STAR(3)
                .TABLE_CNT(6)
                .build();
        //when
        //저장
        storeRepository.save(testStoreEntity);
        //저장후의 매장 고유번호 가져오기
        StoreEntity savedStore = storeRepository.findTopByOrderBySIDXDesc()
                .orElseThrow(()->new RuntimeException("매장이 존재하지 않습니다"));

        //then
        //저장하기 전의 매장 고유번호에 1을 더한값과 저장후의 매장 고유번호가 같아야 한다
        Assertions.assertEquals(store.getSIDX() + 1,savedStore.getSIDX());

    }

    @Test
    @DisplayName("[StoreRepository] 기존 매장 수정 (점장)")
    @Rollback(value = false)
    void 기존매장수정_점장() {
        //given
        StoreEntity store = StoreEntity.builder()
                .SIDX(2)
                .NAME("테스트 매장 3")
                .LOC("테스트 매장 위치 3")
                .DES("테스트 매장 설명 3")
                .STAR(3)
                .TABLE_CNT(6)
                .build();
        //when
        StoreEntity updateStore = storeRepository.save(store);
        //then
        StoreEntity selectStore = storeRepository.findById(2)
                .orElseThrow(()-> new RuntimeException("매장이 없음"));

        Assertions.assertEquals(updateStore.getSIDX(),selectStore.getSIDX());

    }

    @Test
    @DisplayName("[StoreRepository] 기존 매장 삭제 (점장)")
    @Rollback(value = false)
    void 기존매장삭제_점장() {
        //given
        int s_idx = 2;
        //when
        storeRepository.deleteById(s_idx);
        //then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () ->  storeRepository.findById(s_idx)
                        .orElseThrow(()-> new RuntimeException("이미 삭제된 매장 입니다.")));

        Assertions.assertEquals(exception.getMessage(),"이미 삭제된 매장 입니다.");

    }


    @Test
    @DisplayName("[StoreRepository] 특정 매장 정보 조회")
    void 특정매장정보조회() {
        //given
        String storeName = "테스트 매장";
        //when
        StoreEntity storeEntity = storeRepository.findByNAME(storeName)
                .orElseThrow(()->new RuntimeException("매장이 존재하지 않습니다"));
        List<ReservationEntity> reservationList = storeEntity.getRIDX();
        //then
        Assertions.assertEquals(storeName, storeEntity.getNAME());

    }




}
