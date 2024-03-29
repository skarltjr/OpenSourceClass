FROM openjdk:11-jre-slim as builder
ARG JAR_FILE=src/main/docker/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
