package ir.co.sadad.cartollapi.service.util;

import ir.co.sadad.cartollapi.exception.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * service manager for web client
 *
 * @param <T> input type
 * @param <K> output type
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BasicWebClient<T, K> {

    private final WebClient webClient;
    private String serviceUnavailableMessage = "service.unavailable";


    public void setCustomMessage(String message) {
        this.serviceUnavailableMessage = message;
    }

    protected String serviceUnavailableMessage() {
        return this.serviceUnavailableMessage;
    }


    public K doPostService(T body, String headerToken, String urlQueryString, Class<K> responseType) {
        K response = null;
        response = webClient
                .post()
                .uri(urlQueryString)
                .headers(h -> h.addAll(HeaderManager.getMultiHeader(headerToken)))
                .body(Mono.just(body), new ParameterizedTypeReference<T>() {
                })
                .retrieve()
                .onStatus(HttpStatus::isError , res->{
                    throw new RestException(res.statusCode());
                })
                .bodyToMono(responseType)
                .block();
        log.info("response of external service >>>>>>>>>>>>>>>>>> {}", response);
        return response;
    }

}
