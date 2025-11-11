package ufrn.imd.extractions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import ufrn.imd.extractions.models.Notice;
import ufrn.imd.extractions.services.ExtractionService;

@RestController
@RequestMapping
public class ExtractionController {
  private ExtractionService extraction;

  @Autowired
  public ExtractionController(
    ExtractionService extraction
  ) {
    this.extraction = extraction;
  };

  @PostMapping
  public ResponseEntity<Void> extract(
    @RequestBody Notice notice
  ) {
    this.extraction.request(notice);
    return ResponseEntity
      .noContent()
      .build();
  };
};
