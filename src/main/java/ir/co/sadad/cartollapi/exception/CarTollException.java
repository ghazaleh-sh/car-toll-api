package ir.co.sadad.cartollapi.exception;

import ir.co.sadad.cartollapi.dtos.GeneralErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CarTollException extends RuntimeException {

    private final HttpStatus httpStatus;
    private Integer code;
    private GeneralErrorResponse generalErrorResponse;
    private String description;

    public CarTollException(String message, Integer code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public CarTollException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CarTollException(String message,String description, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.description=description;
    }

    public CarTollException(GeneralErrorResponse generalErrorResponse, HttpStatus httpStatus) {
        this.generalErrorResponse = generalErrorResponse;
        this.httpStatus = httpStatus;
    }
}
