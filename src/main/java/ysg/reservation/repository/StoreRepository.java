package ysg.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ysg.reservation.entity.StoreEntity;

import java.util.Optional;
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Integer> {

    Optional<StoreEntity> findByNAME(String storeName);
    Optional<StoreEntity> findTopByOrderBySIDXDesc();



}
