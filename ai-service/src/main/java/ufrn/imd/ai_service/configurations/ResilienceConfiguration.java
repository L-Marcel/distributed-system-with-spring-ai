package ufrn.imd.ai_service.configurations;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.common.ratelimiter.configuration.RateLimiterConfigCustomizer;

@Configuration
public class ResilienceConfiguration {
  @Bean
  public RateLimiterConfigCustomizer rateLimiterConfigCustomizer() {
    return RateLimiterConfigCustomizer
      .of(
        "agent",
        (builder) -> builder
          .limitForPeriod(1)
          .limitRefreshPeriod(Duration.ofMinutes(1))
          .build()
      );
  };
};
