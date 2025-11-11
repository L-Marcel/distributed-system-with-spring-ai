package ufrn.imd.extractions.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
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
  
  
  public VectorStoreRepository(
    VectorStore store
  ) {
    this.store = store;
  };

  public void add(List<Document> documents) {
    this.store.add(documents);
  };

  public void removeByNoticeId(UUID id) {
    Filter.Expression expression = new FilterExpressionBuilder()
      .eq("notice", id.toString())
      .build();
    
    this.store.delete(expression);
  };

  public Filter.Expression expressionByNoticeIdAndVersion(
    UUID id, 
    Integer version
  ) {
    return new FilterExpressionBuilder()
      .and(
        new FilterExpressionBuilder().eq("notice", id.toString()),
        new FilterExpressionBuilder().eq("version", version)
      ).build();
  };

  public SearchRequest searchByNoticeIdAndVersion(
    UUID id, 
    Integer version
  ) {
    return SearchRequest
      .builder()
      .filterExpression(this.expressionByNoticeIdAndVersion(
        id, 
        version
      )).build();
  };
};
