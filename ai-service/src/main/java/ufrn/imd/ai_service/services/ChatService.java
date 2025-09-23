package ufrn.imd.ai_service.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
  private ChatClient client;

  @Autowired
  private ChatService(
    ChatClient.Builder builder
  ) {
    this.client = builder.build();
  };
  
  public String ask(String prompt) {
    return this.client.prompt()
      .user(prompt)
      .call()
      .content();
  };
};
