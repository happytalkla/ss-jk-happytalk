if (window.console === undefined) {
	window.console = {
			assert: function() {},
			clear: function() {},
			count: function() {},
			log: function() {},
			debug: function() {},
			info: function() {},
			warn: function() {},
			error: function() {},
			trace: function() {},
			time: function() {},
			timeEnd: function() {}
	};
}

// ////////////////////////////////////////////////////////////////////////////
// Constant
// ////////////////////////////////////////////////////////////////////////////
var CHAT_MESSAGE_TYPE = {
		JOIN: 'JOIN',
		END: 'END',
		STATUS: 'STATUS',
		ACTIVE: 'ACTIVE',
//		COMMAND: 'COMMAND',
		MESSAGE: 'MESSAGE',
		ASSIGN: 'ASSIGN', // 고객 전용 (NoticeMessage.CHATROOM 대용)
		EDIT: 'EDIT',
		REMOVE: 'REMOVE'
};

var CHAT_MESSAGE_CONTENTS_TYPE = {
		TEXT: 'T', // TODO: CHAT 으로 변경
		CHAT: 'T',
		LOG: 'L', // 로그 (통계용)
		EVENT: 'E', // 이벤트, 상담원에게만 보일 이벤트 용도로 만들어 둠
		FILE: 'F', // TODO: 삭제
		MEMO: 'M'
};

var CHAT_MESSAGE_STATUS = {
		SENT: 'SEND',
		RECEIVED: 'RECEIVE',
		READ: 'READ'
};

var CHAT_COMMAND = {
		REQUEST_COUNSELOR: 'REQUEST_COUNSELOR',
		CHANGE_COUNSELOR: 'CHANGE_COUNSELOR',
		ASSIGN_COUNSELOR: 'ASSIGN_COUNSELOR',
//		ASSIGN_COUNSELOR_SELF: 'ASSIGN_COUNSELOR_SELF',
		SUBMIT_CHATROOM: 'SUBMIT_CHATROOM',
		HIDE_BY_CUSTOMER: 'HIDE_BY_CUSTOMER',
		ASSIGN_CUSTOMER_GRADE: 'ASSIGN_CUSTOMER_GRADE',
		MARK_CHATROOM_FLAG: 'MARK_CHATROOM_FLAG',
		REQUEST_REVIEW: 'REQUEST_REVIEW',
		COMPLETE_REVIEW: 'COMPLETE_REVIEW',
		MANAGER_COUNSEL: 'MANAGER_COUNSEL',
		LEAVE_MANAGER_COUNSEL: 'LEAVE_MANAGER_COUNSEL'
//		END_CHATROOM_BY_COUNSELOR: 'END_CHATROOM_BY_COUNSELOR',
//		END_CHATROOM_BY_CUSTOMER: 'END_CHATROOM_BY_CUSTOMER',
};

var CHAT_COMMAND_RETURN_CODE = {
		SUCCEED: 'SUCCEED',
		FAILED: 'FAILED',
		SCHEDULED: 'SCHEDULED',
		COUNSELOR_OFF_DUTY: 'COUNSELOR_OFF_DUTY',
		COUNSELOR_BREAK_TIME: 'COUNSELOR_BREAK_TIME',
		CENTER_OFF_DUTY: 'CENTER_OFF_DUTY',
		CENTER_BREAK_TIME: 'CENTER_BREAK_TIME',
		NO_PERMIT_SELF_ASSIGN: 'NO_PERMIT_SELF_ASSIGN'
};

var NOTICE_MESSAGE_TYPE = {
		CHATROOM: 'CHATROOM',
		SETTING: 'SETTING',
		ALRAM: 'ALRAM',
		NOTICE: 'NOTICE',
		STATUS_COUNTER: 'STATUS_COUNTER',
		COMMAND: 'COMMAND',
		HIGHLIGHT: 'HIGHLIGHT'
};

var CHAT_ROOM_STATUS = {
		WAIT_ROBOT: '10',
		ASSIGN_ROBOT: '11',
		WAIT_COUNSELOR: '31',
		ASSIGN_COUNSELOR: '41',
		NEED_ANSWER: '51',
		WAIT_REPLY: '61',
		END_COUNSELOR: '91',
		//END_ROBOT: '92',
		MANAGER_COUNSEL: '81'
};

var CHAT_ROOM_STATUS_TITLE_FOR_META_INFO = {
		WAIT_ROBOT: '접수대기',
		ASSIGN_ROBOT: '상담중',
		WAIT_COUNSELOR: '접수대기',
		ASSIGN_COUNSELOR: '상담중',
		NEED_ANSWER: '상담중',
		WAIT_REPLY: '상담중',
		END_COUNSELOR: '완료',
		END_ROBOT: '완료'
};

var USER_ACTIVE_STATUS = {
		TYPING: 'TYPING',
		NOT_TYPING: 'NOT_TYPING',
		IDLE: 'IDLE',
		ACTIVE: 'ACTIVE'
};

var CHANNEL_TYPE = {
		WEB: 'A',
		KAKAO: 'B',
		O2: 'C',
		MPOP: 'D'
};

var CHAT_MEMBER_TYPE = {
		SUPER: 'S',
		ADMIN: 'A',
		MANAGER: 'M',
		COUNSELOR: 'C',
		CUSTOMER: 'U',
		ROBOT: 'R'
};

var CHAT_ROLL_TYPE = {
		MANAGER: 'M',
		COUNSELOR: 'C',
		CUSTOMER: 'U',
		ROBOT: 'R'
};

var CUSTOMER_GRADE_NAME = {
		ELEPHANT: 'elephant',
		VIP: 'VIP',
		BLACK: 'Black'
};

var MESSAGE_SECTION_TYPE = {
		TEXT: 'text',
//		NARROW_TEXT: 'narrowText',
//		LONG_TEXT: 'longText',
//		FORMAT_TEXT: 'formmatText',
		FILE: 'file',
		ACTION: 'action'
		// HOTKEY: 'hotkey',
		// SELECT: 'select',
		// REQUESTION: 'requestion',
		// LINK: 'link',
		// REF: 'ref',
		// TABLE: 'table',
		// MORE: 'more'
};

var MESSAGE_ACTION_TYPE = {
	MESSAGE: 'message',
	SCHEMA: 'schema',
	LINK: 'link',
	REFERENCE: 'reference',
	HOTKEY: 'hotkey'
};

var MESSAGE_ACTION_HOTKEY_TYPE = {
	REQUEST_COUNSELOR: 'HappyTalk/RequestCounselor',
	NEW_CHATROOM: 'HappyTalk/NewChatRoom'
};

var CHAT_MESSAGE_LIST_SIZE = 30; // 메세지 페이징 사이즈

var DEFAULT_CONTENTS_VERSION = '0.1'; // 말풍선 포맷 버전

var SIMULATOR_UID = '61894ef0-1f4f-4bd0-b6ef-bb38a9a62671'; // 시뮬레이터 고객 식별자

// ////////////////////////////////////////////////////////////////////////////
// Global Variables
// ////////////////////////////////////////////////////////////////////////////
var HT_APP_PATH = '/happytalk';
var BUILDER_APP_PATH = '/happybot';
var HT_USER_IDLE_TIME_SECONDS = 120; // 채팅창 비활성화되는 시간
var HT_DEFAULT_ASSIGN_MEMBER_UID = '00000000-0000-0000-0000-000000000000';
var LEGACY_PC_HOST = 'www.samsungpop.com';
var LEGACY_MOBILE_HOST = 'm.samsungpop.com';

var htBrowserActive = true; // 채팅창 활성화 여부
var htUserActiveStatus = USER_ACTIVE_STATUS.IDLE;
var htUtils = htUtils || {};
var htUser = null;
var htForbidden = htForbidden || [];
var htForbiddenAlternative = htForbiddenAlternative || ' ';
var htHighlight = htHighlight || [];
var htClient = null;

var searchCnt = 0;

// ////////////////////////////////////////////////////////////////////////////
// User Class
// ////////////////////////////////////////////////////////////////////////////
/**
 * 사용자 클래스
 *
 * @param $userElement jQueryObject
 */
function User($userElement) {

	this._id = $('input[name=id]', $userElement).length === 1 ? $('input[name=id]', $userElement).val() : '';
	this._nickName = $('input[name=nickName]', $userElement).length === 1 ? $('input[name=nickName]', $userElement).val() : '';
	this._userType = $('input[name=userType]', $userElement).length === 1 ? $('input[name=userType]', $userElement).val() : '';
	this._rollType = $('input[name=rollType]', $userElement).length === 1 ? $('input[name=rollType]', $userElement).val() : '';
	this._departCd = $('input[name=departCd]', $userElement).length === 1 ? $('input[name=departCd]', $userElement).val() : '';
	this._managerId = $('input[name=managerId]', $userElement).length === 1 ? $('input[name=managerId]', $userElement).val() : '';
	this._cocId = $('input[name=cocId]', $userElement).length === 1 ? $('input[name=cocId]', $userElement).val() : '';
	this._cstmDivCd = $('input[name=cstmDivCd]', $userElement).length === 1 ? $('input[name=cstmDivCd]', $userElement).val() : '';
	this._setting = {};
}
User.prototype.getId = function() {

	return this._id;
};
User.prototype.getDepartCd = function() {

	return this._departCd;
};
User.prototype.getNickName = function() {

	return this._nickName;
};
User.prototype.getUserType = function() {

	return this._userType;
};
User.prototype.getRollType = function() {

	return this._rollType;
};
User.prototype.getManagerId = function() {

	if (this._userType === CHAT_MEMBER_TYPE.MANAGER) {
		return this._id;
	} else {
		return this._managerId;
	}
};
User.prototype.getCocId = function() {

	return this._cocId;
};
User.prototype.getCstmDivCd = function() {

	return this._cstmDivCd;
};
User.prototype.getSetting = function() {

	return this._setting;
};
User.prototype.setSetting = function(setting) {

	if (this._setting && this._setting.member_enter_use_yn) {
		console.debug('UPDATE SETTING');
		for (var key in setting) {
			this._setting[key] = setting[key];
		}
	} else {
		console.debug('NEW SETTING');
		this._setting = setting;
	}
	console.debug(this._setting);
};

// ////////////////////////////////////////////////////////////////////////////
// Utilities
// ////////////////////////////////////////////////////////////////////////////
/**
 * 파일 업로드
 *
 * @param formData FormData
 * @param chatRoomUid String
 * @param userId String
 * @param callback Function
 */
htUtils.uploadFile = function(formData, parentPathName, chatRoomUid, userId , callback) {

	formData.append('parentPathName', parentPathName);
	formData.append('chatRoomUid', chatRoomUid);
	formData.append('userId', userId);
	var imagePath = null;
	var url = HT_APP_PATH + '/api/upload';
	if (chatRoomUid) {
		url = HT_APP_PATH + '/api/chat/room/upload/' + chatRoomUid;
	}

	console.info('CALLEEEEEEEEEEEEEEEEEEEEEEEEEEE');
	$.ajax({
			url: url,
			type: 'post',
			data: formData,
			contentType: false,
			processData: false,
			dataType: 'json'
	}).done(function(data) {
		console.info(data);
		if (data.title) { // 파일 이름이 있으면 정상으로 간주
			callback(data);
		} else {
			console.error('Fail REQUEST: UPLOAD FILE: ', data.resultMessage);
			if ('NOT SUPPORTED FILE TYPE' === data.resultMessage) {
				alert('지원하지 않는 파일 형식입니다');
			}
		}
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: UPLOAD FILE: ', textStatus);
	});
};
/**
 * 브라우저 닫기
 */
htUtils.closeWindow = function() {

	var _ua = window.navigator.userAgent || window.navigator.vendor || window.opera;

	if (_ua.toLocaleLowerCase().indexOf('kakaotalk') > -1) {
		// 카카오 인앱 브라우저에서 종료시
		try { window.close(); } catch (e) {}
		try { self.close(); } catch (e) {}
		try {
			window.location.href = (/iPad|iPhone|iPod/.test(_ua)) ? 'kakaoweb://closeBrowser' : 'kakaotalk://inappbrowser/close';
		} catch (e) {}
	} else {
		// 카카오 이외의 브라우저에서 종료시
		try { window.close(); } catch (e) {}
		try { self.close(); } catch (e) {}
		alert('자동으로 닫히지 않을 경우 해당 페이지를 수동으로 닫아주세요.');
	}
}
/**
 * 사용자 객체 생성
 */
htUtils.setHtUser = function() {

	if (htUser === null) {
		console.debug('=== SET USER INFO');
		if ($('#user').length === 1
				&& $('#user input[name=id]').length === 1
				&& $('#user input[name=id]').val() !== '') {
			htUser = new User($('#user'));
		} else {
			console.info('CANNOT FOUND USER ELEMENT');
		}
		console.debug(htUser);
	}
};
/**
 * 'classes'문자열에서 'prefix'로 시작하는 첫번째 단어를 찾음
 *
 * @param classes String
 * @param prefix String
 */
htUtils.getWordStartWith = function(classes, prefix) {

	if (!classes) {
		return '';
	}

	var arrayClasses = classes.match(/\S+/g) || [];
	for (var i = 0; i < arrayClasses.length; i++) {
		if (arrayClasses[i].indexOf(prefix) === 0) {
			return arrayClasses[i]; //.replace(prefix, '');
		}
	}

	return '';
};
/**
 * 'value'값을 갖는 첫 번째 키값을 리턴
 *
 * @param obj Object
 * @param value String
 */
htUtils.getKeyFromValue = function(obj, value) {

	for (var key in obj) {
		if (obj.hasOwnProperty(key)) {
			if (obj[key] === value) {
				return key;
			}
		}
	}

	return null;
};
/**
 * 금지어 블라인드 처리
 *
 * @param text String
 */
htUtils.blindForbidden = function(text) {

	if (htForbidden && $.cookie && $.cookie('SHOW_FORBIDDEN') !== 'true') {
		for (var i = 0; i < htForbidden.length; i++) {
			var word = htForbidden[i];
			var regex = new RegExp('(' + word + ')', 'g');
			text = text.replace(regex, htUtils.repeatString('*', word.length));
		}
	}

	return text;
};
/**
 * 금지어 대체 메세지
 *
 * @param text String
 */
htUtils.alternativeForbidden = function(text) {

	if (htForbidden) {
		for (var i = 0; i < htForbidden.length; i++) {
			var word = htForbidden[i];
			if (text.indexOf(word) > -1) {
				return htForbiddenAlternative;
			}
		}
	}

	return text;
};
/**
 * 문자 반복 (String.prototype.repeat IE 미지원)
 *
 * @param string String
 * @param count Integer
 */
htUtils.repeatString = function(string, count) {

	if (count < 1) {
		return '';
	}

	var result = '';
	var num = 0;
	while (num < count ) {
		num += 1;
		result += string;
	}

	return result;
};
/**
 * Sleep
 *
 * @param seconds Integer
 * @param title String
 */
htUtils.sleep = function(seconds, title) {

	if (title !== undefined) {
		console.info('>>> sleep: ', title);
	}

	var now = new Date().getTime();
	console.time('sleep');
	while (new Date().getTime() < now + seconds * 1000) {
		;
	}
	console.timeEnd('sleep');
};
/**
 * 레이어 알람
 *
 * @param text String
 * @param seconds Integer
 */
htUtils.alert = function(text, seconds) {

	var timer = null;
	if (seconds === undefined) {
		seconds = 2000;
	} else {
		seconds = seconds * 1000;
	}

	// 알림 영역 없을 경우 추가
	if ($('.layer_alert').length === 0) {
		$('body').append($('<div class="layer_alert" style="display: none;"><p class="alert_text"></p></div>'));
	}

	// 기존 함수 있을 경우 실행
	//if (typeof fn_layerMessage !== 'undefined') {
		//fn_layerMessage(text);
	//} else { // 기존 함수 없을 경우
		// 페이드 아웃 중이면 감춤
		if ($('.layer_alert').is(':animated')) {
			console.debug('animated');
			$('.layer_alert').stop().hide();
		}

		$('.alert_text').html(text);
		$('.layer_alert').show();

		if (timer !== null) {
			clearTimeout(timer);
		}

		timer = setTimeout(function() {
			$('.layer_alert').fadeOut(1000);
			timer = null;
		}, seconds);
	//}
};
/**
 * 데스크탑 알람
 *
 * @param text String
 * @param title String
 * @param imageUrl String
 */
htUtils.noti = function(text, title, imageUrl) {

//	if (htBrowserActive) {
//		console.info('BROWSER IS ACTIVE, IGNORE NOTIFICATION');
//		return;
//	}

	var notiOption = {};
	if (text !== undefined) {
		notiOption.body = text;
	}
	if (imageUrl !== undefined) {
		notiOption.icon = imageUrl;
	}
	if (title === undefined) {
		title = '삼성증권 상담채팅';
	}

	htUtils.notiSound();

	if ('Notification' in window) {
		if (Notification.permission === 'granted') {
			var notification = new Notification(title, notiOption);
		} else if (Notification.permission !== 'denied') {
			Notification.requestPermission(function(permission) {
				if (permission === 'granted') {
					var notification = new Notification(title, notiOption);
				}
			});
		} else {
			htUtils.alert(text, 5);
			console.info('Denied notification on this site');
		}
	} else {
		htUtils.alert(text, 5);
	}
};
/**
 * 알림 사운드 재생
 *
 * @param soundName String
 */
htUtils.notiSound = function(soundName) {

	if (typeof ion === 'undefined') {
		console.debug('=== NO LIBRARY');
		return;
	}

	console.debug('=== PLAY NOTIFICATION SOUND');
	if (soundName === undefined) {
		ion.sound.play('bell_ring');
	} else {
		ion.sound.play(soundName);
	}
};
if (typeof ion !== 'undefined') {
	ion.sound({
		sounds: [
			{name: 'beer_can_opening'},
			{name: 'bell_ring'},
			{name: 'button_tiny'},
		],
		path: 'audio/',
		preload: true,
		multiplay: true,
		volume: 0.9,
	});
}
/**
 * 디버그 모드 여부
 */
htUtils.isDebug = function() {

//	return (localStorage.debug &&
//			(localStorage.debug === '*' || localStorage.debug === 'happytalk.io:ht'));
};
/**
 * 프로덕션 프로파일 여부, #debug 요소가 없을 경우 프로덕션으로 간주
 */
htUtils.isProduction = function() {

	return ($('#debug').length !== 1);
};
/**
 * 상담원 연결 가능인지 판별
 */
htUtils.isWorkTime = function() {

	if ($('#siteSetting').length === 1) {
		var unsocialAcceptYn = $('#siteSetting [name=unsocialAcceptYn]').val();
		var workYn = $('#siteSetting [name=workYn]').val();
		var startTime = $('#siteSetting [name=startTime]').val();
		var startHour = parseInt(startTime.substring(0, 2));
		var startMinutes = parseInt(startTime.substring(2));
		var endTime = $('#siteSetting [name=endTime]').val();
		var endHour = parseInt(endTime.substring(0, 2));
		var endMinutes = parseInt(endTime.substring(2));

		// 근무시간외 상담가능
		if (unsocialAcceptYn === 'Y') {
			return true;
		}

		// 근무일이 아니면
		if (workYn !== 'Y') {
			console.debug('workYn: ', workYn);
			return false;
		}

		// 근무시간
		var currentDateTime = new Date();
		console.debug('currentDateTime: ', currentDateTime);
		var startDateTime = new Date();
		startDateTime.setHours(startHour);
		startDateTime.setMinutes(startMinutes);
		startDateTime.setSeconds(0);
		var endDateTime = new Date();
		endDateTime.setHours(endHour);
		endDateTime.setMinutes(endMinutes);
		endDateTime.setSeconds(0);
		console.debug('startDateTime: ', startDateTime);
		console.debug('endDateTime: ', endDateTime);

		if (currentDateTime >= startDateTime && currentDateTime < endDateTime) {
			return true;
		} else {
			console.debug('currentDateTime', currentDateTime, 'startDateTime: ', startDateTime, 'endDateTime: ', endDateTime);
			return false;
		}
	}

	return true;
};
/**
 * 모바일 디바이스 판별
 */
htUtils.isMobile = function() {

	var agent = window.navigator.userAgent.toLowerCase();
	console.debug(agent);
	if (agent.match('iphone')
			|| agent.match('android')) {
		return true;
	}

	return false;
};
/**
 * ChatContents > extra 정보
 * 
 * @param cont String
 */
htUtils.getFirstExtra = function(cont) {

	var chatContents = JSON.parse(cont);
	if (chatContents.balloons) {
		for (var i = 0; chatContents.balloons.length > i; i++) {
			var balloon = chatContents.balloons[i];
			for (var j = 0; balloon.sections.length > j; j++) {
				var section = balloon.sections[j];
				if (section.extra) {
					return section.extra;
				}
			}
		}
	}

	return '';
}

/**
 * O2 링크 이동
 * */
htUtils.moveCTypeLink = function (url){
/*	
	if(url.match('massetapp')){//massetapp
		console.log("==massetapp==");
		location.href = url;
	}else{
		console.log("==http==");
		var data;
		data = {"OPEN_WEB":url};
		data = JSON.stringify(data);
		var mobile = navigator.userAgent;
		if( /iPhone|iPad|iPod|iOS/i.test(mobile) ){
			var dic = {'handlerInterface':'mba','function':'sendToApp','parameters':data};
			window.webkit.messageHandlers['mba'].postMessage(dic);
		}else if( /Android/i.test( mobile )){
			window['mba']['sendToApp']( data );
		}else{
			console.log("pc");
		}
	}*/
	console.log("O2 Link");
	var arrUrl = url.split(":");
	var hostName = window.location.hostname; 
	var serverType
	if((hostName === "tchat.ss.local") || (hostName === "tchat.samsungpop.com")){
		serverType = "T"
	}else if((hostName === "chat.ss.local") || (hostName == "chat.samsungpop.com")){
		serverType = "S"
	}
	
	if(arrUrl[0] ==="D"){//Depplink
		console.log("==massetapp==");
		var link = "massetapp://" + serverType + "/" + arrUrl[1];
		location.href = link;
	}else if(arrUrl[0] ==="L"){//Link Url
		console.log("==link==");
		var data;
		data = {"OPEN_WEB":"https://" + arrUrl[1]};
		data = JSON.stringify(data);
		var mobile = navigator.userAgent;
		if( /iPhone|iPad|iPod|iOS/i.test(mobile) ){
			var dic = {'handlerInterface':'mba','function':'sendToApp','parameters':data};
			window.webkit.messageHandlers['mba'].postMessage(dic);
		}else if( /Android/i.test( mobile )){
			window['mba']['sendToApp']( data );
		}else{
			console.log("pc");
		}
	}else{//file download
		console.log("==file==");
		
		var data;
		var host = window.location.host;
		data = {"OPEN_WEB":"https://"+host+ + url};
		data = JSON.stringify(data);
		var mobile = navigator.userAgent;
		if( /iPhone|iPad|iPod|iOS/i.test(mobile) ){
			var dic = {'handlerInterface':'mba','function':'sendToApp','parameters':data};
			window.webkit.messageHandlers['mba'].postMessage(dic);
		}else if( /Android/i.test( mobile )){
			window['mba']['sendToApp']( data );
		}else{
			console.log("pc");
		}
	}
}

/**
 * mPOP 링크 이동
 * */
htUtils.moveDTypeLink = function (url){
	console.log("mPOP Link");
	var arrUrl = url.split(":");
	var hostName = window.location.hostname; 
	var serverType
	if((hostName === "tchat.ss.local") || (hostName === "tchat.samsungpop.com")){
		serverType = "T"
	}else if((hostName === "chat.ss.local") || (hostName == "chat.samsungpop.com")){
		serverType = "S"
	}
	
	if(arrUrl[0] ==="D"){//Depplink
		console.log("==mpopapp==");
		var link = "mpopapp://" + serverType + "/" + arrUrl[1];
		location.href = link;
	}else if(arrUrl[0] ==="L"){//Link Url
		// to-do 
		// arrUrl[1] --> http: https:가 잇는지 있으면 없애야됨
		// ex) L:http://www.naver.com --> mpopcmd://openurl?url=www.naver.com/?" + urlencodeParma(a=1&b=2&c)
		// 
		//arrUrl[1] --> 2번째 url -->https://
		console.log("==link==");
		var host = window.location.host;
		var newUrl = arrUrl[1];
		var idx = newUrl.indexOf("\/");
		if(idx > -1){
			newUrl = newUrl.substring(0, (idx - 1)) + encodeURIComponent(newUrl.substring(idx - 1));
 		}
		var link = "mpopcmd://openurl?url=" +host+ newUrl;
		location.href = link;
	}else{//file download
		console.log("==file==");
		var host = window.location.host; 
		var link = "mpopcmd://openurl?url=" + host + url;
		location.href = link;
		
	}
}


/**
 * jQuery 로그
 */
$.fn.log = function() {
	console.info.apply(console, this);
	return this;
};

// ////////////////////////////////////////////////////////////////////////////
// Main
// ////////////////////////////////////////////////////////////////////////////
$(document).ready(function() {

	htUtils.setHtUser();
	if (htUtils.isProduction()) {
		console.debug('PRODUCTION');
//		if (window.console) {
			window.console = {
					assert: function() {},
					clear: function() {},
					count: function() {},
					log: function() {},
					debug: function() {},
					info: function() {},
					warn: function() {},
					error: function() {},
					trace: function() {},
					time: function() {},
					timeEnd: function() {},
			}
//		}
	} else {
		console.info('NOT PRODUCTION');
		LEGACY_PC_HOST = 'test-www.samsungpop.com';
		LEGACY_MOBILE_HOST = 'test-m.samsungpop.com';
	}

	// 금지어 단어 저장
	$.ajax({
		url: HT_APP_PATH + '/api/forbidden'
	}).done(function(data) {
		htForbidden = data;
		console.debug('FORBIDDEN WORDS', htForbidden);
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: LOAD FORBIDDEN', textStatus);
	});

	// 금지어 대체 메세지 저장
	$.ajax({
		url: HT_APP_PATH + '/api/forbidden/alternative'
	}).done(function(data) {
		htForbiddenAlternative = data;
		console.debug('FORBIDDEN ALTERNATIVE MESSAGE', htForbiddenAlternative);
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: LOAD FORBIDDEN', textStatus);
	});

	// 알림 창 추가
	if ($('body > .layer_alert').length === 0) {
		var $alertLayer = $('<div class="layer_alert"><p class="alert_text"></p></div>');
		$('body').append($alertLayer);
		$alertLayer.hide();
	}
	
	// 팝업 숨기기

	// 팝업 창 버튼 닫기
	$('.popup .btn_popup_close').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$('body').css("overflow", "scroll");
		
		$(this).closest('.popup').hide();
	});

	// IE 백스페이스, 뒤로가기 무효화
	$(document).on('keydown', function(e) {
		var element = e.target.nodeName.toLowerCase();
		console.debug('FROM COMMON, NODE NAME: ', e);
		if (element !== 'input' && element !== 'textarea') {
			if (e.keyCode === 8) {
				return false;
			}
		}
	});

	// 해피봇 빌더 URL
	if ($('#data [name=happyBotBuilderUrl]').length === 1) {
		BUILDER_APP_PATH = $('#data [name=happyBotBuilderUrl]').val();
	}
});

$(window).on('load', function() {
});

/**
 * 박스 사이즈 체크
 */
function getBoxText200(boxObj, line) {
	if (line === 6) {
		if (typeof(boxObj.css('height')) !== 'undefined'
			&& boxObj.css('height').replace('px', '') * 1 > 500) {
			return true;
		} else {
			return false;
		}
	} else {
		if (typeof(boxObj.css('height')) !== 'undefined'
			&& boxObj.css('height').replace('px', '') * 1 > 500) {
			return true;
		} else {
			return false;
		}
	}
}

function replaceAll(str, searchStr, replaceStr)
{
	while (str.indexOf(searchStr) !== -1) {
		str = str.replace(searchStr, replaceStr);
	}

	return str;
}

function hangul(obj)
{
	if ((event.keyCode < 12592) || (event.keyCode > 12687)) {
		obj.value = '';
	}
}

/**
 * ID 체크
 * onblur="javascript:IdCheck(this.value, this);"
 */
function IdCheck(checkString, obj)
{
	var id = checkString;

	if (!/^[a-zA-Z0-9]{6,50}$/.test(id)) {
		if (id === '') {
			return false;
		}

		alert("ID는 영문자 숫자의 조합으로 6자리 이상 사용해야 합니다.");

		obj.focus();
		return false;
	}

	var chk_num = id.search(/[0-9]/g);
	var chk_eng = id.search(/[a-z]/ig);
	if (chk_num < 0 || chk_eng < 0) {
		if (id === '') {
			return false;
		}
		alert("ID는 영문자와 숫자를 혼용하여야 합니다.");
		obj.focus();
		return false;
	}

	return true;
}

/**
 * 비밀번호 조건 체크
 * onblur="javascript:passwordCheck(this.value, this);"
 */
function passwordCheck(checkString, obj)
{
	if (checkString === '') {
		return;
	}

	var upw = checkString;

	if (upw.length < 8) {
		alert('비밀번호는 숫자, 영문자, 특수문자 조합 8자 이상이어야 합니다.'); 
		obj.value = '';
		obj.focus();
		return false;
	}

	var check= /^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{8,50}$/;
	if (!check.test(upw)) {
		alert('비밀번호는 숫자, 영문자, 특수문자 조합 8자 이상이어야 합니다.'); 
		obj.value = '';
		obj.focus();
		return false;
	}

	// 동일한 문자/숫자 3이상, 연속된 문자
	if(/(\w)\1\1/.test(upw)) {
		alert('비밀번호에 3자 이상의 연속 또는 반복 문자 및 숫자를 사용하실 수 없습니다.'); 
		obj.value = '';
		obj.focus();
		return false;
	}
}

/**
 * 한글과 영문만 가능
 */
function fn_hangulEngOnly(obj)
{
	if (obj.value === '') {
		return;
	}

	if (!/^[가-힣a-zA-Z]+$/.test(obj.value)) {
		alert('한글과 영문만 가능합니다.');
		obj.value = '';
		obj.focus();
		return;
	}
}

/**
 * 숫자만 가능
 */
function fn_numberOnly(obj)
{
	if (obj.value === '') {
		return;
	}

	if (!/^[0-9]+$/.test(obj.value)) {
		alert('숫자만 가능합니다.');
		obj.value = '';
		obj.focus();
		return;
	}
}

/**
 * 숫자만 가능
 * return 0 고정
 */
function fn_numberOnlyReturnValue(obj, currValue)
{
	if (obj.value === '') {
		return;
	}

	if (!/^[0-9]+$/.test(obj.value)) {
		alert('숫자만 가능합니다.');
		obj.value = currValue;
		obj.focus();
		return;
	}
}

function maxLengthCheck(object)
{
	if (object.value.length > object.maxLength) {
		object.value = object.value.slice(0, object.maxLength);
	}
}

function setCookie(cName,cValue,cDay)
{
	var expire = new Date();
	expire.setDate(expire.getDate() + cDay);
	cookies = cName + '=' + escape(cValue) + '; path=/ ';
	if (typeof cDay !== 'undefined') {
		cookies += ';expires=' + expire.toGMTString() + ';';
	}
	document.cookie = cookies;  
}

function deleteCookie(name)
{
	setCookie(name, '', -1);
}

function getCookie(cName)
{
	cName = cName + '=';
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cName);
	var cValue = '';
	if (start !== -1) {
		start += cName.length;
		var end = cookieData.indexOf(';', start);
		if (end == -1) {
			end = cookieData.length;
		}
		cValue = cookieData.substring(start, end);
	}

	return unescape(cValue);
}

function checkNull(cont){
	if(typeof cont ==="undefined" ||cont == null || cont ==""){return true;}else{return false;}
}

//핸드폰번호 마스킹
function maskingPhone(cont){
	var originStr = cont;
	cont = "";
	var phoneStr;
	var maskingStr;
	
	if(checkNull(originStr)){
		return originStr;
	}
	
	if(originStr.toString().split('-').length != 3){
		if(typeof(originStr) === "number"){
			if(originStr.length  == 10){
				phoneStr = originStr.match(/\d{10}/gi)
			}else if(originStr.length  == 11){
				phoneStr = originStr.match(/\d{11}/gi)
			}else if(checkNull(phoneStr)){
				return originStr;
			}
			maskingStr = originStr.toString().replace(phoneStr, phoneStr.toString().replace(/[0-9]/g,'*'));
		}else{
			return originStr;
		}
	}else{
		phoneStr = originStr.match(/\d{2,3}-\d{3,4}-\d{4}/gi);
		if(checkNull(phoneStr)){
			return originStr;
		}  
		maskingStr = originStr.toString().replace(phoneStr, phoneStr.toString().replace(/[0-9]/g,'*'));
	}
	phoneStr  = "";
	originStr = "";
	return maskingStr;
}

//주민번호 마스킹
function maskingRrn(cont){
	var originStr = cont;
	cont= "";
	var rrnStr;
	var maskingStr;
	
	if(checkNull(originStr)){
		return originStr;
	}
	
	rrnStr = originStr.match(/(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4]{1}[0-9]{6}/gi);
	if(!checkNull(rrnStr)){
		maskingStr = originStr.toString().replace(rrnStr, rrnStr.toString().replace(/[0-9]/g,'*'));
	}else{
		rrnStr = originStr.match(/\d{13}/gi);
		if(checkNull(rrnStr)){
			return originStr;
		}else{
			maskingStr = originStr.toString().replace(rrnStr, rrnStr.toString().replace(/[0-9]/g,'*'));
		}
	}
	rrnStr    = "";
	originStr = "";
	return maskingStr;
	
}