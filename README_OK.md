# OK 저축은행
## 수동 배치
### 후처리 정보 BSP 로 보내기
- startDate: 대상 채팅방 조회 시작시간, 필수 파라미터
- endDate: 대상 채팅방 조회 종료시간, 필수 파라미터
- limit: 적용할 종료 채팅방 제한 개수, 없으면 기간내 조회한 채팅방 모두 적용
curl 'http://192.168.247.47:18081/happytalk/api/test/runBatch?jobId=END_INFO_TO_BSP&startDate=20200305_120000&endDate=20200326_120000&limit=10'

### 사원 계정 동기화
curl 'http://192.168.247.47:18081/gateway/ht/job/account/manual'

### 종료 분류 동기화
curl 'http://192.168.247.47:18081/gateway/ht/job/cnsrtype/manual'
