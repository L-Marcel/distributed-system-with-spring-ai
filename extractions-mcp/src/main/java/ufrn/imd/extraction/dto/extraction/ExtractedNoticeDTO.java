package ufrn.imd.extraction.dto.extraction;

import java.util.Set;
import java.util.UUID;

import org.springframework.ai.tool.annotation.ToolParam;

import ufrn.imd.extraction.models.enums.NoticeStatus;
import ufrn.imd.extraction.models.enums.NoticeType;

public record ExtractedNoticeDTO(
  @ToolParam(
    required = true,
    description = "UUID da nóticia."
  ) UUID id,

  @ToolParam(
    required = true,
    description = """
      O novo status da nóticia. Informe apenas um dos seguintes: 
        \"PROCESSING\", 
        \"UNKNOWN_TYPE\", 
        \"WAITING_MORE_DATA,\" 
        \"UNPUBLISHED\" ou 
        \"MISSING_EMAIL\".
      
      Exceto que o que já estivesse antes da extração seja diferente.
    """
  ) NoticeStatus status,

  @ToolParam(
    required = true,
    description = """
      Tipo da nóticia. Informe apenas um dos seguintes: 
        \"UNKNOWN\",
        \"COMMON\",
        \"CONTRACT\".
    """
  ) NoticeType type,

  @ToolParam(
    required = false,
    description = "Anotações que você pode usar para deixar observações."
  ) Set<ExtractedNoteDTO> notes
) {};