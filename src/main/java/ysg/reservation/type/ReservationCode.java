package ysg.reservation.type;

import lombok.Getter;

@Getter
public enum ReservationCode {

    // 예약 상태 값
    SUCCESS("S")
    ,CANCEL("C")
    ,REJECT("R")
    ,NONE("N")

    // 도착 확인 상태 값
    ,YES("Y")
    ,NO("N")
    ,FAIL("F");

    private String stat;    // 상태 값
    ReservationCode(String stat) {
        this.stat = stat;
    }

}
