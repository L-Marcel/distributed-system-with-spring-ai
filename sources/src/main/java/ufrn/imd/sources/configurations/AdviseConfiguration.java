package ufrn.imd.sources.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@RestControllerAdvice
public class AdviseConfiguration {
  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail onException(ResponseStatusException exception) {
    return ProblemDetail.forStatusAndDetail(
      exception.getStatusCode(),
      exception.getMessage()
    );
  };

  @ExceptionHandler(Exception.class)
  public ProblemDetail onException(Exception exception) {
    return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
  };
};
