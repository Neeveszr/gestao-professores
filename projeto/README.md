# CRUD de professores

- **Aluno:** [preencher nome completo]
- **Disciplina:** [preencher nome da disciplina]

API REST de cadastro e gerenciamento de professores, desenvolvida a partir do projeto `gestao_fsa-main`. Permite listar, filtrar por nome e área, cadastrar, editar e excluir professores.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- SpringDoc OpenAPI 3.1.0 (Swagger UI)
- PostgreSQL
- Maven

As únicas dependências de execução são Spring Web, Spring Data JPA, SpringDoc e o driver PostgreSQL. O starter de testes Web MVC é utilizado somente nos testes.

## Como executar

1. Instale o JDK 21 e tenha um servidor PostgreSQL em execução.
2. No PostgreSQL, crie o banco de dados:

   ```sql
   CREATE DATABASE gestao_fsa;
   ```

3. Conecte-se ao banco `gestao_fsa` e execute o arquivo [`sql/professor.sql`](sql/professor.sql). Ele cria a tabela `professor` com `BIGSERIAL` e os quatro campos `VARCHAR` solicitados, além de três registros iniciais. Execute esse script uma vez em um banco novo; ele não apaga tabelas ou dados existentes.
4. Configure a conexão no terminal. Exemplo para PowerShell, substituindo a senha:

   ```powershell
   $env:DB_URL = 'jdbc:postgresql://localhost:5432/gestao_fsa'
   $env:DB_USERNAME = 'postgres'
   $env:DB_PASSWORD = 'SUA_SENHA'
   ```

   Exemplo para Linux/macOS:

   ```bash
   export DB_URL='jdbc:postgresql://localhost:5432/gestao_fsa'
   export DB_USERNAME='postgres'
   export DB_PASSWORD='SUA_SENHA'
   ```

   `DB_URL` e `DB_USERNAME` têm os valores padrão acima. `DB_PASSWORD` deve ser informada. A aplicação valida a tabela criada manualmente; não cria nem altera o esquema automaticamente.

5. Na pasta do projeto, execute:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   No Linux/macOS:

   ```bash
   sh mvnw spring-boot:run
   ```

   O Maven Wrapper baixa o Maven na primeira execução caso ele ainda não esteja disponível.

6. Abra o [Swagger UI](http://localhost:8080/swagger-ui/index.html). A documentação OpenAPI está em [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

## Estrutura

```text
src/main/java/com/fsa/gestao/
├── GestaoApplication.java
├── controller/ProfessorController.java
├── service/ProfessorService.java
├── repository/ProfessorRepository.java
└── model/Professor.java
```

## Endpoints

| Método | Endpoint | Descrição | Sucesso |
| --- | --- | --- | --- |
| GET | `/professores` | Lista todos os professores | 200 |
| GET | `/professores/nome/{nome}` | Filtra pelo trecho do nome, sem diferenciar maiúsculas e minúsculas | 200 |
| GET | `/professores/area/{area}` | Filtra pela área completa, sem diferenciar maiúsculas e minúsculas | 200 |
| POST | `/professores` | Cadastra um professor, com ID gerado pelo banco | 201 |
| PUT | `/professores/{id}` | Atualiza os quatro campos do professor identificado na URL | 200 |
| DELETE | `/professores/{id}` | Exclui o professor identificado na URL | 204 |

As consultas utilizam `findByNomeContainingIgnoreCase` e `findByAreaIgnoreCase`, sem `@Query`. A busca por nome não remove acentos: `joão` encontra `João`, enquanto `joa` encontra `JOANA`. Edição e exclusão de um ID inexistente retornam 404. A exclusão bem-sucedida retorna 204, sem corpo.

### Cadastro

Envie `POST /professores` com `Content-Type: application/json`:

```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "area": "Desenvolvimento",
  "telefone": "86999999999"
}
```

O ID é gerado pelo PostgreSQL e retornado junto aos dados cadastrados.

### Edição

Envie `PUT /professores/{id}` usando o ID recebido no cadastro:

```json
{
  "nome": "Maria Silva Santos",
  "email": "mariasantos@email.com",
  "area": "Engenharia de Software",
  "telefone": "86988888888"
}
```

## Testes automatizados

Execute no Windows:

```powershell
.\mvnw.cmd test
```

No Linux/macOS:

```bash
sh mvnw test
```

Os oito testes usam MockMvc com o controller e o service reais e o repository simulado. Verificam os seis endpoints, os dados das respostas, os códigos HTTP, o encaminhamento dos filtros para os métodos derivados, o uso do ID da URL na edição e os retornos 404. Eles não precisam de PostgreSQL e não comprovam a integração com o banco.

Resultado verificado em 04/10/2026: **8 testes executados, 0 falhas, 0 erros e 0 testes ignorados**. A compilação do código de produção e dos testes foi verificada com Java 21. No ambiente de preparação, a compilação exigiu contornar uma restrição de acesso a JARs do compilador; o Maven Surefire executou a suíte com sucesso.

## Evidências de execução com PostgreSQL

**Pendente:** executar a API com PostgreSQL e inserir os seis prints reais abaixo antes da entrega. Não há imagens de execução incluídas nesta versão. A tentativa de conexão com `localhost:5432` foi recusada no ambiente de preparação.

Use o Swagger UI: abra a operação, clique em **Try it out**, preencha os parâmetros ou o JSON e clique em **Execute**. Cada captura deve mostrar o método, a URL, os dados enviados quando houver, o status e a resposta.

| Caso | Requisição | Verificação e print necessário |
| --- | --- | --- |
| 1 | `GET /professores` | 200 e lista dos professores iniciais |
| 2 | `GET /professores/nome/JOA` | 200 e `MARIA JOANA`, comprovando busca parcial sem diferenciar maiúsculas |
| 3 | `GET /professores/area/desenvolvimento` | 200 e professores da área `Desenvolvimento` |
| 4 | `POST /professores` | JSON de cadastro acima, 201 e registro com ID gerado |
| 5 | `PUT /professores/{id}` | Use o ID do caso 4, envie o JSON de edição e capture 200 com os dados alterados |
| 6 | `DELETE /professores/{id}` | Use o mesmo ID, capture 204 e execute a listagem novamente para conferir sua ausência |

## Entrega

Preencha a identificação, execute os testes com PostgreSQL e inclua os prints nesta seção. Publique os arquivos em um repositório público no GitHub, sem `target/`, pastas de IDE ou senhas. Confira que o README e as imagens estão acessíveis. Envie o link do repositório na Plataforma A, conforme a atividade.
