package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedQualificationDTO(
  @ToolParam(
    required = false,
    description = """
      Verdadeiro se for uma qualificação exigida, falso caso 
      seja apenas uma qualificação desejável.
    """
  ) Boolean requirement,

  @ToolParam(
    required = false,
    description = "Descrição da vaga."
  ) String description
) {};
