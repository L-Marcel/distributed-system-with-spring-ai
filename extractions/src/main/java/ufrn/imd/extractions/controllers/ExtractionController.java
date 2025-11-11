package ufrn.imd.extractions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ufrn.imd.extractions.dto.NoticeBasicReferenceDTO;
import ufrn.imd.extractions.models.Notice;
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

  @PostMapping
  public ResponseEntity<Void> extract(
    @RequestBody @Valid NoticeBasicReferenceDTO body
  ) {
    Notice notice = this.notices.findByIdAndVersion(
      body.id(), 
      body.version()
    );

    this.extraction.request(notice);
    return ResponseEntity
      .noContent()
      .build();
  };
};
