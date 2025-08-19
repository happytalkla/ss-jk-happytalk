'use strict';

// ////////////////////////////////////////////////////////////////////////////
// ChatRoomList Class
// ////////////////////////////////////////////////////////////////////////////
/**
 * 채팅방 목록
 *
 * @param viewer ChatRoomListViewer
 */
function ChatRoomList(viewer) {

	// 채팅방 목록
	this._chatRoomList = [];
	// 목록 관련 변수
	this._page = 1;
	this._requestChatRoomList = null;
	// 현재 열려있는 채팅방
	//this._currentChatRoom = null;
	// 채팅방 뷰어 객체
	this._viewer = viewer;
	// 채팅방 상태 카운터
	this._statusCounter = {
			assign_robot: 0, // 챗봇 대화중
			wait_counselor: 0, // 미배정
			assign_counselor: 0, // 미접수
			need_answer: 0, // 상담직원 대화중 (응대 필요)
			wait_reply: 0, // 상담직원 대화중 (고객 답변 대기중)
			end_counselor: 0, // 상담직원 대화중 종료
			end_robot: 0, // 챗봇 대화중 종료
			request_change_counselor: 0, // 상담직원 변경 요청
			request_manager_counsel: 0, // 매니저 상담 요청
			request_review: 0, // 내용 검토 요청
			request_cstm_grade: 0, // 코끼리 등록 요청
			all: 0 // 전체
	};
}
/**
 * 채탕방 목록 검색
 */
ChatRoomList.prototype.queryChatRoomList = function() {

	// 채팅방 목록 리셋
	this.resetChatRoomList();

	// 쿼리
	var status = $('li a.active', this._viewer._$statusCounterContainer).closest('li').attr('class');
	var sort = $('.list_filter', this._viewer._$searchOptionContainer).attr('data-order-current');
	var q = $('input[type=text]', this._viewer._$searchContainer).val().trim();
	this.loadChatRoomList(status, 1, sort, q);
};
/**
 * 채팅방 목록 다음 페이지 로딩
 */
ChatRoomList.prototype.loadNextPage = function() {

	// 다음 페이지 가져오는 동안 스크롤 이벤트 삭제, 페이지 로딩 완료시 재등록
	this._viewer.removeScrollEvent();

	// 쿼리
	var status = $('li a.active', this._viewer._$statusCounterContainer).closest('li').attr('class');
	var sort = $('.list_filter', this._viewer._$searchOptionContainer).data('order-current');
	var q = $('input[type=text]', this._viewer._$searchContainer).val().trim();
	console.debug('sort: ', sort);
	this.loadChatRoomList(status, this._page + 1, sort, q);
};
/**
 * 채팅방 목록 리셋
 */
ChatRoomList.prototype.resetChatRoomList = function() {

	while (this._chatRoomList.length > 0) {
		var chatRoom = this._chatRoomList[0];
		this.removeChatRoom(chatRoom.getId());
	}
};
/**
 * 채팅방 목록 가져오기
 *
 * 1. ChatRoomList 생성시
 * 2. Client 재연결시
 * 3. 검색, 정렬 변경시
 * 4. 페이징
 *
 * @param statusName String
 * @param page Integer
 * @param sort String ex, chat+desc
 * @param q String
 * @param since String
 */
ChatRoomList.prototype.loadChatRoomList = function(statusName, page, sort, q, since) {

	console.debug('=== LOAD CHAT ROOM LIST, REQUEST AJAX: ', this._requestChatRoomList);
	statusName = statusName || 'all';
	var status = CHAT_ROOM_STATUS[statusName.toUpperCase()];
	page = page || 1;
	sort = sort || null;
	since = since || null;
	q = q || null;

	var reqParams = {
			userId: htUser.getId(),
			rollType: htUser.getRollType(), // 실제 유저 타입 아닌 상담직원 페이지에 필요한 타입
			status: status,
			page: page,
			sort: sort,
			since: since,
			q: q
	};

	var self = this;
	if (this._requestChatRoomList != null) {
		console.info('=== ABORT LOAD CHAT ROOM LIST');
		this._requestChatRoomList.abort();
		this._requestChatRoomList = null;
	}
	this._requestChatRoomList = $.ajax({
		url: HT_APP_PATH + '/api/chat/room',
		method: 'post',
		data: reqParams
	}).done(function(data) {
		if (data.length > 0) {
			// 채팅방 추가
			for (var i = 0; data.length > i; i++) {
				var item = data[i];
				self.addChatRoom(item);
			}

			// 채팅방 상태별 카운터 업데이트
//			self.getStatusCounter();

			// '현재 페이지 번호(this._page)'를 가져온 페이지 번호(page)로 세팅, 콜러에서 '현재 페이지 번호(this._page) + 1'로 호출함
			self._page = page;
			// 스크롤 이벤트 등록
			self._viewer.addScrollEvent();
		}
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: LOAD CHATROOM LIST: ', textStatus);
	}).always(function() {
		self._requestChatRoomList = null;
	});
};
/**
 * 마지막 채팅방
 */
ChatRoomList.prototype.getLastChatRoom = function() {

	if (this._chatRoomList.length > 0) {
		return this._chatRoomList[this._chatRoomList.length - 1];
	}

	return null;
};
/**
 * 'chatRoomUid'에 해당하는 채팅방
 *
 * @param chatRoomUid String
 */
ChatRoomList.prototype.getChatRoom = function(chatRoomUid) {

	for (var i = 0; i < this._chatRoomList.length; i++) {

		var chatRoom = this._chatRoomList[i];
		if (chatRoom.getId() === chatRoomUid) {
			return chatRoom;
		}
	}

	return null;
};
/**
 * 채팅방을 '채팅방 목록'에 추가
 *
 * @param chatRoom JsonObject
 */
ChatRoomList.prototype.addChatRoom = function(chatRoom) {

	console.debug('ADD CHATROOM: ', this._chatRoomList, chatRoom);
	var prevChatRoom = this.getChatRoom(chatRoom.chatRoomUid);
	if (prevChatRoom === null) {
		var c = new ChatRoom(chatRoom);
		this._chatRoomList.push(c);
		this._viewer.addChatRoom(c);
	} else {
		this.updateChatRoom(prevChatRoom, chatRoom);
	}
};
/**
 * '채팅방 목록'에 있는 채팅방을 업데이트 (상태, 마지막 채팅 내용, 아이콘 등)
 *
 * @param destChatRoom ChatRoom
 * @param srcChatRoom JsonObject
 */
ChatRoomList.prototype.updateChatRoom = function(destChatRoom, srcChatRoom) {

	console.info('=== UPDATE CHATROOM: ', this._chatRoomList, srcChatRoom);
	console.info('=== PREV STATUS: ', destChatRoom.getStatus(), ', INCOMING STATUS: ', srcChatRoom.chatRoomStatusCd);
	// 상태 변경
//	if (destChatRoom.getStatus() !== srcChatRoom.chatRoomStatusCd) {
//		console.info('=== CHANGE CHATROOM STATUS: ', destChatRoom.getStatus(), ' to ', srcChatRoom.chatRoomStatusCd);
//		this.changeChatRoomStatus(destChatRoom, srcChatRoom);
//	}
	// 마지막 메세지 변경
	if (destChatRoom.getLastChatMessageOfChatRoomList().chatNum !== srcChatRoom.lastChatNum) {
		console.info('=== CHANGE LAST CHAT MESSAGE: ', destChatRoom.getLastChatMessageOfChatRoomList(), ' to ', srcChatRoom.lastChatCont);
		this.changeLastChatMessageOfChatRoomList(destChatRoom, srcChatRoom);
	}
	// 아이콘 변경: 아이콘 비교는 뷰 단에서 처리
	console.info('=== CHANGE CHATROOM ICONS');
	this.changeChatRoomIcons(destChatRoom, srcChatRoom);
};
/**
 * 'chatRoomUid'에 해당하는 채팅방을 '채팅방 목록'에서 삭제
 *
 * @param chatRoomUid String
 */
ChatRoomList.prototype.removeChatRoom = function(chatRoomUid) {

	console.info('=== REMOVE CHATROOM LIST: ', chatRoomUid);

	for (var i = 0; i < this._chatRoomList.length; i++) {
		var chatRoom = this._chatRoomList[i];
		if (chatRoom.getId() === chatRoomUid) {
			this._chatRoomList.splice(i, 1);
			break;
		}
	}

	this._viewer.removeChatRoom(chatRoomUid);
};
/**
 * 전체 채팅방 상태별 카운트
 */
ChatRoomList.prototype.getStatusCounter = function(userType) {

	console.debug('UPDATE STATUS COUNTER');
	console.debug('getRollType : ' + htUser.getRollType());
	console.debug('********** departcd : ' + htUser.getDepartCd());
	console.debug('********** getusertype : ' + htUser.getUserType());
	
	var self = this;
	if(htUser.getDepartCd() == "") return false;
	$.ajax({
		url: HT_APP_PATH + '/api/chat/room/status/counter',
		data: {
			userId: htUser.getId(),
			departCd: htUser.getDepartCd(),
			rollType: htUser.getRollType(),
		},
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: GET STATUS COUNTER: ', textStatus);
	}).done(function(data) {
		self.setStatusCounter(data);
	});
};
/**
 * 전체 채팅방 상태별 카운트 세팅
 *
 * @param statusCounter Object
 */
ChatRoomList.prototype.setStatusCounter = function(statusCounter) {

	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
		if (statusCounter.assign_counselor > this._statusCounter.assign_counselor) {
			htUtils.noti('미접수 중인 채팅방이 있습니다.');
		}
		if (statusCounter.need_answer > this._statusCounter.need_answer) {
			htUtils.noti('고객이 답변을 기다리고 있습니다.');
		}
	}
	this._statusCounter = statusCounter;

	// 뷰 업데이트
	this._viewer.updateStatusCounter(this._statusCounter);
};
/**
 * 전체 채팅방 상태별 카운트 추가 (새 채팅방 추가시)
 *
 * @param status CHAT_ROOM_STATUS
 */
/*ChatRoomList.prototype.addStatusCounter = function(status) {

	var statusKey = htUtils.getKeyFromValue(CHAT_ROOM_STATUS, status);
	console.assert(statusKey !== null);

	statusKey = statusKey.toLowerCase();
	console.info('=== ADD CHATROOM STATUS: ', statusKey);
	console.info(this._statusCounter);

	this._statusCounter[statusKey]++;
	this._statusCounter['all']++;
	console.info(this._statusCounter);

//	for (var key in statusCounter) {
//		if (statusCounter.hasOwnProperty(key)) {
//			this._statusCounter[key] += statusCounter[key];
//		}
//	}

	// 뷰 업데이트
	this._viewer.updateStatusCounter(this._statusCounter);
};*/
/**
 * 전체 채팅방 상태별 카운트 업데이트
 *
 * @param asIs CHAT_ROOM_STATUS
 * @param toBe CHAT_ROOM_STATUS
 */
/*ChatRoomList.prototype.updateStatusCounter = function(asIs, toBe) {

	var asIsKey = htUtils.getKeyFromValue(CHAT_ROOM_STATUS, asIs);
	var toBeKey = htUtils.getKeyFromValue(CHAT_ROOM_STATUS, toBe);
	console.assert(asIsKey !== null);
	console.assert(toBeKey !== null);

	asIsKey = asIsKey.toLowerCase();
	toBeKey = toBeKey.toLowerCase();
	console.info('=== CHANGE CHATROOM STATUS: ', asIsKey, ' to ', toBeKey);
	console.info(this._statusCounter);

	console.assert(this._statusCounter[asIsKey] > 0);
	this._statusCounter[asIsKey]--;
	this._statusCounter[toBeKey]++;
	console.info(this._statusCounter);

//	for (var key in statusCounter) {
//		if (statusCounter.hasOwnProperty(key)) {
//			this._statusCounter[key] += statusCounter[key];
//		}
//	}

	// 뷰 업데이트
	this._viewer.updateStatusCounter(this._statusCounter);
};*/
/**
 * 채팅방의 상태값을 변경
 *
 * @param chatRoom ChatRoom
 * @param chatRoomMessage JsonObject
 */
/*ChatRoomList.prototype.changeChatRoomStatus = function(chatRoom, chatRoomMessage) {

	var prevStatus = chatRoom.getStatus();
	chatRoom.changeChatRoomStatus(chatRoomMessage);
	this.updateStatusCounter(prevStatus, chatRoomMessage.chatRoomStatusCd);

	// 뷰 업데이트
	this._viewer.changeChatRoomStatus(chatRoom);
};*/
/**
 * 채팅방의 마지막 메세지 관련 필드 변경 (메세지 번호, 메세지, 날짜)
 *
 * @param chatRoom ChatRoom
 * @param chatRoomMessage JsonObject
 */
ChatRoomList.prototype.changeLastChatMessageOfChatRoomList = function(chatRoom, chatRoomMessage) {

	chatRoom.changeLastChatMessageOfChatRoomList(chatRoomMessage);

	// 뷰 업데이트
	this._viewer.changeLastChatMessageOfChatRoomList(chatRoom);
};
/**
 * 채팅방의 아이콘 관련 필드 변경 (경과 시간, 깃발, 코끼리 등)
 *
 * @param chatRoom ChatRoom
 * @param chatRoomMessage JsonObject
 */
ChatRoomList.prototype.changeChatRoomIcons = function(chatRoom, chatRoomMessage) {

	chatRoom.changeChatRoomIcons(chatRoomMessage);

	// 뷰 업데이트
	this._viewer.changeChatRoomIcons(chatRoom);
};
//////////////////////////////////////////////////////////////////////////////
//ChatRoom List Viewer
//////////////////////////////////////////////////////////////////////////////
function ChatRoomListViewer(DOMElements) {

	this._$statusCounterContainer = DOMElements.statusCounterContainer;
	this._$chatRoomListTitleContainer = DOMElements.chatRoomListTitleContainer;
	this._$searchContainer = DOMElements.searchContainer;
	this._$searchOptionContainer = DOMElements.searchOptionContainer;
	this._$chatRoomListContainer = DOMElements.chatRoomListContainer;
	this._$chatRoomListContainerWrapper = DOMElements.chatRoomListContainerWrapper;
	var self = this;

	// ////////////////////////////////////////////////////////////////////////
	// 상태 메뉴 선택 (.counseling_left .tabs_icon)
	if (this._$statusCounterContainer && htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
		this._$statusCounterContainer.on('click', 'li a', function(e) {

			e.preventDefault();
			e.stopPropagation();

			$('li a.active', this._$statusCounterContainer).removeClass('active');
			$(this).addClass('active');

			var title = $('i', $(this)).text().trim();
			$('span', self._$chatRoomListTitleContainer).text(title);

			// 상담 종료 탭에서는 후처리순 정렬 보이기
			if ($(this).parent().hasClass('end_counselor')) {
				$('.end_review', self._$searchOptionContainer).show();
			} else {
				$('.end_review', self._$searchOptionContainer).hide();
			}

			htClient.getChatRoomList().queryChatRoomList();
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 채팅방 목록 검색
	if (this._$searchContainer) {
		// 엔터로 검색
		this._$searchContainer.on('keyup', '#q', function(e) {

			e.preventDefault();
			e.stopPropagation();

			if (e.keyCode === 13) { // 엔터시 검색
				htClient.getChatRoomList().queryChatRoomList();
			}
		});
		// 버튼 클릭시 검색
		this._$searchContainer.on('click', '.btn_go_search', function(e) {

			e.preventDefault();
			e.stopPropagation();

			htClient.getChatRoomList().queryChatRoomList();
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 채팅방 목록 정렬 변경
	if (this._$searchOptionContainer) {
		this._$searchOptionContainer.on('click', '.list_filter a', function(e) {

			e.preventDefault();
			e.stopPropagation();

			var $parent = $(this).closest('.list_filter');

			// $parent > data-order-current 값을 세팅
			if ($(this).hasClass('active')) { // 현재 활성화된 정렬 값이면 반대(data-order-reverse)로 정렬
				if ($(this).hasClass('order_default')) {
					$(this).removeClass('order_default').addClass('order_reverse');
					$parent.attr('data-order-current', $(this).data('order-reverse'));
				} else {
					$(this).removeClass('order_reverse').addClass('order_default');
					$parent.attr('data-order-current', $(this).data('order-default'));
				}
			} else { // 현재 활성화되지 않은 정렬 값이면 해당 정렬 값의 기본값(data-order-default)으로 정렬
				$('a.active', $parent).removeClass('active').removeClass('order_default').removeClass('order_reverse');
				$(this).addClass('active').addClass('order_default');
				$parent.attr('data-order-current', $(this).data('order-default'));
			}

			// $parent > data-order-current 값으로 CSS 화살표 변경
			var order = $parent.attr('data-order-current');
			console.debug(order);
			$('a', $(this).closest('.list_filter')).removeClass('desc').removeClass('asc');
			$('a i', $(this).closest('.list_filter')).remove();
			console.debug(order.indexOf('asc'));
			if (order.indexOf('asc') > -1) {
				$(this).addClass('asc');
			} else {
				$(this).addClass('desc');
			}
			$(this).append('<i />');

			htClient.getChatRoomList().queryChatRoomList();
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 채팅방 목록 페이징
	// 스크롤 이벤트 콜백
	this._scrollCallback = function(e) {
		var scrollPosOffset = Math.round(self._$chatRoomListContainerWrapper.height() * 0.1);
		var scrollPosTrigger = self._$chatRoomListContainer.height() - self._$chatRoomListContainerWrapper.height() - scrollPosOffset;
		if ($(this).scrollTop() > scrollPosTrigger) {
			self._$chatRoomListContainer.trigger('loadNextPage');
		}
	}

	// '다음 페이지 로딩' 이벤트 등록
	if (this._$chatRoomListContainer) {
		this._$chatRoomListContainer.on('loadNextPage', function(e) {
			console.info(this, self);
			htClient.getChatRoomList().loadNextPage();
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 채팅방 선택
	if (this._$chatRoomListContainer) {
		this._$chatRoomListContainer.on('click', '.chat-room', function(e) {

			$('#autoCmpContent').hide();
			
			// 메뉴얼 숨기기
			$('.dev_mid_manual').hide();
			$('.dev_mid_chat').show();

			// 현재 채팅방
			var currentChatRoom = htClient.getChatRoom();

			// 새로 입장할 채팅방
			var chatRoomUid = $(this).data('room-id');
			var newChatRoom = htClient.getChatRoomList().getChatRoom(chatRoomUid);
			if (newChatRoom === null) {
				console.error('NO CHAT ROOM', chatRoomUid);
			}

			// 현재 열려있는 채팅방에 들어가려할 경우 무시
			if (currentChatRoom && currentChatRoom.getId() === newChatRoom.getId()) {
				return;
			}

			// 수동 배정인 경우, 상담직원이 직접 배정
			/*
			console.info('rollType: ', htUser.getRollType(), ', status: ', newChatRoom.getStatus(), ', auto_mat_use_yn: ', htUser.getSetting().auto_mat_use_yn)
			if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR // 상담직원 페이지인 경우
					&& newChatRoom.getStatus() === CHAT_ROOM_STATUS.WAIT_COUNSELOR // 상담직원 대기인 경우
					&& htUser.getSetting().auto_mat_use_yn === 'N') { // 수동 배정인 경우

				console.info('=== ENTER AFTER ASSIGN');
				$.ajax({
					//async: false,
					url: HT_APP_PATH + '/api/chat/room/' + (currentChatRoom ? currentChatRoom.getId() : newChatRoom.getId()),
					method: 'put',
					timeout: 1000,
					data: {
						command: CHAT_COMMAND.ASSIGN_COUNSELOR_SELF,
						userId: htUser.getId(),
					},
				}).done(function(data) {
					console.debug(data);
					self.loadChatRoom(newChatRoom, currentChatRoom);
					//self.loadCustomerHistory(htClient.getChatRoom().getCustomerId());
				}).fail(function(jqXHR, textStatus, errorThrown) {
					// TODO: 에러처리 및 안내
					console.error('FAIL REQUEST: ASSIGN COUNSELOR SELF: ', textStatus);
				}).always(function() {
					;
				});
			} else {
				console.info('=== ONLY ENTER NOT ASSIGN');
				self.loadChatRoom(newChatRoom, currentChatRoom);
				//self.loadCustomerHistory(htClient.getChatRoom().getCustomerId());
			}*/
			
			self.loadChatRoom(newChatRoom, currentChatRoom);
		});
	}
}
/**
 * 채팅방 로드
 *
 * @param ChatRoom newChatRoom
 * @param ChatRoom currentChatRoom
 */
ChatRoomListViewer.prototype.loadChatRoom = function(newChatRoom, currentChatRoom) {

	// 기존 채팅방 닫기, 새 채팅방 뷰어 설정
	if (currentChatRoom) {
		currentChatRoom.closeChatRoom();
		newChatRoom.setViewer(currentChatRoom.getViewer());
	} else {
		var chatRoomViewer = new ChatRoomViewer({
			chatMessageViewerContainer: $('#chat_body'),
			chatControlContainer: $('#chat_bottom'),
			messageArea: $('#chat_bottom textarea'),
			messageSendButton: $('#chat_bottom .btn_area button'),
			chatRoomTitleContainer: $('.counseling_mid .counsel_title_area'),
			chatRoomMetaInfoContainer: $('.counseling_mid .counsel_chat_info'),
		});
		newChatRoom.setViewer(chatRoomViewer);
	}

	newChatRoom.getViewer().openChatRoom();

	// 현재 진행중인 채팅창 변경
	htClient.setChatRoom(newChatRoom);

	// 새 채팅방에 가입
	htClient.subscribeChatRoom();
}
/**
 * 고객 상담 이력
 *
 * @param String customerId
 */
//ChatRoomListViewer.prototype.loadCustomerHistory = function(customerId) {
//
//	$.ajax({
//		url: HT_APP_PATH + '/api/customer/' + customerId,
//		dataType: 'html',
//	}).done(function(data) {
//
//		$('.counseling_right .tmenu_top_cont .wScroll').html(data);
//
//	}).fail(function(jqXHR, textStatus, errorThrown) {
//
//		console.error('FAIL REQUEST: CUSTOMER HISTORY: ', textStatus);
//	});
//}
/**
* 채팅방 추가
*
* @param ChatRoom chatRoom
*/
ChatRoomListViewer.prototype.addChatRoom = function(chatRoom) {

	this.addChatRoomElement(this.buildChatRoomElement(chatRoom));
}
/**
* 채팅방 엘레먼트 추가
*
* @param jQueryObject chatRoomElement
*/
ChatRoomListViewer.prototype.addChatRoomElement = function(chatRoomElement) {

	this._$chatRoomListContainer.append(chatRoomElement);
}
var MAX_CONTENTS_SIZE = 20;
var NO_TEXT_MESSAGE = '파일 메세지';
var INVALID_TEXT_MESSAGE = "메세지 내용을 표시할 수 없습니다.";
/**
* 채팅방 엘레먼트 생성
*
* @param ChatRoom chatRoom
* @return jQueryObject
*/
ChatRoomListViewer.prototype.buildChatRoomElement = function(chatRoom) {

	console.debug(chatRoom);

	var $chatRoomElement = $('<a />').addClass('chat-room')
						.attr('href', 'javascript:void(0);')
						.attr('data-room-id', chatRoom.getId())
						.attr('data-room-status', chatRoom.getStatus());

	// 아이콘 영역
	var $iconAreaElement = $('<div />').addClass('icon_area');
	if (chatRoom._cstmLinkDivCd === CHANNEL_TYPE.KAKAO) { // 채널 아이콘
		$iconAreaElement.append($('<i />').addClass('icon_kakao').text('카카오톡').attr('title', '카카오톡'));
	} else if (chatRoom._cstmLinkDivCd === CHANNEL_TYPE.O2) {
		$iconAreaElement.append($('<i />').addClass('icon_o2').text('O2톡').attr('title', 'O2톡'));
	} else if (chatRoom._cstmLinkDivCd === CHANNEL_TYPE.MPOP) {
		$iconAreaElement.append($('<i />').addClass('icon_mpop').text('mPOP톡').attr('title', 'mPOP톡'));
	} else {
		$iconAreaElement.append($('<i />').addClass('icon_happytalk').text('삼성증권 웹채팅').attr('title', '삼성증권 웹 채팅'));
	}
	if ('Y' === htUser.getSetting().pass_time_use_yn && chatRoom._passHours) { // 경과 시간 아이콘
		$iconAreaElement.append($('<i />').addClass('icon_time').attr('title', '상담 시작 시간: ' + chatRoom._roomCreateDtPretty).text(chatRoom._passHours));
	}
	if (chatRoom._markImgClassName) { // 깃발 아이콘
		if (chatRoom._markImgClassName === 'icon_flag_cancel') {
			;
		} else {
			$iconAreaElement.append($('<i />').addClass('icon_flag').addClass(chatRoom._markImgClassName)
					.attr('title', chatRoom._markDesc)
					.text(chatRoom._markDesc));
		}
	}
	if (chatRoom._gradeNm === CUSTOMER_GRADE_NAME.ELEPHANT || chatRoom._gradeNm === CUSTOMER_GRADE_NAME.VIP || chatRoom._gradeNm === CUSTOMER_GRADE_NAME.BLACK) { // 고객 식별 아이콘
		console.debug(chatRoom);
		$iconAreaElement.append($('<i />')
				.addClass(chatRoom._gradeImgClassName)
				.attr('title', chatRoom._gradeMemo)
				.text(chatRoom._gradeNm));
	}
	if (chatRoom._managerCounselImgClassName === 'MANAGER_COUNSEL') { // 매니저 상담 요청 아이콘
		console.debug(chatRoom);
		$iconAreaElement.append($('<i />')
				.addClass('icon_manager').text('매니저 상담 요청').attr('title', '매니저 상담 요청'));
	}

	// 고객사 아이디
	if (chatRoom._roomCocId) {
		$iconAreaElement.append($('<span />').addClass('right_id').text('@' + chatRoom._roomCocId));
	} else {
		$iconAreaElement.append($('<span />').addClass('right_id').text(''));
	}
	$chatRoomElement.append($iconAreaElement);
	if (chatRoom._passHours && chatRoom._passHours === '0') { // 경과 시간 아이콘 감추기
		$('.icon_time', $chatRoomElement).hide();
	}

	// 사용자 아이디
	var $titleElement = $('<p />').addClass('chat_tit').addClass('cstm_name').text(chatRoom._roomCocNm ? chatRoom._roomCocNm : '익명');
	$chatRoomElement.append($titleElement);

	// 타이틀
//	var $titleElement = $('<p />').addClass('chat_tit').text(chatRoom._roomTitle);
//	$chatRoomElement.append($titleElement);

	// 마지막 메세지 내용
	var $messageElement = $('<p />').addClass('chat_tit02').text(htUtils.blindForbidden(chatRoom._lastChatCont));
	$chatRoomElement.append($messageElement);

	// 마지막 메세지 시간
	var $titleElement = $('<span />').addClass('date').text(chatRoom.getLastChatMessageOfChatRoomList().regDt);
	$chatRoomElement.append($titleElement);

	// 컨테이너
	var $chatRoomContainerElement = $('<li />').append($chatRoomElement);

	// 후처리 내역이 있을 경우 배경 색상 추가
	if (chatRoom._endCtgCd !== null) {
		$chatRoomContainerElement.css('background', '#f5f5f5');
		$messageElement.css('color','#368eff')
	}else{
		$chatRoomContainerElement.css('background', '#ffb28e');
		$messageElement.css('color','#af2727')
	}
	

	// 현재 선택 탭에 따라 숨김 처리
	var currentTabIndex = $('li a', this._$statusCounterContainer).index($('li a.active', this._$statusCounterContainer));
	var currentTabName = $('li:eq(' + currentTabIndex + ')', this._$statusCounterContainer).attr('class');
//	console.info('currentTabName: ', currentTabName);
//	console.info('chatRoom.getStatus: ', chatRoom.getStatus());
//	console.info('CHAT_ROOM_STATUS: ', CHAT_ROOM_STATUS[currentTabName.toUpperCase()]);
	if (currentTabName !== 'all'
			&& chatRoom.getStatus() !== CHAT_ROOM_STATUS[currentTabName.toUpperCase()]) {
		$chatRoomContainerElement.addClass('hidden');
	}

	return $chatRoomContainerElement;
}
/**
* 채팅방 목록 중 chatRoom에 해당하는 채팅방의 상태를 업데이트
*
* @param ChatRoom chatRoom
*/
/*ChatRoomListViewer.prototype.changeChatRoomStatus = function(chatRoom) {

	var $chatRoomElement = $('.chat-room[data-room-id=' + chatRoom.getId() + ']', this._$chatRoomListContainer);
	if ($chatRoomElement.length === 1) {
		// 채팅방 상태 변경
		$chatRoomElement.attr('data-room-status', chatRoom.getStatus());

		var currentTabIndex = $('li a', self._$statusCounterContainer).index($('li a.active', this._$statusCounterContainer));
		var currentTabName = $('li:eq(' + currentTabIndex + ')', self._$statusCounterContainer).attr('class');

		if (currentTabName !== 'all') {
			//console.info(chatRoom.getStatus(), CHAT_ROOM_STATUS[currentTabName.toUpperCase()]);
			if (chatRoom.getStatus() === CHAT_ROOM_STATUS[currentTabName.toUpperCase()]) {
				$chatRoomElement.parent().removeClass('hidden');
			}
		}
	} else {
		console.error('WTF: NOT FOUND CHAT ROOM');
	}
}*/
/**
* 채팅방 목록 중 chatRoom에 해당하는 채팅방의 마지막 메세지를 업데이트
*
* @param ChatRoom chatRoom
*/
ChatRoomListViewer.prototype.changeLastChatMessageOfChatRoomList = function(chatRoom) {

	var $chatRoomElement = $('.chat-room[data-room-id=' + chatRoom.getId() + ']', this._$chatRoomListContainer);
	if ($chatRoomElement.length === 1) {
		if ($('.chat_tit02', $chatRoomElement).length === 0) { // 채팅 내역 영역이 없는 경우 추가
			$chatRoomElement.append($('<p />').addClass('chat_tit02'));
		}

		// 메세지 내용 변경
		$('.chat_tit02', $chatRoomElement).text(htUtils.blindForbidden(chatRoom._lastChatCont));

		$('.date', $chatRoomElement).text(chatRoom.getLastChatMessageOfChatRoomList().regDt);
	} else {
		console.error('NOT FOUND CHAT ROOM');
	}
}
/**
* 채팅방 목록 중 chatRoom에 해당하는 채팅방의 아이콘을 업데이트
*
* @param ChatRoom chatRoom
*/
ChatRoomListViewer.prototype.changeChatRoomIcons = function(chatRoom) {

	console.info('=== CHANGE CHAT ROOM ICONS', chatRoom);

	// 사용자 이름
	var $cstmNameElement = $('.chat-room[data-room-id=' + chatRoom.getId() + '] .cstm_name', this._$chatRoomListContainer);
	var $iconAreaElement = $('.chat-room[data-room-id=' + chatRoom.getId() + '] .icon_area', this._$chatRoomListContainer);

	if ($cstmNameElement.length === 1) {
		$cstmNameElement.text(chatRoom._roomCocNm);
	}
	if ($iconAreaElement.length === 1) {
		// 고객사 아이디
		if (chatRoom._roomCocId) {
			$('.right_id', $iconAreaElement).text('@' + chatRoom._roomCocId);
		} else {
			$('.right_id', $iconAreaElement).text('');
		}

		// 경과 시간 아이콘
		if ('Y' === htUser.getSetting().pass_time_use_yn
				&& chatRoom._passHours && $('.icon_time', $iconAreaElement).length === 1) {
			$('.icon_time', $iconAreaElement).attr('title', '상담 시작 시간: ' + chatRoom._roomCreateDtPretty).text(chatRoom._passHours);
			if (chatRoom._passHours === '0') {
				$('.icon_time', $iconAreaElement).hide();
			} else {
				$('.icon_time', $iconAreaElement).show();
			}
		}

		// 깃발 아이콘
		if (chatRoom._markImgClassName) {
			console.info('markImgClassName: ', chatRoom._markImgClassName);
			if (chatRoom._markImgClassName === 'icon_flag_cancel') { // 아이콘 지움
				if ($('.icon_flag', $iconAreaElement).length === 1) {
					$('.icon_flag', $iconAreaElement).remove();
				}
			} else { // 아이콘 추가, 변경
				if ($('.icon_flag', $iconAreaElement).length === 1) {
					$('.icon_flag', $iconAreaElement).attr('class', '').addClass('icon_flag').addClass(chatRoom._markImgClassName).text(chatRoom._markDesc);
				} else {
					$iconAreaElement.append($('<i />').addClass('icon_flag').addClass(chatRoom._markImgClassName).text(chatRoom._markDesc));
				}
			}
		} else {
			$('.icon_flag', $iconAreaElement).remove();
		}

		// 고객 식별 아이콘
		if (chatRoom._gradeNm === CUSTOMER_GRADE_NAME.ELEPHANT || chatRoom._gradeNm === CUSTOMER_GRADE_NAME.VIP || chatRoom._gradeNm === CUSTOMER_GRADE_NAME.BLACK) {
			if ($('.' + chatRoom._gradeImgClassName, $iconAreaElement).length === 1) {
				;
			} else {
				console.debug(chatRoom);
				
				$iconAreaElement.append($('<i />')
						.addClass(chatRoom._gradeImgClassName)
						.attr('title', chatRoom._gradeMemo)
						.text(chatRoom._gradeNm));
			}
		} else {
			$('.icon_elephant', $iconAreaElement).remove();
			$('.icon_vip', $iconAreaElement).remove();
			$('.icon_black', $iconAreaElement).remove();
		}
	} else {
		console.error('NOT FOUND CHAT ROOM');
	}
}
/**
 * 채팅방 삭제
 *
 * @param String chatRoomUid
 */
ChatRoomListViewer.prototype.removeChatRoom = function(chatRoomUid) {

	$('a[data-room-id=' + chatRoomUid + ']', this._$chatRoomListContainer).closest('li').remove();
}
/**
* 채팅방 목록, 메뉴 카운트 업데이트
*
* @param Object statusCounterMap
*/
ChatRoomListViewer.prototype.updateStatusCounter = function(statusCounter) {

	console.debug(statusCounter);
	console.debug(this._$statusCounterContainer);
	
	if ($('.assign_robot .badge').length === 1 && typeof statusCounter.assign_robot !== undefined) {
		console.debug($('.assign_robot .badge').text(), statusCounter.assign_robot);
		$('.assign_robot .badge', this._$statusCounterContainer).text(statusCounter.assign_robot);
	}
	if ($('.wait_counselor .badge').length === 1 && typeof statusCounter.wait_counselor !== undefined) {
		$('.wait_counselor .badge', this._$statusCounterContainer).text(statusCounter.wait_counselor);
	}
	if ($('.assign_counselor .badge').length === 1 && typeof statusCounter.assign_counselor !== undefined) {
		$('.assign_counselor .badge', this._$statusCounterContainer).text(statusCounter.assign_counselor);
	}
	if ($('.need_answer .badge').length === 1 && typeof statusCounter.need_answer !== undefined) {
		$('.need_answer .badge', this._$statusCounterContainer).text(statusCounter.need_answer);
	}
	if ($('.wait_reply .badge').length === 1 && typeof statusCounter.wait_reply !== undefined) {
		$('.wait_reply .badge', this._$statusCounterContainer).text(statusCounter.wait_reply);
	}
	// 관리자 페이지 '진행중': 응대필요 + 고객 답변 대기중
	/*if ($('.snb_menu_area .counseling .badge').length === 1
			&& typeof statusCounter.wait_reply !== undefined && typeof statusCounter.need_answer !== undefined) {
		$('.snb_menu_area .counseling .badge', this._$statusCounterContainer).text(statusCounter.need_answer + statusCounter.wait_reply);
	}*/
	///관리자 페이지 상담응대완료
	if ($('.snb_menu_area .counseling .badge').length === 1	&& typeof statusCounter.wait_reply !== undefined ){		
		$('.snb_menu_area .counseling .badge', this._$statusCounterContainer).text(statusCounter.wait_reply);
	}
	///관리자페이지 고객응대필요
	if ($('.snb_menu_area .needanswer .badge').length === 1	&& typeof statusCounter.need_answer !== undefined) {
		$('.snb_menu_area .needanswer .badge', this._$statusCounterContainer).text(statusCounter.need_answer);
	}
	if ($('.end_counselor .badge').length === 1 && typeof statusCounter.end_counselor !== undefined) {
		$('.end_counselor .badge', this._$statusCounterContainer).text(statusCounter.end_counselor);
	}
	if ($('.end_robot .badge').length === 1 && typeof statusCounter.end_robot !== undefined) {
		$('.end_robot .badge', this._$statusCounterContainer).text(statusCounter.end_robot);
	}
	
	if ($('.request_change_counselor .badge').length === 1 && typeof statusCounter.request_change_counselor !== undefined) {
		$('.request_change_counselor .badge', this._$statusCounterContainer).text(statusCounter.request_change_counselor);
	}
	if ($('.request_manager_counsel .badge').length === 1 && typeof statusCounter.request_manager_counsel !== undefined) {
		$('.request_manager_counsel .badge', this._$statusCounterContainer).text(statusCounter.request_manager_counsel);
	}	
	if ($('.request_cstm_grade .badge').length === 1 && typeof statusCounter.request_cstm_grade !== undefined) {
		$('.request_cstm_grade .badge', this._$statusCounterContainer).text(statusCounter.request_cstm_grade);
	}	
	
	if ($('.request_review .badge').length === 1 && typeof statusCounter.request_review !== undefined) {
		$('.request_review .badge', this._$statusCounterContainer).text(statusCounter.request_review);
	}
	if ($('.all .badge').length === 1 && typeof statusCounter.all !== undefined) {
		$('.all .badge', this._$statusCounterContainer).text(statusCounter.all);
	}

	// 현재 탭 목록 새로고침 (처음 로드시 실행하면 loadChatList 두 번 실행 됨)
	//$('.counseling_left .tabs_icon .active').trigger('click');
}
/**
 * 스크롤 이벤트 등록
 */
ChatRoomListViewer.prototype.addScrollEvent = function() {

	this._$chatRoomListContainerWrapper.off('scroll', this._scrollCallback)
			.on('scroll', this._scrollCallback);
}
/**
 * 스크롤 이벤트 등록
 */
ChatRoomListViewer.prototype.removeScrollEvent = function() {

	this._$chatRoomListContainerWrapper.off('scroll', this._scrollCallback);
}
/**
 * 윈도우 폭이 좁을 경우 채팅목록 가리고 레이어로 변경
 */
$(window).on('load resize', function() {
	if (window.matchMedia('(max-width: 1024px)').matches) {
		$('.tab_icon_content.counseling').css('display', 'none');
		$('.tabs_icon li a').on('click', function(e) {
			$('.tab_icon_content.counseling').css('display', 'block');
		});
		$('.btn_close_tabcont').on('click', function(e) {
			$('.tab_icon_content.counseling').css('display', 'none');
			$('.tabs_icon li a').removeClass('active');
		});
	} else {
		$('.tab_icon_content.counseling').css('display', 'block');
	}
});
