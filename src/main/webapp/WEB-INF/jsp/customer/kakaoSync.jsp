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
	<div id="chat_body" style="text-align: center;">
		<a id="custom-login-btn" href="javascript:init()">
			<img src="https://mud-kage.kakao.com/14/dn/btqbjxsO6vP/KPiGpdnsubSq3a0PHEGUK1/o.jpg" width="300" alt="카카오 로그인 버튼" />
		</a>
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
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<script type='text/javascript'>
	var chatRoomUid = '${chatRoomUid}';

	function init()
	{
		Kakao.init('${kakaoJavascriptApiKey}');
		if (Kakao.isInitialized() === true) {
			console.info('init complete');
		}
		Kakao.Auth.login({
			success: function(authObj) {
				console.info(authObj);

				Kakao.API.request({
					url: '/v2/user/me'
				}).then(function(success) {
					return success;
				}, function(error) {
					console.log(error);
				}).then(function (userInfo) {
					console.info('userInfo', userInfo);
					if (userInfo && userInfo.kakao_account) {
						$.ajax({
							url: '/happytalk/api/customer/event/' + chatRoomUid + '?event_type=kakao_sync_complete',
							method: 'post',
							contentType:"application/json; charset=UTF-8",
							data: JSON.stringify(userInfo),
							dataType: 'json'
						}).done(function(data) {
							console.info(data);
							alert('인증이 정상적으로 처리되었습니다.');
							close_window(false);
						}).fail(function(jqXHR, textStatus, errorThrown) {
							console.error('FAIL REQUEST: KAKAO SYNC COMPLETE', textStatus);
						});
					} else {
						console.info('NO RESULT DATA', userInfo);
						auth_failed();
					}
				});
			},
			fail: function(err) {
				console.log('FAIL REQUEST, KAKAO SYNC', err);
				auth_failed();
			}
		});
	}

	function auth_failed() {
		$.ajax({
			url: '/happytalk/api/customer/event/' + chatRoomUid + '?event_type=kakao_sync_failed',
			method: 'post'
		}).done(function(data) {
			console.info(data);
		});

		alert('인증에 실패하였습니다.');
		//close_window(false);
	}

	function close_window(result) {
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
		}

		var result_message = (result == true) ? '인증이 완료되었습니다.' : '인증에 실패하였습니다.';
		$('#result_message').html(result_message + '<br>페이지를 닫거나 뒤로가기로 채팅창으로 복귀해 주세요.');
		$('#custom-login-btn').hide();
	}

</script>
</body>
</html>
