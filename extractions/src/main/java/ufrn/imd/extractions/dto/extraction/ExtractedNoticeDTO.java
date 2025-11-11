package ufrn.imd.extractions.dto.extraction;

import java.util.Set;
import java.util.UUID;

import ufrn.imd.extractions.models.enums.NoticeStatus;
import ufrn.imd.extractions.models.enums.NoticeType;

public record ExtractedNoticeDTO(
  UUID id,
  Integer version,
  NoticeStatus status,
  NoticeType type,
  ExtractedContractDTO contract,
  Set<ExtractedNoteDTO> notes
) {};