# Install maven and copy project for compilation
#FROM maven:latest as builder
#COPY pom.xml /usr/local/pom.xml
#WORKDIR /usr/local/
#RUN mvn clean install


#it is necessary to have maven in locale and execute: clean install in order to have jar
FROM openjdk:11-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]