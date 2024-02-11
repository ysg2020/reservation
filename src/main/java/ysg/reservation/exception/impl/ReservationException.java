package ysg.reservation.exception.impl;

import org.springframework.http.HttpStatus;
import ysg.reservation.exception.ReservationAbstractException;
import ysg.reservation.type.ErrorCode;

public class ReservationException extends ReservationAbstractException {
    private ErrorCode errorCode;
    private String errorMessage;

    public ReservationException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
