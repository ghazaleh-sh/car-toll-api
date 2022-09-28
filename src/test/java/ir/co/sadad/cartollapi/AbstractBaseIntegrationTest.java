package ir.co.sadad.cartollapi;


import ir.co.sadad.cartollapi.dtos.TokenDto;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@ActiveProfiles(profiles = {"dev"})
@TestPropertySource(locations = "classpath:application-dev.yml")
public abstract class AbstractBaseIntegrationTest {

    protected WebTestClient webTestClient;
    protected static TokenDto clientToken;

    public AbstractBaseIntegrationTest() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:7501/services/api/gateway/car-toll-api/avarez-api/toll")
                .responseTimeout(Duration.ofMillis(1000000))
                .build();
    }

    @BeforeAll
    static void initialize() {

        WebClient client = WebClient.builder()
                .baseUrl("http://185.135.30.10:9443")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        clientToken = client.post()
                .uri("/identity/oauth2/auth/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic a2V5OnNlY3JldA==")
                .body(BodyInserters.fromFormData("scope", "customer-super")
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToFlux(TokenDto.class).blockLast();


    }


}
