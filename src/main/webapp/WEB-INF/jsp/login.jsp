<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/include.css' />" /> 
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	
	<script type="text/javascript">

	$(document).ready(function() {
		// 운영에서는 주석 제거
		$("#loginForm").submit();
	});


	</script>
</head>

<body id="error">
	<div style="display:none;">
		<form id="loginForm" name="loginForm" method="post" action="<c:url value='/login-processing' />">
			<p>
				id : <input type="text" name="username" value="${username}" />
			</p>
			<p>
				password : <input type="password" name="password" value="${username}" />
			</p>
			<p>
				<input type="submit" value="로그인" />
			</p>
		</form>
	</div>
	<div class="error_text_box">
		<i class="icon_login_load"></i>
		<em>로그인 중입니다.</em>
		<div class="loading_bar"><img src="<c:url value='/images/admin/loading_bar.gif' />" alt=""></div>
	</div>
	<p class="error_logo"><img src="<c:url value='/images/admin/logo_error.png' />" alt="삼성증권"></p>
</body>

</html>