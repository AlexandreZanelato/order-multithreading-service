# Order Multithreading Service

Este projeto é um microserviço desenvolvido com **Spring Boot** que processa pedidos de forma **assíncrona** utilizando **multithreading**. Ele segue boas práticas de arquitetura, testes automatizados, integração contínua e empacotamento com Docker.

---

## Tecnologias Utilizadas

- Java 21 (com recursos em preview)
- Spring Boot 3.4.2
- Spring Web
- Spring Data JPA
- Spring Async
- Spring Actuator
- SpringDoc OpenAPI (Swagger)
- H2 Database (dev/test/staging)
- MySQL Cloud SQL (prod)
- Maven
- Docker & Docker Compose
- GitHub Actions (CI/CD)
- Mockito & MockMvc (testes unitários e de integração)

---

## Boas Práticas Adotadas

- **Separação de responsabilidades**: camadas bem definidas (Controller, Service, Repository)
- **Programação assíncrona com `@Async`** e `CompletableFuture`
- **Testes unitários e de integração** com cobertura de comportamento
- **Uso de `record` para DTOs**: imutabilidade e clareza
- **Ambientes isolados:** `application-test.yml`/`application-dev.yml`/`application-staging.yml`/`application-prod.yml`
- **Dockerfile otimizado** para produção
- **CI/CD com GitHub Actions**: build, testes e deploy automatizado
- **Segurança:** Uso de **secrets no GitHub** para proteger credenciais

---

## Testes Automatizados

- `AsyncConfigTest`: garante execução em threads distintas
- `OrderControllerAsyncTest`: simula múltiplas chamadas paralelas
- `0rderControllerTest`: simula chamadas HTTP com MockMvc
- `OrderServiceIntegrationTest`: verifica persistência real com H2
- `OrderServiceTest`: valida lógica de negócio

---

## Estrutura do Projeto

Organização baseada em **Package by Layer**:
````
src/ 
├── main/
│   ├── java/br/com/zanelato/orderservice/ 
│   │   ├── config/         # Configurações assíncronas
│   │   ├── controller/     # Endpoints REST   
│   │   ├── dto/            # DTOs
│   │   ├── model/          # Entidade
│   │   ├── repository/     # Acesso a dados
│   │   └── service/        # Regras de negócio
│   └── resources/
│       └── application.yml
│       └── application-test.yml
├── test/
│   ├── java/br/com/zanelato/orderservice/
│   │   ├── config/         # Testes de Multithreading
│   │   ├── controller/     # Testes de API
│   │   └── service/        # Testes de Integração

````
---
## Ambientes

| Ambiente | Banco           | Profile | Deploy                                           |
| -------- | --------------- | ------- | ------------------------------------------------ |
| Dev      | H2 (local)      | dev     | mvn spring-boot\:run                             |
| Staging  | H2 (Cloud Run)  | staging | Push main branch → CI/CD                         |
| Prod     | MySQL Cloud SQL | prod    | Workflow manual (`workflow_dispatch`) ou release |

---

## Endpoints da API

A aplicação estará disponível em:
```
http://localhost:8080
```
- src/main/resources/postman → collection para testes.

### Criar Pedido
POST /orders
```
{
"product": "Notebook",
"quantity": 2
}
```
### Consultar Status
GET /orders/{orderId}/status

---

## Swagger (OpenAPI)
A documentação da API está disponível em:

```
http://localhost:8080/swagger-ui.html
```

---

## Exemplos de uso com curl

Crie 5 pedidos simultaneamente

```
for i in {1..5}; do 
  curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" -d '{"product":"Produto-'$i'","quantity":'$i'}' &
done
```

Consulte o status de cada orderId

```
curl http://localhost:8080/orders/{orderId}/status

```

---

## Docker

Build da imagem:

```
docker build -t z-order/order-service .
```

Rodar local:

```
docker run -d -p 8080:8080 z-order/order-service .
```

Subir com Docker Compose:

```
docker-compose up -d
```

---

## CI/CD

Pipeline automatizado com GitHub Actions:
- Build e testes com Maven
- Geração de JAR com preview features
- Build e push da imagem Docker
- Deploy automático em staging, push na main
- Deploy manual em prod via workflow_dispatch ou release
- Uso de secrets para credenciais no, Docker Hub, GCP, Cloud SQL

---

## Observações

É um projeto de estudo/demonstração.

Pode ser usado como base para aprendizado.

Não é uma aplicação pronta para produção.

Perfis ativos (dev, staging, prod) configurados via SPRING_PROFILES_ACTIVE