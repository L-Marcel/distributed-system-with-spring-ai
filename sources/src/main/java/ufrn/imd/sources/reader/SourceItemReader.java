package ufrn.imd.sources.reader;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import ufrn.imd.sources.models.Source;
import ufrn.imd.sources.services.SourcesService;

public class SourceItemReader implements ItemReader<Source> {
  private SourcesService sources;
  private UUID uuid;
  private String filename;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public SourceItemReader(
    SourcesService sources,
    UUID uuid,
    String filename,
    Timestamp createdAt,
    Timestamp updatedAt
  ) {
    this.sources = sources;
    this.uuid = uuid;
    this.filename = filename;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  };

  @Override
  public Source read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    return this.sources.findByIdAndFilenameAndTimestamps(
      this.uuid,
      this.filename,
      this.createdAt,
      this.updatedAt
    );
  };
};
