package ufrn.imd.notices.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoticeVersionNotFound extends ResponseStatusException {
  public NoticeVersionNotFound() {
    super(
      HttpStatus.NOT_FOUND, 
      "Notice's especific version not found! May have been updated."
    );
  };
};