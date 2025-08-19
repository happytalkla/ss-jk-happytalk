<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<title>흥국화재챗봇</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/include.css' />" />
<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/ChartNew.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

</head>

<body id="admin">
	<!-- head -->
	<div class="head">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
	</div>
	<!--// head -->

	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/menu.jsp" />

		<!-- left_area -->
		<form id="batchForm" name="dailyForm" method="post" action="?">
			<div class="left_content_area">
				<div class="left_content_head">
					<h2 class="sub_tit">통계 수동 배치</h2>
					<span class="left_top_text">통계를 수동으로 적재처리합니다.</span>
					
				</div>
				<div class="inner">
					<div class="top_filter_area">
						<div class="search_area" style="width:70%">
							<div>
								<span class="dot_tit">수동배치일</span> 
								&nbsp;&nbsp;
								<input type="text" class="datepicker" name="startDate" id="startDate" value="${startDate}" autocomplete="off"> ~ 
								<input type="text" class="datepicker" name="endDate" id="endDate" value="${endDate}" autocomplete="off">
								<button type="button" class="btn_holiday_plus" id="btn_search_date" style="width:100px;">시작</button>
							</div>	
						</div>
					</div>
					
					
				</div>
				<div class="id_info_area" style="padding-left:100px;">
						${msgText }
					</div>
			</div>
		</form>
		<!--// left_area -->
	</div>
</body>
</html>
<script>
$(document).on("click",".btn_holiday_plus",function() {
	if (periodDayCheck() && $('#startDate').val() != '' && $('#endDateArray').val() != '') {
		$("#batchForm").submit();
	}
});
function periodDayCheck(){
	var iDay = 1000 * 60 * 60 * 24;
	var startDateArray = $('#startDate').val().split('-');
	var endDateArray = $('#endDate').val().split('-');
	var startDateObj = new Date(startDateArray[0],Number(startDateArray[1])-1,startDateArray[2]);
	var endDateObj = new Date(endDateArray[0],Number(endDateArray[1])-1,endDateArray[2]);

	var todateObj = new Date();
	var todateYear = todateObj.getFullYear();
	var todateMon = todateObj.getMonth() + 1;
	var todateDay = todateObj.getDate();
	if(todateMon < 0) todateMon = "0" + todateMon;
	if(todateDay < 0) todateDay = "0" + todateDay;
	
	var betweenDay = (todateObj.getTime()-endDateObj.getTime()) / 1000 / 60/60/ 24;
	console.info("==============" + betweenDay);
	if(betweenDay < 1){
		alert('최소 1일 이전까지 적재 가능합니다.');
		return false;
	}else{
		return true;
	}
}
</script>