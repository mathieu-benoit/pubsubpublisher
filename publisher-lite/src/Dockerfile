FROM maven:3.8.2-openjdk-16-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
WORKDIR /usr/src/app
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:16.0.2-slim
COPY --from=build /usr/src/app/target/lite-pubsub-publisher-0.0.1.jar publisher-lite.jar
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-jar","/publisher-lite.jar"]