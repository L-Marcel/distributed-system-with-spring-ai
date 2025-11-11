package ufrn.imd.extractions.batch;

import java.util.UUID;

import org.springframework.batch.item.ItemReader;

import ufrn.imd.extractions.models.Notice;
import ufrn.imd.extractions.services.NoticesService;

public class NoticeItemReader implements ItemReader<Notice> {
  private NoticesService notices;
  private UUID id;
  private Integer version;
  private Boolean read;

  public NoticeItemReader(
    NoticesService notices,
    UUID id,
    Integer version
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
