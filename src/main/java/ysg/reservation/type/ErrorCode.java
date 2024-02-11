package ysg.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_ENOUGH_TABLE("남아있는 테이블이 없습니다.");

    private String description;

    ErrorCode(String description){
        this.description = description;
    }
}
