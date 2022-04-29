FROM maven:3.8.5-openjdk-11-slim as build
WORKDIR /app
COPY src/ ./src/
COPY pom.xml .
RUN mvn -D maven.test.skip package

FROM openjdk:11-jdk
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} SSA4.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","SSA4.jar"]