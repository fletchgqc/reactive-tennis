package de.codecentric.fletcher.tennis.endpoint;

import de.codecentric.fletcher.tennis.endpoint.vo.ErrorMessage;
import de.codecentric.fletcher.tennis.endpoint.vo.JsonTennisBall;
import de.codecentric.fletcher.tennis.repositories.TennisBallRepository;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class BallsHandler {

  private Validator validator;

  private TennisBallConverter converter;

  private TennisBallRepository repository;

  public Mono<ServerResponse> postBall(ServerRequest request) {
    return request
        .bodyToMono(JsonTennisBall.class)
        .flatMap(
            body -> {
              Set<ConstraintViolation<JsonTennisBall>> violations = validator.validate(body);
              return violations.isEmpty()
                  ? processValidRequest(body)
                  : createBadRequestResponse(collectViolationMessages(violations));
            });
  }

  private Mono<ServerResponse> processValidRequest(JsonTennisBall body) {
    return Mono.just(body)
        .map(converter::convert)
        .flatMap(repository::save)
        .map(b -> b.getId())
        .flatMap(
            id ->
                ServerResponse.created(URI.create("/tennisBalls/" + id))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .build());
  }

  private Mono<ServerResponse> createBadRequestResponse(String error) {
    return ServerResponse.badRequest()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(new ErrorMessage(error));
  }

  private static String collectViolationMessages(
      final Set<ConstraintViolation<JsonTennisBall>> violations) {
    return violations
        .stream()
        .map(v -> v.getPropertyPath() + " " + v.getMessage())
        .collect(Collectors.joining(", "));
  }
}
