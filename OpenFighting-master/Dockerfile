# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy the full project
COPY src ./src

RUN mvn -q package -DskipTests=false

# 2. Runtime Stage

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
