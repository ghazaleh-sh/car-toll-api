package ir.co.sadad.cartollapi.dtos;

import lombok.Data;

@Data
public class NajiTokenResponseDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Long expires_in;


}
