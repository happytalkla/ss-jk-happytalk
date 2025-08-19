# SI HappyTalk

## 로컬 환경
### Docker CE
https://hub.docker.com/?overlay=onboarding

### RabbitMQ Docker 이미지 빌드
```sh
$ pwd
~/Projects/si-happytalk/docker
$ docker build -t ht/rabbitmq-stomp:1.0 .
```

### RabbitMQ 등 인프라 실행
```sh
$ pwd
~/Projects/si-happytalk/docker
$ docker-compose up -d
```

### Oracle (로컬에서 필요할 경우)
https://github.com/oracle/docker-images/blob/master/OracleDatabase/SingleInstance/README.md
#### 패키지 다운로드
https://www.oracle.com/technetwork/database/enterprise-edition/downloads/index.html

#### 도커 이미지 빌드
```sh
$ git clone git@github.com:oracle/docker-images.git
$ cd docker-images/OracleDatabase/SingleInstance/dockerfiles
$ /buildDockerImage.sh -v 12.2.0.1 -e
```

#### 컨테이너 실행
```sh
$ pwd
/Users/dy/docker/service-oracle
$ docker-compose up -d
$ docker logs -f oracle
```

퍼미션 에러 날 경우
```sh
$ pwd
/Users/dy/docker/service-oracle
$ mkdir oradata
$ chown 54321:54321 oradata
$ docker-compose up -d
$ docker logs -f oracle
```

#### 유저 및 스키마 생성
- 유저 생성
    - src/main/resources/sql/user.sql
- 스키마 생성
    - src/main/resources/sql/schema.sql
- 기본 데이터
    - src/main/resources/sql/data.sql

### Jeus 웹소켓 지원
https://technet.tmaxsoft.com/ko/front/technology/viewTechnology.do?cmProductCode=0101&paging.page=3&kss_seq=KSSQ-20150806-000001

## Test
```
$ pwd
~/Projects/api-hook
$ mvn clean compile test
```

## Build
```
$ pwd
~/Projects/api-hook
mvn -DskipTests clean package
```

## 예외 처리
- catch 문으로 잡지 않는 예외는 `config.CustomExceptionHandler` 에서 처리
- `config.CustomExceptionHandler` 에서 처리된 예외는 에러 페이지 노출

## Health Check
`/actuator/health` 호출로 체크 가능

## 로그 설정
### 기본 설정
- `resources/application.yml`
- 로그 파일 위치 지정
- LogBack 설정 파일 지정
- 디버깅 레벨 지정
- 기본 로그 포맷 설정

### 상세 설정
- `resources/logback-*.xml`
- 로그 파일 Rolling 설정
- 보관 기간 설정
- 로그 포맷 설정 (없으면 기본 설정 따름)
