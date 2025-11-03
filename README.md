# Gastos Pessoais

## Descrição

O projeto **Gastos Pessoais** é uma aplicação backend construída em **Java** com **Spring Boot** que permite gerenciar usuários e seus gastos pessoais.  
Ele fornece operações de **CRUD** para usuários e gastos, validações de dados e segurança básica, além de testes de integração e unitários para garantir qualidade.

O objetivo do projeto é treinar boas práticas de desenvolvimento, como **MVC**, **Clean Code**, **Testes Automatizados**, **Spring Data JPA**, e **versionamento com Git**.

---

## Funcionalidades

- CRUD de **Usuários**
- CRUD de **Gastos** associados a usuários
- Validações de campos (ex.: valor do gasto positivo, usuário existente, senha válida)
- Relacionamento **Usuário → Gasto**
- Testes automatizados:
    - **Unitários** (Validador, Service)
    - **Integração** (Repository com H2)
- Scripts SQL para popular o banco de teste (Pode utilizar caso deseje)

---

## Tecnologias e Ferramentas

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Test / JUnit 5**
- **Mockito** (mock de validações e serviços)
- **H2 Database** (banco em memória para testes)
- **Lombok**
- **Maven**
- **Git** (controle de versão)

---

## Estrutura do Projeto

src/
├─ main/
│ ├─ java/
│ │ └─ io.github.wendergustavo.gastospessoais/
│ │ ├─ configuration/ # Configurações do Spring (Beans, Security, etc.)
│ │ ├─ controller/ # Endpoints REST e Controllers
│ │ ├─ dto/ # Data Transfer Objects para requisições/respostas
│ │ ├─ entity/ # Entidades JPA (Usuario, Gasto, etc.)
│ │ ├─ exceptions/ # Exceções customizadas do projeto
│ │ ├─ mapper/ # Conversão entre Entity e DTO
│ │ ├─ repository/ # Interfaces de acesso a dados (JPA)
│ │ ├─ service/ # Lógica de negócio e transações
│ │ └─ validador/ # Regras de validação (UsuarioValidator, GastoValidator)
│ └─ resources/
│ └─ application.yml # Configurações do Spring
└─ test/
├─ java/
│ └─ io.github.wendergustavo.gastospessoais/
│ ├─ entity/ # Testes de getters e setters
│ ├─ repository/ # Testes de integração dos repositories
│ └─ service/ # Testes unitários e de integração dos services
└─ resources/
└─ sql/ # Scripts SQL para popular o banco de teste
