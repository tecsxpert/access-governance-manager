# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file (built by Maven)
COPY target/access-governance-manager-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/users/test || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]