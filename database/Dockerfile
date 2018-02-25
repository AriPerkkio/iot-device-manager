# docker build -t mysql-idm . --build-arg CLIENT_PASS=<add-client-pass>
# docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=<add-root-password> -d mysql-idm

FROM mysql/mysql-server

ARG CLIENT_PASS=${CLIENT_PASS}
ENV CLIENT_PASS=${CLIENT_PASS}

ADD create_databases.sql /home/

ADD create_tables_triggers_user.sql /home/
ADD procedures_configuration.sql /home/
ADD procedures_device_group.sql /home/
ADD procedures_device_icon.sql /home/
ADD procedures_device.sql /home/
ADD procedures_device_type.sql /home/
ADD procedures_measurement.sql /home/
ADD procedures_location.sql /home/

ADD setup.sql /docker-entrypoint-initdb.d

# Replace client password:
RUN ["sh", "-c", "sed -i -- 's/<mysql-user-password>/'$CLIENT_PASS'/g' /home/create_tables_triggers_user.sql"]