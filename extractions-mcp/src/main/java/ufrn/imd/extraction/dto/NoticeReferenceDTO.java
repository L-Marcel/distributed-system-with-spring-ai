package ufrn.imd.extraction.dto;

import java.util.UUID;

import ufrn.imd.extraction.models.enums.NoticeStatus;
import ufrn.imd.extraction.models.enums.NoticeType;

public record NoticeReferenceDTO(
  UUID id,
  Integer version,
  NoticeType type,
  NoticeStatus status
) {};
