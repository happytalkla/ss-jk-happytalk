<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
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
		<h1 class="chat_logo"><img src="<c:url value="/images/chat/logo02.png" />" alt="O2Talk"></h1>
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
				O2Talk 보험전문가와 지금 바로 채팅상담 하시겠어요?
				</div>
				<ul class="go_link slide">
					<li><a tabindex="0" href="<c:url value='/customer?hideEndBtn=true&withBotYn=N&ctgNum=9999999998&departCd=TM&useFrame=Y'><c:param name="title" value="${title}" /></c:url>" class="btn_select">네</a></li>
					<li><a href="<c:url value='/customer?hideEndBtn=true&withBotYn=Y&entranceCode=${entranceCode}&useFrame=Y'><c:param name="title" value="${title}" /></c:url>" class="btn_select">아니오</a></li>
				</ul><%--
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
