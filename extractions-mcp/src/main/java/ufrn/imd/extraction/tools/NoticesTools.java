package ufrn.imd.extraction.tools;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ufrn.imd.extraction.Prompts;
import ufrn.imd.extraction.dto.NoticeReferenceDTO;
import ufrn.imd.extraction.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.extraction.repository.VectorStoreRepository;

@Component
public class NoticesTools {
  private static final Logger log = LoggerFactory.getLogger(
    NoticesTools.class
  );

  private ObjectWriter writer;
  private Prompts prompts;
  private ChatClient client;
  private ChatMemoryRepository momories;
  private ExtractionTools tools;

  @Autowired
  public NoticesTools(
    ObjectMapper mapper,
    Prompts prompts,
    ChatClient client,
    VectorStoreRepository vectors,
    ChatMemoryRepository memories,
    ExtractionTools tools
  ) {
    this.writer = mapper.writerWithDefaultPrettyPrinter();
    this.prompts = prompts;
    this.client = client;
    this.momories = memories;
    this.tools = tools;
  };

  @Tool(
    name = "extract_notice", 
    description = """
      Extrai todos os dados de um edital reaproveitando
      os dados anteriormente extraídos.
    """
  )
  public ExtractedNoticeDTO extract(
    NoticeReferenceDTO reference
  ) throws JsonProcessingException {
    ExtractedNoticeDTO result = new ExtractedNoticeDTO(
      reference.id(),
      reference.version(),
      reference.status(),
      reference.type(),
      null,
      null
    );

    String referenceJson = this.writer.writeValueAsString(
      reference
    );

    log.debug(
      """
      Tool 'extract_notice' requested with params:

      Reference: '{}'
      """,
      referenceJson
    );

    ChatMemory memory = MessageWindowChatMemory.builder()
      .chatMemoryRepository(this.momories)
      .maxMessages(12)
      .build();

    List<Advisor> advisors = List.of(
      MessageChatMemoryAdvisor
        .builder(memory)
        .conversationId(reference.id().toString())
        .build()
    );

    String content = this.client.prompt()
      .advisors(advisors)
      .system(
        this.prompts.get("system_extract_notice")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_extract_notice")
        ).param("reference", referenceJson)
      ).tools(this.tools)
      .call()
      .content();
    
    log.debug(
      "Extraction result: \n{}", 
      content
    );

    return result;
  };
};
