package ufrn.imd.commons.models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedContractDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contract {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = true)
  private Double value;
  
  @Column(nullable = true)
  private String currency;

  @Column(name = "start_date", nullable = true)
  private Date startDate;

  @Column(name = "end_date", nullable = true)
  private Date endDate;
  
  @Column(nullable = true)
  private String location;

  @JsonBackReference
  @OneToOne(optional = false)
  private Notice notice;
  
  @JsonManagedReference
  @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "hired", nullable = true)
  private Company hired;

  @JsonManagedReference
  @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "hirer", nullable = true)
  private Company hirer;

  public void update(ExtractedContractDTO contract, Notice notice) {
    this.setNotice(notice);
    this.setValue(contract.value());
    this.setCurrency(contract.currency());
    this.setLocation(contract.location());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    if(contract.startDate() != null) this.setStartDate(Date.valueOf(
      LocalDate.parse(contract.startDate(), formatter)
    ));
    else this.setStartDate(null);

    if(contract.endDate() != null) this.setEndDate(Date.valueOf(
      LocalDate.parse(contract.endDate(), formatter)
    ));
    else this.setEndDate(null);

    if(contract.hired() != null) {
      Company _hired = this.getHired();
      if(_hired == null) _hired = new Company();
      _hired.update(contract.hired());
      this.setHired(_hired);
    } else {
      this.setHired(null);
    };

    if(contract.hirer() != null) {
      Company _hirer = this.getHirer();
      if(_hirer == null) _hirer = new Company();
      _hirer.update(contract.hirer());
      this.setHirer(_hirer);
    } else {
      this.setHirer(null);
    };
  };
};
