# docker build -t iotdevicemanager . --build-arg DB_PASS=<add-client-pass>
# docker run -dit --name iotdevicemanager -p 8080:8080 --link mysql-idm iotdevicemanager
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
    git clone https://github.com/AriPerkkio/iot-device-manager.git && \
    cd iot-device-manager && \
    sed -i -- 's/spring.datasource.password=client/spring.datasource.password='$DB_PASS'/g' src/main/resources/application.properties && \
    sed -i -- 's/spring.datasource.password=client/spring.datasource.password='$DB_PASS'/g' src/test/resources/application.properties && \
    npm install && \
    npm run build && \
    echo -e "\n\n##########################################\nBuilding api-doc. This may take 10-15 mins\n##########################################\n\n" && \
    npm run api-doc && \
    mvn clean package -DskipTests

CMD cd /home/user/iot-device-manager && \
    mvn test && \
    mvn failsafe:integration-test && \
    npm run test && \
    java -jar target/iot-device-manager-*.jar