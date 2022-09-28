package ir.co.sadad.cartollapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimorqService {

//    @Value(value = "${simorq.base.url}")
//    private String simorqBaseUrl;
//
//    @Value(value = "${simorq.authentication.path}")
//    private String authenticationPath;
//
//    @Value(value = "${simorq.summary.path}")
//    private String summaryPath;
//
//    @Value(value = "${simorq.payToll.path}")
//    private String payTollPath;
//
//    @Value(value = "${simorq.payTolls.path}")
//    private String payTollsPath;
//
//    @Value(value = "${simorq.authentication.username}")
//    private String authenticationUsername;
//
//    @Value(value = "${simorq.authentication.password}")
//    private String authenticationPassword;
//
//    @Value(value = "${simorq.authentication.client_id}")
//    private String authenticationClientId;
//
//    @Value(value = "${simorq.authentication.grant_type}")
//    private String authenticationGrantType;
//
//    @Value(value = "${simorq.authentication.client_secret}")
//    private String authenticationClientSecret;
//
//    private final RestTemplate restTemplate;
//
//    private SimorqTokens simorqTokens;
//
//    public SimorqService() {
//        this.simorqTokens = SimorqTokens.getInstance();
//        this.restTemplate = new RestTemplate();
//    }
//
//    private SimorqAuthentication.Response getAccessToken() {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//            map.add("username", authenticationUsername);
//            map.add("password", "&S@aADd@D&");
//            map.add("grant_type", authenticationGrantType);
//            map.add("client_id", authenticationClientId);
//            map.add("client_secret", authenticationClientSecret);
//            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
//            ResponseEntity<SimorqAuthentication.Response> responseEntity = restTemplate.postForEntity(simorqBaseUrl + authenticationPath, entity, SimorqAuthentication.Response.class);
//            SimorqAuthentication.Response response = responseEntity.getBody();
//            simorqTokens.setAccessToken(response.getAccessToken());
//            simorqTokens.setRefreshToken(response.getRefreshToken());
//            return response;
//        } catch (HttpStatusCodeException e) {
//            log.error("simorq authentication (accessToken) exception : " + e.getResponseBodyAsString());
//            return getRefreshToken(simorqTokens.getRefreshToken());
//        }
//    }
//
//    private SimorqAuthentication.Response getRefreshToken(String refreshToken) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//            map.add("refresh_token", refreshToken);
//            map.add("grant_type", REFRESH_TOKEN);
//            map.add("client_id", authenticationClientId);
//            map.add("client_secret", authenticationClientSecret);
//            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
//            ResponseEntity<SimorqAuthentication.Response> responseEntity = restTemplate.postForEntity(simorqBaseUrl + authenticationPath, entity, SimorqAuthentication.Response.class);
//            return responseEntity.getBody();
//        } catch (HttpStatusCodeException e) {
//            log.error("simorq authentication exception: " + e.getResponseBodyAsString());
//            throw new CarTollException("simorq.authentication.exception", e.getStatusCode());
//        }
//    }
//
//    public CarTollSummaryDto.Response summary(String deviceId, Integer plateNo) {
//
//        SimorqAuthentication.Response accessTokenResponse = getAccessToken();
//
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessTokenResponse.getAccessToken());
//            headers.set("traceNumber", String.valueOf(UUID.randomUUID()));
//            headers.set("deviceId", deviceId);
//
//            HttpEntity entity = new HttpEntity(headers);
//
//            ResponseEntity<CarTollSummaryDto.Response> responseEntity =
//                    restTemplate.exchange(simorqBaseUrl + summaryPath + plateNo,
//                            HttpMethod.GET,
//                            entity,
//                            CarTollSummaryDto.Response.class);
//
//            return responseEntity.getBody();
//        } catch (HttpStatusCodeException e) {
//            log.error("simorq summary exception: " + e.getResponseBodyAsString());
//            throw new CarTollException("simorq.summary.exception", e.getStatusCode());
//        }
//
//    }
//
//    public SimorqPayFreewayTollDto.Response PayFreewayToll(String deviceId, Integer plateNo, String billId, String enquiryId, Integer amount) {
//
//        SimorqAuthentication.Response accessTokenResponse = getAccessToken();
//
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessTokenResponse.getAccessToken());
//            headers.set("traceNumber", String.valueOf(UUID.randomUUID()));
//            headers.set("deviceId", deviceId);
//
//            SimorqPayFreewayTollDto.TollRequest body = new SimorqPayFreewayTollDto.TollRequest();
//            body.setBillId(billId);
//            body.setEnquiryId(enquiryId);
//            body.setFreewayAmount(amount);
//            body.setPlateNo(plateNo);
//
//            HttpEntity<SimorqPayFreewayTollDto.TollRequest> entity = new HttpEntity<>(body, headers);
//            ResponseEntity<SimorqPayFreewayTollDto.Response> responseEntity = restTemplate.postForEntity(simorqBaseUrl + payTollPath, entity, SimorqPayFreewayTollDto.Response.class);
//            return responseEntity.getBody();
//        } catch (HttpStatusCodeException e) {
//            log.error("simorq payFreewayToll exception: " + e.getResponseBodyAsString());
//            throw new CarTollException("simorq.payment.exception", e.getStatusCode());
//        }
//
//    }
//
//    public SimorqPayFreewayTollDto.Response PayFreewayTolls(String deviceId, Integer plateNo, List<String> billIds, String enquiryId, Integer amount) {
//
//        SimorqAuthentication.Response accessTokenResponse = getAccessToken();
//
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessTokenResponse.getAccessToken());
//            headers.set("traceNumber", String.valueOf(UUID.randomUUID()));
//            headers.set("deviceId", deviceId);
//
//            SimorqPayFreewayTollDto.TollsRequest body = new SimorqPayFreewayTollDto.TollsRequest();
//            body.setBillIds(billIds);
//            body.setEnquiryId(enquiryId);
//            body.setFreewayAmount(amount);
//            body.setPlateNo(plateNo);
//
//            HttpEntity<SimorqPayFreewayTollDto.TollsRequest> entity = new HttpEntity<>(body, headers);
//            ResponseEntity<SimorqPayFreewayTollDto.Response> responseEntity = restTemplate.postForEntity(simorqBaseUrl + payTollsPath, entity, SimorqPayFreewayTollDto.Response.class);
//            return responseEntity.getBody();
//        } catch (HttpStatusCodeException e) {
//            log.error("simorq payFreewayToll exception: " + e.getResponseBodyAsString());
//            throw new CarTollException("simorq.payment.exception", e.getStatusCode());
//        }
//
//    }
}
