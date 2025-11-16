package ufrn.imd.extractions.configurations;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import ufrn.imd.extractions.batch.NoticeExtractionProcessor;
import ufrn.imd.extractions.batch.NoticeItemReader;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.extractions.services.ExtractionService;
import ufrn.imd.extractions.services.NoticesService;

@Configuration
public class BatchConfiguration extends DefaultBatchConfiguration {
  private ExtractionService extraction;
  private NoticesService notices;

  public BatchConfiguration(
    ExtractionService extraction,
    NoticesService notices
  ) {
    this.extraction = extraction;
    this.notices = notices;
  };

  @Bean
  public JobLauncher launcher(JobRepository repository) throws Exception {
    TaskExecutorJobLauncher laucher = new TaskExecutorJobLauncher();
    laucher.setJobRepository(repository);
    laucher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    laucher.afterPropertiesSet();
    return laucher;
  };

  @Bean
  public Job extract(JobRepository repository, Step step) {
    return new JobBuilder("extract", repository)
      .incrementer(new RunIdIncrementer())
      .flow(step)
      .end()
      .build();
  };

  @Bean
  public Step extractStep(
    JobRepository repository, 
    PlatformTransactionManager manager,
    ItemReader<Notice> extractReader, 
    ItemProcessor<Notice, Notice> extractProcessor
  ) {
    return new StepBuilder("extractStep", repository)
      .<Notice, Notice>chunk(4, manager)
      .reader(extractReader)
      .processor(extractProcessor)
      .writer((chunks) -> {})
      .build();
  };

  @Bean
  @StepScope
  public ItemReader<Notice> extractReader(
    @Value("#{jobParameters['id']}") String id
  ) {
    return new NoticeItemReader(
      this.notices, 
      UUID.fromString(id)
    );
  };

  @Bean
  public ItemProcessor<Notice, Notice> extractProcessor() {
    return new NoticeExtractionProcessor(this.extraction);
  };
};
