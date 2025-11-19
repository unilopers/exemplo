# Projeto de exemplo

- Marcos Aurelio

## Sobre este repositório

Este é um projeto de exemplo em Java utilizando Spring Boot que demonstra um conjunto básico de funcionalidades REST e persistência com Spring Data JPA.

- Principais modelos: `Usuario`, `Cargo`, `Post`.
- Implementa endpoints CRUD via controllers REST (`UsuarioController`, `CargoController`, `PostController`).
- Utiliza um banco H2 em memória para desenvolvimento (configurado em `src/main/resources/application.properties`).
- As repositories estendem `JpaRepository` para operações de persistência (ex.: `UsuarioRepository`, `CargoRepository`, `PostRepository`).

O objetivo é servir como referência para criar uma API REST simples com validações básicas (por exemplo: unicidade de `email`, `name`, `title`) e relacionamentos JPA (`ManyToMany`, `ManyToOne`).

### Como executar

1. Certifique-se de ter Java instalado e configurado (variável `JAVA_HOME`).
2. Use o Maven wrapper para executar a aplicação:

```bash
./mvnw spring-boot:run
```

3. Acesse a API em `http://localhost:8080` e o console H2 em `http://localhost:8080/h2-console` (ver `application.properties`).
