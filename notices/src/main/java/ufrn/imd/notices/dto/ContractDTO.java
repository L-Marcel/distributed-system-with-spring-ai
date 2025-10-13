package ufrn.imd.notices.dto;

import java.sql.Date;
import java.util.List;

import ufrn.imd.notices.models.Contract;

public record ContractDTO(
  Double value,
  String currency,
  String startDate,
  String endDate,
  String location,
  CompanyDTO hirer,
  CompanyDTO hired,
  List<NoteDTO> notes
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
