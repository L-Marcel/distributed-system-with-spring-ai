package ufrn.imd.notices.services;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ufrn.imd.notices.models.enums.NoticeStatus;

@FeignClient("extractions")
public interface ExtractionsService {
  @PostMapping(value = "/{id}", consumes = "application/json")
  @CircuitBreaker(name = "extractions", fallbackMethod = "fallback")
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
