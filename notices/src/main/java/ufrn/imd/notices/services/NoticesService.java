package ufrn.imd.notices.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.notices.errors.SourceNotFound;
import ufrn.imd.notices.errors.SourceVersionNotFound;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.repository.NoticesRepository;
import ufrn.imd.notices.repository.VectorStoreRepository;

@Service
public class NoticesService {
  private TokenTextSplitter splitter;
  private VectorStoreRepository vectors;
  private NoticesRepository notices;
  private JobLauncher launcher;
  private Job extraction;

  @Autowired
  public NoticesService(
    TokenTextSplitter splitter,
    VectorStoreRepository vectors,
    ChatMemoryRepository memory,
    NoticesRepository notices,
    JobLauncher launcher,
    Job extraction
  ) {
    this.splitter = splitter;
    this.vectors = vectors;
    this.notices = notices;
    this.launcher = launcher;
    this.extraction = extraction;
  };

  public List<Document> read(Resource file) {
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
    Notice notice = new Notice();
    notice.setFilename(filename);
    notice.setBytes(bytes);
    this.notices.save(notice);

    for(Document document : documents) {
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("uuid", notice.getUuid().toString());
    };

    this.vectors.add(documents);
    return notice;
  };

  @Transactional
  public void deleteById(UUID uuid) {
    this.notices.deleteById(uuid);
    this.vectors.removeById(uuid);
  };

  public void requestExtraction(Notice notice) {
    JobParameters parameters = new JobParametersBuilder()
      .addString("uuid", notice.getUuid().toString())
      .addString("filename", notice.getFilename())
      .addLong("updated_at", notice.getUpdatedAt().getTime())
      .toJobParameters();
    
    try {
      this.launcher.run(this.extraction, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    };
  };

  public Notice findById(UUID uuid) {
    Optional<Notice> notice = this.notices.findById(uuid);
    if(!notice.isPresent()) throw new SourceNotFound();
    return notice.get();
  };

  public Notice findByIdAndFilenameAndTimestampsAndExtraction(
    UUID uuid,
    String filename,
    Timestamp createdAt,
    Timestamp updatedAt,
    Boolean extractionCompleted
  ) {
    Optional<Notice> source = this.notices.findByIdAndFilenameAndCreatedAtAndUpdatedAtAndExtractionFinished(
      uuid,
      filename,
      createdAt,
      updatedAt,
      extractionCompleted
    );

    if(!source.isPresent()) throw new SourceVersionNotFound();
    return source.get();
  };
};
