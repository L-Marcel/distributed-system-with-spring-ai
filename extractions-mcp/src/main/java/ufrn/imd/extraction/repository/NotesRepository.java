package ufrn.imd.extraction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.extraction.models.Note;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {};
