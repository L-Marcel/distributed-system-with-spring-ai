package ufrn.imd.extractions.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NoticeBasicReferenceDTO(
  @NotNull
  UUID id,

  @NotNull
  @Positive
  Integer version
) {};
