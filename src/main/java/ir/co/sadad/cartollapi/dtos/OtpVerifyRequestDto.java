package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OtpVerifyRequestDto extends OtpRequestDto {

    @NotNull(message = "{opt.code.require}")
    private String otpCode;

    private Long requestId;
}
