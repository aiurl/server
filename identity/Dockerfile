# Stage 1: Build the identity module from the multi-module root context.
FROM maven:3.9.15-eclipse-temurin-25 AS build
WORKDIR /workspace

# Build with repository root as context: docker build -t theurl.io/identity -f identity/Dockerfile .
COPY pom.xml ./pom.xml
COPY identity/pom.xml ./identity/pom.xml

# Resolve dependencies in a separate layer to speed up rebuilds.
RUN mvn -f identity/pom.xml -DskipTests dependency:go-offline

COPY identity/src ./identity/src

# Force Spring Boot repackage and override parent skip flag to guarantee fat JAR output.
RUN mvn -f identity/pom.xml clean package spring-boot:repackage -DskipTests -Dspring-boot.run.skip=false

RUN mvn -f identity/pom.xml dependency:copy-dependencies -DoutputDirectory=/workspace/identity/target/dependencies -DincludeScope=runtime

# Stage 2: Run the application
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV SPRING_PROFILES_ACTIVE=prod
ENV CONFIG_SERVER_URI=http://theurl-config-server:8900
ENV CONFIG_SERVER_USERNAME=theurl
ENV CONFIG_SERVER_PASSWORD=Qwer.1234
ENV DB_URL=jdbc:postgresql://theurl-db:5432/linkyou?currentSchema=public
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV DB_DRIVER=org.postgresql.Driver
ENV DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
ENV REDIS_URL=redis://theurl-redis:6379
ENV RABBITMQ_HOST=theurl-rabbitmq
ENV RABBITMQ_PORT=5672
ENV RABBITMQ_USERNAME=guest
ENV RABBITMQ_PASSWORD=guest
ENV JWT_SECRET=your-jwt-secret-key
ENV EXTERNAL_AUTH_REDIRECTURI=https://theurl.io/auth/callback
ENV GOOGLE_CLIENT_ID=your-google-client-id
ENV GOOGLE_CLIENT_SECRET=your-google-client-secret
ENV GITHUB_CLIENT_ID=your-github-client-id
ENV GITHUB_CLIENT_SECRET=your-github-client-secret
ENV FACEBOOK_CLIENT_ID=your-facebook-client-id
ENV FACEBOOK_CLIENT_SECRET=your-facebook-client-secret
ENV MICROSOFT_CLIENT_ID=your-microsoft-client-id
ENV MICROSOFT_CLIENT_SECRET=your-microsoft-client-secret

COPY --from=build /workspace/identity/target/identity-1.0.jar /app/app.jar
COPY --from=build /workspace/identity/target/dependencies /app/lib

LABEL maintainer="damon <zhaorong@outlook.com>"

EXPOSE 8901
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

