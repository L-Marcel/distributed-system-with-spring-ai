package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedResponsibleDTO(
  @ToolParam(
    required = false,
    description = "Nome do responsável. Máximo de 200 caracteres."
  ) String name,

  @ToolParam(
    required = false,
    description = "Email do responsável. Máximo de 200 caracteres."
  ) String email,

  @ToolParam(
    required = false,
    description = "Telefone do responsável, de preferência no formato (DDD) XXXXX-XXXX."
  ) String phone
) {};
