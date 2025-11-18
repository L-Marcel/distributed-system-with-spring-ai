package ufrn.imd.commons.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TooManyRequests extends ResponseStatusException {
  public TooManyRequests() {
    super(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições por segundo alcançado.");
  };
};