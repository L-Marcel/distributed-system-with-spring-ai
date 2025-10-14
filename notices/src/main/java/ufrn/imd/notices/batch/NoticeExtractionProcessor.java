package ufrn.imd.notices.batch;

import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import ufrn.imd.notices.dto.extraction.ExtractedContractDTO;
import ufrn.imd.notices.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.models.enums.NoticeType;
import ufrn.imd.notices.services.ExtractionService;

@AllArgsConstructor
public class NoticeExtractionProcessor implements ItemProcessor<Notice, Notice> {
  private ExtractionService extraction;

  @Override
  public Notice process(@NonNull Notice notice) throws Exception {
    // TODO - Extraction
    return notice;
  };
};
