package ufrn.imd.commons.dto;

import java.util.Set;
import java.util.UUID;

import ufrn.imd.commons.models.Note;
import ufrn.imd.commons.models.enums.NoticeStatus;
import ufrn.imd.commons.models.enums.NoticeType;

public record NoticeDTO(
  UUID id,
  Integer version,
  NoticeType type,
  NoticeStatus status,
  Set<Note> notes
) {};
