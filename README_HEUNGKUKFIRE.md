# 흥국화제
## 인프라
### 개발계
  - DB: application.yml 참고
  - WAS: 10.110.1.51:8001
    - /opt/tomcat
    - /opt/log
  - WEB: 125.132.3.169:443
  - 컴서버: 외부와 통신 필요시 사용 (NAT 장비와 유사)
    - 컴서버 담당자에게 설정 요청 필요
    - OUT https://114.108.29.102:8001/blah/blah -> https://bzc-api.kakao.com/blah/blah 로 변환
    - OUT https://114.108.29.102:8002/blah/blah -> https://gw.talk.naver.com/blah/blah 로 변환
    - IN https://114.108.29.102:443/blah/blah -> WEB 서버로 전달
    - IN https://114.108.29.102:443/blah/blah -> WEB 서버로 전달
    - https -> http 불가
  - 통합웹서버: 특정 AP를 통해서 접속가능한 웹서버

### 운영계
  - DB: application.yml 참고
  - WAS1: 10.110.121.81:8001
    - /opt/tomcat
    - /opt/log
  - WAS2: 10.110.121.82:8001
    - /opt/tomcat
    - /opt/log
  - WEB1: 114.108.29.71:443
  - WEB2: 114.108.29.72:443

## 배포
### 개발계
  - 빌드 후 sftp 로 ~/deploy/happytalk.war 로 업로드
  - ssh 연결 후 deploy.sh 실행

### 운영계
