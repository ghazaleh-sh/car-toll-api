package ir.co.sadad.cartollapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * exception for otp services
 */
@Getter
public class SSOException extends CarTollException {
    public SSOException(String message, String description, HttpStatus httpStatus) {
        super(message,description,  httpStatus);
    }
    public SSOException(String message, HttpStatus httpStatus) {
        super(message,  httpStatus);
    }
}
