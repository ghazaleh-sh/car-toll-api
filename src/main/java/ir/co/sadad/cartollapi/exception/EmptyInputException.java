package ir.co.sadad.cartollapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * exception baraye voroD haye khali . in exception zamani throw mishe ke voroD ha khali bashd .
 *
 * @author a.nadi
 */
@Getter
public class EmptyInputException extends CarTollException {

    public EmptyInputException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }

    /**
     * dar mvagheie ke code error nabayd 400 bashd
     *
     * @param msg       payam
     * @param errorCode code error
     */
    public EmptyInputException(String msg, HttpStatus errorCode) {
        super(msg, errorCode);
    }
}
