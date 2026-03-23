# ============================
# SportsVista Dockerfile
# Multi-stage: Maven build → JRE runtime
# ============================

# --- Stage 1: Build ---
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build the WAR + copy jetty-runner
COPY src ./src
COPY database.sql .
COPY database_pg.sql .
RUN mvn clean package -DskipTests -B

# --- Stage 2: Runtime ---
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy WAR, jetty-runner and SQL scripts from build stage
COPY --from=build /app/target/sportsvista-1.0-SNAPSHOT.war app.war
COPY --from=build /app/target/dependency/jetty-runner.jar jetty-runner.jar
COPY --from=build /app/database_pg.sql .
COPY --from=build /app/database.sql .

# Railway provides PORT env var
ENV PORT=8080

# Start Jetty with the WAR at root context
CMD java -jar jetty-runner.jar --port $PORT --path / app.war
