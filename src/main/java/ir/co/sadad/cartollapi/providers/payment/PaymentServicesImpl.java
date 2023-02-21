package ir.co.sadad.cartollapi.providers.payment;

import ir.co.sadad.cartollapi.dtos.NajiPaymentDto;
import ir.co.sadad.cartollapi.dtos.PaymentRequestDto;
import ir.co.sadad.cartollapi.exception.RestException;
import ir.co.sadad.cartollapi.service.util.HeaderManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServicesImpl implements PaymentServices {

    @Value(value = "${payment.wage.path}")
    private String wagePaymentUrl;

    @Value(value = "${payment.wage.reverse-path}")
    private String wageReversePaymentUrl;

    @Value(value = "${payment.wage.terminal-id}")
    private String wageTerminalId;

    @Value(value = "${payment.wage.merchant-id}")
    private String wageMerchantId;

    @Value(value = "${payment.wage.instructed-currency}")
    private String wageCurrency;

    private final PaymentAuthorizationServices paymentAuthorizationServices;


    private final WebClient webClient;

    @Override
    @SneakyThrows
    public NajiPaymentDto.Response paymentService(PaymentRequestDto request, String authToken, String userAgent) {
        request.setInstructedCurrency(wageCurrency)
                .setMerchantId(Integer.valueOf(wageMerchantId))
                .setTerminalId(Integer.valueOf(wageTerminalId));

        return webClient
                .post()
                .uri(wagePaymentUrl)
                .headers(h -> h.addAll(HeaderManager.getMultiHeader(paymentAuthorizationServices.getAccessToken(), authToken, userAgent)))
                .body(Mono.just(request), new ParameterizedTypeReference<PaymentRequestDto>() {
                })
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.BAD_REQUEST), res ->
                        res.bodyToMono(NajiPaymentDto.Response.MetaData.class)
                                .log()
                                .handle((error, sink) -> {
                                            log.error("exception in payment : {}", error);
                                            sink.error(new RestException(res.statusCode(), error.getNotifications().get(0).getMessage()));
                                        }
                                )
                ).onStatus(HttpStatus::isError, res -> {
                    log.error("Payment service Message : {}", res);
                    throw new RestException(res.statusCode());
                })
                .bodyToMono(NajiPaymentDto.Response.class)
                .block();

    }

    @Override
    @SneakyThrows
    public NajiPaymentDto.Response tanVerifyPayment(NajiPaymentDto.PaymentTanRequest request, String tanCode, String authToken) {
        return webClient
                .post()
                .uri(wagePaymentUrl + "?authorizationCode=" + tanCode)
                .headers(h -> h.addAll(HeaderManager.getMultiHeader(paymentAuthorizationServices.getAccessToken(), authToken)))
                .body(Mono.just(request), new ParameterizedTypeReference<NajiPaymentDto.PaymentTanRequest>() {
                })
                .retrieve()
                .onStatus(HttpStatus::isError, res -> {
                    log.error("exception in tan verify : {}", res);
                    throw new RestException(res.statusCode());
                })
                .bodyToMono(NajiPaymentDto.Response.class)
                .block();
    }

    @Override
    @SneakyThrows
    public NajiPaymentDto.ReverseResponse reverse(NajiPaymentDto.ReverseRequest request, String authToken) {

        return webClient
                .post()
                .uri(wageReversePaymentUrl)
                .headers(h -> h.addAll(HeaderManager.getMultiHeader(paymentAuthorizationServices.getAccessToken(), authToken)))
                .body(Mono.just(request), new ParameterizedTypeReference<NajiPaymentDto.ReverseRequest>() {
                })
                .retrieve()
                .onStatus(HttpStatus::isError, res -> {
                    res.toEntity(String.class).subscribe(entity -> log.error("exception in reverse : {}", entity));
                    throw new RestException(res.statusCode());
                })
                .bodyToMono(NajiPaymentDto.ReverseResponse.class)
                .block();

    }
}
