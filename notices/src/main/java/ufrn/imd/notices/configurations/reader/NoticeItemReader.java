package ufrn.imd.notices.configurations.reader;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import lombok.AllArgsConstructor;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.services.NoticesService;

@AllArgsConstructor
public class NoticeItemReader implements ItemReader<Notice> {
  private NoticesService notices;
  private UUID uuid;
  private String filename;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private Boolean extractionFinished;
  
  @Override
  public Notice read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    return this.notices.findByIdAndFilenameAndTimestampsAndExtraction(
      this.uuid,
      this.filename,
      this.createdAt,
      this.updatedAt,
      this.extractionFinished
    );
  };
};
