package ufrn.imd.extractions.dto;

import java.util.Set;
import java.util.UUID;

import ufrn.imd.extractions.models.Note;
import ufrn.imd.extractions.models.enums.NoticeStatus;
import ufrn.imd.extractions.models.enums.NoticeType;

public record NoticeReferenceWithNotesDTO(
  UUID id,
  Integer version,
  NoticeType type,
  NoticeStatus status,
  Set<Note> notes
) {};
