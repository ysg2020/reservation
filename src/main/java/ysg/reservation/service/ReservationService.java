package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    
    private final ReservationRepository reservationRepository;

    // 예약 요청
    public ReservationDto addEditReservation(ReservationDto reservationDto) {
        log.info("[ReservationService] addEditReservation -> "+reservationDto.toString());
        //예약 가능 여부 체크
        validateReservation(reservationDto);

        StoreDto storeDto = reservationDto.getS_IDX();
        MemberDto memberDto = reservationDto.getM_IDX();

        //Dto 에서 Entity로 변환
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .RIDX(reservationDto.getR_IDX())
                .SIDX(StoreEntity.builder()
                        .SIDX(storeDto.getS_IDX())
                        .NAME(storeDto.getNAME())
                        .LOC(storeDto.getLOC())
                        .DES(storeDto.getDES())
                        .STAR(storeDto.getSTAR())
                        .TABLE_CNT(storeDto.getTABLE_CNT())
                        .build())
                .MIDX(MemberEntity.builder()
                        .MIDX(memberDto.getM_IDX())
                        .USER_ID(memberDto.getUSER_ID())
                        .USER_PWD(memberDto.getUSER_PWD())
                        .NAME(memberDto.getNAME())
                        .PHONE(memberDto.getPHONE())
                        .GENDER(memberDto.getGENDER())
                        .ROLE(memberDto.getROLE())
                        .build())
                .START_TIME(reservationDto.getSTART_TIME())
                .RESERTIME(reservationDto.getRESER_TIME())
                .TABLE_CNT(reservationDto.getTABLE_CNT())
                .RESERSTAT(reservationDto.getRESER_STAT())
                .RESER_CHK_TIME(reservationDto.getRESER_CHK_TIME())
                .END_YN(reservationDto.getEND_YN())
                .END_TIME(reservationDto.getEND_TIME())
                .build();

        return ReservationDto.fromEntity(reservationRepository.save(reservationEntity));

    }

    // 매장 도착 확인 체크
    // 이미 성공 처리된 예약 이므로 예약 가능여부 체크 불필요
    public ReservationDto arriveChkReservation(ReservationDto reservationDto) {
        log.info("[ReservationService] arriveChkReservation -> "+reservationDto.toString());
        // 도착확인 10분전에 도착하지 못한 경우
        if(reservationDto.getRESER_TIME().minusMinutes(10).isBefore(LocalDateTime.now())){
            // 예약상태 : C(Cancel), 도착 여부 : F(Fail) 처리
            reservationDto.setRESER_STAT("C");
            reservationDto.setEND_YN("F");
        }else{
            reservationDto.setEND_YN("Y");
            reservationDto.setEND_TIME(LocalDateTime.now());
        }

        // 매장 Dto, 사용자 Dto
        StoreDto storeDto = reservationDto.getS_IDX();
        MemberDto memberDto = reservationDto.getM_IDX();

        //Dto 에서 Entity로 변환
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .RIDX(reservationDto.getR_IDX())
                .SIDX(StoreEntity.builder()
                        .SIDX(storeDto.getS_IDX())
                        .NAME(storeDto.getNAME())
                        .LOC(storeDto.getLOC())
                        .DES(storeDto.getDES())
                        .STAR(storeDto.getSTAR())
                        .TABLE_CNT(storeDto.getTABLE_CNT())
                        .build())
                .MIDX(MemberEntity.builder()
                        .MIDX(memberDto.getM_IDX())
                        .USER_ID(memberDto.getUSER_ID())
                        .USER_PWD(memberDto.getUSER_PWD())
                        .NAME(memberDto.getNAME())
                        .PHONE(memberDto.getPHONE())
                        .GENDER(memberDto.getGENDER())
                        .ROLE(memberDto.getROLE())
                        .build())
                .START_TIME(reservationDto.getSTART_TIME())
                .RESERTIME(reservationDto.getRESER_TIME())
                .TABLE_CNT(reservationDto.getTABLE_CNT())
                .RESERSTAT(reservationDto.getRESER_STAT())
                .RESER_CHK_TIME(reservationDto.getRESER_CHK_TIME())
                .END_YN(reservationDto.getEND_YN())
                .END_TIME(reservationDto.getEND_TIME())
                .build();

        return ReservationDto.fromEntity(reservationRepository.save(reservationEntity));

    }
    // 예약 삭제
    public void removeReservation(int r_idx) {
        log.info("[ReservationService] removeReservation -> "+r_idx);
        reservationRepository.deleteById(r_idx);
    }




    // 예약 가능 여부 체크
    public void validateReservation(ReservationDto reservationDto) {
        log.info("[ReservationService] validateReservation -> "+reservationDto.toString());
        //예약 하려는 시간, -1시간, +1시간
        LocalDateTime reser_time = reservationDto.getRESER_TIME();
        LocalDateTime reser_time_minus = reser_time.minusHours(1);
        LocalDateTime reser_time_plus = reser_time.plusHours(1);

        // 성공 처리된 테이블 수
        int succesReserTableCnt = 0;

        // 예약하려는 테이블 수
        int reservationTableCnt = reservationDto.getTABLE_CNT();

        // 매장이 보유하고 있는 테이블 수
        int storeTableCnt = reservationDto.getS_IDX().getTABLE_CNT();

        // 매장 Dto
        StoreDto storeDto = reservationDto.getS_IDX();

        // 특정 매장의 해당 시간대(예약하려는시간의 앞뒤로 1시간씩)에 성공 처리되어 있는 예약 건수 조회
        List<ReservationEntity> reservationEntities = reservationRepository
                .findByRESERTIMEBetweenAndSIDXAndRESERSTAT(reser_time_minus
                        ,reser_time_plus,StoreEntity.builder()
                                .SIDX(storeDto.getS_IDX())
                                .NAME(storeDto.getNAME())
                                .LOC(storeDto.getLOC())
                                .DES(storeDto.getDES())
                                .STAR(storeDto.getSTAR())
                                .TABLE_CNT(storeDto.getTABLE_CNT())
                                .build(),"S")
                .orElseThrow(()-> new RuntimeException("성공처리된 예약이 없습니다"));

        // 위에서 조회한 예약 건수들의 테이블 합
        for(ReservationEntity reservation : reservationEntities ){
            succesReserTableCnt += reservation.getTABLE_CNT();
        }

        log.info("succesReserTableCnt:" + succesReserTableCnt);
        log.info("reservationTableCnt:" + reservationTableCnt);
        log.info("storeTableCnt:" + storeTableCnt);
        // 특정 매장의 해당 시간대에 성공 처리되어 있는 예약 건수들의 테이블 합 + 예약하려는 테이블 수 > 매장이 보유하고 있는 테이블 수
        // 예약 불가능
        if(succesReserTableCnt + reservationTableCnt > storeTableCnt){
            log.info("[ReservationService] 테이블 수가 부족합니다.");
            throw new RuntimeException("테이블 수가 부족합니다.");
        }

    }


    // 특정 사용자의 예약 정보 확인
    public List<ReservationDto> getMemberReservation(MemberDto memberDto) {
        log.info("[ReservationService] getMemberReservation -> "+memberDto);
        // Dto에서 Entity로 변환
        MemberEntity memberEntity = MemberEntity.builder()
                .MIDX(memberDto.getM_IDX())
                .USER_ID(memberDto.getUSER_ID())
                .USER_PWD(memberDto.getUSER_PWD())
                .NAME(memberDto.getNAME())
                .PHONE(memberDto.getPHONE())
                .GENDER(memberDto.getGENDER())
                .ROLE(memberDto.getROLE())
                .build();
        List<ReservationEntity> memberReservationList = reservationRepository.findByMIDX(memberEntity)
                .orElseThrow(()->new RuntimeException("해당 사용자의 예약 정보가 없습니다"));
        return memberReservationList.stream()
                .map(memberReservation -> ReservationDto.fromEntity(memberReservation))
                .collect(Collectors.toList());
    }

    // 특정 매장의 예약 정보 확인
    public List<ReservationDto> getStoreReservation(StoreDto storeDto) {
        log.info("[ReservationService] getStoreReservation -> "+storeDto);
        // Dto에서 Entity로 변환
        StoreEntity storeEntity = StoreEntity.builder()
                .SIDX(storeDto.getS_IDX())
                .NAME(storeDto.getNAME())
                .LOC(storeDto.getLOC())
                .DES(storeDto.getDES())
                .STAR(storeDto.getSTAR())
                .TABLE_CNT(storeDto.getTABLE_CNT())
                .build();
        List<ReservationEntity> storeReservationList = reservationRepository.findBySIDX(storeEntity)
                .orElseThrow(()->new RuntimeException("해당 매장의 예약 정보가 없습니다"));

        return storeReservationList.stream()
                .map(storeReservation -> ReservationDto.fromEntity(storeReservation))
                .collect(Collectors.toList());
    }
}
