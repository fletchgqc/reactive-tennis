package de.codecentric.fletcher.tennis.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class TennisBall {
  @Id Integer id;

  String colour;
}
