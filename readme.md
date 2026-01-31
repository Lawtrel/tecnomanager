# TecnoManager - Sistema de Gestão para Empresa Júnior

API REST desenvolvida para gerenciamento de membros e projetos, simulando o ambiente de uma Empresa Júnior.

## 🚀 Tecnologias Utilizadas
* **Java 21** & **Spring Boot 3.4**
* **Spring Data JPA** (Hibernate)
* **H2 Database** (Banco em memória para prototipagem rápida)
* **Bean Validation** (Regras de negócio e blindagem da API)
* **Java Records** (Para DTOs imutáveis e código limpo)
* **Swagger / OpenAPI** (Documentação viva da API)

## ⚙️ Funcionalidades
* Cadastro de Membros e Projetos.
* Alocação de Membros em Projetos (Relacionamento N:N).
* Validação de dados de entrada (impedir nulos ou formatos inválidos).
* Documentação interativa via Swagger UI.

## 🛠️ Como Rodar
1.  Clone o repositório.
2.  Abra na sua IDE de preferência (IntelliJ/Eclipse/VS Code).
3.  Execute a classe `TecnomanagerApplication`.
4.  Acesse o Swagger: `http://localhost:8080/swagger-ui/index.html`

## 📚 Aprendizados
Este projeto foi criado para consolidar conhecimentos em arquitetura REST moderna, focando no uso de Records do Java 17+ e boas práticas de DTO pattern para não expor as entidades JPA diretamente.