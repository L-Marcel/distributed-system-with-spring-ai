package ufrn.imd.notices.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoitceNotFound extends ResponseStatusException {
  public NoitceNotFound() {
    super(HttpStatus.NOT_FOUND, "Notice not found!");
  };
};