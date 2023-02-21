package ir.co.sadad.cartollapi.service;

import ir.co.sadad.cartollapi.dtos.*;

import javax.validation.Valid;

/**
 * service for inquiry
 */
public interface InquiryService {


    /**
     * <p>
     * service for get aggregation violation from Naji Services
     * Based on user has debitCount or not, checks the data
     * then, naji inquiry service calls and if success, debitCount decreases
     * otherwise, if user normally requests inquiry (means has not debitCount and paid the wage) reverse service calls to back the payment money
     * </p>
     *
     * @param request request
     * @return violation aggregation
     */
    ViolationAggregationResponseDto getAggregationViolation(@Valid ViolationRequestDto request, String ssn, String authToken);

    /**
     * service for get violation list form naji service
     * inherit from @getAggregationViolation
     *
     * @param request request
     * @return violation list
     */
    ViolationListResponseDto getViolationList(@Valid ViolationRequestDto request, String ssn, String authToken);

    /**
     * get image of violation based on serial
     *
     * @param request request
     * @return image of violation
     */
    ViolationImageResponseDto getViolationImage(@Valid ViolationImageRequestDto request, String ssn);

    /**
     * if request is inquiry detail, otp has been send and success response is 200
     * if plate belongs to the user or aggregation inquiry, otp request service will not call and response code is 202
     *
     * @param otpReq
     * @param ssn
     * @return Id of created request, responseCode and message
     */
    OtpResponseDto otpRequestService(OtpRequestDto otpReq, String ssn,String token);

    /**
     * for verifying otpCode which sent by @otpRequestService
     *
     * @param otpReq
     * @param ssn
     * @param token
     * @return Id of created request in @otpRequestService, responseCode and message
     */
    OtpResponseDto otpVerifyService(OtpVerifyRequestDto otpReq, String ssn,String token);

    /**
     * <p>
     * If plate belongs to the user or aggregation inquiry, we don't have any otp and request creates here.
     * Otherwise, we need requestId which get through otpService with OTP_SUCCESS status
     * </p>
     * Then payment core service calls and if success, debitCount of user increases and payment record creates
     *
     * @param request
     * @param ssn
     * @param authToken
     * @return
     */
    WagePaymentResponseDto wagePayment(WagePaymentRequestDto request, String ssn, String authToken);

    /**
     * verify of tan code that send to user
     *
     * @param request   body request
     * @param ssn       ssn of user
     * @param authToken token
     * @return payment result
     */
    WagePaymentResponseDto tanVerifyWagePayment(TanRequestDto request, String ssn, String authToken);

}
