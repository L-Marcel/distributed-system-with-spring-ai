package ufrn.imd.notices.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
  private JobLauncher launcher;
  private Job extract;

  @Autowired
  public NoticesService(
    TokenTextSplitter splitter,
    VectorStoreRepository vectors,
    //ChatMemoryRepository memory,
    NoticesRepository notices,
    @Lazy JobLauncher launcher,
    @Lazy Job extract
  ) {
    this.splitter = splitter;
    this.vectors = vectors;
    this.notices = notices;
    this.launcher = launcher;
    this.extract = extract;
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

    for(Document document : documents) {
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("id", notice.getId());
      metadata.put("version", notice.getVersion());
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
    log.debug(
      "Updating notice with id '{}'' from file '{}'",
      id,
      filename
    );

    Notice notice = this.findById(id);
    notice.setFilename(filename);
    notice.setType(NoticeType.UNKNOWN);
    // TODO - tirar isso depois
    notice.setBytes(bytes + System.currentTimeMillis());
    this.notices.save(notice);

    log.debug(
      "Notice with id '{}' update dfrom file '{}'", 
      notice.getId(),
      filename
    );

    for(Document document : documents) {
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("id", notice.getId());
      metadata.put("version", notice.getVersion());
    };

    log.debug(
      "Updating vectors from notice '{}'",
      notice.getId()
    );

    this.vectors.add(documents);
    this.vectors.removeByIdAndVersion(id, notice.getVersion());

    log.debug(
      "Vectors from notice '{}' updated",
      notice.getId()
    );

    return notice;
  };

  @Transactional
  public void deleteById(UUID id) {
    log.debug(
      "Deleting vectors and notice by id '{}'", 
      id
    );

    this.notices.deleteById(id);
    this.vectors.removeById(id);

    log.debug(
      "Vectors and notice by id '{}' deleted", 
      id
    );
  };

  public void requestExtraction(Notice notice) {
    log.debug(
      "Requesting notice extraction by id '{}' and version '{}'", 
      notice.getId(),
      notice.getVersion()
    );

    JobParameters parameters = new JobParametersBuilder()
      .addString("id", notice.getId().toString())
      .addLong("version", notice.getVersion())
      .toJobParameters();
    
    try {
      this.launcher.run(this.extract, parameters);

      log.debug(
        "Notice extraction requested by id '{}' and version '{}'", 
        notice.getId(),
        notice.getVersion()
      );
    } catch (Exception e) {
      e.printStackTrace();
    };
  };

  public Notice findById(UUID id) {
    return this.notices.findById(id)
      .orElseThrow(NoitceNotFound::new);
  };

  public Notice findByIdAndVersion(
    UUID id,
    Long version
  ) {
    return this.notices.findByIdAndVersion(id, version)
      .orElseThrow(NoitceNotFound::new);
  };
};
