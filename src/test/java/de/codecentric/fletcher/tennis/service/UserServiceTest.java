package de.codecentric.fletcher.tennis.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import de.codecentric.fletcher.tennis.TestUtils;
import de.codecentric.fletcher.tennis.util.JsonSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class UserServiceTest {

  private UserService userService;

  protected WireMockServer wireMockServer;

  @BeforeEach
  public void setUp() {
    wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
    wireMockServer.start();

    userService =
        new UserService(
            WebClient.builder(),
            new JsonSerializer(TestUtils.getObjectMapper()),
            "http://localhost:" + wireMockServer.port());
  }

  @Test
  public void getUserHappyCase() {
    wireMockServer.stubFor(
        get(urlEqualTo("/users/1"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(
                        "{\"id\": 1,\n"
                            + "  \"name\": \"James Walker\",\n"
                            + "  \"email\": \"Sincere@april.biz\"\n"
                            + "}")));

    StepVerifier.create(userService.getUserName(1)).expectNext("James Walker").verifyComplete();
  }

  @AfterEach
  public void tearDown() {
    wireMockServer.stop();
  }
}
