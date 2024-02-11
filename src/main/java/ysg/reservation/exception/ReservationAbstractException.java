package ysg.reservation.exception;

public abstract class ReservationAbstractException extends RuntimeException{

    abstract public int getStatusCode();
    abstract public String getMessage();
}
