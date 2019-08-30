package de.codecentric.fletcher.tennis.kafka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DomainMessage<T> {
  private String id;
  private String correlationId = "<ABSENT>";
  private Instant timestamp;
  private String type = "<ABSENT>";
  private Integer version;
  private T payload;

  @JsonIgnore private String content;

  protected DomainMessage(String type, String correlationId, T payload) {
    setId(UUID.randomUUID().toString());
    setTimestamp(Instant.now());
    setType(type);
    setVersion(1);
    setCorrelationId(correlationId);
    setPayload(payload);
  }
}
