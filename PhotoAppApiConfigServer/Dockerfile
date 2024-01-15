FROM openjdk:17-oracle
VOLUME /tmp
COPY target/ConfigServer.jar ConfigServer.jar
ENTRYPOINT ["java","-jar","ConfigServer.jar"]
