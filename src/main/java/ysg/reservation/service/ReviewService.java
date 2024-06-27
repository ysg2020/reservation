package ysg.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ysg.reservation.auth.MemberAuthUtil;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReviewDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.entity.MemberEntity;
import ysg.reservation.entity.ReservationEntity;
import ysg.reservation.entity.ReviewEntity;
import ysg.reservation.entity.StoreEntity;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.repository.MemberRepository;
import ysg.reservation.repository.ReservationRepository;
import ysg.reservation.repository.ReviewRepository;
import ysg.reservation.repository.StoreRepository;
import ysg.reservation.type.ErrorCode;
import ysg.reservation.type.ReservationCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    // 리뷰 등록
    public ReviewDto addReview(ReviewDto reviewDto) {
        log.info("[ReviewService] addReview -> "+reviewDto.toString());
        // 파라미터로 받아온 예약 고유번호를 프록시 객체로 반환
        ReservationEntity reservation = reservationRepository.getById(reviewDto.getR_IDX());

        //리뷰 작성 가능 여부 체크
        validateReview(reservation);

        //Dto에서 Entity로 변환
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .VIDX(reviewDto.getV_IDX())
                .RIDX(reservation)
                .SIDX(reviewDto.getS_IDX())
                .MIDX(reviewDto.getM_IDX())
                .TITLE(reviewDto.getTITLE())
                .CNT(reviewDto.getCNT())
                .STAR(reviewDto.getSTAR())
                .WRITE_DATE(reviewDto.getWRITE_DATE())
                .build();

        return ReviewDto.fromEntity(reviewRepository.save(reviewEntity));
    }

    // 리뷰 수정
    public ReviewDto editReview(ReviewDto reviewDto) {
        log.info("[ReviewService] editReview -> "+reviewDto.toString());
        // 파라미터로 받아온 예약 고유번호를 프록시 객체로 반환
        ReservationEntity reservation = reservationRepository.getById(reviewDto.getR_IDX());

        // 리뷰 작성자 조회
        MemberEntity member = memberRepository.findById(reviewDto.getM_IDX())
                .orElseThrow(()-> new ReservationException(ErrorCode.NOT_FOUND_USERID));

        // 로그인한 사용자 체크
        MemberAuthUtil.loginUserCheck(member.getUSERID());

        //Dto에서 Entity로 변환
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .VIDX(reviewDto.getV_IDX())
                .RIDX(reservation)
                .SIDX(reviewDto.getS_IDX())
                .MIDX(reviewDto.getM_IDX())
                .TITLE(reviewDto.getTITLE())
                .CNT(reviewDto.getCNT())
                .STAR(reviewDto.getSTAR())
                .WRITE_DATE(reviewDto.getWRITE_DATE())
                .build();

        return ReviewDto.fromEntity(reviewRepository.save(reviewEntity));
    }

    // 리뷰 작성 가능 여부 체크
    private void validateReview(ReservationEntity reservation) {
        // 해당 예약이 성공 처리가 아닌 경우 (리뷰 작성 불가)
        if(!reservation.getRESERSTAT().equals(ReservationCode.SUCCESS.getStat())){
            throw new ReservationException(ErrorCode.NOT_SUCCESS_RESERVATION);

        }
        // 반환 받은 프록시 객체(reservation)의 예약 고유 번호로 리뷰 조회
        Optional<ReviewEntity> writeReview = reviewRepository.findByRIDX(reservation);

        // 해당 예약 고유번호로 리뷰를 이미 작성한 경우
        if(!writeReview.isEmpty()){
            throw new ReservationException(ErrorCode.ALREADY_WRITE_REVIEW);

        }

        // 매장 도착 시간에서 한시간을 더한 시간 = 리뷰 오픈 시간
        LocalDateTime reviewStartTime = reservation.getEND_TIME().plusHours(1);

        // 리뷰를 작성 할 수 있는 최소 시간에 5일을 더한 시간 = 리뷰 마감 시간
        LocalDateTime reviewEndTime = reviewStartTime.plusDays(5);

        // 리뷰는 매장 도착 시간에서 한시간이 지나야 작성 가능하고 5일 지난뒤엔 리뷰 작성 불가
        if(reviewStartTime.isAfter(LocalDateTime.now()) || reviewEndTime.isBefore(LocalDateTime.now())){
            throw new ReservationException(ErrorCode.NOT_TIME_REVIEW);

        }
    }

    // 리뷰 삭제
    public void removeReview(ReviewDto reviewDto) {
        log.info("[ReviewService] removeReview -> "+reviewDto.getV_IDX());
        // 리뷰 작성자 조회
        MemberEntity member = memberRepository.findById(reviewDto.getM_IDX())
                .orElseThrow(()-> new ReservationException(ErrorCode.NOT_FOUND_USERID));
        // 로그인한 사용자 확인
        MemberAuthUtil.loginUserCheck(member.getUSERID());

        reviewRepository.deleteById(reviewDto.getV_IDX());
    }

    // 특정 사용자의 리뷰 정보 확인
    public List<ReviewDto> getMemberReview(MemberDto memberDto) {
        log.info("[ReviewService] getMemberReview -> "+memberDto);
        List<ReviewEntity> memberReservationList = reviewRepository.findByMIDX(memberDto.getM_IDX());

        return memberReservationList.stream()
                .map(memberReservation -> ReviewDto.fromEntity(memberReservation))
                .collect(Collectors.toList());
    }

    // 특정 매장의 리뷰 정보 확인
    public List<ReviewDto> getStoreReview(StoreDto storeDto) {
        log.info("[ReviewService] getStoreReview -> "+storeDto);
        // Dto에서 Entity로 변환
        List<ReviewEntity> storeReview = reviewRepository.findBySIDX(storeDto.getS_IDX());

        return storeReview.stream()
                .map(storeReservation -> ReviewDto.fromEntity(storeReservation))
                .collect(Collectors.toList());
    }
}
