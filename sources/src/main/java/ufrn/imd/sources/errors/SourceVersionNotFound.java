package ufrn.imd.sources.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SourceVersionNotFound extends ResponseStatusException {
  public SourceVersionNotFound() {
    super(HttpStatus.NOT_FOUND, "Source's especific version not found! May have been updated.");
  };
};