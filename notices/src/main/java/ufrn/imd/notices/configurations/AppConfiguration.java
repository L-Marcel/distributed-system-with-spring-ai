package ufrn.imd.notices.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.configuration")
public class AppConfiguration {
  private String version = "unknown";
};
