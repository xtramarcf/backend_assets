#!/bin/bash

# creates a backup of the postgres-db and safes it as backup.sql
/usr/bin/pg_dump -U postgres assetmanagement > /var/backups/backup.sql