FROM openjdk:11-jre-slim as builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/bithumb-0.0.1-SNAPSHOT.jar"]
