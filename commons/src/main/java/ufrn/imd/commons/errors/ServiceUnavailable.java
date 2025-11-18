package ufrn.imd.commons.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceUnavailable extends ResponseStatusException {
  public ServiceUnavailable() {
    super(HttpStatus.SERVICE_UNAVAILABLE, "Serviço momentaneamente indisponível.");
  };
};