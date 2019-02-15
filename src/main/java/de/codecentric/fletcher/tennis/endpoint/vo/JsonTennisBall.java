package de.codecentric.fletcher.tennis.endpoint.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class JsonTennisBall {

  @NotBlank
  @Size(max = 10)
  private String colour;
}
