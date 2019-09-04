package de.codecentric.fletcher.tennis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebClientLoggingConfiguration {

  /**
   * This configures the {@link WebClient.Builder} which is used to make HTTP calls to log all
   * requests and responses, respecting the MDC context.
   */
//  @Bean
  WebClientCustomizer webClientLoggingCustomizer() {
    return (WebClient.Builder builder) -> builder.filter(logRequest()).filter(logResponse());
  }

  private ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(
        clientRequest ->
            Mono.just(clientRequest)
                .doOnEach(
                    MdcLogger.logOnNext(
                        request ->
                            log.debug(
                                "Request: {} {}, headers: {}",
                                request.method(),
                                request.url(),
                                request.headers()))));
  }

  private ExchangeFilterFunction logResponse() {
    return ExchangeFilterFunction.ofResponseProcessor(
        clientResponse ->
            Mono.just(clientResponse)
                .doOnEach(
                    MdcLogger.logOnNext(
                        response ->
                            log.debug(
                                "Response code: {}, headers: {}",
                                response.statusCode(),
                                response.headers().asHttpHeaders()))));
  }
}
