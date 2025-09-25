package ufrn.imd.ai_service.services;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.ai_service.repository.VectorStoreRepository;

@Service
public class DocumentsService {
  private TokenTextSplitter splitter;
  private VectorStoreRepository repository;

  @Autowired
  public DocumentsService(
    TokenTextSplitter splitter,
    VectorStoreRepository repository
  ) {
    this.splitter = splitter;
    this.repository = repository;
  };

  public List<Document> read(MultipartFile file) {
    TikaDocumentReader reader = new TikaDocumentReader(file.getResource());
    return reader.read();
  };

  public List<Document> split(List<Document> documents) {
    return this.splitter.apply(documents);
  };

  public void registry(List<Document> documents) {
    this.repository.add(documents);
  };
};
