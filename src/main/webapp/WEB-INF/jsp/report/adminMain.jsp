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
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

	<script type="text/javascript">

		$(document).ready(function() {
			$(document).on("click", ".btn_view_day", function() {
				if($('#isToday').val() == "true"){

					window.location.href="<c:url value='/report/adminMain' />?isToday=flase";
				}else{

					window.location.href="<c:url value='/report/adminMain' />?isToday=true";
				}
			});
			/*
			var selectDate = new Date();
			var year = selectDate.getFullYear();
			var month = selectDate.getMonth()+1;
			var date = selectDate.getDate();
			if($('#isToday').val() == "true"){
				$('.btn_view_day').html("Yesterday");
				$('.dash').html(year+"년 "+month+"월 "+date+"일 <span>Today 요약</span>");
				$('.dash_time').html("수집시간 : AM 00 : 00 ~ "+$('#nowTime').val());
			}else{
				$('.btn_view_day').html("Today");
				var yesterDate = new Date(year,month,date-1);
				year = yesterDate.getFullYear();
				month = yesterDate.getMonth();
				date = yesterDate.getDate();
				$('.dash').html(year+"년 "+month+"월 "+date+"일 <span>Yeasterday 요약</span>");
				$('.dash_time').html("수집시간 : AM 00 : 00 ~ PM 12 : 00 ");
			}
			*/

		//	selectDate.setYear(Number(selectDay[0])+1900);
		//	selectDate.setMonth(Number(selectDay[1])-1);
		//	selectDate.setDate(Number(selectDay[2]));
		});


	</script>

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
		<div class="left_content_area">
		<form id="mainForm" name="mainForm" method="post" action="">
		<input type="hidden" name="isToday" id="isToday"  value="${isToday}"/>
		<input type="hidden" name="nowTime" id="nowTime"  value="${nowTime}"/>


			<div class="left_content_head">
				<h2 class="sub_tit">대시보드</h2>
				<span class="left_top_text">웹 사이트의 전체적인 상황을 추적하고 보여줍니다.</span>
			</div>
			<div class="inner">
				<div class="top_datepicker_area">
					<c:choose>
						<c:when test="${isToday eq 'true' }">
							<em class="dash">${schDate } <span>Today 요약</span></em>
							<button type="button" class="btn_view_day">Yesterday</button>
						</c:when>
						<c:otherwise>
							<em class="dash">${schDate } <span>Yesterday 요약</span></em>
							<button type="button" class="btn_view_day">Today</button>
						</c:otherwise>
					</c:choose>
				</div>
				<p class="dash_time">수집시간 : AM 00 : 00 ~ ${nowTime } </p>
				<div class="id_info_area">
				<c:forEach var="data" items="${basicList}" varStatus="status">

					<div class="id_box">
						<span class="tit">전체 상담</span>
						<em>${data.total_cns} <span>건</span></em>
					</div>
					<div class="id_box">
						<span class="tit">상담 대기</span>
						<em>${data.wait_cns} <span>건</span></em>
					</div>
					<div class="id_box">
						<span class="tit">상담직원 상담진행</span>
						<em>${data.prsc_cns} <span>건</span></em>
					</div>
					<div class="id_box">
						<span class="tit">상담종료</span>
						<em>${data.end_cns} <span>건</span></em>
					</div>
					<div class="id_box w100">
						<span class="tit">평균 종료 시간</span>
						<em>
						<c:if test="${data.avg_time_cns_date != '0'}">
						${data.avg_time_cns_date} <span>일</span>
						</c:if>
						<c:if test="${data.avg_time_cns_hour != '0'}">
						${data.avg_time_cns_hour} <span>시</span>
						</c:if>
						<c:if test="${data.avg_time_cns_min != '0'}">
						${data.avg_time_cns_min} <span>분</span>
						</c:if>
						${data.avg_time_cns_sec} <span>초</span>

						</em>
					</div>
					<div class="id_box">
						<span class="tit">투입 상담직원 수</span>
						<em>${data.cnt_adv} <span>명</span></em>
					</div>
					<div class="id_box w100">
						<span class="tit">상담직원당 평균 상담종료</span>
						<em>
						<c:choose>
							<c:when test="${data.cnt_adv != 0}">
								${data.end_cns / data.cnt_adv}
							</c:when>
							<c:otherwise>
								0
							</c:otherwise>
						</c:choose>
						 <span>건</span></em>
					</div>
				</c:forEach>
				</div>

				<div class="talk_info_area">

				<c:forEach var="data" items="${linkDivList}" varStatus="status">
					<div class="id_box kyobo">
						<span class="tit"><i></i>웹채팅</span>
						<span class="text">(상담/종료)</span>
						<em>${data.web_cnt }</em>
					</div>
					<%--
					<div class="id_box naver">
						<span class="tit"><i></i>네이버톡톡</span>
						<span class="text">(상담/종료)</span>
						<em>${data.naver_cnt }</em>
					</div>
					<div class="id_box kakao">
						<span class="tit"><i></i>카카오톡</span>
						<span class="text">(상담/종료)</span>
						<em>${data.kakao_cnt }</em>
					</div>
					 --%>
				</c:forEach>
				<c:if test="${!empty ctgList}">
				<c:forEach var="data" items="${ctgList}" varStatus="status">
					<div class="id_box">
						<span class="tit">${data.ctg_nm }</span>
						<span class="text">(상담/종료)</span>
						<em>${data.cnt }</em>
					</div>
				</c:forEach>
				</c:if>
				</div>
			</div>
		</form>

		</div>
		<!--// left_area -->
	</div>

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

<!-- 유저 정보 -->
<form id="user" style="display: none;">
	<input type="hidden" name="id" value="<c:out value="${member.memberUid}" />" />
	<input type="hidden" name="nickName" value="<c:out value="${member.name}" />" />
	<input type="hidden" name="userType" value="<c:out value="${member.memberDivCd}" />" />
	<input type="hidden" name="departCd" value="<c:out value="${member.departCd}" />" />
	<input type="hidden" name="rollType" value="S" />
</form>
<!--// 유저 정보 -->
</body>
</html>
