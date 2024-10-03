# Этап сборки
FROM gradle:8.5-jdk17 as builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build -x test --no-daemon

# Этап выполнения
FROM openjdk:17-slim
WORKDIR /app

# Копируем скомпилированный JAR файл из этапа сборки
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Копируем файл mock_data.sql в финальный образ
COPY --from=builder /app/src/main/resources/mock_data.sql /app/resources/mock_data.sql

EXPOSE 8081

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
