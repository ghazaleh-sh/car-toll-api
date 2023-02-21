package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class WagePaymentRequestDto {

    /**
     * required for violation detail inquiry.
     */
    private Long requestId;

    @Pattern(regexp = "^[0-9]{8,9}$", message = "{plate.no.pattern.not.valid}")
    @NotNull(message = "{plate.no.must.not.be.null}")
    private String plateNo;

    @NotNull(message = "{account.must.not.be.null}")
    @NotBlank(message = "{account.must.not.be.null}")
    private String accountId;

    /**
     * If this field sets true means we want to pay for violation detail inquiry, so @requestId will be mandatory
     * otherwise, for aggregation inquiry this can be false or not set.
     */
    private boolean inquiryDetail;

    private String paymentReference;
    private String paymentDescription;
    private String alias;

}
