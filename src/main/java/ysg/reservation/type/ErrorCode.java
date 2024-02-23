package ysg.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_ENOUGH_TABLE("남아있는 테이블이 없습니다."),
    NOT_FOUND_REVIEW("리뷰를 찾을수 없습니다."),
    NOT_SUCCESS_RESERVATION("예약 상태가 성공 처리된 예약이 아닙니다."),
    NOT_TIME_REVIEW("리뷰 작성 기간이 아닙니다"),
    ALREADY_WRITE_REVIEW("이미 리뷰를 작성하셨습니다.");

    private String description;

    ErrorCode(String description){
        this.description = description;
    }
}
