<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<title>디지털채팅상담시스템</title>
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
<script type="text/javascript"
	src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/ChartNew.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

<script type="text/javascript">
	$(window).load(function(){

		$(".left_content_area").css('height','');
		$("#snb").css("height",($("#area1").height()+347)+"px");
	});


		$(document).ready(function() {
			var selectDay = $('#regDate').val().split("-");
			var selectDate = new Date();
			var strDay
			selectDate.setYear(Number(selectDay[0])+1900);
			selectDate.setMonth(Number(selectDay[1])-1);
			selectDate.setDate(Number(selectDay[2]));
			switch(selectDate.getDay()){
				case 0 : strDay = "토"
					break;
				case 1 : strDay = "일"
					break;
				case 2 : strDay = "월"
					break;
				case 3 : strDay = "화"
					break;
				case 4 : strDay = "수"
					break;
				case 5 : strDay = "목"
					break;
				case 6 : strDay = "금"
					break;

			}
			$(document).on("click", ".btn_holiday_plus", function() {
				$("#dailyForm").attr("action", "<c:url value='/reporting/daily' />");
				$("#dailyForm").submit();
				}
);
			var selectDayStr = selectDate.getYear() + "년 " + (selectDate.getMonth()+1) +"월 "+ selectDate.getDate() +"일 ("+strDay+")";
			$('#dateArea').val(selectDayStr);

			//탭 버튼 동작설정
			$(document).on("click", "li", function() {
				 $('li').removeClass('active');
				 $(this).addClass('active');
				 $('.graph_area').css('display','none');
				 $("#area"+$(this).attr("data-page")).css('display','block');

				 var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
				 var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
				 var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);
				 $('#adviserTr td:first').click();
				 $("#snb").css({"height":$('.left_content_area').height()+"px"});
				 searchCtg();
			});




			//기간검색
			$(document).on("click", ".btn_search_date", function() {
				if(periodDayCheck()){
					// 조회 시작
					$("#dailyForm").attr("action", "<c:url value='/reporting/daily' />");
					$("#dailyForm").submit();
				}
			});

			//일자별 검색
			$(document).on("click", "#timeSearch", function() {
				searchTime()
			});



			//시간별 엑셀
			$(document).on("click", "#timeDown", function() {
				downloadTimeReportExcel()
			});

			//날짜 선택
			$(document).on("change", ".datepicker", function() {
				var selectDay = $('.datepicker').val().split("-");
				var selectDate = new Date();
				selectDate.setYear(Number(selectDay[0])+1900);
				selectDate.setMonth(Number(selectDay[1])-1);
				selectDate.setDate(Number(selectDay[2]));

				var monthStr;
				if(selectDate.getMonth()+1 < 10){
					monthStr = "0"+(selectDate.getMonth()+1);
				}else{
					monthStr = selectDate.getMonth()+1;
				}

				var dateStr;
				if(selectDate.getDate() < 10){
					dateStr = "0"+(selectDate.getDate());
				}else{
					dateStr = selectDate.getDate();
				}

				$('#regDate').val(selectDate.getYear()+"-"+monthStr+"-"+dateStr);

				$("#dailyForm").attr("action", "<c:url value='/reporting/daily' />");
				$("#dailyForm").submit();
			});


			//상담직원 table click

			$(document).on("click", "#adviserTr", function() {
				$('#member_uid').val($(this).attr('data-adviser'));
				selectAdviser($(this).attr('data-adviser'));
				$('.adviserTr').removeClass('active');
				$(this).addClass('active')
			});

			//상담직원 엑셀
			$(document).on("click", "#downAdviser", function() {
				downloadAdviserListExcel()
			});

			//조회
			$(document).on("change", "#cnsr_div_cd", function() {
				searchTime()
			});

			$(document).on("change", "#cnsr_div_cd_ctg", function() {
				searchCtg();
			});

			$(document).on("click", "#ctgDown", function() {
				downloadCtgReportExcel();
			});



		});




		// 일자별 통계
		var dayLabels = new Array(); //일별라벨
		var dayRoomStart = new Array(); // 상담신청 데이터
		var dayRoomEnd = new Array(); // 상담종료 데이터
		var dayAvgEnd = new Array(); // 평균종료시간 데이터
		var dayAvgEndPerMem = new Array(); // 상담직원당 평균 상담 종료 데이터

		//요일별 통계
		var weekAvgEnd = new Array(); // 평균종료시간 그래프



		<c:forEach var="data" items="${timeList}" varStatus="status">
			dayLabels[${status.index}] = '${data.reg_time}';
			dayRoomStart[${status.index}] = '${data.cns_request_cnt}';
			dayRoomEnd[${status.index}] = '${data.cns_end_cnt}';
			dayAvgEnd[${status.index}] = ${data.evl_end_time} / 60;
			
		</c:forEach>

		function randomRange(n1,n2){
			return Math.floor((Math.random()*(n2-n1+1))+n1);
		}

		var datelabel = new Array();
		for (i=0;i<30;i++){
			datelabel[i] = "2020-01-"+i+1;
		};


		var data1 ={
			labels : dayLabels,
			datasets : [
				
				{
					fillColor           : "rgba(255,38,0,0)", // 
					strokeColor         : "rgba(255,38,0,0.5)",
					pointColor          : "rgba(255,38,0,0.5)",
					pointStrokeColor    : "#fff",
					pointHighlightFill  : "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data : dayRoomEnd,
					title : "상담 수"
				}
				]
		};
		var data2 ={
			labels : dayLabels,
			datasets : [
				{
					fillColor           : "rgba(255,200,112,0)", // 
					strokeColor         : "rgba(255,200,112,0.5)",
					pointColor          : "rgba(255,200,112,0.5)",
					pointStrokeColor    : "#fff",
					pointHighlightFill  : "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data : dayAvgEnd,
					title : "평균 종료 시간 (분)"
				}
			]
		};

		var data3 ={
				labels : dayLabels,
				datasets : [
					{
						fillColor           : "rgba(75,117,255,0)", //
						strokeColor         : "rgba(75,117,255,0.5)",
						pointColor          : "rgba(75,117,255,0.5)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(220,220,220,1)",
						data : dayAvgEndPerMem,
						title : "상담직원당 평균 상담 시간(분)"
					}
				]
			};


		var option1 = {
				responsive: true,
				// legend
				legend : true,
				datasetFill: false,
				showSingleLegend: true,
				legendBorders: false,
				// annotate
				annotateDisplay: true,
				annotateRelocate: true,
				detectAnnotateOnFullLine: true,
				annotateFontSize: 10,
				annotateBorderRadius: '5px',
				annotatePadding: "10px",
				annotateBackgroundColor: 'rgba(0,0,0,0.7)'

		};

		window.onload = function(){
			var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
			var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
			var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);

			$('#area2').css('display','none');
			$('#area3').css('display','none');


		}

		function periodDayCheck(){
			var iDay = 1000 * 60 * 60 * 24;
			var startDateArray = $('#startDate').val().split('-');
			var startDateObj = new Date(startDateArray[0],Number(startDateArray[1])-1,startDateArray[2]);
			var endDateArray = $('#endDate').val().split('-');
			var endDateObj = new Date(endDateArray[0],Number(endDateArray[1])-1,endDateArray[2]);

			var betweenDay = (endDateObj.getTime()-startDateObj.getTime()) / 1000 / 60/60/ 24+1;

			if( betweenDay > 91){
				alert('최대 90일까지 조회 가능합니다.');
				return false;
			}else if(betweenDay <1){
				alert('최소 1일까지 조회 가능합니다.');
				return false;
			}else{
				return true;
			}


		}

		function searchTime(){
			$.ajax({
				url : "<c:url value='/reporting/selectTimeReport' />",
				data : {
						 "regDate" : $('#regDate').val(),
						 "cnsr_div_cd" : $('#cnsr_div_cd').val(),
						 "cstm_link_div_cd" : $('#cstm_link_div_cd').val()
					   },
				type : "post",
				success : function(result) {
					$("#area1").html(result);
					var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
					var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
					var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);


				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});
				}
			});

		}

	    // 상담직원 선택

		function selectAdviser(member_uid){
	    	$('#member_uid').val(member_uid);
			$.ajax({
				url : "<c:url value='/reporting/selectAdviserDailyList' />",
				data : {
						 "regDate" : $('#regDate').val(),
						 "member_uid" : member_uid
					   },
				type : "post",
				success : function(result) {
				//	$("#adviserRight").html(result);
				//	var myLine4 = new Chart(document.getElementById("canvas_Line4").getContext("2d")).Line(data4,option1);
				//	var myLine5 = new Chart(document.getElementById("canvas_Line5").getContext("2d")).Line(data5,option1);

					//상담직원 시간별 엑셀
					$(document).on("click", "#downTimeAdviser", function() {
						downloadTimeAdviserListExcel()
					});

				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});
				}
			});
		}



		function downloadTimeReportExcel(){

				$("#dailyForm").attr("action", "<c:url value='/reporting/downloadTimeReportExcel' />");
				$("#dailyForm").submit();
		}


		function downloadAdviserListExcel(){

			$("#startDate").val($("#regDate").val());
			$("#endDate").val($("#regDate").val());
			$("#dailyForm").attr("action", "<c:url value='/reporting/downloadMonthlyAdviserReportAllExcel' />");
			$("#dailyForm").submit();
		}

		function downloadTimeAdviserListExcel(){
			$("#dailyForm").attr("action", "<c:url value='/reporting/downloadTimeReportExcel' />");
			$("#dailyForm").submit();
		}

		function searchCtg(){
			$.ajax({
				url : "<c:url value='/reporting/selectCtgReport' />",
				data : {
						 "startDate" : $('#regDate').val(),
						 "endDate" : $('#regDate').val(),
						 "cnsr_div_cd_ctg" : $('#cnsr_div_cd_ctg').val(),
						 "cstm_link_div_cd_ctg" : $('#cstm_link_div_cd_ctg').val()
					   },
				type : "get",
				success : function(result,textStatus,jqXHR) {

					$("#area3").html(result);


				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});
				}
			});

		}

		function downloadCtgReportExcel(){
			$('#startDate').val($('#regDate').val());
			$('#endDate').val($('#regDate').val());
			$("#dailyForm").attr("action", "<c:url value='/reporting/downloadCtgReportExcel' />");
			$("#dailyForm").submit();
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
		<form id="dailyForm" name="dailyForm" method="post" action="">
			<input type="hidden" name="member_uid" id="member_uid"  />
			<input type="hidden" name="startDate" id="startDate" />
			<input type="hidden" name="endDate" id="endDate" />
			<input type="hidden" name="dayFlag" id="dayFlag" value="day" />
			<div class="left_content_area">
				<div class="left_content_head">
					<h2 class="sub_tit">Daily Reporting<%--(${sessionVo.departCd})--%></h2>
					<span class="left_top_text">일간 상담직원의 실적을 보여줍니다.</span>
				</div>
				<div class="inner">
					<div class="top_filter_area">
						 <span class="dot_tit">일 검색</span>
						<input type="text" class="datepicker"  id="regDate"name="regDate" value="${regDate}"> 
						<span class="option_info_text" style="margin-left:10px;">전일까지의 데이터만 조회가 가능합니다.</span>
					</div>
					<div class="top_filter_area">
						<span class="dot_tit">채널</span>
						<div class="search_area" style="width:80%">
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdE" value="E"${linkCdE }>
			              	<label for="linkDivCdE"><span></span>ARS</label>
			              	<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdD" value="D"${linkCdD }>
			              	<label for="linkDivCdD"><span></span>mPOP</label>
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdA" value="A"${linkCdA}>
			              	<label for="linkDivCdA"><span></span>홈페이지</label>
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdC" value="C"${linkCdC }>
			              	<label for="linkDivCdC"><span></span>O2</label>
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdB" value="B"${linkCdB }>
			              	<label for="linkDivCdB"><span></span>카카오</label>
			              	<button type="button" class="btn_holiday_plus" style="width:200px;">적용</button>
						</div>
					</div>
					<div class="id_info_area">
						<c:forEach var="data" items="${basicList}" varStatus="status">
							<div class="id_box">
								<span class="tit">${sessionVo.departCd} 상담 인입 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_request_cnt ? 0 : data.cns_request_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">근무시간외</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.OUT_SERVICE_CNT ? 0 : data.OUT_SERVICE_CNT}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">배정전 포기 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_bquit_cnt ? 0 : data.cns_bquit_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">배정후 포기 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_aquit_cnt ? 0 : data.cns_aquit_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">바쁜시간 종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.sys_full_cnt ? 0 : data.sys_full_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">상담 진행(정상종료)수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_cnt ? 0 : data.cns_end_cnt}"/>
									<span>건</span>
								</em>
							</div>
						</div>
						<div class="id_info_area">	
							
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">자동종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_auto ? 0 : data.cns_end_auto}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">고객종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_cstm ? 0 : data.cns_end_cstm}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">상담원종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_cnsr ? 0 : data.cns_end_cnsr}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box w100">
								<span class="tit">평균 상담 시간</span> <em><span>
									<c:if test="${data.avg_end_date *1 != 0}">
										${data.avg_end_date *1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_end_hour *1 != 0}">
										${data.avg_end_hour *1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_end_min *1 != 0}">
										${data.avg_end_min *1}<span>분</span>
									</c:if>
									${data.avg_end_sec *1}<span>초</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">상담원당 평균 상담 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.avg_end_cnt ? 0 : data.avg_end_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">근무상담원수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cnsr_cnt ? 0 : data.cnsr_cnt}"/>
									<span>명</span>
								</em>
							</div>
						</c:forEach>
							<div class="id_box">
								<span class="tit">카카오톡 인입수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty kakaoDivCnt ? 0 : kakaoDivCnt}" />
									<span>건</span>
								</em>
							</div>
					</div>

					<ul class="tabs_menu_s graph">
						<li class="active" data-page="1">시간별 추이</li>
						<li data-page="2">상담직원 통계</li>
						<!-- <li data-page="3">분류별 통계</li>  -->
						<li data-page="4">검색어 통계</li>
					</ul>
					<!-- 시간별 통계 -->
					<div class="graph_area" id="area1">
						<!-- 왼쪽 테이블 영역 -->
						<div class="left_table_area">
						
							<div class="graph_top_area">
								<button type="button" class="btn_excel_down" id="timeDown">
									<i></i>Excel Export
								</button>
							</div>


							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>상담 시간</th>
											<th>상담 인입</th>
											<th>근무 외</th>
											<th>배정전 포기</th>
											<th>배정후 포기</th>
											<th>바쁜시간 종료</th>
											<th>상담 종료</th>
											<th>평균 상담 시간</th>
											<th>상담원당 <br>평균 상담 수</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty timeList}">
												<tr>
													<td colspan="8">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${timeList}" varStatus="status">
													<tr>
														<td>${data.reg_time}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.OUT_SERVICE_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.CNS_BQUIT_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.CNS_AQUIT_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.sys_full_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/> 건</td>
														<td>
														<c:if test="${data.avg_end_date *1 != 0}">
															${data.avg_end_date *1}<span>일</span>
														</c:if>
														<c:if test="${data.avg_end_hour *1 != 0}">
															${data.avg_end_hour *1}<span>시</span>
														</c:if>
														<c:if test="${data.avg_end_min *1 != 0}">
														${data.avg_end_min *1}<span>분</span>
														</c:if>
														${data.avg_end_sec *1}<span>초</span>
														</td>
														<td>
															<fmt:formatNumber  pattern="#,###.#"  value="${data.avg_per_cnsr}"/> <span>건</span>
															<!-- (${data.avg_evl_end_time}초) -->
														</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
						</div>
						<!--// 왼쪽 테이블 영역 -->

						<!-- 오른쪽 그래프 영역 -->
						<div class="right_graph_area" >
							<h3 class="sub_stit">상담 수</h3>
							<canvas id="canvas_Line1" height="90"></canvas>
						</div>
						<div class="right_graph_area">
							<h3 class="sub_stit">평균 상담시간</h3>
						<canvas id="canvas_Line2" height="90"></canvas>
						</div>
						<div class="right_graph_area">
							<h3 class="sub_stit">상담직원당 평균 상담시간(분)</h3>
						<canvas id="canvas_Line3" height="90"></canvas>
						</div>
						<!--// 오른쪽 그래프 영역 -->
					</div>
					<!--// 시간별 통계 -->

					<!-- 상담직원 통계 -->
					<div class="graph_area" id="area2" style="display:none;">
						
							<h3 class="sub_stit">
								상담직원별 데이터
								<button type="button" class="btn_excel_down floatR" id="downAdviser">
									<i></i>Excel Export
								</button>
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>담당매니저</th>
											<th>상담직원</th>
											<th>상담 진행 수</th>
											<th>총 상담 시간</th>
											<th>상담 후처리 수</th>
											<th>평균 상담 시간</th>
											<!-- <th>평가건수</th>
											<th>평균별점</th> -->
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty adviserList}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${adviserList}" varStatus="status">
													<tr>
														<td><c:if test="${data.manager_name eq null}">-</c:if>${data.manager_name}</td>
														<td>${data.name}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/> 건</td>
														<td>${data.total_use_time}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.after_cnt}"/> 건</td>
														<td>
														<c:if test="${data.avg_end_date *1 != 0}">
															${data.avg_end_date *1}<span>일</span>
														</c:if>
														<c:if test="${data.avg_end_hour *1 != 0}">
															${data.avg_end_hour *1}<span>시</span>
														</c:if>
														<c:if test="${data.avg_end_min *1 != 0}">
														${data.avg_end_min *1}<span>분</span>
														</c:if>
														${data.avg_end_sec *1}<span>초</span>
														</td>
														<%-- <td><fmt:formatNumber  pattern="#,###.#"  value="${data.evl_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_evl_score}"/> <span>점</span> </td> --%>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
						
						<!--// 왼쪽 테이블 영역 -->
					</div>
					<!--// 요일별 통계 -->

					<!-- 분류별 통계 -->
					<!-- 
					<div class="graph_area" id="area3">
					 -->
						<!-- 왼쪽 테이블 영역 -->
					<!--
						<div class="graph_table_area">
							<div class="graph_top_area">
								<h3 class="sub_stit">분류별 데이터
									<button type="button" class="btn_excel_down" id="ctgDown">
										<i></i>Excel Export
									</button>
								</h3>
							</div>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<th>소분류</th>
											<th>상담 수</th>
											<th>상담직원당 평균 상담 수</th>
											<th>평균 상담 시간</th>
											<th>평가 건수</th>
											<th>평균 별점</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty ctgList}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${ctgList}" varStatus="status">
													<tr>
														<td>${data.ctg1}</td>
														<td>${data.ctg2}</td>
														<td>${data.ctg3}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/></td>
														<td>
														<c:if test="${data.avg_end_date *1 != 0}">
															${data.avg_end_date *1}일
														</c:if>
														<c:if test="${data.avg_end_hour *1 != 0}">
															${data.avg_end_hour *1}시
														</c:if>
														<c:if test="${data.avg_end_min *1 != 0}">
															${data.avg_end_min *1}분
														</c:if>
															${data.avg_end_sec *1}초
														</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_cnt}"/></td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>

							</div>
						</div>
						 -->
						<!--// 왼쪽 테이블 영역 -->
					<!-- </div>  -->
					<!-- 분류별 통계 -->

					<!-- 검색어 통계 -->
					<div class="graph_area" id="area4" style="display:none;">
							<h3 class="sub_stit">
								용어검색 개수가 0인 데이터
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>순위</th>
											<th>검색어</th>
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty TermRankDayZList}">
												<tr>
													<td colspan="2">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${TermRankDayZList}" varStatus="status">
													<tr>
														<td>${data.ranking}</td>
														<td>${data.search_text}</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
							
							<h3 class="sub_stit">
								용어검색 개수가 0보다 큰 데이터
							</h3>
							<div class="graph_table_area">
								<table class="tCont ser vice">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>순위</th>
											<th>검색어</th>
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty TermRankDayUList}">
												<tr>
													<td colspan="2">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${TermRankDayUList}" varStatus="status">
													<tr>
														<td>${data.ranking}</td>
														<td>${data.search_text}</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
						
						<!--// 왼쪽 테이블 영역 -->
					</div>
					<!--// 검색어 통계 -->

				</div>
			</div>
		</form>
		<!--// left_area -->
	</div>

	<!-- alret 창 -->
	<div class="layer_alert" style="display:none;">
		<p class="alert_text"></p>
	</div>
	<!--// alret 창 -->

</body>
</html>
