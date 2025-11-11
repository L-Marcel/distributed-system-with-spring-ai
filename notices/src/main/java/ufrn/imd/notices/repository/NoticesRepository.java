package ufrn.imd.notices.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.notices.models.Notice;

@Repository
public interface NoticesRepository extends JpaRepository<Notice, UUID> {};
