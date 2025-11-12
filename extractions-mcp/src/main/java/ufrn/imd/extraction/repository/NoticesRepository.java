package ufrn.imd.extraction.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.extraction.models.Notice;

@Repository
public interface NoticesRepository extends JpaRepository<Notice, UUID> {
  public Optional<Notice> findByIdAndVersion(
    UUID id,
    Integer version
  );
};
