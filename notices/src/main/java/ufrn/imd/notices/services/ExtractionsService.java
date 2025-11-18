package ufrn.imd.notices.services;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import ufrn.imd.commons.models.enums.NoticeStatus;

@FeignClient("extractions")
public interface ExtractionsService {
  @PostMapping(value = "/{id}", consumes = "application/json")
  @CircuitBreaker(name = "extractions", fallbackMethod = "fallback")
  @Retry(name = "extractions", fallbackMethod = "fallback")
  @RateLimiter(name = "extractions", fallbackMethod = "fallback")
  @Bulkhead(name = "extractions", fallbackMethod = "fallback", type = Bulkhead.Type.THREADPOOL)
  public ResponseEntity<NoticeStatus> request(
    @PathVariable UUID id
  );

  default ResponseEntity<NoticeStatus> fallback(
    UUID id,
    Exception exception
  ) {
    return ResponseEntity.ok(NoticeStatus.STOPPED);
  };
};
