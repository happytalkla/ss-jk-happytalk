<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
	<title>삼성증권</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='${skinCssUrl}' />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/include.css" />" />
</head>

<body id="chat">
	<!-- 00.채팅창:head-->
	<div id="chat_head" class="chatting">
		<button type="button" class="btn_go_history">상담내역 가기</button>
		<!--button type="button" class="btn_go_prev">뒤로가기</button-->
		<h1 class="chat_logo"><img src="<c:url value="/images/chat/logo02.png" />" alt="삼성증권"></h1>
	</div>
	<!--// 00.채팅창:head-->

	<!-- 00.채팅창:컨텐츠영역-->
	<div id="chat_body">
		<div data-message-id="797476" data-message-sender="9999999996" class="message_box left_area">
			<i class="icon_bot chatbot">로봇</i>
			<div class="text_box"><!--
				<div class="img_area faq_icon_tit">
					<img src="https://chat.heungkukfire.co.kr/happytalk/images/catebot_greeting.jpg" alt="인사말 이미지" />
				</div>-->
				<div class="inner_text type_text">
				네이버
				</div><%--
				<span class="date">오후 07:12</span>--%>
			</div>
		</div>
	</div>
	<!--// 00.채팅창:컨텐츠영역 -->

<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
</body>
</html>
