@echo off
set ROOT=%~dp0..
cd /d "%ROOT%"
echo Subindo stack de producao (Docker Compose)...
docker compose -f docker/compose/docker-compose.prod.yml up -d --build
echo.
echo Gateway:    http://localhost:8080/app
echo Grafana:    http://localhost:3000  (admin/admin)
echo Prometheus: http://localhost:9090
echo Zipkin:     http://localhost:9411
