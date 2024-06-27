package ysg.reservation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.ReviewDto;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.ReviewEntity;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.repository.MemberRepository;
import ysg.reservation.repository.ReservationRepository;
import ysg.reservation.repository.ReviewRepository;
import ysg.reservation.type.ErrorCode;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    ReviewService reviewService;

    @Test
    @DisplayName("리뷰 작성 실패 - 해당 예약이 성공처리가 아닌 경우")
    void addReviewFail_reser_not_success() {
        //given
        ReservationEntity reservation = ReservationEntity.builder()
                .RESERSTAT("N")
                .build();

        ReviewDto review = ReviewDto.builder()
                .R_IDX(1)
                .TITLE("테스트 리뷰")
                .CNT("테스트 리뷰 내용")
                .STAR(5)
                .M_IDX(1)
                .S_IDX(1)
                .build();

        given(reservationRepository.getById(any()))
                .willReturn(reservation);

        //when
        //then
        ReservationException exception = assertThrows(ReservationException.class, () -> reviewService.addReview(review));
        Assertions.assertEquals(ErrorCode.NOT_SUCCESS_RESERVATION.getDescription(),exception.getMessage());
    }
    @Test
    @DisplayName("리뷰 작성 실패 - 이미 리뷰를 작성한 경우")
    void addReviewFail_already_addReview() {
        //given
        ReservationEntity reservation = ReservationEntity.builder()
                .RESERSTAT("S")
                .build();

        ReviewEntity already_addReview = ReviewEntity.builder()
                .VIDX(1)
                .TITLE("이미 작성한 테스트 리뷰")
                .CNT("이미 작성한 테스트 리뷰 내용")
                .STAR(5)
                .MIDX(1)
                .SIDX(1)
                .build();

        ReviewDto review = ReviewDto.builder()
                .R_IDX(1)
                .TITLE("테스트 리뷰")
                .CNT("테스트 리뷰 내용")
                .STAR(5)
                .M_IDX(1)
                .S_IDX(1)
                .build();

        given(reservationRepository.getById(any()))
                .willReturn(reservation);

        given(reviewRepository.findByRIDX(any()))
                .willReturn(Optional.ofNullable(already_addReview));

        //when
        //then
        ReservationException exception = assertThrows(ReservationException.class, () -> reviewService.addReview(review));
        Assertions.assertEquals(ErrorCode.ALREADY_WRITE_REVIEW.getDescription(),exception.getMessage());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 리뷰 작성기간이 아닌 경우")
    void addReviewFail_not_time_addReview() {
        //given
        ReservationEntity reservation = ReservationEntity.builder()
                .RESERSTAT("S")
                .END_TIME(LocalDateTime.parse("2024-06-20T06:00:00"))
                .build();

        ReviewDto review = ReviewDto.builder()
                .R_IDX(1)
                .TITLE("테스트 리뷰")
                .CNT("테스트 리뷰 내용")
                .STAR(5)
                .M_IDX(1)
                .S_IDX(1)
                .build();

        given(reservationRepository.getById(any()))
                .willReturn(reservation);

        given(reviewRepository.findByRIDX(any()))
                .willReturn(Optional.empty());

        //when
        //then
        ReservationException exception = assertThrows(ReservationException.class, () -> reviewService.addReview(review));
        Assertions.assertEquals(ErrorCode.NOT_TIME_REVIEW.getDescription(),exception.getMessage());
    }

}