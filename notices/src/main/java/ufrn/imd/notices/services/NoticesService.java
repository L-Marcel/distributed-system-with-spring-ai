package ufrn.imd.notices.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import ufrn.imd.commons.errors.ServiceUnavailable;
import ufrn.imd.commons.errors.TooManyRequests;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.models.enums.NoticeStatus;

public interface NoticesService {
  static final Logger log = LoggerFactory.getLogger(
    NoticesService.class
  );

  @CircuitBreaker(name = "create-or-update-notice", fallbackMethod = "createFallback")
  @Retry(name = "create-or-update-notice", fallbackMethod = "createFallback")
  @RateLimiter(name = "create-or-update-notice", fallbackMethod = "createFallbackTooManyRequests")
  @Bulkhead(name = "create-or-update-notice", fallbackMethod = "createFallback", type = Bulkhead.Type.SEMAPHORE)
  public Notice create(
    Resource file,
    String filename,
    Long bytes
  );

  default NoticeStatus createFallback(
    Resource file,
    String filename,
    Long bytes,
    Exception exception
  ) {
    log.error(exception.getMessage());
    return NoticeStatus.STOPPED;
  };

  default NoticeStatus createFallbackTooManyRequests(
    Resource file,
    String filename,
    Long bytes,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new TooManyRequests();
  };

  @CircuitBreaker(name = "create-or-update-notice", fallbackMethod = "updateFallback")
  @Retry(name = "create-or-update-notice", fallbackMethod = "updateFallback")
  @RateLimiter(name = "create-or-update-notice", fallbackMethod = "updateFallbackTooManyRequests")
  @Bulkhead(name = "create-or-update-notice", fallbackMethod = "updateFallback", type = Bulkhead.Type.SEMAPHORE)
  public Notice update(
    UUID id,
    Resource file,
    String filename,
    Long bytes
  );

  default NoticeStatus updateFallback(
    UUID id,
    Resource file,
    String filename,
    Long bytes,
    Exception exception
  ) {
    log.error(exception.getMessage());
    return NoticeStatus.STOPPED;
  };

  default NoticeStatus updateFallbackTooManyRequests(
    UUID id,
    Resource file,
    String filename,
    Long bytes,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new TooManyRequests();
  };

  @CircuitBreaker(name = "create-or-update-notice", fallbackMethod = "deleteFallback")
  @Retry(name = "create-or-update-notice", fallbackMethod = "deleteFallback")
  @RateLimiter(name = "create-or-update-notice", fallbackMethod = "deleteFallbackTooManyRequests")
  @Bulkhead(name = "create-or-update-notice", fallbackMethod = "deleteFallback", type = Bulkhead.Type.SEMAPHORE)
  public void deleteById(UUID id);

  default void deleteFallback(
    UUID id,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new ServiceUnavailable();
  };

  default NoticeStatus deleteFallbackTooManyRequests(
    UUID id,
    Resource file,
    String filename,
    Long bytes,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new TooManyRequests();
  };
  
  @CircuitBreaker(name = "read-notice", fallbackMethod = "findFallback")
  @Retry(name = "read-notice", fallbackMethod = "findFallback")
  @RateLimiter(name = "read-notice", fallbackMethod = "findFallbackTooManyRequests")
  @Bulkhead(name = "read-notice", fallbackMethod = "findFallback", type = Bulkhead.Type.SEMAPHORE)
  public Notice findById(UUID id);

  default Notice findFallback(
    UUID id,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new ServiceUnavailable();
  };

  default Notice findFallbackTooManyRequests(
    UUID id,
    Exception exception
  ) {
    log.error(exception.getMessage());
    throw new TooManyRequests();
  };
};
