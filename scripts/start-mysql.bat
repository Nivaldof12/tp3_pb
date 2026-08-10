@echo off
set MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.4\bin
set MY_INI=C:\ProgramData\MySQL\MySQL Server 8.4\my.ini

tasklist /FI "IMAGENAME eq mysqld.exe" 2>nul | find /I "mysqld.exe" >nul
if %ERRORLEVEL%==0 (
    echo MySQL ja esta em execucao.
    exit /b 0
)

echo Iniciando MySQL Server...
start "" "%MYSQL_BIN%\mysqld.exe" --defaults-file="%MY_INI%"
timeout /t 3 /nobreak >nul
"%MYSQL_BIN%\mysqladmin.exe" -u root -padmin ping
if %ERRORLEVEL%==0 (
    echo MySQL iniciado com sucesso.
) else (
    echo Falha ao iniciar o MySQL.
    exit /b 1
)
