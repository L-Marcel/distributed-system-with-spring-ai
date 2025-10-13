package ufrn.imd.notices.dto;

import ufrn.imd.notices.models.Company;

public record CompanyDTO(
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
