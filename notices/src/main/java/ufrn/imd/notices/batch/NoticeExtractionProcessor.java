package ufrn.imd.notices.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.services.ExtractionService;

@AllArgsConstructor
public class NoticeExtractionProcessor implements ItemProcessor<Notice, Notice> {
  private ExtractionService extraction;

  @Override
  public Notice process(@NonNull Notice notice) throws Exception {
    this.extraction.extract(notice);
    return notice;
  };
};
