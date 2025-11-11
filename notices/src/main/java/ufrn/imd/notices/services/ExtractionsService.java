package ufrn.imd.notices.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ufrn.imd.notices.dto.NoticeBasicReferenceDTO;

@FeignClient("extractions")
public interface ExtractionsService {
  @PostMapping(value = "/", consumes = "application/json")
  public ResponseEntity<Void> request(
    @RequestBody NoticeBasicReferenceDTO notice
  );
};
