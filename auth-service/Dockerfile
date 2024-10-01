FROM gradle:8.5-jdk17 as builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build -x test --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
