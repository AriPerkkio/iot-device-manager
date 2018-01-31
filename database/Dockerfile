# docker build -t mysql-idm .
# docker run --name mysql-idm -d mysql-idm
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/create_database.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_configuration.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device_group.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device_icon.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device_type.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_location.sql"
# docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/mockdata.sql"

FROM mysql/mysql-server

# TODO one-time root password
ENV MYSQL_ROOT_PASSWORD=passu
ENV MYSQL_DATABASE=iotdevicemanager

ADD create_database.sql /home/
ADD mockdata.sql /home/
ADD procedures_configuration.sql /home/
ADD procedures_device_group.sql /home/
ADD procedures_device_icon.sql /home/
ADD procedures_device.sql /home/
ADD procedures_device_type.sql /home/
ADD procedures_location.sql /home/
