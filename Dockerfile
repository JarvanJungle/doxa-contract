

#
# Build stage
#

FROM maven:3.6.0-jdk-11-slim AS build
LABEL Doxa contract application
LABEL Maintainer: Vu Duc Noi

COPY src /home/app/src
COPY pom.xml /home/app
COPY target/*.jar /home/app/target/*.jar
#RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:11-jre-slim
ARG JAR_FILE=/home/app/target/*.jar
COPY --from=build ${JAR_FILE} DoxaContractApplication.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","DoxaContractApplication.jar"]