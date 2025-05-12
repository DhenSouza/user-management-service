# User Management Service API

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Uma API RESTful para gerenciamento de usuários e endereços, com autenticação JWT, controle de acesso por papéis (roles), paginação, ordenação, Swagger e cobertura de testes automatizados.

---

## 📦 Funcionalidades

* CRUD de usuários
* CRUD de endereços
* Autenticação com JWT
* Controle de acesso por roles: `ADMIN` e `USER`
* Paginação e ordenação por nome, e-mail e data de criação
* Validação com Jakarta Bean Validation
* Documentação com Swagger/OpenAPI 3.0
* Tratamento global de exceções
* Testes unitários e de integração

---

## ⚙️ Requisitos

* Java 21+
* Maven 3.9+
* Docker (opcional para banco de dados real)

---

## ✨ Executando o Projeto

### Clone o repositório

```bash
git clone git@github.com:DhenSouza/user-management-service.git
```

### Banco de Dados

Por padrão o projeto usa PostgreSQL com Docker:

### 🐳 Rodando o banco PostgreSQL com Docker

Você pode iniciar o PostgreSQL de duas formas:

#### ✅ Usando `docker-compose.yml` (recomendado e usado atualmente no projeto)

Crie um arquivo `docker-compose.yml` com o seguinte conteúdo:

```yaml
services:
  postgres:
    image: postgres:15.2
    container_name: postgres
    environment:
      POSTGRES_USER: administration
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: userdb
      POSTGRES_INITDB_ARGS: "--auth=md5"
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - backend

volumes:
  postgres_data:

networks:
  backend:
```

Depois, execute:

```bash
docker-compose up -d
```

#### ✅ Usando `docker run` diretamente

Se preferir, você pode iniciar o container com:

```bash
docker run --name postgres \
  -e POSTGRES_USER=administration \
  -e POSTGRES_PASSWORD=admin \
  -e POSTGRES_DB=userdb \
  -p 5433:5432 \
  -d postgres
```

#### ✅ Usando a aplicação diretamente com banco de dados local:
- Para isso basta nao rodar o docker-compose.yml e setar as configurações em seu application.properties de acordo com sua necessidade: 


#### 🛠️ Configuração do `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/userdb
spring.datasource.username=administration
spring.datasource.password=admin
```

> 💡 **Nota:** certifique-se de que a porta `5433` não esteja sendo usada por outro serviço.
> > 💡 **Nota:** a porta `5433` foi utilizada para evitar conflitos caso ja tenha um banco de dados PostgresSQL com configurações default.

---

### 🔒 Secret Management

Atualmente, o projeto utiliza a configuração de secrets (como senhas e chaves JWT) diretamente no `application.properties` como uma solução **provisória**, com valores hardcoded.  
A recomendação ideal é armazenar essas informações sensíveis em serviços seguros como o **AWS Secrets Manager**, **HashiCorp Vault** ou similares, com acesso via injeção de dependência ou variáveis de ambiente.

```properties
# === JWT Configuration ===
security.jwt.secret=hVZSpn47ytq9kCM7zPIYmeNgCWbLogF0eQlVa0tVXTYJDrBKQHX8u
security.jwt.expiration=3600000
```
---

### Subir com Docker

```bash
docker-compose up -d
```

> Certifique-se de que seu `application.properties` esteja configurado corretamente para usar PostgreSQL e os secrets adequados.

---
### 📦 Migrations automáticas com Flyway

O projeto utiliza o Flyway para versionamento e execução automática de scripts SQL na inicialização.  
As migrations devem ser colocadas no diretório `src/main/resources/db/migration` com o prefixo `V` seguido do número da versão e nome descritivo.

Exemplo:
- `V1__create_table_users.sql`
- `V2__insert_initial_roles.sql`

Exemplo de conteúdo:
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

Configuração necessária no `application.properties`:
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.ignore-future-migration=true
spring.flyway.ignore-missing-migrations=true
spring.flyway.fail-on-missing-locations=false
spring.flyway.skip-default-callbacks=true
spring.flyway.skip-default-resolvers=true
spring.flyway.teams.enabled=false
spring.flyway.teams.url-check-enabled=false
```

➡️ Com isso, ao rodar a aplicação com `./mvnw spring-boot:run` ou `docker-compose up`, as migrations serão executadas automaticamente se ainda não aplicadas.

---
### Executar localmente via Maven

```bash
./mvnw spring-boot:run
```

A aplicação subirá em: [http://localhost:8080](http://localhost:8080)

---

## 🔐 Autenticação JWT

### Login:

**POST** `/api/auth/login`

```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```
- OBS: Projeto tera um Migrator que criara um usuario Administrador, para testar os endpoints livremente basta realizar a request de login

**Request de exemplo do Login**
```txt
curl --request POST \
--url http://localhost:8080/api/auth/login \
--header 'Content-Type: application/json' \
--header 'User-Agent: insomnia/11.1.0' \
--data '{
"email": "admin@example.com",
"password": "admin123"
}'
```
**Request de exemplo de endpoint**
```txt
curl --request GET \
--url 'http://localhost:8080/api/users?page=0&size=5&sort=name%2Casc' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImlhdCI6MTc0NzA2ODYwMiwiZXhwIjoxNzQ3MDcyMjAyfQ.08MhFGTmk1iayPeJE4v3s61rqQ9VM0-qql99KLTJpwo' \
--header 'User-Agent: insomnia/11.1.0'
```
```
```

Use o token JWT retornado no header das requisições protegidas:

```
Authorization: Bearer <token>
```

---

## 🔖 Documentação Swagger

Disponível em:

* [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 👥 Papéis de Acesso

| Papel | Permissões                        |
| ----- | --------------------------------- |
| ADMIN | Acesso total a todos os recursos  |
| USER  | Acesso somente aos próprios dados |

---

## 🔢 Endpoints

### Usuários

* `GET /api/users` \[ADMIN]
* `GET /api/users/{id}` \[ADMIN ou próprio]
* `POST /api/users` \[ADMIN]
* `PUT /api/users/{id}` \[ADMIN ou próprio]
* `PUT /api/users/{id}/password` \[ADMIN ou próprio]
* `DELETE /api/users/{id}` \[ADMIN]

### Endereços

* `GET /api/addresses/user/{userId}` \[ADMIN ou próprio]
* `POST /api/addresses/user/{userId}` \[ADMIN ou próprio]
* `PUT /api/addresses/{id}` \[ADMIN ou próprio]
* `DELETE /api/addresses/{id}` \[ADMIN ou próprio]

---

## 🔬 Testes

Executar todos os testes:

```bash
./mvnw test
```

* Testes unitários com Mockito
* Testes de integração com MockMvc
* Testcontainers configurável para PostgreSQL

---

## 📂 Estrutura

```
src/
 ├── main/java/.../Domain
 ├── application
 ├── infrastructure
 ├── config
 └── test/
     ├── unitTest
     └── integration
```

---

## 👤 Autor

Feito por [Denilson Souza](https://github.com/DhenSouza). Contribuições são bem-vindas!

---

## ✉️ Licença

Distribuído sob licença MIT. Veja [LICENSE](LICENSE) para mais detalhes.
