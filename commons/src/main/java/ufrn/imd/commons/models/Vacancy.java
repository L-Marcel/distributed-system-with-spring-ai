package ufrn.imd.commons.models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedVacancyDTO;
import ufrn.imd.commons.models.enums.WorkloadFrequency;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vacancy {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = true)
  private String title;

  @Column(nullable = true)
  private String description;

  @Column(nullable = true)
  private Integer amount;

  @Column(nullable = true)
  private Double salary;

  @Column(nullable = true)
  private String currency;

  @Column(nullable = true)
  private Integer workload;

  @Enumerated(EnumType.STRING)
  private WorkloadFrequency frequency = WorkloadFrequency.HOURS_PER_DAY;

  @Column(name = "start_date", nullable = true)
  private Date startDate;

  @Column(name = "end_date", nullable = true)
  private Date endDate;

  @JsonBackReference
  @ManyToOne(optional = false)
  @JoinColumn(name = "notice", nullable = false)
  private Notice notice;
  
  @JsonManagedReference
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "vacancy", orphanRemoval = true)
  private Set<Qualification> qualifications;

  public void update(ExtractedVacancyDTO vacancy) {
    this.setTitle(vacancy.title());
    this.setDescription(vacancy.description());
    this.setAmount(vacancy.amount());
    this.setSalary(vacancy.salary());
    this.setCurrency(vacancy.currency());
    this.setWorkload(vacancy.workload());
    this.setFrequency(vacancy.frequency());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    if(vacancy.startDate() != null) this.setStartDate(Date.valueOf(
      LocalDate.parse(vacancy.startDate(), formatter)
    ));
    else this.setStartDate(null);

    if(vacancy.endDate() != null) this.setEndDate(Date.valueOf(
      LocalDate.parse(vacancy.endDate(), formatter)
    ));
    else this.setEndDate(null);

    if(vacancy.qualifications() != null) this.getQualifications().addAll(
      vacancy.qualifications().stream().map((qualification) -> {
        Qualification _qualification = new Qualification();
        _qualification.update(qualification, this);
        return _qualification;
      }).toList());
  };
};
