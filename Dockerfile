#FROM openjdk:17
#WORKDIR /app
#COPY /target/wehack-backend-0.0.1-SNAPSHOT.jar wehack-backend.jar
#ENTRYPOINT ["java", "-jar", "wehack-backend.jar"]

# Build stage - using official Maven image
FROM maven:3.8.6-jdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/wehack-backend-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]