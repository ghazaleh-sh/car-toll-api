package ir.co.sadad.cartollapi.providers.naji;


import ir.co.sadad.cartollapi.dtos.NajiTokenResponseDto;
import ir.co.sadad.cartollapi.dtos.SadadNajiErrorDto;
import ir.co.sadad.cartollapi.exception.CarTollException;
import ir.co.sadad.cartollapi.exception.SadadNajiException;
import ir.co.sadad.cartollapi.service.util.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static ir.co.sadad.cartollapi.service.util.Constants.GRANT_TYPE_PASSWORD;
import static ir.co.sadad.cartollapi.service.util.Constants.GRANT_TYPE_REFRESH;

/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SadadNajiAuthorizationServicesImpl implements SadadNajiAuthorizationServices {


    private static NajiTokenResponseDto token;
    /**
     * valid date is in second
     */
    private static Long validDate;


    @Value(value = "${sadad_psp_naji.access_token}")
    private String tokenUrl;
    @Value(value = "${sadad_psp_naji.user_name}")
    private String userName;
    @Value(value = "${sadad_psp_naji.password}")
    private String password;
    @Value(value = "${sadad_psp_naji.client_id}")
    private String clientId;
    @Value(value = "${sadad_psp_naji.client_secret}")
    private String clientSec;

    private final WebClient webClient;


    @Override
    public String getAccessToken() {
        if (isTokenEmpty()) {
            getToken();
        } else if (isTokenShouldBeRefreshed()) {
            refreshToken();
        }
        return token.getToken_type() + " " + token.getAccess_token();
    }

    @Override
    public void removeToken() {
        token = new NajiTokenResponseDto();
    }

    /**
     * should refresh in 2 min before expire
     * <pre>
     *     compare 2 dates in second
     * </pre>
     *
     * @return
     */
    static synchronized boolean isTokenShouldBeRefreshed() {
        return (validDate - 2 * 60) < System.currentTimeMillis() / 1000;
    }

    static synchronized boolean isTokenEmpty() {
        return token == null
                || validDate == null
                || Empty.isEmpty(token.getAccess_token());
    }


    private void getToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("Username", userName);
            requestBody.add("Password", password);
            requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSec);
            callService(headers, requestBody);

        } catch (Exception e) {
            log.error("token SERVICE ERROR IS >>>>>>>>> " + e);
            throw new CarTollException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void refreshToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("refresh_token", token.getRefresh_token());
            requestBody.add("grant_type", GRANT_TYPE_REFRESH);
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSec);
            callService(headers, requestBody);

        } catch (Exception e) {
            log.error("token SERVICE ERROR IS >>>>>>>>> " + e);
            throw new CarTollException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void callService(HttpHeaders headers, MultiValueMap<String, String> requestBody)  {


        token = webClient
                .post()
                .uri(tokenUrl)
                .headers(h -> h.addAll(headers))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .onStatus(HttpStatus::isError, res ->
                        res.bodyToMono(SadadNajiErrorDto.class)
                                .handle((error, sink) ->
                                        sink.error(new SadadNajiException(error.getError().getMessage()
                                                , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS))
                                )
                )
                .bodyToMono(NajiTokenResponseDto.class)
                .block();

        validDate = System.currentTimeMillis() / 1000 + token.getExpires_in();

    }

}
