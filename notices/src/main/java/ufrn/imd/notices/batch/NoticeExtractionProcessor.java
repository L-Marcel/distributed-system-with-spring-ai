package ufrn.imd.notices.batch;

import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import ufrn.imd.notices.dto.ContractDTO;
import ufrn.imd.notices.models.Notice;
import ufrn.imd.notices.models.enums.NoticeType;
import ufrn.imd.notices.services.ExtractionService;

@AllArgsConstructor
public class NoticeExtractionProcessor implements ItemProcessor<Notice, Notice> {
  private ExtractionService extraction;

  @Override
  public Notice process(@NonNull Notice notice) throws Exception {
    NoticeType type = this.extraction.extractType(notice);
    notice.setType(type);

    switch (type) {
      case COMMON:
        break;
      case CONTRACT:
        Optional<ContractDTO> contract = this.extraction.extractContract(
          notice
        );

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(
          objectMapper.writeValueAsString(contract)
        );

        break;
      case UNKNOWN:
      default:
        break;
    };
    
    return notice;
  };
};
