package ysg.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ysg.reservation.entity.MemberEntity;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

}
