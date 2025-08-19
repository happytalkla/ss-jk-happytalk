<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
	<title>삼성증권 - 인증</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='${skinCssUrl }' />" />
	<style type="text/css">
	.text_box {
		cursor: pointer;
	}
	</style>
</head>

<body id="chat">
	인증이 완료되었습니다.
<form id="chatRoom" style="display: none;">
	<input type="hidden" name="chatRoomUid" value="<c:out value="${ chatRoom.chatRoomUid }" />" />
	<input type="hidden" name="cstmUid" value="<c:out value="${ chatRoom.cstmUid }" />" />
	<input type="hidden" name="memberUid" value="<c:out value="${ chatRoom.memberUid }" />" />
</form>
<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
<script src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
</body>
</html>