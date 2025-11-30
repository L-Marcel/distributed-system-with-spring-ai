package ufrn.imd.notices.events;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import ufrn.imd.commons.models.Notice;
import ufrn.imd.commons.models.enums.NoticeStatus;
import ufrn.imd.commons.repositories.NoticesRepository;
import ufrn.imd.notices.services.ExtractionsService;
import ufrn.imd.notices.services.NoticesServiceImpl;

@Component
public class NoticeListener {
  private static final Logger log = LoggerFactory.getLogger(
    NoticeListener.class
  );
  
  private ExtractionsService extractions;
  private NoticesRepository notices;

  @Autowired
  public NoticeListener(
    ExtractionsService extractions,
    NoticesRepository notices
  ) {
    this.extractions = extractions;
    this.notices = notices;
  };

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNoticeCreated(NoticeChangedEvent event) {
    Optional<Notice> notice = this.notices.findById(event.id());

    if(notice.isEmpty()) {
      log.warn("Notice with id {} not found on changed event", event.id());
      return;
    };

    try {
      NoticeStatus status = this.extractions
        .request(event.id())
        .getBody();
      
      notice.get().setStatus(status);

      this.notices.save(notice.get());
    } catch (Exception e) {
      log.error(
        "Error on changed event of notice with id {}.\n\n{}", 
        event.id(), 
        e.getMessage()
      );
    }
  };
};