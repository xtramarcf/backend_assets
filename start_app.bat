@echo off

REM starts docker-compose as a background task
docker-compose up -d

REM waits 15 seconds until next command
timeout /t 30 >nul

REM executes the "cron"-command for starting the backup-cronjob at the database container
docker exec database cron