FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY build/libs/*.jar app.jar

ENV PORT 8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
