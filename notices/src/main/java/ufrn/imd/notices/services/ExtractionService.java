package ufrn.imd.notices.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import ufrn.imd.notices.agents.Prompts;
import ufrn.imd.notices.dto.ContractDTO;
import ufrn.imd.notices.dto.ExtractedNoticeTypeDTO;
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
  private VectorStoreRepository vectors;
  private JobLauncher launcher;
  private Job extract;

  @Autowired
  public ExtractionService(
    Prompts prompts,
    ChatClient client,
    ChatMemoryRepository memory,
    VectorStoreRepository vectors,
    @Lazy JobLauncher launcher,
    @Lazy Job extract
  ) {
    this.prompts = prompts;
    this.client = client;
    this.vectors = vectors;
    this.launcher = launcher;
    this.extract = extract;
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

  public NoticeType extractType(Notice notice) {
    if(!notice.getType().equals(NoticeType.UNKNOWN))
      return notice.getType();

    log.debug(
      "Trying extract type of notice with id '{}'", 
      notice.getId()
    );

    SearchRequest request = this.vectors.searchByNoticeIdAndVersion(
      notice.getId(),
      notice.getVersion()
    );

    List<Advisor> advisors = List.of(
      QuestionAnswerAdvisor
        .builder(this.vectors.getStore())
        .searchRequest(request)
        .build()
    );
    
    NoticeType type = this.client.prompt()
      .advisors(advisors)
      .system(
        this.prompts.get("system_extract_type")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_extract_type")
        ).param("version", notice.getVersion())
        .param("id", notice.getId())
      ).call()
      .entity(ExtractedNoticeTypeDTO.class)
      .type();
    
    log.debug(
      "Extracted type of notice with id '{}' is '{}'", 
      notice.getId(),
      type
    );

    return type;
  };

  public Optional<ContractDTO> extractContract(Notice notice) {
    if(!notice.getType().equals(NoticeType.CONTRACT))
      return Optional.empty();

    log.debug(
      "Trying extract contract of notice with id '{}'", 
      notice.getId()
    );

    SearchRequest request = this.vectors.searchByNoticeIdAndVersion(
      notice.getId(),
      notice.getVersion()
    );

    List<Advisor> advisors = List.of(
      QuestionAnswerAdvisor
        .builder(this.vectors.getStore())
        .searchRequest(request)
        .build()
    );
    
    ContractDTO contract = this.client.prompt()
      .advisors(advisors)
      .system(
        this.prompts.get("system_extract_contract")
      ).user((prompt) -> 
        prompt.text(
          this.prompts.get("user_extract_contract")
        ).param("version", notice.getVersion())
        .param("id", notice.getId())
      ).call()
      .entity(ContractDTO.class);

    return Optional.of(contract);
  };
};
