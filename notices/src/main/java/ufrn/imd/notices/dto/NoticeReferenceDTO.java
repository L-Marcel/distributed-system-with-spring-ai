package ufrn.imd.notices.dto;

import java.util.UUID;

public record NoticeReferenceDTO(
  UUID id,
  Integer version
) {};
