FROM openjdk:11-jdk-oracle
ARG JAR_FILE=_JAR_NAME
COPY ${JAR_FILE} pubsubpuller.jar
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-jar","/pubsubpuller.jar"]
