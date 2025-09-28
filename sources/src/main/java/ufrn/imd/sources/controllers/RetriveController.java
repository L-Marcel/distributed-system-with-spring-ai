package ufrn.imd.sources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.sources.services.RetriveService;
import ufrn.imd.sources.services.SourcesService;

@RestController
@RequestMapping("/retrieve")
public class RetriveController {
  private RetriveService retrieve;
  
  @Autowired
  public RetriveController(
    RetriveService retrieve,
    SourcesService sources
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
