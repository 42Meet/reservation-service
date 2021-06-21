# Reservation-Service

### 🕹 실행 방법

1. Gradle을 활용한 빌드 및 패키징

   ```
   ./gradlew clean build
   ```

2. Dockerizing

   ```
   docker build -t nonalias/reservation-service:1.0 .
   ```

3. Container 실행

   ```
   docker run -d -p 8081:8080 --name reservation-service \
   nonalias/reservation-service:1.0
   ```

* DockerHub 이용시

  ```
  docker pull nonalias/reservation-service:1.0
  ```

<br/>

# API 명세
🗒

|Method|URL|기능|파라미터|Body|
|------|---|---|------|----|
|GET|/list?date=2021-06-21|해당 날짜의 예약 현황 가져오기|date, location, roomName|
|POST|/register|예약하기||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/register.md)|
|GET|/mypage|예약 정보 가져오기|||
|POST|/delete|삭제||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/delete.md)|
|GET|/rooms|전체 회의실 정보 가져오기|||
