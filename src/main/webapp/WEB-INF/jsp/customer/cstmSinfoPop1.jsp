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

	function fn_save() {
		if($('#name').val() == '') {
			alert("이름을 입력해 주세요.");
			return false;
		}

		if($("#telNo1" ).val()=='' || $("#telNo2" ).val()=='' || $("#telNo3" ).val()==''){
			alert("연락처를 입력해 주세요.");
			return false;
		}

		$.ajax({
			url : "<c:url value='/customer/saveCstmSinfo' />",
			data : $("#cstmSinfoForm").serialize(),
			type : "post",
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					alert(result.rtnMsg);
					htUtils.closeWindow();
				}else{
					alert(result.rtnMsg);
				}
			}
		});
	}
	</script>

</head>

<body>
<form id="cstmSinfoForm" name="cstmSinfoForm" method="post" action="">
	<input type="hidden" name="chatRoomUid" id="chatRoomUid" value="${chatRoomUid}" />
	<input type="hidden" name="part" id="part" value="1" />
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

		<div class="top-txt">연락처정보</div>
		<div class="data-area">
			<div class="info-box">
				<div>이름</div>
				<input type="text" name="name" id="name" value="" onblur="fn_hangulEngOnly(this);" tabindex="1">
			</div>
			<div class="info-box">
				<div>연락처</div>
				<div class="data-3-align">
					<input type="number" name="telNo1" id="telNo1" value="" onblur="fn_numberOnly(this);" tabindex="2" style="float:none;">
					<input type="number" name="telNo2" id="telNo2" value="" onblur="fn_numberOnly(this);" tabindex="3" style="float:none;">
					<input type="number" name="telNo3" id="telNo3" value="" onblur="fn_numberOnly(this);" tabindex="4" style="float:none;">
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
