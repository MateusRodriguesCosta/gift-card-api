FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jdk AS runtime
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]