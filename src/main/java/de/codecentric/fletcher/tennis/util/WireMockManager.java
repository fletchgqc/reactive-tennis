package de.codecentric.fletcher.tennis.util;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Bean that starts and stops WireMock. WireMock is used to mock the backend, this is useful to do a
 * demo where you have no internet access. The conditionalOnProperty annotation means that this bean
 * (and consequently WireMock) will automatically and only be available if the backendUrl is
 * configured to localhost (see application.properties).
 */
@Component
@ConditionalOnProperty(name = "backend.baseUrl", havingValue = "http://localhost:9999")
public class WireMockManager {

  WireMockServer wireMockServer;

  @PostConstruct
  public void startWireMock() {
    wireMockServer =
        new WireMockServer(wireMockConfig().port(9999).usingFilesUnderClasspath("wiremock"));
    wireMockServer.start();
  }

  @PreDestroy
  public void stopWireMock() {
    wireMockServer.stop();
  }
}
