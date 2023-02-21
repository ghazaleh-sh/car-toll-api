package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ViolationRequestDto {

    /**
     * phone no of owner - for sending request to naji service
     */
    @Pattern(regexp = "^09[0-9]{9}$",  message = "{phone.pattern.not.valid}")
    private String phoneNo;

    @NotBlank(message = "{plate.tag.must.not.be.null}")
    @NotNull(message = "{plate.tag.must.not.be.null}")
    private String plateNo;

    private String paymentId;

    private Long requestId;

    private boolean hasDebit;
}
