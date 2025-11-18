package ufrn.imd.commons.models;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ufrn.imd.commons.dto.extraction.ExtractedNoticeDTO;
import ufrn.imd.commons.dto.extraction.ExtractedVacanciesDTO;
import ufrn.imd.commons.models.enums.NoticeStatus;
import ufrn.imd.commons.models.enums.NoticeType;

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

  @JsonManagedReference
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "notice", orphanRemoval = true)
  private Set<Note> notes;
  
  @JsonManagedReference
  @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "notice", orphanRemoval = true)
  @JoinColumn(name = "contract", nullable = true)
  private Contract contract;

  @JsonManagedReference
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "notice", orphanRemoval = true)
  private Set<Vacancy> vancacies;

  @JsonManagedReference
  @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "responsible", nullable = true)
  private Person responsible;

  public void update(ExtractedNoticeDTO notice) {
    this.setStatus(notice.status());
    this.setType(notice.type());

    if(notice.responsible() != null) {
      Person _responsible = this.getResponsible();
      if(_responsible == null) _responsible = new Person();
      _responsible.update(notice.responsible());
      this.setResponsible(_responsible);
    } else this.setResponsible(null);

    if(notice.notes() != null) {
      Set<Note> notes = notice.notes().stream().map((note) -> {
        Note _note = new Note();
        _note.setContent(note.content());
        _note.setNotice(this);
        return _note;
      }).collect(Collectors.toSet());

      if(this.getNotes() != null) this.getNotes().addAll(notes);
      else this.setNotes(notes);
    };
  };

  public void update(ExtractedVacanciesDTO vacancies) {
    if(vacancies.vacancies() != null) {
      Set<Vacancy> _vacancies = vacancies.vacancies().stream().map((vacancy) -> {
        Vacancy _vacancy = new Vacancy();
        _vacancy.update(vacancy);
        return _vacancy; 
      }).collect(Collectors.toSet());
      
      if(vacancies.append() && this.getVancacies() != null) {
        this.getVancacies().addAll(_vacancies);
      } else this.setVancacies(_vacancies);
    } else if(!vacancies.append()) this.setVancacies(null);
  };
};
