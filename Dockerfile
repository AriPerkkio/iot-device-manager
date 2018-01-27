# docker build -t iotdevicemanager . 
# docker run -dit --name iotdevicemanager -p 8080:8080 --link mysql-idm iotdevicemanager bash
FROM ubuntu:16.04

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
    git \
    wget \
    tar

# TODO archived version
# Tomcat
RUN cd /opt && \
    wget http://www.nic.funet.fi/pub/mirrors/apache.org/tomcat/tomcat-9/v9.0.4/bin/apache-tomcat-9.0.4.tar.gz && \
    tar xzf apache-tomcat-9.0.4.tar.gz && \
    mv apache-tomcat-9.0.4 tomcat9

# TODO ENV credentials
RUN mkdir /home/user && \
    cd /home/user && \
    git clone https://github.com/AriPerkkio/iot-device-manager.git && \
    cd iot-device-manager && \
    npm install && \
    npm run build && \
    mvn package && \
    mv target/iot-device-manager-*.war /opt/tomcat9/webapps/iot-device-manager.war

# TODO
# CMD ['/opt/tomcat9/bin/startup.sh']

CMD ['bash']