package ufrn.imd.commons.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedCompanyDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
  @Id
  private String cnpj;

  @Column(nullable = true)
  private String legalName;

  @Column(nullable = true)
  private String tradeName;

  @Column(nullable = true, unique = true)
  private String phone;

  @Column(nullable = true, unique = true)
  private String email;
  
  @JsonBackReference
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hirer")
  private Set<Contract> contractsAsHirer;
  
  @JsonBackReference
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hired")
  private Set<Contract> contractsAsHired;

  @JsonManagedReference
  @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "representative", nullable = true)
  private Person representative;

  @JsonManagedReference
  @OneToOne(optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "address", nullable = true)
  private Address address;

  public void update(ExtractedCompanyDTO company) {
    if(company.cnpj() != null) this.setCnpj(company.cnpj());
    
    this.setLegalName(company.legalName());
    this.setTradeName(company.tradeName());
    this.setPhone(company.phone());
    this.setEmail(company.email());
  };
};
