package ufrn.imd.notices.configurations;

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

import ufrn.imd.notices.configurations.reader.NoticeItemReader;
import ufrn.imd.notices.dto.RawData;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.services.NoticesService;


@Configuration
@EnableBatchProcessing
public class ExtractorConfiguration {
  private NoticesService notices;

  @Autowired
  public ExtractorConfiguration(
    NoticesService notices
  ) {
    this.notices = notices;
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
    ItemProcessor<Notice, RawData> extract, 
    ItemWriter<RawData> extractWriter
  ) {
    return new StepBuilder("extractStep", repository)
      .<Notice, RawData>chunk(4, manager)
      .reader(extractReader)
      .processor(extract)
      .writer(extractWriter)
      .build();
  };

  @Bean
  @StepScope
  public ItemReader<Notice> extractReader(
    @Value("#{jobParameters['uuid']}") String uuid,
    @Value("#{jobParameters['filename']}") String filename,
    @Value("#{jobParameters['created_at']}") Long createdAt,
    @Value("#{jobParameters['updated_at']}") Long updatedAt,
    @Value("#{jobParameters['extraction_finished']}") Boolean extractionFinished
  ) {
    return new NoticeItemReader(
      this.notices, 
      UUID.fromString(uuid),
      filename,
      new Timestamp(createdAt),
      new Timestamp(updatedAt),
      extractionFinished
    );
  };

  @Bean
  public ItemProcessor<Notice, RawData> extract() {
    return (source) -> {
      // TODO - Extractor
      return new RawData(
        source.getUuid(),
        List.of()
      );
    };
  };
  
  @Bean
  public ItemWriter<RawData> extractWriter() {
    return (chunk) -> {
      chunk
        .getItems()
        .forEach((data) -> {
          // this.notices.addSummariesById(
          //   data.uuid(),
          //   data.vectors()
          // );
        });
    };
  };
};
