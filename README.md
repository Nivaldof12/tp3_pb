# 💰 Projeto Infnet — Gestão Financeira Pessoal

> **Disciplina:** Engenharia de Software Escaláveis — 3ª Entrega  
> **Aluno:** Nivaldo Filho
> 
> **Objetivo:** Expandir a aplicação monolítica com um microsserviço usando Spring Boot e Spring Cloud.

---

## 📌 O que é este projeto?

Este projeto é um sistema de **controle financeiro pessoal** que evoluiu de uma aplicação monolítica para uma **arquitetura de microsserviços**.

Com ele, é possível:

- 👤 Cadastrar usuários
- 💵 Registrar receitas e despesas
- 📜 Consultar histórico de alterações (Hibernate Envers)
- 🔔 Receber **notificações automáticas** ao registrar lançamentos (microsserviço)
- 🖥️ Visualizar tudo em uma **interface web** (Thymeleaf)

---

## 🏗️ Arquitetura do Sistema

```
                    ┌─────────────────────┐
                    │   API Gateway       │
                    │   (porta 8080)      │
                    └─────────┬───────────┘
                              │
              ┌───────────────┼───────────────┐
              ▼                               ▼
   ┌─────────────────────┐       ┌─────────────────────────┐
   │   infnet-core       │       │  notificacao-service    │
   │   (Monolito)        │       │  (Microsserviço)        │
   │   porta 8081        │       │  porta 8082             │
   │                     │       │                         │
   │ • Usuários          │ Feign │ • Notificações          │
   │ • Financeiros       │──────▶│ • Alertas de despesa    │
   │ • Histórico         │       │ • Avisos de receita     │
   │ • Front-end /app    │       │                         │
   └─────────┬───────────┘       └────────────┬────────────┘
             │                                │
             ▼                                ▼
   ┌─────────────────┐             ┌─────────────────────┐
   │ MySQL PBInfnet  │             │ MySQL PBInfnetNotif.│
   └─────────────────┘             └─────────────────────┘

              ┌─────────────────────┐
              │   Eureka Server     │
              │   (porta 8761)      │
              │   Service Discovery │
              └─────────────────────┘
```

### 📦 Módulos do Projeto

| Módulo | Porta | Responsabilidade |
|---|---|---|
| `eureka-server` | 8761 | Descoberta de serviços (Netflix Eureka) |
| `api-gateway` | 8080 | Ponto único de entrada (Spring Cloud Gateway) |
| `infnet-core` | 8081 | Monolito: usuários, financeiros, histórico, UI |
| `notificacao-service` | 8082 | Microsserviço de notificações financeiras |

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Para que serve? |
|---|---|
| ☕ **Java 17** | Linguagem principal |
| 🍃 **Spring Boot 2.7.6** | Framework base de cada serviço |
| ☁️ **Spring Cloud 2021.0.8** | Comunicação distribuída entre serviços |
| 🔍 **Netflix Eureka** | Registro e descoberta de serviços |
| 🚪 **Spring Cloud Gateway** | Roteamento centralizado da API |
| 🔗 **OpenFeign** | Comunicação HTTP entre monolito e microsserviço |
| 🗄️ **Spring Data JPA** | Persistência de dados |
| 📚 **Hibernate Envers** | Histórico/auditoria de alterações |
| 🎨 **Thymeleaf** | Interface web (front-end) |
| 🐬 **MySQL** | Banco de dados em produção |
| 🧪 **H2** | Banco em memória para testes |

---

## 🚀 Como rodar o projeto

### 1️⃣ Pré-requisitos

- ✅ JDK 17
- ✅ MySQL rodando (usuário `root`, senha `admin`)
- ✅ Maven (ou use o `mvnw` incluso)

### 2️⃣ Iniciar todos os serviços de uma vez

```bash
scripts\start-all.bat
```

Esse script inicia na ordem: **Eureka → Notificação → Monolito → Gateway**

### 3️⃣ Ou iniciar manualmente (4 terminais)

```bash
# Terminal 1 - Eureka
.\mvnw.cmd -pl eureka-server spring-boot:run

# Terminal 2 - Microsserviço
.\mvnw.cmd -pl notificacao-service spring-boot:run

# Terminal 3 - Monolito
.\mvnw.cmd -pl infnet-core spring-boot:run

# Terminal 4 - Gateway
.\mvnw.cmd -pl api-gateway spring-boot:run
```

### 4️⃣ Acessar a aplicação

| Recurso | URL |
|---|---|
| 🖥️ **Front-end** | http://localhost:8080/app |
| 📋 **Eureka Dashboard** | http://localhost:8761 |
| 👤 **API Usuários** | http://localhost:8080/usuarios |
| 💸 **API Financeiros** | http://localhost:8080/financeiros |
| 🔔 **API Notificações** | http://localhost:8080/notificacoes |

### 5️⃣ Rodar os testes

```bash
.\mvnw.cmd test
```

Resultado esperado: **23 testes passando** ✅

---

## 🔔 Microsserviço de Notificações

### Por que foi criado?

Quando um lançamento financeiro é registrado no monolito, o usuário deve ser **notificado automaticamente**. Essa responsabilidade foi separada em um microsserviço independente, seguindo o princípio de **separação de responsabilidades**.

### Como funciona a comunicação?

```
1. Cliente POST /financeiros  →  Gateway  →  infnet-core
2. FinanceiroService salva no banco
3. FinanceiroService chama NotificacaoClient (OpenFeign)
4. Feign descobre notificacao-service via Eureka
5. Microsserviço cria a notificação no banco dele
6. Front-end exibe notificações via AppController
```

### Modelo de domínio — `Notificacao`

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador |
| `usuarioId` | Long | ID do usuário (referência ao monolito) |
| `titulo` | String | Título da notificação |
| `mensagem` | String | Texto descritivo |
| `tipo` | Enum | `ALERTA`, `INFO` ou `SUCESSO` |
| `lida` | boolean | Se foi lida ou não |
| `dataCriacao` | LocalDateTime | Data de criação |

### Regras de negócio

| Evento | Tipo de notificação |
|---|---|
| Despesa ≥ R$ 1.000 | 🔴 `ALERTA` |
| Despesa < R$ 1.000 | 🔵 `INFO` |
| Receita registrada | 🟢 `SUCESSO` |

### Endpoints do microsserviço

| Método | URL | Descrição |
|---|---|---|
| `POST` | `/notificacoes` | Criar notificação |
| `GET` | `/notificacoes/usuario/{id}` | Listar por usuário |
| `GET` | `/notificacoes/usuario/{id}/nao-lidas` | Listar não lidas |
| `GET` | `/notificacoes/usuario/{id}/contagem` | Contar não lidas |
| `PATCH` | `/notificacoes/{id}/lida` | Marcar como lida |
| `DELETE` | `/notificacoes/{id}` | Excluir |

**Exemplo — criar notificação:**

```http
POST http://localhost:8080/notificacoes
Content-Type: application/json

{
  "usuarioId": 1,
  "titulo": "Nova despesa registrada",
  "mensagem": "Despesa de R$ 1500.00 em Aluguel foi registrada.",
  "tipo": "ALERTA"
}
```

---

## 🖥️ Front-end

A interface web fica no monolito (`infnet-core`) e consome o microsserviço via **OpenFeign**.

| Página | URL | O que mostra? |
|---|---|---|
| Início | `/app` | Lista de usuários cadastrados |
| Detalhe do usuário | `/app/usuarios/{id}` | Lançamentos + notificações recentes |
| Notificações | `/app/notificacoes/{id}` | Todas as notificações do microsserviço |

---

## 🗃️ Modelo de Dados

### 👤 Usuario (`TUsuario`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador |
| `nome` | String | Nome do usuário |
| `email` | String | E-mail único |
| `senha` | String | Senha |
| `dataCriacao` | LocalDateTime | Preenchida automaticamente |
| `dataAtualizacao` | LocalDateTime | Atualizada automaticamente |

### 💸 Financeiro (`TFinanceiro`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador |
| `usuario` | Usuario | Dono do lançamento |
| `tipo` | Enum | `RECEITA` ou `DESPESA` |
| `categoria` | String | Categoria |
| `descricao` | String | Descrição |
| `valor` | BigDecimal | Valor |
| `data` | LocalDate | Data do lançamento |

---

## 📜 Histórico de Dados (Envers)

As entidades usam `@Audited` para rastrear alterações:

| Endpoint | Descrição |
|---|---|
| `GET /historico/usuarios/{id}` | Histórico de um usuário |
| `GET /historico/financeiros/{id}` | Histórico de um lançamento |

---

## 🌐 API REST

### 👤 Usuários — `/usuarios`

| Método | URL | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Criar |
| `GET` | `/usuarios` | Listar todos |
| `GET` | `/usuarios/{id}` | Buscar por ID |
| `PUT` | `/usuarios/{id}` | Atualizar |
| `DELETE` | `/usuarios/{id}` | Excluir |

### 💸 Financeiros — `/financeiros`

| Método | URL | Descrição |
|---|---|---|
| `POST` | `/financeiros` | Criar (dispara notificação) |
| `GET` | `/financeiros` | Listar todos |
| `GET` | `/financeiros/usuario/{id}` | Listar por usuário |
| `PUT` | `/financeiros/{id}` | Atualizar |
| `DELETE` | `/financeiros/{id}` | Excluir |

---

## 🧪 Testes Automatizados

| Módulo | Classe | Testes |
|---|---|---|
| `infnet-core` | `UsuarioRepositoryTest` | 6 |
| `infnet-core` | `FinanceiroRepositoryTest` | 6 |
| `infnet-core` | `HistoricoServiceTest` | 5 |
| `infnet-core` | `InfnetApplicationTests` | 1 |
| `notificacao-service` | `NotificacaoRepositoryTest` | 4 |
| `notificacao-service` | `NotificacaoServiceApplicationTests` | 1 |
| **Total** | | **23** |

---

<p align="center">
  <b>Projeto Infnet — 2026</b><br>
  Desenvolvido com ☕ Java + 🍃 Spring Boot + ☁️ Spring Cloud
</p>
