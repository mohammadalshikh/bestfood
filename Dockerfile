# Stage 1: Build the WAR using Maven
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy only the pom first to leverage Docker cache
COPY pom.xml .
# Download dependencies only (faster rebuilds)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the WAR, skipping tests for faster build
RUN mvn clean package -DskipTests

# Stage 2: Run the WAR using a JDK image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the WAR from the build stage
COPY --from=build /app/target/*.war app.war

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Run the Spring Boot WAR
ENTRYPOINT ["java", "-jar", "app.war"]
