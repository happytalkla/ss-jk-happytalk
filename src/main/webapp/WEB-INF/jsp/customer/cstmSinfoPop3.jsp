<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>삼성증권</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/data_style.css' />" />
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.dotdotdot.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/common.js' />"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		//셀렉트 박스 onchange시 색상변경
		var selectTarget = $('.selectbox');
		selectTarget.change(function() {
			$(this).css("color","#000");
		});
	});

	function fn_save() {
		if ($('#cardCstm').val() === '') {
			alert('카드주를 입력해 주세요.');
			return false;
		}

		if ($('#cardCd').val() === '') {
			alert('카드사를 선택해 주세요.');
			return false;
		}

		if ($('#cardNo1').val() === '' || $('#cardNo2').val() === ''
			|| $('#cardNo3').val() === '' || $('#cardNo4').val() === '') {
			alert('카드번호를 입력해 주세요.');
			return false;
		}

		if ($('#checkMonth').val() === '' || $('#checkYear').val() === '') {
			alert('유효기간을 입력해 주세요.');
			return false;
		}

		$.ajax({
			url: '<c:url value="/customer/saveCstmSinfo" />',
			data: $('#cstmSinfoForm').serialize(),
			type: 'post'
		}).done(function(result) {
			if (result !== null && result.rtnCd !== null && result.rtnCd === 'SUCCESS') {
				alert(result.rtnMsg);
				htUtils.closeWindow();
			} else {
				alert(result.rtnMsg);
m			}
		});
	}


	</script>

</head>

<body>
<form id="cstmSinfoForm" name="cstmSinfoForm" method="post" action="">
	<input type="hidden" name="chatRoomUid" id="chatRoomUid" value="${chatRoomUid}" />
	<input type="hidden" name="part" id="part" value="3" />
	<div class="top">
		<img src="<c:url value='/images/hk/logo.png' />" alt="삼성증권">
	</div>

	<div class="contents">
		<div class="title-btn">
			<div >
				<span style="color:white">민감정보 수집</span>
			</div>
		</div>

		<div class="description notap" >
			<p class="point_color">안전한 정보 수집을 위해 데이터를 암호화하고 있어요!</p>
			<p>상담직원이 요청한 내용만 입력해주세요</p>
		</div>

		<div class="top-txt">카드정보</div>
		<div class="data-area">
			<div class="info-box">
				<div>카드주</div>
				<input type="text" name="cardCstm" id="cardCstm" value="" tabindex="1"/>
			</div>
			<div class="info-box ">
				<div>카드사</div>
				<select name="cardCd" id="cardCd" class="selectbox" tabindex="2">
					<option>선택하세요</option>
					<c:forEach var="data" items="${cardList}" varStatus="status1">
						<option value="${data.cardCd}">${data.cardNm}</option>
					</c:forEach>	
				</select>			
			</div>
			<div class="info-box">
				<div>카드번호</div>
				<div class="data-4-align">
					<input type="text" pattern="\d*" name="cardNo1" id="cardNo1" value="" maxlength="4"  oninput="maxLengthCheck(this)" tabindex="3" style="float:none;"/> 
					<input type="text" pattern="\d*" name="cardNo2" id="cardNo2" value="" maxlength="4"  oninput="maxLengthCheck(this)" tabindex="4" style="float:none;"/>
					<input type="text" pattern="\d*" name="cardNo3" id="cardNo3" value="" maxlength="4"  oninput="maxLengthCheck(this)" tabindex="5" style="float:none;"/>
					<input type="password" pattern="\d*" name="cardNo4" id="cardNo4" value="" maxlength="4"  oninput="maxLengthCheck(this)" tabindex="6" style="float:none;"/>
				</div>
			</div>
			<div class="info-box">
				<div>유효기간</div>
				<div class="expiration">
					<input type="number" name="checkMonth" id="checkMonth" value="" maxlength="2"  placeholder="MM" pattern="\d*" oninput="maxLengthCheck(this)" tabindex="7" /><span>월</span>
					<input type="number" name="checkYear" id="checkYear" value="" maxlength="4" placeholder="YYYY" pattern="\d*" oninput="maxLengthCheck(this)" tabindex="8" /><span>년</span>
				</div>
			</div>
			<div class="btn" onclick="javascript:fn_save();">
				<div>
					<a href="#">전송</a>
				</div>
			</div>
		</div>
	</div>

	<div class="footer">
		<img src="<c:url value='/images/hk/bottom.jpg' />" width="100%">
	</div>
</form>
</body>
</html>
