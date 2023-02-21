package ir.co.sadad.cartollapi.providers.otp;

import ir.co.sadad.cartollapi.dtos.SSOResponseDto;
import ir.co.sadad.cartollapi.exception.SSOException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import reactor.core.publisher.Mono;


/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OTPServicesImpl implements OTPServices {

    @Value(value = "${otp.path}")
    private String otpPath;

    private final WebClient webClient;

    @Override
    @SneakyThrows
    public SSOResponseDto sendOtpMessage(String ssn, String cellPhoneNumber, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("ssn", ssn);


        Mono<SSOResponseDto> otpServiceResponseDtoMono = webClient
                .post()
                .uri(otpPath + cellPhoneNumber)
                .headers(h -> h.addAll(headers))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, res ->
                        res.bodyToMono(SSOResponseDto.class)
                                .handle((error, sink) -> {
                                            log.error("exception in send otp message : {}", error);
                                            sink.error(new SSOException(translateError(error.getError_description()), error.getError_description(), res.statusCode()));
                                        }
                                )
                )
                .onStatus(HttpStatus::is5xxServerError, res ->
                {
                    log.error("send OTP Message : {}", res);
                    throw new SSOException("core.otp.service.exception", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
                })
                .bodyToMono(SSOResponseDto.class);

        return otpServiceResponseDtoMono.block();

    }

    @Override
    @SneakyThrows
    public SSOResponseDto verifyOtp(String ssn,
                                    String cellPhoneNumber,
                                    String otpCode,
                                    String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        headers.set("X-OTP", otpCode);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("ssn", ssn);


        Mono<SSOResponseDto> otpServiceResponseDtoMono = webClient
                .post()
                .uri(otpPath + cellPhoneNumber)
                .headers(h -> h.addAll(headers))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, res ->
                        res.bodyToMono(SSOResponseDto.class)
                                .handle((error, sink) -> {
                                            log.error("exception in verify otp : {}", error);
                                            sink.error(new SSOException(translateError(error.getError_description()), error.getError_description(), res.statusCode()));
                                        }
                                )
                )
                .onStatus(HttpStatus::is5xxServerError, res ->
                {
                    log.error("Verify OTP Message : {}", res);
                    throw new SSOException("core.otp.service.exception", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
                })
                .bodyToMono(SSOResponseDto.class);


        return otpServiceResponseDtoMono.block();
    }


    /**
     * error in otp services is not in persian form, this method will translate errors to persian
     *
     * @param errorDesc error of otp services
     * @return translated
     */
    private String translateError(String errorDesc) {
        switch (errorDesc) {
            case "CELLPHONE_DOESNT_BELONG_TO_SSN":
                return "cell.phone.does.not.belong.to.ssn";
            case "OTP_SENT":
                return "otp_sent";
            case "OTP_IS_INCORRECT":
                return "otp.code.incorrect";
            case "OTP_LIMIT_REACHED_TRY_AGAIN_LATER":
                return "otp.limit.reached";
            default:
                return "otp.unseen.problem";
        }
    }


}
