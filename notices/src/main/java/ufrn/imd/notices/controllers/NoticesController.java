package ufrn.imd.notices.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.commons.dto.NoticeDTO;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.notices.services.NoticesService;

@RestController
@RequestMapping
public class NoticesController {
  private NoticesService notices;

  @Autowired
  public NoticesController(
    NoticesService notices
  ) {
    this.notices = notices;
  };

  @PostMapping
  public ResponseEntity<NoticeDTO> create(
    @RequestPart("file") MultipartFile file
  ) {
    Notice notice = this.notices.create(
      file.getResource(),
      file.getOriginalFilename(),
      file.getSize()
    );

    NoticeDTO reference = new NoticeDTO(
      notice.getId(),
      notice.getVersion(),
      notice.getType(),
      notice.getStatus(),
      notice.getNotes()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(reference);
  };

  @PutMapping("/{id}")
  public ResponseEntity<NoticeDTO> update(
    @PathVariable UUID id,
    @RequestPart("file") MultipartFile file
  ) {
    Notice notice = this.notices.update(
      id,
      file.getResource(),
      file.getOriginalFilename(),
      file.getSize()
    );

    NoticeDTO reference = new NoticeDTO(
      notice.getId(),
      notice.getVersion(),
      notice.getType(),
      notice.getStatus(),
      notice.getNotes()
    );

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(reference);
  };

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @PathVariable UUID id
  ) {
    this.notices.deleteById(id);
    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .build();
  };
};
