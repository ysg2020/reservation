package ysg.reservation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.repository.ReservationRepository;
import ysg.reservation.type.ReservationCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
public class ReservationServiceTest {

    //@Autowired
    @Mock
    private ReservationRepository reservationRepository;

    //@Autowired
    @InjectMocks
    private ReservationService reservationService;


    @Test
    void 예약가능여부체크() {
        //given
        // 총 6개의 테이블을 가지고 있는 매장에서 2월 9일 4시에 2개의 테이블 예약 시도
        ReservationDto reservationDto = ReservationDto.builder()
                .S_IDX(StoreDto.builder()
                        .S_IDX(1)
                        .NAME("테스트 매장")
                        .LOC("테스트 매장 위치")
                        .DES("테스트 매장 설명")
                        .STAR(3.5)
                        .TABLE_CNT(6)       // 총 6개의 테이블을 가지고 있는 매장
                        .build())
                .M_IDX(MemberDto.builder()
                        .M_IDX(2)
                        .USER_ID("user123")
                        .USER_PWD("pwd123")
                        .NAME("테스터2")
                        .PHONE("01012345678")
                        .GENDER("M")
                        .ROLE("aimin")
                        .build())
                .START_TIME(LocalDateTime.parse("2024-02-07T16:00:00"))
                .RESER_TIME(LocalDateTime.parse("2024-02-07T16:00:00").plusDays(2))
                .TABLE_CNT(2)       //2개의 테이블 예약 시도
                .RESER_STAT(ReservationCode.NONE.getStat())
                .RESER_CHK_TIME(null)
                .END_YN(ReservationCode.NO.getStat())
                .END_TIME(null)
                .build();

        //validateReservation 안에 있는
        //findByRESERTIMEBetweenAndSIDXAndRESERSTAT 메소드의 리턴 되는값 모킹
        // 1번 예약 : 해당 매장에서 2월 9일에 2개의 테이블 예약이 이미 되어있음
        ReservationEntity reser1st = ReservationEntity.builder()
                .RIDX(1)
                .SIDX(StoreEntity.builder()
                        .SIDX(1)
                        .NAME("테스트 매장")
                        .LOC("테스트 매장 위치")
                        .DES("테스트 매장 설명")
                        .STAR(3.5)
                        .TABLE_CNT(6)
                        .build())
                .MIDX(MemberEntity.builder()
                        .MIDX(1)
                        .USER_ID("user123")
                        .USER_PWD("pwd123")
                        .NAME("테스터")
                        .PHONE("01012345678")
                        .GENDER("M")
                        .ROLE("aimin")
                        .build())
                .START_TIME(LocalDateTime.parse("2024-02-07T15:30:00"))
                .RESERTIME(LocalDateTime.parse("2024-02-07T15:30:00").plusDays(2))
                .TABLE_CNT(2)       //2개의 테이블 예약
                .RESERSTAT(ReservationCode.SUCCESS.getStat())
                .RESER_CHK_TIME(LocalDateTime.now())
                .END_YN(ReservationCode.NO.getStat())
                .END_TIME(null)
                .build();

        // 2번 예약 : 해당 매장에서 2월 9일에 3개의 테이블 예약이 이미 되어있음
        ReservationEntity reser2nd = ReservationEntity.builder()
                .RIDX(2)
                .SIDX(StoreEntity.builder()
                        .SIDX(1)
                        .NAME("테스트 매장")
                        .LOC("테스트 매장 위치")
                        .DES("테스트 매장 설명")
                        .STAR(3.5)
                        .TABLE_CNT(6)
                        .build())
                .MIDX(MemberEntity.builder()
                        .MIDX(1)
                        .USER_ID("user123")
                        .USER_PWD("pwd123")
                        .NAME("테스터")
                        .PHONE("01012345678")
                        .GENDER("M")
                        .ROLE("aimin")
                        .build())
                .START_TIME(LocalDateTime.parse("2024-02-07T16:30:00"))
                .RESERTIME(LocalDateTime.parse("2024-02-07T16:30:00").plusDays(2))
                .TABLE_CNT(3)       //3개의 테이블 예약
                .RESERSTAT(ReservationCode.SUCCESS.getStat())
                .RESER_CHK_TIME(LocalDateTime.now())
                .END_YN(ReservationCode.NO.getStat())
                .END_TIME(null)
                .build();
        List<ReservationEntity> reserList = new ArrayList<>();
        reserList.add(reser1st);
        reserList.add(reser2nd);

        //총 6개의 테이블을 가지고 있는 매장에서 예약하려는 시간대에 5개의 테이블 예약되어있음
        given(reservationRepository.findByRESERTIMEBetweenAndSIDXAndRESERSTAT(any(),any(),any(),any()))
                .willReturn(Optional.of(reserList));
        //when
        //then
        //2개의 테이블 예약 시도 >> 실패
        Assertions.assertThrows(RuntimeException.class,()-> reservationService.validateReservation(reservationDto));

    }


    @Test
    @DisplayName("[ReservationService] 매장 예약시간 10분 이전에 도착 실패")
    void 매장10분이전도착실패() {
        //given
        // 파라미터로 넘겨지는 Dto
        // 현재보다 이전인 예약시간으로 도착확인 시도
        ReservationDto reservationDto = ReservationDto.builder()
                .R_IDX(1)
                .S_IDX(StoreDto.builder()
                        .S_IDX(1)
                        .NAME("테스트 매장")
                        .LOC("테스트 매장 위치")
                        .DES("테스트 매장 설명")
                        .STAR(3.5)
                        .TABLE_CNT(6)
                        .build())
                .M_IDX(MemberDto.builder()
                        .M_IDX(1)
                        .USER_ID("user123")
                        .USER_PWD("pwd123")
                        .NAME("테스터")
                        .PHONE("01012345678")
                        .GENDER("M")
                        .ROLE("aimin")
                        .build())
                .START_TIME(LocalDateTime.parse("2024-02-07T23:30:00"))
                .RESER_TIME(LocalDateTime.parse("2024-02-07T23:30:00").plusDays(2))
                .TABLE_CNT(3)
                .RESER_STAT(ReservationCode.SUCCESS.getStat())
                .RESER_CHK_TIME(LocalDateTime.parse("2024-02-07T23:40:00"))
                .END_YN(ReservationCode.NO.getStat())
                .END_TIME(null)
                .build();

        // 리턴되는 값 모킹
        // 10분전에 도착하지 못했음을 예상
        given(reservationRepository.save(any()))
                .willReturn(ReservationEntity.builder()
                        .RIDX(1)
                        .SIDX(StoreEntity.builder()
                                .SIDX(1)
                                .NAME("테스트 매장")
                                .LOC("테스트 매장 위치")
                                .DES("테스트 매장 설명")
                                .STAR(3.5)
                                .TABLE_CNT(6)
                                .build())
                        .MIDX(MemberEntity.builder()
                                .MIDX(1)
                                .USER_ID("user123")
                                .USER_PWD("pwd123")
                                .NAME("테스터")
                                .PHONE("01012345678")
                                .GENDER("M")
                                .ROLE("aimin")
                                .build())
                        .START_TIME(LocalDateTime.parse("2024-02-07T23:30:00"))
                        .RESERTIME(LocalDateTime.parse("2024-02-07T23:30:00").plusDays(2))
                        .TABLE_CNT(2)
                        .RESERSTAT(ReservationCode.REJECT.getStat()) // 도착 실패
                        .RESER_CHK_TIME(LocalDateTime.parse("2024-02-07T23:40:00"))
                        .END_YN(ReservationCode.FAIL.getStat())    // 도착 실패
                        .END_TIME(null)
                        .build());

        // 저장되기 전의 값을 찾기 위해 사용 (실제 검증)
        ArgumentCaptor<ReservationEntity> captor = ArgumentCaptor.forClass(ReservationEntity.class);

        //when
        reservationService.arriveChkReservation(reservationDto);

        //then
        verify(reservationRepository,times(1)).save(captor.capture());
        Assertions.assertEquals(captor.getValue().getRESERSTAT(),"R");
    }

}
