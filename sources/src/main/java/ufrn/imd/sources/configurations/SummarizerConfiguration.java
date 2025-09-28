package ufrn.imd.sources.configurations;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import ufrn.imd.sources.dto.Summary;
import ufrn.imd.sources.models.Source;
import ufrn.imd.sources.reader.SourceItemReader;
import ufrn.imd.sources.services.SourcesService;


@Configuration
@EnableBatchProcessing
public class SummarizerConfiguration {
  private SourcesService sources;

  @Autowired
  public SummarizerConfiguration(
    SourcesService sources
  ) {
    this.sources = sources;
  };

  @Bean
  public Job summarization(JobRepository repository, Step step) {
    return new JobBuilder("summarization", repository)
      .incrementer(new RunIdIncrementer())
      .flow(step)
      .end()
      .build();
  };

  @Bean
  public Step summarizationStep(
    JobRepository repository, 
    PlatformTransactionManager manager,
    ItemReader<Source> summarizationReader, 
    ItemProcessor<Source, Summary> summarize, 
    ItemWriter<Summary> summarizationWriter
  ) {
    return new StepBuilder("summarizationStep", repository)
      .<Source, Summary>chunk(4, manager)
      .reader(summarizationReader)
      .processor(summarize)
      .writer(summarizationWriter)
      .build();
  };

  @Bean
  @StepScope
  public ItemReader<Source> summarizationReader(
    @Value("#{jobParameters['uuid']}") String uuid,
    @Value("#{jobParameters['filename']}") String filename,
    @Value("#{jobParameters['created_at']}") Long createdAt,
    @Value("#{jobParameters['updated_at']}") Long updatedAt
  ) {
    return new SourceItemReader(
      this.sources, 
      UUID.fromString(uuid),
      filename,
      new Timestamp(createdAt),
      new Timestamp(updatedAt)
    );
  };

  @Bean
  public ItemProcessor<Source, Summary> summarize() {
    return (source) -> {
      // TODO - Summarize
      return new Summary(
        source.getUuid(),
        List.of()
      );
    };
  };
  
  @Bean
  public ItemWriter<Summary> summarizationWriter() {
    return (chunk) -> {
      chunk
        .getItems()
        .forEach((summary) -> {
          this.sources.addSummariesById(
            summary.uuid(),
            summary.documents()
          );
        });
    };
  };
};
