package ufrn.imd.notices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ufrn.imd.notices.configurations.AppConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(AppConfiguration.class)
public class NoticesAgentApplication {
	public static void main(String[] args) {
		SpringApplication.run(NoticesAgentApplication.class, args);
	};
};
