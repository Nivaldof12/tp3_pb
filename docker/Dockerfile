# syntax=docker/dockerfile:1
# Build: docker build -f docker/Dockerfile --build-arg MAVEN_MODULE=infnet-core -t infnet-core .
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace
COPY . .
ARG MAVEN_MODULE
RUN chmod +x mvnw \
    && ./mvnw -B -q -pl "${MAVEN_MODULE}" -am package -DskipTests

FROM eclipse-temurin:17-jre-jammy
ARG MAVEN_MODULE
RUN useradd -ms /bin/bash spring \
    && apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
USER spring
WORKDIR /app
COPY --from=build /workspace/${MAVEN_MODULE}/target/*.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=docker,observability
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
  CMD curl -sf "http://localhost:${SERVER_PORT}/actuator/health" || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
