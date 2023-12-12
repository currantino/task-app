FROM eclipse-temurin:17-jre-alpine
COPY build/libs/task-app-*.jar /app.jar
ENTRYPOINT java -jar /app.jar