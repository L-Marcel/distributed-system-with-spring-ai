package ufrn.imd.extractions.models;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "hirer")
  private Set<Contract> contractsAsHirer;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "hired")
  private Set<Contract> contractsAsHired;
};
