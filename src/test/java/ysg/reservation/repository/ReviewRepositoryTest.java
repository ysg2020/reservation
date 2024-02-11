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
import ysg.reservation.entity.ReviewEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.type.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("[ReviewRepository] 매장 리뷰 저장")
    @Rollback(value = false)
    void 매장리뷰저장() {
        //given
        //저장하기 전의 예약 고유번호 가져오기
        ReviewEntity review = reviewRepository.findTopByOrderByRIDXDesc()
                .orElse(new ReviewEntity());

        ReviewEntity reviewEntity = ReviewEntity.builder()
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
                .TITLE("테스트 리뷰 제목")
                .CNT("테스트 리뷰 내용")
                .STAR(3)
                .WRITE_DATE(LocalDateTime.now())
                .build();
        //when
        reviewRepository.save(reviewEntity);
        //저장후의 예약 고유번호 가져오기
        ReviewEntity savedReview = reviewRepository.findTopByOrderByRIDXDesc()
                .orElseThrow(()->new RuntimeException("저장되어 있는 리뷰가 없습니다"));

        //then
        //저장하기 전의 예약 고유번호에 1을 더한값과 저장후의 예약 고유번호가 같아야 한다
        Assertions.assertEquals(review.getRIDX() + 1,savedReview.getRIDX());

    }

    @Test
    @DisplayName("[ReviewRepository] 매장 리뷰 삭제")
    void 매장리뷰삭제() {
        //given
        int r_idx = 1;
        //when
        reviewRepository.deleteById(r_idx);
        //then
        ReservationException exception = Assertions.assertThrows(ReservationException.class,
                () -> reviewRepository.findById(r_idx)
                        .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_REVIEW)));

        Assertions.assertEquals(exception.getMessage(),"리뷰를 찾을수 없습니다.");
    }


    @Test
    void 특정매장리뷰조회() {
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
        List<ReviewEntity> reviewEntityList = reviewRepository.findBySIDX(storeEntity);

        //then
        Assertions.assertEquals(reviewEntityList.size(),1);

    }



}
