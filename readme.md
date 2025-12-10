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
  - [Execução rápida](#execução-rápida)
  - [Execução otimizada](#execução-otimizada)

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

## Execução rápida


## Execução otimizada

