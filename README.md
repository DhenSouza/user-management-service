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

### Clone o repositório:

```bash
git clone git@github.com:DhenSouza/user-management-service.git
```

### Banco de Dados

Por padrão o projeto usa PostgreSQL com Docker:

```bash
docker run --name postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=userdb -p 5432:5432 -d postgres
```

Configure no `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=postgres
spring.datasource.password=admin
```

### Subir com Docker

```bash
docker-compose up --build
```

> Certifique-se de que seu `application.properties` esteja configurado corretamente para usar PostgreSQL.
> OBS: Secret estara alocada via .properties provisóriamente.
> 
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
