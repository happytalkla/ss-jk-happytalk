# 메세지 송수신 기본 구조
## ChatMessageController.java (서버), client.js (클라이언트)
- ex) 채팅방 입장
  - client.js:Client.sendJoin() -> ChatMessageController.join() -> client.js:Client.onPublicMessageReceived()

- ex) 메세지 보내기
  - client.js:Client.sendMessage() -> ChatMessageController.message() -> client.js:Client.onPublicMessageReceived()

- ex) 채팅방 종료
  - client.js:Client.sendEnd() -> ChatMessageController.end() -> client.js:Client.onPublicMessageReceived()

## 메세지 보내기/받기 외 이벤트 처리 (Ajax 호출)
- Rest*Controller.java 참조

- ex) 상담원 연결 버튼
  - 클라이언트: customer.js:46
  	- onClick(.btn_hot_key.requestCounselor)
  - 서버: RestChatController.putChatRoom()
  	- if (ChatCommand.REQUEST_COUNSELOR.equals(command))

