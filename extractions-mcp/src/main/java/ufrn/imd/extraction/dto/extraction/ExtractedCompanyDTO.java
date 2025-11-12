package ufrn.imd.extraction.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedCompanyDTO(
  @ToolParam(
    required = false,
    description = "CNPJ da empresa."
  ) String cnpj,

  @ToolParam(
    required = false,
    description = "Nome legal da empresa."
  ) String legalName,

  @ToolParam(
    required = false,
    description = "Nome comercial da empresa."
  ) String tradeName,

  @ToolParam(
    required = false,
    description = "Telefone da empresa, de preferência no formato (DDD) XXXXX-XXXX."
  ) String phone,
  
  @ToolParam(
    required = false,
    description = "Email da empresa."
  ) String email
) {};
