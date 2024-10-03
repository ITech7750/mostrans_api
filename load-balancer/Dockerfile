FROM gradle:8.5-jdk17 as builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build -x test --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/load-balancer.jar
EXPOSE 8003
ENTRYPOINT ["java", "-jar", "/app/load-balancer.jar"]
