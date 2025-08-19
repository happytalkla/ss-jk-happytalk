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
</head>

<body id="chat">
	<!-- 00.채팅창:컨텐츠영역-->
	<div id="chat_login" class="chatting">
		<div class="chat_login_info"><c:out value="${message}" /></div>
	</div>
	<!--// 00.채팅창:컨텐츠영역 -->
<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
<script src="<c:url value="/js/jquery.min.js" />"></script>
<script src="<c:url value="/js/jquery-ui.min.js" />"></script><%--
<script src="<c:url value="/js/common.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>--%>
<script>
$(document).ready(function() {
});
</script>
</body>
</html>
