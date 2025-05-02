#FROM openjdk:17
#WORKDIR /app
#COPY /target/wehack-backend-0.0.1-SNAPSHOT.jar wehack-backend.jar
#ENTRYPOINT ["java", "-jar", "wehack-backend.jar"]

# Build stage
FROM maven:3.8.6-openjdk-17-slim AS build
# or maven:3.8.6-jdk-17
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/wehack-backend-0.0.1-SNAPSHOT.jar wehack-backend.jar
ENTRYPOINT ["java", "-jar", "wehack-backend.jar"]