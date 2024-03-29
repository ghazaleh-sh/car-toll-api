package ir.co.sadad.cartollapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class RestException extends HttpStatusCodeException {

    private String message;

    public RestException(HttpStatus statusCode) {
        super(statusCode);
    }

    public RestException(HttpStatus statusCode, String message) {

        super(statusCode);
        this.message = message;
    }
}
