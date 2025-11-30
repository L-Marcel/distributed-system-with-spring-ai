package ufrn.imd.extractions.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.commons.models.enums.NoticeStatus;
import ufrn.imd.extractions.services.ExtractionsService;

@RestController
@RequestMapping
public class ExtractionController {
  private ExtractionsService extractions;
  @Autowired
  public ExtractionController(
    ExtractionsService extractions
  ) {
    this.extractions = extractions;
  };

  @PostMapping("/{id}")
  public ResponseEntity<NoticeStatus> extract(
    @PathVariable UUID id
  ) {
    NoticeStatus status = this.extractions.request(id);
    return ResponseEntity.ok(status);
  };
};
