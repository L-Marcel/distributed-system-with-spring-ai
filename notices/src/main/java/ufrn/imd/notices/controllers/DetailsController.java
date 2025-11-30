package ufrn.imd.notices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.notices.configurations.AppConfiguration;
import ufrn.imd.notices.services.ServerlessService;

@RestController
@RequestMapping("/details")
public class DetailsController {
  private AppConfiguration configuration;
  private ServerlessService serverless;

  @Autowired
  public DetailsController(
    AppConfiguration configuration,
    ServerlessService serverless
  ) {
    this.configuration = configuration;
    this.serverless = serverless;
  };

  @GetMapping("/version")
  public ResponseEntity<String> getVersion() {
    return ResponseEntity.ok(this.configuration.getVersion());
  };

  @GetMapping("/info")
  public ResponseEntity<String> getInfo() {
    String info = this.serverless.info().getBody();
    return ResponseEntity.ok(info);
  };
};
