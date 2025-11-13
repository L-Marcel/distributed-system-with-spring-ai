package ufrn.imd.notices.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ufrn.imd.notices.dto.NoticeBasicReferenceDTO;
import ufrn.imd.notices.models.enums.NoticeStatus;

@FeignClient("extractions")
public interface ExtractionsService {
  @PostMapping(value = "/", consumes = "application/json")
  @CircuitBreaker(name = "extractions", fallbackMethod = "fallback")
  public ResponseEntity<NoticeStatus> request(
    @RequestBody NoticeBasicReferenceDTO notice
  );

  default ResponseEntity<NoticeStatus> fallback(
    NoticeBasicReferenceDTO notice,
    Exception exception
  ) {
    return ResponseEntity.ok(NoticeStatus.STOPPED);
  };
};
