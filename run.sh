
mvn spring-boot:run -pl config-server
mvn spring-boot:run -pl eureka -Dspring-boot.run.arguments=--spring.profiles.active=dev,server1
mvn spring-boot:run -pl eureka -Dspring-boot.run.arguments=--spring.profiles.active=dev,server2
mvn spring-boot:run -pl eureka -Dspring-boot.run.arguments=--spring.profiles.active=dev,server3
mvn spring-boot:run - pl extraction-mcp
mvn spring-boot:run -pl gateway
mvn spring-boot:run -pl notices