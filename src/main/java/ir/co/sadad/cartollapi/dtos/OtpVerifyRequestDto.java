package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class OtpVerifyRequestDto extends OtpRequestDto {

    @NotNull(message = "{opt.code.require}")
    private String otpCode;

    private Long requestId;
}
