package ufrn.imd.sources.models;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Source {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @Column(nullable = false, unique = true)
  private String filename;

  @Column(nullable = false)
  private Long bytes;

  @Column(nullable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private Timestamp updatedAt;
};
