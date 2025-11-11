package ufrn.imd.notices.models;

import java.sql.Date;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contract {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Double value;
  
  @Column(nullable = false)
  private String currency;

  @Column(name = "start_date", nullable = false)
  private Date startDate;

  @Column(name = "end_date", nullable = true)
  private Date endDate;

  @Column(nullable = true)
  private String location;

  @OneToOne(optional = false)
  @JoinColumn(name = "notice", nullable = false)
  private Notice notice;
  
  @ManyToOne(optional = false)
  @JoinColumn(name = "hired", nullable = false)
  private Company hired;

  @ManyToOne(optional = false)
  @JoinColumn(name = "hirer", nullable = false)
  private Company hirer;
};
