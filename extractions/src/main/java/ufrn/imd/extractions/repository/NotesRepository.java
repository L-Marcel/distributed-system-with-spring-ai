package ufrn.imd.extractions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.extractions.models.Note;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {};
