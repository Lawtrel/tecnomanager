📊 TecnoManager - API de Gestão de Projetos
Status: 🚀 Em Desenvolvimento (MVP Completo)

API REST robusta desenvolvida para gerenciamento de membros, tarefas e projetos, simulando o ambiente de uma Empresa Júnior. Este projeto vai além do CRUD básico, focando em Arquitetura de Software, Qualidade de Código e DevOps Local.

🛠️ Tecnologias & Arquitetura
O projeto foi construído uotimtilizando as práticas mais modernas do ecossistema Java (2025/2026):

Linguagem: Java 21 (LTS) - Utilizando Records e recursos modernos.

Framework: Spring Boot 3.4.

Banco de Dados: PostgreSQL 16 (Produção/Dev via Docker) e H2 (Testes Automatizados).

Versionamento de Banco: Flyway Migrations (Gerenciamento seguro de schema).

Containerização: Docker & Docker Compose.

Qualidade de Código:

Zero Lombok: Código Java puro e explícito para garantir compatibilidade futura e estabilidade.

Bean Validation: Blindagem da API contra dados inválidos.

JPA Auditing: Rastreamento automático de criação e atualização (@CreatedDate, @LastModifiedDate).

Documentação: Swagger UI / OpenAPI (Documentação viva).

Testes: JUnit 5 e Mockito.

⚙️ Funcionalidades Implementadas
1. Gestão de Projetos e Membros
   Cadastro completo (CRUD) de Projetos e Membros.

Alocação de Membros em Projetos (Relacionamento N:N).

Auditoria Automática: O sistema registra automaticamente quando um dado foi criado ou alterado.

2. Gestão de Tarefas (Task Management)
   Criação de tarefas vinculadas a projetos.

Atribuição de responsabilidade (Tarefas x Membros).

Controle de Prazos e Status.

3. Regras de Negócio Blindadas 🛡️
   Bloqueio de Conclusão: O sistema impede (Erro 409) que um projeto seja marcado como CONCLUIDO se ainda existirem tarefas pendentes. A integridade dos dados é garantida pelo Backend.

4. Tratamento de Erros Profissional
   Implementação do padrão RFC 7807 (Problem Details) via Global Exception Handler.

Respostas de erro JSON claras, padronizadas e sem "stack trace" exposto ao cliente.

5. Dashboard Gerencial 📊
   Endpoint exclusivo para métricas (KPIs):

Total de Projetos Ativos.

Contagem de Tarefas Pendentes (Gargalos).

Identificação de Membros Ociosos.

🚀 Como Rodar o Projeto
Este projeto utiliza Docker Compose para subir o banco de dados, eliminando a necessidade de instalar o PostgreSQL manualmente na sua máquina.

Pré-requisitos
Java 21 JDK

Docker & Docker Compose

Passo a Passo
Clone o repositório:

Bash
git clone https://github.com/seu-usuario/tecnomanager.git
cd tecnomanager
Suba a infraestrutura (Banco de Dados):

Bash
docker-compose up -d
Isso iniciará o PostgreSQL na porta 5432.

Execute a aplicação:

Bash
./mvnw spring-boot:run
(No Windows, use mvnw.cmd spring-boot:run)

Acesse a Documentação (Swagger): Abra no navegador: http://localhost:8080/swagger-ui.html

🧪 Rodando os Testes
O projeto possui uma estratégia híbrida de testes. Enquanto o ambiente de desenvolvimento usa PostgreSQL (Docker), os testes rodam em H2 Database (Memória) para máxima velocidade.

Bash
./mvnw clean test
📚 Aprendizados e Decisões Técnicas
Migração para Java Puro: Inicialmente o projeto usava Lombok. Decidi remover a dependência para garantir controle total sobre o bytecode gerado e compatibilidade nativa com novas versões do JDK (21+), utilizando Records para DTOs e Getters/Setters explícitos nas Entidades.

Dockerização: A transição de H2 local para PostgreSQL via Docker aproximou o ambiente de desenvolvimento do ambiente real de produção.

State Pattern Simples: A implementação da regra de bloqueio de status demonstrou como encapsular regras de negócio complexas dentro da camada de Serviço, protegendo o domínio.

Desenvolvido por Law 🚀