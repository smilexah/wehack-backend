FROM openjdk:17
WORKDIR /app
COPY /target/wehack-backend-0.0.1-SNAPSHOT.jar wehack-backend.jar
ENTRYPOINT ["java", "-jar", "wehack-backend.jar"]