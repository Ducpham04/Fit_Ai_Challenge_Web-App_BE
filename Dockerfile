# Stage 1: Cache dependencies
FROM maven:3.8.5-openjdk-17-slim AS dependencies
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn dependency:go-offline -B

# Stage 2: Development (dùng cho docker-compose)
FROM openjdk:17-jdk-slim AS development
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy dependencies cache
COPY --from=dependencies /root/.m2 /root/.m2

# Copy Maven wrapper và pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code (sẽ được override bởi volume)
COPY src ./src

EXPOSE 8080
EXPOSE 5005

# Chạy với Maven Spring Boot Plugin (auto-reload)
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]

# Stage 3: Production build
FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /app

COPY --from=dependencies /root/.m2 /root/.m2
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 4: Production runtime
FROM openjdk:17-jdk-slim AS production
WORKDIR /app
COPY --from=builder /app/target/FIT_Challenge-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]