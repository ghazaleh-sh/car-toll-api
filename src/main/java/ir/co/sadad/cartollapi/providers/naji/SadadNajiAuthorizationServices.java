package ir.co.sadad.cartollapi.providers.naji;

/**
 * service for get token and refresh token
 */
public interface SadadNajiAuthorizationServices {

    /**
     * get access token for Naji services
     * <pre>
     *     if token expired , must call refresh token service for refresh access token
     * </pre>
     *
     * @return accessToken
     */
    String getAccessToken();

    /**
     * method for remove token
     * <pre>
     *     if service get 401 , must remove token and get it again
     * </pre>
     */
    void removeToken();

}
