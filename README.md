# Library API

API REST de gerenciamento de biblioteca: cadastro de usuários e livros, empréstimos com cálculo de preço e multa por atraso, autenticação JWT e controle de acesso por perfil (ADMIN/USER).

Projeto de portfólio. O frontend será desenvolvido em uma próxima etapa.

## Funcionalidades

- Autenticação e autorização com JWT
- Perfis ADMIN e USER
- CRUD de livros com controle de estoque
- Empréstimos com preço progressivo por dia e multa composta em caso de atraso
- Exclusão de conta com soft delete (bloqueada se houver empréstimo ativo)
- Paginação nas listagens
- Tratamento global de exceções
- Documentação interativa com Swagger/OpenAPI

## Stack

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Security, Validation)
- MySQL 8
- JWT (jjwt 0.11.5)
- springdoc-openapi (Swagger UI)
- Lombok
- Gradle (wrapper incluso)

## Pré-requisitos

- JDK 17
- MySQL 8 em execução
- Git

Não é preciso instalar o Gradle, o projeto usa o wrapper (`gradlew`).

## Como rodar

1. Clone o repositório:

   ```bash
   git clone https://github.com/Herbert-Monteiro/user-management-api.git
   cd user-management-api
   ```

2. Crie o banco de dados no MySQL:

   ```sql
   CREATE DATABASE user_management_api;
   ```

3. Crie o arquivo de configuração a partir do exemplo:

   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

   No Windows (PowerShell):

   ```powershell
   copy src\main\resources\application.properties.example src\main\resources\application.properties
   ```

4. Edite o `application.properties` com seus dados:

   | Propriedade | Descrição |
      |---|---|
   | `spring.datasource.username` | Usuário do MySQL |
   | `spring.datasource.password` | Senha do MySQL |
   | `api.security.token.secret` | Segredo do JWT (mínimo de 32 caracteres) |
   | `api.security.token.expiration` | Validade do token em ms (padrão: 3600000 = 1h) |

   O `application.properties` está no `.gitignore` e não deve ser commitado.

5. Execute a aplicação:

   ```bash
   ./gradlew bootRun
   ```

   No Windows: `gradlew.bat bootRun`

6. Acesse o Swagger UI em: http://localhost:8080/swagger-ui/index.html