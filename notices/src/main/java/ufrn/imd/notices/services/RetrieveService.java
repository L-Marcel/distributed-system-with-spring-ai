package ufrn.imd.notices.services;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.notices.repository.VectorStoreRepository;

@Service
public class RetrieveService {
  private ChatClient client;
  private VectorStoreRepository repository;

  @Autowired
  public RetrieveService(
    ChatClient.Builder builder,
    VectorStoreRepository repository
  ) {
    this.client = builder.build();
    this.repository = repository;
  };
  
  public String retrieve(UUID uuid, String prompt) {
    Filter.Expression expression = new FilterExpressionBuilder()
      .eq("uuid", uuid.toString())
      .build();
    
    SearchRequest request = SearchRequest
      .builder()
      .filterExpression(expression)
      .build();

    List<Advisor> advisors = List.of(
      QuestionAnswerAdvisor
        .builder(this.repository.getStore())
        .searchRequest(request)
        .build()
    );

    return this.client.prompt()
      .advisors(advisors)
      .user(prompt)
      .call()
      .content();
  };
};
