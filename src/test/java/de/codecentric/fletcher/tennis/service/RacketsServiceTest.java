package de.codecentric.fletcher.tennis.service;

import static org.junit.jupiter.api.Assertions.*;

import de.codecentric.fletcher.tennis.domain.Racket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RacketsServiceTest {

  @Test
  void getRackets() {
    RacketsService service = new RacketsService();

    Assertions.assertThat(service.getRackets()).containsExactly(new Racket(75), new Racket(68));
  }
}
