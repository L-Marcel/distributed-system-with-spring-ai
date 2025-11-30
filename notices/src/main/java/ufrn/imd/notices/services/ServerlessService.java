package ufrn.imd.notices.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import ufrn.imd.commons.errors.TooManyRequests;

@FeignClient("serverless")
public interface ServerlessService {
  static final Logger log = LoggerFactory.getLogger(
    ServerlessService.class
  );

  @GetMapping(value = "/info", consumes = "application/json")
  @CircuitBreaker(name = "serverless", fallbackMethod = "fallback")
  @Retry(name = "serverless", fallbackMethod = "fallback")
  @RateLimiter(name = "serverless", fallbackMethod = "fallbackTooManyRequests")
  @Bulkhead(name = "serverless", fallbackMethod = "fallback", type = Bulkhead.Type.SEMAPHORE)
  public ResponseEntity<String> info();

  default ResponseEntity<String> fallback(
    UUID id,
    Exception exception
  ) {
    log.error(exception.getMessage());
    return ResponseEntity
      .status(HttpStatus.SERVICE_UNAVAILABLE)
      .body(exception.getMessage());
  };

  default ResponseEntity<String> fallbackTooManyRequests(
    UUID id,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new TooManyRequests();
  };
};
