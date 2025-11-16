package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedResponsibleDTO(
  @ToolParam(
    required = false,
    description = "Nome do responsável."
  ) String name,

  @ToolParam(
    required = false,
    description = "Email do responsável."
  ) String email,

  @ToolParam(
    required = false,
    description = "Telefone do responsável, de preferência no formato (DDD) XXXXX-XXXX."
  ) String phone
) {};
