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
	<script type="text/javascript" src="/js/jquery-1.12.2.min.js"></script>
</head>

<body id="chat">
	<!-- 00.채팅창:head-->
	<div id="chat_head" class="chatting">
		<button type="button" class="btn_go_history">상담내역 가기</button>
		<h1 class="chat_logo"><img src="/images/chat/logo02.png" alt="O2Talk"></h1>
	</div>
	<!--// 00.채팅창:head-->
	<!-- 00.채팅창:컨텐츠영역-->
	<div id="chat_list_body">
		<ul class="chat_list">
			<c:forEach var="item" items="${ctgList}">
				<li>
					<a href="<c:url value="/customer">
						<c:param name="userType" value="${param.userType}" />
						<c:param name="entranceCode" value="${item.ctg_num}" />
						<c:param name="userId" value="${user.coc_id}" />
					</c:url>" class="chat_category">${item.ctg_nm}</a>
				</li>
			</c:forEach>
		</ul>
	</div>
	
	<!--// 00.채팅창:컨텐츠영역 -->

<script>
$(document).ready(function() {
	// 상담내역 돌아가기
	$('.btn_go_history').on('click', function() {
		window.location.href = '<c:url value="/customer/list?userType=${param.userType}&entranceCode=${param.entranceCode}&userId=${user.coc_id}" />';
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
