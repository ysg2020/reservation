package ysg.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReviewEntity;
import ysg.reservation.entity.StoreEntity;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {
    //특정 사용자의 예약 조회
    List<ReviewEntity> findByMIDX(MemberEntity m_idx);
    //특정 매장의 예약 조회
    List<ReviewEntity> findBySIDX(StoreEntity s_idx);
    //가장 마지막의 리뷰 고유번호 조회
    Optional<ReviewEntity> findTopByOrderByRIDXDesc();
}
