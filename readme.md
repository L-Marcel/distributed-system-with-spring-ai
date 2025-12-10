# Distributed System With AI

Este projeto foi elaborado durante a disciplina de `Programação Distribuída` na `UFRN`, ministrada pelo professor `Nélio Cacho`. Se trata de um sistema distribuído de extração de dados de editais que agrega recursos de observabilidade, resiliência, agentes inteligêntes, etc.

- [Observações](#observações)
- [Estrutura](#estrutura)
- [Instruções para execução](#instruções-para-execução)
  - [Eureka](#eureka)
  - [Config server](#config-server)
    - [Limitações relacionadas aos modelos](#limitações-relacionadas-aos-modelos)
  - [Docker](#docker)
    - [Sobre a escolha do banco de dados](#sobre-a-escolha-de-banco-de-dados)
  - [Execução otimizada](#execução-otimizada)
    - [Comandos de execução](#comandos-de-execução)
    - [Por que o serverless requer permissões elevadas no linux?](#por-que-o-serverless-requer-permissões-elevadas-no-linux)
    - [Ordem de execução](#ordem-de-execução)
- [Rotas](#rotas)
  - [Mocks e teste básico](#mocks-e-teste-básico)
  - [Refresh das configurações](#refresh-das-configurações)
- [Testes com o JMeter](#testes-com-o-jmeter)

# Observações

O foco deste projeto não é ter um produto final imediatamente aplicável, mas aprender e aplicar conceitos importantes de sistemas distribuídos. Você pode utilizá-lo como bem entender, vou deixar uma licensa `MIT`.

# Estrutura

São ao todo 7 microsserviços:
- Configuration ([/config-server](/config-server/)):
  - Fornece para os outros microsserviços os `arquivos de configuração` centralizados em um repositório.
- Eureka ([/eureka](/eureka/)):
  - Responsável pela `descoberta de serviços`.
- Gateway ([/gateway](/gateway/));
- Notices ([/notices](/notices/)):
  - Microsserviço acessível pelo Gateway que permite criar e atualizar editais existentes;
  - Vetoriza os documentos enviados.
- Extractions ([/extractions](/extractions/)):
  - Microsserviço interno que realiza de forma assíncrona (não bloqueante) a extração dos dados dos editais;
  - Faz uso de um `agente de IA` para extrair dados dos editais, usando as ferramentas expostas pelo `Extractions MCP` e recursos de `Retrieval-augmented generation (RAG)`;
  - Também envia um e-mail quando termina de extrair os dados usando o `Mailtrap MCP`:
    - Como não deixei uma variável para definir o destinatário do e-mail, você pode configurar alterando no arquivo [/extractions/src/main/resources/prompts/system_notice.xml](/extractions/src/main/resources/prompts/system_notice.xml).
- Extractions MCP ([/extractions-mcp](/extractions-mcp/)):
  - Fornece as ferramentas para que um agente de IA possa extrair os dados de um edital e persistir eles no banco de dados.
- Serverless ([/serverless](/serverless/)):
  - Um microsserviço utilizado para mostrar o funcionamento básico de um microserviço serverless, com apenas uma função registrada e que retorna informações sobre o hardware do computador.

# Instruções para execução

## Eureka 

Você vai precisar alterar seu arquivos `/hosts`. No `Ubuntu` o caminho dele é `/etc/hosts`. Sem esse mapeamento, você não conseguirá iniciar com facilidade as instâncias do `Eureka`. No arquivo, deve conter o conteúdo abaixo (ou similar):
```txt
127.0.0.1 localhost
127.0.0.1 server1
127.0.0.1 server2
127.0.0.1 server3
```

## Config server

Na pasta (/config-server/src/main/resources/)[/config-server/src/main/resources/], deixei no arquivo `application.properties` o servidor configurado para procurar as configurações em um repositório privado. É importante que seja privado, uma vez que há nas configurações várias chaves secretas.
```.properties
spring.cloud.config.server.git.uri=https://github.com/L-Marcel/distributed-system-with-ai-config.git
spring.security.user.name=root
spring.security.user.password=root
spring.cloud.config.server.git.search-paths={application}/{profile}
spring.cloud.config.server.git.default-label=main
spring.cloud.config.server.git.username=L-Marcel
```

Estou deixando para você um repositório template para as configurações, disponível em [Distributed System With AI Config Template](https://github.com/L-Marcel/distributed-system-with-ai-config-template). Há instruções extras no `readme.md` deste mesmo template destinadas exclusivamente ao repositório de configurações.

Além disso, você vai precisar criar um arquivo nessa mesma pasta chamado `secret.properties`. É nele que há a chave de acesso para o repositório privado.
```.properties
spring.cloud.config.server.git.password=<ACCESS TOKEN>
```

Você pode conseguir esse token no `GitHub`, indo em [Personal Access Token](https://github.com/settings/personal-access-tokens) e criando um token de acesso restrito apenas ao seu repositório de configuração. Cuidado para não deixar esse token vazar! E se vazar, apague-o o quanto antes e crie um novo.

### Limitações relacionadas aos modelos

Este projeto não está utilizando o `autoconfigure` do `Spring AI` que permite uma troca rápida entre modelos de empresas diferentes. Então, como para este projeto utilizei o model `gpt-5-mini` da `Open AI`, se quiser utilizar algum modelo, por exemplo, do `Gemini`, vai precisar mexer nas configurações manuais que fiz de alguns beans (ou migrar para as configurações automáticas).

Essas configurações manuais estão, geralmente, nos arquivos `AiConfiguration`, localizados dentro das pastas `configurations` de cada microsserviço.

Por que não usei as automáticas? Enfrentei muitos problemas de compatibilidade entre o `Spring Cloud` e `Spring AI`. Pode soar arrogante, mas acredito ter relação com o fato do `Spring AI` ainda está meio prematuro no momento em que este projeto foi desenvolvido (por ser um pacote recente). É bem provável que em versões futuras esses problemas, que não vou detalhar aqui, sumam.

## Docker

Alguns microsserviços não vão iniciar se não conseguir se comunicar, por exemplo, com o banco de dados. Dito isto, deixei um arquivo [docker-compose.yml](/docker-compose.yml) pronto. Você só precisará ter o [Docker](https://www.docker.com/) corretamente instalado na sua máquina e executar o comando:
```cmd
docker compose up -d
```

Esse comando inicializará os seguintes containers:
- Redis (utilizado para rate-limit do gateway);
- Postgres (banco de dados padrão);
- Prometheus (extração de métricas);
- Grafana (observabilidade);
- Zipkin (rastreamento).

### Sobre a escolha de banco de dados

Eu sei que para um sistema distribuído o `Postgres` pode não ser uma boa ideia, no geral temos que olhar para as propriedades: `tolerância a partições`, `consistência` e `disponibilidade`; pensando no [Teorema CAP](https://pt.wikipedia.org/wiki/Teorema_CAP), ou sua extensão, o [Teorema PACELC](https://en.wikipedia.org/wiki/PACELC_design_principle). Contudo, se trantando de uma aplicação simples e que não vai escalar muito para a finalidade a qual a desenvolvi, escolhi o `Postgres` unicamente pela familiaridade.

## Execução otimizada

É necessário ter o [JDK 21](https://www.oracle.com/br/java/technologies/downloads/#java21) e o [Maven](https://maven.apache.org/) — estou usando a versão `3.8.7`, mas não acho que a versão se tornará um problema para você.

Sobre o ambiente, recomendo o uso do [VSCode](https://code.visualstudio.com/) com o [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack). Mas, para a execução de alguns microserviços, que devem iniciar com `profiles` específicos, como é o caso do `Eureka`, ele não vai ser muito útil.

### Comandos de execução

Antes de executar quaisquer comandos, dê um olhada na [ordem de execução](#ordem-de-execução).

Vale destacar, para todos os comandos listados a seguir, deixei uma `Task` do `VSCode` configurada em [/.vscode/tasks.jon](/.vscode/tasks.json). Utilizando a extensão [Task Runner](https://marketplace.visualstudio.com/items?itemName=SanaAjani.taskrunnercode) você deve conseguir executar tudo com mais facilidade.

Além disso, alguns comandos seguem parâmetros de configuração para otimização do uso de recursos de memória (principalmente). Logo, talvez você queira mudar um pouco alguns em um cenário de uso real. Segue a lista de comandos:

- Compilar tudo:
  ```cmd
  mvn clean install
  ```
- Executar configuration ([/config-server](/config-server/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar config-server/target/config-server-*.jar
  ```
- Executar eureka server 1 ([/eureka](/eureka/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar eureka/target/eureka-*.jar --spring.profiles.active=dev,server1
  ```
- Executar eureka server 2 ([/eureka](/eureka/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar eureka/target/eureka-*.jar --spring.profiles.active=dev,server2
  ```
- Executar eureka server 3 ([/eureka](/eureka/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar eureka/target/eureka-*.jar --spring.profiles.active=dev,server3
  ```
- Executar gateway ([/gateway](/gateway/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar gateway/target/gateway-*.jar
  ```
- Executar extractions MCP ([/extractions-mcp](/extractions-mcp/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar extractions-mcp/target/extractions-mcp-*.jar
  ```
- Executar extractions ([/extractions](/extractions/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar extractions/target/extractions-*.jar
  ```
- Executar notices ([/notices](/notices/)):
  ```cmd
  java -Xms128m -Xmx256m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar notices/target/notices-*.jar
  ```
- Executar serverless ([/notices](/notices/)):
  - Windows:
    ```cmd
    java -Xms64m -Xmx128m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar serverless/target/serverless-*.jar
    ```
  - Linux (precisa de privilégio de administrador):
    ```cmd
    sudo java -Xms64m -Xmx128m -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xss256k -jar serverless/target/serverless-*.jar
    ```

### Por que o serverless requer permissões elevadas no Linux?

Tive a ideia de utiliza o pacote `oshi-core` no `serverless` para que ele retornasse alguma informação mais interessante, neste caso, dados do hardware do computador que está hospedando. O problema é que no método `.getPhysicalMemory()`, se você olhar a descrição atribuída a ele, está escrito (pelo menos na versão que utilizei):

> Physical memory, such as banks of memory.
> 
> On Linux, requires elevated permissions. On FreeBSD and Solaris, requires installation of dmidecode.

Não perdi permissão no `Windows` é que talvez seja estranho, parando para pensar. Segue o trecho do código em que este método é chamado no arquivo [/serverless/src/main/java/ufrn/imd/serverless/functions/SystemFunctions.java](/serverless/src/main/java/ufrn/imd/serverless/functions/SystemFunctions.java):
```java
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
```

### Ordem de execução

Respeite a ordem de execução e regras estabelecidas abaixo, e sem esquecer de subir os containers do `Docker` antes.

1. Configuration ([/config-server](/config-server/)):

- Configurado para apenas uma instância;
- Espere ele iniciar corretamente, todos os demais vão tentar se comunicar com ele assim que iniciarem.

2. Eureka ([/eureka](/eureka/)):

- Configurado para exatamente trẽs instâncias;
- Espere ao menos uma instância iniciar corretamente;
- É normal algumas instâncias lançarem um `warning` por não encontrarem as demais em um primeiro momento.

3. Gateway ([/gateway](/gateway/));

4. Notices ([/notices](/notices/)):

- Espere o banco de dados está disponível.

5. Serverless ([/serverless](/serverless/));

6. Extractions MCP ([/extractions-mcp](/extractions-mcp/)):

- Espere o banco de dados está disponível.

7. Extractions ([/extractions](/extractions/)):

- Espere o `Extractions MCP` está disponível pelo `Gateway`.
 
# Rotas

```
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
```

## Mocks e teste básico

Mande um `PUT` na rota `http://localhost:8080/notices/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11` enviando dentro de um `multipart/form-data` o `.pdf` do edital no formato `application/pdf`. Deixe um edital público que pode ser usado como exemplo em [/tests/docs](/tests/docs).

Por que o `PUT` vai funcionar em um `UUID` específico antes de criar qualquer coisa no banco de dados? Bom, deixei dados mockados em [/notices/src/main/resources/data.sql](/notices/src/main/resources/data.sql).

Você também pode testar o `POST` na rota `http://localhost:8080/notices` enviando dentro de um `multipart/form-data` o `.pdf` do edital no formato `application/pdf`. Neste caso, acho que vai precisar de um `.pdf` com nome diferente do mockado.

## Refresh das configurações

```cmd
curl -X POST http://localhost:8080/notices/actuator/refresh
```

# Testes com o JMeter

Uma vez tendo configurado tudo, e iniciado as instâncias, você pode testar utilizando o [Apache Jmeter](https://jmeter.apache.org/). Basta importar com ele o arquivo [/tests/jmeter.jmx](/tests/jmeter.jmx) e executar os testes. Há nele também exemplo de requisições para o `notices`.