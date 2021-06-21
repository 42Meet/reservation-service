# reservation-service

🕹 실행 방법
Gradle을 활용한 빌드 및 패키징

./gradlew clean build
Dockerizing

docker build -t kangjm2/member-service:1.0 .
Container 실행

docker run -d -p 8080:8080 --name member-service \
-e "token.secret=my_secret_token" \
kangjm2/memberservice:1.0
DockerHub 이용시

docker pull kangjm2/memberservice:1.0



origin: http://15.164.85.227:8080

GET	/reservation/list?date=2021-06-21	해당 날짜의 예약 현황 가져오기	date(string)	예약내역 없는경우: []
response :
[{
"id": 2,
"leaderName": "Taehkim",
"roomName": "경복궁",
"location": "개포",
"date": "2021-06-18",
"startTime": "14:00:00",
"endTime": "15:00:00"
},
{
"id": 1,
"leaderName": "Taehkim",
"roomName": "경복궁",
"location": "개포",
"date": "2021-06-18",
"startTime": "17:00:00",
"endTime": "18:00:00"
}]
POST	/reservation/register	예약하기	{
"location": "개포",
"roomName": "유튜브스튜디오 1층",
"date": "2021-06-27",
"startTime": "17:00:00",
"endTime": "18:00:00",
"leaderName": "taehkim",
"department": "42Seoul",
"purpose": "알고리즘 스터디",
"title": "취업폭주기관차",
"content": "아무내용입니다.",
"members": [
"sebaek",
"jakang",
"esim",
"good"
]
}	http status code
GET	/reservation/mypage	예약 정보 가져오기		

[예정]

예약내역 없는경우: []
예약내역 존재할경우:
[
	{                      // 예약 만료 목록 (0번째 배열)
		{
		"id": 1,
		"leaderName": "Taehkim",
		"roomName": "경복궁",
		"location": "개포",
		"date": "2021-06-18",
		"startTime": "17:00:00",
		"endTime": "18:00:00"
		},
		{
		"id": 2,
		"leaderName": "Taehkim",
		"roomName": "경복궁",
		"location": "개포",
		"date": "2021-06-18",
		"startTime": "14:00:00",
		"endTime": "15:00:00"
		}
	},
	{                        // 진행중인 예약 목록 (1번째 배열)
		{
		"id": 1,
		"leaderName": "Taehkim",
		"roomName": "경복궁",
		"location": "개포",
		"date": "2021-06-18",
		"startTime": "17:00:00",
		"endTime": "18:00:00"
		},
		{
		"id": 2,
		"leaderName": "Taehkim",
		"roomName": "경복궁",
		"location": "개포",
		"date": "2021-06-18",
		"startTime": "14:00:00",
		"endTime": "15:00:00"
		}
	},
	{                        // 예정된 예약 목록 (2번째 배열)
		{
		"id": 1,
		"leaderName": "Taehkim",
		"roomName": "경복궁",
		"location": "개포",
		"date": "2021-06-18",
		"startTime": "17:00:00",
		"endTime": "18:00:00"
		},
		{
		"id": 2,
		"leaderName": "Taehkim",
		"roomName": "경복궁",
		"location": "개포",
		"date": "2021-06-18",
		"startTime": "14:00:00",
		"endTime": "15:00:00"
		}
	}
]
POST	/reservation/delete	삭제	body:
{
"id": 2
}	http status code
GET	/reservation/rooms	전체 회의실 정보 가져오기		{
{
location: '개포',
roomName: ['경복궁', '창경궁', '덕수궁']
},
{
location: '서초',
roomName: ['7클', '9클']
}
}
