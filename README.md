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
*  **CI:** GitHub Actions (build, execução de testes automatizados e validações em pipeline)
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
* **Auto-cadastro:** Pode criar sua própria conta via rota pública ou criar users comuns.
* **Privacidade:** Acessa e gerencia **apenas** os seus próprios gastos.
* **Segurança:** Não tem permissão para visualizar dados de outros usuários.

---

🐳 Como Rodar (Passo a Passo)
A aplicação é totalmente containerizada. Você não precisa ter Java ou Postgres instalados na sua máquina, apenas o Docker.

1. Clone o repositório(Você precisa ter o git instalado)
```bash
git clone https://github.com/WenderGustavo/gastos-pessoais.git
```
```bash
cd gastos-pessoais
```

Para rodar o projeto, é **obrigatório** configurar as variáveis de ambiente. O projeto utiliza um arquivo `.env` na raiz para facilitar o uso com Docker.

### 2. Crie o arquivo `.env`
Na raiz do projeto, crie um arquivo chamado `.env` e cole o conteúdo abaixo:

```ini
# Configurações do Banco de Dados (Docker)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/gasto
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
JWT_SECRET_KEY=chave_base64_segura_aqui
```

🔐 Como gerar uma JWT_SECRET segura?
Você precisa de uma string codificada em Base64. Você pode gerar executando este comando no terminal (Linux/Mac/Git Bash) (Opcional):
```bash
openssl rand -base64 32
```

Copie o resultado gerado e cole na variável JWT_SECRET dentro do arquivo .env.

3. Suba o ambiente com Docker Compose
Este comando irá baixar as imagens, compilar a aplicação, subir o Banco, o Redis e o Grafana.
```bash
docker-compose up -d --build
```

Esse comando irá:

Buildar a aplicação Spring Boot

Subir PostgreSQL, Redis, Prometheus e Grafana

Executar migrations (Flyway)

Inicializar o sistema

3. População Inicial (Seed) 🌱
Assim que a aplicação sobe pela primeira vez, um Script Seeder executa automaticamente para criar usuários de teste no banco de dados.

Use estas credenciais para testar no Swagger/Postman:

| Perfil | Email       | Senha    |
|--------|-------------|----------|
| Admin  | admin@.com  | 12345678 |
| User   | user@.com   | 12345678 |

## 📖 Guia de Requisições (Swagger & Testes)

A API possui documentação interativa via Swagger (OpenAPI), permitindo testar todos os endpoints diretamente pelo navegador, além de suporte completo para testes via Postman.
1. **Login**  
Faça uma requisição `POST` em `/auth/login` com as credenciais de Admin ou User (tabela acima):

👉 Swagger UI:
http://localhost:8080/swagger-ui.html

🔐 Fluxo de Autenticação (Obrigatório)

Todas as rotas protegidas exigem autenticação via JWT.

1️⃣ Login

Faça uma requisição POST para o endpoint:

POST /auth/login

Request Body
```json
{
  "email": "admin@.com",
  "senha": "12345678"
}
```

2. Pegar o Token: A API retornará um JSON com o token:

Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsIn..."
}

```

2️⃣ Autorização no Swagger

Clique no botão Authorize 🔒 (canto superior direito)

Insira o token no formato:

Bearer SEU_TOKEN_JWT_AQUI

Clique em Authorize e depois em Close

A partir desse momento, todas as rotas protegidas ficarão acessíveis de acordo com o perfil do usuário (ADMIN ou USER).

🧭 Como Usar os Endpoints no Swagger

Escolha um Controller (ex: Gastos Controller)

Selecione o endpoint desejado

Clique em Try it out

Preencha os parâmetros ou o Request Body

Clique em Execute

Analise:

Status HTTP

Response Body

Headers retornados

🧾 Exemplo: Criar um Gasto

Endpoint
POST /gastos

Request Body
{
  "descricao": "Almoço",
  "valor": 35.90,
  "categoria": "ALIMENTACAO",
  "data": "2026-01-10"
}

🔹 USER: cria gasto apenas para si
🔹 ADMIN: pode criar gastos para outros usuários (quando aplicável)

📊 Exemplo: Listar Gastos

Endpoint
GET /gastos

USER: retorna apenas seus próprios gastos
ADMIN: pode acessar gastos globais ou por usuário específico

🚫 Possíveis Erros Comuns
| Status           | Descrição                 |
| ---------------- | ------------------------- |
| 401 Unauthorized | Token ausente ou inválido |
| 403 Forbidden    | Usuário sem permissão     |
| 400 Bad Request  | Dados inválidos           |
| 404 Not Found    | Recurso inexistente       |

📬 Utilizando a API com Postman

Caso prefira usar o Postman, siga os passos abaixo.

🔐 Login no Postman

POST
http://localhost:8080/auth/login

Body (JSON)
{
  "email": "admin@.com",
  "senha": "12345678"
}

Copie o token retornado.

🔑 Autorização no Postman

Em cada requisição protegida:

Aba Authorization

Tipo: Bearer Token

Cole o token

Ou via Header manual:

Authorization: Bearer SEU_TOKEN_JWT

🧾 Exemplo: Criar Gasto via Postman

POST
http://localhost:8080/gastos

Headers
Authorization: Bearer SEU_TOKEN
Content-Type: application/json

Body
{
  "descricao": "Internet",
  "valor": 120.00,
  "categoria": "SERVICOS",
  "data": "2026-01-05"
}

📂 Organização Recomendada no Postman
Gastos Pessoais API
├── Auth
│   └── Login
├── Usuários
│   ├── Criar Usuário
│   └── Listar Usuários (ADMIN)
└── Gastos
    ├── Criar
    ├── Listar
    ├── Atualizar
    └── Remover

💡 Observações Importantes

O acesso aos endpoints respeita RBAC (Role Based Access Control)

Tokens expirados ou inválidos retornam 401

Permissões insuficientes retornam 403

Toda a API é stateless (JWT)

Prometheus: http://localhost:9090 
Grafana: http://localhost:3000 (Login: admin / admin) 
Health Check: http://localhost:8080/actuator/health 

👨‍💻 Autor Desenvolvido por Wender Gustavo.
