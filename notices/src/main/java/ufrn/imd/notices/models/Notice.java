package ufrn.imd.notices.models;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.notices.models.enums.NoticeStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @Column(nullable = false, unique = true)
  private String filename;

  @Column(nullable = false)
  private Long bytes;

  @Column(nullable = false)
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Enumerated(EnumType.STRING)
  private NoticeStatus status;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "notice")
  private Set<Note> notes;
};
