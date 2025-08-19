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
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript">

		// on, off 버튼 클릭
		$(document).on("click", 'input[name="btn_auth_success"], input[name="btn_auth_phone_success"]', function() {
			$.ajax({
				url : "<c:url value='/sample/auth_success' />",
				data : {
					"uuid" : "${uuid}",
					"channel" : "${channel}",
					"auth_type" : "${auth_type}"
				},
				type : "post",
				success : function(result) {
					alert("인증이 완료 되었습니다.");
					_close()
				},
				complete : function() {
					
				}
			});
		});

		// 상담직원 근무여부 설정
		$(document).on("click", 'input[name="btn_auth_cancel"]', function() {
			$.ajax({
				url : "<c:url value='/sample/auth_cancel' />",
				data : {
					"uuid" : "${uuid}",
					"channel" : "${channel}"
				},
				type : "post",
				success : function(result) {
					alert("인증이 해제 되었습니다.");
					_close()
				},
				complete : function() {
					
				}
			});
		});
		
		// up button 클릭
		$(document).on("click", 'input[name="btn_close"]', function() {
			_close()
		});

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
	</script>

</head>

<body id="admin">

	<div class="container">
		<input name="btn_auth_success" type="button" value="인증하기" />
		<input name="btn_auth_cancel" type="button" value="인증해제" />
		<input name="btn_close" type="button" value="닫기" />
	</div>
</body>
</html>