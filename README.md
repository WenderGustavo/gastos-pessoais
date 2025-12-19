# 💰 API de Controle de Gastos Pessoais

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" alt="Java"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3-green?style=for-the-badge&logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Redis-Cache-red?style=for-the-badge&logo=redis" alt="Redis"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" alt="Docker"/>
</div>

<br>

## 📋 Sobre o Projeto

O **Gastos Pessoais API** é um serviço backend robusto e escalável desenvolvido para gerenciamento financeiro pessoal. O projeto vai além do CRUD básico, implementando práticas de arquitetura de software modernas, foco em performance e observabilidade.

O objetivo principal foi criar uma API performática utilizando **Cache Distribuído (Redis)** para leituras rápidas, segurança com **JWT**, e monitoramento em tempo real com **Prometheus e Grafana**.

---

## 🚀 Tecnologias e Arquitetura

O projeto foi construído utilizando as seguintes tecnologias:

* **Linguagem:** Java 21+
* **Framework:** Spring Boot 3
* **Banco de Dados:** PostgreSQL 15 (com Migrations via Flyway)
* **Cache:** Redis (Implementação Cache-Aside e Serialização JSON Customizada)
* **Segurança:** Spring Security + JWT (Stateless Authentication)
* **Monitoramento:** Spring Actuator, Prometheus e Grafana
* **Containerização:** Docker e Docker Compose
* **Outros:** Lombok, MapStruct, Hibernate Validator

---

## ✨ Funcionalidades Principais

* **Autenticação e Segurança:** Login, Cadastro e proteção de rotas via Token JWT.
* **Gestão de Gastos:** CRUD completo com validações de negócio.
* **Alta Performance:**
    * Cache de leitura (`@Cacheable`) para listagens frequentes.
    * Invalidação inteligente de cache (`@CacheEvict`) em atualizações.
    * Serialização JSON customizada no Redis para suportar Java Records e Datas (Java 8 Time).
* **Consultas Otimizadas:**
    * Uso de **Projections (DTOs)** para leituras rápidas.
    * **Índices de Banco de Dados** para filtros por data e usuário.
* **Observabilidade:** Exposição de métricas para monitoramento de CPU, Memória e Connection Pool.

---

## 🐳 Como Rodar (Via Docker)

A maneira mais fácil de rodar a aplicação é utilizando o Docker Compose, que sobe o Banco, o Redis e a Aplicação automaticamente.

### Pré-requisitos
* Docker e Docker Compose instalados.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/WenderGustavo/gastospessoais.git](https://github.com/WenderGustavo/gastospessoais.git)
    cd gastospessoais
    ```

2.  **Configure as Variáveis de Ambiente:**
    Crie um arquivo `.env` na raiz (ou altere o `docker-compose.yml` se preferir) com suas credenciais.
    *(O projeto já possui configurações padrão para ambiente de desenvolvimento)*.

3.  **Suba os containers:**
    ```bash
    docker-compose up -d --build
    ```

4.  **Acesse a Aplicação:**
    * **API:** `http://localhost:8080`
    * **Swagger UI (Doc):** `http://localhost:8080/swagger-ui.html` (Se configurado)
    * **Métricas (Prometheus):** `http://localhost:8080/actuator/prometheus`

---

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura em camadas focada em separação de responsabilidades:

```text
src/main/java/io/github/wendergustavo/gastospessoais
├── 📁 configuration  # Configurações (Cache, Security, Swagger, Jackson)
├── 📁 controller     # Camada REST (Entrada de dados)
├── 📁 service        # Regras de Negócio e Cache
├── 📁 repository     # Acesso a Dados (Spring Data JPA)
├── 📁 Validator      # Validação das regras de negocio
├── 📁 dto            # Objetos de Transferência (Request/Response/Projections)
├── 📁 exception      # Tratamento global de erros (ControllerAdvice)
└── 📁 security       # Filtros e Configuração JWT
