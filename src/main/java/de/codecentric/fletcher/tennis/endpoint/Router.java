package de.codecentric.fletcher.tennis.endpoint;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
public class Router {

  private final BallsHandler ballsHandler;

  @Bean
  public RouterFunction<ServerResponse> postTennisBall() {
    return route(POST("/tennisBalls"), ballsHandler::postBall);
  }
}
