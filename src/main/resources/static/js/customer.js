'use strict';

$(document).ready(function() {

	htUtils.setHtUser();

	// ////////////////////////////////////////////////////////////////////////
	// 채팅 클라이언트 설정
	(function() {
		var chatRoomUid = $('#chatRoom input[name="id"]').val();

		console.info('>>> LOAD CHAT ROOM');

		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + chatRoomUid,
//			data: {
//				size: CHAT_MESSAGE_LIST_SIZE,
//			},
		}).done(function(data) {
			if (data) {
				var chatRoom = new ChatRoom(data);
				
				if (chatRoom) {
					var chatRoomStatusCd = chatRoom._chatRoomStatusCd;
					var btnClass = $('#chat_bottom')[0].children[0].className;
					var textarea = $('#chat_bottom .inner textarea')[0];
					 if (chatRoomStatusCd == "11") {
						 //readonly
						 btnClass = "qna-line-wrap type-ing";
						 $('#chat_bottom .inner textarea').eq(0).attr('placeholder','위 항목을 선택해 주세요.');
						 $('#chat_bottom .inner textarea').eq(0).attr('readOnly',true);
					 } else if (chatRoomStatusCd == "31") {
						 btnClass = "qna-line-wrap";
					 } else if (chatRoomStatusCd == "41") {
						 btnClass = "qna-line-wrap";
					 }
					 $('#chat_bottom')[0].children[0].className = btnClass;
				}
				var chatRoomViewer = new ChatRoomViewer({
					chatMessageViewerContainer: $('#chat_body'),
					chatControlContainer: $('#chat_bottom'),
					messageArea: $('#autosize_form01'),
					messageSendButton: $('#chat_bottom .btn_send_area a'),	//2020-04-27 수정
				});
				chatRoom.setViewer(chatRoomViewer);

				htClient = new Client();
				htClient.setChatRoom(chatRoom);
			} else {
				htUtils.alert('채팅방 정보를 가져오지 못했습니다. 다시 접속해주세요.');
				console.error('FAILED GET CHATROOM', chatRoomUid);
			}
		}).fail(function(jqXHR, textStatus, errorThrown) {
			htUtils.alert('채팅방 정보를 가져오지 못했습니다. 다시 접속해주세요.');
	    	console.error('FAIL REQUEST: GET CHATROOM: ', textStatus);
	    });
	})();

	// ////////////////////////////////////////////////////////////////////////
	// 대화 종료
	$('.btn_close_chat').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();
		
		if (htClient.canSendMessage()) {
			htClient.sendEnd();
		}
	});
	
	//검색버튼
	$('.btn_chat_search').on('click',function(e){
		e.preventDefault();
		e.stopPropagation();
		
		/*$('#autosize_form01').focus();*/
		$('.chat-top-inner').hide();
		$('.search_div').show();
		//사이즈 조절
		$("#chat_body").css('height','calc(100% - 60px)');
		
		$("#chat_bottom").hide();
		$(".chat_search").focus();
	});

	// ////////////////////////////////////////////////////////////////////////
	// 한 줄 평가
	// 한 줄 평가 별점
	$(document).on('click', '.icon_star', function(e) {
		$('.icon_star').removeClass('on');
		var level = $('.icon_star').index($(this)) + 1;
		$('.icon_star:lt(' + level + ')').addClass('on');
	});
	
	// 한 줄 평가 등록
	$('#chat_bottom').on('click', '.review_inner .btn-connect', function(e) {
		
		e.preventDefault();
		var MAX_REVIEW_LENGTH = 100;
		var star = $('.icon_star.on').length;
		//var statement = $('.form_star_send').val();
		var statement =$(".review_inner textarea").val();

		if (statement.length > MAX_REVIEW_LENGTH) {
			alert('한 줄 평가는 ' + MAX_REVIEW_LENGTH + '자 이내로 작성해주세요');
			return;
		}

		htClient.reviewChatRoom(star, statement);
		$('#chat_bottom .review_inner textarea').val('');
	});
	// 평가 엔터로 등록
	$('#chat_bottom').on('keyup', '.review_inner .input-connect', function(e) {

		e.preventDefault();
		if (e.keyCode === 13) {
			$('#chat_bottom .review_inner .btn-connect').trigger('click');
		}
	});
	
	// ////////////////////////////////////////////////////////////////////////
	
	
	// ////////////////////////////////////////////////////////////////////////
	$('#alret_confirm').on('click', function(e) {
		$("#alret_layer").hide();
	});
	
	// ////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////
	// 시뮬레이션 중 블록 빌더로 이동 
	$(document).on('click', '#chat_body .block_name span', function(e) {

		e.preventDefault();
		e.stopPropagation();
		var obj = $(this);

		var name = $(this).data('name');

		if (name) {
			window.open(BUILDER_APP_PATH + '/editor/block/by-name/' + encodeURIComponent(name), 'builder');
		}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 용어사전
	$('.search-line').removeClass('on');
	$('.search-line .input').click(function() {
		$('.search-line').addClass('on');
		});
	$('.search-line .btn-close').click(function() {
		$('.search-line').removeClass('on');
	});
	// 용어검색 열기
	$(document).on('click', '#chat_bottom .btn_chat_term', function(e) {
		e.preventDefault();
		e.stopPropagation();
		$(".poopup_chat_term .popup_title").text("용어 사전 검색");
		$(".poopup_chat_term").show();
	});
	// 용어검색 닫기
	$(document).on('click', '.btn_close_detail', function(e) {
		e.preventDefault();
		e.stopPropagation();
		$(".poopup_chat_term").hide();
	});
	
	//용어 검색 자동완성
	$(document).on('keyup', '.poopup_chat_term .term_search', function(e) {
		e.preventDefault();
		console.debug(e.keyCode);
		var schText = $(this).val().trim();
		if(schText.length > 0){
			$(".btn_search_term_close").show();
		}else{
			$(".btn_search_term_close").hide();
		}

		if (e.keyCode === 27) { // ESC시 자동완성 레이어 가리기
			if ($('#termAutoCmp').is(':visible')) {
				$('#termAutoCmp').empty();
				$('#termAutoCmp').hide();
			}
		} else if (e.keyCode === 13) { // 엔터시 첫번째 자동완성 컨텐츠 선택
			if ($('#termAutoCmp').is(':visible')) {
				if ($('#termAutoCmp .auto_cmp').length > 0) {
					$(this).val($('#termAutoCmp .auto_cmp:eq(0)').text());
					$('#termAutoCmp').empty();
					$('#termAutoCmp').hide();
				}
			}
		} else {
			fn_autoCmp('termAutoCmp',$(this).val().trim());
		}
	});
	//용어검색 검색어 지우기
	$(document).on('click', '.btn_search_term_close', function(e) {
		e.preventDefault();
		$('.term_search').val('').focus();
		$(".btn_search_term_close").hide();
	});
	//용어상세 보기
	$(document).on('click', '.poopup_chat_term dd.text', function(e) {
		e.preventDefault();
		var isText = $(this).eq(0);
		if(isText.hasClass('text_ellipsis')){
			isText.removeClass('text_ellipsis');
		}else{
			isText.addClass('text_ellipsis');
		}
	});
	
	//용어검색
	$(document).on('click', '.poopup_chat_term .btn_search_term', function(e) {
		e.preventDefault();
		$('#termAutoCmp').hide();
		$('.term_search_list').html('');
		var schText = $(".poopup_chat_term .term_search").val();
		
		fn_searchTerm(schText);
	});
	
	//검색  input box	
	$(document).on('keyup', ' .chat_search', function(e) {
		var searchText = $(this).val();
		searchCnt = 0;
		if(searchText.length > 0){
			$(".btn_search_clear").show();
		}else{
			$(".btn_search_clear").hide();
		}
		$('.type_text').each(function(index) {
			//var contents =$(this).text();
			var contents =$(this).html().replace(/<(\/span|span)([^>]*)>/gi,'');
			if(searchText != ""){
				var regex = new RegExp('(' + searchText + ')', 'g');
				contents = contents.replace(regex, '<span class="point_blue_bg">$1</span>');	
			}
			$(this).html(contents);
		});
		
		if($(".point_blue_bg").length > 0){
			searchCnt = 0;
			//$('.btn_search_next').trigger("click");
			moveMessage(searchCnt);
			$('.btn_search_next').removeClass("onDisabled");
			$('.btn_search_prev').addClass("onDisabled");
			if($(".point_blue_bg").length == 1){
				$('.btn_search_next').addClass("onDisabled");
			}
		}
		
	});
	
	//검색 삭제	
	$(document).on('click', '.btn_search_clear', function(e) {
		$(".chat_search").val("");
		
		$(".btn_search_next").addClass("onDisabled");
		$(".btn_search_prev").addClass("onDisabled");
		
		$(".chat_search").trigger('keyup');
	});
	
	$(document).on('click', '.btn_search_exid', function(e) {
		$(".chat-top-inner").show();
		$(".search_div").hide();
		$(".chat_search").val("");
		$(".chat_search").trigger('keyup');
		
		//사이즈 조절
		$("#chat_bottom").show();
		$("#chat_body").css('height','calc(100% - 120px)');
		
		$('.search_div').hide();
	});
	
	//다음 검색
	$(document).on('click', '.btn_search_next', function(e) {
		
		if(!$(".btn_search_next").hasClass("onDisabled")){
			
			var targetList = $(".point_blue_bg");
			$(".point_blue_bg").removeClass('inHighLigth');
			searchCnt++;
			
			if(searchCnt == 0){
				moveMessage(searchCnt);
				$(".btn_search_prev").addClass("onDisabled");
			}else if(searchCnt >= 0){
				moveMessage(searchCnt);
				$(".btn_search_prev").removeClass("onDisabled")
			}
			
			if(searchCnt >= (targetList.length-1)){
				$(".btn_search_next").addClass("onDisabled")
			}
		}
	});
	
	//이전 검색
	$(document).on('click', ' .btn_search_prev', function(e) {
		if(!$(".btn_search_prev").hasClass("onDisabled")){
			
			var targetList = $(".point_blue_bg"); 
			$(".point_blue_bg").removeClass('inHighLigth');
			searchCnt--;
			
			if(searchCnt == targetList.length){
				searchCnt = searchCnt - 2;
				moveMessage(searchCnt);
				$(".btn_search_next").removeClass("onDisabled");
			}else if(searchCnt >= 0){
				moveMessage(searchCnt);
				$(".btn_search_next").removeClass("onDisabled")
			}
			
			if(searchCnt == 0 ){
				$(".btn_search_prev").addClass("onDisabled")
			}
		}
	});
});

function moveMessage(searchCnt){
	/*$(".bubble-templet").removeClass("msg_shadow");
	$(".bubble-round").removeClass("msg_shadow");
	$(".bubble-templet").removeClass("msg_shadow");
	$(".bubble-arrow-blue").removeClass("msg_shadow");
	$(".bubble-arrow").removeClass("msg_shadow");*/
	
	$(".point_blue_bg").eq(searchCnt).addClass('inHighLigth');
	var target = "+="+($(".point_blue_bg").eq(searchCnt).parent().offset().top - 87);
	/*$(".point_blue_bg").eq(searchCnt).parent().parent().addClass("msg_shadow");*/
	$('#chat_body').animate({scrollTop : target}, 150)
}

function fn_textChat(termTitle){
	$('.term_search').val(termTitle);
	$('#termAutoCmp').hide();
	$(".btn_search_term").trigger("click");
}

function fn_autoCmp(domId, inputText) {

	var autoCompleteList = autoCmpTermList;

	$('#' + domId).html('');
	
	if (inputText.length > 1) {
		$('.no-data-search').hide();
		var html = '';
		var validCnt = 0;
		var regexp = /[\{\}\[\}\/?.,;:|\)*~'!^\-_+<>@\#$%&\\\=|(\'\"]/gi;
		if (autoCompleteList.length > 0) {
			for (var i = 0; i < autoCompleteList.length; i++) {
				var content = autoCompleteList[i].title.replace(regexp,"")
				content = content.toLowerCase();
				inputText = inputText.replace(regexp,"");
				inputText = inputText.toLowerCase();
				var content = content.match(inputText);

				//var content = autoCompleteList[i].title.match(inputText);
				if (content !== null) {
					var contents = inputText;
					var regex = new RegExp('(' + inputText + ')', 'g');
					var contents = autoCompleteList[i].title.replace(regex, '<span class="point">$1</span>');
					
					html +='<a onclick="javascript:fn_textChat(&#39;' + autoCompleteList[i].title + '&#39;);">'
					html +='<li class="relation-line">';
					html +='<span class="bg">아이콘</span><span class="text">'+contents;
					html +='</span></span></li></a>';
					validCnt++;
				}
			}
			if (validCnt === 0) {
				$('#termAutoCmp').hide();
			}else{
				$('#termAutoCmp').html(html);
				$('#termAutoCmp').show();						
			}
		}
	} else {
		$('#' + domId).hide();
	}
}

function fn_searchTerm(inputText){
	var term_search_list = autoCmpTermList;
	
	if (inputText.length > 1) {
		var html = '';
		var validCnt = 0;
		var regexp = /[\{\}\[\}\/?.,;:|\)*~'!^\-_+<>@\#$%&\\\=|(\'\"]/gi;
		if (term_search_list.length > 0) {
			for (var i = 0; i < term_search_list.length; i++) {
				var content = term_search_list[i].title
				inputText = inputText.replace(regexp,"");

				inputText = inputText.toLowerCase();
				content = content.toLowerCase();
//				var content = content.replace(regexp,"").match(inputText);
				var content = content.match(inputText);
				
				var tags = term_search_list[i].term_tag;
				
				if(tags != null){
					tags = tags.toLowerCase();
					tags = term_search_list[i].term_tag.match(inputText);
				}else{
					tags = null;
				}
				
				if (content !== null ||tags !== null ) {
					html += '<dl class="line">';
					html += '<dt class="tit-info">'+term_search_list[i].title+'</dt>';
					var text = term_search_list[i].cont.replace('/(\n|\r\n)/g','<br>')
					html += '<dd class="text text_ellipsis" style="white-space: pre-line;">'+text+'</dd>';
					html += '</dl>';
					validCnt++;
				}
			}
			if (validCnt === 0) {
				$('.no-data-search').show()
				$(".result-wrap").hide();
			}else{
				$('.no-data-search').hide();
				$(".result-wrap").show();
				$(".term_cnt").text(validCnt);
				$('.info-list').html(html);
			}
			fn_reportingTerm(validCnt);
		} 
	}else {
		$('.no-data-search').show();
		$(".result-wrap").hide();
	}
}

function fn_reportingTerm(validCnt){
	
	//용어사전통계
	$.ajax({
		url : HT_APP_PATH + '/api/customer/insertReportingTerm',
		data : {
			schCnt : validCnt,
			schText : $(".term_search").val()
		},
		type: 'post'
	}).done(function(result) {
	});
}

var autoCmpTermList;
$(window).on('load', function() {
	
	//용어사전검색
	$.ajax({
		url : HT_APP_PATH + '/api/customer/selectAutoTermListAjax',
		data : {
		},
		type: 'post'
	}).done(function(result) {
		autoCmpTermList = result
	});
	
	// ////////////////////////////////////////////////////////////////////////
	// 브라우저 활성화 이벤트
	
	ifvisible.setIdleDuration(HT_USER_IDLE_TIME_SECONDS);
	ifvisible.idle(function() {

		htBrowserActive = false;

		if (htClient.canSendMessage()) {
			htClient.sendActive(USER_ACTIVE_STATUS.IDLE);
		}

	}).wakeup(function() {

		htBrowserActive = true;

		if (htClient.canSendMessage()) {
			if ($('#autosize_form01').val().length > 0) {
				htClient.sendActive(USER_ACTIVE_STATUS.TYPING);
			} else {
				htClient.sendActive(USER_ACTIVE_STATUS.ACTIVE);
			}

			// 마지막 메세지에 읽음 상태 처리
    		var chatMessage = htClient.getChatRoom().getLastChatMessage();
	    	if (chatMessage) {
	    		if (chatMessage.chatNum
	    				&& chatMessage.status !== CHAT_MESSAGE_STATUS.READ) {
		    		console.info('SEND STATUS AS READ BY BROWSER ACTIVE');
		    		htClient.sendStatus(CHAT_MESSAGE_STATUS.READ, chatMessage);
	    		}
	    	}
		}
		htClient.sendActive(USER_ACTIVE_STATUS.ACTIVE);
    });
	
	// ////////////////////////////////////////////////////////////////////////
	// 말풍선 클릭시 채팅내용 복사
	/*var clipboard = new ClipboardJS('#chat_body .text_box .type_text', {
		text: function($trigger) {
			var $parent = $trigger.closest('.text_box');
			if ($('.chat_text_slide', $parent).length > 0) {
				return '';
			}

			var resultText = '';
			$('.type_text', $parent).each(function() {
				if (resultText === '') {
					resultText += $(this).text();
				} else {
					resultText += ' ' + $(this).text();
				}
			});

			return resultText;
		},
	});
	clipboard.on('success', function(e) {
		e.clearSelection();
		e.trigger.blur();
		htUtils.alert('메세지를 복사했습니다.');
	});*/
}).on('beforeunload', function() {
	//////////////////////////////////////////////////////////////////////////
	// 창, 탭 닫기시 소켓 연결 해제
//	if (htClient.isConnected()) {
//		htClient.disconnect();
//	}
}).on('unload', function() {
}).on('visibilitychange', function() {
	if(document.visibilityState === 'visible') htClient.sendActive(USER_ACTIVE_STATUS.ACTIVE);
	else 	 htClient.sendActive(USER_ACTIVE_STATUS.IDLE);
});


