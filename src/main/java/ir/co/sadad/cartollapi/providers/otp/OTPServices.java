package ir.co.sadad.cartollapi.providers.otp;

import ir.co.sadad.cartollapi.dtos.SSOResponseDto;

/**
 * service provider for otp service
 * <pre>
 *     this services will get user phone and national code of user
 *     check authentication of user
 *     check SHAHKAR service
 *     send sms to user
 *     verify of sms
 * </pre>
 */
public interface OTPServices {

    /**
     * method for send otp message
     *
     * @param ssn             ssn of user
     * @param cellPhoneNumber cellPhone of user
     * @return response of message
     */
    SSOResponseDto sendOtpMessage(String ssn, String cellPhoneNumber, String token);

    /**
     * method for verify sms of user
     *
     * @param ssn             ssn of user
     * @param cellPhoneNumber phone number of user
     * @param otpCode         otp code that send in first method
     * @param token
     * @return is user verify or not
     */
    SSOResponseDto verifyOtp(String ssn, String cellPhoneNumber, String otpCode, String token);

}
