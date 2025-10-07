package ufrn.imd.notices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.notices.services.NoticesService;
import ufrn.imd.notices.services.RetrieveService;

@RestController
@RequestMapping("/retrieve")
public class RetrieveController {
  private RetrieveService retrieve;
  
  @Autowired
  public RetrieveController(
    RetrieveService retrieve,
    NoticesService sources
  ) {
    this.retrieve = retrieve;
  };

  @GetMapping
  public ResponseEntity<String> ask(
    @RequestParam(name="prompt", required=true) String prompt
  ) {
    //String answer = this.service.ask(prompt);
    return ResponseEntity.ok(prompt);
  };
};
