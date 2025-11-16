package ufrn.imd.commons.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedQualificationDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Qualification {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = true)
  private Boolean requirement;

  @Column(nullable = true)
  private String description;

  @JsonBackReference
  @ManyToOne(optional = false)
  @JoinColumn(name = "vacancy", nullable = false)
  private Vacancy vacancy;

  public void update(ExtractedQualificationDTO qualification, Vacancy vacancy) {
    this.setVacancy(vacancy);
    this.setRequirement(qualification.requirement());
    this.setDescription(qualification.description());
  };
};
