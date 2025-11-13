package ufrn.imd.extractions.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.extractions.models.Notice;
import ufrn.imd.extractions.models.enums.NoticeStatus;
import ufrn.imd.extractions.services.ExtractionService;
import ufrn.imd.extractions.services.NoticesService;

@RestController
@RequestMapping
public class ExtractionController {
  private ExtractionService extraction;
  private NoticesService notices;

  @Autowired
  public ExtractionController(
    ExtractionService extraction,
    NoticesService notices
  ) {
    this.extraction = extraction;
    this.notices = notices;
  };

  @PostMapping("/{id}")
  public ResponseEntity<NoticeStatus> extract(
    @PathVariable UUID id
  ) {
    Notice notice = this.notices.findById(id);
    NoticeStatus status = this.extraction.request(notice);
    return ResponseEntity.ok(status);
  };
};
