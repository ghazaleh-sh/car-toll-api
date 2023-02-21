package ir.co.sadad.cartollapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
public class SimorqAuthentication {

    @Data
    @Accessors(chain = true)
    public static class Request {

        private String username;
        private String password;
        @JsonProperty("grant_type")
        private String grantType;
        @JsonProperty("client_id")
        private String clientId;
        @JsonProperty("client_secret")
        private String clientSecret;
    }

    @Data
    @Accessors(chain = true)
    public static class Response {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("expires_in")
        private Integer expiresIn;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("scope")
        private String scope;
    }

}
