### Proper instructions - TODO

# Iot Device Manager (school project)

## Tech
- Spring boot
- Mysql
- React, Redux, Router, Webpack, etc
- Robot Framework
  - Database tests
  - Probably some UI + Integration tests
- Docker
- Deployment in AWS or DigitalOcean <b>TODO</b>

## Documentation
- database/schema-draw-io.xml
- database/schema.png <b>TODO</b>
- doc/iot-device-manager.raml

## Build requirements:

### Option 1:

#### 1. Database
- Install Docker
- cd iot-device-manager/database/
- docker build -t mysql-idm . --build-arg CLIENT_PASS=<add-client-pass>
- docker run --name mysql-idm -e MYSQL_ROOT_PASSWORD=<add-root-password> -d mysql-idm

#### 2. Server
- Install Docker
- cd iot-device-manager/
- docker build -t iotdevicemanager . --build-arg DB_PASS=<add-client-pass>
- docker run -dit --name iotdevicemanager -p 8080:8080 --link mysql-idm iotdevicemanager

### Option 2:
- Check Dockerfiles for requirements and steps
