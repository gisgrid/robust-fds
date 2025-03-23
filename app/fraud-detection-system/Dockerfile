# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Environment variables will be injected at runtime
ENV AMOUNT_THRESHOLD=10000
ENV SUSPICIOUS_ACCOUNTS=ACC-001,ACC-002,ACC-003

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 