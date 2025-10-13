package ufrn.imd.notices.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.notices.agents.Prompts;
import ufrn.imd.notices.dto.DetectNoticeTypeResult;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.models.enums.NoticeType;
import ufrn.imd.notices.repository.VectorStoreRepository;

@Service
public class ExtractionService {
  private static final Logger log = LoggerFactory.getLogger(
    ExtractionService.class
  );

  private Prompts prompts;
  private ChatClient client;
  private ChatMemoryRepository memory;
  private VectorStoreRepository vectors;

  @Autowired
  public ExtractionService(
    Prompts prompts,
    ChatClient client,
    ChatMemoryRepository memory,
    VectorStoreRepository vectors
  ) {
    this.prompts = prompts;
    this.client = client;
    this.memory = memory;
    this.vectors = vectors;
  };

  public String ask(String prompt) {
    return this.client.prompt()
      .user(prompt)
      .call()
      .content();
  };

  public NoticeType detectType(Notice notice) {
    if(!notice.getType().equals(NoticeType.UNKNOWN))
      return notice.getType();

    Filter.Expression expression = this.vectors.expressionByNoticeIdAndVersion(
      notice.getId(),
      notice.getVersion()
    );

    SearchRequest request = SearchRequest
      .builder()
      .filterExpression(expression)
      .build();

    List<Advisor> advisors = List.of(
      QuestionAnswerAdvisor
        .builder(this.vectors.getStore())
        .searchRequest(request)
        .build()
    );
    
    return this.client.prompt()
      .advisors(advisors)
      .system(
        this.prompts.get("system_detect_type")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_detect_type")
        ).param("version", notice.getVersion())
        .param("id", notice.getId())
      ).call()
      .entity(DetectNoticeTypeResult.class)
      .type();
  };
};
