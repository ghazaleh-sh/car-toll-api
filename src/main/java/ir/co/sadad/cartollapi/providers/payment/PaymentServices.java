package ir.co.sadad.cartollapi.providers.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.co.sadad.cartollapi.dtos.NajiPaymentDto;
import ir.co.sadad.cartollapi.dtos.PaymentRequestDto;

/**
 * payment services providers
 */
public interface PaymentServices {

    /**
     * payment service for fee payment
     *
     * @param request   request for service
     * @param authToken auth token
     * @return payment response
     */
    NajiPaymentDto.Response paymentService(PaymentRequestDto request, String authToken);

    /**
     * method for tan verify
     *
     * @param request   request
     * @param tanCode   tan code
     * @param authToken auth token
     * @return payment response
     */
    NajiPaymentDto.Response tanVerifyPayment(NajiPaymentDto.PaymentTanRequest request, String tanCode, String authToken);

    /**
     * reverse service
     *
     * @param request   request
     * @param authToken auth token
     * @return is reversed
     */
    NajiPaymentDto.ReverseResponse reverse(NajiPaymentDto.ReverseRequest request, String authToken) throws JsonProcessingException;
}
