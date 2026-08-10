@echo off
echo ========================================
echo   Infnet - Iniciando todos os servicos
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Zulu\zulu-17
set MVNW=%~dp0..\mvnw.cmd

echo [1/4] Iniciando Eureka Server (porta 8761)...
start "Eureka Server" cmd /k "%MVNW%" -pl eureka-server spring-boot:run
timeout /t 15 /nobreak >nul

echo [2/4] Iniciando Notificacao Service (porta 8082)...
start "Notificacao Service" cmd /k "%MVNW%" -pl notificacao-service spring-boot:run
timeout /t 15 /nobreak >nul

echo [3/4] Iniciando Monolito - infnet-core (porta 8081)...
start "Infnet Core" cmd /k "%MVNW%" -pl infnet-core spring-boot:run
timeout /t 20 /nobreak >nul

echo [4/4] Iniciando API Gateway (porta 8080)...
start "API Gateway" cmd /k "%MVNW%" -pl api-gateway spring-boot:run

echo.
echo ========================================
echo   Servicos iniciados!
echo   Gateway:      http://localhost:8080
echo   Front-end:    http://localhost:8080/app
echo   Eureka:       http://localhost:8761
echo ========================================
