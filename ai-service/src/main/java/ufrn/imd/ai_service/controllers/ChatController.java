package ufrn.imd.ai_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import ufrn.imd.ai_service.services.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {
  private ChatService service;

  @Autowired
  public ChatController(
    ChatService service
  ) {
    this.service = service;
  };

  @GetMapping
  @RateLimiter(name = "agent", fallbackMethod = "fallback")
  public ResponseEntity<String> ask(
    @RequestParam(name="prompt", required=true) String prompt
  ) {
    //String answer = this.service.ask(prompt);
    return ResponseEntity.ok(prompt);
  };

  public ResponseEntity<Void> fallback(RequestNotPermitted ex) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
  };
};
