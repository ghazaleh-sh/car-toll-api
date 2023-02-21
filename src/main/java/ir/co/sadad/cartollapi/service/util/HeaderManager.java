package ir.co.sadad.cartollapi.service.util;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

/**
 * this is manager for header , responsibility of this class is for create header for external request , call services
 *
 * @author a.nadi
 */
public class HeaderManager {

    /**
     * method for get multi headers
     * <pre>
     *     containes :
     *     1 . auth header
     *     2 . ticket header
     * </pre>
     *
     * @param token value obj of token
     * @return multi value header
     */
    public static MultiValueMap<String, String> getMultiHeader(String token) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }

    /**
     * get headers for service that has two token . like Payment
     *
     * @param token     client credential token
     * @param xUserAuth portal token
     * @return
     */
    public static MultiValueMap<String, String> getMultiHeader(String token, String xUserAuth) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        headers.add("x-user-authorization", xUserAuth);
        return headers;
    }
}
