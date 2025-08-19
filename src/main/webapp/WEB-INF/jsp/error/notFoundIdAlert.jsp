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
				"본인인증된 정보로는 삼성증권 고객으로 확인되는 사항이 없습니다.\n"
				+ "만약, \"삼성증권에 계약이 분명 있는데?\" 라고 생각하신다면, 아래의 방법 중 1가지로 확인하여 주시기 바랍니다.\n"
				+ "본인인증된 정보로는 삼성증권 고객으로 확인되는 사항이 없습니다.\n\n"
				+ "1. 대표홈페이지 간편로그인 후 보험계약조회 진행하기\n"
				+ "2. 대표콜센터 (1688-1688)로 문의하기\n"
				+ "※ 콜센터 운영시간 : 평일 오전 9시~오후 6시"
			);

			_close();
		});
	</script>
</head>
<body id="error"></body>
</html>