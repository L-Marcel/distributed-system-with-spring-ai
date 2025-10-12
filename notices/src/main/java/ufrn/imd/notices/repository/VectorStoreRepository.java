package ufrn.imd.notices.repository;

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

  public void removeById(UUID id) {
    Filter.Expression expression = new FilterExpressionBuilder()
      .in("id", id.toString())
      .build();
    
    this.store.delete(expression);
  };

  public void removeByIdAndVersion(UUID id, Long version) {
    Filter.Expression expression = this.expressionByIdAndVersion(
      id, 
      version
    );
    
    this.store.delete(expression);
  };

  public Filter.Expression expressionByIdAndVersion(UUID id, Long version) {
    // return new FilterExpressionBuilder().eq("version", version).build();
    return new FilterExpressionBuilder()
      .and(
        new FilterExpressionBuilder().in("id", id.toString()),
        new FilterExpressionBuilder().eq("version", version)
      ).build();
  };
};
