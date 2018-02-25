# docker build -t iotdevicemanager . --build-arg DB_PASS=<add-client-pass>
# docker run -dit --name iotdevicemanager -p 8080:8080 --link mysql-idm iotdevicemanager bash
FROM ubuntu:16.04

ARG DB_PASS=${DB_PASS}
ENV DB_PASS=${DB_PASS}

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre

## Debugging tools
RUN apt-get update && apt-get install -y \
    nano \
    curl

# Add dependencies
RUN curl -sL https://deb.nodesource.com/setup_9.x | bash -

# Build tools
RUN apt-get update && apt-get install -y \
    openjdk-8-jdk \
    maven \
    nodejs \
    git

RUN mkdir /home/user && \
    cd /home/user && \
    mv /home/iot-device-manager . && \
    git clone https://github.com/AriPerkkio/iot-device-manager.git && \
    cd iot-device-manager && \
    sed -i -- 's/spring.datasource.password=client/spring.datasource.password='$DB_PASS'/g' src/main/resources/application.properties &&\
    sed -i -- 's/spring.datasource.password=client/spring.datasource.password='$DB_PASS'/g' src/test/resources/application.properties &&\
    npm install && \
    npm run build && \
    mvn package && \
    mv target/iot-device-manager-*.jar /home/user/iot-device-manager.jar

CMD ["java","-jar","/home/user/iot-device-manager.jar"]