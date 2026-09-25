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
docker compose up -d
```

O Compose inicia PostgreSQL 16. Como `application.properties` também contém configuração de H2 e o driver H2 está limitado aos testes no `pom.xml`, configure explicitamente o PostgreSQL antes de iniciar. Exemplo para **PowerShell**, usando exclusivamente o banco local do Compose:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/tecnomanager"
$env:SPRING_DATASOURCE_USERNAME="user"
$env:SPRING_DATASOURCE_PASSWORD="secret"
$env:SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"
$env:SPRING_DOCKER_COMPOSE_ENABLED="false"
.\mvnw.cmd spring-boot:run
```

Em Linux/macOS, exporte as mesmas variáveis e execute `./mvnw spring-boot:run`. `user` e `secret` são valores de desenvolvimento presentes no Compose; use credenciais próprias em outros ambientes.

Swagger: <http://localhost:8080/swagger-ui.html>.

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

Os testes existentes ficam em `src/test`. Execute `./mvnw test` (Windows: `.\mvnw.cmd test`) em um ambiente de teste separado. A documentação não representa uma execução recente da suíte.

Roteiro de validação do caso principal:

1. Criar um projeto e uma tarefa pendente.
2. Tentar concluir o projeto e verificar a rejeição da operação.
3. Concluir a tarefa e repetir a alteração do projeto.
4. Verificar o estado persistido e a resposta HTTP.

## Estado e próximos passos

Projeto de portfólio em evolução. Antes de publicar uma demonstração acessível externamente:

- Separar as configurações de desenvolvimento e testes.
- Cobrir a regra de conclusão com testes automatizados, incluindo projeto inexistente e status inválido.
- Restringir o endpoint `POST /seed`: a implementação limpa e repopula o banco, e a restrição por perfil está comentada no controller. Não o execute sobre dados que precise preservar.
- Validar autenticação, autorização e configuração do ambiente de implantação.

O repositório contém um Dockerfile, mas sua presença não equivale a validação para produção.
