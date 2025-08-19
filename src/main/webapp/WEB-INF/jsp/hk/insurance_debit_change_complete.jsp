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
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/insurance_debit_change.css' />">
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript">

		// up button 클릭
		$(document).on("click", '#btn_close', function() {
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

<body>

	<div class="top">
		<img src="/happytalk/images/hk/logo.png" />
		<div class="step" >
			<ul>
				<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">1</span></li>
				<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">2</span></li>
				<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">3</span></li>
				<li class="step_active"><img src="/happytalk/images/hk/step1.png" /><span class="step_num">4</span><span> 완료</span></li>
			</ul>
		</div>
	</div>



	<div class="contents step3">
			<div class="contents-line"></div>
			<div class="step3-img">
				<div><img src="/happytalk/images/hk/complete.png" class="completeImg"/></div>
			</div>
			<div class="step3-text">
				<div class="completeText" >자동이체 계좌/이체일 변경 신청이<br>정상 처리 되었습니다.</div>
			</div>
			<div >
				<div class="step3-subText">
					<p>고객을 위해 보다 나은 서비스를 드리는<br/>보험회사로 거듭나겠습니다.<br/><br/>감사합니다.</p>
				</div>
			</div>
			<div class="btn" id="btn_close">
				<div>
					<a href="#">확인</a>
				</div>
			</div>
	</div>
	<div class="footer">
		<div >
			<img src="/happytalk/images/hk/bottom.jpg" width="100%">
		</div>
	</div>

</body>
</html>