package ufrn.imd.extractions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"ufrn.imd.extractions",
	"ufrn.imd.commons"
}) @EnableJpaRepositories(basePackages = {
	"ufrn.imd.commons.repositories"
}) @EntityScan(basePackages = {
  "ufrn.imd.commons.models"
}) public class ExtractionsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExtractionsApplication.class, args);
	};
};
