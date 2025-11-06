package ufrn.imd.extraction.configurations;

import java.net.http.HttpClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import ufrn.imd.extraction.tools.NoticesTools;

@Configuration
public class AiConfiguration {
  @Value("${spring.ai.openai.api-key}")
  private String apiKey;

  @Value("${spring.ai.openai.base-url}")
  private String baseUrl;

  @Value("${spring.ai.openai.embedding.options.model}")
  private String embeddingsModel;

  @Bean
  public OpenAiApi openAiApi() {
    HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    JdkClientHttpRequestFactory jdkClientHttpRequestFactory = new JdkClientHttpRequestFactory(httpClient);

    return OpenAiApi.builder()
      .apiKey(this.apiKey)
      .baseUrl(this.baseUrl)
      .restClientBuilder(
        RestClient.builder()
          .requestFactory(jdkClientHttpRequestFactory)
      ).build();
  };

  @Bean
  public ChatClient.Builder builder(OpenAiApi openAiApi) {
    OpenAiChatModel chatModel = OpenAiChatModel.builder()
      .openAiApi(openAiApi)
      .build();

    return ChatClient.builder(chatModel);
  };

  @Bean
  public EmbeddingModel embeddingModel(OpenAiApi openAiApi) {
    return new OpenAiEmbeddingModel(openAiApi, MetadataMode.EMBED, OpenAiEmbeddingOptions.builder()
      .model(this.embeddingsModel)
      .build());
  };
    
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
