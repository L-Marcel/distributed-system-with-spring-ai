package ufrn.imd.notices.dto;

import java.util.UUID;

public record NoticeBasicReferenceDTO(
  UUID id,
  Integer version
) {};
