package de.codecentric.fletcher.tennis;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ErrorHandlingTest {

  /*
    @Test
    public void checkedException() {
      Flux<URL> twitterProfilePages =
          Flux.just("Johannes", "Sebastian", "Kai", "Damien")
              .map(a -> {
                  return new URL("http://twitter.com/" + a);
              });

      StepVerifier.create(twitterProfilePages).expectNextCount(4).expectComplete().verify();
    }
  */

  @Test
  public void throwingExceptionShouldResultInErrorFlux() {
    Flux<String> fluxWhichWillThrow =
        Flux.just("Johannes", "Sebastian", "Kai", "Damien")
            .map(
                a -> {
                  throw new RuntimeException("Something bad happened");
                });

    StepVerifier.create(fluxWhichWillThrow).expectError().verify();
  }

  @Test
  public void errorTest() {
    Flux nameFlux =
        Flux.just("Johannes", "Sebastian", "Kai", "Damien")
            .doOnNext(item -> System.out.println("Processing " + item));

    StepVerifier.create(nameFlux).expectNextCount(4).verifyComplete();
  }

  @Test
  public void errorTest2() {
    Flux nameFlux =
        Flux.just("Johannes", "Sebastian", "Kai", "Damien")
            .map(a -> a.substring(0, 4))
            .onErrorContinue(
                (exception, item) -> {
                  System.out.println("error with item " + item);
                })
            .doOnNext(item -> System.out.println("Processing " + item));

    StepVerifier.create(nameFlux).expectNextCount(3).verifyComplete();
  }
}
