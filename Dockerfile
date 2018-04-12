# docker build -t iotdevicemanager .

# docker run -dit --name iotdevicemanager \
# -e APP_PASS=<add-api/ui-pass> \
# -e DB_PASS=<add-db-pass> \
# -p 8080:8080 \
# --link mysql-idm iotdevicemanager

# docker logs -f iotdevicemanager

FROM ubuntu:16.04

ARG DB_PASS=${DB_PASS}
ENV DB_PASS=${DB_PASS}

ARG APP_PASS=${APP_PASS}
ENV APP_PASS=${APP_PASS}

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

# Download sources, build api documentation
RUN mkdir /home/user && \
    cd /home/user && \
    git clone https://github.com/AriPerkkio/iot-device-manager.git && \
    cd iot-device-manager && \
    npm install && \
    echo -e "\n\n##########################################\nBuilding api-doc. This may take 10-15 mins\n##########################################\n\n" && \
    npm run api-doc

# Update sources, replace passwords, build api docs, build app, run tests, launch app.
CMD cd /home/user/iot-device-manager && \
    git checkout . && \
    git pull && \
    sed -i -- 's/spring.datasource.password=client/spring.datasource.password='$DB_PASS'/g' src/main/resources/application.properties && \
    sed -i -- 's/spring.datasource.password=client/spring.datasource.password='$DB_PASS'/g' src/test/resources/application.properties && \
    sed -i -- 's/iotdevicemanager.password=default-password/iotdevicemanager.password='$APP_PASS'/g' src/main/resources/application.properties && \
    sed -i -- 's/iotdevicemanager.password=default-password/iotdevicemanager.password='$APP_PASS'/g' src/test/resources/application.properties && \
    npm install && \
    npm run build && \
    mvn clean package -DskipTests && \
    mvn test && \
    mvn failsafe:integration-test && \
    npm run test && \
    java -jar target/iot-device-manager-*.jar