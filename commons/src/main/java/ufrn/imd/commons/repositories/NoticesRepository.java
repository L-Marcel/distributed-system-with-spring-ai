package ufrn.imd.commons.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.commons.models.Notice;

@Repository
public interface NoticesRepository extends JpaRepository<Notice, UUID> {};
