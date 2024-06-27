package ysg.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ReservationAbstractException.class)
    public ResponseEntity<ErrorResponse> reservationAbstractException(ReservationAbstractException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> usernamePasswordException(BadCredentialsException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("아이디가 존재하지 않거나 비밀번호가 다릅니다.")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(HttpStatus.BAD_REQUEST.value()));
    }


}
