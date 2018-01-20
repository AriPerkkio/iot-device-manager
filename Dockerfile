# docker build -t iotdevicemanager .
# docker run -dit --name iotdevicemanager -p 8080:8080 iotdevicemanager bash
FROM ubuntu:16.04

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre

RUN apt-get update && \
  apt-get dist-upgrade -y

# Add dependencies
RUN curl -sL https://deb.nodesource.com/setup_9.x | bash -

# Build tools
RUN apt-get install -y \
    openjdk-8-jdk \
    maven \
    nodejs \
    git \
    wget \
    tar

# Tomcat
RUN cd /opt && \
    wget http://www.nic.funet.fi/pub/mirrors/apache.org/tomcat/tomcat-9/v9.0.2/bin/apache-tomcat-9.0.2.tar.gz && \
    tar xzf apache-tomcat-9.0.2.tar.gz && \
    mv apache-tomcat-9.0.2 tomcat9

## Debugging tools
RUN apt-get install -y nano

RUN mkdir /home/user && \
    cd /home/user
RUN git clone https://github.com/AriPerkkio/iot-device-manager.git && \
    cd iot-device-manager
# TODO configure credentials etc
#    && mvn package \
#    && mv target/iot-device-manager-*.war /opt/tomcat9/webapps/iot-device-manager.war

CMD ['bash']