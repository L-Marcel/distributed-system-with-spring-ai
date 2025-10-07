package ufrn.imd.notices.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SourceNotFound extends ResponseStatusException {
  public SourceNotFound() {
    super(HttpStatus.NOT_FOUND, "Source not found!");
  };
};