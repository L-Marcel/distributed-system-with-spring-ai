package ufrn.imd.extractions.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
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
import ufrn.imd.extractions.dto.NoticeReferenceDTO;
import ufrn.imd.extractions.models.Notice;

@Service
public class ExtractionService {
  private static final Logger log = LoggerFactory.getLogger(
    ExtractionService.class
  );

  private Prompts prompts;
  private ObjectWriter writer;
  private ChatClient client;
  private SyncMcpToolCallbackProvider tools;
  private JobLauncher launcher;
  private Job extract;
  
  @Autowired
  public ExtractionService(
    @Lazy JobLauncher launcher,
    @Lazy Job extract,
    SyncMcpToolCallbackProvider tools,
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
  };

  public void request(Notice notice) {
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
    } catch (Exception e) {
      e.printStackTrace();
    };
  };

  public void extract(Notice notice) throws JsonProcessingException {
    NoticeReferenceDTO reference = new NoticeReferenceDTO(
      notice.getId(),
      notice.getVersion(),
      notice.getType(),
      notice.getStatus()
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

    String content = this.client
      .prompt()
      .toolCallbacks(this.tools)
      .system(
        this.prompts.get("system_extract_notice")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_extract_notice")
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
