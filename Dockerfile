FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY schedule_helper/pom.xml .
RUN mvn -q dependency:go-offline
COPY schedule_helper/src ./src
RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/schedule_helper-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]