package ufrn.imd.extractions.batch;

import java.util.UUID;

import org.springframework.batch.item.ItemReader;

import ufrn.imd.commons.models.Notice;
import ufrn.imd.extractions.services.ExtractionsService;

public class NoticeItemReader implements ItemReader<Notice> {
  private ExtractionsService extractions;
  private UUID id;
  private Boolean read;

  public NoticeItemReader(
    ExtractionsService extractions,
    UUID id
  ) {
    this.extractions = extractions;
    this.id = id;
    this.read = false;
  };
  
  @Override
  public Notice read() throws Exception {
    if(this.read) return null;
    Notice notice = this.extractions.findById(this.id);
    this.read = true;
    return notice;
  };
};
