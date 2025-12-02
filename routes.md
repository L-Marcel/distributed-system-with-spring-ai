# Base

config server  - http://localhost:8888/
eureka 1 - http://localhost:8761/
eureka 2 - http://localhost:8762
eureka 2 - http://localhost:8763/
gateway - http://localhost:8080/
notices - http://localhost:8080/notices/
extractions - http://localhost:8080/extractions/
extractions mcp - http://localhost:8080/extractions-mcp/
serverless - http://localhost:8080/serverless/
prometheus - http://localhost:5432/
grafana - http://localhost:8760/
zipkin - http://localhost:9411/

# Refresh

notices - POST http://localhost:8080/notices/actuator/refresh
```cmd
curl -X POST http://localhost:8080/notices/actuator/refresh
```