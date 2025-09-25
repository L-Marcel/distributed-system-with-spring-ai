package ufrn.imd.ai_service.services;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.ai_service.repository.VectorStoreRepository;

@Service
public class ChatsService {
  private ChatClient client;
  private VectorStoreRepository repository;

  @Autowired
  private ChatsService(
    ChatClient.Builder builder,
    VectorStoreRepository repository
  ) {
    this.client = builder.build();
    this.repository = repository;
  };
  
  public String ask(String prompt) {
    List<Advisor> advisors = List.of(
      new QuestionAnswerAdvisor(repository.getStore())
    );

    return this.client.prompt()
      .advisors(advisors)
      .user(prompt)
      .call()
      .content();
  };
};
