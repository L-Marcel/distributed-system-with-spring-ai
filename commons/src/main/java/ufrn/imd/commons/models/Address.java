package ufrn.imd.commons.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedAddressDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  @Column(nullable = true)
  private String street;

  @Column(nullable = true)
  private Integer number;

  @Column(nullable = true)
  private String district;

  @Column(nullable = true)
  private String complement;

  @Column(nullable = true)
  private String city;

  @Column(nullable = true)
  private String uf;

  @Column(nullable = true)
  private String cep;
  
  @JsonBackReference
  @OneToOne(optional = false, mappedBy = "address")
  private Company company;

  public void update(ExtractedAddressDTO address, Company company) {
    this.setCompany(company);
    this.setStreet(address.street());
    this.setNumber(address.number());
    this.setDistrict(address.district());
    this.setComplement(address.complement());
    this.setCity(address.city());
    this.setUf(address.uf());
    this.setCep(address.cep());
  };
};
