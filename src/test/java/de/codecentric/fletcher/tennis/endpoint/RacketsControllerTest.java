package de.codecentric.fletcher.tennis.endpoint;

import de.codecentric.fletcher.tennis.domain.Racket;
import de.codecentric.fletcher.tennis.service.RacketsService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class RacketsControllerTest {

  @Autowired private WebTestClient client;

  @MockBean private RacketsService service;

  @Test
  void validRequestShouldReturn200() {
    Mockito.when(service.getRackets()).thenReturn(List.of(new Racket(45), new Racket(48)));

    client
        .get()
        .uri("/rackets")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json("[{\"lengthInCm\":45},{\"lengthInCm\":48}]");
  }
}
