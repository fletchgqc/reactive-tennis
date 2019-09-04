package de.codecentric.fletcher.tennis.service;

import de.codecentric.fletcher.tennis.config.MdcLogger;
import de.codecentric.fletcher.tennis.service.vo.User;
import de.codecentric.fletcher.tennis.util.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

  WebClient webClient;
  JsonSerializer serializer;

  public UserService(
      WebClient.Builder webClientBuilder,
      JsonSerializer serializer,
      @Value("${backend.baseUrl}") String baseUrl) {
    this.webClient = webClientBuilder.baseUrl(baseUrl).build();

    this.serializer = serializer;
  }

  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //

  public Mono<String> getUserName2(Integer id) {
    return webClient
        .get()
        .uri("/users/{id}", id)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .retrieve()
        /**
         * Without this line, default error handler will be called on 4xx or 5xx response. However,
         * it doesn't log the body nor get called on 3xx.
         */
        .onStatus(s -> !s.is2xxSuccessful(), this::mapResponseToExceptionMono)
        .bodyToMono(String.class)
        .defaultIfEmpty("<EMPTY>")
        .doOnEach(MdcLogger.logOnNext(r -> log.debug("Response body as json {}", r)))
        .map(s -> serializer.deserialise(s, User.class))
        .doOnEach(MdcLogger.logOnNext(r -> log.debug("Response body as pojo {}", r)))
        .map(this::failIfUserIdTooHigh)
        .map(user -> user.getName())
        .doOnEach(MdcLogger.logOnNext(b -> log.debug("User name from response: {}", b)))
        .doOnEach(MdcLogger.logOnComplete(() -> log.debug("Completed HTTP request")));
  }

  public Mono<Void> postUser(User user) {
    return Mono.just(user)
        .doOnEach(MdcLogger.logOnNext(t -> log.debug("Request body as pojo {}", t)))
        .map(serializer::serialize)
        .doOnEach(MdcLogger.logOnNext(t -> log.debug("Request body as json {}", t)))
        .flatMap(
            body ->
                webClient
                    .post()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .syncBody(body)
                    .retrieve()
                    .onStatus(s -> !s.is2xxSuccessful(), this::mapResponseToExceptionMono)
                    .bodyToMono(String.class))
        .defaultIfEmpty("<EMPTY>")
        .doOnEach(MdcLogger.logOnNext(r -> log.debug("Response body as text {}", r)))
        .then()
        .doOnEach(MdcLogger.logOnComplete(() -> log.debug("Completed HTTP request")));
  }

  private Mono<RuntimeException> mapResponseToExceptionMono(ClientResponse response) {
    return response
        .bodyToMono(String.class)
        .defaultIfEmpty("<EMPTY>")
        .map(
            responseBody ->
                new RuntimeException(
                    "Unexpected HTTP response code: "
                        + response.statusCode()
                        + ", headers: "
                        + response.headers().asHttpHeaders()
                        + ", body: "
                        + responseBody));
  }

  private User failIfUserIdTooHigh(final User user) {
    int id = user.getId();

    if (id > 99) {
      throw new RuntimeException("ID too high: " + id);
    } else {
      return user;
    }
  }
}
