package ufrn.imd.notices.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.notices.errors.NoitceNotFound;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.models.enums.NoticeType;
import ufrn.imd.notices.repository.NoticesRepository;
import ufrn.imd.notices.repository.VectorStoreRepository;

@Service
public class NoticesService {
  private static final Logger log = LoggerFactory.getLogger(
    NoticesService.class
  );

  private TokenTextSplitter splitter;
  private VectorStoreRepository vectors;
  private NoticesRepository notices;

  @Autowired
  public NoticesService(
    TokenTextSplitter splitter,
    VectorStoreRepository vectors,
    NoticesRepository notices
  ) {
    this.splitter = splitter;
    this.vectors = vectors;
    this.notices = notices;
  };

  public List<Document> read(Resource file) {
    log.debug(
      "Reading file '{}'", 
      file.getFilename()
    );

    TikaDocumentReader reader = new TikaDocumentReader(file);
    return reader.read();
  };

  public List<Document> split(
    List<Document> documents
  ) {
    return this.splitter.apply(documents);
  };

  @Transactional
  public Notice create(
    List<Document> documents,
    String filename,
    Long bytes
  ) {
    log.info(
      "Creating notice from file '{}'", 
      filename
    );

    Notice notice = new Notice();
    notice.setFilename(filename);
    notice.setBytes(bytes);
    this.notices.save(notice);

    log.debug(
      "Notice saved from file '{}'' with id '{}'", 
      filename, 
      notice.getId()
    );

    for(int i = 0; i < documents.size(); i++) {
      Document document = documents.get(i);
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("notice", notice.getId().toString());
      metadata.put("version", notice.getVersion());
      metadata.put("end_index", documents.size() - 1);
      metadata.put("index", i);
    };

    log.debug(
      "Saving vectors from notice '{}'",
      notice.getId()
    );

    this.vectors.add(documents);

    log.debug(
      "Vectors from notice '{}' saved",
      notice.getId()
    );

    return notice;
  };

  @Transactional
  public Notice update(
    UUID id,
    List<Document> documents,
    String filename,
    Long bytes
  ) {
    Notice notice = this.findById(id);

    log.debug(
      "Updating notice with id '{}' and version '{}' from file '{}'",
      id,
      notice.getVersion(),
      filename
    );

    notice.setFilename(filename);
    notice.setType(NoticeType.UNKNOWN);
    // TODO - tirar isso depois
    notice.setBytes(bytes + System.currentTimeMillis());
    this.notices.saveAndFlush(notice);

    log.debug(
      "Notice with id '{}' update to version '{}' from file '{}'", 
      id,
      notice.getVersion(),
      filename
    );

    for(int i = 0; i < documents.size(); i++) {
      Document document = documents.get(i);
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("notice", notice.getId().toString());
      metadata.put("version", notice.getVersion());
      metadata.put("end_index", documents.size() - 1);
      metadata.put("index", i);
    };

    log.debug(
      "Updating vectors from notice with id '{}'",
      id
    );

    this.vectors.removeByNoticeId(id);
    this.vectors.add(documents);

    log.debug(
      "Vectors from notice with id '{}' updated",
      id
    );

    return notice;
  };

  @Transactional
  public void deleteById(UUID id) {
    this.findById(id);

    log.debug(
      "Deleting vectors and notice by id '{}'", 
      id
    );

    this.notices.deleteById(id);
    this.vectors.removeByNoticeId(id);

    log.debug(
      "Vectors and notice by id '{}' deleted", 
      id
    );
  };

  public Notice findById(UUID id) {
    return this.notices.findById(id)
      .orElseThrow(NoitceNotFound::new);
  };

  public Notice findByIdAndVersion(
    UUID id,
    Integer version
  ) {
    return this.notices.findByIdAndVersion(id, version)
      .orElseThrow(NoitceNotFound::new);
  };
};
