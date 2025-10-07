package ufrn.imd.notices.controllers;

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

import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.services.NoticesService;

@RestController
@RequestMapping("/")
public class NoticesController {
  private NoticesService notices;

  @Autowired
  public NoticesController(
    NoticesService notices
  ) {
    this.notices = notices;
  };

  @PostMapping
  public ResponseEntity<Void> upload(
    @RequestPart MultipartFile file
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
};
