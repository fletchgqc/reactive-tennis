package de.codecentric.fletcher.tennis.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Tests for the Handler class. SpringBootTest is used here instead of a simple Mockito test because
 * this allows us to test the validation error handling - mocking the Validator is almost
 * impossible.
 */
@SpringBootTest
@AutoConfigureWebTestClient
class HandlerTest {

  private String validRequest = "{\"colour\":\"yellow\"}";

  private String singleInvalidRequest = "{}";

  @Autowired private WebTestClient client;

  @Test
  void validRequestShouldReturn201() {
    postBall(validRequest).expectStatus().isCreated().expectBody().isEmpty();
  }

  @Test
  void invalidRequestShouldReturn400AndNotCallService() {
    postBall(singleInvalidRequest)
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .json("{\"message\": \"colour must not be blank\"}");
  }

  private WebTestClient.ResponseSpec postBall(String body) {
    return client
        .post()
        .uri("/tennisBalls")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(body)
        .exchange();
  }
}
