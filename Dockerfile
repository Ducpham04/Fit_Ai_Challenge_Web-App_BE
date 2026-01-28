# Stage 1: Cache dependencies
FROM maven:3.9.6-eclipse-temurin-17 AS dependencies
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn dependency:go-offline -B

# Stage 2: Development (dùng cho docker-compose)
FROM eclipse-temurin:17-jdk-jammy AS development
WORKDIR /app

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

COPY --from=dependencies /root/.m2 /root/.m2
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

EXPOSE 8080
EXPOSE 5005

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]

# Stage 3: Production build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY --from=dependencies /root/.m2 /root/.m2
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 4: Production runtime
FROM eclipse-temurin:17-jdk-jammy AS production
WORKDIR /app
COPY --from=builder /app/target/FIT_Challenge-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
