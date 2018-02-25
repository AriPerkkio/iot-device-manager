### Database

#### Instructions
- Install Docker
- cd iot-device-manager/database/
- docker build -t mysql-idm . --build-arg CLIENT_PASS=<add-client-pass>
- docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=<add-root-password> -d mysql-idm