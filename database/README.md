### Database

#### Instructions
- Install Docker (https://docs.docker.com/install/)
- cd iot-device-manager/database/
- docker build -t mysql-idm . --build-arg CLIENT_PASS=<add-client-pass>
- docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=<add-root-password> -d mysql-idm
- (Optional - Verify container starts up properly before moving on) watch docker ps
##### Example: 
```bash
$ docker build -t mysql-idm . --build-arg CLIENT_PASS=my-db-secret && \
     docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=my-root-secret -d mysql-idm && \
     watch docker ps
```