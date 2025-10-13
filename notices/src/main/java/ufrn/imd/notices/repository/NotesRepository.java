package ufrn.imd.notices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.notices.models.Note;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {};
