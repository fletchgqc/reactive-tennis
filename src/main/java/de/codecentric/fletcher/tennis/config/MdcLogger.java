package de.codecentric.fletcher.tennis.config;

import java.util.function.Consumer;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;

/**
 * Based on https://simonbasle.github.io/2018/02/contextual-logging-with-reactor-context-and-mdc/
 */
public class MdcLogger {

  public static final String CORRELATION_ID = "correlationId";

  public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
    return signal -> {
      if (signal.isOnNext()) {

        if (signal.getContext().hasKey(CORRELATION_ID)) {
          try (MDC.MDCCloseable closeable =
              MDC.putCloseable(CORRELATION_ID, signal.getContext().get(CORRELATION_ID))) {
            logStatement.accept(signal.get());
          }
        } else {
          logStatement.accept(signal.get());
        }
      }
    };
  }

  public static <T> Consumer<Signal<T>> logOnComplete(Runnable logStatement) {
    return signal -> {
      if (signal.isOnComplete()) {

        if (signal.getContext().hasKey(CORRELATION_ID)) {
          try (MDC.MDCCloseable closeable =
              MDC.putCloseable(CORRELATION_ID, signal.getContext().get(CORRELATION_ID))) {
            logStatement.run();
          }
        } else {
          logStatement.run();
        }
      }
    };
  }

  public static <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> logStatement) {
    return signal -> {
      if (signal.isOnError()) {

        if (signal.getContext().hasKey(CORRELATION_ID)) {
          try (MDC.MDCCloseable closeable =
              MDC.putCloseable(CORRELATION_ID, signal.getContext().get(CORRELATION_ID))) {
            logStatement.accept(signal.getThrowable());
          }
        } else {
          logStatement.accept(signal.getThrowable());
        }
      }
    };
  }

  public static void logWithMdc(Runnable logStatement, String correlationId) {
    try (MDC.MDCCloseable closeable = MDC.putCloseable(CORRELATION_ID, correlationId)) {
      logStatement.run();
    }
  }
}
