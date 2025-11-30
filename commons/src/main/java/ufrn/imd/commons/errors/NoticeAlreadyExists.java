package ufrn.imd.commons.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoticeAlreadyExists extends ResponseStatusException {
  public NoticeAlreadyExists() {
    super(HttpStatus.CONFLICT, "Edital já existe.");
  };
};