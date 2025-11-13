package ufrn.imd.extractions.dto.extraction;
public record ExtractedContractDTO(
  Double value,
  String currency,
  String startDate,
  String endDate,
  String location,
  ExtractedCompanyDTO hirer,
  ExtractedCompanyDTO hired
) {};
