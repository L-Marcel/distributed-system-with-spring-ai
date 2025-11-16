package ufrn.imd.commons.dto.extraction;

import java.util.Set;
import java.util.UUID;

import org.springframework.ai.tool.annotation.ToolParam;

import ufrn.imd.commons.models.enums.NoticeStatus;
import ufrn.imd.commons.models.enums.NoticeType;

public record ExtractedNoticeDTO(
  @ToolParam(
    required = true,
    description = "UUID da nóticia."
  ) UUID id,

  @ToolParam(
    required = true,
    description = """
      O novo status da nóticia. Informe apenas um dos seguintes: 
        \"STOPPED\",
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
    description = "Responsável pelo edital, aplicável APENAS quando o edital é do tipo COMMON"
  ) ExtractedResponsibleDTO responsible,
  
  @ToolParam(
    required = false,
    description = "Anotações que você pode usar para deixar observações."
  ) Set<ExtractedNoteDTO> notes
) {};