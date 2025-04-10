# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app
# Copy pom.xml and source code to leverage cache
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM amazoncorretto:23
WORKDIR /app
# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]