package ufrn.imd.notices.configurations;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@Profile("dev")
public class DatabaseInitializer {
  private ResourcePatternResolver resolver;
  private DataSource source;

  @Value("${spring.datasource.data:#{null}}")
  private String data;

  @Autowired
  public DatabaseInitializer(
    ResourcePatternResolver resolver,
    DataSource source
  ) {
    this.resolver = resolver;
    this.source = source;
  };

  @EventListener(ContextRefreshedEvent.class)
  private void populate() {
    if (this.data != null && !this.data.isEmpty()) {
      Resource resource = this.resolver.getResource(this.data);
      DatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
      try {
        DatabasePopulatorUtils.execute(databasePopulator, this.source);
      } catch (Exception e) {};
    };
  };
};
