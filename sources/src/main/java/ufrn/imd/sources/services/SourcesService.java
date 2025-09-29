package ufrn.imd.sources.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

import ufrn.imd.sources.errors.SourceNotFound;
import ufrn.imd.sources.errors.SourceVersionNotFound;
import ufrn.imd.sources.models.Source;
import ufrn.imd.sources.repository.SourcesRepository;
import ufrn.imd.sources.repository.VectorStoreRepository;

@Service
public class SourcesService {
  private TokenTextSplitter splitter;
  private VectorStoreRepository vectors;
  private SourcesRepository sources;
  private JobLauncher launcher;
  private Job summarization;

  @Autowired
  public SourcesService(
    TokenTextSplitter splitter,
    VectorStoreRepository vectors,
    SourcesRepository sources,
    JobLauncher launcher,
    Job summarization
  ) {
    this.splitter = splitter;
    this.vectors = vectors;
    this.sources = sources;
    this.launcher = launcher;
    this.summarization = summarization;
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
  public Source create(
    List<Document> documents,
    String filename,
    Long bytes
  ) {
    Source source = new Source();
    source.setFilename(filename);
    source.setBytes(bytes);
    this.sources.save(source);

    for(Document document : documents) {
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("uuid", source.getUuid().toString());
      metadata.put("summarized", false);
    };

    this.vectors.add(documents);
    return source;
  };

  @Transactional
  public void deleteById(UUID uuid) {
    this.sources.deleteById(uuid);
    this.vectors.removeById(uuid);
  };

  public void addSummariesById(UUID uuid, List<Document> documents) {
    for(Document document : documents) {
      Map<String, Object> metadata = document.getMetadata();
      metadata.put("uuid", uuid.toString());
      metadata.put("summarized", true);
    };

    this.vectors.add(documents);
  };

  public void requestSummarization(Source source) {
    JobParameters parameters = new JobParametersBuilder()
      .addString("uuid", source.getUuid().toString())
      .addString("filename", source.getFilename())
      .addLong("created_at", source.getCreatedAt().getTime())
      .addLong("updated_at", source.getUpdatedAt().getTime())
      .toJobParameters();
    
    try {
      this.launcher.run(this.summarization, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    };
  };

  public void requestSummarizationById(UUID uuid) {
    Optional<Source> source = this.sources.findById(uuid);
    if(!source.isPresent()) throw new SourceNotFound();
    this.requestSummarization(source.get());
  };

  public Source findById(UUID uuid) {
    Optional<Source> source = this.sources.findById(uuid);
    if(!source.isPresent()) throw new SourceNotFound();
    return source.get();
  };

  public Source findByIdAndFilenameAndTimestamps(
    UUID uuid,
    String filename,
    Timestamp createdAt,
    Timestamp updatedAt
  ) {
    Optional<Source> source = this.sources.findByIdAndFilenameAndCreatedAtAndUpdatedAt(
      uuid,
      filename,
      createdAt,
      updatedAt
    );

    if(!source.isPresent()) throw new SourceVersionNotFound();
    return source.get();
  };
};
