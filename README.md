### Proper instructions - TODO

# Iot Device Manager (school project)

## Tech
- Spring boot + Tomcat
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
- docker build -t mysql-idm .
- docker run --name mysql-idm -d mysql-idm
- Initialize database:
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/create_database.sql"
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_configuration.sql"
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device_group.sql"
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device_icon.sql"
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device.sql"
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_device_type.sql"
   - docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/procedures_location.sql"
   - (Optional - add mockdata) docker exec -it mysql-idm bash -c "mysql -uroot -p < /home/mockdata.sql"
   - (Optional - run tests) robot database-tests.robot
#### 2. Server
- cd iot-device-manager/
- docker build -t iotdevicemanager . 
- docker run -dit --name iotdevicemanager -p 8080:8080 --link mysql-idm iotdevicemanager bash
- (Required for now) docker exec -dit iotdevicemanager bash
- (Required for now) /opt/tomcat9/bin/startup.sh

### Option 2: 
- Check Dockerfiles for requirements and steps
