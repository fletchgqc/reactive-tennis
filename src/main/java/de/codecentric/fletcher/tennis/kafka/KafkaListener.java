package de.codecentric.fletcher.tennis.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.fletcher.tennis.config.MdcLogger;
import java.io.IOException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.context.Context;

@Component
@Slf4j
public class KafkaListener {

  @Autowired private ObjectMapper objectMapper;

  private Scheduler scheduler = Schedulers.newSingle("sample");

  /**
   * Calls processor.apply (the business logic) with every message which arrives on the topic. The
   * argument to the function is the message deserialised as type DomainMessage.
   *
   * <p>This method should be called a maximum of one time per topic. Calling it twice will result
   * in unpredictable behaviour. Multiple processors handling the same message type on the same
   * topic are also currently not supported.
   *
   * @param topic the Kafka topic to listen on.
   * @param processor the business logic to execute with this message.
   */
  private Flux<Void> processMessages(String topic, Function<DomainMessage, Mono<Void>> processor) {
    return newSubscription(topic)
        .flatMapSequential(
            receiverRecord -> {
              Mono<Void> result = Mono.empty();

              DomainMessage<?> domainMessage = parseDomainMessage(receiverRecord);

              if (domainMessage != null) {
                result =
                    Mono.just(domainMessage)
                        .flatMap(message -> processor.apply(message))
                        .onErrorResume(
                            e -> {
                              MdcLogger.logWithMdc(
                                  () ->
                                      log.error(
                                          "The error ‘{}’ occurred whilst processing the message: {}",
                                          e.getMessage(),
                                          domainMessage.getContent(),
                                          e),
                                  domainMessage.getCorrelationId());
                              return Mono.empty();
                            })
                        .subscriberContext(
                            Context.of(MdcLogger.CORRELATION_ID, domainMessage.getCorrelationId()));
              }

              return result.doOnSuccess(x -> receiverRecord.receiverOffset().acknowledge());
            },
            1);
  }

  private DomainMessage<?> parseDomainMessage(ReceiverRecord<String, String> receiverRecord) {
    DomainMessage<?> domainMessage = null;
    try {
      domainMessage = objectMapper.readValue(receiverRecord.value(), DomainMessage.class);
      domainMessage.setContent(receiverRecord.value());
    } catch (IOException e) {
      // No context since correlationId cannot be parsed out of message, just log
      // normally.
      log.error("Error deserialising message {}", receiverRecord.value(), e);
    }
    return domainMessage;
  }

  /** Insert Kafka subscription code here. Examples can be found on reactor-kafka website. */
  public Flux<ReceiverRecord<String, String>> newSubscription(String topic) {
    return Flux.empty();
  }
}
