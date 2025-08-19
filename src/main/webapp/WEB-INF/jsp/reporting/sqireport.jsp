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
	$(document).ready(function() {
		$(document).on( "click", ".btn_holiday_plus", function() {
			if (periodDayCheck()) {
				goPaging();
			}
		});

		// 이전 클릭
		$(document).on( "click", "#counselingForm .dev_prev_btn", function() {
			var nowPage = $("#counselingForm [name='nowPage']").val() * 1 - 1;
			var totPage = $( "#counselingForm [name='totPage']").val() * 1;
			if (nowPage <= 0) {
				return false;
			}
			$("#counselingForm [name='nowPage']").val(nowPage);
			goPaging();
		});

		// 이후 클릭
		$(document).on("click","#counselingForm .dev_next_btn",function() {
			var nowPage = $("#counselingForm [name='nowPage']").val() * 1 + 1;
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
				$("#counselingForm [name='nowPage']").trigger("change");
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
				alert("최대 페이지가 " + totPage + "페이지 입니다.");
				$("#counselingForm [name='nowPage']").val(nowPage);
				return false;
			}

			$("#counselingForm [name='nowPage']").val(inputNowPage);
			goPaging();
		});

		// 이전 클릭
		$(document).on("click","#excelDown",function() {
			$("#counselingForm").attr("action", "<c:url value='/reporting/downloadSelectSqiAllExcel' />");
			$("#counselingForm").submit();
		});

	});

	function goPaging() {
		var ctg1 = $("#ctg1 option:selected").val();
		var ctg2 = $("#ctg2 option:selected").val();
		if(ctg1 != ''){
			if(ctg2 == ''){
				alert('중분류를 선택하여 주십시오.');
				return false;
			}
		}
		$("#counselingForm").attr("action", "<c:url value='/reporting/sqireport' />");
		$("#counselingForm").submit();
	}

	function periodDayCheck(){
		var iDay = 1000 * 60 * 60 * 24;
		var startDateArray = $('#startDate').val().split('-');
		var startDateObj = new Date(startDateArray[0],Number(startDateArray[1])-1,startDateArray[2]);
		var endDateArray = $('#endDate').val().split('-');
		var endDateObj = new Date(endDateArray[0],Number(endDateArray[1])-1,endDateArray[2]);
		$('#diffDay').val(betweenDay);
		var betweenDay = (endDateObj.getTime()-startDateObj.getTime()) / 1000 / 60/60/ 24+1;

		if( betweenDay > 31){
			alert('최대 30일까지 조회 가능합니다.');
			return false;
		}else if(betweenDay <1){
			alert('최소 1일까지 조회 가능합니다.');
			return false;
		}else{
			return true;
		}


	}


	$(document).on( "change", "#ctg1", function() {
		var ctgNum = $("#ctg1 option:selected").val();
		var step = 2;
		$.ajax({
			url : "<c:url value='/category/selectCategoryAjax' />",
			data : {"ctgNum" : ctgNum},
			type : "post",
			success : function(result) {
				var li = '<option value="">중분류 전체</option>';
				$.each(result, function(idx, item){
					li += '<option value="'+item.ctg_num+'">'+item.ctg_nm+'</option>';
				});
				$("#ctg2").html(li);
			},
			complete : function() {
			}
		});
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
		<form id="counselingForm" name="dailyForm" method="post" action="">
			<div class="left_content_area">
				<div class="left_content_head">
					<h2 class="sub_tit">상담품질지표 - SQI<%--(${sessionVo.departCd})--%></h2>
					<span class="left_top_text">선택된 기간의 상담내역을 보여줍니다.</span>
				</div>
				<div class="inner">
					<div class="top_filter_area">
						<div class="search_area">
								<span class="dot_tit" style="width:80px">기간</span><input type="text" class="datepicker" name="startDate" id="startDate" value="${startDate}">
								~ 
								<input type="text" class="datepicker" name="endDate" id="endDate" value="${endDate}"> 
								<input type="hidden" name="totPage" id="totPage" value="${totPage }" />
								<input type="hidden" name="diffDay" id="diffDay" value="${diffDays }" />
						</div>
						
					</div>
					<%-- <div class="top_filter_area">
							<span class="dot_tit" style="width:80px">분류</span>
							<select class='line' name="ctg1" id="ctg1">
								<option value="">대분류 전체</option>
								<c:forEach var="data" items="${categoryList }" varStatus="status">
									<option value="${data.ctg_num }"
									<c:if test="${data.ctg_num == ctg1}"> selected</c:if>
									>${data.ctg_nm }</option>
								</c:forEach>
							</select>
							<select class='line' name="ctg2" id="ctg2">
								<option value="">중분류 전체</option>
								<c:forEach var="data" items="${categoryList2 }" varStatus="status">
									<option value="${data.ctg_num }"
										<c:if test="${data.ctg_num == ctg2}"> selected</c:if>
									>${data.ctg_nm }</option>
								</c:forEach>
							</select>
					</div> --%>
					<div class="top_filter_area">
						<button type="button" class="btn_holiday_plus" style="width:500px;">검색</button>
					</div>
					<div class="id_info_area">

						<c:forEach var="data" items="${basicCnt}" varStatus="status">
							<div class="id_box w100">
								<span class="tit">배정대기 시간</span> 
								<center>
									<em><span>
									<c:if test="${data.avg_assign_wait_date * 1 != 0}">
										${data.avg_assign_wait_date * 1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_assign_wait_hour * 1 != 0}">
										${data.avg_assign_wait_hour * 1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_assign_wait_min * 1 != 0}">
									${data.avg_assign_wait_min * 1}<span>분</span>
									</c:if>
									${data.avg_assign_wait_sec * 1}<span>초</span>
								</em>
									<span style="font-size:13px;">(
										<c:if test="${data.cmp_ass_w_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_w_time * (-1)}"/></span></c:if>
										<c:if test="${data.cmp_ass_w_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_w_time}"/></span></c:if>
										<c:if test="${data.cmp_ass_w_time == 0 }"><span style="color:black;">0</c:if> 
									%
									)</span>
								</center>
							</div>
							<div class="id_box">
								<span class="tit">포기율</span> 
								<center>
								<em><span>
										${data.total_quit_per} 
									%</span>
								</em>
								<span style="font-size:13px;">(
									<c:if test="${data.cmp_total_quit_per < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_total_quit_per * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_total_quit_per > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_total_quit_per}"/></span></c:if>
									<c:if test="${data.cmp_total_quit_per == 0 }"><span style="color:black;">0</span></c:if> 
								%
								
								)</span></center>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">배정 전 포기율</span>
								<em><span>${data.bfr_quit_per}
									<span>%</span>
								</em>
								<center>
								<span style="font-size:13px;">(
									<c:if test="${data.cmp_ass_bfr_cnt < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_bfr_cnt * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_ass_bfr_cnt > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_bfr_cnt}"/></span></c:if>
									<c:if test="${data.cmp_ass_bfr_cnt == 0 }"><span style="color:black;">0</span></c:if>  
								%)</span></center>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">배정 후 포기율</span>
								<center>
								 <em><span>${data.aft_quit_per}
									<span>%</span>
								</em>
								<span style="font-size:13px;">(
									<c:if test="${data.cmp_ass_aft_cnt < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_aft_cnt * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_ass_aft_cnt > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_aft_cnt}"/></span></c:if>
									<c:if test="${data.cmp_ass_aft_cnt == 0 }"><span style="color:black;">0</span></c:if>  
								%)</span></center>
							</div>
						</div>
						<div class="id_info_area">
							<div class="id_box w100">
								<span class="tit">배정 후 최초 응대 시간</span> 
								<center>
								<em><span>
								<c:if test="${data.avg_assign_fst_date * 1 != 0}">
										${data.avg_assign_fst_date * 1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_assign_fst_hour * 1 != 0}">
										${data.avg_assign_fst_hour * 1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_assign_fst_min * 1 != 0}">
									${data.avg_assign_fst_min * 1}<span>분</span>
									</c:if>
									${data.avg_assign_fst_sec * 1}<span>초</span>
								</em>
								<span style="font-size:13px;">( 
									<c:if test="${data.cmp_ass_frt_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_frt_time * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_ass_frt_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_ass_frt_time}"/></span></c:if>
									<c:if test="${data.cmp_ass_frt_time == 0 }"><span style="color:black;">0</span></c:if> 
								%)</span></center>
							</div>
							<div class="id_box  w100">
								<span class="tit">대화 중 고객 대기 시간</span> 
								<center>
								<em><span>
									<c:if test="${data.avg_cstm_wait_date * 1 != 0}">
										${data.avg_cstm_wait_date * 1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_cstm_wait_hour * 1 != 0}">
										${data.avg_cstm_wait_hour * 1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_cstm_wait_min * 1 != 0}">
									${data.avg_cstm_wait_min * 1}<span>분</span>
									</c:if>
									${data.avg_cstm_wait_sec * 1}<span>초</span>
								</em>
								<span style="font-size:13px;">( 
									<c:if test="${data.cmp_cstm_w_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_cstm_w_time * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_cstm_w_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_cstm_w_time}"/></span></c:if>
									<c:if test="${data.cmp_cstm_w_time == 0 }"><span style="color:black;">0</span></c:if> 
								%)</span></center>
							</div>
							<div class="id_box w100">
								<span class="tit">대화 중 상담원 대기 시간</span> 
								<center>
								<em><span>
								<c:if test="${data.avg_cnsr_wait_date * 1 != 0}">
										${data.avg_cnsr_wait_date * 1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_cnsr_wait_hour * 1 != 0}">
										${data.avg_cnsr_wait_hour * 1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_cnsr_wait_min * 1 != 0}">
									${data.avg_cnsr_wait_min * 1}<span>분</span>
									</c:if>
									${data.avg_cnsr_wait_sec * 1}<span>초</span>
								</em>
								<span style="font-size:13px;">( 
									<c:if test="${data.cmp_cnsr_w_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_cnsr_w_time * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_cnsr_w_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_cnsr_w_time}"/></span></c:if>
									<c:if test="${data.cmp_cnsr_w_time == 0 }"><span style="color:black;">0</span></c:if> 
								%)</span></center>
							</div>
							<div class="id_box w100">
								<span class="tit">평균 대화 시간</span>
								<center>
								<em><span>
									<c:if test="${data.avg_chat_tot_date * 1 != 0}">
										${data.avg_chat_tot_date * 1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_chat_tot_hour * 1 != 0}">
										${data.avg_chat_tot_hour * 1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_chat_tot_min * 1 != 0}">
									${data.avg_chat_tot_min * 1}<span>분</span>
									</c:if>
									${data.avg_chat_tot_sec * 1}<span>초</span>
								</em>
								<span style="font-size:13px;">( 
									<c:if test="${data.cmp_chat_tot_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_chat_tot_time * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_chat_tot_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_chat_tot_time}"/></span></c:if>
									<c:if test="${data.cmp_chat_tot_time == 0 }"><span style="color:black;">0</span></c:if> 
								%)</span></center>
							</div>
							<div class="id_box w100">
								<span class="tit">상담 후처리 시간</span> 
								<center>
								<em><span>
									<c:if test="${data.avg_after_date * 1 != 0}">
										${data.avg_after_date * 1}<span>일</span>
									</c:if>
									<c:if test="${data.avg_after_hour * 1 != 0}">
										${data.avg_after_hour * 1}<span>시</span>
									</c:if>
									<c:if test="${data.avg_after_min * 1 != 0}">
									${data.avg_after_min * 1}<span>분</span>
									</c:if>
									${data.avg_after_sec * 1}<span>초</span>
								</em>
								<span style="font-size:13px;">( 
									<c:if test="${data.cmp_after_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_after_time * (-1)}"/></span></c:if>
									<c:if test="${data.cmp_after_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_after_time}"/></span></c:if>
									<c:if test="${data.cmp_after_time == 0 }"><span style="color:black;">0</span></c:if> 
								%)</span></center>
							</div>
							
						</c:forEach>
					</div>

					<div class="graph_area">
						<div class="report_table_top">
							<div class="left" style="margin-right:15px;"><h3 class="sub_stit">상담원 별 품질 지표
							<button type="button" class="btn_excel_down floatR"
								id="excelDown">
								<i></i>Excel Export
							</button>
							</h3></div>
						</div>
						<div class="" style="min-height:100px;">
							<table class="tCont service" style="width:100%;">
							
								<colgroup>
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<!-- 상담상태 -->
								</colgroup>
								<thead>
									<tr>
										<th rowspan="2">담당 매니저</th>
										<th rowspan="2">상담원</th>
										<th colspan="2">상담 종료 건수</th>
										<th colspan="2">배정 후 최초응대</th>
										<th colspan="2">대화중 고객대기</th>
										<th colspan="2">대화중 상담원대기</th>
										<th colspan="2">평균대화시간(상담시간의 총합 / 상담건수)</th>
										<th colspan="2">상담후처리시간(후처리누락건 제외)</th>
									</tr>
									
									<tr>
										<th>검색기간 내</th>
										<th>전 기간 대비</th>
										<th>검색기간 내</th>
										<th>전 기간 대비</th>
										<th>검색기간 내</th>
										<th>전 기간 대비</th>
										<th>검색기간 내</th>
										<th>전 기간 대비</th>
										<th>검색기간 내</th>
										<th>전 기간 대비</th>
										<th>검색기간 내</th>
										<th>전 기간 대비</th>
									</tr>
								</thead>
								<tbody>
								<c:choose>
											<c:when test="${empty sqiList}">
												<tr>
													<td colspan="16">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
									<c:forEach var="data" items="${sqiList}"	varStatus="status">
										<tr>
											<td>${data.manager_nm}</td>
											<td>${data.member_nm}</td>
											<td>${data.s_cnsr_end_cnt} 건</td>
											<td>
												<c:if test="${data.cmp_end_cnt < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_end_cnt * (-1)}"/></c:if>
												<c:if test="${data.cmp_end_cnt > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_end_cnt}"/></c:if>
												<c:if test="${data.cmp_end_cnt == 0 }"><span style="color:black;">0</c:if>
											%</td>
											<td>${data.s_assign_fst_time_hour} : ${data.s_assign_fst_time_min} : ${data.s_assign_fst_time_sec}</td>
											<td>
												<c:if test="${data.cmp_s_assign_fst_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_assign_fst_time * (-1)}"/></c:if>
												<c:if test="${data.cmp_s_assign_fst_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_assign_fst_time}"/></c:if>
												<c:if test="${data.cmp_s_assign_fst_time == 0 }"><span style="color:black;">0</c:if>
											 %</span>
											</td>
											<td>${data.s_cstm_wait_time_hour} : ${data.s_cstm_wait_time_min} : ${data.s_cstm_wait_time_sec}</td>
											<td>
												<c:if test="${data.cmp_s_cstm_wait_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_cstm_wait_time * (-1)}"/></c:if>
												<c:if test="${data.cmp_s_cstm_wait_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_cstm_wait_time}"/></c:if>
												<c:if test="${data.cmp_s_cstm_wait_time == 0 }"><span style="color:black;">0</c:if>
											%</td>
											<td>${data.s_cnsr_wait_time_hour} : ${data.s_cnsr_wait_time_min} : ${data.s_cnsr_wait_time_sec}</td>
											<td>
												<c:if test="${data.cmp_s_cnsr_wait_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_cnsr_wait_time * (-1)}"/></c:if>
												<c:if test="${data.cmp_s_cnsr_wait_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_cnsr_wait_time}"/></c:if>
												<c:if test="${data.cmp_s_cnsr_wait_time == 0 }"><span style="color:black;">0</c:if>
											%</td>
											<td>${data.s_chat_total_time_hour} : ${data.s_chat_total_time_min} : ${data.s_chat_total_time_sec}</td>
											<td>
												<c:if test="${data.cmp_s_chat_total_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_chat_total_time * (-1)}"/></c:if>
												<c:if test="${data.cmp_s_chat_total_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_chat_total_time}"/></c:if>
												<c:if test="${data.cmp_s_chat_total_time == 0 }"><span style="color:black;">0</c:if>
											%</td>
											<td>${data.s_after_time_hour} : ${data.s_after_time_min} : ${data.s_after_time_sec}</td>
											<td>
												<c:if test="${data.cmp_s_after_time < 0 }"><span style="color:blue;">▼ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_after_time * (-1)}"/></c:if>
												<c:if test="${data.cmp_s_after_time > 0 }"><span style="color:red;">▲ <fmt:formatNumber  pattern="#,###.#"  value="${data.cmp_s_after_time}"/></c:if>
												<c:if test="${data.cmp_s_after_time == 0 }"><span style="color:black;">0</c:if>
											%</td>
											
										</tr>
									</c:forEach>
									</c:otherwise>
									</c:choose>
									
								</tbody>
							</table>
						</div>
						<div class="table_bottom_area">
							<!-- pager -->
							<div class="pager">
								<button type="button" class="btn_prev_page dev_prev_btn"
									id="dev_prev_btn">이전으로</button>
								<input type="text" class="form_pager" value="${nowPage }"
									name="nowPage"> <span class="page_no">/
									${totPage } page(s)</span>
								<button type="button" class="btn_next_page dev_next_btn"
									id="dev_next_btn">다음으로</button>
							</div>
							<!--// pager -->
							<div class="table_count">
								<span class="cont_text"><em><fmt:formatNumber  pattern="#,###.#"  value="${totCount }"/></em> count(s)</span>
							</div>
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

</body>
</html>
