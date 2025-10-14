package ufrn.imd.notices.configurations;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import ufrn.imd.notices.agents.NoticesTools;

@Configuration
public class AiConfiguration {
  @Bean
  @Primary
  public ChatClient chatClient(ChatClient.Builder builder) {
    ChatOptions chatOptions = ChatOptions
      .builder()
      .model("gpt-4o-mini")
      .build();
    
    return builder
      .defaultOptions(chatOptions)
      .build();
  };

  @Bean
  public ChatClient extractionChatClient(
    ChatClient.Builder builder,
    NoticesTools tools
  ) {
    ChatOptions chatOptions = ChatOptions
      .builder()
      .model("gpt-4o-mini")
      .build();
    
    return builder
      .defaultOptions(chatOptions)
      .defaultTools(tools)
      .build();
  };
};
