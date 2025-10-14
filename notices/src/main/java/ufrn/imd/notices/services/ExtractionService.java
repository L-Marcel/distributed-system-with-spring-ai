package ufrn.imd.notices.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ufrn.imd.notices.agents.NoticesTools;
import ufrn.imd.notices.dto.NoticeReferenceDTO;
import ufrn.imd.notices.models.Notice;

@Service
public class ExtractionService {
  private static final Logger log = LoggerFactory.getLogger(
    ExtractionService.class
  );

  private NoticesTools tools;
  private JobLauncher launcher;
  private Job extract;

  @Autowired
  public ExtractionService(
    NoticesTools tools,
    @Lazy JobLauncher launcher,
    @Lazy Job extract
  ) {
    this.tools = tools;
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

  public void extract(Notice notice) throws JsonProcessingException {
    NoticeReferenceDTO reference = new NoticeReferenceDTO(
      notice.getId(),
      notice.getVersion(),
      notice.getType(),
      notice.getStatus()
    );

    this.tools.extract(reference);
  };
};
