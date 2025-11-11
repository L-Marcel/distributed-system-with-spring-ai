package ufrn.imd.notices.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import ufrn.imd.notices.models.Notice;

@FeignClient("extractions")
public interface ExtractionsService {
  @PostMapping
  ResponseEntity<Void> request(
    @RequestBody Notice notice
  );
};
