package ufrn.imd.extractions.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.extractions.errors.NoitceNotFound;
import ufrn.imd.extractions.models.Notice;
import ufrn.imd.extractions.repository.NoticesRepository;

@Service
public class NoticesService {
  private NoticesRepository notices;

  @Autowired
  public NoticesService(
    NoticesRepository notices
  ) {
    this.notices = notices;
  };

  public Notice findByIdAndVersion(
    UUID id,
    Integer version
  ) {
    return this.notices.findByIdAndVersion(id, version)
      .orElseThrow(NoitceNotFound::new);
  };
};
