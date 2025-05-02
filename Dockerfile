# Stage 1: Build the application using Maven and OpenJDK 17
FROM maven:3.9.6-openjdk-17-slim AS builder

# Create app directory for sources
WORKDIR /usr/src/app

# Copy the pom.xml file (takes advantage of Docker cache)
COPY pom.xml .

# Download all dependencies (cached as long as pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src src

# Build the application (and skip tests for production image)
RUN mvn package -DskipTests

# Stage 2: Create the production image
FROM openjdk:17-jdk-slim

# Add app user to avoid running as root
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

# Set the working directory
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /usr/src/app/target/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SPRING_PROFILES_ACTIVE="production"

# Expose the port the app runs on
EXPOSE 8080

# Entry point to run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]