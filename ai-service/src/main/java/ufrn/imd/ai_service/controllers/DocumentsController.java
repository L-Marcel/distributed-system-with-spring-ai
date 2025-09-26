package ufrn.imd.ai_service.controllers;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.ai_service.dto.DocumentRegistry;
import ufrn.imd.ai_service.services.DocumentsService;

@RestController
@RequestMapping("/documents")
public class DocumentsController {
  private DocumentsService service;

  @Autowired
  public DocumentsController(
    DocumentsService service
  ) {
    this.service = service;
  };

  @PostMapping
  public ResponseEntity<Void> upload(
    @RequestPart MultipartFile[] files
  ) {
    List<Document> documents = this.service.read(files);
    documents = this.service.split(documents);
    this.service.registry(documents);
    System.out.println(documents);
    return ResponseEntity.ok().build();
  };
};
