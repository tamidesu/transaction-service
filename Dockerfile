FROM amazoncorretto:17.0.12-al2023
WORKDIR /app
COPY build/libs/transaction-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]