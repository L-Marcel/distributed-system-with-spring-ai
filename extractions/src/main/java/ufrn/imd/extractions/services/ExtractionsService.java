package ufrn.imd.extractions.services;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import ufrn.imd.commons.errors.TooManyRequests;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.models.enums.NoticeStatus;

public interface ExtractionsService {
  public Notice findById(UUID id);
  public void extract(Notice notice) throws JsonProcessingException;
  
  @CircuitBreaker(name = "extractions-request", fallbackMethod = "requestFallback")
  @Retry(name = "extractions-request", fallbackMethod = "requestFallback")
  @RateLimiter(name = "extractions-request", fallbackMethod = "requestFallbackTooManyRequests")
  @Bulkhead(name = "extractions-request", fallbackMethod = "requestFallback", type = Bulkhead.Type.THREADPOOL)
  public NoticeStatus request(UUID id);
  
  default NoticeStatus requestFallback(
    UUID id,
    Exception exception
  ) {
    return NoticeStatus.STOPPED;
  };

  default NoticeStatus requestFallbackTooManyRequests(
    UUID id,
    Exception exception
  ) {
    throw new TooManyRequests();
  };
};
