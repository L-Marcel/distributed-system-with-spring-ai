package ufrn.imd.commons.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedNoteDTO(
  @ToolParam(
    required = true,
    description = "Conteúdo da anotação. Máximo de 200 caracteres."
  ) String content
) {};