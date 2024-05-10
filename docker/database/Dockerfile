FROM postgres:15.2

RUN apt-get update && apt-get install -y nano
RUN apt-get -y install cron

ADD backup.sh /root/backup.sh

RUN chmod +x /root/backup.sh

RUN crontab -l | { cat; echo "0 3 * * * bash /root/backup.sh"; } | crontab -






