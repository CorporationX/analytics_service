FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY service.jar /app/analytics_service.jar
ENTRYPOINT ["java", "-jar", "analytics_service.jar"]