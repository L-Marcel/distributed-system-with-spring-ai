package ufrn.imd.notices.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Note {
  @ManyToOne(optional=false)
  @JoinColumn(name="notice", nullable=false, updatable=false)
  private Notice notice;
};
