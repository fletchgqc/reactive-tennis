package de.codecentric.fletcher.tennis.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class RacketController {

  @RestController
  public class HelloController {

    @GetMapping("/rackets")
    public String getRackets() {
      return "Hello WebFlux";
    }
  }
}
