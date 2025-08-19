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
</head>

<body id="error">
	<div class="error_text_box">
		<i></i>
		<c:if test="${!empty message}">
		${message}
		</c:if>
		<c:if test="${empty message}">
		<em>페이지를 찾을수 없습니다.</em>
		관리자에게 문의하세요.
		</c:if>
	</div>
	<p class="error_logo"><a href="<c:url value='/admin' />"><img src="<c:url value='/images/admin/logo_error.png' />" alt="삼성증권"></a></p>
	<script>
	$(window).on('load', function() {
		<c:if test="${!empty alert}">
		setTimeout(function () {
			if (!alert('${alert}')) {
				window.close();
			}
		}, 500);
		</c:if>
	});
	</script>
</body>

</html>