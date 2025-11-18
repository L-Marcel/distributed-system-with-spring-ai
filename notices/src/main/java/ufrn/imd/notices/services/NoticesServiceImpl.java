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

import ufrn.imd.commons.errors.NoticeNotFound;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.models.enums.NoticeStatus;
import ufrn.imd.commons.models.enums.NoticeType;
import ufrn.imd.commons.repositories.NoticesRepository;
import ufrn.imd.commons.repositories.VectorStoreRepository;

@Service
public class NoticesServiceImpl implements NoticesService {
  private static final Logger log = LoggerFactory.getLogger(
    NoticesServiceImpl.class
  );

  private TokenTextSplitter splitter;
  private VectorStoreRepository vectors;
  private NoticesRepository notices;
  private ExtractionsService extractions;

  @Autowired
  public NoticesServiceImpl(
    TokenTextSplitter splitter,
    VectorStoreRepository vectors,
    NoticesRepository notices,
    ExtractionsService extractions
  ) {
    this.splitter = splitter;
    this.vectors = vectors;
    this.notices = notices;
    this.extractions = extractions;
  };

  private List<Document> read(Resource file) {
    log.debug(
      "Reading file '{}'", 
      file.getFilename()
    );

    TikaDocumentReader reader = new TikaDocumentReader(file);
    return reader.read();
  };

  private List<Document> split(
    List<Document> documents
  ) {
    return this.splitter.apply(documents);
  };

  @Override
  @Transactional
  public Notice create(
    Resource file,
    String filename,
    Long bytes
  ) {
    List<Document> documents = this.read(file);
    documents = this.split(documents);

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

    NoticeStatus status = this.extractions.request(notice.getId())
      .getBody();

    notice.setStatus(status);

    this.notices.save(notice);

    log.debug(
      "Notice with id '{}' updated to status '{}'",
      notice.getId(),
      notice.getStatus()
    );
    
    return notice;
  };

  @Override
  @Transactional
  public Notice update(
    UUID id,
    Resource file,
    String filename,
    Long bytes
  ) {
    List<Document> documents = this.read(file);
    documents = this.split(documents);

    Notice notice = this.findById(id);

    log.debug(
      "Updating notice with id '{}' and version '{}' from file '{}'",
      id,
      notice.getVersion(),
      filename
    );

    notice.setFilename(filename);
    notice.setType(NoticeType.UNKNOWN);
    notice.setStatus(NoticeStatus.PROCESSING);
    notice.setBytes(bytes);

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

    NoticeStatus status = this.extractions.request(notice.getId())
      .getBody();

    notice.setStatus(status);

    this.notices.save(notice);
    
    log.debug(
      "Notice with id '{}' updated to status '{}'",
      notice.getId(),
      notice.getStatus()
    );
    
    return notice;
  };
  
  @Override
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

  @Override
  public Notice findById(UUID id) {
    return this.notices.findById(id)
      .orElseThrow(NoticeNotFound::new);
  };
};
