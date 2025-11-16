package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedCompanyDTO(
  @ToolParam(
    required = true,
    description = "CNPJ da empresa."
  ) String cnpj,

  @ToolParam(
    required = false,
    description = "Nome legal da empresa. Máximo de 200 caracteres."
  ) String legalName,

  @ToolParam(
    required = false,
    description = "Nome comercial da empresa. Máximo de 200 caracteres."
  ) String tradeName,

  @ToolParam(
    required = false,
    description = "Telefone da empresa, de preferência no formato (DDD) XXXXX-XXXX."
  ) String phone,
  
  @ToolParam(
    required = false,
    description = "Email da empresa. Máximo de 200 caracteres."
  ) String email
) {};
