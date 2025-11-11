package ufrn.imd.notices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import ufrn.imd.notices.configurations.AppConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(AppConfiguration.class)
@EnableFeignClients
public class NoticesApplication {
	public static void main(String[] args) {
		SpringApplication.run(NoticesApplication.class, args);
	};
};
