# 💰 Projeto Infnet — Gestão Financeira (4ª Entrega)

Sistema multi-módulo em **Spring Boot 2.7** + **Spring Cloud**, evoluindo de monolito com persistência (2ª entrega), microsserviços (3ª entrega) para **arquitetura orientada a eventos com RabbitMQ** (4ª entrega).

---

## 📦 Módulos Maven

| Módulo | Descrição | Porta |
|--------|-----------|-------|
| `infnet-events` | Contratos de eventos + topologia RabbitMQ compartilhada | — |
| `eureka-server` | Service discovery | 8761 |
| `infnet-core` | Usuários, financeiros, histórico Envers, painel `/app`, **publicador de eventos** | 8081 |
| `notificacao-service` | Notificações + **consumidor RabbitMQ** | 8082 |
| `api-gateway` | Spring Cloud Gateway | 8080 |

---

## 🚀 Como rodar

### Pré-requisitos

- **JDK 17** (ex.: Azul Zulu)
- **MySQL** — bancos `PBInfnet` e `PBInfnetNotificacao` (usuário/senha configurados em `application.properties`)
- **Docker** (para RabbitMQ) ou RabbitMQ local na porta **5672**

### 1. RabbitMQ

```bash
docker compose up -d
```

- AMQP: `localhost:5672`
- Console: http://localhost:15672 (`guest` / `guest`)

### 2. MySQL

Use o script `scripts/start-mysql.bat` se o serviço não estiver instalado como Windows Service.

### 3. Testes

```bash
set JAVA_HOME=C:\Program Files\Zulu\zulu-17
mvnw.cmd test
```

### 4. Subir todos os serviços

```bash
scripts\start-all.bat
```

| URL | Uso |
|-----|-----|
| http://localhost:8080/app | Front-end Thymeleaf |
| http://localhost:8080/usuarios | API usuários |
| http://localhost:8080/financeiros | API financeiros |
| http://localhost:8080/notificacoes | API notificações |
| http://localhost:8761 | Eureka |

---

## 🔔 4ª entrega — Eventos (resumo)

- **Escrita desacoplada:** ao salvar financeiro/usuário, o core publica eventos no exchange **`infnet.events.topic`**.
- **Notificações assíncronas:** o `notificacao-service` consome **`notificacao.financeiro.queue`** e persiste a notificação (regras: ALERTA para despesa ≥ R$ 1000, etc.).
- **Feign:** mantido apenas para **listar** notificações no painel web (leitura síncrona).
- **Documentação detalhada:** [docs/ARQUITETURA-EVENTOS.md](docs/ARQUITETURA-EVENTOS.md) — prós/contras, padrões de mensagem, diagramas Mermaid.

---

## 🧪 Testes automatizados

- Persistência e Envers (`infnet-core`)
- Repositório e serviço de notificações (`notificacao-service`)
- `EventPublisherTest` — verifica envio ao `RabbitTemplate`
- `NotificacaoFromFinanceiroProcessorTest` — regras de negócio a partir do evento

---

## 👨‍🎓 Entregas anteriores (ainda presentes no código)

- **2ª:** JPA, Spring Data, Hibernate Envers (`/historico`)
- **3ª:** Eureka, Gateway, Feign, microsserviço de notificações, Thymeleaf

---

## 📄 Licença / uso acadêmico

Projeto desenvolvido para disciplina Infnet — entregas incrementais documentadas neste repositório.
