# Webapplication for managing companys assets
The Webapplication contains a backend and a frontend (GitHub-Repository for the frontend: https://github.com/xtramarcf/frontend_assets).

## Installation Guide
1. Docker Engine needs to be installed on the host-system.
2. Download the docker-compose-file (https://github.com/xtramarcf/backend_assets/blob/master/docker/docker-compose.yaml).
3. Replace the POSTGRES_PASSWORD and the SPRING_DATASOURCE_PASSWORD with your own safe password.
4. Set a new Admin Password. You can log in to the Admin-Account with the user "Admin" and your set password.
5. Change the volume-directory at services.database.volumes to the directory, where the backups will be stored. Leave the part ":/var/backups/" untouched.
6. Run the start_app.bat command für Windows. For Linux, you can run "docker-compose up -d" and then, after 15 seconds, run "docker exec database cron".
7. The application is now accessible under "localhost:4200".

## Contact
For further questions, do not hesitate to contact me under marc.fortmeier@iu-study.org.
