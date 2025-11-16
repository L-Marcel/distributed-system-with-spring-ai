package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedAddressDTO(
  @ToolParam(
    required = false,
    description = "CNPJ da empresa."
  ) String cnpj,

  @ToolParam(
    required = false,
    description = "Rua da empresa."
  ) String street,

  @ToolParam(
    required = false,
    description = "Numero do endereço da empresa."
  ) Integer number,

  @ToolParam(
    required = false,
    description = "Bairro da empresa."
  ) String district,

  @ToolParam(
    required = false,
    description = "Complemento do endereço da empresa."
  ) String complement,

  @ToolParam(
    required = false,
    description = "Cidade da empresa."
  ) String city,

  @ToolParam(
    required = false,
    description = """
      UF (unidade federativa, representada normalmente 
      por duas siglas no formato XX) da empresa.
    """
  ) String uf,
  
  @ToolParam(
    required = false,
    description = "CEP da empresa."
  ) String cep
) {};
