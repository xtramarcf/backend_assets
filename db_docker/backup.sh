#!/bin/bash

current_time=$(date "+%Y-%m-%d %H:%M:%S")
echo "[current_time]: Backup-Skript gestartet" >> /var/log/backup.log
/usr/bin/pg_dump -U postgres assetmanagement > /var/backups/backup.sql