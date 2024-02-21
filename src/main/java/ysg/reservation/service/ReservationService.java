package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.repository.MemberRepository;
import ysg.reservation.repository.ReservationRepository;
import ysg.reservation.repository.StoreRepository;
import ysg.reservation.type.ErrorCode;
import ysg.reservation.type.ReservationCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;



    // 예약 요청
    @Transactional
    @CacheEvict(key = "#reservationDto.S_IDX", value = "storeReservation") // 예약 요청시 해당 매장 캐시 삭제
    public ReservationDto addEditReservation(ReservationDto reservationDto) {
        log.info("[ReservationService] addEditReservation -> " + reservationDto.toString());
        StoreEntity store = storeRepository.getById(reservationDto.getS_IDX());
        MemberEntity member = memberRepository.getById(reservationDto.getM_IDX());

        //예약 가능 여부 체크
        validateReservation(reservationDto);

        //Dto 에서 Entity로 변환
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .RIDX(reservationDto.getR_IDX())
                .SIDX(store)
                .MIDX(member)
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
        log.info("[ReservationService] arriveChkReservation -> " + reservationDto.toString());
        // 성공 처리된 예약이어야만 도착 확인 가능
        if (!reservationDto.getRESER_STAT().equals(ReservationCode.SUCCESS.getStat())) {
            throw new ReservationException(ErrorCode.NOT_SUCCESS_RESERVATION);
        }
        // 도착확인 10분전에 도착하지 못한 경우
        if (reservationDto.getRESER_TIME().minusMinutes(10).isBefore(LocalDateTime.now())) {
            // 예약상태 : R(Reject), 도착 여부 : F(Fail) 처리
            reservationDto.setRESER_STAT(ReservationCode.REJECT.getStat());
            reservationDto.setEND_YN(ReservationCode.FAIL.getStat());
        } else {
            reservationDto.setEND_YN(ReservationCode.YES.getStat());
            reservationDto.setEND_TIME(LocalDateTime.now());
        }

        StoreEntity store = storeRepository.getById(reservationDto.getS_IDX());
        MemberEntity member = memberRepository.getById(reservationDto.getM_IDX());

        //Dto 에서 Entity로 변환
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .RIDX(reservationDto.getR_IDX())
                .SIDX(store)
                .MIDX(member)
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
        log.info("[ReservationService] removeReservation -> " + r_idx);
        reservationRepository.deleteById(r_idx);
    }

    // 예약 가능 여부 체크
    public void validateReservation(ReservationDto reservationDto) {
        log.info("[ReservationService] validateReservation -> " + reservationDto.toString());
        //예약 하려는 시간, -1시간, +1시간
        LocalDateTime reser_time = reservationDto.getRESER_TIME();
        LocalDateTime reser_time_minus = reser_time.minusHours(1);
        LocalDateTime reser_time_plus = reser_time.plusHours(1);

        StoreEntity store = storeRepository.getById(reservationDto.getS_IDX());

        log.info("findByRESERTIMEBetweenAndSIDXAndRESERSTAT start!!");
        // 특정 매장의 해당 시간대(예약하려는시간의 앞뒤로 1시간씩)에 성공 처리되어 있는 예약 건수 조회
        List<ReservationEntity> reservationEntities = reservationRepository
                .findByRESERTIMEBetweenAndSIDXAndRESERSTAT(reser_time_minus
                        , reser_time_plus, store, ReservationCode.SUCCESS.getStat());
        log.info("findByRESERTIMEBetweenAndSIDXAndRESERSTAT end!!");
        // 성공 처리되어 있는 예약 건수가 없는 경우 (남아 있는 테이블 수를 계산할 필요 없이 예약 가능)
        if (reservationEntities.isEmpty()) {
            return;
        }

        log.info("table calculate!!");
        // 매장에 남아 있는 테이블 계산
        // 성공 처리된 테이블 수
        int succesReserTableCnt = 0;

        // 예약하려는 테이블 수
        int reservationTableCnt = reservationDto.getTABLE_CNT();

        // 매장이 보유하고 있는 테이블 수
        int storeTableCnt = store.getTABLE_CNT();
        // 위에서 조회한 예약 건수들의 테이블 합
        for (ReservationEntity reservation : reservationEntities) {
            succesReserTableCnt += reservation.getTABLE_CNT();
        }

        log.info("succesReserTableCnt:" + succesReserTableCnt);
        log.info("reservationTableCnt:" + reservationTableCnt);
        log.info("storeTableCnt:" + storeTableCnt);
        // 특정 매장의 해당 시간대에 성공 처리되어 있는 예약 건수들의 테이블 합 + 예약하려는 테이블 수 > 매장이 보유하고 있는 테이블 수
        // 예약 불가능
        if (succesReserTableCnt + reservationTableCnt > storeTableCnt) {
            log.info("[ReservationService] 남아있는 테이블이 없습니다.");
            throw new ReservationException(ErrorCode.NOT_ENOUGH_TABLE);
        }

    }

    // 특정 사용자의 예약 정보 확인
    public List<ReservationDto> getMemberReservation(MemberDto memberDto) {
        log.info("[ReservationService] getMemberReservation -> " + memberDto);
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
        List<ReservationEntity> memberReservationList = reservationRepository.findByMIDX(memberEntity);
        return memberReservationList.stream()
                .map(memberReservation -> ReservationDto.fromEntity(memberReservation))
                .collect(Collectors.toList());
    }

    // 특정 매장의 예약 정보 확인
    @Cacheable(key = "#storeDto.S_IDX", value = "storeReservation") // 캐시 저장
    public List<ReservationDto> getStoreReservation(StoreDto storeDto) {
        log.info("[ReservationService] getStoreReservation -> " + storeDto);
        // Dto에서 Entity로 변환
        StoreEntity storeEntity = StoreEntity.builder()
                .SIDX(storeDto.getS_IDX())
                .NAME(storeDto.getNAME())
                .LOC(storeDto.getLOC())
                .DES(storeDto.getDES())
                .STAR(storeDto.getSTAR())
                .TABLE_CNT(storeDto.getTABLE_CNT())
                .build();
        List<ReservationEntity> storeReservationList = reservationRepository.findBySIDX(storeEntity);

        return storeReservationList.stream()
                .map(storeReservation -> ReservationDto.fromEntity(storeReservation))
                .collect(Collectors.toList());

    }
}
