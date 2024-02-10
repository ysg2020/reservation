package ysg.reservation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ysg.reservation.dto.MemberDto;
import ysg.reservation.dto.ReservationDto;
import ysg.reservation.dto.StoreDto;
import ysg.reservation.service.ReservationService;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    // 예약 추가
    @PostMapping("/addEdit")
    public ReservationDto addReservation(@RequestBody ReservationDto reservationDto){
        log.info("[ReservationController] addReservation -> "+reservationDto.toString());
        return reservationService.addEditReservation(reservationDto);
    }

    // 예약 수정
    @PutMapping("/addEdit")
    public ReservationDto editReservation(@RequestBody ReservationDto reservationDto){
        log.info("[ReservationController] editReservation -> "+reservationDto.toString());
        return reservationService.addEditReservation(reservationDto);
    }

    // 예약 도착 확인
    @PostMapping("/arrChk")
    public ReservationDto arriveReservation(@RequestBody ReservationDto reservationDto){
        log.info("[ReservationController] arriveReservation -> "+reservationDto.toString());
        return reservationService.arriveChkReservation(reservationDto);
    }

    // 예약 삭제
    @DeleteMapping
    public void removeReservation(@RequestParam int r_idx){
        log.info("[ReservationController] removeReservation -> "+r_idx);
        reservationService.removeReservation(r_idx);
    }

    // 특정 사용자의 예약 정보 확인
    @GetMapping("/member")
    public List<ReservationDto> getStoreInfo(@RequestBody MemberDto memberDto){
        return reservationService.getMemberReservation(memberDto);
    }
    // 특정 매장의 예약 정보 확인
    @GetMapping("/store")
    public List<ReservationDto> getAllReservationInfo(@RequestBody StoreDto storeDto){
        return reservationService.getStoreReservation(storeDto);
    }



}
