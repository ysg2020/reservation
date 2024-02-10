package ysg.reservation.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.type.ReservationCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("[ReservationRepository] 매장 예약 저장")
    @Rollback(value = false)
    void 매장예약저장() {
        //given
        //저장하기 전의 예약 고유번호 가져오기
        ReservationEntity reservation = reservationRepository.findTopByOrderByRIDXDesc()
                .orElse(new ReservationEntity());
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .SIDX(StoreEntity.builder()
                        .SIDX(1)
                        .NAME("테스트 매장")
                        .LOC("테스트 매장 위치")
                        .DES("테스트 매장 설명")
                        .STAR(3.5)
                        .TABLE_CNT(6)
                        .build())
                .MIDX(MemberEntity.builder()
                        .MIDX(2)
                        .USER_ID("user123")
                        .USER_PWD("pwd123")
                        .NAME("테스터2")
                        .PHONE("01012345678")
                        .GENDER("M")
                        .ROLE("aimin")
                        .build())
                .START_TIME(LocalDateTime.now())
                .RESERTIME(LocalDateTime.now().plusDays(2))
                .TABLE_CNT(2)
                .RESERSTAT(ReservationCode.NONE.getStat())
                .RESER_CHK_TIME(null)
                .END_YN(ReservationCode.NO.getStat())                // 도착유무 수정 (사용자가 결정하는 요소)
                .END_TIME(null)             // 도착시간 세팅 (사용자가 결정하는 요소)
                .build();
        //when
        reservationRepository.save(reservationEntity);

        //저장후의 예약 고유번호 가져오기
        ReservationEntity savedReservation = reservationRepository.findTopByOrderByRIDXDesc()
                .orElseThrow(()->new RuntimeException("저장되어 있는 예약이 없습니다"));

        //then
        //저장하기 전의 예약 고유번호에 1을 더한값과 저장후의 예약 고유번호가 같아야 한다
        Assertions.assertEquals(reservation.getRIDX() + 1,savedReservation.getRIDX());
    }

    @Test
    @DisplayName("[ReservationRepository] 매장 예약 수정 (점장)")
    @Rollback(value = false)
    void 매장예약수정_점장() {
        //given
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .RIDX(6)
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
                .TABLE_CNT(2)
                .RESERSTAT(ReservationCode.SUCCESS.getStat())                               // 예약상태 값 수정 (점장이 수정하는 요소)
                .RESER_CHK_TIME(LocalDateTime.now())           // 예약확인시간 수정 (점장이 수정하는 요소)
                .END_YN(ReservationCode.NO.getStat())
                .END_TIME(null)
                .build();

        //when
        reservationRepository.save(reservationEntity);

    }


    @Test
    @DisplayName("[ReservationRepository] 매장 예약 삭제")
    @Rollback(value = false)
    void 매장예약삭제() {
        //given
        int r_idx = 12;
        //when
        reservationRepository.deleteById(r_idx);
        //then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () ->  reservationRepository.findById(r_idx)
                        .orElseThrow(()-> new RuntimeException("이미 삭제된 예약 입니다.")));

        Assertions.assertEquals(exception.getMessage(),"이미 삭제된 예약 입니다.");

    }

    @Test
    @DisplayName("[ReservationRepository] 매장 예약 조회")
    void 매장예약조회() {
        //given
        StoreEntity storeEntity = StoreEntity.builder()
                .SIDX(1)
                .NAME("테스트 매장")
                .LOC("테스트 매장 위치")
                .DES("테스트 매장 설명")
                .STAR(3.5)
                .TABLE_CNT(6)
                .build();
        //when
        List<ReservationEntity> reservationList = reservationRepository.findBySIDX(storeEntity)
                .orElseThrow(()-> new RuntimeException("저장되어 있는 예약이 없습니다"));
        //then
        Assertions.assertEquals(reservationList.size(),14); //현재 14건 추가해놓음

    }



    @Test
    @DisplayName("[ReservationRepository] ")
    void 매장예약시간사이조회() {
        //given
        LocalDateTime reser_time = LocalDateTime.parse("2024-02-09T16:00:00");
        
        //when
        LocalDateTime reser_time_minus = reser_time.minusHours(1);
        LocalDateTime reser_time_plus = reser_time.plusHours(1);
        List<ReservationEntity> reservationBetween = reservationRepository
                .findByRESERTIMEBetween(reser_time_minus, reser_time_plus)
                .orElseThrow(()-> new RuntimeException("해당 기간에 예약이 없습니다."));        
        //then

    }
    
    
    @Test
    void 성공처리된예약건들의테이블수조회() {
        //given
        LocalDateTime reser_time = LocalDateTime.parse("2024-02-09T16:00:00");

        //when
        LocalDateTime reser_time_minus = reser_time.minusHours(1);
        LocalDateTime reser_time_plus = reser_time.plusHours(1);
        List<ReservationEntity> reservationEntities = reservationRepository
                .findByRESERTIMEBetweenAndSIDXAndRESERSTAT(reser_time_minus,reser_time_plus,StoreEntity.builder()
                        .SIDX(1)
                        .NAME("테스트 매장")
                        .LOC("테스트 매장 위치")
                        .DES("테스트 매장 설명")
                        .STAR(3.5)
                        .TABLE_CNT(6)
                        .build(),"S")
                .orElseThrow(()-> new RuntimeException("성공처리된 예약이 없습니다"));

        int result = 0;
        //then
        for(ReservationEntity reservation : reservationEntities ){
            result += reservation.getTABLE_CNT();
        }
        Assertions.assertEquals(6,result);

    }

}
