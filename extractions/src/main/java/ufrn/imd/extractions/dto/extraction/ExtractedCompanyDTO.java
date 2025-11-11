package ufrn.imd.extractions.dto.extraction;

import ufrn.imd.extractions.models.Company;

public record ExtractedCompanyDTO(
  String cnpj,
  String legalName,
  String tradeName,
  String phone,
  String email
) {
  public Company toEntity() {
    Company company = new Company();
    company.setCnpj(this.cnpj());
    company.setLegalName(this.legalName());
    company.setTradeName(this.tradeName());
    company.setPhone(this.phone());
    company.setEmail(this.email());
    return company;
  };
};
