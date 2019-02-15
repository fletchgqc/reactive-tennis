package de.codecentric.fletcher.tennis.repositories;

import de.codecentric.fletcher.tennis.domain.TennisBall;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface TennisBallRepository extends ReactiveCrudRepository<TennisBall, Integer> {}
