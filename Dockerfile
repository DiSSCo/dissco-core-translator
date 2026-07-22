FROM eclipse-temurin:25-alpine AS builder
WORKDIR /application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract

FROM eclipse-temurin:25-alpine
RUN adduser --disabled-password -u 1000 java
WORKDIR /application
COPY --chown=java:java --from=builder /builder/extracted/dependencies/ ./
RUN true
COPY --chown=java:java --from=builder /builder/extracted/spring-boot-loader/ ./
RUN true
COPY --chown=java:java --from=builder /builder/extracted/snapshot-dependencies/ ./
RUN true
COPY --chown=java:java --from=builder /builder/extracted/application/ ./
USER 1000

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
