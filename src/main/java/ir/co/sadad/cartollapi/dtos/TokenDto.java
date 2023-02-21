package ir.co.sadad.cartollapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenDto {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
}
