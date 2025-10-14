package ufrn.imd.notices.dto;

import java.util.UUID;

import ufrn.imd.notices.models.enums.NoticeStatus;
import ufrn.imd.notices.models.enums.NoticeType;

public record NoticeReferenceDTO(
  UUID id,
  Integer version,
  NoticeType type,
  NoticeStatus status
) {};
