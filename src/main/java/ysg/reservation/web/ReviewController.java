package ysg.reservation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.ReviewDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 추가
    @PostMapping("/addEdit")
    public ReviewDto addReview(@RequestBody ReviewDto reviewDto){
        log.info("[ReviewController] addReservation -> "+reviewDto.toString());
        return reviewService.addReview(reviewDto);
    }

    // 리뷰 수정
    @PutMapping("/addEdit")
    public ReviewDto editReview(@RequestBody ReviewDto reviewDto){
        log.info("[ReviewController] editReservation -> "+reviewDto.toString());
        return reviewService.editReview(reviewDto);
    }

    // 리뷰 삭제
    @DeleteMapping
    public void removeReview(@RequestBody ReviewDto reviewDto){
        log.info("[ReviewController] removeReservation -> "+reviewDto.getV_IDX());
        reviewService.removeReview(reviewDto);
    }

    // 특정 사용자의 리뷰 정보 확인
    @GetMapping("/member")
    public List<ReviewDto> getMemberReview(@RequestBody MemberDto memberDto){
        return reviewService.getMemberReview(memberDto);
    }

    // 특정 매장의 리뷰 정보 확인
    @GetMapping("/store")
    public List<ReviewDto> getStoreReview(@RequestBody StoreDto storeDto){
        return reviewService.getStoreReview(storeDto);
    }
}
