package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedAddressDTO(
  @ToolParam(
    required = true,
    description = "CNPJ da empresa."
  ) String cnpj,

  @ToolParam(
    required = false,
    description = "Rua da empresa. Máximo de 200 caracteres."
  ) String street,

  @ToolParam(
    required = false,
    description = "Numero do endereço da empresa."
  ) Integer number,

  @ToolParam(
    required = false,
    description = "Bairro da empresa. Máximo de 200 caracteres."
  ) String district,

  @ToolParam(
    required = false,
    description = "Complemento do endereço da empresa. Máximo de 200 caracteres."
  ) String complement,

  @ToolParam(
    required = false,
    description = "Cidade da empresa. Máximo de 200 caracteres."
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
