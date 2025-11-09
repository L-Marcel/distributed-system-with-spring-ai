package ufrn.imd.notices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.notices.configurations.AppConfiguration;

@RestController
@RequestMapping("/configuration")
public class ConfigurationsController {
  private AppConfiguration configuration;

  @Autowired
  public ConfigurationsController(
    AppConfiguration configuration
  ) {
    this.configuration = configuration;
  };

  @GetMapping("/version")
  public ResponseEntity<String> getVersion() {
    return ResponseEntity.ok(this.configuration.getVersion());
  };
};
