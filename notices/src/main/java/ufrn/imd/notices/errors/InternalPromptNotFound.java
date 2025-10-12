package ufrn.imd.notices.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalPromptNotFound extends ResponseStatusException {
  public InternalPromptNotFound() {
    super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal prompt not found!");
  };
};