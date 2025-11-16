package ufrn.imd.extractions.batch;

import java.util.UUID;

import org.springframework.batch.item.ItemReader;

import ufrn.imd.commons.models.Notice;
import ufrn.imd.extractions.services.NoticesService;

public class NoticeItemReader implements ItemReader<Notice> {
  private NoticesService notices;
  private UUID id;
  private Boolean read;

  public NoticeItemReader(
    NoticesService notices,
    UUID id
  ) {
    this.notices = notices;
    this.id = id;
    this.read = false;
  };
  
  @Override
  public Notice read() throws Exception {
    if(this.read) return null;
    Notice notice = this.notices.findById(this.id);
    this.read = true;
    return notice;
  };
};
