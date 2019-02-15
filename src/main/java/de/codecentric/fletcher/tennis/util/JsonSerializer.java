package de.codecentric.fletcher.tennis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonSerializer {
  private final ObjectMapper objectMapper;

  public JsonSerializer(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String serialize(final Object body) {
    try {
      return objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserialise(final String json, final Class<T> targetType) {
    try {
      return objectMapper.readValue(json, targetType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
