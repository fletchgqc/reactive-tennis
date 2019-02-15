package de.codecentric.fletcher.tennis.endpoint;

import de.codecentric.fletcher.tennis.domain.TennisBall;
import de.codecentric.fletcher.tennis.endpoint.vo.JsonTennisBall;
import org.springframework.stereotype.Component;

@Component
public class TennisBallConverter {

  public TennisBall convert(JsonTennisBall json) {
    TennisBall ball = new TennisBall();
    ball.setColour(json.getColour());
    return ball;
  }
}
