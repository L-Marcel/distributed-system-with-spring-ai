package ufrn.imd.commons.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CompanyNotFound extends ResponseStatusException {
  public CompanyNotFound() {
    super(HttpStatus.NOT_FOUND, "Empresa não encontrada.");
  };
};