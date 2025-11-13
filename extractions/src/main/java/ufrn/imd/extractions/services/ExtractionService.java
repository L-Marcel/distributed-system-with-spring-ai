package ufrn.imd.extractions.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ufrn.imd.extractions.Prompts;
import ufrn.imd.extractions.dto.NoticeDTO;
import ufrn.imd.extractions.models.Notice;
import ufrn.imd.extractions.models.enums.NoticeStatus;
import ufrn.imd.extractions.repository.VectorStoreRepository;

@Service
public class ExtractionService {
  private static final Logger log = LoggerFactory.getLogger(
    ExtractionService.class
  );

  private Prompts prompts;
  private ObjectWriter writer;
  private ChatClient client;
  private SyncMcpToolCallbackProvider tools;
  private VectorStoreRepository vectors;
  private ChatMemoryRepository momories;
  private JobLauncher launcher;
  private Job extract;
  
  @Autowired
  public ExtractionService(
    @Lazy JobLauncher launcher,
    @Lazy Job extract,
    SyncMcpToolCallbackProvider tools,
    ChatMemoryRepository momories,
    VectorStoreRepository vectors,
    ObjectMapper mapper,
    ChatClient client,
    Prompts prompts
  ) {
    this.launcher = launcher;
    this.extract = extract;
    this.tools = tools;
    this.client = client;
    this.writer = mapper.writerWithDefaultPrettyPrinter();
    this.prompts = prompts;
    this.vectors = vectors;
  };

  public NoticeStatus request(Notice notice) {
    log.debug(
      "Requesting notice extraction by id '{}' and version '{}'", 
      notice.getId(),
      notice.getVersion()
    );

    JobParameters parameters = new JobParametersBuilder()
      .addString("id", notice.getId().toString())
      .addString("version", notice.getVersion().toString())
      .toJobParameters();
    
    try {
      this.launcher.run(this.extract, parameters);

      log.debug(
        "Notice extraction requested by id '{}' and version '{}'", 
        notice.getId(),
        notice.getVersion()
      );

      return NoticeStatus.PROCESSING;
    } catch (Exception e) {
      e.printStackTrace();
      return NoticeStatus.STOPPED;
    }
  };

  public void extract(Notice notice) throws JsonProcessingException {
    NoticeDTO reference = new NoticeDTO(
      notice.getId(),
      notice.getVersion(),
      notice.getType(),
      notice.getStatus(),
      notice.getNotes()
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
      RetrievalAugmentationAdvisor.builder()
        .queryTransformers(
          RewriteQueryTransformer.builder()
            .chatClientBuilder(client.mutate())
            .build()
        ).documentRetriever(VectorStoreDocumentRetriever.builder()
          .similarityThreshold(0.50)
          .filterExpression(this.vectors.expressionByNoticeIdAndVersion(
            reference.id(),
            reference.version()
          ))
          .vectorStore(this.vectors.getStore())
          .build())
        .build(),
      MessageChatMemoryAdvisor
        .builder(memory)
        .conversationId(reference.id().toString())
        .build()
    );

    String content = this.client
      .prompt()
      .toolCallbacks(this.tools)
      .advisors(advisors)
      .system(
        this.prompts.get("system_notice")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_notice")
        ).param("reference", referenceJson)
      )
      .call()
      .content();
    
    log.debug(
      "Extraction result: \n{}", 
      content
    );
  };
};
