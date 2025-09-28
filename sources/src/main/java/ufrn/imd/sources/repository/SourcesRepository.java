package ufrn.imd.sources.repository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ufrn.imd.sources.models.Source;

public interface SourcesRepository extends JpaRepository<Source, UUID> {
  public Optional<Source> findByIdAndFilenameAndCreatedAtAndUpdatedAt(
    UUID uuid,
    String filename,
    Timestamp createdAt,
    Timestamp updatedAt
  );
};
