package ufrn.imd.notices.batch;

import java.util.UUID;

import org.springframework.batch.item.ItemReader;

import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.services.NoticesService;

public class NoticeItemReader implements ItemReader<Notice> {
  private NoticesService notices;
  private UUID id;
  private Long version;
  private Boolean read;

  public NoticeItemReader(
    NoticesService notices,
    UUID id,
    Long version
  ) {
    this.notices = notices;
    this.id = id;
    this.version = version;
    this.read = false;
  };
  
  @Override
  public Notice read() throws Exception {
    if(this.read) return null;
    Notice notice = this.notices.findByIdAndVersion(this.id, this.version);
    this.read = true;
    return notice;
  };
};
