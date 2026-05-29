# Stage 1: Build the config module from the multi-module root context.
FROM maven:3.9.15-eclipse-temurin-25 AS build
WORKDIR /workspace

# Build with repository root as context: docker build -t theurl.io/config -f config/Dockerfile .
COPY pom.xml ./pom.xml
COPY config/pom.xml ./config/pom.xml

# Resolve dependencies in a separate layer to speed up rebuilds.
RUN mvn -f config/pom.xml -DskipTests dependency:go-offline

COPY config/src ./config/src

# Force Spring Boot repackage and override parent skip flag to guarantee fat JAR output.
RUN mvn -f config/pom.xml clean package spring-boot:repackage -DskipTests -Dspring-boot.run.skip=false

# Stage 2: Run the application
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV SPRING_PROFILES_ACTIVE=native
ENV CONFIG_SEARCH_LOCATIONS=file:./config/

COPY --from=build /workspace/config/target/config-1.0.jar /app/app.jar

LABEL maintainer="damon <zhaorong@outlook.com>"

EXPOSE 8900
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
