package ufrn.imd.notices.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.models.enums.NoticeType;
import ufrn.imd.notices.services.ExtractionService;

@AllArgsConstructor
public class NoticeExtractionProcessor implements ItemProcessor<Notice, Notice> {
  private ExtractionService extraction;

  @Override
  public Notice process(@NonNull Notice notice) throws Exception {
    NoticeType type = this.extraction.detectType(notice);
    System.out.println("TYPE " + type.name());
    return notice;
  };
};
