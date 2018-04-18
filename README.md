# Iot Device Manager (school project)

## Build requirements:

### Option 1:

#### 1. Database
Build arguments:
- \<add-client-pass> = Password for mysql user. This user has only access to 27 procedures.
- \<add-root-password> = Password for mysql root user. Base image requires changing of root user password. 

Steps:
- Install Docker (https://docs.docker.com/install/)
- cd iot-device-manager/database/
- docker build -t mysql-idm . --build-arg CLIENT_PASS=<add-client-pass>
- docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=<add-root-password> -d mysql-idm
- (Optional - Verify container starts up properly before moving on) watch docker ps
##### Example: 
```bash
# cd to database directory
$ cd iot-device-manager/database

# Build image, create and run container, check container status
$ docker build -t mysql-idm . --build-arg CLIENT_PASS=my-db-secret && \
     docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=my-root-secret -d mysql-idm && \
     watch docker ps
```

#### 2. Server
Build arguments:
- \<add-api/ui-pass> = Password for API and UI
- \<add-db-pass> = Your mysql user password. Must match with database container's \<add-client-pass>. 


Steps:
- Install Docker (https://docs.docker.com/install/)
- cd iot-device-manager/
- docker build -t iotdevicemanager .
- docker run -dit --name iotdevicemanager -e APP_PASS=<add-api/ui-pass> -e DB_PASS=<add-db-pass> -p 80:8080 --link mysql-idm iotdevicemanager
- (Optional - Follows process from logs) docker logs -f iotdevicemanager
##### Example: 
```bash
# cd to project root
$ cd iot-device-manager 

# Build image, create and run container, follow logs as container starts
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
- doc/iot-device-manager.raml

