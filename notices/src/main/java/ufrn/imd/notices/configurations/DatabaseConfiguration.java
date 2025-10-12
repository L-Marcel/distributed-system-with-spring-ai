package ufrn.imd.notices.configurations;

import javax.sql.DataSource;

import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore.MetadataField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfiguration {
  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.driver-class-name}")
  private String driver;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.schema}")
  private String schema;
  
  @Bean
  @Primary
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(this.url);
    config.setDriverClassName(this.driver);
    config.setUsername(this.username);
    config.setPassword(this.password);
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setSchema(this.schema);
    return new HikariDataSource(config);
  };
};
