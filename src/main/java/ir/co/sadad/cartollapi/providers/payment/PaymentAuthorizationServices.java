package ir.co.sadad.cartollapi.providers.payment;

/**
 * service for get token based on client of fee payment
 * <pre>
 *     client of fee : this client has limit for tan services , and used only in payment services
 * </pre>
 */
public interface PaymentAuthorizationServices {


    /**
     * get access token for fee services
     * <pre>
     *     if token expired , must call refresh token service for refresh access token
     * </pre>
     *
     * @return accessToken
     */
    String getAccessToken();
}
