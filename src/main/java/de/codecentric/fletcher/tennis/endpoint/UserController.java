package de.codecentric.fletcher.tennis.endpoint;

import de.codecentric.fletcher.tennis.config.MdcLogger;
import de.codecentric.fletcher.tennis.service.UserService;
import de.codecentric.fletcher.tennis.service.vo.User;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@RestController
public class UserController {

  @Autowired private UserService userService;

  @GetMapping(value = "/users/{id}", produces = "text/plain; charset=utf-8")
  public Mono<String> getUser(
      @PathVariable Integer id,
      @RequestHeader(required = false, name = MdcLogger.CORRELATION_ID) String correlationId) {

    return userService
        .getUserName(id)
        .subscriberContext(Context.of(MdcLogger.CORRELATION_ID, correlationId));
  }

  @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Mono<Void> postUser(
      @Valid @RequestBody Mono<User> user,
      @RequestHeader(required = false, name = MdcLogger.CORRELATION_ID) String correlationId) {

    return user.flatMap(u -> userService.postUser(u))
        .subscriberContext(Context.of(MdcLogger.CORRELATION_ID, correlationId));
  }
}
