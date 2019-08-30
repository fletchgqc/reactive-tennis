package de.codecentric.fletcher.tennis.endpoint;

import static org.junit.jupiter.api.Assertions.*;

import de.codecentric.fletcher.tennis.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock UserService userService;

  @InjectMocks UserController controller;

  @Test
  void getUser() {
    Mockito.when(userService.getUserName(Mockito.eq(2))).thenReturn(Mono.just("Roland"));

    StepVerifier.create(controller.getUser(2, "thaoteha"))
        .expectNext("Roland")
        .expectComplete()
        .verify();
  }
}
