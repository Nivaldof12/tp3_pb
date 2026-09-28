@echo off
echo ========================================
echo   Infnet - Iniciando todos os servicos
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Zulu\zulu-17
set MVNW=%~dp0..\mvnw.cmd
set ROOT=%~dp0..

echo [0/6] Subindo RabbitMQ (Docker Compose)...
cd /d "%ROOT%"
docker compose up -d
if errorlevel 1 (
    echo AVISO: Docker/RabbitMQ nao iniciou. Verifique Docker Desktop ou suba RabbitMQ manualmente.
)
timeout /t 5 /nobreak >nul

echo [1/6] Instalando modulos Maven (infnet-events no repositorio local)...
"%MVNW%" install -DskipTests -q -pl infnet-events,notificacao-service,infnet-core,api-gateway -am
if errorlevel 1 (
    echo ERRO: falha no mvn install. Verifique JDK 17 e JAVA_HOME.
    exit /b 1
)

echo [2/6] Iniciando Eureka Server (porta 8761)...
start "Eureka Server" cmd /k "%MVNW%" -pl eureka-server spring-boot:run
timeout /t 15 /nobreak >nul

echo [3/6] Iniciando Notificacao Service (porta 8082)...
start "Notificacao Service" cmd /k "%MVNW%" -pl notificacao-service spring-boot:run
timeout /t 15 /nobreak >nul

echo [4/6] Iniciando Monolito - infnet-core (porta 8081)...
start "Infnet Core" cmd /k "%MVNW%" -pl infnet-core spring-boot:run
timeout /t 20 /nobreak >nul

echo [5/6] Iniciando API Gateway (porta 8080)...
start "API Gateway" cmd /k "%MVNW%" -pl api-gateway spring-boot:run

echo.
echo ========================================
echo   Servicos iniciados!
echo   Gateway:      http://localhost:8080
echo   Front-end:    http://localhost:8080/app
echo   Eureka:       http://localhost:8761
echo   RabbitMQ UI:  http://localhost:15672  (guest/guest)
echo ========================================
