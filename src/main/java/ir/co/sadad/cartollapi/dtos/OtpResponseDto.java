package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResponseDto {
    private String responseCode;
    private String responseMessage;
    private Long requestId;
}
