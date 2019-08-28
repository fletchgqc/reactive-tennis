package de.codecentric.fletcher.tennis.service;

import de.codecentric.fletcher.tennis.domain.Racket;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RacketsService {

  List rackets = List.of(new Racket(75), new Racket(68));

  public List<Racket> getRackets() {
    return rackets;
  }
}
