package ufrn.imd.commons.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedRepresentativeDTO;
import ufrn.imd.commons.dto.extraction.ExtractedResponsibleDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = true)
  private String name;

  @Column(nullable = true, unique = true)
  private String phone;

  @Column(nullable = true, unique = true)
  private String email;

  @JsonBackReference
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "representative")
  private Set<Company> representatives;

  @JsonBackReference
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsible")
  private Set<Notice> responsibles;

  public void update(ExtractedResponsibleDTO responsible) {
    this.setName(responsible.name());
    this.setPhone(responsible.phone());
    this.setEmail(responsible.email());
  };

  public void update(ExtractedRepresentativeDTO representative) {
    this.setName(representative.name());
    this.setPhone(representative.phone());
    this.setEmail(representative.email());
  };
};
