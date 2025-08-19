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
		function _close() {
			try {
				window.close();
			} catch (e) {
			}
	
			// 카카오톡 인앱 창닫기 버튼
			var _ua = window.navigator.userAgent || window.navigator.vendor
					|| window.opera;
	
			if (_ua.toLocaleLowerCase().indexOf("kakaotalk")) {
				window.location.href = (/iPad|iPhone|iPod/.test(_ua)) ? "kakaoweb://closeBrowser"
						: "kakaotalk://inappbrowser/close";
			}
		}

		$(document).ready(function() {
			alert(
			    "죄송합니다.\n"
			    + "현재, 통신장애로 인하여 해당 서비스가 정상적으로 진행되지 못했습니다.\n"
			    + "해당 서비스의 장애가 장시간 지속될 수도 있으니 번거로우시더라도 "
			    + "고객센터(1588-2323)로 연락 부탁드려요~ 친절하게 도와 드리겠습니다.\n"
			    + "※ 고객센터 운영시간 : 평일 오전 8시~오후 6시"
			);

			_close();
		});
	</script>
</head>
<body id="error"></body>
</html>