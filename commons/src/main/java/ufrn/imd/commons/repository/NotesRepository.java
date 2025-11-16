package ufrn.imd.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.commons.models.Note;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {};
