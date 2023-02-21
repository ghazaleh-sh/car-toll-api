package ir.co.sadad.cartollapi.controller;

import ir.co.sadad.cartollapi.AbstractBaseIntegrationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TollResourceIT extends AbstractBaseIntegrationTest {

    private String duplictedPlateTag = "72ب36599";
    private String normalPlateTag = "88ب36410";
    private String duplicatedPlateNo = "720236599";
    private String normalPlateNo = "880236400";

    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzc24iOiIwMDc5OTkzMTQxIiwiY2VsbHBob25lIjoiOTg5MjE4MzAxNjMxIiwiaW5zdGFuY2VJZCI6bnVsbCwib3NOYW1lIjoiaW9zIiwiZGV2aWNlTmFtZSI6ImlwaG9uZSA4IiwidXNlckFnZW50IjoiUG9zdG1hblJ1bnRpbWUvNy4yOC40IiwidXNlcklkIjoxNTgsImV4cCI6MTYzNzE0Njc4MDM1MywicmVnRGF0ZSI6MTYzNDU1NDc4MDM1Mywic2VyaWFsSWQiOiI1NzAwY2Q1OC0zY2Q2LTRjZTMtODFmZi1lZTUxOWUxZjZkZjcifQ==.jMU6e94AbMinyek3BcsGsIBKPHhuErNtp1vpt_N6l8A";

//    @Test
//    @Order(1)
//    void shouldPassCreate() {
//
//        CarTollDto.Create.Request mockRequest = new CarTollDto.Create.Request();
//        mockRequest.setPlateNo(normalPlateTag);
////        mockRequest.setVin("12345655");
//        webTestClient
//                .post()
//                .uri("/plates")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .body(Mono.just(mockRequest), CarTollDto.Create.Request.class)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(CarTollDto.Create.Response.class)
//                .returnResult()
//                .getResponseBody();
////                .value(res -> res.getPlateInfo().getPlateTag(), equalTo(mockRequest.getPlateTag()));
//    }

//    @Test
//    @Order(2)
//    void shouldFailedCreateDuplicated() {
//
//        CarTollDto.Create.Request mockRequest = new CarTollDto.Create.Request();
//        mockRequest.setPlateNo(duplictedPlateTag);
////        mockRequest.setVin("12345655");
//        webTestClient
//                .post()
//                .uri("/plates")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .body(Mono.just(mockRequest), CarTollDto.Create.Request.class)
//                .exchange()
//                .expectStatus()
//                .is4xxClientError();
//    }

//    @Test
//    @Order(3)
//    void shouldPassGet() {
//        webTestClient
//                .get()
//                .uri("/plates")
//                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful()
//                .expectBodyList(CarTollDto.Search.Response.class)
//                .consumeWith(result -> {
//                    result.getResponseBody().contains(CarTollDto.PlateInfo.class);
//                });
//    }

//    @Test
//    @Order(4)
//    void shouldPassDelete() {
//        CarTollDto.Delete.Request mockRequest = new CarTollDto.Delete.Request();
//        mockRequest.setPlateNo(normalPlateNo);
//        webTestClient
//                .method(HttpMethod.DELETE)
//                .uri("/plates")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .body(Mono.just(mockRequest), CarTollDto.Delete.Request.class)
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful()
//                .expectBodyList(CarTollDto.Delete.Response.class);
//    }

//    @Test
//    @Order(4)
//    void shouldFailedDeleteNotExist() {
//        CarTollDto.Delete.Request mockRequest = new CarTollDto.Delete.Request();
//        mockRequest.setPlateNo(duplicatedPlateNo);
//
//        webTestClient
//                .method(HttpMethod.DELETE)
//                .uri("/plates")
//                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .body(Mono.just(mockRequest), CarTollDto.Delete.Request.class)
//                .exchange()
//                .expectStatus()
//                .is4xxClientError();
//    }

//    @Test
//    @Order(5)
//    void shouldPassSummary() {
//        webTestClient
//                .get()
//                .uri("/freewayTollsSummary?plateNo=" + normalPlateNo)
//                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful()
//                .expectBody(CarTollSummaryDto.Response.class);
//    }

    @Test
    @Order(6)
    void shouldFailedSummaryNotExist() {
        webTestClient
                .get()
                .uri("/freewayTollsSummary?plateNo=" + duplicatedPlateNo)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

}