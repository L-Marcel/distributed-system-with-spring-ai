package ufrn.imd.notices.configurations;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentsConfiguration {
  @Bean
  public TokenTextSplitter tokenTextSplitter() {
    return new TokenTextSplitter();
  };
};
