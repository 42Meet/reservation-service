## MariaDB를 Docker Container에 띄우자

### 일단, Docker가 실행되어 있어야 한다.
`docker ps` 를 해봐서 아무것도 안뜨거나, 자신이 실행한 컨테이너가 뜨면 켜져있는 것
만약 .sock 이런 오류가 뜨면 Docker가 켜져있지 않은 것이다.

### 그 후, mariaDB Docker image를 Dockerhub에서 pull 받는다.
`docker pull mariadb:latest`

* 위 과정은 사실 생략해도 된다. docker run 할때 image가 없으면 알아서 Dockerhub에서 찾는다.

### Docker image를 실행시켜 Container로 만들어주자.
`docker run -d -e MARIADB_ROOT_PASSWORD=1q2w3e4r -p 3306:3306 mariadb`

### DB 테이블을 만들어 주어야 한다. Docker container 안으로 들어가자.
`docker ps`
* 방금 run 해서 만든 Container의 `CONTAINER ID` 값의 첫 네자리를 기억하자. 예를 들어 (wef2fsdiofjsg33f) 일경우 wef2가 된다.
* 다음 명령어를 입력하자.
`docker exec -it wef2 bash`

* 성공적으로 컨테이너에 접속했으면 테이블을 만들어야 한다. 다음을 입력하자.
`mysql -uroot -p1q2w3e4r`

* 우리가 사용하는 DB인 reservation-service DB를 만들어주자.
``create database `reservation-service`;``

### 그 후 application-credential.yml에 적혀있는 주소값을 다음과 같이 변경한다.
`url: jdbc:mariadb://localhost:3306/reservation-service`
![image](https://user-images.githubusercontent.com/43032377/123230787-8c01fc00-d512-11eb-8950-776d7fb00f96.png)

### 그리고 나서 개발하면 된다 !
