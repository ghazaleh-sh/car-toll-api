package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class WagePaymentResponseDto {
    private String responseCode;
    private String responseMessage;
    private Long requestId;
    private Long paymentId;
    private String wageId;
}
