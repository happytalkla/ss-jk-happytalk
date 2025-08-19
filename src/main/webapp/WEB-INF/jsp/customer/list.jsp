<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/include.css" />" />
	<style type="text/css">
	.chat_list_bottom .left.green {color: #47b819}
	.chat_list_bottom .left.status_11 {color: #47b819}

	.chat_list_bottom .left.blue {color: #60a3d8}
	.chat_list_bottom .left.status_31 {color: #60a3d8}
	.chat_list_bottom .left.status_41 {color: #60a3d8}

	.chat_list_bottom .left.red {color: #ff0000}
	.chat_list_bottom .left.status_51 {color: #ff0000}
	.chat_list_bottom .left.status_61 {color: #ff0000}

	.chat_list_bottom .left.block {color: #000}
	.chat_list_bottom .left.status_91 {color: #000}
	</style>
</head>

<body id="chat">
	<!-- 00.채팅창:head-->
	<div id="chat_head" class="chatting">
		<button type="button" class="btn_go_prev">이전으로 가기</button>
		<h1 class="chat_logo"><img src="<c:url value="/images/chat/logo02.png" />" alt="O2Talk"></h1>
	</div>
	<!--// 00.채팅창:head-->

	<!-- 00.채팅창:컨텐츠영역-->
	<div id="chat_list_body">
		<ul class="chat_list">
			<c:forEach var="chatRoom" items="${chatRoomList}">
			<li data-chat-room-uid="${chatRoom.chatRoomUid}">
				<div class="list_date">
					<p class="no_day">${chatRoom.roomCreateDay}</p>
					<p class="day_name">${chatRoom.roomCreateWeekday}</p>
				</div>
				<div class="list_body">
					<p class="title">
						<a href="<c:url value="/customer/${chatRoom.chatRoomUid}?entranceCode=${chatRoom.ctgNum}" />">
						<c:if test="${not empty chatRoom.lastContText}">
						${chatRoom.lastContText}
						</c:if>
						<c:if test="${empty chatRoom.lastContText}">
						${chatRoom.roomTitle}
						</c:if>
						</a></p>
					<p class="chat_list_date">${chatRoom.roomCreateDateTime}</p>
					<p class="chat_list_bottom">
						<span class="left status_${chatRoom.chatRoomStatusCd}">${chatRoom.chatRoomStatusCdNm}</span>
						<c:if test="${chatRoom.chatRoomStatusCd eq '91'}">
						<span class="right">${chatRoom.endDivCdNm}</span>
						</c:if>
						<c:if test="${chatRoom.chatRoomStatusCd ne '91'}">
						<span class="right">${chatRoom.cnsrDivCdNm}</span>
						</c:if>
					</p>
				</div>
				<button type="button" class="btn_go_trash">삭제</button>
			</li>
			</c:forEach>
		</ul>
		<button type="button" class="btn_plus_chat">추가</button>
	</div>
	<!--// 00.채팅창:컨텐츠영역 -->

	<!-- layer:채팅삭제 -->
	<div class="poopup_chat_delete" data-room-id="" style="display: none;">
		<div class="inner">
			<div class="popup_head">
				<h2 class="popup_title">채팅삭제</h2>
				<button type="button" class="btn_close_del">창닫기</button>
			</div>
			<div class="popup_body">
				해당 채팅내역을 리스트에서 삭제 하시겠습니까?
				<div class="popup_btn_right">
					<button type="button" class="btn_popupchat_cancel">취소</button>
					<button type="button" class="btn_popupchat_del">삭제</button>
				</div>
			</div>
		</div>
	</div>
	<!-- layer:채팅삭제 -->
<form id="user" style="display: none;">
	<input type="hidden" name="id" value="<c:out value="${ user.cstm_uid }" />" />
	<input type="hidden" name="nickName" value="<c:out value="${ user.cstm_uid }" />" />
	<input type="hidden" name="userType" value="U" />
	<input type="hidden" name="rollType" value="U" />
</form>
<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
<script src="<c:url value="/js/jquery.min.js" />"></script>

<script src="<c:url value="/js/common.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script>
$(document).ready(function() {

	// 신규 상담
	$('.btn_plus_chat').on('click', function(e) {
		window.location.href = '<c:url value="/customer" />';
	});

	// 상담내역 돌아가기
	$('.btn_go_prev').on('click', function(e) {
		window.location.href = '<c:url value="/customer/${previousChatRoomUid}?entranceCode=${param.entranceCode}" />';
	});

	// '채팅방 감추기' 팝업 열기
	$('.btn_go_trash').on('click', function(e) {
		var chatRoomUid = $(this).closest('li').data('chat-room-uid');
		$('.poopup_chat_delete').attr('data-room-id', chatRoomUid).show();
	});

	// '채팅방 감추기' 팝업 닫기
	$('.btn_close_del, .btn_popupchat_cancel').on('click', function() {
		$('.poopup_chat_delete').hide();
	});

	// '채팅방 감추기' 실행
	$('.btn_popupchat_del').on('click', function() {

		var chatRoomUid = $('.poopup_chat_delete').data('room-id');
		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + chatRoomUid,
			method: 'put',
			data: {
				command: CHAT_COMMAND.HIDE_BY_CUSTOMER,
				userId: htUser.getId(),
			},
		}).done(function(data, textStatus, jqXHR) {
			location.reload();
		}).fail(function(jqXHR, textStatus, errorThrown) {
	    	console.error('FAIL REQUEST: HIDE BY CUSTOMER: ', textStatus);
	    }).always(function() {
	    	;
	    });
	});
	
	var varUA = navigator.userAgent.toLowerCase();
	var ref= document.referrer;

	var iconUrl = "";

	/*
	if(varUA.match("android") != null){
		
		//안드로이드
		if(varUA.match("android 8") == null){
			//아이콘 표시 생기는 버전
			iconUrl = '<c:url value="/images/icon_72_android_1.png" />';
			
		}else{
			//버전 생기지 않는 버전
			iconUrl = '<c:url value="/images/icon_72_android_2.png" />';
			
		}
		$("head").append('<link rel="shortcut icon" href="'+iconUrl+'" />');
	}else{
		//ios or pc
		iconUrl = '<c:url value="/images/icon_114_ios.png" />';
		$("head").append('<link rel="apple-touch-icon" href="'+iconUrl+'" />');
		$("head").append('<link rel="apple-touch-icon-precomposed" href="'+iconUrl+'" />');
	}*/
});
</script>
</body>
</html>