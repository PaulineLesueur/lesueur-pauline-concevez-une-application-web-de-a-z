FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/PayMyBuddy-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/PayMyBuddy-0.0.1-SNAPSHOT.jar"]