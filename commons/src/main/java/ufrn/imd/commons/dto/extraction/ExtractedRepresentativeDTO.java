package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedRepresentativeDTO(
  @ToolParam(
    required = false,
    description = "CNPJ da empresa."
  ) String cnpj,

  @ToolParam(
    required = false,
    description = "Nome do representante."
  ) String name,

  @ToolParam(
    required = false,
    description = "Email do representante."
  ) String email,

  @ToolParam(
    required = false,
    description = "Telefone do representante, de preferência no formato (DDD) XXXXX-XXXX."
  ) String phone
) {};
