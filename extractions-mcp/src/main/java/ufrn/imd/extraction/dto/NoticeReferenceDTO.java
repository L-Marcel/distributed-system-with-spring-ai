package ufrn.imd.extraction.dto;

import java.util.UUID;

import org.springframework.ai.tool.annotation.ToolParam;

public record NoticeReferenceDTO(
  @ToolParam(
    required = true,
    description = "UUID da nóticia."
  ) UUID id,

  @ToolParam(
    required = true,
    description = "Versão da nóticia."
  ) Integer version,
  
  @ToolParam(
    required = true,
    description = """
      O novo status da nóticia. Informe apenas um dos seguintes: 
        \"PROCESSING\", 
        \"UNKNOWN_TYPE\", 
        \"WAITING_MORE_DATA,\" 
        \"UNPUBLISHED\" ou 
        \"MISSING_EMAIL\".
      
      Mas quando você ler, esse campo pode ter outro valor.
    """
  ) String status,

  @ToolParam(
    required = true,
    description = """
      Tipo da nóticia. Informe apenas um dos seguintes: 
        \"UNKNOWN\",
        \"COMMON\",
        \"CONTRACT\".
    """
  ) String type
) {};
