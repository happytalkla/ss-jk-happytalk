<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="ko">
<head>
<title>디지털채팅상담시스템</title>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/include.css' />" />
<!--[if lt IE 9]>
	<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
	<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
	<![endif]-->
<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script src="<c:url value="/js/sockjs.min.js" />"></script>
<script src="<c:url value="/js/stomp.min.js" />"></script>
<script
	src="<c:url value='/js/menu.js'><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script
	src="<c:url value="/js/common.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script
	src="<c:url value="/js/chatRoom.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script
	src="<c:url value="/js/chatRoomList.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script
	src="<c:url value="/js/client.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/manager.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script type="text/javascript" 	src="<c:url value='/js/ChartNew.min.js' />"></script>
<script type="text/javascript">
	$(document)
			.ready(

					function() {
						$('.left_content_area').css("height","none");
						// 현재 날짜 시간 가져오기
						var date = new Date();
						var y = date.getFullYear();
						var m = date.getMonth()+1;
						var d = date.getDate();
						var yeasterDayDate = new Date();
						yeasterDayDate.setDate(yeasterDayDate.getDate()-1);
						var yy = yeasterDayDate.getFullYear();
						var ym = yeasterDayDate.getMonth()+1;
						var yd = yeasterDayDate.getDate();
						if(m < 10){
							m = "0"+m;
						}
						if(d < 10){
							d = "0"+d;
						}

						if(ym < 10){
							ym = "0"+ym;
						}

						if(yd < 10){
							yd = "0"+yd;
						}
						$(document)
								.on(
										"click",
										".btn_view_day",
										function() {
											var  isToday = $('#isToday').val();
											if ($('#isToday').val() == "true") {
												isToday = $('#isToday').val("false");

												isToday="flase";

											} else {
												isToday = $('#isToday').val("true");

												isToday="true";
											}

											$.ajax({
												url : "<c:url value='/report/managerMain' />",
												data : {
														 "isAjax" : "true"
														 ,"isToday" : isToday
													   },
												type : "post",
												success : function(result) {
													$("#day_area").html(result);

														var chart = document.getElementById("canvas_Line1").getContext("2d");

														new Chart(chart).Doughnut(chart_data, {
															legend : true,
															showSingleLegend : true,
															legendBorders : false,
															responsive : true,
															annotateDisplay : true,
															annotateRelocate : true,
															detectAnnotateOnFullLine : true,
															annotateFontSize : 10,
															annotateBorderRadius : '5px',
															annotatePadding : "10px",
															annotateBackgroundColor : 'rgba(0,0,0,0.7)',
															inGraphDataMinimumAngle : 1
														});

														$(document).on("click", "#adviserTr", function() {
															$('#member_uid').val($(this).attr('data-adviser'));
															selectAdviser($(this).attr('data-adviser'));
															$('.adviserTr').removeClass('active');
															$(this).addClass('active')
														});
														if(isToday == "true"){
															$('#dash_day').html(y+"년 "+m+"월"+d+"일 <span>Today 요약</span>");
															$('#dash_time_day').html("수집시간 : AM 00 : 00 ~ "+$("#nowTime").val());

															$('#sub_stit_cnsr').html(y+"년 "+m+"월"+d+"일 Today 상담직원별 처리 현황</span>");
															$('#dash_time_cnsr').html("수집시간 : AM 00 : 00 ~ "+$("#nowTime").val());

															$('#sub_stit_score').html(y+"년 "+m+"월"+d+"일 Today 상담 만족도</span>");
															$('#dash_time_score').html("수집시간 : AM 00 : 00 ~ "+$("#nowTime").val());
															$('.btn_view_day').html("Yesterday");

														}else{
															$('#dash_day').html(yy+"년 "+ym+"월"+yd+"일 <span>Today 요약</span>");
															$('#dash_time_day').html("수집시간 : AM 00 : 00 ~ PM 11 : 59");

															$('#sub_stit_cnsr').html(yy+"년 "+ym+"월"+yd+"일 Yesterday 상담직원별 처리 현황</span>");
															$('#dash_time_cnsr').html("수집시간 : AM 00 : 00 ~ PM 11 : 59");

															$('#sub_stit_score').html(yy+"년 "+ym+"월"+yd+"일 Yesterday 상담 만족도</span>");
															$('#dash_time_score').html("수집시간 : AM 00 : 00 ~ PM 11 : 59");
															$('.btn_view_day').html("Today");
														}



												},
												complete : function() {

													$('.adviserTr td:first').click();

												}
											});

										});
						$(document).on("click", "#adviserTr", function() {
							$('#member_uid').val($(this).attr('data-adviser'));
							selectAdviser($(this).attr('data-adviser'));
							$('.adviserTr').removeClass('active');
							$(this).addClass('active')
						});
						// 이전 클릭
						$(document).on("click", "#mainForm .dev_prev_btn", function() {
							var nowPage = $("#mainForm [name='nowPage']").val()*1 - 1;
							var totPage = $("#mainForm [name='totPage']").val()*1;
							if(nowPage <= 0){
								return false;
							}
							$("#mainForm [name='nowPage']").val(nowPage);
							goPaging();
						});

						// 이후 클릭
						$(document).on("click", "#mainForm .dev_next_btn", function() {
							var nowPage = $("#mainForm [name='nowPage']").val()*1 + 1;
							var totPage = $("#mainForm [name='totPage']").val()*1;
							if(nowPage > totPage){
								return false;
							}
							$("#mainForm [name='nowPage']").val(nowPage);
							goPaging();
						});


						// 페이지 입력창 키 입력 처리
						$(document).on("keypress", "#mainForm [name='inputNowPage']", function(e) {
							if(e.keyCode == 13){
								e.preventDefault();
								$("#mainForm [name='inputNowPage']").trigger("change");
							}
						});

						// 페이지 입력창 입력
						$(document).on("change", "#mainForm [name='inputNowPage']", function() {
							var nowPage = $("#mainForm [name='nowPage']").val();
							var inputNowPage = $(this).val()*1;
							var totPage = $("#mainForm [name='totPage']").val()*1;

							if(!$.isNumeric(inputNowPage)){
								alert("숫자만 입력해 주세요.");
								$("#mainForm [name='inputNowPage']").val(nowPage);
								return false;
							}

							if(inputNowPage <= 0){
								alert("1페이지 부터 입력해 주세요");
								$("#mainForm [name='inputNowPage']").val(nowPage);
								return false;
							}

							if(inputNowPage > totPage){
								alert("최대 페이지가 "+totPage+"페이지 입니다.");
								$("#mainForm [name='inputNowPage']").val(nowPage);
								return false;
							}

							$("#mainForm [name='nowPage']").val(inputNowPage);
							goPaging();
						});

						// 팝업 닫기 버튼
						 $('body').on('click', '.btn_popup_close', function(e) {
								$('.popup').hide();
						 });



						$('#dash_month').html(y+"년 "+m+"월 <span>서비스 상태</span>");
						$('#dash_time_month').html("서비스 시간 : "+y+"-"+m+"-01 ~ "+y+"-"+m+"-"+d);
						$('#dash_day').html(y+"년 "+m+"월"+d+"일 <span>Today 요약</span>");
						$('#sub_stit_cnsr').html(y+"년 "+m+"월"+d+"일 Today 상담직원별 처리 현황</span>");
						$('#dash_time_cnsr').html("수집시간 : AM 00 : 00 ~ "+$("#nowTime").val());

						$('#sub_stit_score').html(y+"년 "+m+"월"+d+"일 Today 상담 만족도</span>");
						$('#dash_time_score').html("수집시간 : AM 00 : 00 ~ "+$("#nowTime").val());



	});

	var room_waiting_cnt;
	var room_ing_cnt;
	var room_end_cnt;

	<c:forEach var="data" items="${selectTotalGroupReport}" varStatus="status">
	room_waiting_cnt = ${data.wait_cnt};
	room_ing_cnt = ${data.prsc_cnt};
	room_end_cnt = ${data.end_cnt};
	</c:forEach>


	var chart_data = [];
	chart_data.push({
		value : room_waiting_cnt,
		color : "#ffa941",
		highlight : "#ffa941",
		label : "상담대기"
	});
	chart_data.push({
		value : room_ing_cnt,
		color : "#417dd6",
		highlight : "#417dd6",
		label : "상담진행중"
	});
	chart_data.push({
		value : room_end_cnt,
		color : "#00ba63",
		highlight : "#00ba63",
		label : "상담완료"
	});
	window.onload = function() {

		var chart = document.getElementById("canvas_Line1").getContext("2d");

		new Chart(chart).Doughnut(chart_data, {
			legend : true,
			showSingleLegend : true,
			legendBorders : false,
			responsive : true,
			annotateDisplay : true,
			annotateRelocate : true,
			detectAnnotateOnFullLine : true,
			annotateFontSize : 10,
			annotateBorderRadius : '5px',
			annotatePadding : "10px",
			annotateBackgroundColor : 'rgba(0,0,0,0.7)',
			inGraphDataMinimumAngle : 1
		});

		$('#adviserTr td:first').click();

	}

	function selectAdviser(member_uid){
		$.ajax({
			url : "<c:url value='/report/selectCounseler' />",
			data : {
					 "csr_uid" : member_uid
					 ,"is_detail" : "Y"
					 ,"isToday" : $('#isToday').val()
				   },
			type : "post",
			success : function(result) {
				$("#graph_area").html(result);

					var chart = document.getElementById("canvas_Line1").getContext("2d");

					new Chart(chart).Doughnut(chart_data, {
						legend : true,
						showSingleLegend : true,
						legendBorders : false,
						responsive : true,
						annotateDisplay : true,
						annotateRelocate : true,
						detectAnnotateOnFullLine : true,
						annotateFontSize : 10,
						annotateBorderRadius : '5px',
						annotatePadding : "10px",
						annotateBackgroundColor : 'rgba(0,0,0,0.7)',
						inGraphDataMinimumAngle : 1
					});




			},
			complete : function() {


			}
		});
	}

	function goPaging(){
		var  isToday = $('#isToday').val();

		$.ajax({
			url : "<c:url value='/report/selectScore' />",
			data : {
					 "isToday" : isToday,
					 "nowPage" : $('#nowPage').val(),
					 "totPage" : $('#totPage').val()
				   },
			type : "post",
			success : function(result) {
				$("#bottom_score_area").html(result);
				var date = new Date();
				var y = date.getFullYear();
				var m = date.getMonth()+1;
				var d = date.getDate();
				var yeasterDayDate = new Date();
				yeasterDayDate.setDate(yeasterDayDate.getDate()-1);
				var yy = yeasterDayDate.getFullYear();
				var ym = yeasterDayDate.getMonth()+1;
				var yd = yeasterDayDate.getDate();
				if(m < 10){
					m = "0"+m;
				}
				if(d < 10){
					d = "0"+d;
				}

				if(ym < 10){
					ym = "0"+ym;
				}

				if(yd < 10){
					yd = "0"+yd;
				}
				if ($('#isToday').val() == "true") {

					$('#sub_stit_score').html(y+"년 "+m+"월"+d+"일 Today 상담 만족도</span>");
					$('#dash_time_score').html("수집시간 : AM 00 : 00 ~ "+$("#nowTime").val());

				}else{

					$('#sub_stit_score').html(yy+"년 "+ym+"월"+yd+"일 Yesterday 상담 만족도</span>");
					$('#dash_time_score').html("수집시간 : AM 00 : 00 ~ PM 11 : 59");
				}


			},
			complete : function() {

			}
		});

	}

	function openChatRoom(chatRoomUid){
		$.ajax({
			url: "<c:url value='/api/html/chat/room/'/>" + chatRoomUid,
			data: {
				rollType: "C",
				withChatMessageList: 'true',
				withMetaInfo: 'true',
			},
			dataType: 'html',
		}).done(function(data) {
			//$('.popup').hide();
			$('.popup_counseling').show()
			$('.popup_counseling').html(data);

			// 멀티형 메세지 슬라이더
			new Swiper($('.popup_counseling .chat_text_slide'), {
				slidesPerView: 'auto',
				centeredSlides: false,
				spaceBetween: 30,
			});
	    }).fail(function(jqXHR, textStatus, errorThrown) {
	    	console.error('FAIL REQUEST: REVIEW: ', textStatus);
	    });
	}
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
				<input type="hidden" name="isToday" id="isToday" value="${isToday}" />
				<input type="hidden" name="nowTime" id="nowTime" value="${selectNowTime}" />


				<div class="left_content_head">
					<h2 class="sub_tit">대시보드</h2>
					<span class="left_top_text">웹 사이트의 전체적인 상황을 추적하고 보여줍니다.</span>
				</div>
				<div class="inner">
					<div class="top_datepicker_area">
						<em class="dash" id="dash_month">2018년 09월 <span>서비스 상태</span></em>
					</div>
					<p class="dash_time" id="dash_time_month">서비스 시간 : 2018-09-01 ~ 2018-09-19</p>
					<div class="id_info_area">
						<c:forEach var="data" items="${basicMonthlyList}"
							varStatus="status">
							<div class="id_box">
								<span class="tit">월 전체 상담</span> <em>${data.total_cns} <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit">월 전체 상담 종료</span> <em>${data.end_cns} <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit"> 평균 상담 종료 시간</span> <em> <c:if
										test="${data.avg_time_cns_date != '0'}">
						${data.avg_time_cns_date} <span>일</span>
									</c:if> <c:if test="${data.avg_time_cns_hour != '0'}">
						${data.avg_time_cns_hour} <span>시</span>
									</c:if> <c:if test="${data.avg_time_cns_min != '0'}">
						${data.avg_time_cns_min} <span>분</span>
									</c:if> ${data.avg_time_cns_sec} <span>초</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">상담직원당 평균 처리</span> <em><c:choose>
										<c:when test="${data.cnt_adv != 0}">
								${data.end_cns / data.cnt_adv}
							</c:when>
										<c:otherwise>
								0
							</c:otherwise>
									</c:choose> <span>건</span> </em>
							</div>
							<div class="id_box">
								<span class="tit">상담인원</span> <em>${data.cnt_adv} <span>명</span></em>
							</div>
						</c:forEach>
					</div>

					<div class="talk_info_area">
						<c:forEach var="data" items="${linkDivMonthlyList}"
							varStatus="status">
							<div class="id_box kyobo">
								<span class="tit"><i></i>삼성증권 채팅</span> <span class="text">(상담/종료)</span>
								<em>${data.web_cnt }</em>
							</div>
							<div class="id_box naver">
								<span class="tit"><i></i>O2톡</span> <span class="text">(상담/종료)</span>
								<em>${data.o2_cnt }</em>
							</div>
							<div class="id_box kakao">
								<span class="tit"><i></i>카카오톡</span> <span class="text">(상담/종료)</span>
								<em>${data.kakao_cnt }</em>
							</div>
							<div class="id_box kakao">
								<span class="tit"><i></i>mPop톡</span> <span class="text">(상담/종료)</span>
								<em>${data.mpop_cnt }</em>
							</div>
							
						</c:forEach>
					</div>
				</div>
				<!-- 아래 블럭 -->
				<div id="day_area">
					<div class="inner">
						<div class="top_datepicker_area">
							<em class="dash" id="dash_day">2018년 09월 11일 <span>Today
									요약</span></em>
							<button type="button" class="btn_view_day">YesterDay</button>
						</div>
						<p class="dash_time" id="dash_time_day">수집시간 : AM 00 : 00 ~ ${selectNowTime}</p>
						<div class="id_info_area">
							<c:forEach var="data" items="${basicList}" varStatus="status">

								<div class="id_box">
									<span class="tit">전체 상담</span> <em>${data.total_cns} <span>건</span></em>
								</div>
								<div class="id_box">
									<span class="tit">상담 대기</span> <em>${data.wait_cns} <span>건</span></em>
								</div>
								<div class="id_box">
									<span class="tit">상담직원 상담진행</span> <em>${data.prsc_cns} <span>건</span></em>
								</div>
								<div class="id_box">
									<span class="tit">상담종료</span> <em>${data.end_cns} <span>건</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit">평균 종료 시간</span> <em> <c:if
											test="${data.avg_time_cns_date != '0'}">
							${data.avg_time_cns_date} <span>일</span>
										</c:if> <c:if test="${data.avg_time_cns_hour != '0'}">
							${data.avg_time_cns_hour} <span>시</span>
										</c:if> <c:if test="${data.avg_time_cns_min != '0'}">
							${data.avg_time_cns_min} <span>분</span>
										</c:if> ${data.avg_time_cns_sec} <span>초</span>

									</em>
								</div>
								<div class="id_box">
									<span class="tit">투입 상담직원 수</span> <em>${data.cnt_adv} <span>명</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit">상담직원당 평균 상담종료</span> <em> <c:choose>
											<c:when test="${data.cnt_adv != 0}">
									${data.end_cns / data.cnt_adv}
								</c:when>
											<c:otherwise>
									0
								</c:otherwise>
										</c:choose> <span>건</span></em>
								</div>
							</c:forEach>
						</div>

						<div class="talk_info_area">

							<c:forEach var="data" items="${linkDivList}" varStatus="status">
								<div class="id_box kyobo">
									<span class="tit"><i></i>삼성증권 채팅</span> <span class="text">(상담/종료)</span>
									<em>${data.web_cnt }</em>
								</div>
								<%--
								<div class="id_box naver">
									<span class="tit"><i></i>네이버톡톡</span> <span class="text">(상담/종료)</span>
									<em>${data.naver_cnt }</em>
								</div>
								<div class="id_box kakao">
									<span class="tit"><i></i>카카오톡</span> <span class="text">(상담/종료)</span>
									<em>${data.kakao_cnt }</em>
								</div>
								 --%>
							</c:forEach>
							<c:if test="${!empty ctgList}">
								<c:forEach var="data" items="${ctgList}" varStatus="status">
									<div class="id_box">
										<span class="tit">${data.ctg_nm }</span> <span class="text">(상담/종료)</span>
										<em>${data.cnt }</em>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>

					<div class="inner">
						<div class="graph_area">
							<!-- 왼쪽 테이블 영역 -->
							<div class="left_table_area">
								<h3 class="sub_stit" id="sub_stit_cnsr">2018년 09월 19일 Today 상담직원별 처리 현황</h3>
								<p class="dash_time" id="dash_time_cnsr">수집시간 : AM 00 : 00 ~ PM 02 : 11 </p>
								<div class="graph_table_area dash_left_table">
									<table class="tCont service">
										<caption>통계테이블 목록입니다.</caption>
										<colgroup>
											<col style="width:16%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
										</colgroup>
										<thead>
											<tr>
												<th>상담직원</th>
												<th>상담대기</th>
												<th>상담진행중</th>
												<th>상담완료</th>
												<th>평균 상담종료 시간</th>
												<th>상담 종료 미처리</th>
												<th>휴식여부</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
											<c:when test="${empty selectTotalGroupReport}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${selectTotalGroupReport}"
													varStatus="status">
													<tr  data-adviser="${data.member_uid}" class="adviserTr" id="adviserTr">
														<td>${data.name }</td>
														<td>${data.wait_cnt }</td>
														<td>${data.prsc_cnt }</td>
														<td>${data.end_cnt }</td>
														<td><c:if test="${data.cns_date != 0 }">
													${data.cns_date }일
												</c:if> <c:if test="${data.cns_hour != 0 }">
													${data.cns_hour }시
												</c:if> <c:if test="${data.cns_min != 0 }">
													${data.cns_min }분
												</c:if> ${data.cns_sec }초</td>
														<td>${data.no_pcs_cnt }</td>
														<td>${data.work_yn }</td>
													</tr>
												</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
								<div class="table_bottom_area">
									<div class="table_count">
										<span class="cont_text"><em>${cnsTotCount}</em> count(s)</span>
									</div>
								</div>
							</div>


							<!--// 왼쪽 테이블 영역 -->

							<!-- 오른쪽 그래프 영역 -->
							<div class="right_graph_area" id="graph_area">
								<h3 class="sub_stit">appliance 상담직원</h3>
								<div class="graph_view_area">
									<canvas id="canvas_Line1" height="90"></canvas>
								</div>
								<table class="tCont">
									<caption>상담직원별 처리 상황 목록입니다.</caption>
									<colgroup>
										<col style="width: 25%">
										<col style="width: 25%">
										<col style="width: 25%">
										<col style="width: 25%">
									</colgroup>
									<tbody>
										<tr>
											<th>전체상담</th>
											<td>0건</td>
											<th>상담대기</th>
											<td>0건</td>
										</tr>
										<tr>
											<th>상담진행중</th>
											<td>0건</td>
											<th>상담완료</th>
											<td>0건</td>
										</tr>
									</tbody>
								</table>
							</div>
							<!--// 오른쪽 그래프 영역 -->
						</div>
					</div>

					<div class="inner" id="bottom_score_area">
						<h3 class="sub_stit" id="sub_stit_score">2018년 09월 19일 Today 상담 만족도</h3>
						<p class="dash_time" id="dash_time_score">수집시간 : AM 00 : 00 ~ PM 02 : 11</p>

						<div class="graph_table_area">

							<div class="counseling_star_area">
							<c:set var="isDot" value="false" />
							<c:set var="compareDot" value="false" />
							<c:forEach var="i" begin="1" end="5">
								<c:choose>
									<c:when test="${i <= scoreAvg}">
									<span class="star_full"><i></i>
									<p style="width: 100%"></p></span>
									</c:when>
									<c:when test="${isDot == compareDot}">
									<c:set var="isDot" value="true" />
									<span class="star_full"><i></i>
									<p style="width:${data.avg_evl_score_dot}0%"></p></span>
									</c:when>
									<c:otherwise>
									<span class="star_full"><i></i>
									<p style="width: 0%"></p></span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						<!-- 		<span class="star_full"><i></i>
								<p style="width: 100%"></p></span> <span class="star_full"><i></i>
								<p style="width: 80%"></p></span> <span class="star_full"><i></i>
								<p style="width: 60%"></p></span> <span class="star_full"><i></i>
								<p style="width: 40%"></p></span> <span class="star_full"><i></i>
								<p style="width: 20%">

									</p></span> -->
								<p class="star_full_text">매우 만족해요 (${scoreAvg}점/5점)</p>
							</div>
							<table class="tCont service">
								<caption>통계테이블 목록입니다.</caption>
								<colgroup>
									<col style="width: 12%">
									<col style="width: 12%">
									<col style="width: 12%">
									<col style="width: 18%">
									<col style="width: 14%">
									<col style="width: 20%">
									<col style="width:">
								</colgroup>
								<thead>
									<tr>
										<th>평가입력 날짜</th>
										<th>상담종료 날짜</th>
										<th>별점</th>
										<th>한줄평가</th>
										<th>상담직원</th>
										<th>대화방</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${empty scoreList}">
											<tr>
												<td colspan="9">조회된 내용이 없습니다.</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach var="data" items="${scoreList}" varStatus="status">
												<tr>
													<td>${data.create_dt }</td>
													<td>${data.room_end_dt }</td>
													<td>
														<div class="reporting_list_star">
															<c:set var="isDot" value="false" />
															<c:set var="compareDot" value="false" />
															<c:forEach var="i" begin="1" end="5">
																<c:choose>
																	<c:when test="${i <= data.evl_score}">
																		<span class="star_full"><i></i>
																			<p style="width: 100%"></p></span>
																	</c:when>
																	<c:when test="${isDot == compareDot}">
																		<c:set var="isDot" value="true" />
																		<span class="star_full"><i></i>
																			<p style="width:${data.avg_evl_score_dot}0%"></p></span>
																	</c:when>
																	<c:otherwise>
																		<span class="star_full"><i></i>
																			<p style="width: 0%"></p></span>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</div>
													</td>
													<td>${data.evl_cont }</td>
													<td>${data.name }</td>
													<td><a href="javascript:openChatRoom('${data.chat_room_uid}')" class="link_report_title">${data.chat_room_uid}</a></td>
													<td></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<div class="table_bottom_area">
								<!-- pager -->
								<input type="hidden" name="nowPage" id="nowPage" value="${nowPage }" />
								<input type="hidden" name="totPage"	id="totPage" value="${totPage }" />
								<div class="pager">
									<button type="button" class="btn_prev_page dev_prev_btn"
										id="dev_prev_btn">이전으로</button>
									<input type="text" class="form_pager" value="${nowPage }"
										name="inputNowPage"> <span class="page_no">/
										${totPage } page(s)</span>
									<button type="button" class="btn_next_page dev_next_btn"
										id="dev_next_btn">다음으로</button>
								</div>
								<!--// pager -->

								<!--// pager -->
								<div class="table_count">
									<span class="cont_text"><em>${totCount }</em> count(s)</span>
								</div>
							</div>
						</div>
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
	<!-- popup:상담내역 -->
	<div class="popup popup_counseling" style="display: none">
	</div>
	<!--// popup:상담내역 -->
</body>
</html>
