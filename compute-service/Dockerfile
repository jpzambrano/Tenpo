# Start with a lightweight JDK base image
FROM eclipse-temurin:21-jdk-alpine as builder

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and project files
COPY mvnw pom.xml ./   
COPY .mvn .mvn       

# Ensure mvnw is executable
RUN chmod +x mvnw

# Download dependencies (caching layer)
RUN ./mvnw dependency:go-offline

# Copy the application source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Use a lightweight JRE base image for running the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/compute-service-*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
RUN apk add --no-cache bash
COPY wait-for-it.sh /usr/local/bin/wait-for-it
ENTRYPOINT ["wait-for-it", "database:5432", "--", "java", "-jar", "/app/app.jar"]