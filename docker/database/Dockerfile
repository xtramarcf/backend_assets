# Uses the postgres image
FROM postgres:15.2

# install nano editor and cron
RUN apt-get update && apt-get install -y nano
RUN apt-get -y install cron

# add the back backup.sh file to the container
ADD backup.sh /root/backup.sh

# give the backup.sh file executing permissions
RUN chmod +x /root/backup.sh

# add a cronjob to the crontab file, that executes the backup.sh file every day at 3 AM
RUN crontab -l | { cat; echo "0 3 * * * bash /root/backup.sh"; } | crontab -






