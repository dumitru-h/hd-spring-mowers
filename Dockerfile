# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ADD src/test/resources/input.txt testInput.txt
ENTRYPOINT ["java","-jar","/app.jar", "--inputFile=testInput.txt"]