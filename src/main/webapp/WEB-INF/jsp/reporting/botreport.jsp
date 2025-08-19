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
<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/ChartNew.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>
      <script type="text/javascript">
	$(document).ready(function() {
		
			
		$(document).on("click",".btn_holiday_plus",function() {
			if (periodDayCheck()) {
				// 조회 시작
				$("#nowPage").val('');
				goPaging();
			}
		});

		// 이전 클릭
		$(document).on("click","#counselingForm .dev_prev_btn", function() {
			var nowPage = $("#counselingForm [name='nowPage']").val() * 1 - 1;
			var totPage = $("#counselingForm [name='totPage']").val() * 1;
			if (nowPage <= 0) {
				return false;
			}
			$("#counselingForm [name='nowPage']").val(
					nowPage);
			goPaging();
		});

		// 이후 클릭
		$(document).on("click","#counselingForm .dev_next_btn",function() {
			var nowPage = $(	"#counselingForm [name='nowPage']").val() * 1 + 1;
			var totPage = $("#counselingForm [name='totPage']").val() * 1;
			if (nowPage > totPage) {
				return false;
			}
			$("#counselingForm [name='nowPage']").val(nowPage);
			goPaging();
		});

		// 페이지 입력창 키 입력 처리
		$(document).on("keypress","#counselingForm [name='nowPage']",function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				$(
						"#counselingForm [name='nowPage']")
						.trigger("change");
			}
		});

		// 페이지 입력창 입력
		$(document).on("change","#counselingForm [name='nowPage']",function() {
				var nowPage = $("#counselingForm [name='nowPage']").val();
				var inputNowPage = $(this).val() * 1;
				var totPage = $("#counselingForm [name='totPage']").val() * 1;

				if (!$.isNumeric(inputNowPage)) {
					alert("숫자만 입력해 주세요.");
					$("#counselingForm [name='nowPage']").val(nowPage);
					return false;
				}
				if (inputNowPage <= 0) {
					alert("1페이지 부터 입력해 주세요");
					$("#counselingForm [name='nowPage']").val(nowPage);
					return false;
				}

				if (inputNowPage > totPage) {
					alert("최대 페이지가 " + totPage+ "페이지 입니다.");
					$("#counselingForm [name='nowPage']").val(nowPage);
					return false;
				}

				$("#counselingForm [name='nowPage']").val(inputNowPage);
				goPaging();
		});

		// 방문고객추이
		$(document).on("click","#dateDown",function() {
			$("#counselingForm").attr("action", "<c:url value='/reporting/downloadLinkDateExcel' />");
			$("#counselingForm").submit();
		});
		// 블럭당
		$(document).on("click","#blockDown",function() {
			$("#counselingForm").attr("action", "<c:url value='/reporting/downloadBlockExcel' />");
			$("#counselingForm").submit();
		});
		// 봇대화방리스트
		$(document).on("click","#roomDown",function() {
			$("#counselingForm").attr("action", "<c:url value='/reporting/downloadBotRoomExcel' />");
			$("#counselingForm").submit();
		});

		// 팝업 닫기 버튼
		$('body').on('click', '.btn_popup_close', function(e) {
			$('.popup').hide();
		});
	});

	$(document).ready(function() {
		$(document).on("click",".btn_holiday_plus",function() {
			if (periodDayCheck()) {												// 조회 시작
					goPaging();
				}
			});
	});

	function goPaging() {
		$("#counselingForm").attr("action", "<c:url value='/reporting/botreport' />");
		$("#counselingForm").submit();
	}

	function periodDayCheck(){
		var iDay = 1000 * 60 * 60 * 24;
		var startDateArray = $('#startDate').val().split('-');
		var startDateObj = new Date(startDateArray[0],Number(startDateArray[1])-1,startDateArray[2]);
		var endDateArray = $('#endDate').val().split('-');
		var endDateObj = new Date(endDateArray[0],Number(endDateArray[1])-1,endDateArray[2]);

		var betweenDay = (endDateObj.getTime()-startDateObj.getTime()) / 1000 / 60/60/ 24+1;

		if( betweenDay > 91){
			alert('최대 3개월까지 조회 가능합니다.');
			return false;
		}else if(betweenDay <1){
			alert('최소 1일까지 조회 가능합니다.');
			return false;
		}else{
			return true;
		}
	}
	
	
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
			});
			// 일자별 통계
			var dayLabels = new Array(); //일별라벨
			var dayVisitAll = new Array();
			var dayAllKakao = new Array(); 
			var dayNewKakao = new Array(); 
			var dayReKakao = new Array();
			var dayAllWeb = new Array(); 
			var dayNewWeb = new Array(); 
			var dayReWeb = new Array();
			var dayAllo2 = new Array(); 
			var dayNewo2 = new Array(); 
			var dayReo2 = new Array();
			var dayAllMpop = new Array(); 
			var dayNewMpop = new Array(); 
			var dayReMpop = new Array();

			var tmpKakao = 0;
			var tmpo2 = 0;
			var tmpMpop = 0;
			var tmpWeb = 0;
			
			<c:forEach var="data" items="${channelListGraph}" varStatus="status">
			dayLabels[${status.index}] = '${data.day}';
			dayAllWeb[${status.index}] = '${data.web_all_cnt}';
			dayNewWeb[${status.index}] = '${data.web_new_vst_cnt}';
			dayReWeb[${status.index}] = '${data.web_re_vst_cnt}';
			dayAllKakao[${status.index}] = '${data.kak_all_cnt}';
			dayNewKakao[${status.index}] = '${data.kak_new_vst_cnt}';
			dayReKakao[${status.index}] = '${data.kak_re_vst_cnt}';
			dayAllo2[${status.index}] = '${data.o2_all_cnt}';
			dayNewo2[${status.index}] = '${data.o2_new_vst_cnt}';
			dayReo2[${status.index}] = '${data.o2_re_vst_cnt}';
			dayAllMpop[${status.index}] = '${data.mpop_all_cnt}';
			dayNewMpop[${status.index}] = '${data.mpop_new_vst_cnt}';
			dayReMpop[${status.index}] = '${data.mpop_re_vst_cnt}';
		</c:forEach>
			var dateCountlabel = new Array();
			<c:forEach var="data" items="${weekList}" varStatus="status">
				dateCountlabel[${status.index}] = '${data.day}';
				weekAvgEnd[${status.index}] = '${data.avg_end_time}' * 4;
			</c:forEach>


			var data1 ={
				labels : dayLabels,
				datasets : [
					{
						fillColor           : "rgba(255,200,100,0)",
						strokeColor         : "rgba(255,200,100,1)",
						pointColor          : "rgba(255,200,100,1)",
						pointStrokeColor    : "rgba(255,200,100,1)",
						pointHighlightFill  : "rgba(255,200,100,1)",
						pointHighlightStroke: "rgba(255,255,255,1)",
						data : dayAllKakao
					},
					{
						fillColor           : "rgba(100,180,255,0)",
						strokeColor         : "rgba(100,180,255,1)",
						pointColor          : "rgba(100,180,255,1)",
						pointStrokeColor    : "rgba(100,180,255,1)",
						pointHighlightFill  : "rgba(100,180,255,1)",
						pointHighlightStroke: "rgba(255,255,255,1)",
						data : dayAllo2
					},
					{
						fillColor           : "rgba(255,130,110,0)",
						strokeColor         : "rgba(255,130,110,1)",
						pointColor          : "rgba(255,130,110,1)",
						pointStrokeColor    : "rgba(255,130,110,1)",
						pointHighlightFill  : "rgba(255,130,110,1)",
						pointHighlightStroke: "rgba(255,255,255,1)",
						data : dayAllWeb
					},
					{
						fillColor           : "rgba(180,120,210,0)",
						strokeColor         : "rgba(180,120,210,1)",
						pointColor          : "rgba(180,120,210,1)",
						pointStrokeColor    : "rgba(180,120,210,1)",
						pointHighlightFill  : "rgba(180,120,210,1)",
						pointHighlightStroke: "rgba(255,255,255,1)",
						data : dayAllMpop
					}
				],
				xPos : ["1","2","3","4"]
			};

			var data2 ={
					labels : dayLabels,
					datasets : [
						{
							fillColor           : "rgba(255,200,100,0)",
							strokeColor         : "rgba(255,200,100,1)",
							pointColor          : "rgba(255,200,100,1)",
							pointStrokeColor    : "rgba(255,200,100,1)",
							pointHighlightFill  : "rgba(255,200,100,1)",
							pointHighlightStroke: "rgba(255,255,255,1)",
							data : dayNewKakao,
							title : "카카오톡"
						},
						{
							fillColor           : "rgba(100,180,255,0)",    
							strokeColor         : "rgba(100,180,255,1)",    
							pointColor          : "rgba(100,180,255,1)",    
							pointStrokeColor    : "rgba(100,180,255,1)",    
							pointHighlightFill  : "rgba(100,180,255,1)",    
							pointHighlightStroke: "rgba(255,255,255,1)",  
							data : dayNewo2,
							title : "O2톡"
						},
						{
							fillColor           : "rgba(255,130,110,0)",       
							strokeColor         : "rgba(255,130,110,1)",       
							pointColor          : "rgba(255,130,110,1)",       
							pointStrokeColor    : "rgba(255,130,110,1)",       
							pointHighlightFill  : "rgba(255,130,110,1)",       
							pointHighlightStroke: "rgba(255,255,255,1)",       
							data : dayNewWeb,
							title : "웹채팅"
						},
						{
							fillColor           : "rgba(180,120,210,0)",           
							strokeColor         : "rgba(180,120,210,1)",           
							pointColor          : "rgba(180,120,210,1)",           
							pointStrokeColor    : "rgba(180,120,210,1)",           
							pointHighlightFill  : "rgba(180,120,210,1)",           
							pointHighlightStroke: "rgba(255,255,255,1)",           
							data : dayNewMpop,
							title : "mPop"
						}
					],
					xPos : ["1","2","3","4"]
				};

			var data3 ={
					labels : dayLabels,
					datasets : [
						{
							fillColor           : "rgba(255,200,100,0)",         
							strokeColor         : "rgba(255,200,100,1)",         
							pointColor          : "rgba(255,200,100,1)",         
							pointStrokeColor    : "rgba(255,200,100,1)",         
							pointHighlightFill  : "rgba(255,200,100,1)",         
							pointHighlightStroke: "rgba(255,255,255,1)",         
							data : dayReKakao,
							title : "카카오톡"
						},
						{
							fillColor           : "rgba(100,180,255,0)",               
							strokeColor         : "rgba(100,180,255,1)",               
							pointColor          : "rgba(100,180,255,1)",               
							pointStrokeColor    : "rgba(100,180,255,1)",               
							pointHighlightFill  : "rgba(100,180,255,1)",               
							pointHighlightStroke: "rgba(255,255,255,1)",             
							data : dayReo2,                                          
							title : "O2톡"
						},
						{
							fillColor           : "rgba(255,130,110,0)",               
							strokeColor         : "rgba(255,130,110,1)",               
							pointColor          : "rgba(255,130,110,1)",               
							pointStrokeColor    : "rgba(255,130,110,1)",               
							pointHighlightFill  : "rgba(255,130,110,1)",               
							pointHighlightStroke: "rgba(255,255,255,1)",               
							data : dayReWeb,                                           
							title : "웹채팅"
						},
						{
							fillColor           : "rgba(180,120,210,0)",           
							strokeColor         : "rgba(180,120,210,1)",           
							pointColor          : "rgba(180,120,210,1)",           
							pointStrokeColor    : "rgba(180,120,210,1)",           
							pointHighlightFill  : "rgba(180,120,210,1)",           
							pointHighlightStroke: "rgba(255,255,255,1)",           
							data : dayReMpop,
							title : "mPop"
						}
					],
					xPos : ["1","2","3","4"]
				};			
			var option1 = {
					responsive: true,
					// legend
					legend : true,
					datasetFill: true,
					showSingleLegend: true,
					legendBorders: false,
					// annotate
					annotateDisplay: true,
					annotateRelocate: true,
					detectAnnotateOnFullLine: false,
					annotateFontSize: 10,
					annotateBorderRadius: '0px',
					annotatePadding: "10px",
					annotateBackgroundColor: 'rgba(0,0,0,0.7)'

			};
			window.onload = function(){
				var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
				var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
				var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);
				if($("#nowPage").val() > 1){
					 $('li').removeClass('active');
					 $("#tabArea2").addClass('active');
					 $('.graph_area').css('display','none');
					$("#area1").hide();
					 $("#area2").show();
					 
				} 
				$('#area2').css('display','none');
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
		<form id="counselingForm" name="dailyForm" method="post" action="">
			<div class="left_content_area">
				<div class="left_content_head">
					<h2 class="sub_tit">BOT Reporting</h2>
					<span class="left_top_text">선택된 기간의 상담내역을 보여줍니다.</span>
					
				</div>
				<div class="inner">
					<div class="top_filter_area">
						<div class="search_area" style="width:70%">
							<div>
								<span class="dot_tit">기간</span> 
								<!--<input type="radio" name="searchType" id="searchTypeD" value="D" class="radio_18" ${searchTypeD}><label for="searchTypeD"><span></span>일별</label>	
								<input type="radio" name="searchType" id="searchTypeM" value="M" class="radio_18" ${searchTypeM}><label for="searchTypeM"><span></span>월별</label>	-->
								&nbsp;&nbsp;
								<input type="text" class="datepicker" name="startDate" id="startDate" value="${startDate}">
								~ 
								<input type="text" class="datepicker" name="endDate" id="endDate" value="${endDate}"> 
							</div>							
							<div style="padding:10px 0px;">
								<span class="dot_tit">채널</span> 
								<%-- <input type="checkbox" name="cstm_link_cd" value="A" id="cstm_link_div_cd_w" class="checkbox_18" ${linkCdW} ><label for="cstm_link_div_cd_w" ><span></span>웹채팅</label> --%>
								<input type="checkbox" name="cstm_link_cd" value="C" id="cstm_link_div_cd_n" class="checkbox_18" ${linkCdN}><label for="cstm_link_div_cd_n" ><span></span>O2</label>
								<input type="checkbox" name="cstm_link_cd" value="B" id="cstm_link_div_cd_k" class="checkbox_18" ${linkCdK}><label for="cstm_link_div_cd_k" ><span></span>카카오</label>
							</div>							
							<div style="padding:10px 100px;">
								<span >&nbsp;&nbsp;</span> <button type="button" class="btn_holiday_plus" id="btn_search_date" style="width:300px;">검색</button>
							</div>							
						</div>
					</div>
					
					<div class="id_info_area">

						<c:forEach var="data" items="${selectBotBasicCnt}" varStatus="status">
							<div class="id_box">
								<span class="tit">챗봇 인입 건수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.bot_all_cnt}"/>
									건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">챗봇 사용 건수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.bot_use_cnt}"/>
									건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">챗봇 방문자 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.bot_cstm_cnt}"/>
									명</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">챗봇 신규 방문자 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.newvisit_cnt}"/>
									명</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">챗봇 재 방문자 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.revisit_cnt}"/>
									명</span>
								</em>
							</div>
							
							<div class="id_box w100">
								<span class="tit">평균 챗봇 사용시간</span> <em>
										<span>${data.els_avg_time}</span>
								</em>
							</div>
							</c:forEach>
						</div>
						<div class="id_info_area">
							<c:forEach var="data" items="${selectBotChannelCnt}" varStatus="status">
							<div class="id_box w100">
								<span class="tit">검색기간 내 전체 방문자 수</span>
								<em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.all_cnt}"/></span>
									<span>명</span>
								</em>
							</div>
							<div class="id_box w100">
								<span class="tit" style="background-color:#ddd">검색기간 내 카카오톡 방문자 수</span>
								<em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.kak_cnt}"/>
									명</span>
								</em>
							</div>
							<div class="id_box w100">
								<span class="tit" style="background-color:#ddd">검색기간 내 O2톡 방문자 수</span>
								<em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.nav_cnt}"/>
									명</span>
								</em>
							</div>
							</c:forEach>
					</div>
					<%--<div class="id_info_area" >
						<div class="left_table_area">
							<!-- <h3 class="sub_stit" style="height:32px;">
								Chat-bot 개인화(기간계) 서비스 사용건수
							</h3> -->
							 <div class="graph_table_area" style="overflow-y:auto;overflow-x:none;height:180px;">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th colspan="2">서비스명</th>
											<th>사용 건수</th>
											<th>사용 고객 수</th>
											<th>평균 사용 건수 / 1인</th>
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty mciList}">
												<tr>
													<td colspan="5">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${mciList}" varStatus="status">
													<tr>
														<c:if test="${data.auth_type_name == null }"><td  colspan="2" style="background-color:#eee;">${data.service_name}
															<c:if test="${data.from_home != null }">(${data.from_home })</c:if>
														</td></c:if>
														<c:if test="${data.auth_type_name != null }">
														<td  style="background-color:#eee;">${data.service_name}</td>
														<td  style="background-color:#eee;">${data.auth_type_name}
															<c:if test="${data.from_home != null }">(${data.from_home })</c:if>
														</td>
														</c:if>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.total_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cstm_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_cnt}"/> 건</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div
						</div>
						<div class="right_graph_area">
						
						</div>> 
					</div> --%>
					<ul class="tabs_menu_s graph">
						<li class="active" data-page="1">방문 고객 추이</li>
						<li data-page="2">블럭당 사용건수 </li>
					</ul>
					<div class="graph_area" id="area1">
						<!-- 왼쪽 테이블 영역 -->
						<div class="left_table_area">
							<h3 class="sub_stit">통계데이터
								<button type="button" class="btn_excel_down floatR" id="dateDown">
									<i></i>Excel Export
								</button>
								</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
										<col>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th colspan="2">방문일(챗봇시작일)</th>
											<th>총 방문자 수</th>
											<th>신규 방문자 수</th>
											<th>재 방문자 수</th>
											<th>챗봇 사용건수</th>
											<th>평균 사용시간</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty channelList}">
												<tr>
													<td colspan="7">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:set var="sum_web_all" value="0"></c:set>
												<c:set var="sum_web_new" value="0"></c:set>
												<c:set var="sum_web_re" value="0"></c:set>
												<c:set var="sum_web_use" value="0"></c:set>
												<c:set var="sum_web_esp" value="0"></c:set>
												<c:set var="sum_kak_all" value="0"></c:set>
												<c:set var="sum_kak_new" value="0"></c:set>
												<c:set var="sum_kak_re" value="0"></c:set>
												<c:set var="sum_kak_use" value="0"></c:set>
												<c:set var="sum_kak_esp" value="0"></c:set>
												<c:set var="sum_o2_all" value="0"></c:set>
												<c:set var="sum_o2_new" value="0"></c:set>
												<c:set var="sum_o2_re" value="0"></c:set>
												<c:set var="sum_o2_use" value="0"></c:set>
												<c:set var="sum_o2_esp" value="0"></c:set>
												<c:set var="sum_tot_esp" value="0"></c:set>
												<c:set var="sum_mpop_all" value="0"></c:set>
												<c:set var="sum_mpop_new" value="0"></c:set>
												<c:set var="sum_mpop_re" value="0"></c:set>
												<c:set var="sum_mpop_use" value="0"></c:set>
												<c:set var="sum_mpop_esp" value="0"></c:set>
												<c:forEach var="data" items="${channelList}" varStatus="status">
														<c:if test="${status.index == '0'}">
															<c:set var="frst_date" value="${data.reg_date }"></c:set>
														</c:if>
														<c:set var="last_date" value="${data.reg_date }"></c:set>
														<c:if test="${data.link_div_nm == '웹' && linkCdW == ' checked'}">
															<c:set var="sum_web_all" value="${sum_web_all + data.all_cnt }"></c:set>
															<c:set var="sum_web_new" value="${sum_web_new + data.new_visit_cnt }"></c:set>
															<c:set var="sum_web_re" value="${sum_web_re + data.re_visit_cnt }"></c:set>
															<c:set var="sum_web_use" value="${sum_web_use + data.chatbot_cnt }"></c:set>
															<c:set var="sum_web_esp" value="${sum_web_esp + data.elapse_time }"></c:set>
														</c:if>
														<c:if test="${data.link_div_nm == '카톡' && linkCdK == ' checked'}">
															<c:set var="sum_kak_all" value="${sum_kak_all + data.all_cnt }"></c:set>
															<c:set var="sum_kak_new" value="${sum_kak_new + data.new_visit_cnt }"></c:set>
															<c:set var="sum_kak_re" value="${sum_kak_re + data.re_visit_cnt }"></c:set>
															<c:set var="sum_kak_use" value="${sum_kak_use + data.chatbot_cnt }"></c:set>
															<c:set var="sum_kak_esp" value="${sum_kak_esp + data.elapse_time }"></c:set>
														</c:if>
														<c:if test="${data.link_div_nm == 'O2' && linkCdN == ' checked'}">
															<c:set var="sum_o2_all" value="${sum_o2_all + data.all_cnt }"></c:set>
															<c:set var="sum_o2_new" value="${sum_o2_new + data.new_visit_cnt }"></c:set>
															<c:set var="sum_o2_re" value="${sum_o2_re + data.re_visit_cnt }"></c:set>
															<c:set var="sum_o2_use" value="${sum_o2_use + data.chatbot_cnt }"></c:set>
															<c:set var="sum_o2_esp" value="${sum_o2_esp + data.elapse_time }"></c:set>
														</c:if>
														<c:if test="${data.link_div_nm == 'mPOP' && linkCdK == ' checked'}">
															<c:set var="sum_mpop_all" value="${sum_mpop_all + data.all_cnt }"></c:set>
															<c:set var="sum_mpop_new" value="${sum_mpop_new + data.new_visit_cnt }"></c:set>
															<c:set var="sum_mpop_re" value="${sum_mpop_re + data.re_visit_cnt }"></c:set>
															<c:set var="sum_mpop_use" value="${sum_mpop_use + data.chatbot_cnt }"></c:set>
															<c:set var="sum_mpop_esp" value="${sum_mpop_esp + data.elapse_time }"></c:set>
														</c:if>
													<tr>
														<c:if test="${status.index % spanNum == '0'}">
															<td rowspan="${spanNum}">${data.reg_date}</td>
														</c:if>
														
														<td>${data.link_div_nm}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.all_cnt}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.new_visit_cnt}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.re_visit_cnt}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.chatbot_cnt}"/> 건</td>
														<td>
														
														<c:if test="${data.elapse_hour  * 1 != 0}">
															${data.elapse_hour * 1}<span>시</span>
														</c:if>
														<c:if test="${data.elapse_min * 1 != 0 }">
														${data.elapse_min * 1}<span>분</span>
														</c:if>
														${data.elapse_sec * 1}<span>초</span>
														</td>														
													</tr>

												</c:forEach>
													<tr>
														<td rowspan="4">${frst_date} <br> ~ <br>${last_date}</td>
														<td>웹</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_all}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_new}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_re}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_use}"/> 건</td>
														<td id="sum_web_esp"></td>
													</tr>
													<tr>
														<td>카톡</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_kak_all}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_kak_new}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_kak_re}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_kak_use}"/> 건</td>
														<td id="sum_kak_esp"></td>														
													</tr>
													<tr>
														<td>O2</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_o2_all}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_o2_new}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_o2_re}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_o2_use}"/> 건</td>
														<td id="sum_o2_esp"></td>														
													</tr>
													<tr>
														<td>mPop</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_mpop_all}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_mpop_new}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_mpop_re}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_mpop_use}"/> 건</td>
														<td id="sum_mpop_esp"></td>														
													</tr>
													
													<tr>
														<td colspan="2">전체</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_all + sum_kak_all + sum_o2_all + sum_mpop_all}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_new + sum_kak_new + sum_o2_new + sum_mpop_new}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_re + sum_kak_re + sum_o2_re + sum_mpop_re}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${sum_web_use + sum_kak_use + sum_o2_use + sum_mpop_use}"/> 건</td>
														<td id="sum_tot_esp"></td>														
													</tr>
											</c:otherwise>
										</c:choose>
										
									</tbody>
								</table>
							</div>
						</div>
						<!--// 왼쪽 테이블 영역 -->

						<!-- 오른쪽 그래프 영역
						<div class="right_graph_area" >
							<h3 class="sub_stit">참조</h3>
							<div>
								1.총 개수
							</div>
						</div> -->
						<div class="right_graph_area" >
							<h3 class="sub_stit">총 방문자 추이</h3>
							<canvas id="canvas_Line1" height="90"></canvas>
						</div>
						<div class="right_graph_area">
							<h3 class="sub_stit">신규 방문자 추이</h3>
							<canvas id="canvas_Line2" height="90"></canvas>
						</div>
						<div class="right_graph_area">
							<h3 class="sub_stit">재 방문자 추이</h3>
							<canvas id="canvas_Line3" height="90"></canvas>
						</div>
						<!--// 오른쪽 그래프 영역 -->
					</div>
					<!--// 일자별 통계 -->
					<div class="graph_area" id="area2">
						<h3 class="sub_stit">
								챗봇 블럭당 사용건수 데이터(오픈빌더 제외)
								<button type="button" class="btn_excel_down floatR" id="blockDown">
									<i></i>Excel Export
								</button>
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th>블럭명</th>
											<th>호출건수</th>
											<th>사용 비율</th>
											<th>시작 건수</th>
											<th>상담직원연결수</th>
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty blockList}">
												<tr>
													<td colspan="5">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${blockList}" varStatus="status">
													<tr id="adviserTr" class="adviserTr" >
														<td>${data.name}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.block_cnt}"/> 건</td>
														<td>
															<fmt:formatNumber  pattern="#,###.##" var="block_cnt" value="${data.block_cnt}"/>
															
															 <c:choose>
																<c:when test="${block_cnt > 0 && sumBlockCnt > 0}">
																	<fmt:formatNumber  pattern="0.0%"  value="${block_cnt / sumBlockCnt}" type="percent" /></td>
																</c:when>
																<c:otherwise> 0%</c:otherwise>
															</c:choose>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.str_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.req_cnt}"/> 건</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
					</div>
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

	<!-- popup:상담내역 -->
	<div class="popup popup_counseling" style="display: none">
	</div>
	<!--// popup:상담내역 -->
<script type="text/javascript">
String.prototype.toDDHHMMSS = function (){
	var thisNum	= parseInt(this, 10);
	var days			= Math.floor(thisNum / 86400);
	var hours		= Math.floor((thisNum -(days * 86400)) / 3600);
	var minutes		= Math.floor((thisNum - (days * 86400) - (hours * 3600) ) / 60);
	var seconds		= thisNum - (days * 86400) - (hours * 3600) - (minutes * 60);
	var dtFormat	= "";



	if(days*1 > 0) { dtFormat += days + "<span>일</span> ";}
	if(hours*1 > 0) { dtFormat += hours + "<span>시간</span> ";}
	if(minutes*1 > 0) { dtFormat += minutes + "<span>분</span> ";}
	dtFormat += seconds + "<span>초</span> ";
	return dtFormat;
}
$(document).ready( function() {
	var sum_web_esp = '${sum_web_esp}';
	var sum_kak_esp = '${sum_kak_esp }';
	var sum_o2_esp = '${sum_o2_esp }';
	var sum_mpop_esp = '${sum_mpop_esp }';
	if(sum_web_esp == null) sum_web_esp = 0;
	if(sum_kak_esp == null) sum_kak_esp = 0;
	if(sum_o2_esp == null) sum_o2_esp = 0;
	if(sum_mpop_esp == null) sum_mpop_esp = 0;
	
	var sum_tot_esp = String(Number(sum_web_esp) + Number(sum_kak_esp) + Number(sum_o2_esp) + Number(sum_mpop_esp));
	
	$("#sum_web_esp").html(sum_web_esp.toDDHHMMSS());
	$("#sum_kak_esp").html(sum_kak_esp.toDDHHMMSS());
	$("#sum_o2_esp").html(sum_o2_esp.toDDHHMMSS());
	$("#sum_mpop_esp").html(sum_mpop_esp.toDDHHMMSS());
	$("#sum_tot_esp").html(sum_tot_esp.toDDHHMMSS());
});
</script>
</body>
</html>
