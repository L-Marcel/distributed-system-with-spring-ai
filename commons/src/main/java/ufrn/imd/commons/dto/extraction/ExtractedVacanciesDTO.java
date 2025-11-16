package ufrn.imd.commons.dto.extraction;

import java.util.Set;
import java.util.UUID;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedVacanciesDTO(
  @ToolParam(
    required = true,
    description = "UUID da nóticia."
  ) UUID notice,

  @ToolParam(
    required = true,
    description = """
      Verdadeiro se não for para sobrescrever as vagas já salvas,
      falso caso contrário.
    """
  ) Boolean append,
  
  @ToolParam(
    required = false,
    description = "vagas."
  ) Set<ExtractedVacancyDTO> vacancies
) {};
