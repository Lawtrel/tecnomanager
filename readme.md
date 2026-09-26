# TecnoManager

API REST para organizar projetos, tarefas e membros de uma empresa júnior. O principal caso de negócio é impedir a conclusão de um projeto enquanto existirem tarefas não concluídas.

**Java 21 · Spring Boot 3.4 · Spring Data JPA · PostgreSQL · Flyway · OpenAPI**

## O que o código implementa

- Cadastro de projetos e membros, com associação entre eles.
- Tarefas vinculadas a projetos, responsáveis, prazos e status.
- Validação de tarefas pendentes ao alterar um projeto para `CONCLUIDO`.
- Dashboard com indicadores de projetos, tarefas e membros.
- DTOs com Java Records, validação de entrada, tratamento de exceções e auditoria JPA.
- Migrações de banco com Flyway e documentação Swagger.

## Organização

O código está em `src/main/java/br/lawtrel/tecnomanager`: `controller` recebe as requisições, `service` aplica as regras de negócio, `repository` acessa os dados e `model` contém as entidades. `dto`, `exception` e `config` complementam essas camadas.

## Executar localmente

Requisitos: JDK 21, Git e Docker com Compose. O Maven Wrapper está incluído.

```bash
git clone https://github.com/Lawtrel/tecnomanager.git
cd tecnomanager
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

No PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

O perfil `local` inicia o PostgreSQL 16 do `compose.yaml` por meio da integração Spring Boot Docker Compose e limita a API a `127.0.0.1`. Confira se a porta 5432 está livre. Swagger: <http://localhost:8080/swagger-ui.html>.

Sem o perfil local, Docker Compose fica desativado. Configure `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` para o seu PostgreSQL. O driver é inferido pela URL JDBC. Não ative o perfil local em uma implantação.

### Banco de demonstração

`POST /seed` limpa e repopula o banco. Ele só é registrado com `local` ativo e com `prod` e `test` ausentes. Use exclusivamente em um banco local descartável. A seleção por perfil não substitui autenticação e autorização nas demais rotas.

## Endpoints para explorar

| Método | Caminho | Uso |
| --- | --- | --- |
| GET / POST | `/projetos` | Listar ou cadastrar projetos |
| POST | `/projetos/{idProjeto}/membros/{idMembro}` | Associar um membro |
| POST | `/projetos/{id}/tarefas` | Criar tarefa |
| PATCH | `/projetos/{id}/status` | Atualizar status e validar pendências |
| GET | `/dashboard` | Consultar indicadores |

Consulte o Swagger para os corpos das requisições.

## Testes e demonstração

```bash
./mvnw --batch-mode --no-transfer-progress verify
```

Windows: `.\mvnw.cmd --batch-mode --no-transfer-progress verify`.

A suíte utiliza o perfil `test`, com H2 em memória e modo PostgreSQL, executa as migrações Flyway e valida o schema com Hibernate. Não exige Docker nem utiliza o PostgreSQL local. O arquivo de configuração H2 existe apenas em `src/test/resources`.

Os testes cobrem:

- Projeto com tarefa pendente ou em andamento: conclusão rejeitada com HTTP 409.
- Tarefas concluídas ou projeto sem tarefas: conclusão aceita com HTTP 204.
- Projeto inexistente: HTTP 404.
- Status ausente, nulo, vazio ou desconhecido: HTTP 400 sem alterar o projeto.
- JSON inválido e status inválido na criação: HTTP 400.
- Disponibilidade do seed por perfil, incluindo combinações `local,prod` e `local,test`.

Status de projeto aceitos: `PLANEJAMENTO`, `EM_PLANEJAMENTO`, `INICIADO`, `EM_ANDAMENTO` e `CONCLUIDO`. Os dois nomes de planejamento foram preservados por compatibilidade com as referências existentes. Espaços nas extremidades são removidos e letras são normalizadas para maiúsculas.

Validação local em 26/09/2026: **26 testes, nenhuma falha, nenhum erro, nenhum teste ignorado; `verify` e empacotamento concluídos** com Java 21. A execução com H2 não substitui os testes em PostgreSQL e Docker.

O workflow `.github/workflows/ci.yml` executa `verify` a cada push e pull request.

## Próximos passos

- Validar a inicialização com Docker/PostgreSQL em ambiente limpo e adicionar testes de integração nesse banco.
- Revisar autenticação e autorização antes de disponibilizar dados reais.
- Testar concorrência entre a conclusão de projetos e alterações de tarefas; a suíte atual cobre cenários sequenciais.
- Padronizar o domínio de status de projetos e tarefas em uma evolução compatível da API.

Projeto de portfólio em evolução. O repositório contém um Dockerfile, mas sua presença não equivale a validação para produção.
