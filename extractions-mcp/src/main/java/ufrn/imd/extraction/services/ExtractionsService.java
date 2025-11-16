package ufrn.imd.extraction.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import ufrn.imd.commons.dto.extraction.ExtractedAddressDTO;
import ufrn.imd.commons.dto.extraction.ExtractedContractDTO;
import ufrn.imd.commons.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.commons.dto.extraction.ExtractedRepresentativeDTO;
import ufrn.imd.commons.dto.extraction.ExtractedVacanciesDTO;
import ufrn.imd.commons.errors.NoticeNotFound;
import ufrn.imd.commons.models.Address;
import ufrn.imd.commons.models.Company;
import ufrn.imd.commons.models.Contract;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.models.Person;
import ufrn.imd.commons.repository.CompaniesRepository;
import ufrn.imd.commons.repository.NoticesRepository;

@Service
public class ExtractionsService {
  private NoticesRepository notices;
  private CompaniesRepository companies;

  @Autowired
  public ExtractionsService(
    NoticesRepository notices,
    CompaniesRepository companies
  ) {
    this.notices = notices;
    this.companies = companies;
  };

  public Notice findNoticeById(UUID id) {
    return this.notices.findById(id)
      .orElseThrow(NoticeNotFound::new);
  };

  public Company findCompanyByCNPJ(String cnpj) {
    return this.companies.findById(cnpj)
      .orElseThrow(NoticeNotFound::new);
  };

  @Transactional 
  public Notice updateFrom(ExtractedNoticeDTO data) {
    Notice notice = this.findNoticeById(
      data.id()
    );
    
    notice.update(data);

    this.notices.saveAndFlush(notice);

    return notice;
  };

  @Transactional 
  public Notice updateFrom(ExtractedVacanciesDTO data) {
    Notice notice = this.findNoticeById(
      data.notice()
    );

    notice.update(data);
    this.notices.saveAndFlush(notice);
    
    return notice;
  };
  
  @Transactional 
  public Notice updateFrom(ExtractedContractDTO data) {
    Notice notice = this.findNoticeById(
      data.notice()
    );

    Contract contract = notice.getContract();
    if(contract == null) contract = new Contract();
    contract.update(data);

    notice.setContract(contract);
    this.notices.saveAndFlush(notice);

    return notice;
  };

  @Transactional 
  public Company updateFrom(ExtractedRepresentativeDTO data) {
    Company company = this.findCompanyByCNPJ(
      data.cnpj()
    );
    
    Person representative = company.getRepresentative();
    if(representative == null) representative = new Person();
    representative.update(data);

    company.setRepresentative(representative);
    this.companies.saveAndFlush(company);

    return company;
  };

  @Transactional 
  public Company updateFrom(ExtractedAddressDTO data) {
    Company company = this.findCompanyByCNPJ(
      data.cnpj()
    );
    
    Address address = company.getAddress();
    if(address == null) address = new Address();
    address.update(data);

    company.setAddress(address);
    this.companies.saveAndFlush(company);

    return company;
  };
};
