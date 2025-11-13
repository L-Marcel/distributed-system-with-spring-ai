package ufrn.imd.extractions.dto.extraction;

public record ExtractedCompanyDTO(
  String cnpj,
  String legalName,
  String tradeName,
  String phone,
  String email
) {};
