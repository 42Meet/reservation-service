FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/reservation-service-1.0.jar ReservationService.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "ReservationService.jar"]
