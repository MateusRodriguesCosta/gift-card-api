# Base image with Java
FROM amazoncorretto:23

# Working directory
WORKDIR /app

# Copy the built jar file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose Spring Boot application port listening
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]