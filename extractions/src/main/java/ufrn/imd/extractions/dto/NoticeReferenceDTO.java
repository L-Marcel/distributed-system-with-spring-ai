package ufrn.imd.extractions.dto;

import java.util.UUID;

import ufrn.imd.extractions.models.enums.NoticeStatus;
import ufrn.imd.extractions.models.enums.NoticeType;

public record NoticeReferenceDTO(
  UUID id,
  Integer version,
  NoticeType type,
  NoticeStatus status
) {};
