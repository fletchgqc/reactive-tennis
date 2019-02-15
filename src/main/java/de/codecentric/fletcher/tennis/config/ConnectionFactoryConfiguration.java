package de.codecentric.fletcher.tennis.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionFactoryConfiguration {

  @Bean
  ConnectionFactory connectionFactory() {
    return new H2ConnectionFactory(
        H2ConnectionConfiguration.builder().inMemory("testdb").username("sa").build());
  }
}
