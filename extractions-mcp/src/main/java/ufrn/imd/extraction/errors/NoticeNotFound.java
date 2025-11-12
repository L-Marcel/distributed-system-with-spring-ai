package ufrn.imd.extraction.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoticeNotFound extends ResponseStatusException {
  public NoticeNotFound() {
    super(HttpStatus.NOT_FOUND, "Edital não encontrado.");
  };
};