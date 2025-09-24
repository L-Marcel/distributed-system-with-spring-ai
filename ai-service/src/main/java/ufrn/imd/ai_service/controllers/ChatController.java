package ufrn.imd.ai_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<String> ask(
    @RequestParam(name="prompt", required=true) String prompt
  ) {
    //String answer = this.service.ask(prompt);
    return ResponseEntity.ok(prompt);
  };

  // public ResponseEntity<Void> fallback(RequestNotPermitted ex) {
  //   return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
  // };
};
