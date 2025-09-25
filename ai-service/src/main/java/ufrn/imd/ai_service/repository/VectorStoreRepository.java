package ufrn.imd.ai_service.repository;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Getter;

@Repository
public class VectorStoreRepository {
  @Getter
  private VectorStore store;
  
  @Autowired
  public VectorStoreRepository(
    VectorStore store
  ) {
    this.store = store;
  };

  public void add(List<Document> documents) {
    this.store.add(documents);
  };
};
