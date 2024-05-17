# Webapplication for managing companys assets

The Webapplication contains a backend and a frontend (GitHub-Repository for the
frontend: https://github.com/xtramarcf/frontend_assets).

## Installation Guide

1. Docker Engine needs to be installed on the host-system.
2. Download the docker-compose-file (https://github.com/xtramarcf/backend_assets/blob/master/docker-compose.yaml)
   and the startup-file (https://github.com/xtramarcf/backend_assets/blob/master/start_app.bat) and put them into one
   folder.
3. Replace the POSTGRES_PASSWORD and the SPRING_DATASOURCE_PASSWORD with your own safe password.
4. Set a new Admin Password. You can log in to the Admin-Account with the user "Admin" and your set password.
5. Change the volume-directory at services.database.volumes to the directory, where the backups will be stored. Leave
   the part ":/var/backups/" untouched.
6. Run the start_app.bat for Windows. For Linux, you can navigate to the folder with the two files and run "
   docker-compose up -d" and then, after 30 seconds, run "
   docker exec database cron".
7. The application is now accessible under "localhost:4200".

## Documentation

The code contains javadoc comments and uses speaking variable and method names. An overview of the application is
represented
by an uml-class-diagram for the function-block 'assets' and for the '
iam' (https://github.com/xtramarcf/backend_assets/tree/master/uml).

## Contact

For further questions, do not hesitate to contact me under marc.fortmeier@iu-study.org.
