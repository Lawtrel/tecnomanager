# 🚀 TecnoManager - Sistema de Gestão para Empresas Júnior

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Available-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> **API RESTful** desenvolvida para gerenciamento inteligente de projetos, tarefas e membros, simulando o ecossistema de uma Empresa Júnior. Focada em arquitetura limpa, regras de negócio robustas e deploy automatizado.

---

## 📋 Sobre o Projeto

O **TecnoManager** vai além de um CRUD simples. Ele resolve problemas reais de integridade de dados e gestão de fluxo de trabalho. O sistema impede inconsistências lógicas (como fechar um projeto com pendências) e oferece visão gerencial através de dashboards.

### Principais Diferenciais:
* **Zero Lombok:** Código Java puro e explícito, utilizando **Records** para DTOs e Getters/Setters encapsulados, garantindo compatibilidade futura e clareza.
* **Regras de Negócio Bloqueantes:** O backend atua como guardião da consistência (Pattern *Fail Fast*).
* **Auditoria Automática:** Rastreamento de criação e edição via JPA Auditing.
* **Developer Experience:** Endpoint de **Auto-Seeding** para popular o banco instantaneamente em apresentações.

---

## ⚙️ Funcionalidades

### 1. Gestão Core (Projetos & Membros)
* Cadastro completo de Membros e Projetos.
* Alocação de Membros em Projetos (Relacionamento `N:N`).
* Histórico de datas (criação e atualização) automático.

### 2. Gestão de Tarefas (Tasks)
* Criação de tarefas vinculadas a Projetos.
* Atribuição de responsabilidade (Task ➡️ Membro).
* Controle de Prazos e Status (`PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDO`).

### 3. 🛡️ Regra de Ouro (Business Logic)
O sistema possui uma trava lógica que garante a qualidade da entrega:
> **Regra:** É *impossível* alterar o status de um Projeto para `CONCLUIDO` se houver tarefas pendentes vinculadas a ele. O sistema retorna um erro **409 Conflict** com explicação detalhada.

### 4. 📊 Dashboard Gerencial
Endpoint exclusivo para métricas e KPIs:
* Total de Projetos Ativos.
* Gargalos (Tarefas Pendentes).
* Membros Ociosos (Livres para alocação).

### 5. 🪄 Database Seeding (Botão Mágico)
Endpoint `POST /seed` que limpa e repopula o banco de dados com dados fictícios prontos para demonstração/testes.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21 (LTS)
* **Framework:** Spring Boot 3.4
* **Banco de Dados:** PostgreSQL 16 (Produção/Docker) & H2 (Testes Rápidos)
* **Versionamento de Banco:** Flyway Migrations
* **Containerização:** Docker & Docker Compose
* **Documentação:** Swagger UI (OpenAPI 3)
* **Utils:** Bean Validation, Spring Data JPA

---

## 🚀 Como Rodar (Localmente)

### Pré-requisitos
* Docker & Docker Compose instalados.
* Java 21 (Opcional se rodar via Docker).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/tecnomanager.git](https://github.com/seu-usuario/tecnomanager.git)
    cd tecnomanager
    ```

2.  **Suba o Banco de Dados (Postgres):**
    ```bash
    docker-compose up -d
    ```

3.  **Execute a Aplicação:**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Acesse a Documentação (Swagger):**
    🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## ☁️ Deploy (Produção)

O projeto contém um `Dockerfile` otimizado e está pronto para plataformas como **Railway** ou **Render**.

### Variáveis de Ambiente Necessárias (Cloud)
| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Ativa configurações de prod | `prod` |
| `SPRING_DATASOURCE_URL` | URL JDBC do Postgres | `jdbc:postgresql://host:port/db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do Banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do Banco | `senha123` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | Força o driver correto | `org.postgresql.Driver` |

---

## 🧪 Endpoints Principais

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **POST** | `/seed` | **Popular Banco:** Apaga tudo e cria dados de teste. |
| **GET** | `/dashboard` | **Relatório:** Retorna JSON com estatísticas do sistema. |
| **PATCH** | `/projetos/{id}/status` | **Regra:** Tenta concluir projeto (valida tarefas). |
| **POST** | `/projetos/{id}/tarefas` | Cria uma tarefa num projeto. |

---

## 📂 Estrutura do Projeto

```text
src/main/java/br/lawtrel/tecnomanager
├── config/          # Configurações (Swagger, CORS)
├── controller/      # Camada de API (REST)
├── dto/             # Objetos de Transferência (Java Records)
├── exception/       # Tratamento Global de Erros (RFC 7807)
├── model/           # Entidades JPA (Domínio)
├── repository/      # Acesso a Dados
└── service/         # Regras de Negócio (Onde a mágica acontece)