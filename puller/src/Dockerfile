FROM maven:3.8.1-jdk-11 AS build
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app
WORKDIR /usr/src/app
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:11-jdk-oracle
COPY --from=build /usr/src/app/target/puller-1.0-SNAPSHOT.jar puller.jar
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-jar","/puller.jar"]