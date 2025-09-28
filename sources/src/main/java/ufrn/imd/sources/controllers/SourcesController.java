package ufrn.imd.sources.controllers;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.sources.models.Source;
import ufrn.imd.sources.services.SourcesService;

@RestController
@RequestMapping("/")
public class SourcesController {
  private SourcesService sources;

  @Autowired
  public SourcesController(
    SourcesService sources
  ) {
    this.sources = sources;
  };

  @PostMapping
  public ResponseEntity<Void> upload(
    @RequestPart MultipartFile file
  ) {
    List<Document> chunks = this.sources.read(file.getResource());
    chunks = this.sources.split(chunks);
    
    Source source = this.sources.create(
      chunks,
      file.getOriginalFilename(),
      file.getSize()
    );

    this.sources.requestSummarization(source);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .build();
  };
};
