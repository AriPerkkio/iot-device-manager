# docker build -t mysql-idm .
# docker run --name mysql-idm -d mysql-idm
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/create_database.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/mockdata.sql"
FROM mysql/mysql-server

# TODO one-time root password
ENV MYSQL_ROOT_PASSWORD=passu
ENV MYSQL_DATABASE=iotdevicemanager

ADD create_database.sql /home/
ADD mockdata.sql /home/
