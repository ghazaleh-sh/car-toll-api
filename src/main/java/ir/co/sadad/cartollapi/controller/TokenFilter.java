package ir.co.sadad.cartollapi.controller;

import ir.co.sadad.cartollapi.exception.CarTollException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import static ir.co.sadad.cartollapi.service.util.Constants.SSN;

/**
 * filter for despatch authorization token and made custom  header
 */
@Slf4j
@Component
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (((HttpServletRequest) servletRequest).getRequestURI().contains("car-toll-api/api.html") ||
                ((HttpServletRequest) servletRequest).getRequestURI().contains("swagger") ||
                ((HttpServletRequest) servletRequest).getRequestURI().contains("swagger-resources") ||
                ((HttpServletRequest) servletRequest).getRequestURI().contains("actuator") ||
                ((HttpServletRequest) servletRequest).getRequestURI().contains("v2") ||
                ((HttpServletRequest) servletRequest).getRequestURI().contains("webjars") ||
                ((HttpServletRequest) servletRequest).getRequestURI().contains("v3") ||
                checkSsnInHeader(req)) {
            filterChain.doFilter(servletRequest, servletResponse);

        } else {
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
            String header = ((HttpServletRequest) servletRequest).getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                throw new CarTollException("No JWT token found in request headers", HttpStatus.BAD_REQUEST);
            }

            requestWrapper.addHeader(SSN, getCustomHeader(SSN, header));

            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }


    private String decodeJWTBody(String JWTToken) {
        String[] split_string = JWTToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);

        return new String(base64Url.decode(base64EncodedBody));
    }


    private String getCustomHeader(String headerName, String ticket) {
        final String decodedTicket = decodeJWTBody(ticket);
        String customHeader;
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(decodedTicket));
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            customHeader = jsonObject.getString(headerName);
        } catch (JsonParsingException x) {
            throw new IllegalArgumentException();
        }
        return customHeader;
    }

    private boolean checkSsnInHeader(HttpServletRequest req) {
        return Collections.list(req.getHeaderNames())
                .stream().anyMatch(h -> h.contains(SSN));

    }
}
