package ir.co.sadad.cartollapi.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * response of verify service of OTP
 */
@Data
@Accessors(chain = true)
public class OtpServiceVerifyResponseDto {
    private String accessToken;

}
