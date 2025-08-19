'use strict';

// ////////////////////////////////////////////////////////////////////////////
// HT Client
// ////////////////////////////////////////////////////////////////////////////
function Client() {

	// 채팅방 목록 관리 여부
	this._hasChatRoomList = false;
	// 구독중인 채팅방
	this._chatRoom = null;
	// 구독중인 채팅방 목록
	this._chatRoomList = null;
	// 채팅 목록 요청 (jQuery Ajax 객체)
	this._requestChatList = null;

	this._wsConnectUrl = HT_APP_PATH + '/ws'; // 소켓 접속 주소
	this._wsTopicPath = '/topic/'; // '상담 채팅방' 구독 베이스 주소
	this._wsQueuePath = '/topic/'; // '개인 메세지' 구독 베이스 주소
	this._wsNoticePath = this._wsTopicPath + 'notice'; // 'Member'용 구독 주소
	this._wsDefaultDestinationPath = '/publish/'; // '상담 채팅방 메세지' 발행 베이스 주소

	// 소켓 연결
	this._SUBSCRIBE_DELAY = 1000;
	this._SEND_JOIN_DELAY = 1000;
	this._MAX_RECONNECT_DELAY = 1000 * 600;
	this._MAX_RECONNECT_COUNT = 10000;
	this._reconnectDelay = 1000;
	this._reconnectCount = 0;
	this._socket = null;
	this._client = null;
	this.connectAndReconnect();
}
Client.prototype.getChatRoom = function(chatRoom) {

	return this._chatRoom;
};
Client.prototype.setChatRoom = function(chatRoom) {

	this._chatRoom = chatRoom;
};
Client.prototype.getChatRoomList = function() {

	return this._chatRoomList;
};
Client.prototype.setChatRoomList = function(chatRoomList) {

	this._hasChatRoomList = true;
	this._chatRoomList = chatRoomList;
};
Client.prototype.hasChatRoomList = function() {

	return this._hasChatRoomList;
};
/**
 * 소켓 연결
 */
Client.prototype.connectAndReconnect = function() {

	// Unsubscribe

	// Disconnect
	if (this._client !== null && this._client.connected === true) {
		this._client.disconnect();
		this._client = null;
	}
	if (this._socket !== null) {
		this._socket.close();
		this._socket = null;
	}

	this._socket = new SockJS(this._wsConnectUrl);
	this._client = Stomp.over(this._socket);
	this._client.reconnect_delay = 1000;
	this._client.heartbeat.outgoing = 10000;
	this._client.heartbeat.incoming = 10000;
	if (htUtils.isProduction()) {
		this._client.debug = function() {};
	}

	var self = this;
	this._client.connect({}, function(frame) {
		self.onConnected(frame);
	}, function(error) {
		self.onError(error);
	});
};
/**
 * 소켓 연결 성공시 콜백 메소드
 *
 * @param frame StompFrame
 */
Client.prototype.onConnected = function(frame) {

//	console.info('>> onConnectCallback Argument');
//	console.info(frame);

	this._reconnectDelay = 1000;
	this._reconnectCount = 0;

	// 채팅방 목록, 개인 메세지 채널에 가입
	if (htUser.getRollType() !== CHAT_ROLL_TYPE.CUSTOMER) {
		var self = this;
		this._client.subscribe(this._wsQueuePath + htUser.getId(), function(payload) {
			self.onWhisperMessageReceived(payload);
		}, {});
		this._client.subscribe(this._wsNoticePath, function(payload) {
			self.onNoticeMessageReceived(payload);
			console.log("========================wsNoticePath=====start==============");
			console.log(payload);
			console.log("========================wsNoticePath=====end==============");
		}, {});
	}

	// 대화중인 채팅방있을 경우 재가입
	if (this._chatRoom) {
		if (this._chatRoom.getSocketChannel()) {
			//this._chatRoom.getSocketChannel().unsubscribeChatRoom();
			this.unsubscribeChatRoom();
		}
		this.subscribeChatRoom();
	}

	// 채팅방 목록이 있을 경우(상담직원) 채팅방 목록 가져오기
	if (this._hasChatRoomList && this._chatRoomList) {
		if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
			this._chatRoomList.loadChatRoomList();
		}
		// 채팅방 상태별 카운터 업데이트
		this._chatRoomList.getStatusCounter();
	}
	this.sendActive(USER_ACTIVE_STATUS.ACTIVE);
};
/**
 * 소켓 커넥션 요류시 콜백 메소드
 *
 * @param error StompObject
 */
Client.prototype.onError = function(error) {

	// 최대 재연결 횟수 초과
	if (this._reconnectCount === this._MAX_RECONNECT_COUNT) {
		alert('현재 채팅서버 접속이 원활하지 않습니다. 잠시 후 다시 접속해주세요.');
		return;
	}

	this._reconnectCount += 1;
	console.info('reconnectDelay: ', this._reconnectDelay, 'reconnectCount: ', this._reconnectCount);

	if (this._client.connected) {
		this._client.disconnect();
	}

	if (this.getChatRoom()) {
		this.getChatRoom().setReceiveJoinMessage(false);
	}

	var self = this;
	setTimeout(function() {
		self.connectAndReconnect();
	}, this._reconnectDelay);

	// 재연결 지연 시간 조정
	//this._reconnectDelay ;
	// 최대 재연결 지연 시간
	if (this._reconnectDelay > this._MAX_RECONNECT_DELAY) {
		this._reconnectDelay = this._MAX_RECONNECT_DELAY;
	}
};
Client.prototype.isConnected = function() {

	if (this._client !== null) {
		return this._client.connected;
	}

	return false;
};
Client.prototype.disconnect = function() {

	if (this._client !== null && this._client.connected) {
		this._client.disconnect();
	}
};
/**
 * 채팅방 구독 요청
 */
Client.prototype.subscribeChatRoom = function() {

	var self = this;
	if (this._chatRoom && this._client.connected) {
		var channel = this._client.subscribe(this._wsTopicPath + this._chatRoom.getId(), function(payload) {
			self.onPublicMessageReceived(payload);
		}, { userId: htUser.getId() });

		this._chatRoom.setSocketChannel(channel);

		var chatRoomUid = this._chatRoom.getId(); // 빠르게 채팅방 전환 할 경우 chatRoom이 바뀔 수 있음
		var sentInv = setInterval(function() {
			if (self.getChatRoom().isReceiveJoinMessage()) {
				console.info('=== SENT JOIN MESSAGE');
				clearInterval(sentInv);
			} else {
				console.info('=== SENDING JOIN MESSAGE');
				if (chatRoomUid === self._chatRoom.getId()) { // 빠르게 채팅방 전환 할 경우 chatRoom이 바뀔 수 있음
					htClient.sendJoin(chatRoomUid);
				} else {
					console.info('=== IGNORE NOT CURRENT CHATROOM');
				}
			}
		}, this._SEND_JOIN_DELAY);
	} else {
		var subscribeInv = setInterval(function() {
			if (self._client.connected) {
				self.subscribeChatRoom();
				clearInterval(subscribeInv);
			}
		}, this._SUBSCRIBE_DELAY);
	}
};
/**
 * chatRoom 구독 해지
 *
 * @param chatRoomUid String
 */
Client.prototype.unsubscribeChatRoom = function(chatRoomUid) {

	// chatRoomUid 파라미터가 있을 경우 현재 채팅방이 맞는지 확인
	if (chatRoomUid && this._chatRoom
			&& this._chatRoom.getId() !== chatRoomUid) {
		return;
	}

	if (this._chatRoom && this._chatRoom.getSocketChannel()) {
		this._chatRoom.getSocketChannel().unsubscribe();
		this._chatRoom.setSocketChannel(null);
		this._chatRoom.setReceiveJoinMessage(false);
	} else {
		console.debug('NO CHAT ROOM SOCKET CHANNEL');
	}
};
// ////////////////////////////////////////////////////////////////////////////
// 채팅방 메세지 전송관련 함수, CHAT_MESSAGE_TYPE 에 따라 함수 분리함
// ////////////////////////////////////////////////////////////////////////////
/**
 * 메세지를 보낼 수 있는 상태인지 판단
 */
Client.prototype.canSendMessage = function() {

	if (this.isConnected() === false) {
		console.error('NOT CONNECTED');
		return false;
	} else if (this._chatRoom === null) {
		console.error('NO CURRENT CHATROOM');
		return false;
	} else if (this._chatRoom.getStatus() === CHAT_ROOM_STATUS.END_COUNSELOR) {
		console.error('ALREADY END CHAT ROOM');
		return false;
	} else if (this._chatRoom.getSocketChannel() === null) {
		console.error('NO SOCKET CHANNEL');
		return false;
	}

	return true;
};
/**
 * CHAT_MESSAGE_TYPE.JOIN: 채팅방 입장
 *
 * @param contents String
 */
Client.prototype.sendJoin = function(contents) {

	console.info('=== SEND JOIN', this._chatRoom.getId());

	var message = {
			type: CHAT_MESSAGE_TYPE.JOIN,
			chatRoomUid: this._chatRoom.getId(),
			senderUid: htUser.getId(),
			senderDivCd: htUser.getRollType()
	};
	this._client.send(this._wsDefaultDestinationPath + 'join', { userId: htUser.getId() }, JSON.stringify(message));
};
/**
 * CHAT_MESSAGE_TYPE.END: 채팅방 종료
 *
 * @param contents String
 */
Client.prototype.sendEnd = function(contents) {
	
	if (this.canSendMessage() === false) {
		return;
	}

	var message = {
			type: CHAT_MESSAGE_TYPE.END,
			chatRoomUid: this._chatRoom.getId(),
			senderUid: htUser.getId(),
			senderDivCd: htUser.getRollType(),
			cont: JSON.stringify({
				v: DEFAULT_CONTENTS_VERSION,
				balloons: [
					{
						sections: [
							{
								type: 'text', data: contents
							}
						]
					}
				]
			}),
			contDivCd: CHAT_MESSAGE_CONTENTS_TYPE.TEXT,
			msgStatusCd: CHAT_MESSAGE_STATUS.SENT
	};
	this._client.send(this._wsDefaultDestinationPath + 'end', {}, JSON.stringify(message));
};
/**
 * CHAT_MESSAGE_TYPE.STATUS: 받은 메세지에 상태 (보냄, 받음, 읽음 등...)를 변경
 *
 * @param status CHAT_MESSAGE_STATUS
 * @param chatMessage Object
 */
Client.prototype.sendStatus = function(status, chatMessage) {

	if (this.canSendMessage() === false) {
		return;
	}

	if (status) {
		// 자기 자신이 보낸 메세지에 읽음 상태 보내지 않음
		if (CHAT_MESSAGE_STATUS.READ === status
				&& chatMessage.senderUid === htUser.getId()) {
			return;
		}

		// 이미 변경된 status 를 다시 변경하지 않음
		if (status === chatMessage.msgStatusCd) {
			return;
		}

		// status 변경 기록
		chatMessage.msgStatusCd = status;

		var message = {
			type: CHAT_MESSAGE_TYPE.STATUS,
			chatNum: chatMessage.chatNum,
			chatRoomUid: this._chatRoom.getId(),
			senderUid: htUser.getId(),
			msgStatusCd: status
		};
		this._client.send(this._wsDefaultDestinationPath + 'status', {}, JSON.stringify(message));
	} else {
		console.error('status: ', status, 'chatMessage: ', chatMessage);
	}
};
/**
 * 텍스트 메세지를 발행
 *
 * @param contents [String | JSON.stringify(ChatContents)]
 * @param isJson Boolean, false: String, true: ChatContents
 * @deprecated
 * @param receiver String 받을 사용자 ID
 */
Client.prototype.sendMessage = function(contents, isJson, receiver) {

	if (this.canSendMessage() === false) {
		return;
	}

	if (contents) {
		var message = {
			type: CHAT_MESSAGE_TYPE.MESSAGE,
			chatRoomUid: this._chatRoom.getId(),
			senderUid: htUser.getId(),
			senderDivCd: htUser.getRollType(),
			cont: JSON.stringify({
				v: DEFAULT_CONTENTS_VERSION,
				balloons: [
					{
						sections: [
							{
								type: 'text', data: contents
							}
						]
					}
				]
			}),
			contDivCd: CHAT_MESSAGE_CONTENTS_TYPE.TEXT,
			msgStatusCd: CHAT_MESSAGE_STATUS.SENT
		};
		if (isJson) {
			message.cont = contents;
			console.debug(message.cont);
		}
		if (receiver !== undefined) {
			message.receiver = receiver;
		}
		this._client.send(this._wsDefaultDestinationPath + (receiver === undefined ? 'message' : 'whisper'), {}, JSON.stringify(message));
		contents == "";
	} else {
		console.error('contents: ', contents, 'receiver: ', receiver);
	}
};
/**
 * 유저 활동 상태 발행
 *
 * @param userActiveStatus USER_ACTIVE_STATUS
 */
Client.prototype.sendActive = function(userActiveStatus) {

	if (this.canSendMessage() === false) {
		return;
	}

	if (userActiveStatus) {
		var message = {
			type: CHAT_MESSAGE_TYPE.ACTIVE,
			chatRoomUid: this._chatRoom.getId(),
			senderUid: htUser.getId(),
			userActiveStatus: userActiveStatus
		};
		this._client.send(this._wsDefaultDestinationPath + 'active', {}, JSON.stringify(message));
	} else {
		console.error('userActiveStatus: ', userActiveStatus);
	}
};
/**
 * 메모를 발행
 *
 * @param contents String
 * @param receiver String 받을 사용자 ID
 */
Client.prototype.sendMemo = function(contents, receiver) {

	if (contents) {
		var message = {
			type: CHAT_MESSAGE_TYPE.MESSAGE,
			chatRoomUid: this._chatRoom.getId(),
			senderUid: htUser.getId(),
			senderDivCd: htUser.getRollType(),
			cont: JSON.stringify({
				v: DEFAULT_CONTENTS_VERSION,
				balloons: [
					{
						sections: [
							{
								type: 'text', data: contents
							}
						]
					}
				]
			}),
			contDivCd: CHAT_MESSAGE_CONTENTS_TYPE.MEMO,
			msgStatusCd: CHAT_MESSAGE_STATUS.SENT
		};
		if (typeof receiver !== 'undefined') {
			message.receiver = receiver;
		}
		this._client.send(this._wsDefaultDestinationPath + 'memo', {}, JSON.stringify(message));
	} else {
		console.error('contents: ', contents, 'receiver: ', receiver);
	}
};
/**
 * 파일 메세지를 발행
 *
 * @param data FormData
 * @deprecated
 * @param receiver String 받을 사용자 ID
 */
Client.prototype.sendFileMessage = function(data, receiver) {

	if (htClient) {
		if (htClient.canSendMessage() === false) {
			return;
		}
	} else {
		return;
	}

	if (data && data.fileNm) {
		var message = {
			type: CHAT_MESSAGE_TYPE.MESSAGE,
			chatRoomUid: htClient._chatRoom.getId(),
			senderUid: htUser.getId(),
			senderDivCd: htUser.getRollType(),
			cont: JSON.stringify({
				v: DEFAULT_CONTENTS_VERSION,
				balloons: [
					{
						sections: [
							{
								type: 'file', data: data.fileNm, display: data.fileType
							}
						]
					}
				]
			}),
			//, fileList: [data]
			contDivCd: CHAT_MESSAGE_CONTENTS_TYPE.TEXT,
			msgStatusCd: CHAT_MESSAGE_STATUS.SENT
		};
		if (receiver !== undefined) {
			message.receiver = receiver;
		}
		htClient._client.send(htClient._wsDefaultDestinationPath + (receiver === undefined ? 'message' : 'whisper'), {}, JSON.stringify(message));
	} else {
		console.error('data: ', data, 'receiver: ', receiver);
	}
};
// ////////////////////////////////////////////////////////////////////////////
// 메세지 이벤트 콜백
// ////////////////////////////////////////////////////////////////////////////
/**
 * '상담 채팅방' 구독 소켓에서 메세지 받을 경우
 *
 * @param payload StompMessage
 */
Client.prototype.onPublicMessageReceived = function(payload) {

	var message = JSON.parse(payload.body);
	console.info('=== CHAT MESSAGE', message.type);
	console.info('=== MESSAGE', message);
	if (this._chatRoom === null) {
		console.info('NO CURRENT CHAT ROOM');
		return;
	}

	// 현재 열려있는 채팅방에서 온 메세지가 아닌 경우 무시
	if (this._chatRoom.getId() !== message.chatRoomUid) {
		console.error('NOT YOUR MESSAGE: ', 'CHATROOM: ', message, 'message.chatRoomUid', message.chatRoomUid);
		return;
	}

	// 채팅방 입장 메세지
	if (message.type === CHAT_MESSAGE_TYPE.JOIN) {

		if (message.senderUid === htUser.getId()) { // 자신이 JOIN 했을 때 메세지 목록을 로드

			// 이미 조인한 경우 다른 클라이언트에서 접속으로 간주
			if (this.getChatRoom().isReceiveJoinMessage()) {
				console.warn('ANOTHER CLIENT WITH SAME ACCOUNT');
				this.getChatRoom().setReceiveJoinMessage(true);
				return;
			}

			this.getChatRoom().setReceiveJoinMessage(true);

			// 이전 메세지 목록 요청이 있으면 취소
			if (this._requestChatList) {
				try {
					this._requestChatList.abort();
				} catch(e) {
					console.error(e);
				}
				this._requestChatList = null;
			}

			// 메세지 목록 로드
			this._requestChatList = this._chatRoom.loadChatMessageList(CHAT_MESSAGE_LIST_SIZE);

		} else { // 대화 상대가 JOIN 했을 때, 활동 상태 발행
			this.sendActive(htBrowserActive ? USER_ACTIVE_STATUS.ACTIVE : USER_ACTIVE_STATUS.IDLE);
		}

	}

	// 채팅방 종료 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.END) {

		// 종료 메세지
		this._chatRoom.addChatMessage(message, true, true, 'scroll');

		// 고객일 경우, 채팅방 가입 해지
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) {
			this.unsubscribeChatRoom();
		}

		// 종료
		this._chatRoom.endChatRoom();

		// 상담직원일 경우, 후처리 팝업
		if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
			if ($('.btn_end_modify').length === 1) {
				//$('.btn_end_modify').trigger('click');
				//$('.btn_end_modify').trigger('click');		//종료후 자동 후처리 팝업 실행시 닫기버튼 삭제
			}
		}

	}

	// '채팅 메세지 상태' 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.STATUS) {

		// 상대가 보낸 상태 메세지만 처리
		if (message.senderUid === htUser.getId()) {
			return;
		}

		console.debug('STATUS: ', message.msgStatusCd);

		if (message.msgStatusCd === CHAT_MESSAGE_STATUS.READ) { // 읽음 메세지
			this._chatRoom.setMessageAsRead(message.chatNum);
		} else if (message.msgStatusCd === CHAT_MESSAGE_STATUS.RECEIVED) { // 받음 메세지
			// TODO: 클라이언트에서 메세지 큐를 관리 할 경우
		} else {
			console.error('WRONG CHAT MESSAGE STATUS: ', message.msgStatusCd);
		}

	}

	// 명령 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.COMMAND) {

		console.warn('DEPRECATED');
//		console.debug('=== COMMAND: ', message.command, 'RETURN CODE: ', message.returnCode);
//		if (message.command === CHAT_COMMAND.REQUEST_COUNSELOR) { // 상담직원 연결 요청
//			if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객일 경우
//				if (message.returnCode !== CHAT_COMMAND_RETURN_CODE.SUCCEED) {
//					alert(message.returnCode + ': ' + message.cont);
//				}
//			} else { // 상담직원, 매니저인 경우
//				;
//			}
//		}

	}

	// 상담직원 배정 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.ASSIGN) {

		console.warn('TODO: 배정 변경 확인');
		this._chatRoom._memberUid = message.assign;
		this._chatRoom._chatRoomStatusCd = CHAT_ROOM_STATUS.ASSIGN_COUNSELOR;
		this._chatRoom._chatRoomStatusNm = CHAT_ROOM_STATUS_TITLE_FOR_META_INFO.ASSIGN_COUNSELOR;
		this._chatRoom.chatAreaSetting(this._chatRoom);
//		
//		$.ajax({
//			// url: HT_APP_PATH + '/api/customer/' + htClient.getChatRoom()._cstmUid
//			url: HT_APP_PATH + '/api/chat/room/' + message.chatRoomUid
//		}).done(function(data) {
//			console.log("==chatroom status update== ("+ data.chatRoomStatusCd+")");
//			
//			self.getChatRoom().setStatus(data.chatRoomStatusCd);
//			self.getChatRoom().chatAreaSetting(htClient.getChatRoom())
//		}).fail(function(jqXHR, textStatus, errorThrown) {
//			console.error('FAILED GET CHATROOM', chatRoomUid);
//		});

	}

	// 채팅 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.MESSAGE) {

		// 채팅방 연결 후 기존 채팅 내용 로드(ChatRoom.loadChatMessageList) 중에
		// 받은 메세지를 임시 큐에 저장
		// ChatRoom.loadChatMessageList 마지막에 한 번에 처리함
		if (this._chatRoom.isLoadingChatMessageList()) {
			console.debug('=== ADD CHAT MESSAGE INTO TEMPORARY LIST');
			this._chatRoom._tmpChatMessageList.push(message);
		} else {
			this._chatRoom.addChatMessage(message, true, false, message.senderUid === htUser.getId() ? 'scroll' : 'auto');
			///강제처리
			if(message.senderDivCd === CHAT_MEMBER_TYPE.CUSTOMER){
				$(".no_read").hide();	//안읽음 모두 읽음처리 - 고객이 메세지를 보냈을 경우
				$('.state_customer', this._$chatControlContainer).attr('class', '').addClass('state_customer').addClass("active");		/// 고객아이콘 역시 활성상태로 강제 전환
				console.log("=================== 고객전송 ================== ")
			}

		}

		// 메세지 상태 전송 (CHAT_MESSAGE_STATUS)
		if (message.senderUid !== htUser.getId()) { // 상대가 보낸 메세지에만 상태 메세지 전송
			if (message.msgStatusCd === CHAT_MESSAGE_STATUS.SENT) {
				if (htBrowserActive) {
					this.sendStatus(CHAT_MESSAGE_STATUS.READ, message);
				} else {
					this.sendStatus(CHAT_MESSAGE_STATUS.RECEIVED, message);
				}
			} else {
				console.error('WRONG CHAT MESSAGE STATUS: ', message.msgStatusCd);
			}
		}

		// 상담하기 페이지에서, 고객 메세지가 '인증/개인정보 완료'인 경우 고객 정보 업데이트
		if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR
				&& message.senderDivCd === CHAT_MEMBER_TYPE.CUSTOMER) {

			var extra = htUtils.getFirstExtra(message.cont);
			if (extra && extra.indexOf('HappyTalk/CompleteCustomerAuth') === 0) {
				var self = this;
				$.ajax({
					// url: HT_APP_PATH + '/api/customer/' + htClient.getChatRoom()._cstmUid
					url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId()
				}).done(function(data) {
					console.info('=== GET CUSTOMER INFO\n%o', data);
					self.getChatRoom().setCustomerInfo(data);
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: LOAD CUSTOMER INFO: ', textStatus);
				});
			}
		}
	}

	// '채팅 메세지 수정' 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.EDIT) {

		if (htUser.getRollType() !== CHAT_ROLL_TYPE.CUSTOMER) {
			this._chatRoom.editMessage(message);
		}
	}

	// '채팅 메세지 삭제' 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.REMOVE) {

		if (htUser.getRollType() !== CHAT_ROLL_TYPE.CUSTOMER) {
			this._chatRoom.removeMessage(message);
		}
	}

	// '유저 활동 상태' 메세지
	else if (message.type === CHAT_MESSAGE_TYPE.ACTIVE) {

		if (message.senderUid !== htUser.getId()) { // 상대가 보낸 상태 메세지만 처리
			console.debug(message.senderUid + ' is ' + message.userActiveStatus);
			this._chatRoom.setUserActiveStatus(message.userActiveStatus);
		}
	}

	else {
		console.error('UNKNOWN MESSAGE TYPE: ', message.type);
	}
};
/**
 * '개인 메세지' 구독 소켓에서 메세지 받을 경우
 *
 * @param payload StompMessage
 */
Client.prototype.onWhisperMessageReceived = function(payload) {

	var message = JSON.parse(payload.body);
	console.info('=== WHISPER MESSAGE');
	console.info(message);
	// 채팅 메세지(서블릿: domain.ChatMessage) 포맷일 경우
	if (message.signature === 'ChatMessage') {

		// 상담직원페이지에서 메모 받을 경우 알람으로 표시
		if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
			console.info('=== MEMO MESSAGE', JSON.parse(message.cont));

			if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR
					&& message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO
					&& htUser.getId() !== message.senderUid
					&& htUser.getUserType() === CHAT_MEMBER_TYPE.COUNSELOR // 상담직원만
					// && message.senderDivCd === CHAT_ROLL_TYPE.MANAGER // 매니저가 보낸 메세지만
			) {
				// 내용 파싱
				var jsonObjectContents = JSON.parse(message.cont);
				try {
					var firstBalloonSection = jsonObjectContents.balloons[0].sections[0];
					if (firstBalloonSection.type === MESSAGE_SECTION_TYPE.TEXT) {
						htUtils.noti(firstBalloonSection.data);
						this.onPublicMessageReceived(payload);
						return;
					}
				} catch (e) {
					console.error(e);
					return;
				}
			}
		}

		// 매니저페이지에서 메모 받을 경우 알람으로 표시
		if (htUser.getRollType() === CHAT_ROLL_TYPE.MANAGER
				&& message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO
				&& htUser.getId() !== message.senderUid) {

			// 내용 파싱
			var jsonObject = JSON.parse(message.cont);
			try {
				var firstSection = jsonObject.balloons[0].sections[0];
				if (firstSection.type === MESSAGE_SECTION_TYPE.TEXT) {
					// 채팅방 상태별 카운터 업데이트 (변경, 검토 요청 업데이트)
					this._chatRoomList.getStatusCounter(htUser.getUserType());
					if (firstSection.data.indexOf('상담직원 변경 요청') > -1) {
						setTimeout(function() {
							if (confirm('상담직원 변경요청이 접수되었습니다.\n [확인]클릭 시 상세 확인 페이지로 이동합니다.')) {
								location.href = HT_APP_PATH + "/chatManage/selectStatusRoomList?schRoomStatus=cnsrChange";
							}
						}, 500);
						return;
					} else if (firstSection.data.indexOf('매니저 상담 요청') > -1) {
						setTimeout(function() {
							if (confirm('매니저 상담요청이 접수되었습니다.\n [확인]클릭 시 상세 확인 페이지로 이동합니다.')) {
								location.href = HT_APP_PATH + "/chatManage/selectStatusRoomList?schRoomStatus=managerChange";
							}
						}, 500);
						return;
					} else if (firstSection.data.indexOf('검토 요청') > -1) {
						setTimeout(function() {
							if (confirm('상담내용 검토요청이 접수되었습니다.\n [확인]클릭 시 상세 확인 페이지로 이동합니다.')) {
								location.href = HT_APP_PATH + "/chatManage/selectStatusRoomList?schRoomStatus=contReview";
							}
						}, 500);
						return;
					} else if (firstSection.data.indexOf('코끼리 등록 요청') > -1) {
						setTimeout(function() {
							if (confirm('코끼리 등록요청이 접수되었습니다.\n [확인]클릭 시 상세 확인 페이지로 이동합니다.')) {
								location.href = HT_APP_PATH + "/chatManage/selectStatusRoomList?schRoomStatus=cstmGrade";
							}
						}, 500);
						return;
					} else if (firstSection.data.indexOf('메모:') > -1) {
						console.info('SKIP MEMO', firstSection.data);
						return;
					} else {
						htUtils.noti(firstSection.data);
						return;
					}
				} else {
					console.error('INVALID MEMO FORMAT');
					return;
				}
			} catch (e) {
				console.error(e);
				return;
			}
		}

		this.onPublicMessageReceived(payload);
	}

	// 알림 메세지(서블릿: domain.NoticeMessage) 포맷일 경우
	else if (message.signature === 'NoticeMessage') {
		this.onNoticeMessageReceived(payload);
	}

	else {
		console.error('INVALID MESSAGE SIGNATURE');
	}
};
/**
 *  '전역설정 변경, 알람, 공지 등' 구독 소켓에서 메세지 받을 경우
 *
 * @param payload StompMessage
 */
Client.prototype.onNoticeMessageReceived = function(payload) {
	var message = JSON.parse(payload.body);
	console.info('=== NOTICE MESSAGE', message.type);
	console.info('=== MESSAGE', message);
	// '채팅방 목록' 메세지
	if (message.type === NOTICE_MESSAGE_TYPE.CHATROOM) {
		console.info('=== NOTICE CHATROOM MESSAGE', message.type);

		// ////////////////////////////////////////////////////////////////////
		// 상담직원 페이지
		if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
			var chatRoom = this._chatRoomList.getChatRoom(message.chatRoom.chatRoomUid);
			if (chatRoom) { // 목록에 있는 채팅방일 경우
				console.info('=== CHATROOM EXIST');
				if (htUser.getSetting().auto_mat_use_yn === 'Y') { // 자동 배정
					console.info('=== AUTO ASSIGN');
					if (message.chatRoom.memberUid === htUser.getId()) { // 자신에게 배정된 채팅방
						// 채팅방 상태별 카운터 업데이트
						this._chatRoomList.getStatusCounter();
						// 채팅방 업데이트
						console.info('=== UPDATE CHATROOM', message.chatRoom);
						this._chatRoomList.updateChatRoom(chatRoom, message.chatRoom);
						
					} else {
						// 채팅방 상태별 카운터 업데이트
						this._chatRoomList.getStatusCounter();
						// 삭제 대상
						console.info('=== DELETE CHATROOM', message.chatRoom);
						if (this._chatRoom && this._chatRoom.getId() === message.chatRoom.chatRoomUid) {
							// 열려있는 채팅방일 경우 닫기
							this._chatRoom.closeChatRoom(message.chatRoom.chatRoomUid);
						}
						this._chatRoomList.removeChatRoom(message.chatRoom.chatRoomUid);
						//console.debug(this._chatRoom, this._chatRoom.getId(), message.chatRoom.chatRoomUid);
						
					}
				} else { // 수동 배정
					console.info('=== MANUAL ASSIGN');
					if (message.chatRoom.memberUid === htUser.getId() // 자신에게 배정된 채팅방
							|| message.chatRoom.memberUid === HT_DEFAULT_ASSIGN_MEMBER_UID) { // 미배정 채팅방
						// 채팅방 업데이트
						console.info('=== UPDATE CHATROOM', message.chatRoom);
						this._chatRoomList.updateChatRoom(chatRoom, message.chatRoom);
						// 채팅방 상태별 카운터 업데이트
						this._chatRoomList.getStatusCounter();
					} else {
						console.debug('=== DELETE CHATROOM', message.chatRoom);
						// 채팅방 상태별 카운터 업데이트
						this._chatRoomList.getStatusCounter();
					}
				}
			} else { // 목록에 없는 채팅방일 경우
				console.info('=== NEW CHATROOM');
				// 자동 배정인 경우
				if (htUser.getSetting().auto_mat_use_yn === 'Y') {
					console.info('=== AUTO ASSIGN');
					if (message.chatRoom.memberUid === htUser.getId()) { // 자신에게 배정된 채팅방
						// 채팅방 목록에 추가
						console.info('=== ADD NEW CHATROOM: ', message.chatRoom);
	//					this._chatRoomList.addChatRoom(message.chatRoom);
						// 채팅방 상태별 카운터 업데이트
						this._chatRoomList.getStatusCounter();
					} else {
						console.info('=== SKIP CHATROOM: ', message.chatRoom)
					}
				}

				// 수동 배정인 경우
				if (htUser.getSetting().auto_mat_use_yn === 'N') {
					console.info('=== MANUAL ASSIGN');
					// 자신에게 할당된 채팅방이면 목록에 추가
					if (message.chatRoom.memberUid === htUser.getId() // 자신에게 배정된 채팅방
							|| message.chatRoom.memberUid === HT_DEFAULT_ASSIGN_MEMBER_UID) { // 미배정 채팅방
						// 채팅방 목록에 추가
						console.info('=== ADD NEW CHATROOM: ', message.chatRoom);
	//					this._chatRoomList.addChatRoom(message.chatRoom);
						// 채팅방 상태별 카운터 업데이트
						this._chatRoomList.getStatusCounter();
					} else {
						console.info('=== SKIP CHATROOM: ', message.chatRoom);
					}
				}
			}

			// IPCC_MCH 후처리 입력 알림 팝업 추가
			if (message.chatRoom.chatRoomStatusCd === "91" && !message.chatRoom.endCtgCd1) {
				htUtils.alert('채팅상담이 종료되었습니다.<br/>후처리 입력 버튼을 눌러 상담이력을 등록해주세요');
			}
		}
		
		// ////////////////////////////////////////////////////////////////////
		// 매니저 페이지
		else if (htUser.getRollType() === CHAT_ROLL_TYPE.MANAGER) {
			console.debug(message.chatRoom.managerUid === htUser.getId(), message.chatRoom.managerUid, htUser.getId());
			// 사용자가 매니저인 경우
			if (htUser.getUserType() === CHAT_MEMBER_TYPE.MANAGER) {
				console.debug('message.chatRoom.chatRoomStatusCd: ', message.chatRoom.chatRoomStatusCd, CHAT_ROOM_STATUS.ASSIGN_ROBOT);
				if (message.chatRoom.chatRoomStatusCd === CHAT_ROOM_STATUS.ASSIGN_ROBOT // 봇 상담
						|| message.chatRoom.chatRoomStatusCd === CHAT_ROOM_STATUS.WAIT_COUNSELOR // 미배정
						|| message.chatRoom.chatRoomStatusCd === CHAT_ROOM_STATUS.END_COUNSELOR // 종료
						|| message.chatRoom.managerUid === htUser.getId()) { // 자신에게 배정된 채팅방일 경우
					// 채팅방 상태별 카운터 업데이트
					if (this._chatRoomList) {
						this._chatRoomList.getStatusCounter(htUser.getUserType());
					} else {
						console.error('=== NO STATUS COUNTER AREA');
					}
				} else {
					console.info('=== SKIP CHATROOM: ', message.chatRoom)
				}
			}
			// 사용자가 관리자인 경우
			else if (htUser.getUserType() === CHAT_MEMBER_TYPE.ADMIN
					|| htUser.getUserType() === CHAT_MEMBER_TYPE.SUPER) {
				// 채팅방 상태별 카운터 업데이트
				if (this._chatRoomList) {
					this._chatRoomList.getStatusCounter(htUser.getUserType());
				} else {
					console.error('=== NO STATUS COUNTER AREA');
				}
			} else {
				console.error('=== WHO ARE YOU?');
			}
		} else {
			console.info('=== MISS COUNSELOR ROLL, SKIP MESSAGE');
		}
	}

	// '환경 설정' 메세지
	else if (message.type === NOTICE_MESSAGE_TYPE.SETTING) {

		console.info('=== NOTICE SETTING MESSAGE', message.type);
		console.debug(message.setting);
		if (htUser && htUser.getSetting()) {
			htUser.setSetting(message.setting);
		}
	}

	// '템플릿 태그 단어 목록' 메세지
	else if (message.type === NOTICE_MESSAGE_TYPE.HIGHLIGHT) {

		console.info('=== NOTICE HIGHLIGHT MESSAGE', message.type);

		// 태그 단어 저장
		$.ajax({
			url: HT_APP_PATH + '/api/highlight/' + htUser.getId()
		}).done(function(data) {
			htHighlight = data;
			console.debug('HIGHLIGHT WORDS', htHighlight);
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: LOAD HIGHLIGHT', textStatus);
		});
	}

	else {
		console.error('=== UNKNOWN MESSAGE TYPE: ', message.type);
	}
};
// ////////////////////////////////////////////////////////////////////////////
// 기타
// ////////////////////////////////////////////////////////////////////////////
/**
 * 고객: 종료된 채팅방 한 줄 평가
 */
Client.prototype.reviewChatRoom = function(star, statement) {

	this._chatRoom.reviewChatRoom(star, statement);
};
