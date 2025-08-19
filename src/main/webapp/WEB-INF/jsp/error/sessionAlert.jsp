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
		function _close()
		{
			// 카카오톡 인앱 창닫기 버튼
			var _ua = window.navigator.userAgent || window.navigator.vendor || window.opera;
			if (_ua.toLocaleLowerCase().indexOf("kakaotalk") > -1) {
				// 카카오 인앱 브라우저에서 종료시
				try { window.close(); } catch (e) {}
				try { self.close(); } catch (e) {}
				try {
					window.location.href = (/iPad|iPhone|iPod/.test(_ua)) ? "kakaoweb://closeBrowser" : "kakaotalk://inappbrowser/close";
				} catch (e) {}
			} else {
				// 카카오 이외의 브라우저에서 종료시
				try { window.close(); } catch (e) {}
				try { self.close(); } catch (e) {}
				alert("자동으로 닫히지 않을 경우 해당 페이지를 수동으로 닫아주세요.");
			}
		}

		$(document).ready(function() {
			alert(
				"인증정보가 없어 접속을 종료합니다.\n"
				+ "재인증을 하여 주시기 바랍니다."
			);

			_close();
		});
	</script>
</head>
<body id="error"></body>
</html>