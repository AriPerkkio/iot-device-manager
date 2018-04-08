# Iot Device Manager (school project)

## Build requirements:

### Option 1:

#### 1. Database
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

#### 2. Server
- Install Docker (https://docs.docker.com/install/)
- cd iot-device-manager/
- docker build -t iotdevicemanager .
- docker run -dit --name iotdevicemanager -e APP_PASS=<add-api/ui-pass> -e DB_PASS=<add-db-pass> -p 80:8080 --link mysql-idm iotdevicemanager
- (Optional - Follows process from logs) docker logs -f iotdevicemanager
##### Example: 
```bash
$ docker build -t iotdevicemanager . && \
     docker run -dit --name iotdevicemanager \
         -e APP_PASS=my-api-secret \
         -e DB_PASS=my-db-secret \
         -p 80:8080 \
         --link mysql-idm iotdevicemanager && \
     docker logs -f iotdevicemanager
```
### Option 2:
- Check Dockerfiles for requirements and steps

## Tech
- Spring boot
- Mysql
- React, Redux, Router, Webpack, etc
- Robot Framework
  - Database tests
  - Probably some UI + Integration tests
- Docker
- Deployment in AWS

## Documentation
- http://ec2-34-207-126-204.compute-1.amazonaws.com/api-doc/index.html
- database/schema-draw-io.xml
- database/schema.png <b>TODO</b>
- doc/iot-device-manager.raml

