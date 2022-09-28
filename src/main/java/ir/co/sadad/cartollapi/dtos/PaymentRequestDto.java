package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentRequestDto {
    private String id;
    private String alias;
    private String accountId;
    private Integer instructedAmount;
    private String instructedCurrency;
    private Integer merchantId;
    private Integer terminalId;
    private String paymentReference;
    private String paymentDescription;
}
