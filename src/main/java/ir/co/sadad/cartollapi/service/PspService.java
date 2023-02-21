package ir.co.sadad.cartollapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PspService {

//    @Value(value = "${sadad.psp.base-url}")
//    private String sadadPspBaseUri;
//
//    @Value(value = "${sadad.psp.payment.init-address}")
//    private String sadadPspPaymentInitAddress;
//
//    @Value(value = "${sadad.psp.payment.verify-address}")
//    private String sadadPspPaymentVerifyAddress;
//
//    @Value(value = "${sadad.psp.merchant-id}")
//    private String merchantId;
//
//    @Value(value = "${sadad.psp.terminal-id}")
//    private String terminalId;
//
//    @Value(value = "${sadad.psp.return-url}")
//    private String returnUrl;
//
//    private final RestTemplate restTemplate;
//    private final Utility utility;
//
//
//    public PspPaymentRequestDto.Response paymentRequest(Long orderId, Long totalAmount) throws ServiceUnavailableException {
//
//        PspPaymentRequestDto.Request paymentRequestDtoRequest = new PspPaymentRequestDto.Request();
//        paymentRequestDtoRequest.setMerchantId(merchantId);
//        paymentRequestDtoRequest.setTerminalId(terminalId);
//        paymentRequestDtoRequest.setAmount(totalAmount);
//        paymentRequestDtoRequest.setOrderId(orderId);
//        paymentRequestDtoRequest.setLocalDateTime(LocalDateTime.now());
//        paymentRequestDtoRequest.setReturnUrl(returnUrl);
//        paymentRequestDtoRequest.setSignData(utility.getPaymentRequestSignData(terminalId,
//                orderId.toString(), totalAmount.toString()));
//
//        paymentRequestDtoRequest.setAdditionalData("*");
//        paymentRequestDtoRequest.setMultiplexingData(null);
//        paymentRequestDtoRequest.setUserId(0L);
//
//        ResponseEntity<PspPaymentRequestDto.Response> paymentRequestDtoResponse = restTemplate.postForEntity(sadadPspBaseUri + sadadPspPaymentInitAddress,
//                paymentRequestDtoRequest, PspPaymentRequestDto.Response.class);
//
//        if (paymentRequestDtoResponse.getStatusCode() == HttpStatus.OK) {
//            return paymentRequestDtoResponse.getBody();
//        } else {
//            throw new ServiceUnavailableException("payment.request.unavailable");
//        }
//    }
//
//    public PspVerifyDto.Response verify(String token) throws ServiceUnavailableException {
//
//        PspVerifyDto.Request verifyRequest = new PspVerifyDto.Request();
//        verifyRequest.setToken(token);
//        verifyRequest.setSignData(utility.getVerifySignData(token));
//
//        ResponseEntity<PspVerifyDto.Response> verifyResponse = restTemplate.postForEntity(sadadPspBaseUri + sadadPspPaymentVerifyAddress,
//                verifyRequest, PspVerifyDto.Response.class);
//
//        if (verifyResponse.getStatusCode() == HttpStatus.OK) {
//            return verifyResponse.getBody();
//        } else {
//            throw new ServiceUnavailableException("psp.verify.unavailable");
//        }
//    }

}
