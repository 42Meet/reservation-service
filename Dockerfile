FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/reservation-service-1.0.jar ReservationService.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "ReservationService.jar"]
