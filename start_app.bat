@echo off

REM
docker-compose up -d

REM
timeout /t 15 >nul

REM Führe den Befehl "cron" im database-Container aus
docker exec database cron