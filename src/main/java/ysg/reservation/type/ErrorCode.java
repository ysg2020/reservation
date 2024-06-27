package ysg.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {

    // 회원
    NOT_FOUND_USERID("해당 아이디가 존재하지 않습니다."),
    NOT_MATCH_PASSWORD("비밀번호가 다릅니다."),
    INVALID_USER_ACCESS("잘못된 사용자로 접근하셨습니다."),
    ALREADY_LOGOUT_USER("이미 로그아웃한 사용자입니다."),
    EXPIRE_TOKEN("토큰이 만료되었습니다."),
    NO_AUTH_TOKEN("권한 정보가 없는 토큰입니다."),


    // 예약
    NOT_ENOUGH_TABLE("남아있는 테이블이 없습니다."),
    NOT_SUCCESS_RESERVATION("예약 상태가 성공 처리된 예약이 아닙니다."),

    // 리뷰
    NOT_FOUND_REVIEW("리뷰를 찾을수 없습니다."),
    NOT_TIME_REVIEW("리뷰 작성 기간이 아닙니다"),
    ALREADY_WRITE_REVIEW("이미 리뷰를 작성하셨습니다.");

    private String description;

    ErrorCode(String description){
        this.description = description;
    }
}
