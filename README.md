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

### Configuração do H2

Este projeto usa o H2 (in-memory) por padrão para desenvolvimento. A configuração principal está em `src/main/resources/application.properties` e inclui os seguintes pontos importantes:

- `spring.datasource.url` — string de conexão do H2 (ex.: `jdbc:h2:mem:exemplo`).
- `spring.h2.console.enabled` e `spring.h2.console.path` — ativam e definem o path do console web do H2 (ex.: `/h2-console`).
- `spring.sql.init.mode` — controla se os scripts SQL (`schema.sql`, `data.sql`) serão executados automaticamente na inicialização. No projeto está definido como `always`.
- `spring.jpa.hibernate.ddl-auto` — controla a criação/atualização do schema via JPA (ex.: `update`, `create`, `create-drop`, `none`).
- `spring.jpa.defer-datasource-initialization` — quando `true`, atrasa a execução dos scripts SQL até que o JPA esteja pronto (útil quando se usa JPA + scripts SQL juntos).

Existem duas formas principais de inicializar o schema e dados do H2 neste projeto:

1) Inicializando usando `schema.sql` e `data.sql` (scripts SQL)

- Coloque arquivos `schema.sql` e `data.sql` em `src/main/resources`. O Spring Boot executará esses arquivos na inicialização quando `spring.sql.init.mode=always`.
- Exemplo prático (files já incluídos neste repositório):

	- `src/main/resources/schema.sql` contém as definições de tabela e chaves estrangeiras (DDL).
	- `src/main/resources/data.sql` insere dados iniciais (DML).

- Vantagens: total controle sobre DDL/DML; ideal para seeds e exemplos específicos.
- Cuidado: se usar JPA auto-generation (ex.: `spring.jpa.hibernate.ddl-auto=update`), pode haver conflito de criação/alteração de tabelas. Use `spring.jpa.defer-datasource-initialization=true` para forçar a ordem correta: JPA prepara-se e os scripts são executados depois.

2) Gerar tabelas a partir das entidades (JPA / Hibernate)

- A propriedade `spring.jpa.hibernate.ddl-auto` define o comportamento. Valores comuns:
	- `none` — não altera o schema
	- `validate` — checa se o schema é compatível (sem alterações)
	- `update` — atualiza o schema para refletir entidades JPA (útil em dev)
	- `create` — cria o schema na inicialização (apaga dados previos)
	- `create-drop` — cria o schema na inicialização e apaga-o na parada

- Uso sugerido em desenvolvimento: `update` ou `create` para testar rapidamente. Em produção, prefira `validate` ou `none` + migrações gerenciadas por ferramentas como Flyway ou Liquibase.

Trocar entre as abordagens
- Se quiser que apenas o JPA gere as tabelas, altere `spring.sql.init.mode` para `never` e use `spring.jpa.hibernate.ddl-auto=update` (ou `create`).
- Se quiser usar apenas os scripts SQL (`schema.sql` / `data.sql`) e não o `ddl-auto`, defina `spring.jpa.hibernate.ddl-auto=none` para evitar alterações pelo Hibernate.

Exemplos rápidos:

- H2 in-memory com scripts SQL (padrão neste projeto):

```properties
spring.datasource.url=jdbc:h2:mem:exemplo;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
spring.jpa.hibernate.ddl-auto=none
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

- H2 file-based persistente e JPA gerando schema:

```properties
spring.datasource.url=jdbc:h2:./data/exemplo;AUTO_SERVER=TRUE
spring.sql.init.mode=never
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Como acessar o H2 Console
- Acesse `http://localhost:8080/h2-console` e informe a JDBC URL usada (ex.: `jdbc:h2:mem:exemplo`). Se estiver usando memória, o nome do DB deve ser o mesmo configurado em `spring.datasource.url`.

Observações e boas práticas
- `spring.jpa.hibernate.ddl-auto=update` pode ser conveniente para desenvolvimento, mas pode causar mudanças inesperadas no schema em ambientes maiores; evite em produção.
- Prefira usar scripts SQL e/ou ferramentas de migração (Flyway/Liquibase) para garantir controle total do schema em produção.
