package ufrn.imd.notices.controllers;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.services.ExtractionService;
import ufrn.imd.notices.services.NoticesService;

@RestController
@RequestMapping
public class NoticesController {
  private NoticesService notices;
  private ExtractionService extraction;

  @Autowired
  public NoticesController(
    NoticesService notices,
    ExtractionService extraction
  ) {
    this.notices = notices;
    this.extraction = extraction;
  };

  @GetMapping
  public ResponseEntity<String> ask() {
    String answer = this.extraction.ask("Olá! Você tem um nome?");
    System.out.println("ANSWER: " + answer);
    return ResponseEntity.ok(answer);
  };

  @PostMapping
  public ResponseEntity<Void> create(
    @RequestPart("file") MultipartFile file
  ) {
    List<Document> chunks = this.notices.read(file.getResource());
    chunks = this.notices.split(chunks);
    
    Notice notice = this.notices.create(
      chunks,
      file.getOriginalFilename(),
      file.getSize()
    );

    this.notices.requestExtraction(notice);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .build();
  };

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
    @PathVariable Long id,
    @RequestPart("file") MultipartFile file
  ) {
    List<Document> chunks = this.notices.read(file.getResource());
    chunks = this.notices.split(chunks);
    
    Notice notice = this.notices.update(
      id,
      chunks,
      file.getOriginalFilename(),
      file.getSize()
    );

    this.notices.requestExtraction(notice);
    return ResponseEntity
      .status(HttpStatus.OK)
      .build();
  };

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @PathVariable Long id
  ) {
    this.notices.deleteById(id);
    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .build();
  };
};
