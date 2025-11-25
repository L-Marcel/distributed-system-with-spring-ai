package ufrn.imd.serverless.functions;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oshi.SystemInfo;

@Configuration
public class SystemFunctions {
  @Bean
  public Supplier<String> info() {
    SystemInfo system = new SystemInfo();

    return () -> {
      StringBuilder builder = new StringBuilder();
      builder.append("Processor: ");
      builder.append(system.getHardware().getProcessor().toString());
      builder.append("\n\nMemory: ");
      builder.append(system.getHardware().getMemory().getPhysicalMemory().getFirst().toString());
      return builder.toString();
    };
  };
};
