package ufrn.imd.notices.dto.extraction;

import java.sql.Date;

import ufrn.imd.notices.models.Contract;

public record ExtractedContractDTO(
  Double value,
  String currency,
  String startDate,
  String endDate,
  String location,
  ExtractedCompanyDTO hirer,
  ExtractedCompanyDTO hired
) {
  public Contract toEntity() {
    Contract contract = new Contract();
    contract.setValue(this.value());
    contract.setCurrency(this.currency());
    contract.setStartDate(Date.valueOf(this.startDate()));
    contract.setEndDate(Date.valueOf(this.endDate()));
    contract.setLocation(this.location());
    contract.setHirer(this.hirer().toEntity());
    contract.setHired(this.hired().toEntity());
    return contract;
  };
};
