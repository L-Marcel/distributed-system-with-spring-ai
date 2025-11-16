package ufrn.imd.notices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ufrn.imd.notices.configurations.AppConfiguration;

@SpringBootApplication(scanBasePackages = {
	"ufrn.imd.notices",
	"ufrn.imd.commons"
}) @EnableJpaRepositories(basePackages = {
	"ufrn.imd.commons.repositories"
}) @EntityScan(basePackages = {
  "ufrn.imd.commons.models"
}) @EnableConfigurationProperties(AppConfiguration.class)
@EnableFeignClients
public class NoticesApplication {
	public static void main(String[] args) {
		SpringApplication.run(NoticesApplication.class, args);
	};
};
