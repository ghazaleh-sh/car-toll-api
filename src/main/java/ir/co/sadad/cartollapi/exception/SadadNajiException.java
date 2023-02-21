package ir.co.sadad.cartollapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * exceptions for sadad naji
 */
@Getter
public class SadadNajiException extends CarTollException  {

    public SadadNajiException(String message,  HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
