package ufrn.imd.extractions.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.commons.errors.NoticeNotFound;
import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.repository.NoticesRepository;

@Service
public class NoticesService {
  private NoticesRepository notices;

  @Autowired
  public NoticesService(
    NoticesRepository notices
  ) {
    this.notices = notices;
  };

  public Notice findById(
    UUID id
  ) {
    return this.notices.findById(id)
      .orElseThrow(NoticeNotFound::new);
  };
};
