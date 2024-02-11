package ysg.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.StoreEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Integer> {


    //특정 사용자의 예약 조회
    List<ReservationEntity> findByMIDX(MemberEntity m_idx);

    //특정 매장의 예약 조회
    List<ReservationEntity> findBySIDX(StoreEntity s_idx);

    //가장 마지막의 예약 고유번호 조회
    Optional<ReservationEntity> findTopByOrderByRIDXDesc();

    //특정 매장의 해당 시간대에 성공처리된 예약 조회
    List<ReservationEntity> findByRESERTIMEBetweenAndSIDXAndRESERSTAT(LocalDateTime start_time
                                                            , LocalDateTime end_time, StoreEntity s_idx, String reser_stat);




}
