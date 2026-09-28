# 💰 Projeto Infnet — Gestão Financeira (5ª Entrega)

Sistema multi-módulo **Spring Boot 2.7** + **Spring Cloud**: persistência, microsserviços, **eventos (RabbitMQ)** e **implantação em produção** (Docker, Kubernetes, monitoramento, CI/CD).

---

## 📦 Módulos Maven

| Módulo | Descrição | Porta |
|--------|-----------|-------|
| `infnet-events` | Eventos + topologia RabbitMQ | — |
| `eureka-server` | Service discovery | 8761 |
| `infnet-core` | Usuários, financeiros, Envers, `/app`, publicador de eventos | 8081 |
| `notificacao-service` | Notificações + consumidor RabbitMQ | 8082 |
| `api-gateway` | Spring Cloud Gateway | 8080 |
| `system-tests` | Smoke tests ponta a ponta (stack em execução) | — |

---

## 🚀 Desenvolvimento local (sem Docker nos apps)

1. RabbitMQ: `docker compose up -d` (raiz)  
2. MySQL: `scripts\start-mysql.bat`  
3. Apps Java: `scripts\start-all.bat`  
4. Testes: `mvnw.cmd test`

---

## 🐳 Produção local — Docker Compose (5ª entrega)

Stack completa (MySQL, RabbitMQ, apps, Zipkin, Prometheus, Grafana, Loki):

```bash
docker compose -f docker/compose/docker-compose.prod.yml up -d --build
```

Ou no Windows: `scripts\docker-prod-up.bat`

| URL | Uso |
|-----|-----|
| http://localhost:8080/app | Front-end |
| http://localhost:3000 | Grafana (admin/admin) |
| http://localhost:9090 | Prometheus |
| http://localhost:9411 | Zipkin (tracing) |
| http://localhost:15672 | RabbitMQ (guest/guest) |

Documentação detalhada: **[docs/IMPLANTACAO-PRODUCAO.md](docs/IMPLANTACAO-PRODUCAO.md)**  
Eventos (4ª entrega): **[docs/ARQUITETURA-EVENTOS.md](docs/ARQUITETURA-EVENTOS.md)**

---

## ☸️ Kubernetes

```bash
kubectl apply -f k8s/
```

Build e carga de imagens: ver seção Kubernetes em `docs/IMPLANTACAO-PRODUCAO.md`.

---

## 🔄 CI/CD

GitHub Actions (`.github/workflows/ci-cd.yml`): testes Maven, build Docker, smoke test com Compose, validação dos manifests K8s.

---

## 🧪 Testes

- **Unitários:** `mvnw test` em todos os módulos  
- **Smoke (stack no ar):** `mvnw -pl system-tests verify -Dgroups=integration`

---

## 📚 Entregas anteriores

- **2ª:** JPA, Envers  
- **3ª:** Eureka, Gateway, Feign, Thymeleaf  
- **4ª:** RabbitMQ, arquitetura orientada a eventos  
- **5ª:** Docker, K8s, Prometheus/Grafana/Zipkin/Loki, GitHub Actions  

Projeto acadêmico Infnet.
