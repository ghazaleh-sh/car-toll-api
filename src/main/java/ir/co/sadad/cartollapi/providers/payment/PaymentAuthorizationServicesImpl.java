package ir.co.sadad.cartollapi.providers.payment;

import ir.co.sadad.cartollapi.dtos.SSOResponseDto;
import ir.co.sadad.cartollapi.dtos.TokenDto;
import ir.co.sadad.cartollapi.exception.SSOException;
import ir.co.sadad.cartollapi.service.util.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentAuthorizationServicesImpl implements PaymentAuthorizationServices {

    @Value(value = "${payment.wage.sso_path}")
    private String tokenUrl;

    @Value(value = "${payment.wage.client_id}")
    private String client_id;

    @Value(value = "${payment.wage.client_sec}")
    private String client_sec;

    @Value(value = "${payment.wage.scope}")
    private String tokenScope;

    private final WebClient webClient;

    private static TokenDto token;
    private static Long validDate;


    @Override
    public String getAccessToken() {
        if (isTokenShouldBeRefreshed()) {
            refreshToken();
        }
        return "Bearer " + token.getAccess_token();
    }


    static synchronized boolean isTokenShouldBeRefreshed() {
        return token == null
                || validDate == null
                || Empty.isEmpty(token.getAccess_token())
                || (validDate - 2 * 60 * 1000) < System.currentTimeMillis();
    }

    private void refreshToken() {

        try {
            token = webClient
                    .post()
                    .uri(tokenUrl)

                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + client_sec).getBytes()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("scope", tokenScope)
                            .with("grant_type", "client_credentials"))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, res ->
                            res.bodyToMono(SSOResponseDto.class)
                                    .handle((error, sink) -> {
                                        log.error("exception in Token Service of payment : {}", error);
                                        sink.error(new SSOException(error.getError_description(), error.getError_description(), res.statusCode()));
                                    })
                    )
                    .bodyToMono(TokenDto.class)
                    .block();
            if (token != null)
                validDate = System.currentTimeMillis() + token.getExpires_in() * 1000;
        } catch (HttpStatusCodeException e) {
            log.error("refreshToken exception responseBodyAsString " + e.getResponseBodyAsString());
            log.error("exception on calling service", e);
        }
    }
}
