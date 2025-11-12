package ufrn.imd.extraction.dto.extraction;

import org.springframework.ai.tool.annotation.ToolParam;

public record ExtractedNoteDTO(
  @ToolParam(
    required = true,
    description = "Conteúdo da anotação."
  ) String content
) {};