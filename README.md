# Reservation-Service

## 🕹 실행 방법

1. Gradle을 활용한 빌드 및 패키징

   ```
   ./gradlew clean build
   ```

2. Dockerizing

   ```
   docker build -t nonalias/reservation-service:1.0 .
   ```
   * ***OSX M1의 경우***
       * `docker build --platform linux/amd64 -t nonalias/reservation-service:1.0 .`

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

## 🗒 API 명세 [swagger](http://42meet.kro.kr:8081/swagger-ui.html)

|Method|URL|기능|파라미터|RequestBody|ResponseBody|
|------|---|---|------|-----------|------------|
|GET|/list?date=2021-06-21|해당 날짜의 예약 현황 가져오기|date, location, roomName||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/listResponse.md)|
|POST|/register|예약하기||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/register.md)||
|GET|/mypage|예약 정보 가져오기|||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/mypageResponse.md)|
|POST|/delete|삭제||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/delete.md)||
|GET|/rooms|전체 회의실 정보 가져오기|||[보기](https://github.com/42Meet/reservation-service/blob/main/mdfiles/roomsResponse.md)|

## 📕 디렉토리 구조

```
📁reservation-service
└── 📁src
    └──  📁main
         ├── 📁java
         │    ├── 📁config
         │    ├── 📁controller
         │    ├── 📁domain
         │    ├── 📁service
         │    ├── 📁utils
         │    ├── 📁utils  
         │    └── 📁dto
         └── 📁resources
```


## DB 구조
![image](https://user-images.githubusercontent.com/43032377/122879335-be292780-d373-11eb-8801-134b861ae65d.png)

## Author
김태훈 nonalias
백승호 sebaek42
