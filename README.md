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
* Docker (opcional para banco de dados real e de teste)

---

## ✨ Executando o Projeto

### Clone o repositório

```bash
git clone git@github.com:DhenSouza/user-management-service.git
```

### Banco de Dados

Este projeto disponibiliza dois ambientes de banco PostgreSQL via Docker:

1. **Banco principal** (para desenvolvimento e produção local)
2. **Banco de teste** (para execução de testes automatizados)

#### 🐳 Docker Compose completo

Crie (ou atualize) um arquivo `docker-compose.yml` na raiz do projeto com o seguinte conteúdo:

```yaml
version: '3.8'

services:
  user-service:
    image: postgres:15.2
    container_name: user-service
    restart: always
    environment:
      POSTGRES_DB: userdb
      POSTGRES_USER: administration
      POSTGRES_PASSWORD: admin
      POSTGRES_INITDB_ARGS: "--auth=md5"
    ports:
      - "5433:5432"
    volumes:
      - userdb_data:/var/lib/postgresql/data
    networks:
      - backend

  db-test:
    image: postgres:15.2
    container_name: postgres_userdb_test
    restart: always
    environment:
      POSTGRES_DB: userdb_test
      POSTGRES_USER: administration
      POSTGRES_PASSWORD: admin
      POSTGRES_INITDB_ARGS: "--auth=md5"
    ports:
      - "5434:5432"
    volumes:
      - userdb_test_data:/var/lib/postgresql/data
    networks:
      - backend

volumes:
  userdb_data:
  userdb_test_data:

networks:
  backend:
    driver: bridge
```

> 🚀 **Iniciar os containers**
>
> ```bash
> docker-compose up -d
> ```

#### 🔧 Configuração de conexões no `application.properties`

Para o banco principal (desenvolvimento local):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/userdb
spring.datasource.username=administration
spring.datasource.password=admin
```

Para os testes de integração (arquivo `src/test/resources/application-test.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5434/userdb_test
spring.datasource.username=administration
spring.datasource.password=admin
spring.flyway.enabled=false
```

> 💡 **Nota:** ajuste as portas caso já haja outro PostgreSQL rodando.

---

### 🔒 Secret Management

Atualmente, o projeto utiliza a configuração de secrets (como senhas e chaves JWT) diretamente no `application.properties` como uma solução **provisória**. A recomendação ideal é usar serviços seguros como **AWS Secrets Manager**, **HashiCorp Vault** ou variáveis de ambiente.

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

> Certifique-se de que seu `application.properties` ou `application-test.properties` esteja configurado para usar o banco correto.

---

### 📦 Migrations automáticas com Flyway

O projeto utiliza Flyway para versionamento e execução automática de scripts SQL.

Coloque migrations em `src/main/resources/db/migration` com prefixo `V`:

```sql
-- exemplo: V1__create_table_users.sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

Configuração adicional em `application.properties`:

```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.ignore-future-migration=true
spring.flyway.ignore-missing-migrations=true
spring.flyway.fail-on-missing-locations=false
```

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

> Há um migrator que cria um usuário ADMIN inicial para testes de endpoints.

### Exemplo de request protegido

```bash
curl --request GET \
  --url 'http://localhost:8080/api/users?page=0&size=5&sort=name%2Casc' \
  --header 'Authorization: Bearer <token>'
```

---

## 🔖 Documentação Swagger

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
