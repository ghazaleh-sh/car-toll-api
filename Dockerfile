FROM eclipse-temurin:8u362-b09-jdk-alpine

VOLUME /tmp

ENV TZ=Asia/Tehran

RUN  mkdir -p /var/log/car-toll-api
RUN  chmod -R 777 /var/log/car-toll-api

RUN  mkdir -p /opt/security/car-toll-api
RUN  chmod -R 777 /opt/security/car-toll-api
COPY src/main/resources/private.pem /opt/security/car-toll-api/
COPY src/main/resources/public.pem /opt/security/car-toll-api/


COPY target/*.jar car-toll-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1514","-Dcom.ibm.icu.util.TimeZone.DefaultTimeZoneType=JDK","-jar","/car-toll-api-0.0.1-SNAPSHOT.jar"]