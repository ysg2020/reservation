package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReviewDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReviewEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 등록
    public ReviewDto addEditReview(ReviewDto reviewDto) {
        log.info("[ReviewService] addEditReview -> "+reviewDto.toString());

        StoreDto storeDto = reviewDto.getS_IDX();
        MemberDto memberDto = reviewDto.getM_IDX();

        //Dto에서 Entity로 변환
        ReviewEntity reservationEntity = ReviewEntity.builder()
                .RIDX(reviewDto.getR_IDX())
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
                .TITLE(reviewDto.getTITLE())
                .CNT(reviewDto.getCNT())
                .STAR(reviewDto.getSTAR())
                .WRITE_DATE(reviewDto.getWRITE_DATE())
                .build();

        return ReviewDto.fromEntity(reviewRepository.save(reservationEntity));
    }

    // 리뷰 삭제
    public void removeReview(int r_idx) {
        log.info("[ReviewService] removeReview -> "+r_idx);
        reviewRepository.deleteById(r_idx);
    }

    // 특정 사용자의 리뷰 정보 확인
    public List<ReviewDto> getMemberReview(MemberDto memberDto) {
        log.info("[ReviewService] getMemberReview -> "+memberDto);
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
        List<ReviewEntity> memberReviewList = reviewRepository.findByMIDX(memberEntity);
        return memberReviewList.stream()
                .map(memberReservation -> ReviewDto.fromEntity(memberReservation))
                .collect(Collectors.toList());
    }

    // 특정 매장의 리뷰 정보 확인
    public List<ReviewDto> getStoreReview(StoreDto storeDto) {
        log.info("[ReviewService] getStoreReview -> "+storeDto);
        // Dto에서 Entity로 변환
        StoreEntity storeEntity = StoreEntity.builder()
                .SIDX(storeDto.getS_IDX())
                .NAME(storeDto.getNAME())
                .LOC(storeDto.getLOC())
                .DES(storeDto.getDES())
                .STAR(storeDto.getSTAR())
                .TABLE_CNT(storeDto.getTABLE_CNT())
                .build();
        List<ReviewEntity> storeReview = reviewRepository.findBySIDX(storeEntity);

        return storeReview.stream()
                .map(storeReservation -> ReviewDto.fromEntity(storeReservation))
                .collect(Collectors.toList());
    }
}
