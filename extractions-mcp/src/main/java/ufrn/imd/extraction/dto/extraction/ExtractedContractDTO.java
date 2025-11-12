package ufrn.imd.extraction.dto.extraction;

import java.util.UUID;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedContractDTO(
  @ToolParam(
    required = true,
    description = "UUID da nóticia."
  ) UUID notice,

  @ToolParam(
    required = true,
    description = "Versão da nóticia."
  ) Integer version,

  @ToolParam(
    required = false,
    description = "Valor do contrato."
  ) Double value,

  @ToolParam(
    required = false,
    description = "Moeda do valor do contrato. Ex: BRL."
  ) String currency,
  
  @ToolParam(
    required = false,
    description = "Data de início do contrato, no formato dd/MM/yyyy."
  ) String startDate,

  @ToolParam(
    required = false,
    description = "Data de termino do contrato, no formato dd/MM/yyyy."
  ) String endDate,

  @ToolParam(
    required = false,
    description = "Local repassado no contrato."
  ) String location,

  @ToolParam(
    required = false,
    description = "Dados da empresa contratante."
  ) ExtractedCompanyDTO hirer,

  @ToolParam(
    required = false,
    description = "Dados da empresa contratada."
  ) ExtractedCompanyDTO hired
) {};
