package ufrn.imd.sources.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
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

  public void removeById(UUID uuid) {
    Filter.Expression expression = new FilterExpressionBuilder()
      .eq("uuid", uuid.toString())
      .build();
    
    this.store.delete(expression);
  };
};
