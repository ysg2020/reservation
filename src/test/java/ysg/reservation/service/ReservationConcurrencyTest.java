package ysg.reservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.type.ReservationCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
public class ReservationConcurrencyTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약요청동시성테스트() throws InterruptedException {
        // 동시성 테스트 시작;

        // Given
        // 동시성 테스트 준비;
        int numberOfThreads = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // 총 6개 테이블을 보유하고 있는 테스트 매장에서
        // 테스트 매장에 A 사용자가 5개 테이블 예약 요청
        ReservationDto request1 = ReservationDto.builder()
                .S_IDX(1)
                .M_IDX(1)
                .START_TIME(LocalDateTime.parse("2024-02-10T17:00:00"))
                .RESER_TIME(LocalDateTime.parse("2024-02-13T17:00:00"))
                .TABLE_CNT(5)
                .RESER_STAT(ReservationCode.SUCCESS.getStat())
                .RESER_CHK_TIME(null)
                .END_YN("N")
                .END_TIME(null)
                .build();

        // 같은 시간대에 똑같이 테스트 매장의 테이블 5개를 B 사용자가 예약 요청
        ReservationDto request2 = ReservationDto.builder()
                .S_IDX(1)
                .M_IDX(2)
                .START_TIME(LocalDateTime.parse("2024-02-10T17:00:00"))
                .RESER_TIME(LocalDateTime.parse("2024-02-13T17:00:00"))
                .TABLE_CNT(5)
                .RESER_STAT(ReservationCode.SUCCESS.getStat())
                .RESER_CHK_TIME(null)
                .END_YN("N")
                .END_TIME(null)
                .build();

        // When
        // 동시성 테스트 진행;
        service.execute(() -> {
            reservationService.addEditReservation(request1);
            latch.countDown();
        });
        service.execute(() -> {
            reservationService.addEditReservation(request2);
            latch.countDown();
        });
        latch.await();

        // Then
        // 동시성 테스트 결과 검증;
        StoreDto storeDto = StoreDto.builder()
                .S_IDX(1)
                .NAME("테스트 매장")
                .LOC("테스트 매장")
                .DES("테스트 매장")
                .STAR(4)
                .TABLE_CNT(6)
                .build();
        List<ReservationDto> reservations = reservationService.getStoreReservation(storeDto);
    }
}
