# 💰 API de Controle de Gastos Pessoais

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" alt="Java"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3-green?style=for-the-badge&logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Redis-Cache-red?style=for-the-badge&logo=redis" alt="Redis"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" alt="Docker"/>
  <img src="https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway" alt="Flyway"/>
</div>

<br>

## 📋 Sobre o Projeto

O **Gastos Pessoais API** é um sistema backend de alta performance desenvolvido para gestão financeira. O projeto foi arquitetado para resolver problemas reais de escalabilidade, utilizando **Cache Distribuído (Redis)** para reduzir a latência de leitura e **PostgreSQL** para persistência segura.

O sistema implementa autenticação robusta via **JWT (JSON Web Token)** e diferenciação de níveis de acesso (RBAC - Role Based Access Control) entre Administradores e Usuários comuns. Além disso, conta com um setup completo de observabilidade com **Prometheus e Grafana**.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3
* **Banco de Dados:** PostgreSQL 15
* **Cache:** Redis (Estratégia Cache-Aside com Serialização Jackson Customizada)
* **Gerenciamento de Dados:** Flyway (Migrations)
* **Segurança:** Spring Security + JWT Stateless
* **Containerização:** Docker & Docker Compose
* **Observabilidade:** Spring Actuator, Prometheus e Grafana
* **Documentação:** Swagger (OpenAPI)

---

## ✨ Funcionalidades e Perfis

O sistema possui controle de acesso rigoroso dividido em dois perfis:

### 👑 ADMIN (Administrador)
* **Gestão de Usuários:** Pode criar novos Administradores e gerenciar qualquer usuário.
* **Visão Global:** Pode listar gastos de qualquer usuário para fins de auditoria.
* **Gestão de Gastos:** Pode criar, editar ou remover gastos em nome de outros usuários.

### 👤 USER (Usuário Comum)
* **Auto-cadastro:** Pode criar sua própria conta via rota pública.
* **Privacidade:** Acessa e gerencia **apenas** os seus próprios gastos.
* **Segurança:** Não tem permissão para visualizar dados de outros usuários.

---

## ⚙️ Configuração e Variáveis de Ambiente

Para rodar o projeto, é **obrigatório** configurar as variáveis de ambiente. O projeto utiliza um arquivo `.env` na raiz para facilitar o uso com Docker.

### 1. Crie o arquivo `.env`
Na raiz do projeto, crie um arquivo chamado `.env` e cole o conteúdo abaixo:

```ini
# Configurações do Banco de Dados (Docker)
POSTGRES_DB=gasto
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Conexão da Aplicação (Dentro do Container)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/gasto
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Configuração do Redis
SPRING_DATA_REDIS_HOST=redis
SPRING_DATA_REDIS_PORT=6379

# Segurança JWT (JSON Web Token)
# IMPORTANTE: Gere uma chave segura (veja instrução abaixo)
JWT_SECRET=sua_chave_secreta_super_segura_base64_aqui
JWT_EXPIRATION=86400000
```

🔐 Como gerar uma JWT_SECRET segura?
Você precisa de uma string codificada em Base64. Você pode gerar executando este comando no terminal (Linux/Mac/Git Bash):
```bash
openssl rand -base64 32
```

Copie o resultado gerado e cole na variável JWT_SECRET dentro do arquivo .env.

🐳 Como Rodar (Passo a Passo)
A aplicação é totalmente containerizada. Você não precisa ter Java ou Postgres instalados na sua máquina, apenas o Docker.

1. Clone o repositório
```bash
git clone https://github.com/WenderGustavo/gastospessoais.git
cd gastospessoais
```

2. Suba o ambiente com Docker Compose
Este comando irá baixar as imagens, compilar a aplicação, subir o Banco, o Redis e o Grafana.
```bash
docker-compose up -d --build
```

3. População Inicial (Seed) 🌱
Assim que a aplicação sobe pela primeira vez, um Script Seeder executa automaticamente para criar usuários de teste no banco de dados.

Use estas credenciais para testar no Swagger/Postman:

| Perfil | Email       | Senha    |
|--------|-------------|----------|
| Admin  | admin@.com  | 12345678 |
| User   | user@.com   | 12345678 |


## 📖 Guia de Requisições (Swagger & Testes)

A documentação interativa da API está disponível em:  
👉 http://localhost:8080/swagger-ui.html

### 🔐 Fluxo de Autenticação

1. **Login**  
Faça uma requisição `POST` em `/auth/login` com as credenciais de Admin ou User (tabela acima):

```json
{
  "email": "admin@.com",
  "senha": "12345678"
}
```

2. Pegar o Token: A API retornará um JSON com o token:
 
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsIn..."
}

```
3. Autorizar: No Swagger, clique no botão Authorize (cadeado) no  lado superior direito e insira o token no formato: Bearer eyJhbGciOiJIUzI1NiIsIn....

📂 Estrutura do Projeto
O projeto segue uma arquitetura em camadas (Layered Architecture) com forte influência de Clean Code e SOLID.
```
src/main/java/io/github/wendergustavo/gastospessoais
├── configuration   # Configs de Beans
├── controller      # Camada REST
├── service         # Regras de Negócio
├── repository      # Persistência
├── model           # Entidades JPA
├── dto             # DTOs
├── mapper          # MapStruct
├── validator       # Validações
├── security        # JWT e Acesso
└── exception       # Handler global
```

Prometheus: http://localhost:9090

Grafana: http://localhost:3000 (Login: admin / admin)

Health Check: http://localhost:8080/actuator/health

👨‍💻 Autor
Desenvolvido por Wender Gustavo.
