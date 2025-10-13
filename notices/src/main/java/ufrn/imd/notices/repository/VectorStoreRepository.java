package ufrn.imd.notices.repository;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.filter.Filter.ExpressionType;
import org.springframework.ai.vectorstore.filter.Filter.Key;
import org.springframework.ai.vectorstore.filter.Filter.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.graphbuilder.math.Expression;

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

  public void removeByNoticeId(Long id) {
    Filter.Expression expression = new FilterExpressionBuilder()
      .eq("notice", id)
      .build();
    
    this.store.delete(expression);
  };

  public Filter.Expression expressionByNoticeIdAndVersion(Long id, Long version) {
    return new FilterExpressionBuilder()
      .and(
        new FilterExpressionBuilder().eq("notice", id),
        new FilterExpressionBuilder().eq("version", version)
      ).build();
  };
};
