package ir.co.sadad.cartollapi.providers.naji;

import com.google.gson.Gson;
import ir.co.sadad.cartollapi.dtos.*;
import ir.co.sadad.cartollapi.exception.SadadNajiException;
import ir.co.sadad.cartollapi.exception.UnauthorizedException;
import ir.co.sadad.cartollapi.service.util.HeaderManager;
import ir.co.sadad.cartollapi.service.util.TripleDES;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * {@inheritDoc}
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SadadNajiServicesImpl implements SadadNajiServices {

    private final TripleDES tripleDES;
    private final SadadNajiAuthorizationServices authorizationServices;
    private final WebClient webClient;
    @Value(value = "${sadad_psp_naji.violation_list}")
    private String violationListUrl;

    @Value(value = "${sadad_psp_naji.violation_aggregation}")
    private String violationAggregationUrl;

    @Value(value = "${sadad_psp_naji.violation_image}")
    private String violationImageUrl;

    @Value(value = "${sadad_psp_naji.check_shahkar}")
    private boolean checkShahkar;


    private int counter = 0;


    @Override
    @SneakyThrows
    public ViolationAggregationData getViolationAggregation(String phoneNo,
                                                            String plateNo,
                                                            String ownerSsn) {

        if (counter >= 2) {
            throw new SadadNajiException("inquiry.error.reverse.failed",
                    HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }

        SadadNajiRequestDto sadadNajiRequest = makeBody(phoneNo, plateNo, ownerSsn);

        Mono<ViolationAggregationData> violationAggregationDataMono = null;
        try {
            violationAggregationDataMono = webClient
                    .post()
                    .uri(violationAggregationUrl)
                    .headers(h -> h.addAll(HeaderManager.getMultiHeader(authorizationServices.getAccessToken())))
                    .body(Mono.just(sadadNajiRequest),
                            new ParameterizedTypeReference<SadadNajiRequestDto>() {
                            })
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus == HttpStatus.UNAUTHORIZED,
                            clientResponse -> Mono.error(new UnauthorizedException()))

                    .onStatus(HttpStatus::is4xxClientError, res ->
                            res.bodyToMono(SadadNajiErrorDto.class)
                                    .handle((error, sink) ->
                                            {
                                                sink.error(new SadadNajiException(error.getError().getMessage()
                                                        , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS));

                                            }
                                    )
                    )
                    .onStatus(HttpStatus::is5xxServerError, res ->
                    {
                        throw new SadadNajiException("inquiry.error.reverse.failed", res.statusCode());
                    })
                    .bodyToMono(ViolationAggregationData.class);
            counter = 0;
            return violationAggregationDataMono.block();


        } catch (UnauthorizedException e) {
            authorizationServices.removeToken();
            counter = +1;
            return getViolationAggregation(phoneNo, plateNo, ownerSsn);
        }


    }

    @Override
    @SneakyThrows
    public ViolationListData getViolationList(String phoneNo, String plateNo, String ownerSsn) {
        if (counter >= 2) {
            throw new SadadNajiException("inquiry.error.reverse.failed",
                    HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
        SadadNajiRequestDto sadadNajiRequest = makeBody(phoneNo, plateNo, ownerSsn);
        try {
            Mono<ViolationListData> violationListDataMono = webClient
                    .post()
                    .uri(violationListUrl)
                    .headers(h -> h.addAll(HeaderManager.getMultiHeader(authorizationServices.getAccessToken())))
                    .body(Mono.just(sadadNajiRequest),
                            new ParameterizedTypeReference<SadadNajiRequestDto>() {
                            })
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus == HttpStatus.UNAUTHORIZED,
                            clientResponse -> Mono.error(new UnauthorizedException()))
                    .onStatus(HttpStatus::is4xxClientError, res ->
                            res.bodyToMono(SadadNajiErrorDto.class)
                                    .handle((error, sink) ->
                                            {
                                                sink.error(new SadadNajiException(error.getError().getMessage()
                                                        , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS));

                                            }
                                    )
                    )
                    .onStatus(HttpStatus::is5xxServerError, res ->
                    {
                        throw new SadadNajiException("inquiry.error.reverse.failed", res.statusCode());
                    })
                    .bodyToMono(ViolationListData.class);
            counter = 0;
            return violationListDataMono.block();


        } catch (UnauthorizedException e) {
            authorizationServices.removeToken();
            counter = +1;
            return getViolationList(phoneNo, plateNo, ownerSsn);
        }
    }


    @Override
    @SneakyThrows
    public ViolationImageData getViolationImage(String phoneNo, String plateNo, String ownerSsn, String serialNo) {
        if (counter >= 2) {
            throw new SadadNajiException("inquiry.error.reverse.failed",
                    HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
        SadadNajiViolationImageRequestDto request = new SadadNajiViolationImageRequestDto();
        request.setPhoneNumber(phoneNo);
        request.setNationalCode(ownerSsn);
        request.setPlateNo(plateNo);
        request.setSerialNo(serialNo);
        request.setCheckShahkar(checkShahkar);

        try {
            Mono<ViolationImageData> violationListDataMono = webClient
                    .post()
                    .uri(violationImageUrl)
                    .headers(h -> h.addAll(HeaderManager.getMultiHeader(authorizationServices.getAccessToken())))
                    .body(Mono.just(makeRequest(request)),
                            new ParameterizedTypeReference<SadadNajiRequestDto>() {
                            })
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus == HttpStatus.UNAUTHORIZED,
                            clientResponse -> Mono.error(new UnauthorizedException()))
                    .onStatus(HttpStatus::is4xxClientError, res ->
                            res.bodyToMono(SadadNajiErrorDto.class)
                                    .handle((error, sink) ->
                                            {
                                                sink.error(new SadadNajiException(error.getError().getMessage()
                                                        , HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS));

                                            }
                                    )
                    )
                    .onStatus(HttpStatus::is5xxServerError, res ->
                    {
                        throw new SadadNajiException("inquiry.error.reverse.failed", res.statusCode());
                    })
                    .bodyToMono(ViolationImageData.class);

            counter = 0;
            return violationListDataMono.block();
        } catch (UnauthorizedException e) {
            authorizationServices.removeToken();
            counter = +1;
            return getViolationImage(phoneNo, plateNo, ownerSsn, serialNo);
        }
    }


    private SadadNajiRequestDto makeBody(String phoneNo, String plateNo, String ownerSsn) {
        SadadNajiViolationRequestDto request = new SadadNajiViolationRequestDto();
        request.setPhoneNumber(phoneNo);
        request.setNationalCode(ownerSsn);
        request.setPlateNo(plateNo);
        request.setCheckShahkar(checkShahkar);


        SadadNajiRequestDto sadadNajiRequest = new SadadNajiRequestDto();
        sadadNajiRequest.setRequest(new Gson().toJson(request));
        sadadNajiRequest.setSign(Base64.toBase64String(tripleDES.sign(sadadNajiRequest.getRequest())));
        return sadadNajiRequest;
    }


    private SadadNajiRequestDto makeRequest(SadadNajiViolationImageRequestDto request) {
        SadadNajiRequestDto sadadNajiRequest = new SadadNajiRequestDto();
        sadadNajiRequest.setRequest(new Gson().toJson(request));
        sadadNajiRequest.setSign(Base64.toBase64String(tripleDES.sign(sadadNajiRequest.getRequest())));
        return sadadNajiRequest;
    }
}
