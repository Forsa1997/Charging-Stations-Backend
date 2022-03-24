FROM openjdk:11-ea-slim

WORKDIR /

COPY build/libs/backend-0.0.1-SNAPSHOT.jar charging-station-backend.jar

EXPOSE 8080

CMD java -jar charging-station-backend.jar
