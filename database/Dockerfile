# docker build -t mysql-idm .
# docker run --name mysql-idm -d mysql-idm
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/create_database.sql"
FROM mysql/mysql-server

# TODO shared credentials
ENV MYSQL_ROOT_PASSWORD=passu
ENV MYSQL_DATABASE=iotdevicemanager
ENV MYSQL_ROOT_HOST=%

ADD create_database.sql /home/
