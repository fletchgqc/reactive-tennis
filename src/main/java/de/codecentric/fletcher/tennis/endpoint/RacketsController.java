package de.codecentric.fletcher.tennis.endpoint;

import de.codecentric.fletcher.tennis.domain.Racket;
import de.codecentric.fletcher.tennis.service.RacketsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RacketsController {

  @Autowired private RacketsService racketsService;

  @GetMapping("/rackets")
  public List<Racket> getRackets() {
    return racketsService.getRackets();
  }
}
