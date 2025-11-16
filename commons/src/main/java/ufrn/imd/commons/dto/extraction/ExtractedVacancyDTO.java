package ufrn.imd.commons.dto.extraction;

import java.util.Set;
import java.util.UUID;

import org.springframework.ai.tool.annotation.ToolParam;

import ufrn.imd.commons.models.enums.WorkloadFrequency;

public record ExtractedVacancyDTO(
  @ToolParam(
    required = true,
    description = "UUID da nóticia."
  ) UUID notice,

  @ToolParam(
    required = false,
    description = "Título da vaga."
  ) String title,

  @ToolParam(
    required = false,
    description = "Descrição da vaga."
  ) String description,

  @ToolParam(
    required = false,
    description = "Número de pessoas que podem preencher a vaga."
  ) Integer amount,

  @ToolParam(
    required = false,
    description = "Salário oferecido pela vaga."
  ) Double salary,

  @ToolParam(
    required = false,
    description = "Moeada do salário oferecido pela vaga. Ex: BRL."
  ) String currency,

  @ToolParam(
    required = false,
    description = "Carga horária da vaga."
  ) Integer workload,

  @ToolParam(
    required = true,
    description = """
      Frequência da carga horária: 
        \"HOURS_PER_DAY\",
        \"HOURS_PER_WEEK\",
        \"HOURS_PER_MONTH\".
    """
  ) WorkloadFrequency frequency,

  @ToolParam(
    required = false,
    description = "Data de início da vaga, no formato dd/MM/yyyy."
  ) String startDate,

  @ToolParam(
    required = false,
    description = "Data de termino da vaga, no formato dd/MM/yyyy."
  ) String endDate,
  
  @ToolParam(
    required = false,
    description = "Qualificaçẽos da vaga."
  ) Set<ExtractedQualificationDTO> qualifications
) {};
