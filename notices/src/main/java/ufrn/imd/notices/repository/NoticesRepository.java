package ufrn.imd.notices.repository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ufrn.imd.notices.models.Notice;

public interface NoticesRepository extends JpaRepository<Notice, UUID> {
  public Optional<Notice> findByIdAndFilenameAndCreatedAtAndUpdatedAtAndExtractionFinished(
    UUID uuid,
    String filename,
    Timestamp createdAt,
    Timestamp updatedAt,
    Boolean extractionFinished
  );
};
