FROM openjdk:8-jdk-alpine

VOLUME /tmp

ENV TZ=Asia/Tehran

RUN  mkdir -p /var/log/car-toll-api
RUN  chmod -R 777 /var/log/car-toll-api

RUN  mkdir -p /opt/security/car-toll-api
RUN  chmod -R 777 /opt/security/car-toll-api
COPY src/main/resources/private.pem /opt/security/car-toll-api/
COPY src/main/resources/public.pem /opt/security/car-toll-api/


COPY target/*.jar car-toll-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1514","-jar","/car-toll-api-0.0.1-SNAPSHOT.jar"]