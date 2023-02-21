package ir.co.sadad.cartollapi.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class GeneralErrorResponse {

    private HttpStatus status;
    private Long timestamp;
    private String code;
    private String message;
    private String localizedMessage;
    private List<SubError> subErrors = new ArrayList<>();

    @Getter
    @Setter
    public static class SubError {
        private Long timestamp;
        private String code;
        private String message;
        private String localizedMessage;
    }

}
