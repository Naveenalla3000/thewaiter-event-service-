# Use official Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and dependencies (for efficient caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK image for running the app
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
