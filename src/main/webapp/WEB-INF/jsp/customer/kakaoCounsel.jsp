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
		<%-- <h1 class="chat_logo"><img src="<c:url value="/images/chat/logo02.png" />" alt="O2Talk"></h1> --%>
	</div>
	<!--// 00.채팅창:head-->

	<!-- 00.채팅창:컨텐츠영역-->
	<div id="chat_body">
		<div data-message-id="797476" data-message-sender="9999999996" class="message_box left-chat-wrap">
			<div class="bg-chat">상담아이콘</div>
			<div class="bubble-wrap">
				<article class="inner-chat">
					<div class="bubble-templet type-2">
						<span class="bg"></span>
						<dt class="tit">
							<a>
								<img src="/happytalk/images/chat/welcome.png" alt="Hi" class="chatImageCs">
							</a>
						</dt>
						<div class="mch type_text">카카오로 이동중입니다.</div>
					</div>
				</article>
			</div>
		</div>
	</div>
	<!--// 00.채팅창:컨텐츠영역 -->

	<form id="open" name="open" action="https://bizmessage.kakao.com/chat/open" method="post" style="display: none;">
		<input type="hidden" name="uuid" value="${kakaoCounselId}" />
		<input type="hidden" name="extra" value="${extra}" />
		<input type="submit" />
	</form>

<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
<script src="<c:url value='/js/jquery.min.js' />"></script>
<script type="text/javascript" src="https://bizmessage.kakao.com/chat/includeScript"></script>
<script>
$(document).ready(function() {
	$('#open').submit();
});
</script>
</body>
</html>
