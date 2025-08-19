'use strict';

var MAX_HIGHLIGHT_SIZE = 20;

// ////////////////////////////////////////////////////////////////////////////
// ChatRoom Class
// ////////////////////////////////////////////////////////////////////////////
/**
 * 채팅룸
 *
 * @param chatRoom JsonObject
 * @returns
 */
function ChatRoom(chatRoom) {

	console.debug('CHATROOM (JSON OBJECT)', chatRoom);

	this._chatRoomUid = chatRoom.chatRoomUid;
	this._roomTitle = chatRoom.roomTitle;
	this._roomCocId = chatRoom.roomCocId;
	this._roomCocNm = chatRoom.roomCocNm;
	this._cstmUid = chatRoom.cstmUid;
	this._cstmName = chatRoom.cstmName;
	this._cstmCocId = chatRoom.cstmCocId;
	this._cstmCocNm = chatRoom.cstmCocNm;
	this._cstmDivCd = chatRoom.cstmDivCd;
	this._cstmOsDivCd = chatRoom.cstmOsDivCd;
	this._cstmLinkDivCd = chatRoom.cstmLinkDivCd;
	this._cstmLinkCustomerUid = chatRoom.cstmLinkCustomerUid;
	this._cstmCocTypeNm = chatRoom.cstmCocTypeNm;
	this._memberUid = chatRoom.memberUid;
	this._managerUid = chatRoom.managerUid;
	this._chatRoomStatusCd = chatRoom.chatRoomStatusCd;
	this._frtCnsrDivCd = chatRoom.frtCnsrDivCd;
	this._cnsrDivCd = chatRoom.cnsrDivCd;
	this._cnsrLinkDt = chatRoom.cnsrLinkDt;
	this._senderDivCd = chatRoom.senderDivCd;
	this._ctgNum = chatRoom.ctgNum || '';
	this._ctgNm1 = chatRoom.ctgNm1 || '';
	this._ctgNm2 = chatRoom.ctgNm2 || '';
	this._ctgNm3 = chatRoom.ctgNm3 || '';
	this._roomCreateDt = chatRoom.roomCreateDt;
	this._roomCreateDtPretty = chatRoom.roomCreateDtPretty;
	this._passHours = chatRoom.passHours;
	this._passMinutes = chatRoom.passMinutes;
	this._markImgClassName = chatRoom.markImgClassName;
	this._markDesc = chatRoom.markDesc;
	this._gradeNm = chatRoom.gradeNm;
	this._gradeMemo = chatRoom.gradeMemo;
	this._gradeImgClassName = chatRoom.gradeImgClassName;
	this._managerCounselImgClassName = chatRoom.managerCounselImgClassName;
	this._evlScore = chatRoom.evlScore;
	this._evlCont = chatRoom.evlCont;
	this._endCtgCd = chatRoom.endCtgCd;
	this._chatMessageList = chatRoom.chatMessageList || [];
	this._viewer = null;
	this._socketChannel = null;
	this._receiveJoinMessage = false;

	// 채팅방 목록에서만 사용
	this._lastChatNum = chatRoom.lastChatNum;
	this._lastChatCont = chatRoom.lastChatCont;
	this._lastChatDtPretty = chatRoom.lastChatDtPretty;

	// 임시 채팅 목록 (초기 채팅 목록 로드시 들어오는 메세지 저장)
	this._tmpChatMessageList = [];
	this._loadingChatMessageList = true;
	this._loadFirstMessage = false;

	/*
	 * IPCC_MCH ARS 채널 추가 관련 부서 코드 추가
	 */
	this._departCd = chatRoom.departCd;
	console.debug(this);
}
ChatRoom.prototype.getId = function() {

	return this._chatRoomUid;
};
ChatRoom.prototype.getTitle = function() {

	return this._roomTitle;
};
ChatRoom.prototype.getCustomerId = function() {

	return this._cstmUid;
};
ChatRoom.prototype.getStatus = function() {

	return this._chatRoomStatusCd;
};
ChatRoom.prototype.setStatus = function(chatRoomStatusCd) {

	this._chatRoomStatusCd = chatRoomStatusCd;
};
ChatRoom.prototype.getCustomerGradeName = function() {

	return this._gradeNm;
};
ChatRoom.prototype.getSenderType = function() {

	return this._senderDivCd;
};
ChatRoom.prototype.getCnsrDivCd = function() {

	return this._cnsrDivCd;
};
ChatRoom.prototype.setCnsrDivCd = function(cnsrDivCd) {

	this._cnsrDivCd = cnsrDivCd;
};
ChatRoom.prototype.getViewer = function() {

	return this._viewer;
};
ChatRoom.prototype.setViewer = function(viewer) {

	this._viewer = viewer;
};
ChatRoom.prototype.getSocketChannel = function() {

	return this._socketChannel;
};
ChatRoom.prototype.setSocketChannel = function(socketChannel) {

	this._socketChannel = socketChannel;
};
ChatRoom.prototype.isReceiveJoinMessage = function() {

	return this._receiveJoinMessage;
};
ChatRoom.prototype.setReceiveJoinMessage = function(receiveJoinMessage) {

	this._receiveJoinMessage = receiveJoinMessage;
};
ChatRoom.prototype.isLoadingChatMessageList = function() {

	return this._loadingChatMessageList;
};
/**
 * 상담직원이 채팅방 접수
 */
ChatRoom.prototype.submitChatRoom = function() {

	$.ajax({
		url: HT_APP_PATH + '/api/chat/room/' + this._chatRoomUid,
		method: 'put',
		data: {
			command: CHAT_COMMAND.SUBMIT_CHATROOM,
			userId: htUser.getId(),
		},
	}).done(function(data, textStatus, jqXHR) {
		console.info(data);
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: SUBMIT CHATROOM: ', textStatus);
	}).always(function() {
		;
	});
};
var MAX_MESSAGE_LIST_SIZE = 100;
/**
 * 채팅방에 입장시(JOIN) 채팅 내용 로드
 */
ChatRoom.prototype.loadChatMessageList = function() {

	this._loadingChatMessageList = true;
	var self = this;
	var reqParams = {
			rollType: htUser.getRollType(),
			withChatMessageList: 'true',
			withMetaInfo: 'true',
//			ctgNum: this._ctgNum,
	};

//	if (this.getLastChatMessage()) { // 메세지 목록이 있을 경우 이후 메세지만 요청
//		reqParams.chatNumGt = this.getLastChatMessage().chatNum;
//	} else {
//		reqParams.size = MAX_MESSAGE_LIST_SIZE;
//	}
	reqParams.size = MAX_MESSAGE_LIST_SIZE;

	console.info('>>> LOAD CHAT ROOM WITH MESSAGE LIST');

	var requestChatList = $.ajax({
		url: HT_APP_PATH + '/api/chat/room/' + this._chatRoomUid,
		data: reqParams,
	}).done(function(data, textStatus, jqXHR) {

		console.debug('=== FINISHED REQUEST MESSAGE LIST');
		
		if (textStatus !== 'nocontent') {
			console.debug('reqParams: ', reqParams, 'data: ', data);
			console.assert(data);
			console.debug('data.chatMessageList: ', data.chatMessageList.length, 'htHighlight: ', htHighlight.length);
			console.time('loadMessage');
			if (!reqParams.chatNumGt) {
				console.assert(data.chatMessageList.length > 0, 'NO MESSAGE LIST');
			}

			// 채팅창 열기
			self._viewer.openChatRoom();

			// 메모 삭제
			if (self._viewer._$chatMemoViewerContainer.length === 1) {
				self._viewer.emptyMemoContainer();
				self._viewer.hideMemoContainer();
			}

			// 메타 정보 입력
			self._viewer.setMetaInfo(data);
			
			self._chatRoomStatusCd = data.chatRoomStatusCd;
			
			// 전체/기존 메세지 개수 (하이라이트 적용 범위 식별)
			var messageSize = data.chatMessageList.length;
			if (self._chatMessageList) {
				messageSize += self._chatMessageList.length;
			}
			
			//첫메세지가 시나리오 일 경우 상담직원연결 hide -- 원래 시스템이나 삼성증권은 바로 연결을 요구하여 주석처리
			if( (messageSize == 1) && (data.cnsrDivCd === CHAT_MEMBER_TYPE.ROBOT) ){
				$(".btn-connect").hide();
			}else{
				$(".btn-connect").show();
			}
			
			var printedMessageSize = 0;
			
			var prvDay = null;
			// 기존 메세지 목록 출력
			if (self.getLastChatMessage()) {
				self._chatMessageList.forEach(function(item) {
					if(item.cont === null || item.cont === "" || item.cont === undefined ){
						return;
					}else{
						if(prvDay){
							if(prvDay != item.regDay){
								var $messageElement = $('<div />').addClass('date-ymd-line');
								var $messageElement2 = $('<span />').addClass('day').text(prvDay)
								$messageElement.append($messageElement2)
								self._viewer._$chatMessageViewerContainer.prepend($messageElement);
							}
						}
						printedMessageSize++;
						self.addChatMessage(item, true, (messageSize - printedMessageSize) > MAX_HIGHLIGHT_SIZE, 'scroll');
						prvDay = item.regDay;
					}
				});
			}

			// 요청한 메세지 목록 추가 및 출력
			prvDay = null;
			data.chatMessageList.forEach(function(item) {
				if(item.cont === null || item.cont === "" || item.cont === undefined ){
					return;
				}else{
					if(prvDay){
						if(prvDay != item.regDay){
							var $messageElement = $('<div />').addClass('date-ymd-line');
							var $messageElement2 = $('<span />').addClass('day').text(prvDay)
							$messageElement.append($messageElement2)
							self._viewer._$chatMessageViewerContainer.prepend($messageElement);
						}
					}
					printedMessageSize++;
					self.addChatMessage(item, true, (messageSize - printedMessageSize) > MAX_HIGHLIGHT_SIZE, 'scroll');
					prvDay = item.regDay;
				}
			});

			// 임시 채팅 목록
			self._tmpChatMessageList.forEach(function(item) {
				self.addChatMessage(item, true, false, 'scroll');
			});

			self._tmpChatMessageList = [];
		}

		if (self.getStatus() === CHAT_ROOM_STATUS.END_COUNSELOR) { // 종료된 페이지
				//|| self.getStatus() === CHAT_ROOM_STATUS.END_ROBOT) {
			console.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>277 : " + this.getStatus());
			self._viewer.endChatRoom(self._evlScore, self._evlCont, self._cnsrDivCd, this.getStatus());
		} else {
			// 읽음 처리
			console.debug('lastChatMessage: ', self.getLastChatMessage());
			if (self.getLastChatMessage()
					&& self.getLastChatMessage().msgStatusCd !== CHAT_MESSAGE_STATUS.READ
					&& self.getLastChatMessage().snederUid !== htUser.getId()) {
				htClient.sendStatus(CHAT_MESSAGE_STATUS.READ, self.getLastChatMessage());
			}
		}

		if (htUtils.isDebug()) {
			try {
				self.validateChatMessageList(self._chatMessageList);
			} catch(e) {
				console.error(e);
			}
		}

		console.timeEnd('loadMessage');

		console.time('highLightMessage');
		self._viewer.highLightMessage();
		console.timeEnd('highLightMessage');

		// 챗봇과 대화중이 아니면 '상담직원 연결' 버튼 삭제
	/*	if (self._chatRoomStatusCd !== CHAT_ROOM_STATUS.ASSIGN_ROBOT) {
			self._viewer.hideRequestCounselorButton();
		}*/
		
		//고객 채팅창일 경우 
		if(htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){
			self.chatAreaSetting(self);
		}

	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: LOAD CHAT LIST: ', textStatus);
	}).always(function() {
		console.info('=== FINISHED LOAD CHAT MESSAGE');
		self._loadingChatMessageList = false;
	});

	return requestChatList;
};

/**
 * 채팅 타입별 chat_bottom 
 **/
ChatRoom.prototype.chatAreaSetting = function (self){
	
	var chatRoomStatusCd = self._chatRoomStatusCd;
	var chatType = $('#chat_bottom').eq(0).children().eq(0);
	var textArea = $('#chat_bottom .inner textarea').eq(0);
	var btnClass = $('#chat_bottom .inner .btn_send_area a').eq(0);
	
	if (chatRoomStatusCd == CHAT_ROOM_STATUS.ASSIGN_ROBOT) {// 챗봇 상담 중
		 chatType.attr('class','qna-line-wrap type-on');
		 textArea.attr('placeholder','위 항목을 선택해 주세요.').attr('data-placeholder','위 항목을 선택해 주세요.').attr('readOnly',true);
		 btnClass.attr('title','상담직원연결').text('상담직원채팅').removeClass('endChat');
	 } else if (chatRoomStatusCd == CHAT_ROOM_STATUS.WAIT_COUNSELOR) {
		 chatType.attr('class','qna-line-wrap type-on');
		 textArea.attr('placeholder','연결을 기다려 주세요.').attr('data-placeholder','연결을 기다려 주세요.').attr('readOnly',true);
		 btnClass.attr('title','상담 종료').text('상담 종료').addClass('endChat');
	 } else if (chatRoomStatusCd == CHAT_ROOM_STATUS.ASSIGN_COUNSELOR || chatRoomStatusCd == CHAT_ROOM_STATUS.WAIT_REPLY || chatRoomStatusCd == CHAT_ROOM_STATUS.NEED_ANSWER) {
		 chatType.attr('class','qna-line-wrap type-ing');
		 textArea.attr('placeholder','궁금한 내용을 질문해 주세요.').attr('data-placeholder','궁금한 내용을 질문해 주세요.').attr('readOnly',false);
		 btnClass.attr('title','메세지 보내기').text('메세지 보내기').removeClass('endChat');
	 } else if(chatRoomStatusCd == CHAT_ROOM_STATUS.END_COUNSELOR) {
		 chatType.attr('class','qna-line-wrap type-on');
	 }else{
		 textArea.attr('placeholder','궁금한 내용을 질문해 주세요.').attr('data-placeholder','궁금한 내용을 질문해 주세요.').attr('readOnly',false);
	 }
};
/**
 * 채팅 목록 유효성 검사
 *
 * @param chatMessageList JSONObject[]
 */
ChatRoom.prototype.validateChatMessageList = function(chatMessageList) {

	var result = true;
	for (var i = 1; chatMessageList.length > i; i++) {
		console.assert(chatMessageList[i - 1].chatNum < chatMessageList[i].chatNum);
		if (chatMessageList[i - 1].chatNum >= chatMessageList[i].chatNum) {
			console.error(chatMessageList[i - 1]);
			console.error(chatMessageList[i]);
			throw {code: 'ASSERT ERROR', message: 'MESSAGE LIST HAS WRONG ORDER'};
		}
	}
};
/**
 * 채팅방에 채팅 내용 로드 (이전 채팅 내용)
 */
ChatRoom.prototype.loadPreviousChatMessageList = function() {

	if (this._chatMessageList.length === 0) {
		console.info('NO PREVIOUS CHAT MESSAGE LIST', this._chatMessageList);
		return;
	}

	var self = this;
	var reqParams = {
			rollType: htUser.getRollType(),
			withChatMessageList: 'true',
			withMetaInfo: 'true',
//			ctgNum: this._ctgNum,
			chatNumLt: this._chatMessageList[0].chatNum,
			size: MAX_MESSAGE_LIST_SIZE,
	};

	console.info('>>> LOAD CHAT ROOM WITH PREVIOUS MESSAGE LIST');
	if (this._loadFirstMessage) {
		return false;
	}

	// 원래 스크롤 위치
//	var $firstMessageElement = this._viewer.getFirstMessageElement();
//	console.debug('$firstMessageElement: ', $firstMessageElement, 'height: ', $firstMessageElement.height());

	var requestChatList = $.ajax({
		url: HT_APP_PATH + '/api/chat/room/' + this._chatRoomUid,
		data: reqParams,
	}).done(function(data, textStatus, jqXHR) {

		console.debug('=== FINISHED REQUEST MESSAGE LIST');
		
		if (textStatus !== 'nocontent') {
			console.info('reqParams: ', reqParams, 'data: ', data);
			console.assert(data);
			console.info('data.chatMessageList: ', data.chatMessageList.length, 'htHighlight: ', htHighlight.length);
			if (data.chatMessageList.length === 0) {
				self._loadFirstMessage = true;
			}
			console.time('loadMessage');

			// 전체/기존 메세지 개수 (하이라이트 적용 범위 식별)
			var messageSize = data.chatMessageList.length;
			if (self._chatMessageList) {
				messageSize += self._chatMessageList.length;
			}
			var printedMessageSize = 0;

			// 기존 메세지 목록 출력
//			if (self.getLastChatMessage()) {
//				self._chatMessageList.forEach(function(item) {
//					printedMessageSize++;
//					self.addChatMessage(item, true, (messageSize - printedMessageSize) > MAX_HIGHLIGHT_SIZE, 'auto');
//				});
//			}
			/*선언 - koh */
			var msgCnt = $('.message_box').length;
			var msgScrollHeight = 0;
			var i=0;
			/*선언 - koh*/

			// 요청한 메세지 목록 추가 및 출력
			data.chatMessageList.forEach(function(item) {
				if(item.cont ==null){
					return;
				}
				printedMessageSize++;
				self.addChatMessage(item, true, (messageSize - printedMessageSize) > MAX_HIGHLIGHT_SIZE, 'auto');
				//$('#chat_body').scrollTop(242);
			});

			/*처리 - koh */
			msgCnt = $('.message_box').length - msgCnt;
			for (i=0;i<msgCnt;i++){
				//alert($($('.message_box')[i]).height())
				msgScrollHeight += $($('.message_box')[i]).height();
			}
			$('#chat_body').scrollTop(msgScrollHeight);
			msgScrollHeight = 0;
			/*처리 - koh */

			// 이전 스크롤위치로 스크롤
//			console.debug('END PREPEND');
//			console.debug('$firstMessageElement: ', $firstMessageElement, 'height: ', $firstMessageElement.height());
//			self._viewer.setScrollPosition($firstMessageElement);

		}

		console.timeEnd('loadMessage');

		console.time('highLightMessage');
		//setTimeout(function() {
		self._viewer.highLightMessage();
		//}, 3000);
		console.timeEnd('highLightMessage');

	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: LOAD PREVIOUS CHAT LIST: ', textStatus);
	}).always(function() {
		console.info('=== FINISHED LOAD PREVIOUS CHAT MESSAGE');
	});

	return requestChatList;
};
/**
 * 메세지 추가 및 뷰 클래스 호출
 *
 * @param chatMessage JsonObject
 * @param isInit Boolean 새 채팅방 (재진입 포함)
 * @param skipHighLight Boolean
 * @param scroll String
 */
ChatRoom.prototype.addChatMessage = function(chatMessage, isInit, skipHighLight, scroll) {

	console.debug('=== PUSH START: ', this._chatMessageList.length);

	var findChatMessage = false; // 리스트에 메세지가 있는 판별
	var index = 0; // 메세지가 추가될 위치

	for (; this._chatMessageList.length > index; index++) {
		var item = this._chatMessageList[index];
		if (chatMessage.chatNum === item.chatNum) {
			findChatMessage = true;
		} else if (chatMessage.chatNum < item.chatNum) {
			break;
		}
	}

	if (findChatMessage === false) {
		if (index === this._chatMessageList.length) {
			this._chatMessageList.push(chatMessage);
		} else {
			this._chatMessageList.splice(index, 0, chatMessage);
		}

		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
			if (this._cnsrDivCd === CHAT_MEMBER_TYPE.ROBOT // 로봇 배정
					&& chatMessage.senderDivCd === CHAT_MEMBER_TYPE.COUNSELOR) { // 상담직원 메세지
				this._cnsrDivCd = chatMessage.senderDivCd;
			}
		}
		// 뷰 업데이트
		this._viewer.addChatMessage(chatMessage, skipHighLight, scroll);
	} else {
		if (isInit) {
			if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
				if (this._cnsrDivCd === CHAT_MEMBER_TYPE.ROBOT // 로봇 배정
						&& chatMessage.senderDivCd === CHAT_MEMBER_TYPE.COUNSELOR) { // 상담직원 메세지
					this._cnsrDivCd = chatMessage.senderDivCd;
				}
			}
			// 뷰 업데이트
			this._viewer.addChatMessage(chatMessage, skipHighLight, scroll);
		}
	}

	console.debug('=== PUSH RESULT: ', this._chatMessageList.map(function(i) {
			return i.chatNum;
		}).join(',')
	);
};
/**
 * 채팅 목록에서 채팅 메세지 검색
 *
 * @param chatMessage JsonObject
 */
ChatRoom.prototype.getChatMessage = function(chatMessage) {

	for (var i = 0; this._chatMessageList.length > i; i++) {
		var item = this._chatMessageList[i];
		if (chatMessage.chatNum === item.chatNum) {
			return item;
		}
	}

	return null;
};
/**
* 메모/메세지 수정
*
* @param message JsonObject
*/
ChatRoom.prototype.editMessage = function(message) {

	for (var i = 0; this._chatMessageList.length > i; i++) {
		var item = this._chatMessageList[i];
		if (message.chatNum === item.chatNum) {
			item.cont = message.cont;
			break;
		}
	}

	this._viewer.editMessage(message);
};
/**
* 메모/메세지 삭제
*
* @param message JsonObject
*/
ChatRoom.prototype.removeMessage = function(message) {

	for (var i = 0; this._chatMessageList.length > i; i++) {
		var item = this._chatMessageList[i];
		if (message.chatNum === item.chatNum) {
			this._chatMessageList.splice(i, 1);
			break;
		}
	}

	this._viewer.removeMessage(message);
};
/**
 * 첫번째 메세지 열람
 *
 * @return chatMessage JsonObject
 */
ChatRoom.prototype.getFirstChatMessage = function() {

	if (this._chatMessageList.length === 0) {
		return null;
	} else {
		return this._chatMessageList[0];
	}
};
/**
 * 마지막 메세지 열람
 *
 * @return chatMessage JsonObject
 */
ChatRoom.prototype.getLastChatMessage = function() {

	if (this._chatMessageList.length === 0) {
		return null;
	} else {
		return this._chatMessageList[this._chatMessageList.length - 1];
	}
};
/**
 * 메세지 읽음 처리
 *
 * 'chatNum'보다 작거나 같은 메세지를 읽음 처리
 *
 * @param chatNum String
 */
ChatRoom.prototype.setMessageAsRead = function(chatNum) {

	this._chatMessageList.forEach(function(chatMessage) {
		if (chatMessage.chatNum <= chatNum) {
			if (chatMessage.msgStatusCd !== CHAT_MESSAGE_STATUS.READ) {
				chatMessage.msgStatusCd = CHAT_MESSAGE_STATUS.READ;
			}
		}
	});

	// 뷰 업데이트
	this._viewer.setMessageAsRead(chatNum);
};
/**
 * 대화 상대 활동 상태 변경
 *
 * @param userActiveStatus USER_ACTIVE_STATUS
 */
ChatRoom.prototype.setUserActiveStatus = function(userActiveStatus) {

	// 뷰 업데이트
	this._viewer.setUserActiveStatus(userActiveStatus);
};
ChatRoom.prototype.endChatRoom = function() {
	console.info("********************************642 : " + this.getStatus());
	this._viewer.endChatRoom(this._evlScore, this._evlCont, this._cnsrDivCd, this.getStatus());
	this.setStatus(CHAT_ROOM_STATUS.END_COUNSELOR);
	
};
ChatRoom.prototype.reviewChatRoom = function(star, statement) {

	if (this.getStatus() === CHAT_ROOM_STATUS.END_COUNSELOR) {

		var self = this;
		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/review/' + this._chatRoomUid,
			type: 'post',
			data: {
				chatRoomUid: this.getId(),
				userId: htUser.getId(),
				star: parseInt(star),
				statement: statement,
			},
			dataType: 'json',
		}).done(function(data) {
			if (parseInt(data) === 1) {
				self._viewer.closeReviewArea();
			} else {
				console.error('FAIL REQUEST: REVIEW');
			}
		}).fail(function(jqXHR, textStatus, errorThrown) {

			console.error('FAIL REQUEST: REVIEW: ', textStatus);
		});
	} else {
		console.error('CANNOT REVIEW ON UN-FINISHED CHATROOM');
	}
};
/**
* 채팅방 닫기: 채팅방 해지, ChatRoomViewer.closeChatRoom 호출
*
 * @param chatRoomUid String
*/
ChatRoom.prototype.closeChatRoom = function(chatRoomUid) {

	console.info('=== CLOSE CHATROOM');

	// chatRoomUid 파라미터가 있을 경우 현재 채팅방이 맞는지 확인
	if (chatRoomUid && htClient.getChatRoom()
			&& htClient.getChatRoom().getId() !== chatRoomUid) {
		return;
	}

	// 가입 해지
	htClient.unsubscribeChatRoom();
	// 뷰어 닫기
	if (!chatRoomUid && this._viewer) {
		this._viewer.closeChatRoom();
	}

	$('.dev_mid_manual').show();
	$('.dev_mid_chat').hide();
};

// ////////////////////////////////////////////////////////////////////////////
// Called by ChatRoomList
// ////////////////////////////////////////////////////////////////////////////
/**
 * 채팅방 상태 변경
 *
 * @param JsonObject chatRoomMessage
 */
ChatRoom.prototype.changeChatRoomStatus = function(chatRoomMessage) {

	this._chatRoomStatusCd = chatRoomMessage.chatRoomStatusCd;
}
/**
 * 채팅방 마자막 메세지 관련 필드 (채팅방 목록 페이지에서만 사용)
 * 실제 메세지 목록(this._chatMessageList)을 사용하지 않고 채팅방 필드를 사용
 */
ChatRoom.prototype.getLastChatMessageOfChatRoomList = function() {

	console.debug(this);
	var cont = {
		chatNum: this._lastChatNum,
		cont: this._lastChatCont,
		regDt: this._lastChatDtPretty,
	};
	console.debug(cont);
	return cont;
};
/**
 * 채팅방 마자막 메세지 관련 필드 변경 (채팅방 목록 페이지에서만 사용)
 * 실제 메세지 목록(this._chatMessageList)은 변경하지 않고 채팅방 필드만 변경함
 *
 * @param chatRoomMessage JsonObject
 */
ChatRoom.prototype.changeLastChatMessageOfChatRoomList = function(chatRoomMessage) {

	this._lastChatNum = chatRoomMessage.lastChatNum;
	this._lastChatCont = chatRoomMessage.lastChatCont;
	this._lastChatDtPretty = chatRoomMessage.lastChatDtPretty;
};
/**
 * 채팅방 아이콘 관련 필드 변경 (채팅방 목록 페이지에서만 사용)
 *
 * @param chatRoomMessage JsonObject
 */
ChatRoom.prototype.changeChatRoomIcons = function(chatRoomMessage) {

	if (chatRoomMessage.roomCocNm) this._roomCocNm = chatRoomMessage.roomCocNm;
	if (chatRoomMessage.roomCocId) this._roomCocId = chatRoomMessage.roomCocId;
	this._passHours = chatRoomMessage.passHours;
	this._passMinutes = chatRoomMessage.passMinutes;
	this._markImgClassName = chatRoomMessage.markImgClassName;
	this._markDesc = chatRoomMessage.markDesc;
	this._gradeNm = chatRoomMessage.gradeNm;
	this._gradeMemo = chatRoomMessage.gradeMemo;
	this._gradeImgClassName = chatRoomMessage.gradeImgClassName;
};
/**
 * 채팅방 고객 정보 업데이트
 *
 * @param customerInfo JsonObject
 */
ChatRoom.prototype.setCustomerInfo = function(customerInfo) {

	this._viewer.setMetaInfo(customerInfo);
}
// ////////////////////////////////////////////////////////////////////////////
// ChatRoom Viewer
// ////////////////////////////////////////////////////////////////////////////
/**
 * 채팅룸 뷰어
 *
 * @param DOMElements
 * @returns
 */
function ChatRoomViewer(DOMElements) {

	this._$chatMessageViewerContainer = DOMElements.chatMessageViewerContainer;
	this._$chatControlContainer = DOMElements.chatControlContainer;
	this._$messageArea = DOMElements.messageArea;
	this._$messageSendButton = DOMElements.messageSendButton;
	this._$chatRoomTitleContainer = DOMElements.chatRoomTitleContainer;
	this._$chatRoomMetaInfoContainer = DOMElements.chatRoomMetaInfoContainer;
	this._userActiveStatus = USER_ACTIVE_STATUS.IDLE;
	this._swiper = null;
	this._sendingMessage = null;

	// 상담직원 채팅창
	this._$chatMemoViewerContainer = $('.counseling_mid .request_info .info_list');

	if (typeof autosize !== 'undefined') {
		autosize(this._$messageArea);
	}
	if (this._$messageArea) {
		this._defaultMessageAreaHeight = this._$messageArea.css('height');
	} else {
		return;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 메세지 전송, 엔터로 메세지 전송
	// 메세지 전송시 유저 활동 상태 변경 및 전송 포함
	var self = this;
	this._$messageArea.on('keyup', function(e) {

		e.preventDefault();
		console.debug(e.keyCode);

		// 자동완성 기능
		if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {

			if (e.keyCode === 27) { // ESC시 자동완성 레이어 가리기
				if ($('#autoCmpContent').is(':visible')) {
					$('#autoCmpContent').empty();
					$('#autoCmpContent').hide();
				}
			} else if (e.keyCode === 13) { // 엔터시 첫번째 자동완성 컨텐츠 선택
				if ($('#autoCmpContent').is(':visible')) {
					if ($('#autoCmpContent .auto_cmp').length > 0) {
						self._$messageArea.val($('#autoCmpContent .auto_cmp:eq(0)').text());
						$('#autoCmpContent').empty();
						$('#autoCmpContent').hide();
						return;
					}
				}
			} else {
				fn_autoCmp('autoCmpContent', self._$messageArea.val().trim());
			}
		}

		var contents = self._$messageArea.val().trim();

		if (self._sendingMessage) {
			return;
		}

		if (e.keyCode === 13) { // 엔터시 메세지 전송
			if ($('#check_enter_key').length === 1 // '엔터로 전송' 옵션이 있을 경우
					&& $('#check_enter_key').prop('checked') === false) { // 옵션에 따라 무시
				return;
			}

			if (contents && contents !== ''
				&& contents !== '@' && contents !== '#') {
				// 상담직원
				if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
					// 휴식(상담불가) 상태시 메세지 전송 금지
					// console.debug(htUser.getSetting().member_work_status_cd);
					// if (htUser.getSetting().member_work_status_cd === 'R') {
					// 	htUtils.alert('상담불가 상태 입니다.');
					// 	return;
					// }
				}
				
				// 매니저 상담시 메세지앞에 '매니저: ' 추가
				if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR
						&& htUser.getUserType() === CHAT_MEMBER_TYPE.MANAGER
						&& htUser.getId() !== htClient.getChatRoom()._memberUid
						&& $('#initialActions input[name=openChatRoomWithSystemMsg]').length === 1
						&& $('#initialActions input[name=openChatRoomWithSystemMsg]').val() === 'true') {
							contents = '매니저: ' + contents;
				}

				// 메세지 전송
				if (self._sendingMessage) {
					console.debug('SENDING MESSAGE');
					setTimeout(function() {self._sendingMessage = false}, 500);
					return;
				}
				self._sendingMessage = true;
				htClient.sendMessage(contents);
				self._sendingMessage = false;
				self._$messageArea.val('');
				self._$messageArea.css('height', self._defaultMessageAreaHeight);
				self.sendButtonOff();
				// 유저 활동 상태 변경 및 전송
				self._userActiveStatus = USER_ACTIVE_STATUS.ACTIVE;
				htClient.sendActive(self._userActiveStatus);

				// push msessage 전송 : 엔터 전송
				console.info(" >>>>>>>>>>>>>>> push enter >>>>>>>>>>>>>>>>>>")
				if($(".state_customer").hasClass("idle") === true){
					if(htClient.getChatRoom()._cstmLinkDivCd == "C" || htClient.getChatRoom()._cstmLinkDivCd == "D"){
						console.info(" >>>>>>>>>>>>>>> push >>>>>>>>>>>>>>>>>>")
						self.sendPushAlarm(htClient.getChatRoom()._cstmLinkDivCd, htClient.getChatRoom()._cstmCocId);
					}
				}		
				
				// 모바일인 경우, 입력창 포커스 아웃
				/*if (htUtils.isMobile()) {
					self._$messageArea.blur();
				}*/
			} else {
				self._$messageArea.val('');
				self._$messageArea.css('height', self._defaultMessageAreaHeight);
				self.sendButtonOff();
			}
		} else { // 유저 활동 상태만 변경 및 전송
			if (contents && contents !== ''	&& contents !== '@' && contents !== '#') { // 메세지 내용이 있을 경우
				if (self._userActiveStatus !== USER_ACTIVE_STATUS.TYPING) { // '타이핑중' 상태가 아니면 '타이핑중' 상태로 변경 및 전송
					self._userActiveStatus = USER_ACTIVE_STATUS.TYPING;
					htClient.sendActive(self._userActiveStatus);
				}
				self.sendButtonOn();
			} else { // 메세지 내용이 빈 값일 경우
				if (self._userActiveStatus === USER_ACTIVE_STATUS.TYPING) { // '타이핑증' 상태 이면 '활동중' 상태로 변경 및 전송
					self._userActiveStatus = USER_ACTIVE_STATUS.ACTIVE;
					htClient.sendActive(self._userActiveStatus);
				}
				self.sendButtonOff();
			}
		}

		// 스크롤 다운
		console.info('TRIGGER .to-the-down');
		$('.to-the-down').trigger('click');

	}).on('focus', function(e) { // 인풋창에 포커스시 스크롤 다운
		console.info('TRIGGER .to-the-down: ', e);
		$('.to-the-down').trigger('click');
	});

	// 버튼으로 메세지 전송
	this._$messageSendButton.on('click', function(e, extra) {
		e.preventDefault();
		var contents = self._$messageArea.val().trim();

		if ($('.layer_selected_template:visible', self._$chatControlContainer).length === 1) { // 템플릿 미리보기 있을 경우
			//var xxx =$('.layer_selected_template .type_text' ).html();
			var cmp =$('.layer_selected_template .left_area' ).html(); //자동완성과 구분
			if (typeof cmp == undefined) {
				alert(cmp);
				return;
			}
			// 템플릿 메세지 전송
			var tplNum = $('.layer_selected_template', self._$chatControlContainer).attr('data-tpl-num');

			$.ajax({
				url: HT_APP_PATH + '/chatCsnr/selectTemplateOne',
				method: 'get',
				data: {
					tplNum: tplNum,
				},
			}).done(function(data, textStatus, jqXHR) {

				// 메세지 전송
				if (self._sendingMessage) {
					console.debug('SENDING MESSAGE');
					setTimeout(function() {self._sendingMessage = false}, 500);
					return;
				}
				self._sendingMessage = true;
				htClient.sendMessage(data.template.reply_cont, true);
				self._sendingMessage = false;
				$('#chat_bottom .layer_selected_template').hide();

				// 상담직원 접수: 상담직원 첫 메세지 발행시 접수한 것으로 간주
//				if (htClient.getChatRoom()._cnsrLinkDt === null) {
//					htClient.getChatRoom().submitChatRoom();
//				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: TEMPLATE: ', textStatus);
			}).always(function() {
				;
			});
		} else if (contents && contents !== ''
			&& contents !== '@' && contents !== '#') {
			// 상담직원
			if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
				// 휴식(상담불가) 상태시 메세지 전송 금지
				// console.debug(htUser.getSetting().member_work_status_cd);
				// if (htUser.getSetting().member_work_status_cd === 'R') {
				// 	htUtils.alert('상담불가 상태 입니다.');
				// 	return;
				// }
			}

			// 매니저 상담시 메세지앞에 '매니저: ' 추가
			if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR
					&& htUser.getUserType() === CHAT_MEMBER_TYPE.MANAGER
					&& htUser.getId() !== htClient.getChatRoom()._memberUid
					&& $('#initialActions input[name=openChatRoomWithSystemMsg]').length === 1
					&& $('#initialActions input[name=openChatRoomWithSystemMsg]').val() === 'true') {
						contents = '매니저: ' + contents;
			}

			// 메세지 전송
			if (self._sendingMessage) {
				console.debug('SENDING MESSAGE');
				setTimeout(function() {self._sendingMessage = false}, 500);
				return;
			}
			self._sendingMessage = true;
			
			/**
			 * 마스킹 처리
			 */
			contents = maskingPhone(contents);		//핸드폰 번호 마스킹 처리 
			contents = maskingRrn(contents);		//주민번호 마스킹 처리
			
			htClient.sendMessage(contents);			//메세지 전송
			
			contents = "";							//전송 메세지 초기화
			
			self._sendingMessage = false;
			self._$messageArea.val('');
			self._$messageArea.css('height', self._defaultMessageAreaHeight);
			self.sendButtonOff();
			// 유저 활동 상태 변경 및 전송
			self._userActiveStatus = USER_ACTIVE_STATUS.ACTIVE;
			htClient.sendActive(self._userActiveStatus);
			
			// push msessage 전송 : 버튼 전송
			console.info(" >>>>>>>>>>>>>>> push button >>>>>>>>>>>>>>>>>>")
			if($(".state_customer").hasClass("idle") === true){
				if(htClient.getChatRoom()._cstmLinkDivCd == "C" || htClient.getChatRoom()._cstmLinkDivCd == "D"){
					console.info(" >>>>>>>>>>>>>>> push >>>>>>>>>>>>>>>>>>")
					self.sendPushAlarm(htClient.getChatRoom()._cstmLinkDivCd, htClient.getChatRoom()._cstmCocId);
				}
			}		
			
			
		}

		// 인풋창에 포커스
//		if (htUtils.isMobile() === false) { // 모바일 제외
			if (extra !== 'button') { // 셀렉트 버튼이 클릭된 경우 제외
				self._$messageArea.focus();  //2020-06-19
			}
//		}

		// 스크롤 다운
		console.info('TRIGGER .to-the-down');
		$('.to-the-down').trigger('click');
		
		/**
		 * 버튼 클릭시!
		 * */
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){
			var chatRoomStatusCd = htClient.getChatRoom()._chatRoomStatusCd;
			
			if (chatRoomStatusCd == CHAT_ROOM_STATUS.ASSIGN_ROBOT) {// 챗봇 상담 중 -> 상담직원 연결
				console.log('===========================상담원연결===============================');
				$.ajax({
					url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
					method: 'put',
					data: {
						command: CHAT_COMMAND.REQUEST_COUNSELOR,
						userId: htUser.getId(),
					},
				}).done(function(data) {
					console.info(data);
					htClient.getChatRoom().setStatus(data.chatRoom.chatRoomStatusCd);
					htClient.getChatRoom().setCnsrDivCd(data.chatRoom.cnsrDivCd);
					/*htClient.getChatRoom().getViewer().hideRequestCounselorButton();*/
					
					htClient.getChatRoom().chatAreaSetting(htClient.getChatRoom());
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: REQUEST COUNSELOR: ', textStatus);
				}).always(function() {
					;
				});
			 } else if (chatRoomStatusCd == CHAT_ROOM_STATUS.WAIT_COUNSELOR) { //상담직원 호출 -> 상담 종료
				if (htClient.canSendMessage()) {
					htClient.sendEnd();
					/*if($(this).hasClass('endChat')){
						console.info("****************************** 1045");
						self.endChatRoom(null,null,htClient.getChatRoom().getCnsrDivCd());
					}*/
				}
			 } else if (chatRoomStatusCd == CHAT_ROOM_STATUS.ASSIGN_COUNSELOR) {
			 }
		}
		
	});

	// ////////////////////////////////////////////////////////////////////////
	// 파일 메세지 전송
	// Drag & Drop 사용시
	$('html').on('dragover', function(e) {

		e.preventDefault();
		e.stopPropagation();
		self._$messageArea.css('background-color', 'cyan');

	}).on('dragleave drop', function(e) {

		e.preventDefault();
		e.stopPropagation();
		self._$messageArea.css('background-color', 'white');
	});

	this._$messageArea.on('dragenter dragover', function(e) {

		e.preventDefault();
		e.stopPropagation();
		self._$messageArea.css('background-color', 'cyan');

	}).on('drop', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$(this).css('background-color', 'white');

		var file = e.originalEvent.dataTransfer.files;
		var formData = new FormData();
		formData.append('file', file[0]);

		htUtils.uploadFile(formData, 'chat', htClient.getChatRoom().getId(), htUser.getId(), htClient.sendFileMessage);

		// 스크롤 다운
		console.info('TRIGGER .to-the-down');
		$('.to-the-down').trigger('click');
	});

	// File 선택창
	$('button.btn_send_file02', this._$chatControlContainer).on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();
		$('#upload_file').trigger('click');
	});
	$('#upload_file').on('change', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var formData = new FormData();
		var file = $(this)[0].files;
		formData.append('file', file[0]);

		htUtils.uploadFile(formData, 'chat', htClient.getChatRoom().getId(), htUser.getId(), htClient.sendFileMessage);
		$(this).blur();

		// 스크롤 다운
		console.info('TRIGGER .to-the-down');
		$('.to-the-down').trigger('click');
	});

	// ////////////////////////////////////////////////////////////////////////
	// 버튼: 자세히 보기
	// 채팅 메세지, 템플릿 메세지
	$(document).on('click', '#chat_body .btn_view_detail, #chat_bottom .btn_view_detail', function(e) {

		e.preventDefault();
		e.stopPropagation();

		//if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창인 경우
			var $parent = $(this).parent();
			$('.poopup_chat_detail .popup_body .detail_contents').html($parent.prev().html());
			if ($(this).closest('.text_box').hasClass('slide')) {
				$('.poopup_chat_detail .btn_text_copy').hide();
			} else {
				$('.poopup_chat_detail .btn_text_copy').show();
			}
			$('.poopup_chat_detail').show();
			$('.poopup_chat_detail .popup_body').scrollTop(0);
		//}
	});

	// 팝업 복사
//	if ($('.poopup_chat_detail .btn_text_copy').length > 0) {
//		var clipboard = new ClipboardJS('.poopup_chat_detail .btn_text_copy');
//		clipboard.on('success', function(e) {
//			e.clearSelection();
//			e.trigger.blur();
//			htUtils.alert('내용을 복사했습니다.');
//		});
//	}

	// 팝업 닫기
	$('.btn_close_detail').on('click', function(e) {

		$('.poopup_chat_detail .popup_head .popup_title').text('');
		$('.poopup_chat_detail .popup_body .detail_contents').text('');
		$('.poopup_chat_detail .btn_text_copy').show();
		$('.poopup_chat_detail').hide();
	});

	// 별첨 보기
	$(document).on('click', '#chat_body .btn_ref .btn_ref_more', function(e) {

		console.info('ALERT');

		e.preventDefault();
		e.stopPropagation();

		var $parent = $(this).closest('li');

		//if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창인 경우
			$('.poopup_chat_detail .popup_head .popup_title').text($('.title', $parent).text());
			$('.poopup_chat_detail .popup_body .detail_contents').html($('.ref', $parent).html().replace(/○/, '1&nbsp;○'));
			$('.poopup_chat_detail').show();
			$('.poopup_chat_detail .popup_body').scrollTop(0);
		//}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 버튼: 링크 (고객이 아니면 무효화)
	// 채팅 메세지, 템플릿 메세지
	$('.counseling_mid').on('click', '#chat_body .btn_link, #chat_bottom .btn_link', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if (htUser.getRollType() !== CHAT_ROLL_TYPE.CUSTOMER) {
			return false;
		}
	});
	
	$(document).on('click', 'a.moveLink',function(e){
		var url  = $(this).attr('data-url');
		
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) {
			var channel = htClient.getChatRoom()._cstmLinkDivCd;
			var appcode = $("#appCode").val();
			switch (channel) {
			case "A"://homepage
				console.log('channel A WEB CHAT');
				window.open(url); 
				break;
			case "B"://kakao
				console.log('channel B KAKAO');
				location.href = url;
				break;
			case "C"://o2
				console.log('channel C O2');
				htUtils.moveCTypeLink(url);
				break;
			case "D"://mPop
				console.log('channel D mPOP');
				htUtils.moveDTypeLink(url);
				break;
			/*
			 * IPCC_MCH ARS 채널 추가 관련 수정
			 * mPOP과 동일하게 처리(ARS도 mPOP에서 넘어옴) 
			 */
			case "E": //ARS
				console.log('channel E ARS');
				htUtils.moveDTypeLink(url);
				break;
			default:
				console.log('channel ==>>'+channel);
				break;
			}
		}else{
			window.open(url);
		}
	});
	// ////////////////////////////////////////////////////////////////////////
	// 고객 채팅창: 챗봇과 대화중 이벤트
	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) {

	// 버튼: 시나리오 (선택)
	$(document).on('click', '#chat_body .btn-bn-list, #chat_body .btn_hot_key, #chat_body .btn-radius', function(e) {

		e.preventDefault();
		e.stopPropagation();
		$(".btn-connect").show();
		
		if($(this).hasClass("moveLink")){
			return false;
		}

		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창인 경우

			if ($(this).hasClass('btn_hot_key') && $(this).hasClass('requestCounselor')) { // 상담직원 연결
				if (htClient.getChatRoom() && htClient.getChatRoom().getStatus() === CHAT_ROOM_STATUS.ASSIGN_ROBOT) {
					$.ajax({
						url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
						method: 'put',
						data: {
							command: CHAT_COMMAND.REQUEST_COUNSELOR,
							userId: htUser.getId(),
						},
					}).done(function(data) {
						console.info(data);
						htClient.getChatRoom().setStatus(data.chatRoom.chatRoomStatusCd);
						htClient.getChatRoom().setCnsrDivCd(data.chatRoom.cnsrDivCd);
						/*htClient.getChatRoom().getViewer().hideRequestCounselorButton();*/
						
						htClient.getChatRoom().chatAreaSetting(htClient.getChatRoom());
					}).fail(function(jqXHR, textStatus, errorThrown) {
						console.error('FAIL REQUEST: REQUEST COUNSELOR: ', textStatus);
					}).always(function() {
						;
					});
				} else {
					console.error('NOT ASSIGN ROBOT, STATUS: ', htClient.getChatRoom().getStatus());
				}
			} else if ($(this).hasClass('btn_hot_key') && $(this).hasClass('newChatRoom')) { // 새 채팅방 생성
				console.debug(htClient.getChatRoom());
				
				var userId = htClient.getChatRoom()._cstmCocId;
				var channel = htClient.getChatRoom()._cstmLinkDivCd;
				var url = "?userId="+userId+"&channel="+channel;
				
				if (channel == "E") {
					url += "&departCd=" + htClient.getChatRoom()._departCd;
				}

				window.location.href = HT_APP_PATH + '/customer'+url;
			} else {
				var contents = $(this).text();
				var section = {
						type: 'text', data: contents
				};
				var extra = $(this).data('extra');
				if (extra) {
					section.extra = extra;
				}
				section.params = {};
				console.debug(section.params);
				if ($(this).data('params-stocknumber')) {
					section.params.stockNumber = $(this).data('params-stocknumber');
					console.debug(section.params);
				}
				if ($(this).data('params-retry')) {
					section.params.retry = $(this).data('params-retry');
					console.debug(section.params);
				}
				if ($(this).data('params-insurancedebitchangeoption')) {
					section.params.insuranceDebitChangeOption = $(this).data('params-insurancedebitchangeoption');
					console.debug(section.params);
				}
				if ($(this).data('params-confirmcertificationoption')) {
					section.params.confirmCertificationOption = $(this).data('params-confirmcertificationoption');
					console.debug(section.params);
				}

				var chatContents = {
						v: DEFAULT_CONTENTS_VERSION,
						balloons: [
							{
								sections: [
									section
								]
							}
						]
				};

				if (extra) {
					htClient.sendMessage(JSON.stringify(chatContents), true);
				} else {
					self._$messageSendButton.trigger('focus');
					self._$messageArea.val(contents);
					self._$messageSendButton.trigger('click', 'button');

					console.error($(this).closest('.swiper-slide').length);
					// 재질의 선택인 경우 버튼 감추기 (캐러셀 메세지일 경우에는 가리지 않음)
					if ($(this).hasClass('btn_requestion')
						&& $(this).closest('.swiper-slide').length === 0) {
						$(this).closest('.text_box').find('ul').hide();
					}

					// 메세지 입력 막음 해제
					self._$messageArea.prop('disabled', false);
					self._$messageArea.attr('placeholder', self._$messageArea.attr('data-placeholder'));
				}

				// 웹접근성: Tab키 입력시 다음 말풍선으로 이동하도록 
				var $buttonList = $('.btn-bn-list', $(this).closest('.message_box'));
				if ($buttonList.length > 0) {
					$buttonList.each(function() {
						$(this).attr('tabindex', '-1');
					});
				}
			}
		}
	});

	// 검색결과 더보기 버튼
	$(document).on('click', '#chat_body .btn_more', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창인 경우
			var keyword = $(this).data('keyword');
			var page = $(this).data('page');

			if (keyword && page && page > 1) {
				$.ajax({
					url: HT_APP_PATH + '/api/chat/withbot',
					method: 'post',
					data: {
						chatRoomUid: htClient.getChatRoom().getId(),
						userId: htUser.getId(),
						keyword: keyword,
						page: page,
					},
				}).done(function(data, textStatus, jqXHR) {
					console.info(data);
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL SEARCH MORE: ', textStatus, jqXHR);
					console.error(jqXHR.responseJSON.returnMessage);
				}).always(function() {
					;
				});
			}
		}
	});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 위/아래 스크롤

	// 버튼 클릭시 스크롤 업
	$(document).on('click', '.to-the-up', function(e) {
		self._$chatMessageViewerContainer.scrollTop(0);
	});

	// 버튼 클릭시 스크롤 다운
	$('.to-the-down').on('click', function(e) {
		setScrollTop(self._$chatMessageViewerContainer, 0);
		setScrollTop(self._$chatMessageViewerContainer, 300);
		setScrollTop(self._$chatMessageViewerContainer, 500);
		setScrollTop(self._$chatMessageViewerContainer, 700);
		setScrollTop(self._$chatMessageViewerContainer, 900);
	});
	
	function setScrollTop(viewContainer, time){
		setTimeout(function() {viewContainer.scrollTop(viewContainer.prop('scrollHeight') - viewContainer.innerHeight());}, time);
	}

	if ($('.to-the-down').length === 1) {
		var scrollMax = this._$chatMessageViewerContainer.prop('scrollHeight') - this._$chatMessageViewerContainer.innerHeight();  
		if (scrollMax === 0) {
			$('.to-the-down').hide();
		}
	}

	// 스크롤 이벤트
	this._$chatMessageViewerContainer.on('scroll', function(e) {
		// 맨 아래로 스크롤 됐을 경우 버튼 감춤
		if ($('.to-the-down').length === 1) {
			var scrollMax = self._$chatMessageViewerContainer.prop('scrollHeight')-self._$chatMessageViewerContainer.innerHeight();
			if ( scrollMax === 0 ||
					self._$chatMessageViewerContainer.scrollTop() === parseInt(scrollMax + 1) ||
					self._$chatMessageViewerContainer.scrollTop() === parseInt(scrollMax - 1) ||
					self._$chatMessageViewerContainer.scrollTop() >= self._$chatMessageViewerContainer.prop('scrollHeight')-(self._$chatMessageViewerContainer.innerHeight() +5)
						){
				$('.to-the-down').hide();
			} else {
				$('.to-the-down').show();
			}
		}

		// 맨 위로 스크롤 됐을 경우 페이징
		if (self._$chatMessageViewerContainer.scrollTop() === 0) {
			if (htClient._requestChatList) {
				try {
					htClient._requestChatList.abort();
				} catch(e) {
					console.error(e);
				}
				htClient._requestChatList = null;
			}
			htClient._requestChatList = htClient.getChatRoom().loadPreviousChatMessageList();
		}
	});
}
/**
* 메세지 추가
*
* @param message JsonObject
* @param skipHighLight Boolean
* @param scroll String
*/
ChatRoomViewer.prototype.addChatMessage = function(message, skipHighLight, scroll) {

	// 스크롤 판별
	var scrollMax = this._$chatMessageViewerContainer.prop('scrollHeight') - this._$chatMessageViewerContainer.innerHeight();
	if (parseInt(scrollMax) === parseInt(this._$chatMessageViewerContainer.scrollTop()) ||
			parseInt(scrollMax + 1) === parseInt(this._$chatMessageViewerContainer.scrollTop()) ||
			parseInt(scrollMax - 1) === parseInt(this._$chatMessageViewerContainer.scrollTop()) ) { // 문서 끝까지 스크롤되있는 상태
		scroll = 'scroll';
	}
	console.debug('=== SCROLL:', scroll);

	// 메모를 말풍선으로 표시 여부
	var addMemoAsMessage = false;
	if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창인 경우 에러
			console.error('CUSTOMER CANNOT VIEW MEMO');
			return;
		}

		// 매니저 페이지인 경우 말풍선으로도 표시
		addMemoAsMessage = htUser.getRollType() === CHAT_ROLL_TYPE.MANAGER;
	}

	// 메모인 경우 메모 영역에 표시
	if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
		var $messageElement = this.buildMemoElement(message);
		if ($messageElement !== null) {
			this.addMemoElement($messageElement);
		}
	}

	// 메세지인 경우 말풍선으로 표시
//	if (addMemoAsMessage || message.contDivCd !== CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
	if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.CHAT) {
		if (this.hasMessage(message)) {
			console.info('SKIP ALREADY EXIST MESSAGE: ', message);
			return;
		}
		var $messageElement = this.buildMessageElement(message, skipHighLight);
		if ($messageElement !== null) {
			this.addMessageElement($messageElement);
		}

		// 스크롤
		console.debug('containerOuterHeight', this._$chatMessageViewerContainer.outerHeight());
		console.debug('containerInnerHeight', this._$chatMessageViewerContainer.innerHeight());
		console.debug('containerHeight', this._$chatMessageViewerContainer.prop('scrollHeight'));
		console.debug('containerScrollTop', this._$chatMessageViewerContainer.scrollTop());
		console.debug('scrollMax', scrollMax);
		if (scroll === 'scroll') {
			// 이미지가 있을 경우
			var allImageCount = $('img', $messageElement).length;
			console.debug('allImageCount: ', allImageCount);
			var loadedImageCount = 0;
			if (allImageCount > 0) {
				var self = this;
				$('img', $messageElement).on('load error', function() { // 이미지 로드완료시 스크롤
					console.debug('loaded image');
					loadedImageCount++;
					if (loadedImageCount === allImageCount) {
						console.debug('scroll by load image');
						if ($('.to-the-down').length === 1) {
							$('.to-the-down').trigger('click', 'addChatMessage');
						} else {
							self._$chatMessageViewerContainer.scrollTop(self._$chatMessageViewerContainer.prop('scrollHeight'));
						}
					}
				}).on('error', function() { // 에러시 노이미지
					$(this).attr('src', HT_APP_PATH + '/images/chat/no_image.png').attr('alt', 'NO IMAGE').addClass('chatImageCs');
					if ($('.to-the-down').length === 1) {
						$('.to-the-down').trigger('click', 'addChatMessage');
					} else {
						self._$chatMessageViewerContainer.scrollTop(self._$chatMessageViewerContainer.prop('scrollHeight'));
					}
				});
			} else {
				if ($('.to-the-down').length === 1) {
					$('.to-the-down').trigger('click', 'addChatMessage');
				} else {
					this._$chatMessageViewerContainer.scrollTop(this._$chatMessageViewerContainer.prop('scrollHeight'));
				}
			}
		} else if (scroll === 'auto') {
			;
		}
		console.debug('containerHeight', this._$chatMessageViewerContainer.prop('scrollHeight'));
		console.debug('containerScrollTop', this._$chatMessageViewerContainer.scrollTop());
	}

	// 상태별 메세지창 수정
	if(htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){
		htClient.getChatRoom().chatAreaSetting(htClient.getChatRoom())
	}
	
	// 상태별 메세지창 수정
//	if(htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER && message.cnsrMemberUid != null && message.senderDivCd === CHAT_ROLL_TYPE.COUNSELOR){
//		var chatRoomUid = message.chatRoomUid;
//
//		$.ajax({
//			url: HT_APP_PATH + '/api/chat/room/' + chatRoomUid,
//		}).done(function(data) {
//			if (data) {
//				console.log("==chatroom status update== ("+ data.chatRoomStatusCd+")");
//				htClient.getChatRoom().setStatus(data.chatRoomStatusCd);
//				htClient.getChatRoom().chatAreaSetting(htClient.getChatRoom())
//			} else {
//				console.error('FAILED GET CHATROOM', chatRoomUid);
//			}
//		}).fail(function(jqXHR, textStatus, errorThrown) {
//	    	console.error('FAIL REQUEST: GET CHATROOM: ', textStatus);
//	    });
//	}
};
/**
 * 메세지 존재 여부
 *
 * @param message JsonObject
 */
ChatRoomViewer.prototype.hasMessage = function(message) {

	return $('div[data-message-id=' + message.chatNum + ']', this._$chatMessageViewerContainer).length === 1;


};
///**
// * 긴 텍스트 메세지 스타일 변경
// */
//ChatRoomViewer.prototype.resizeLongText = function() {
//
//	$('.text200').each(function() {
//		if($(this).height() > 205){
//			$(this).parent().find(".go_link.btn_detail").show();
//		}else{
//			$(this).parent().find(".go_link.btn_detail").hide();
//		}
//	});
//};
/**
* 메모 엘리먼트 추가
*
* @param messageElement jQueryObject
*/
ChatRoomViewer.prototype.addMemoElement = function(messageElement) {

	console.debug(messageElement);
	this.showMemoContainer();
	this._$chatMemoViewerContainer.prepend(messageElement);
};
/**
 * 메모 영역 보이기
 */
ChatRoomViewer.prototype.showMemoContainer = function() {

	console.debug('SHOW MEMO');
	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
		this._$chatMemoViewerContainer.closest('.request_info').show();
		$('#chat_body').height('calc(100% - 438px)').css('margin-top', '140px');
	} else {
		this._$chatMemoViewerContainer.closest('.request_info').show();
		$('#chat_body').height('calc(100% - 202px)').css('margin-top', '140px');
	}
};
/**
 * 메모 영역 감추기
 */
ChatRoomViewer.prototype.hideMemoContainer = function() {

	console.debug('HIDE MEMO');
	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
		this._$chatMemoViewerContainer.closest('.request_info').hide();
		$('#chat_body').height('calc(100% - 398px)').css('margin-top', '100px');
	} else {
		this._$chatMemoViewerContainer.closest('.request_info').hide();
		$('#chat_body').height('calc(100% - 162px)').css('margin-top', '100px');
	}
};
/**
 * 메모 영역 비우기
 */
ChatRoomViewer.prototype.emptyMemoContainer = function() {

	this._$chatMemoViewerContainer.empty();
};
/**
* 메세지 엘리먼트 추가
*
* @param $messageElement jQueryObject
*/
ChatRoomViewer.prototype.addMessageElement = function($messageElement) {

	var messageId = parseInt($messageElement.attr('data-message-id'));

	if ($messageElement.attr('data-message-id') === undefined || messageId === NaN) {
		this._$chatMessageViewerContainer.append($messageElement);
	} else if ($('.message_box', this._$chatMessageViewerContainer).length === 0) {
		this._$chatMessageViewerContainer.append($messageElement);
	} else if (messageId < parseInt($('.message_box:first', this._$chatMessageViewerContainer).data('message-id'))) {
		this._$chatMessageViewerContainer.prepend($messageElement);
	} else if (messageId > parseInt($('.message_box:last', this._$chatMessageViewerContainer).data('message-id'))) {
		this._$chatMessageViewerContainer.append($messageElement);
	} else {
		console.warn('=== NOT TAIL MESSAGE');
		console.warn('messageId: ', messageId
				, 'first: ', parseInt($('.message_box:first', this._$chatMessageViewerContainer).data('message-id'))
				, 'last: ', parseInt($('.message_box:last', this._$chatMessageViewerContainer).data('message-id')));
	}

	// TEXT 200
	var lineSize = $('.swiper-slide', $messageElement).length > 0 ? 6 : 10;
	console.debug("=======================================================");
	console.debug('lineSize: ', lineSize, 'chatNum: ', $messageElement.data('message-id'));
	$('.inner_text', $messageElement).each(function() {
		console.debug('height: ', $(this).innerHeight());
		if (getBoxText200($(this), lineSize)) {
			console.debug('text200');
			$(this).addClass('text200').addClass('text_top');
			var detailText = $('.swiper-slide', $messageElement).length > 0 ? '자세히 보기 &gt;' : '자세히 보기';
			var $button = $('<div class="inner_btn_area"><button type="button" class="btn_view_detail">' + detailText + '</button></div>');
			$(this).after($button);
		}
	});

	console.debug($('.swiper-container > ul', $messageElement).length);
	
	// 상태별 메세지창 수정
	if(htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){
		htClient.getChatRoom().chatAreaSetting(htClient.getChatRoom())
	}

	// 멀티형 메세지 슬라이더
	if ($('.swiper-container', $messageElement).length > 0) {

		new Swiper($('.swiper-container', $messageElement), {
			//autoHeight: true,
			//calculateHeight: true,
			slidesPerView: 'auto',
			centeredSlides: false,
			spaceBetween:6,
			on: function(e) {
				console.debug(e);
			}
		});
	}
};
/**
* 메모 엘리먼트 생성
*
* @param message Object
*/
ChatRoomViewer.prototype.buildMemoElement = function(message) {

	var $containerElement = $('<li />').attr('data-message-id', message.chatNum);

	// 날짜
	var $dateElement = $('<p />').addClass('label_area').append($('<span />').addClass('labelGray').text(message.regDtPretty));
	$containerElement.append($dateElement);

	// 내용
	console.debug(message.cont);
	var chatContents = JSON.parse(message.cont);
	var balloonList = chatContents.balloons;
	console.debug('balloonList.length', balloonList.length);

	if (balloonList.length === 1) { // 메모는 단일 말풍선만 가능
		var balloon = balloonList[0];
		var sectionList = balloon.sections;
		if (sectionList.length === 1) { // 메모는 단일 섹션만 가능
			var section = sectionList[0];
			if (section.type === MESSAGE_SECTION_TYPE.TEXT) { // 메모는 TEXT 타입만 가능
				var $contentsElement = $('<p />').addClass('text_area').html(section.data);
				$containerElement.append($contentsElement);
				return $containerElement;
			}
		}
	}

	console.error('INVALID MEMO FORMAT');
};

function getBgIcon(){
	if(htClient.getChatRoom()._cstmLinkDivCd === 'A'){
		return '<div class="bg-chat2">상담아이콘</div>';
	}
	return '<div class="bg-chat">상담아이콘</div>';
}
/**
* 메세지 엘리먼트 생성
*
* @param message JsonObject
* @param skipHighLight Boolean
*/
ChatRoomViewer.prototype.buildMessageElement = function(message, skipHighLight) {
	
	console.debug(message);
	//options = options || {};
	var $messageElement = $('<div />')
							.attr('data-message-id', message.chatNum)
							.attr('data-message-sender', message.senderUid)
							.addClass('message_box');	//필수

	//챗 위치
	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
		if (message.senderUid === htUser.getId()) { // 고객 메세지
			$messageElement.addClass('right-chat-wrap');
		} else {
			$messageElement.addClass('left-chat-wrap');
		}
	} else { // 상담직원 채팅창
		if (message.senderDivCd === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 메세지
			$messageElement.addClass('left-chat-wrap');
		} else {
			if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) { // 메모
				$messageElement.addClass('left-chat-wrap');
				$messageElement.addClass('change_staff');
			} else { // 메세지
				$messageElement.addClass('right-chat-wrap');
			}
		}
	}
	
	// 메세지 내용 파싱
	var chatContents = JSON.parse(message.cont);
	var balloonList = chatContents.balloons;
	
	var isMultiMessage = balloonList.length > 1;
	var $slideContainerElement = null;

	// 메세지 박스 스타일
	if(message.senderDivCd === CHAT_ROLL_TYPE.ROBOT && isMultiMessage){
		var $contentsElement = $('<div />').addClass('swiper-area')//.addClass('text_box');
	}else{
		var $contentsElement = $('<div />').addClass('bubble-wrap')//.addClass('text_box');	
	}

	
	//var $targetElement = $contentsElement;
	if (htUser.getRollType() !== CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창 아니면
		if (message.senderDivCd === CHAT_ROLL_TYPE.ROBOT) { // 로봇 메세지
			$contentsElement.addClass('blue');
		} else { // if (message.senderDivCd === CHAT_ROLL_TYPE.COUNSELOR) { // 상담직원 메세지
			$contentsElement.addClass('green');
		}
	}
	
	var $innerChat = $('<article />').addClass('inner-chat');
	
	// 사용자 아바타
	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
		console.debug('message.avatarCd: ', message.avatarCd);
		if (message.senderDivCd === CHAT_ROLL_TYPE.ROBOT) { // 로봇 메세지
			$messageElement.append(getBgIcon());
		} else if (message.senderDivCd !== CHAT_ROLL_TYPE.CUSTOMER) { // 상담직원 메세지
			$messageElement.append($(getBgIcon()));
		}
	} else { // 상담직원 채팅창
		if (message.senderDivCd === CHAT_ROLL_TYPE.ROBOT) { // 로봇 메세지
		} else if (message.senderDivCd === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 메세지
		} else if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) { // 메모: 아바타 미표시
			;
		} else { // 상담직원 메세지
		}
	}
	
	//화살표 아이콘
	var $targetElement = null;
	if (!isMultiMessage) {
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { 			//[ 고객 채팅창 ]
			
			if (message.senderDivCd === CHAT_ROLL_TYPE.ROBOT) { 			// 로봇 메세지
				$targetElement = $('<div />').addClass('bubble-templet');
			} else if (message.senderDivCd === CHAT_ROLL_TYPE.CUSTOMER) { 	// 고객 메세지
				$targetElement = $('<div />').addClass('bubble-arrow-blue');
				var $arrow = $('<span />').addClass('bg');
				$targetElement.append($arrow);
			} else if (message.senderDivCd !== CHAT_ROLL_TYPE.CUSTOMER) { 	// 상담직원 메세지 (매니저,상담직원)
				if(balloonList[0].sections.length > 1){			//templet
					$targetElement = $('<div />').addClass('bubble-templet');
				}else{
					$targetElement = $('<div />').addClass('bubble-arrow');
					var $arrow = $('<span />').addClass('bg');
					$targetElement.append($arrow);
				}
			}
		}else{																// [ 상담직원 채팅창 ]
			if (message.senderDivCd === CHAT_ROLL_TYPE.ROBOT) { 			// 로봇 메세지
				$targetElement = $('<div />').addClass('bubble-templet');
			} else if (message.senderDivCd === CHAT_ROLL_TYPE.CUSTOMER) { 	// 고객 메세지
				$targetElement = $('<div />').addClass('bubble-arrow-blue');
			} else if (message.senderDivCd !== CHAT_ROLL_TYPE.CUSTOMER) { 	// 상담직원 메세지 (매니저,상담직원)
				if(balloonList[0].sections.length > 1){			//templet
					$targetElement = $('<div />').addClass('bubble-templet');
				}else{
					$targetElement = $('<div />').addClass('bubble-arrow');
				}
			}
		}
		
		
		$innerChat.append($targetElement);
	}
	
	// 캐러셀 메세지인 경우 Container 추가
	if (isMultiMessage) {
		console.info('MULTI MESSAGE');
		
		var $slideWrapElement = $('<div />').addClass('swiper-container');
		$innerChat.append($slideWrapElement);
		$slideContainerElement = $('<div />').addClass('swiper-wrapper');
		$slideWrapElement.append($slideContainerElement);
	}
	

	
	for (var i = 0; balloonList.length > i; i++) {

		var balloon = balloonList[i];
		var isLastBalloon = i === (balloonList.length - 1);
		var $slide = $('<div />').addClass('swiper-slide');
		var $warp = $('<div />').addClass('bubble-wrap');
		var $inner = $('<article />').addClass('inner-chat');

		// 캐러셀 메세지인 경우 Container 추가
		if (isMultiMessage) {
			if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER && $innerChat.hasClass('slide') === false) {// 고객채팅창인 경우
				$innerChat.addClass('slide');
			}
			$targetElement = $('<div />').addClass('bubble-templet');
		}

		var sectionList = balloon.sections;
		for (var j = 0; sectionList.length > j; j++) {
			var section = sectionList[j];

			switch (section.type) {
			case MESSAGE_SECTION_TYPE.TEXT:
				if (this.textElementHandler(section, $targetElement, skipHighLight, message.senderDivCd)) { //, sectionSize))
					if ($slideContainerElement !== null) {
						if(!isMultiMessage){
							$slideContainerElement.append($targetElement);
						}else{
							$slide.append($targetElement);
						}
					}
				}
				break;
			case MESSAGE_SECTION_TYPE.FILE:
				if (this.fileElementHandler(section, $targetElement, isLastBalloon)) {
					if ($slideContainerElement !== null) {
						if(!isMultiMessage){
							$slideContainerElement.append($targetElement);
						}else{
							$slide.append($targetElement);
						}
					}
				}
				break;
			case MESSAGE_SECTION_TYPE.ACTION:
				console.debug(section);
				if (this.actionElementHandler(section, $targetElement, $innerChat, chatContents.orient)) {
					if ($slideContainerElement !== null) {
						if(!isMultiMessage){
							$slideContainerElement.append($targetElement);
						}else{
							$slide.append($targetElement);
						}
					}
				}
				break;
			// case MESSAGE_SECTION_TYPE.TABLE:
			// 	if (this.tableElementHandler(section, $targetElement)) {
			// 		if ($slideContainerElement !== null) {
			// 			$slideContainerElement.append($targetElement);
			// 		}
			// 	}
			// 	break;
			default:
				console.error('INVALID MESSAGE SECTION TYPE: ', section.type, section);
				break;
			}
		}
		if ($slideContainerElement !== null) {
			$inner.append($targetElement);
			$warp.append($inner);
			$slide.append($warp);
			$slideContainerElement.append($slide);
		}
	}

	// 시간
	var $timeElement;
	
	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){
		$timeElement = $('<div />').addClass('time-chat').text(message.regDtPretty);
	}else{
		if(message.senderDivCd === CHAT_ROLL_TYPE.CUSTOMER){
			$timeElement = $('<div />').addClass('time-chat').text(message.regDtPretty);
		}else{
			
			/*if(message.memberName && message.senderDivCd != 'S' && message.senderDivCd != 'A' && message.senderDivCd != 'R'){
				$timeElement = $('<div />').addClass('time-chat').text(message.regDtPretty+"  "+message.memberName);
			}else{
				$timeElement = $('<div />').addClass('time-chat').text(message.regDtPretty);
			}*/
			
			if(message.memberName && message.senderDivCd != 'R'){
				$timeElement = $('<div />').addClass('time-chat').text(message.regDtPretty+"  "+message.memberName);
			}else{
				$timeElement = $('<div />').addClass('time-chat').text(message.regDtPretty);
			}
			
		}
	}
	
	if (!isMultiMessage) {
		$targetElement.append($timeElement);
	}else{
		$innerChat.append($timeElement);
	}

	/*
	// 편집 버튼 (관리자)
	if (htUser.getRollType() === CHAT_ROLL_TYPE.MANAGER) { // 매니저 페이지
			//&& options.editable === true) {
		if ($('.admin_counseling .sub_stit').length === 1
				&& $('.admin_counseling .sub_stit').attr('data-select-status') === 'contReview') { // 검토요청 페이지
			if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO // 메모
					|| message.senderDivCd === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 메세지
				var $editElement = $('<div />').addClass('chat_btn_area');
				if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) { // 메모
					$editElement.append($('<button type="button" class="btn_chat_modify">수정</button>'));
					$editElement.append($('<button type="button" class="btn_chat_del">삭제</button>'));
					$timeElement.prepend($editElement);
				} else { // 고객 메세지
					// if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.TEXT) { // 텍스트 메세지
					$editElement.append($('<button type="button" class="btn_chat_modify">수정</button>'));
					$timeElement.prepend($editElement);
					// } else if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.FILE) { // 파일 메세지
					// }
				}
			}
		}
	}*/

	// 안읽음 표시
	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) { // 상담직원만 적용
		if (message.senderUid === htUser.getId()
				&& message.contDivCd !== CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
			if (message.msgStatusCd === CHAT_MESSAGE_STATUS.SENT
					|| message.msgStatusCd === CHAT_MESSAGE_STATUS.RECEIVED) {
				var $unreadElement = $('<span />').addClass('no_read').text('안읽음');
				$unreadElement.append('</br>')
				$timeElement.prepend($unreadElement);
			}
		}
	}

	// TODO: DELETEME, 임시오픈시만 사용
	// 챗봇 답변 평가 버튼
	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER
			&& message.senderDivCd === CHAT_MEMBER_TYPE.ROBOT) {

		if (htUser.getId() === SIMULATOR_UID && message.botIntent
				&& message.botIntent !== 'CATEGORY') { // 분류봇은 제외
//			var $evlBotElement = $('<div class="evl_bot" style="width:100%; float:left; margin-top: 11px"></div>');
			var $evlBotElement = $('<div class="block_name" style="position:absolute;bottom:0;margin-left:5px;margin-bottom:5px;"></div>');
			//var selectedColor = '' ;
			// var notSelectedColor = '';
//			message.evlTypeCd;
			$evlBotElement
//				.append($('<div>요청한 질문에 맞는 답변인가요?</div>'))
//				.append($('<span style="cursor: pointer; margin: 5px 5px; background: #4a9fc1; border-top: 1px solid #4a9fc12; border-radius: 3px; box-shadow: 0 1px 3px 0 #000; width: 70px; display: inline-block; text-align: center; color: #fff; font-weight: 400;"/>')
//							.addClass('evl_soso').attr('data-evl', 'SOSO').text('애매해요'))
//				.append($('<span style="cursor: pointer; margin: 5px 5px; background: #4a9fc1; border-top: 1px solid #4a9fc12; border-radius: 3px; box-shadow: 0 1px 3px 0 #000; width: 70px; display: inline-block; text-align: center; color: #fff; font-weight: 400;"/>')
//							.addClass('evl_down').attr('data-evl', 'DOWN').text('틀렸어요'));
				.append($('<span style="cursor: pointer; padding: 5px; background: #4a9fc1; border: 1px solid #4a9fc12; border-radius: 3px; box-shadow: 0 1px 3px 0 #000; display: inline-block; text-align: center; color: #fff; font-weight: 400;"/>')
					.addClass('evl_up').attr('data-name', message.botIntent).text(message.botIntent));
			$innerChat.append($evlBotElement);
		}
	}
	// TODO: DELETEME, 임시오픈시만 사용
	// 챗봇 답변 평가 버튼
	
	//inner_chat 에 담고 inner_chat을 messageElement에 넣는다.
	/*
	if(){
		$contentsElement
	}*/
	
	$contentsElement.append($innerChat);
	$messageElement.append($contentsElement);
	
	return $messageElement;
};
/**
 * 채팅 메세지 > 일반 텍스트 섹션 핸들러
 *
 * @param section JsonObject
 * @param $parent jQueryObject
 * @param skipHighLight Boolean
 */
ChatRoomViewer.prototype.textElementHandler = function(section, $parent, skipHighLight, senderDivCd) { //, sectionSize) {

	var contents = section.data || '';
	if (contents === '') {
		return;
	}
	var extra = section.extra;
	
	if (senderDivCd === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 메세지
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) {
			contents = htUtils.alternativeForbidden(section.data);
		} else if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
			contents = htUtils.blindForbidden(section.data);
		}
	}else{
		
	}

	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR
			&& !skipHighLight) {
		for (var i = 0; i < htHighlight.length; i++) {
			var word = htHighlight[i];
			var regex = new RegExp('(' + word + ')', 'g');
			if (contents) {
				contents = contents.replace(regex, '<span class="point-blue-bg">$1</span>');
			}
		}
	}

	//var $contents = $('<div />').addClass('inner_text').addClass('type_text').html(contents.replace(/(\n|\r\n)/g, '<br />'));
	var $contents = $('<div />').addClass('mch').addClass('type_text').html(contents.replace(/(\n|\r\n)/g, '<br />'));

	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR
			&& !skipHighLight) {
		$contents.addClass('highlighted');
	}

	// 메세지 별 스타일 적용
//	if (sectionSize === 1 && contentsLength < 200) { // 텍스트만 있는 메세지
//		$contents.addClass('w100');
//		$parent.append($contents);
//	} else
	$parent.append($contents);
//	var lineSize = $parent.hasClass('swiper-slide') ? 7 : 10;
//	console.error('lineSize: ', lineSize);
//	if (getBoxText200($contents, lineSize)) {
//		console.error('text200');
//	//if (contentsLength >= 200) { // 200자 이상 메세지, 말줄임 스타일 및 '자세히보기'버튼 추가
//		$contents.addClass('text200').addClass('text_top');
//		var $button = $('<div class="inner_btn_area"><button type="button" class="btn_view_detail">자세히 보기</button></div>');
//		$parent.append($button);
//	}

	return true;
};
ChatRoomViewer.prototype.highLightMessage = function() {

	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
		//$('.type_text:not(.highlighted)', this._$chatMessageViewerContainer)//.slice(-MAX_HIGHLIGHT_SIZE)
		$('.type_text:not(.highlighted)', this._$chatMessageViewerContainer).each(function() {
			var contents = $(this).text();
			for (var i = 0; i < htHighlight.length; i++) {
				var word = htHighlight[i];
				var regex = new RegExp('(' + word + ')', 'g');
				contents = contents.replace(regex, '<span class="highlight">$1</span>');
			}
			if ($(this).text() !== contents) {
				$(this).html(contents);
			}
		});
	}
};
/**
 * 채팅 메세지 > 파일 섹션 핸들러
 *
 * @param section JsonObject
 * @param $parent jQueryObject
 * @param isLastItem boolean
 */
ChatRoomViewer.prototype.fileElementHandler = function(section, $parent, isLastItem) {
	if(!$parent.hasClass('bubble-templet type-2')){
		$parent.removeClass('bubble-round');
		$parent.removeClass('bubble-arrow');
		$parent.addClass('bubble-templet type-2');
	}

	if (section.display.indexOf('image') === 0) {
/*		
		console.debug('section.data: ', section.data.indexOf('/') === 0 ? 'relative' : 'absolute');
		var srcPath = section.data;
		if (section.data.indexOf('/') === 0) {
			srcPath = section.data;
		}

		var $contents = $('<div />').addClass('img_area');
		if (isLastItem) {
			$contents.addClass('faq_icon_tit');
		} else {
			$contents.addClass('marginT15');
		}

		var $link = $('<a />').attr('href', srcPath).attr('target', '_blank').attr('title', '새창으로 이미지 보기');
		if (section.extra) {
			$link.append($('<img />').attr('src', srcPath).attr('alt', section.extra));
		} else {
			$link.append($('<img />').attr('src', srcPath).attr('alt', '채팅 이미지'));
		}
		$contents.append($link);

		$parent.append($contents);
*/		
		
		var srcPath = section.data;
		if (section.data.indexOf('/') === 0) {
			srcPath = section.data;
		}
		
		var $contents = $('<dt />').addClass('tit');
		var $link = $('<a />'); //.attr('href', srcPath).attr('target', '_blank').attr('title', '새창으로 이미지 보기')
		if (section.extra) {
			$link.append($('<img />').attr('src', srcPath).attr('alt', section.extra).addClass('chatImageCs'));
		} else {
			$link.append($('<img />').attr('src', srcPath).attr('alt', '채팅 이미지').addClass('chatImageCs'));
		}

		$contents.append($link);
		$parent.append($contents);		
		return true;
	}else if(section.display.indexOf('pdf') > -1){
		var srcPath = section.data;
		var $contents = $('<dd />').addClass('btn-wrap');
		
		//$contents.append('<a href="' + section.data + '" target="_blank" class="btn-bn-list-icon" title="새창열림"><img class="bg" src="../images/icon/icon_line_bottom_2.png" alt="PDF" />'+section.extra+'</a>');
		$contents.append('<a href="javascript:void(0);" class="btn-bn-list-icon moveLink" title="새창열림" data-url="' + section.data + '"><img class="bg" src="../images/icon/icon_line_bottom_2.png" alt="PDF" />'+section.extra+'</a>');
		$parent.append($contents);
		return true;		
	} else {
		console.error('NOT IMPLEMENT FILE TYPE: ', section.display);
		return false;
	}
};
/**
 * 채팅 메세지 > 액션 섹션 핸들러
 *
 * @param section JsonObject
 * @param $parent jQueryObject
 */
ChatRoomViewer.prototype.actionElementHandler = function(section, $parent1, $parent2, orient) {
	var $parent;
	console.debug("== action == " + section);
	if (section.actions) {
		// 링크, 메세지 액션
		var actionSize = 0;
		if(orient =="H"){
			var $wrapper = $('<ul />').addClass('btn-radius-wrap');//.addClass('slide');
			$parent = $parent2;
		}else{
			var $wrapper = $('<div />').addClass('btn-wrap');//.addClass('slide');
			$parent = $parent1;
		}

		for (var i = 0; section.actions.length > i; i++) {
			var action = section.actions[i];
			if (action.type === MESSAGE_ACTION_TYPE.MESSAGE) {
				this.messageActionHandler(action, $wrapper, orient);
				actionSize++;
			} else if (action.type === MESSAGE_ACTION_TYPE.LINK) {
				this.linkActionHandler(action, $wrapper, orient);
				actionSize++;
			}
		}
		if (actionSize > 0) {
			$parent.append($wrapper);
		}

		// 핫키 액션
		for (var i = 0; section.actions.length > i; i++) {
			var hotKeyAction = section.actions[i];
			if (hotKeyAction.type === MESSAGE_ACTION_TYPE.HOTKEY) {
				if(orient =="H"){
					this.hotkeyActionHandler(hotKeyAction, $wrapper, orient);
				}else{
					this.hotkeyActionHandler(hotKeyAction, $parent, orient);
				}
			}
			// else {
			// 	console.error('INVALID MESSAGE ACTION TYPE: ', section.actionType, section);
			// 	return false;
			// }
		}
		if(orient =="H"){
			$parent.append($wrapper);
		}
	}

	return true;
};
/**
 * 채팅 메세지 > 섹션 > 별첨 액션
 *
 * @param actionList JsonArray
 * @param $parent jQueryObject
 */
ChatRoomViewer.prototype.referenceActionHandler = function(actionList, $parent) {

	console.info(actionList);

	var $wrapper = $('<ul />').addClass('go_link').addClass('slide').addClass('btn_ref').css({'max-witdh': '190px'});

	for (var i = 0; actionList.length > i; i++) {
		var action = actionList[i];
		var $refTitle = $('<span />').addClass('title').css({'display': 'none'}).html(action.name);
		var $ref = $('<span />').addClass('ref').css({'display': 'none'}).html(action.data.replace(/(\n|\r\n)/g, '<br />'));
		var $button = $('<li><a href="javascript:void(0);" class="btn_ref_more">' + action.name + '</a></li>').append($ref).append($refTitle);
		$wrapper.append($button);
	}

	$parent.append($wrapper);

	return true;
};
/**
 * 채팅 메세지 > 핫키 섹션 핸들러
 *
 * @param action JsonObject
 * @param $parent jQueryObject
 */
ChatRoomViewer.prototype.hotkeyActionHandler = function(action, $parent, orient) {

	console.debug('hotkeyActionHandler, action: %o', action);
	if(orient == "H"){
		var $option = $('<li>').addClass('inner-line');
	}
	
	// 상담직원 연결 버튼 예외
	switch (action.extra) {
		case MESSAGE_ACTION_HOTKEY_TYPE.REQUEST_COUNSELOR:
			if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER
				&& htUtils.isWorkTime() === false) {
				console.debug('SKIP SORRY BUTTON');
				return false;
			}
			// 소켓 메세지로 처리됨
			var $hotkeyButton = $('<a href="javascript:void(0);" />').addClass('btn_hot_key').addClass('requestCounselor')
				.attr('data-extra', action.extra)
				.text(action.name).append('<i />');
				if(orient == "H"){
					$hotkeyButton.addClass('btn-radius');
				}

			if (action.params) {
				for (var key in action.params) {
					if (action.params.hasOwnProperty(key)) {
						$hotkeyButton.attr('data-params-' + key, action.params[key]);
					}
				}
			}
			if(orient == "H"){
				$option.append($hotkeyButton);
				$parent.append($option);
			}else{
				$parent.append($hotkeyButton);
			}

			break;

		case MESSAGE_ACTION_HOTKEY_TYPE.NEW_CHATROOM:
			$parent.css("padding","0px");
			if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){
				$parent.children().eq(1).css("padding","14px 14px 16px 14px");
			}else if(htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR){
				$parent.children().eq(0).css("padding","14px 14px 16px 14px");
			}
			
			$parent.append(
				$('<a href="javascript:void(0);" />').addClass('btn_hot_key')
					.addClass('newChatRoom')
					//.text('채팅상담 다시 시작하기').append('<i />')
					.text('다시 시작하기').append('<i />')
			);
			break;

		default:
			var $hotkeyButton = $('<a href="javascript:void(0);" />').addClass('btn_hot_key')
			.attr('data-extra', action.extra)
			.text(action.name).append('<i />');
		
			if(orient == "H"){
				$hotkeyButton.addClass('btn-radius');
			}

			if (action.params) {
				for (var key in action.params) {
					if (action.params.hasOwnProperty(key)) {
						$hotkeyButton.attr('data-params-' + key, action.params[key]);
					}
				}
			}

			if(orient == "H"){
				$option.append($hotkeyButton);
				$parent.append($option);
			}else{
				$parent.append($hotkeyButton);
			}
			break;
	}

	return true;
};
/**
 * 채팅 메세지 > 섹션 > 메세지 액션 (Echo)
 *
 * @param action JsonObject
 * @param $wrapper jQueryObject
 */
ChatRoomViewer.prototype.messageActionHandler = function(action, $parent, orient) {

	console.debug(action);
	if(orient == "H"){
		var $option = $('<li>').addClass('inner-line');
		$parent.append($option);
		
		var $link = $('<a href="javascript:void(0);" />')
					.addClass('btn-radius')
					.attr('data-extra', action.extra)
					.text(action.name);
		
		if (action.params) {
			for (var key in action.params) {
				if (action.params.hasOwnProperty(key)) {
					$link.attr('data-params-' + key, action.params[key]);
				}
			}
		}
		
		$option.append($link);
		$parent.append($option);
		
	}else{
		var $link = $('<a href="javascript:void(0);" />')
					.addClass('btn-bn-list')
					.attr('data-extra', action.extra)
					.text(action.name);
		
		if (action.params) {
			for (var key in action.params) {
				if (action.params.hasOwnProperty(key)) {
					$link.attr('data-params-' + key, action.params[key]);
				}
			}
		}
		
		$parent.append($link);
	}
	return true;
};
/**
 * 채팅 메세지 > 섹션 > 링크 액션
 *
 * @param action JsonObject
 * @param $wrapper jQueryObject
 */
ChatRoomViewer.prototype.linkActionHandler = function(action, $wrapper, orient) {

	console.debug(action);
	console.debug("/////////////////////////////// action : " ); 
	console.debug(action.name, action.deviceType, htClient.getChatRoom()._cstmOsDivCd);

	if (action.deviceType && action.deviceType !== 'all') {
		if (action.deviceType !== htClient.getChatRoom()._cstmOsDivCd) {
			return true;
		}
	}
	console.log("****************************** action.keyword; " + action.keyword);
	if(orient=="H"){
		var $option = $('<li class="inner-line"><a href="javascript:void(0);" class="btn-radius moveLink" title="새창열림" data-url="' + action.data + '">' + action.name + '</a></li>');
	}else{
		var iconKey = action.keyword;
		var iconStr = "";
		if(iconKey == "" || iconKey === undefined){
			iconStr = "";
		}else{
			iconStr ='<img class="bg" alt="아이콘" src="/happytalk/images/icon/icon_line_bottom_'+iconKey+'.png">';
		}
		
		var $option = $('<a href="javascript:void(0);" class="btn-bn-list-icon moveLink" title="새창열림"  data-url="' + action.data + '">'+ iconStr + action.name + '</a>');
	}
	//var $option = $('<li><a href="' + action.data + '" target="_blank" class="btn_link" title="새창열림">' + action.name + '</a></li>');
	$wrapper.append($option);
	return true;
};

// /**
//  * 채팅 메세지 > 더보기형 섹션 핸들러
//  *
//  * @param actionList JsonArray
//  * @param $parent jQueryObject
//  */
// ChatRoomViewer.prototype.moreActionHandler = function(actionList, $parent) {
//
// 	console.debug(actionList.length);
// 	var $wrapper = $('<ul />').addClass('go_link').addClass('go_more').addClass('slide');
// 	for (var i = 0; actionList.length > i; i++) {
// 		var action = actionList[i];
// 		console.debug(action);
// 		var $option = $('<li><a class="btn_more" href="javascript:void(0);" data-keyword="'
// 				+ action.data + '" data-page="' + action.extra + '">' + action.name + '</a></li>');
// 		$wrapper.append($option);
// 	}
// 	$parent.append($wrapper);
// 	return true;
// };
// /**
//  * 채팅 메세지 > 테이블 섹션 핸들러
//  *
//  * @param contents JsonObject
//  * @param $parent jQueryObject
//  */
// ChatRoomViewer.prototype.tableElementHandler = function(contents, $parent) {
//
// 	console.debug(contents.length);
// 	for (var n = 0; contents.length > n; n++) {
// 		var $wrapper = $('<div />').addClass('inner_table');
// 		var table = contents[n];
// 		var $tableHeader = $('<div />').addClass('inner_table_tit').text(table.head);
// 		$wrapper.append($tableHeader);
// 		var $table = $('<table />').addClass('tCont').addClass('chat');
// 		$wrapper.append($table);
// 		var $tbody = $('<tbody />');
// 		$table.append($tbody);
//
// 		for (var r = 0; table.body.length > r; r++) {
// 			var rowList = table.body[r];
// 			var $row = $('<tr />');
// 			for (var c = 0; rowList.length > c; c++) {
// 				if (r === 0) {
// 					$row.append('<th>' + rowList[c] + '</th>');
// 				} else {
// 					$row.append('<td>' + rowList[c] + '</td>');
// 				}
// 			}
// 			$tbody.append($row);
// 		}
// 		$parent.append($wrapper);
// 	}
//
// 	return true;
// };
//ChatRoomViewer.prototype.tableElementHandler = function(contents, $parent) {
//
//	console.info(contents.length);
//	var $wrapper = $('<div />').addClass('inner_table');
//	for (var n = 0; contents.length > n; n++) {
//		var tableList = contents[n];
//		var $table = $('<table />').addClass('tCont').addClass('chat');
//		$wrapper.append($table);
//		var $tbody = $('<tbody />');
//		$table.append($tbody);
//
//		for (var i = 0; tableList.length > i; i++) {
//			var rowList = tableList[i];
//			var $row = $('<tr />');
//			for (var j = 0; rowList.length > j; j++) {
//				if (i === 0) {
//					$row.append('<th>' + rowList[j] + '</th>');
//				} else {
//					$row.append('<td>' + rowList[j] + '</td>');
//				}
//			}
//			$tbody.append($row);
//		}
//	}
//	$parent.append($wrapper);
//	return true;
//}
/**
* chatNum 보다 작은 메세지 (이전 메세지) 모두 읽음으로 표시
*
* @param chatNum Number
*/
ChatRoomViewer.prototype.setMessageAsRead = function(chatNum) {

	$('.right-chat-wrap', this._$chatMessageViewerContainer).each(function() {

		var id = $(this).data('message-id');
		if (chatNum >= parseInt(id)) {
			$('.bubble-round .no_read', $(this)).remove();
			$('.bubble-arrow .no_read', $(this)).remove();
			$('.bubble-templet .no_read', $(this)).remove();
		}
	});
};
/**
 * 대화 상대 활동 상태 변경
 *
 * @param userActiveStatus USER_ACTIVE_STATUS
 */
ChatRoomViewer.prototype.setUserActiveStatus = function(userActiveStatus) {

	//console.info(this._userActiveStatus, userActiveStatus);
	var $userActiveElement = $('.state_customer', this._$chatControlContainer);
	console.info(this._userActiveStatus, '->', userActiveStatus);
	if ($userActiveElement.length === 1) {
		if (this._userActiveStatus !== userActiveStatus) {
			console.info(this._userActiveStatus, '->', userActiveStatus);
			$userActiveElement.removeClass(this._userActiveStatus.toLowerCase()).addClass(userActiveStatus.toLowerCase());
			$('.state_customer', this._$chatControlContainer).attr('class', '').addClass('state_customer').addClass(userActiveStatus.toLowerCase());
			this._userActiveStatus = userActiveStatus;
			console.info("$userActiveElement.length === 1");
		}
		if(this._userActiveStatus == USER_ACTIVE_STATUS.ACTIVE)
		{
		
			$('.bubble-round .no_read').remove();
			$('.bubble-arrow .no_read').remove();
			$('.bubble-templet .no_read').remove();
		
			console.info("no_read");
		}
	}
};
/**
* 메모/메세지 수정
*
* @param message JsonObject
*/
ChatRoomViewer.prototype.editMessage = function(message) {

	if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.TEXT) {
		var $messageElement = $('.left_area[data-message-id=' + message.chatNum + ']', this._$chatMessageViewerContainer);
		if ($messageElement.length === 1) {
			$('.type_text', $messageElement).text(message.commandContents);
		} else {
			console.error('NO MESSAGE TO EDIT', message.chatNum);
		}
	} else if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
		var $messageElement = $('li[data-message-id=' + message.chatNum + ']', this._$chatMemoViewerContainer);
		if ($messageElement.length === 1) {
			$('.text_area', $messageElement).text(message.commandContents);
		} else {
			console.error('NO MESSAGE TO EDIT', message.chatNum);
		}
	} else {
		console.error('INVALID MESSAGE TYPE TO EDIT: ', message.contDivCd);
	}
};
/**
* 메모/메세지 삭제
*
* @param message JsonObject
*/
ChatRoomViewer.prototype.removeMessage = function(message) {

	console.error(message);
	if (message.contDivCd === CHAT_MESSAGE_CONTENTS_TYPE.MEMO) {
		var $messageElement = $('li[data-message-id=' + message.chatNum + ']', this._$chatMemoViewerContainer);
		if ($messageElement.length === 1) {
			$messageElement.remove();
		} else {
			console.error('NO MESSAGE TO REMOVE', message.chatNum);
		}
	} else {
		console.error('INVALID MESSAGE TYPE TO REMOVE: ', message.contDivCd);
	}
};
/**
 * 채팅 내역 삭제
 */
ChatRoomViewer.prototype.clearMessages = function() {

	this._$chatMessageViewerContainer.empty();
};

/**
 * 채팅방 열기
 */
ChatRoomViewer.prototype.openChatRoom = function() {

	console.debug('=== OPEN CHAT ROOM');
	
	//채널에 맞는 채팅 종료 사용 유무 Key 값 가져오기
	var endMsgKey = "cnsr_end_msg_use_yn";
	var endUseYn = "N";	//고객은 종료가 안됨 default N
	
	//선택된 채팅방 채널 고객채팅방일 경우 chatRoomList가 없음
	if (htClient.getChatRoomList() != null && htClient.getChatRoomList().length > 0) {
		var channel = htClient.getChatRoomList()._chatRoomList[0]._cstmLinkDivCd;
		
		switch (channel) {
			//웹
			case CHANNEL_TYPE.WEB :
				endMsgKey = "cnsr_end_msg_use_yn";
				break;
			//카카오
			case CHANNEL_TYPE.KAKAO :
				endMsgKey = "cnsr_end_msg_ko_use_yn";
				break;
			//o2
			case CHANNEL_TYPE.O2 :
				endMsgKey = "cnsr_end_msg_o2_use_yn";
				break;
			//mpop
			case CHANNEL_TYPE.MPOP :
				endMsgKey = "cnsr_end_msg_mpop_use_yn";
				break;
		}
		endUseYn = htUser.getSetting()[endMsgKey];
	}
	
	$('.dev_mid_manual').hide();
	$('.dev_mid_chat').show();

	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) { // 상담직원 채팅창
		this.clearMessages();
		// 컨트롤 영역 가리기
		if (this._$chatControlContainer) {
			$('.bottom_top_area, .textarea_area, .bottom_down_area .left', this._$chatControlContainer).show();
		}
		// 경계선 조정
		if (this._$chatControlContainer) {
			this._$chatControlContainer.css('border-top', '1px #c2c2c2');
		}
		// 높이 조정
		this._$chatMessageViewerContainer.css('height', 'calc(100% - 398px)');
		// 종료 버튼 변경
		/*if (this._$chatControlContainer && endUseYn == 'Y') {
			$('.btn_end', this._$chatControlContainer).show();
			//$('.btn_end_modify', this._$chatControlContainer).hide();
		} else if (endUseYn == 'N') {
			$('.btn_end', this._$chatControlContainer).hide();
		}*/
	} else if (htUser.getRollType() === CHAT_ROLL_TYPE.MANAGER) { // 관리자 채팅 내역

		console.debug('=== OPEN CHAT ROOM by MANAGER ROLL');
		this.clearMessages();
	}else if(htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER){		//고객 채팅 내역
	}
};
/**
* 채팅방 닫기 (종료된 방 볼 때)
*/
ChatRoomViewer.prototype.closeChatRoom = function() {

	console.debug('=== CLOSE CHAT ROOM');

	// 메세지 입력창 리셋
	this._$messageArea.val('');

	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) { // 상담직원 채팅창

		// 대화 상대 상태 아이콘 리셋
		$('.state_customer', this._$chatControlContainer).attr('class', '').addClass('state_customer').addClass('idle');
		// 컨트롤 영역 가리기
		$('.bottom_top_area, .textarea_area, .bottom_down_area .left', this._$chatControlContainer).hide();
		// 경계선 조정
		this._$chatControlContainer.css('border-top', 'none');
		// 메모 영역 감추기
		this.hideMemoContainer();
//		this.emptyMemoContainer();
//		this.clearMessages();
		// 높이 조정
		this._$chatMessageViewerContainer.css('height', 'calc(100% - 438px)');
	}
};
/**
 * 채팅창 종료
 *
 * @param reviewScore Number
 * @param reviewContents String
 * @param cnsrDivCd String 현재 채팅 상대 (C: 상담직원, R: 로봇)
 * @parma bEndStauts String 종료 처리 요청 직전의 방 상태값
 */
ChatRoomViewer.prototype.endChatRoom = function(reviewScore, reviewContents, cnsrDivCd, bEndStauts) {

	console.debug('=== END CHAT ROOM');
	console.debug('=== cnsrLinkDt' + htClient.getChatRoom._cnsrLinkDt);
	var evlYn = $("input[name='cns_evl_use_yn']").val();
	
	if(!(CHAT_ROLL_TYPE.ROBOT === cnsrDivCd && htClient.getChatRoom()._cstmLinkDivCd === CHANNEL_TYPE.O2)){
		// 종료 안내 메세지 생성 및 노출
		if($(".progress-sentence").length == 0 ){
			var $messageElement = $('<div />').addClass('progress-sentence').addClass('end').text('상담이 종료되었습니다.');
			this.addMessageElement($messageElement);
		}
		// UI 변경
		if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
			this._$chatMessageViewerContainer.addClass('end');
			
			
			if ("C" === cnsrDivCd && evlYn === 'Y') {	//상담원일 경우	
				console.log(" CHAT_ROLL_TYPE.COUNSELOR === cnsrDivCd : " + cnsrDivCd);
				if(!reviewScore){	//리뷰점수 없는 경우
					/*console.log("!reviewScore : " + reviewScore);
					console.log("_memberUid : " + htClient.getChatRoom()._memberUid);*/
					console.log("status : " + bEndStauts);
					
					if(bEndStauts * 1 > 31) {
						this.openReviewArea();
					}
					else{
						var textArea = $('#chat_bottom .inner textarea').eq(0);
						var btnClass = $('#chat_bottom .inner .btn_send_area a').eq(0);
						var chatType = $('#chat_bottom').eq(0).children().eq(0);
						 chatType.attr('class','qna-line-wrap type-on');
						 textArea.attr('placeholder','위 채팅상담 다시 시작하기 항목을 선택해 주세요.').attr('data-placeholder','위 채팅상담 다시 시작하기 항목을 선택해 주세요.').attr('readOnly',true);
						 //btnClass.attr('title','상담직원연결').text('상담직원연결').removeClass('endChat');
						 btnClass.attr('title','메세지 보내기').text('메세지 보내기').removeClass('endChat');
						 this.closeReviewArea();
					}
				}
				else {				//리뷰점수 있는 경우
					console.log("reviewScore : " + reviewScore);
					this.closeReviewArea();
				}
			}
			else {			//상담원 이외
				console.log(" CHAT_ROLL_TYPE.COUNSELOR === cnsrDivCd : " + cnsrDivCd);
				
				this.closeReviewArea();
			}
		
			
			/*console.info('reviewScore: ', reviewScore, 'reviewContents: ', reviewContents);
			if (reviewScore || CHAT_ROLL_TYPE.ROBOT === cnsrDivCd) {
				console.log("reviewScore || CHAT_ROLL_TYPE.ROBOT === cnsrDivCd");
				this.closeReviewArea();
			} else if(htClient.getChatRoom()._cnsrLinkDt === undefined || htClient.getChatRoom()._cnsrLinkDt === "" || htClient.getChatRoom()._cnsrLinkDt == null ){ //2020-06-25 evlYn 추가
			} else if(htClient.getChatRoom()._memberUid.indexOf("999999999") = 0){	
			} else if("R" === cnsrDivCd && !reviewScore){
				console.log(" CHAT_ROLL_TYPE.COUNSELOR === cnsrDivCd : " + cnsrDivCd);
				//console.log("*************************미배정 고객 종료***********2701 : " + htClient.getChatRoom()._memberUid);
				this.closeReviewArea();
			} else {
				console.log("*************************배정 상담원 종료***********2704 : " + htClient.getChatRoom()._memberUid);
				if (evlYn == 'N') this.closeReviewArea();
				else this.openReviewArea();
				console.log(" evlYn === " + evlYn);
				//this.openReviewArea();
			}*/
			$('.btn_close_chat', this._$chatRoomTitleContainer).hide();
			
			//고객이 배정 대기일 경우에만 고객 종료 20.07.07
			if(htClient._chatRoom.getStatus() === CHAT_ROOM_STATUS.WAIT_COUNSELOR){
				//20.06.22 종료 로직 추가
				$.ajax({
					url: HT_APP_PATH + '/api/customer/endChatRoom',
					method: 'get',
					data: {
						cstmUid: $('#user input[name=id]').val()
					},
					success : function(result) {
						console.debug("endChatRoom SUCCESS !!");
					}
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: END CHATROOM: ', textStatus);
				});
			}
			
		} else if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) { // 상담직원 채팅창
			this._$chatMessageViewerContainer.addClass('end');
			this.closeChatRoom();
			// 높이 조정
			this._$chatMessageViewerContainer.css('height', 'calc(100% - 260px)');
		} else {
			console.error('INVALID ROLL TYPE:', htUser.getRollType());
		}
	}else{
		this.closeReviewArea();
		$('.btn_close_chat', this._$chatRoomTitleContainer).hide();
	}
	
	this._$chatMessageViewerContainer.scrollTop(this._$chatMessageViewerContainer.prop('scrollHeight'));
};
/**
 * 리뷰 영역 보이기 (상담 종료시)
 */
ChatRoomViewer.prototype.openReviewArea = function() {
	console.debug('=== 오픈 REVIEW AREA');

	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
		
		var cns_evl_guide_msg = $("input[name='cns_evl_guide_msg']").val().replace(/(\n|\r\n)/g, '<br />');
		var $endMassege = $('<div />').addClass("bubble-wrap");
		var $viewText = '<div class="bg-chat">상담아이콘</div><div class="bubble-arrow"><span class="bg">아이콘</span>'
				+'<div class="mch ">'+cns_evl_guide_msg+'</div></div></div>'
				+'<div class="bubble-wrap review-message"><div class="bubble-round"><div class="mch"><dl class="service-wrap">'
				+'<dt class="tit">상담 만족도를 평가해주세요.</dt><dd class="info-text">보다 나은 서비스를 위해 답변의 만족여부를 선택해 주시면 적극 반영하도록 하겠습니다. </dd>'
				+'<dd class="star-line"><span class="icon_star off on">별</span><span class="icon_star off on">별</span><span class="icon_star off on">별</span><span class="icon_star off on">별</span><span class="icon_star off on">별</span></dd>'
				//<dd id="review_decs" class="description">3점(보통이에요)</dd>
				+'</dl></div></div></div>';
		$endMassege.append($viewText);
		
		this._$messageArea.val('');
		this.addMessageElement($endMassege);
		
		$("#chat_bottom .inner").hide();
		$("#chat_bottom .review_inner").show();
		//this._$chatControlContainer.html($('#chat_bottom_end').children());
		//this._$chatMessageViewerContainer.css('height', 'calc(100% - 230px)');
	} else {
		console.error('INVALID ROLL TYPE:', htUser.getRollType());
	}
};
/**
 * 리뷰 영역 가리기 (상담 종료시)
 */
ChatRoomViewer.prototype.closeReviewArea = function() {

	console.debug('=== CLOSE REVIEW AREA');

	if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창
		this._$chatControlContainer.remove();
		$(".review-message").hide();
		this._$chatMessageViewerContainer.css('height', 'calc(100% - 60px)');
	} else {
		console.error('INVALID ROLL TYPE:', htUser.getRollType());
	}
};
/**
 * 메타 정보 세팅
 *
 * @param data JsonObject [chatRoom|cstmInfo]
 */
ChatRoomViewer.prototype.setMetaInfo = function(data) {

	console.info('=== SET META INFO', data);

	if (this._$chatRoomTitleContainer) {

		if (data.chatRoomUid) { // 채팅방 정보
			// 채팅방 상태
			$('.labelBlue2', this._$chatRoomTitleContainer).text(
					CHAT_ROOM_STATUS_TITLE_FOR_META_INFO[htUtils.getKeyFromValue(CHAT_ROOM_STATUS, data.chatRoomStatusCd)]);
			// 채팅방 제목
			$('.room_title', this._$chatRoomTitleContainer).text(data.roomTitle);
			// 채팅방 시작 시간
			$('.room_start_datetime', this._$chatRoomTitleContainer).text(data.roomCreateDtPretty);
			// 채팅방 UID 세팅
			this._$chatRoomTitleContainer.attr('data-chat-room-uid', data.chatRoomUid);
			// 분류
			var categoryNameList = data.ctgNm1;
			if (data.ctgNm2) categoryNameList += ' > ' + data.ctgNm2;
			if (data.ctgNm3) categoryNameList += ' > ' + data.ctgNm3;
			if (categoryNameList) {
				$('li.category_info .text_area span', this._$chatRoomMetaInfoContainer).text(categoryNameList);
			}
		}

		if (data.cstmUid) { // 고객 정보 (채팅방 로드시)

			// 고객 '고객사 아이디'
			if (data.roomCocId) {
				$('li.customer_info .text_area span.target_customer_id'
						, this._$chatRoomMetaInfoContainer).text(data.roomCocId);
				$('li.customer_info .text_area .target_customer_id'
						, this._$chatRoomMetaInfoContainer).show();
			} else {
				$('li.customer_info .text_area .target_customer_id'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 이름
			if (data.roomCocNm) {
				$('li.customer_info .text_area .target_customer_name'
						, this._$chatRoomMetaInfoContainer).text(data.roomCocNm).show();
				$('#dev_tabMenu5').trigger('click', 'setMetaInfo');
				$('#titleCocNm').text(data.roomCocNm);
			} else {
				$('li.customer_info .text_area .target_customer_name'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 코끼리 표시
			if (data.gradeNm === CUSTOMER_GRADE_NAME.ELEPHANT || data.gradeNm === CUSTOMER_GRADE_NAME.VIP || data.gradeNm === CUSTOMER_GRADE_NAME.BLACK) {
				$('li.customer_info .text_area .target_elf_info'
						, this._$chatRoomMetaInfoContainer).html($('<i />')
								.addClass(data.gradeImgClassName)
								.attr('title', data.gradeMemo)
								.text(data.gradeNm)).show();
				$('li.dev_elephant_area .text_area .target_elf_info'
						, this._$chatRoomMetaInfoContainer).text(data.gradeMemo);
				$('.dev_elephant_area', this._$chatRoomMetaInfoContainer).show();
			} else {
				$('li.customer_info .text_area .target_elf_info'
						, this._$chatRoomMetaInfoContainer).hide();
				$('li.dev_elephant_area .text_area .target_elf_info'
						, this._$chatRoomMetaInfoContainer).text('');
				$('.dev_elephant_area', this._$chatRoomMetaInfoContainer).hide();
			}			
			// 고객 인증
			if (data.authTypeName) {
				$('li.customer_info .text_area .target_auth_info'
						, this._$chatRoomMetaInfoContainer).text('(인증: ' +  data.authTypeName + ')').show();
			} else {
				$('li.customer_info .text_area .target_auth_info'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 생년월일
			if (data.cstmBirthDay) {
				$('li.customer_info .text_area .target_customer_birthday'
						, this._$chatRoomMetaInfoContainer).text(data.cstmBirthDay).show();
			} else {
				$('li.customer_info .text_area .target_customer_birthday'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 성별
			if (data.cstmGender) {
				$('li.customer_info .text_area .target_customer_gender'
						, this._$chatRoomMetaInfoContainer).text(data.cstmGender).show();
			} else {
				$('li.customer_info .text_area .target_customer_gender'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 인입채널
			if (data.cstmLinkDivCdNm) {
				if(data.cstmLinkDivCd && data.cstmLinkDivCd == 'C'){	//인입채널이 O2일 경우
					if(data.screenName){	//인입채널이 O2이면서 화면명 정보가 있을 경우
						$('li.customer_info .text_area .target_customer_channel'
								, this._$chatRoomMetaInfoContainer).text(data.cstmLinkDivCdNm + " - " + data.screenName).show();
					}else{
						$('li.customer_info .text_area .target_customer_channel'
								, this._$chatRoomMetaInfoContainer).text(data.cstmLinkDivCdNm).show();
					}
				}else{
					$('li.customer_info .text_area .target_customer_channel'
							, this._$chatRoomMetaInfoContainer).text(data.cstmLinkDivCdNm).show();
				}
				if(data.cstmLinkDivCd && data.cstmLinkDivCd == 'B'){	//인입채널이 카카오일 경우
					$("#btnEndKakao").show();
				}
				else $("#btnEndKakao").hide();
			} else {
				$('li.customer_info .text_area .target_customer_channel'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 디바이스
			if (data.cstmOsDivCdNm) {
				$('li.customer_info .text_area .target_customer_device'
						, this._$chatRoomMetaInfoContainer).text(data.cstmOsDivCdNm).show();
			} else {
				$('li.customer_info .text_area .target_customer_device'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객사 고객 구분
			if (data.cstmCocTypeNm) {
				$('li.customer_info .text_area .target_customer_type'
						, this._$chatRoomMetaInfoContainer).text(data.cstmCocTypeNm).show();
			} else {
				$('li.customer_info .text_area .target_customer_type'
						, this._$chatRoomMetaInfoContainer).hide();
			}
		}

		if (data.cstm_uid) { // 고객 정보 (인증 완료 메세지 수신시)
			console.error('DEPRECATED');
			// 고객 '고객사 아이디'
//			if (data.room_coc_id) {
//				$('li.customer_info .text_area span.target_customer_id'
//						, this._$chatRoomMetaInfoContainer).text(data.room_coc_id);
//				$('li.customer_info .text_area .target_customer_id'
//						, this._$chatRoomMetaInfoContainer).show();
//			} else {
//				$('li.customer_info .text_area .target_customer_id'
//						, this._$chatRoomMetaInfoContainer).hide();
//			}
			// 고객 이름이 바뀌었을 경우 상담이력 탭 클릭 (trigger)
			/*
			var oldCstmName = $('li.customer_info .text_area .target_customer_name'
					, this._$chatRoomMetaInfoContainer).text();
			if (oldCstmName !== data.name) {
				$('#dev_tabMenu1').trigger('click', 'setMetaInfo');
			}*/
			// 고객 이름
//			if (data.room_coc_nm) {
//				$('li.customer_info .text_area .target_customer_name'
//						, this._$chatRoomMetaInfoContainer).text(data.room_coc_nm).show();
//			} else {
//				$('li.customer_info .text_area .target_customer_name'
//						, this._$chatRoomMetaInfoContainer).hide();
//			}
			// 고객 인증
//			if (data.auth_type_name) {
//				$('li.customer_info .text_area .target_auth_info'
//						, this._$chatRoomMetaInfoContainer).text('(인증: ' +  data.auth_type_name + ')').show();
//			} else {
//				$('li.customer_info .text_area .target_auth_info'
//						, this._$chatRoomMetaInfoContainer).hide();
//			}
			// 고객 생년월일
			/*
			if (data.birth_date) {
				$('li.customer_info .text_area .target_customer_birthday'
						, this._$chatRoomMetaInfoContainer).text(data.birth_date).show();
			} else {
				$('li.customer_info .text_area .target_customer_birthday'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 성별
			if (data.sex) {
				$('li.customer_info .text_area .target_customer_gender'
						, this._$chatRoomMetaInfoContainer).text(data.sex).show();
			} else {
				$('li.customer_info .text_area .target_customer_gender'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 인입채널
			if (data.cstm_link_div_cd_nm) {
				$('li.customer_info .text_area .target_customer_channel'
						, this._$chatRoomMetaInfoContainer).text(data.cstm_link_div_cd_nm).show();
			} else {
				$('li.customer_info .text_area .target_customer_channel'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객 디바이스
			if (data.cstm_link_div_nm) {
				$('li.customer_info .text_area .target_customer_device'
						, this._$chatRoomMetaInfoContainer).text(data.cstm_link_div_nm).show();
			} else {
				$('li.customer_info .text_area .target_customer_device'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			// 고객사 고객 구분
			if (data.cstm_coc_type_nm) {
				$('li.customer_info .text_area .target_customer_type'
						, this._$chatRoomMetaInfoContainer).text(data.cstm_coc_type_nm).show();
			} else {
				$('li.customer_info .text_area .target_customer_type'
						, this._$chatRoomMetaInfoContainer).hide();
			}
			*/
		}
	}
};
/**
 * 상담직원 연결 버튼 숨기기
 */
ChatRoomViewer.prototype.hideRequestCounselorButton = function() {

	$('.btn_hot_key.requestCounselor', this._$chatMessageViewerContainer).each(function() {
		$(this).hide();
	});
};
/**
 * 보내기 버튼 활성화
 */
ChatRoomViewer.prototype.sendButtonOn = function() {

	this._$messageSendButton.removeClass('out');
};
/**
 * 보내기 버튼 비활성화
 */
ChatRoomViewer.prototype.sendButtonOff = function() {

	this._$messageSendButton.addClass('out');
};
/**
 * 첫번째 메세지 엘리먼트
 */
//ChatRoomViewer.prototype.getFirstMessageElement = function() {
//
//	return $('.message_box:first', this._$chatMessageViewerContainer);
//}
/**
 * 첫번째 메세지 엘리먼트의 스크롤 위치 조정
 */
//ChatRoomViewer.prototype.setScrollPosition = function($element) {
//
//	this._$chatMessageViewerContainer.scrollTop($element.height());
//}


ChatRoomViewer.prototype.sendPushAlarm = function(channel, cocId) {
	$.ajax({
		url: HT_APP_PATH + '/eaiums/sendUms',
		method: 'post',
		data: {
			entityId: cocId
			, cstmLinkDivCd: channel
			, pushType: "고객상태 idle"
			, pushMsg: "고객님. 문의하신 내용에 대한 답변이 도착하였습니다."
		},
		success : function(result) {
			console.debug("push SUCCESS !!");
		}
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL push: ', textStatus);
	});
}