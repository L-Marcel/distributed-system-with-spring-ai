package ufrn.imd.extraction;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"ufrn.imd.extraction",
	"ufrn.imd.commons"
}) @EnableJpaRepositories(basePackages = {
	"ufrn.imd.commons.repositories"
}) @EntityScan(basePackages = {
  "ufrn.imd.commons.models"
}) public class ExtractionsMcpApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExtractionsMcpApplication.class, args);
	};

	@Bean
	public ToolCallbackProvider toolCallbackProvider(
		Tools tools
	) {
		return MethodToolCallbackProvider
			.builder()
			.toolObjects(tools)
			.build();
	};
};
