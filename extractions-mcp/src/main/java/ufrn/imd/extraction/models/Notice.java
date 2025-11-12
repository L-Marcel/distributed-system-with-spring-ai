package ufrn.imd.extraction.models;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.extraction.models.enums.NoticeStatus;
import ufrn.imd.extraction.models.enums.NoticeType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notice {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Version
  private Integer version;

  @Column(nullable = false, unique = true)
  private String filename;

  @Column(nullable = false)
  private Long bytes;

  @Column(nullable = false)
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Enumerated(EnumType.STRING)
  private NoticeStatus status = NoticeStatus.PROCESSING;

  @Enumerated(EnumType.STRING)
  private NoticeType type = NoticeType.UNKNOWN;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "notice")
  private Set<Note> notes;
  
  @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "notice")
  private Contract contract;
};
