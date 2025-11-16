package ufrn.imd.extractions.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.extractions.services.ExtractionService;

@AllArgsConstructor
public class NoticeExtractionProcessor implements ItemProcessor<Notice, Notice> {
  private ExtractionService extraction;

  @Override
  public Notice process(@NonNull Notice notice) throws Exception {
    this.extraction.extract(notice);
    return notice;
  };
};
