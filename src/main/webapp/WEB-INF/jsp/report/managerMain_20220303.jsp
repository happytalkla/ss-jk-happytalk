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
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/include.css' />" />
<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>
<script type="text/javascript">
	$(document)
			.ready(

					function() {
						//$('.left_content_area').css("height","none");
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

										/*	$.ajax({
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
											});*/

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



						$('#dash_day').html(y+"년 "+m+"월"+d+"일");
						

	});


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

	
	///5분 새로고침
	/* setTimeout(function(){
		location.reload();
		}, 300000); */
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
					<h2 class="sub_tit_dashboard">대시보드</h2>
					<span class="left_top_text">웹 사이트의 전체적인 상황을 추적하고 보여줍니다.</span>
				</div>
				<div class="inner">
					<div class="top_datepicker_area">
						
						<%-- <span class="left_top_text">${nowDate} ${selectNowTime}</span> <button type="button" class="btn_chat_s" onclick="location.reload();">새로고침<button> --%>
						
					</div>
					<div class="id_info_area">
						
						<!--<div class="left_table_area"> -->
						<c:forEach var="data" items="${basicList}"
							varStatus="status">
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">당일 근무 상담직원 수</span> <em>${data.cnt_today_adv} <span>명</span></em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">현재 근무 상담직원 수</span> <em>${data.cnt_now_adv}  <span>명</span></em>
							</div>
							<div class="id_box">
								<span class="tit">상담직원 배정대기 중</span> <em>${data.wait_cns}  <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit">배정 후 상담 대기 중</span> <em>${data.wait_cstm}  <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit">고객응대 필요</span> <em>${data.ing_stanby_cstm} <span>명</span></em>
							</div>
							<div class="id_box">
								<span class="tit">고객답변 대기중</span> <em>${data.ing_stanby_cnsr} <span>명</span></em>
							</div>
						</c:forEach>
						<!-- </div>  -->
						<!-- 
						<div class="left_table_area">
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<th>배정 후 상담대기중</th>
											<th>상담 진행 중</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty ctgWiList}">
												<tr>
													<td colspan="4">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${ctgWiList}" varStatus="status">
													<tr>
														<td>${data.ctg_nm_1}</td>
														<td>${data.ctg_nm_2}</td>
														<td>${data.wait_cnt}</td>
														<td>${data.ing_cnt}</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
						</div>
						 -->
					</div>
					<div style="overflow-y:auto;height:200px;">
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
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th>담당매니저</th>
											<th>상담직원</th>
											<th>근무상태</th>
											<th>배정 후 상담대기 중</th>
											<th>진행 중</th>
											<th>상담종료</th>
											<th>후처리 누락 건</th>
											<th>평균 상담 시간</th>
											<!-- <th>평균평점 / 건수</th> -->
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty cnsrList}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:set var="sum_wait_cnt" value="0"></c:set>
												<c:set var="sum_prsc_cnt" value="0"></c:set>
												<c:set var="sum_end_cnt" value="0"></c:set>
												<c:set var="sum_after_yn" value="0"></c:set>
												<c:set var="sum_cns_time" value="0"></c:set>
												<c:set var="sum_evl_avg_score" value="0"></c:set>
												<c:forEach var="data" items="${channelList}" varStatus="status">
													<c:set var="sum_wait_cnt" value="${sum_wait_cnt + data.wait_cnt }"></c:set>
													<c:set var="sum_prsc_cnt" value="${sum_prsc_cnt + data.prsc_cnt }"></c:set>
													<c:set var="sum_end_cnt" value="${sum_end_cnt + data.end_cnt }"></c:set>
													<c:set var="sum_after_yn" value="${sum_after_yn + data.after_yn }"></c:set>
													<c:set var="sum_cns_time" value="${sum_cns_time + data.cns_time }"></c:set>
													<c:set var="sum_evl_avg_score" value="${sum_evl_avg_score + data.evl_avg_score }"></c:set>
												</c:forEach>
												
												<c:forEach var="data" items="${cnsrList}" varStatus="status">
													<tr>
														<td><c:if test="${data.manager_nm eq null}">-</c:if>${data.manager_nm}</td>
														<td>${data.name}</td>
														<td>${data.work_yn}</td>
														<td>${data.wait_cnt}건</td>
														<td>
															${data.prsc_cnt}건
														</td>
														<td>${data.end_cnt}건</td>
														<td>${data.after_yn}건</td>
														<td><c:if test="${data.cns_date *1 != 0 }">
															${data.cns_date *1}<span>일</span>
														</c:if>
														<c:if test="${data.cns_hour *1 != 0}">
															${data.cns_hour *1}<span>시</span>
														</c:if>
														<c:if test="${data.cns_min *1 != 0}">
														${data.cns_min *1}<span>분</span>
														</c:if>
														${data.cns_sec *1}<span>초</span></td>
<%-- 														<td>
														<div class="reporting_list_star">
														${data.evl_avg_score} 점<span>/</span> ${data.end_cnt}건
														</div>
														</td> --%>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
					</div>
				</div>
				<!-- 아래 블럭 -->
				<div id="day_area">
					<div class="inner">
						<div class="top_datepicker_area"><em class="dash">Today	요약</em>
							<span id="dash_day"></span>
									<span class="dash_time" id="dash_time_day">수집시간 : AM 00 : 00 ~ ${selectNowTime}</span>
						</div>
						
						<div class="id_info_area">
							<c:forEach var="data" items="${basicTodayList}" varStatus="status">
								<div class="id_box w100">
									<span class="tit">${sessionVo.departCd} 상담 인입수(근무시간 내/외)</span>
									<em>총 <fmt:formatNumber  pattern="#,###.#"  value="${data.total_cns + data.default_disable_end}"/><span>건</span>
									<span>(<fmt:formatNumber  pattern="#,###.#"  value="${data.total_cns}"/>건 / <fmt:formatNumber  pattern="#,###.#"  value="${data.default_disable_end }"/>건)</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit">상담 포기 수(배정전/후/시스템)</span> 
									<em><fmt:formatNumber  pattern="#,###.#"  value="${data.quit_b_cnt + data.quit_a_cnt+ data.default_busy_end}"/> <span>건</span>
										<span>(<fmt:formatNumber  pattern="#,###.#"  value="${data.quit_b_cnt }"/> 건 / 
										<fmt:formatNumber  pattern="#,###.#"  value="${data.quit_a_cnt }"/> 건 / 
										<fmt:formatNumber  pattern="#,###.#"  value="${data.default_busy_end }"/> 건 )</span>
									</em>
								</div>
								<div class="id_box ">
									<span class="tit">상담원 진행 수<!-- (미배정+미접수+접수) --></span> <em>${data.prsc_cns} <span>건</span></em>
								</div>
								<div class="id_box ">
									<span class="tit">상담 종료 수</span> <em>${data.end_cns} <span>건</span></em>
								</div>
								<div class="id_box ">
									<span class="tit" style="background-color:#ddd">자동 종료 수</span> <em>${data.auto_end} <span>건</span></em>
								</div>
								<div class="id_box ">
									<span class="tit" style="background-color:#ddd">고객 종료 수</span> <em>${data.cstm_end} <span>건</span></em>
								</div>
								<div class="id_box ">
									<span class="tit" style="background-color:#ddd">상담원 종료 수</span> <em>${data.cnsr_end} <span>건</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit" >전체 상담원 평균 상담 시간</span> <em>${data.total_avg_talk_time}</em>
								</div>
<%-- 								<div class="id_box w100">
									<span class="tit" >전체 상담원 평균 점수 / 건수</span> <em>${data.total_avg_elv_score} <span>점 / </span>${data.total_elv_cnt} <span>건 </span></em>
								</div> --%>
								</c:forEach>
						</div>
						
						<div class="id_info_area">
								<c:forEach var="data" items="${linkDivList}" varStatus="status">
								<div class="id_box w100">
									<span class="tit">${sessionVo.departCd} 상담원 연결률<span style="font-size:10px;">(상담원 접수 / 챗봇연결수 )</span> / 건수</span> <em><span>		
									<c:choose>
										<c:when test="${Integer.parseInt(String.valueOf(data.bot2cnsr_cnt)) > 0 && Integer.parseInt(String.valueOf(data.bot_throu_cnt)) > 0}">
										<fmt:formatNumber value="${Integer.parseInt(String.valueOf(data.bot2cnsr_cnt)) div Integer.parseInt(String.valueOf(data.bot_throu_cnt)) * 100 }" type="number" pattern="#,###.#" />	
										</c:when>
										<c:otherwise>
											0
										</c:otherwise>
									</c:choose>
									% /
									 <fmt:formatNumber  pattern="#,###.#"  value="${data.bot2cnsr_cnt}"/> 건</span></em>
								</div>
								
								<div class="id_box w100">
									<span class="tit">전체 봇 진행 중 / 완료</span>
									 <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.total_ing_bot}"/> <span>건</span> / 
									 <fmt:formatNumber  pattern="#,###.#"  value="${data.total_end_bot}"/> <span>건</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit" style="background-color:#ddd">카카오톡 봇 진행 중 / 완료</span>
									<em><span> <fmt:formatNumber  pattern="#,###.#"  value="${data.kakao_ing_cnt }"/> <span>건</span> / <fmt:formatNumber  pattern="#,###.#"  value="${data.kakao_end_cnt }"/> <span>건</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit" style="background-color:#ddd">네이버톡 봇 진행 중 / 완료</span>
									<em> <span> <fmt:formatNumber  pattern="#,###.#"  value="${data.naver_ing_cnt }"/> <span>건</span> / 
									<fmt:formatNumber  pattern="#,###.#"  value="${data.naver_end_cnt }"/> <span>건</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit" style="background-color:#ddd">웹채팅 봇 진행 중 / 완료</span>
									<em> <span>
									<fmt:formatNumber  pattern="#,###.#"  value="${data.web_ing_cnt }"/> <span>건</span>  / 
									<fmt:formatNumber  pattern="#,###.#"  value="${data.web_end_cnt }"/> <span>건</span></em>
									
								</div>
								</c:forEach>
						</div>
					</div>

					<div class="inner">
						<div class="graph_area">
							<!-- 왼쪽 테이블 영역 -->
							<!-- 
							<div class="left_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<th>후처리 수</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty ctgList}">
												<tr>
													<td colspan="3">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${ctgList}" varStatus="status">
													<tr>
														<td>${data.ctg_nm_1}</td>
														<td>${data.ctg_nm_2}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.after_cnt}"/></td>
														
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
							 -->
							<!--// 왼쪽 테이블 영역 -->

							<!-- 오른쪽 그래프 영역 -->
							<div id="graph_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th colspan="2">서비스명</th>
											<th>봇 처리 수</th>
											<th>평균 사용 건수 / 1인</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty mciList}">
												<tr>
													<td colspan="4">조회된 내용이 없습니다.</td>
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
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.total_cnt}"></fmt:formatNumber></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cstm_cnt}"></fmt:formatNumber></td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
							<!--// 오른쪽 그래프 영역 -->
						</div>
					</div>

				</div>
				<c:if test="${member.memberDivCd == 'M'}">
				<div class="left_content_head">
					<h2 class="sub_tit">${member.name}의 대시보드</h2>
					<span class="left_top_text">담당 상담직원의 상황을 추적하고 보여줍니다.</span>
				</div>
				
				<div class="inner">
					<div class="id_info_area">
						<!--<div class="left_table_area"> -->
						<c:forEach var="data" items="${basicListManager}"
							varStatus="status">
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">당일 근무 상담직원 수</span> <em>${data.cnt_today_adv} <span>명</span></em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">현재 근무 상담직원 수</span> <em>${data.cnt_now_adv}  <span>명</span></em>
							</div>
							<div class="id_box">
								<span class="tit">상담직원 배정대기 중</span> <em>${data.wait_cns}  <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit">배정 후 상담 대기 중</span> <em>${data.wait_cstm}  <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit">고객응대 필요</span> <em>${data.ing_stanby_cstm} <span>명</span></em>
							</div>
							<div class="id_box">
								<span class="tit">고객답변 대기중</span> <em>${data.ing_stanby_cnsr} <span>명</span></em>
							</div>
						</c:forEach>
						<!-- </div>  -->
						<!-- 
						<div class="left_table_area">
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col>
										<col>
										<col>
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<th>배정 후 상담대기중</th>
											<th>상담 진행 중</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty ctgWiList}">
												<tr>
													<td colspan="4">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${ctgWiList}" varStatus="status">
													<tr>
														<td>${data.ctg_nm_1}</td>
														<td>${data.ctg_nm_2}</td>
														<td>${data.wait_cnt}</td>
														<td>${data.ing_cnt}</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
						</div>
						 -->
					</div>
					<div class="tablewrap" style="hegith:150px;overflow-y:auto;">  <!-- 상담직원 목록 시작-->
						<table class="tCont service">
							<caption>통계테이블 목록입니다.</caption>
							<colgroup>
								<col><col><col><col><col><col><col><col><col>
							</colgroup>
							<thead>
								<tr>
									<th>담당매니저</th>
									<th>상담직원</th>
									<th>근무상태</th>
									<th>배정 후 상담대기 중</th>
									<th>진행 중</th>
									<th>상담종료</th>
									<th>후처리 누락 건</th>
									<th>평균 상담 시간</th>
									<!-- <th>평균평점 / 건수</th> -->
								</tr>
							</thead>
							<tbody>

							<c:choose>
								<c:when test="${empty cnsrListManager}">
								<tr>
									<td colspan="9">조회된 내용이 없습니다.</td>
								</tr>
								</c:when>
								<c:otherwise>
									<c:set var="sum_wait_cnt" value="0"></c:set>
									<c:set var="sum_prsc_cnt" value="0"></c:set>
									<c:set var="sum_end_cnt" value="0"></c:set>
									<c:set var="sum_after_yn" value="0"></c:set>
									<c:set var="sum_cns_time" value="0"></c:set>
									<c:set var="sum_evl_avg_score" value="0"></c:set>
									<c:forEach var="data" items="${channelListManager}" varStatus="status">
										<c:set var="sum_wait_cnt" value="${sum_wait_cnt + data.wait_cnt }"></c:set>
										<c:set var="sum_prsc_cnt" value="${sum_prsc_cnt + data.prsc_cnt }"></c:set>
										<c:set var="sum_end_cnt" value="${sum_end_cnt + data.end_cnt }"></c:set>
										<c:set var="sum_after_yn" value="${sum_after_yn + data.after_yn }"></c:set>
										<c:set var="sum_cns_time" value="${sum_cns_time + data.cns_time }"></c:set>
										<c:set var="sum_evl_avg_score" value="${sum_evl_avg_score + data.evl_avg_score }"></c:set>
									</c:forEach>
									
									<c:forEach var="data" items="${cnsrListManager}" varStatus="status">
										<tr>
											<td><c:if test="${data.manager_nm eq null}">-</c:if>${data.manager_nm}</td>
											<td>${data.name}</td>
											<td>${data.work_yn}</td>
											<td>${data.wait_cnt}건</td>
											<td>
												${data.prsc_cnt}건
											</td>
											<td>${data.end_cnt}건</td>
											<td>${data.after_yn}건</td>
											<td><c:if test="${data.cns_date *1 != 0 }">
												${data.cns_date *1}<span>일</span>
											</c:if>
											<c:if test="${data.cns_hour *1 != 0}">
												${data.cns_hour *1}<span>시</span>
											</c:if>
											<c:if test="${data.cns_min *1 != 0}">
											${data.cns_min *1}<span>분</span>
											</c:if>
											${data.cns_sec *1}<span>초</span></td>
<%-- 											<td>
											<div class="reporting_list_star">
											${data.evl_avg_score} 점<span>/</span> ${data.end_cnt}건
											</div>
											</td> --%>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					</div><!-- 상담직원 목록 끝-->
				</div>
				<!-- 아래 블럭 -->
					<div class="inner">
						<div class="top_datepicker_area"><em class="dash">Today	요약</em>
							<span id="dash_day"></span>
									<span class="dash_time" id="dash_time_day">수집시간 : AM 00 : 00 ~ ${selectNowTime}</span>
						</div>
						
						<div class="id_info_area" style="border:0px;">
							<c:forEach var="data" items="${basicTodayListManager}" varStatus="status">
							<div class="id_box w100">
								<span class="tit">${sessionVo.departCd} 상담 인입수(근무시간 내/외)</span>
								<em>총 <fmt:formatNumber  pattern="#,###.#"  value="${data.total_cns + data.default_disable_end}"/><span>건</span>
								<span>(<fmt:formatNumber  pattern="#,###.#"  value="${data.total_cns - data.default_disable_end }"/>건 / <fmt:formatNumber  pattern="#,###.#"  value="${data.default_disable_end }"/>건)</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit">상담 포기 수(배정전/후/시스템)</span> 
								<em><fmt:formatNumber  pattern="#,###.#"  value="${data.quit_b_cnt + data.quit_a_cnt}"/> <span>건</span>
									<span>(<fmt:formatNumber  pattern="#,###.#"  value="${data.quit_b_cnt }"/> 건 / <fmt:formatNumber  pattern="#,###.#"  value="${data.quit_a_cnt }"/> 건 / <fmt:formatNumber  pattern="#,###.#"  value="${data.default_busy_end }"/> 건 )</span>
								</em>
							</div>
							<div class="id_box ">
								<span class="tit">상담직원 진행 수<!-- (미배정+미접수+접수) --></span> <em>${data.prsc_cns} <span>건</span></em>
							</div>
							<div class="id_box ">
								<span class="tit">상담 종료 수</span> <em>${data.end_cns} <span>건</span></em>
							</div>
							<div class="id_box ">
								<span class="tit" style="background-color:#ddd">자동 종료 수</span> <em>${data.auto_end} <span>건</span></em>
							</div>
							<div class="id_box ">
								<span class="tit" style="background-color:#ddd">고객 종료 수</span> <em>${data.cstm_end} <span>건</span></em>
							</div>
							<div class="id_box ">
								<span class="tit" style="background-color:#ddd">상담직원 종료 수</span> <em>${data.cnsr_end} <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit" >전체 상담직원 평균 상담 시간</span> <em>${data.total_avg_talk_time}</em>
							</div>
<%-- 							<div class="id_box w100">
								<span class="tit" >전체 상담직원 평균 점수 / 건수</span> <em>${data.total_avg_elv_score} <span>점 / </span>${data.total_elv_cnt} <span>건 </span></em>
							</div> --%>
							</c:forEach>
						
							<c:forEach var="data" items="${linkDivListManager}" varStatus="status">
							<div class="id_box w100">
								<span class="tit">${sessionVo.departCd} 상담직원 연결률<span style="font-size:10px;">(상담직원 접수 / 챗봇연결수 )</span> / 건수</span> <em><span>		
								<c:choose>
									<c:when test="${Integer.parseInt(String.valueOf(data.bot2cnsr_cnt)) > 0 && Integer.parseInt(String.valueOf(data.bot_throu_cnt)) > 0}">
									<fmt:formatNumber value="${Integer.parseInt(String.valueOf(data.bot2cnsr_cnt)) div Integer.parseInt(String.valueOf(data.bot_throu_cnt)) * 100 }" type="number" pattern="#,###.#" />	
									</c:when>
									<c:otherwise>
										0
									</c:otherwise>
								</c:choose>
								% /
								 <fmt:formatNumber  pattern="#,###.#"  value="${data.bot2cnsr_cnt}"/> 건</span></em>
							</div>
							</div>
						
						<div class="id_info_area" style="border:0px;">
							<div class="id_box w100">
								<span class="tit">전체 봇 진행 중 / 완료</span>
								 <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.total_ing_bot}"/> <span>건</span> / 
								 <fmt:formatNumber  pattern="#,###.#"  value="${data.total_end_bot}"/> <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit" style="background-color:#ddd">카카오톡 봇 진행 중 / 완료</span>
								<em><span> <fmt:formatNumber  pattern="#,###.#"  value="${data.kakao_ing_cnt }"/> <span>건</span> / <fmt:formatNumber  pattern="#,###.#"  value="${data.kakao_end_cnt }"/> <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit" style="background-color:#ddd">O2 봇 진행 중 / 완료</span>
								<em> <span> <fmt:formatNumber  pattern="#,###.#"  value="${data.o2_ing_cnt }"/> <span>건</span> / 
								<fmt:formatNumber  pattern="#,###.#"  value="${data.o2_end_cnt }"/> <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit" style="background-color:#ddd">mPop 봇 진행 중 / 완료</span>
								<em> <span> <fmt:formatNumber  pattern="#,###.#"  value="${data.mpop_ing_cnt }"/> <span>건</span> / 
								<fmt:formatNumber  pattern="#,###.#"  value="${data.mpop_end_cnt }"/> <span>건</span></em>
							</div>
							<div class="id_box w100">
								<span class="tit" style="background-color:#ddd">웹채팅 봇 진행 중 / 완료</span>
								<em> <span>
								<fmt:formatNumber  pattern="#,###.#"  value="${data.web_ing_cnt }"/> <span>건</span>  / 
								<fmt:formatNumber  pattern="#,###.#"  value="${data.web_end_cnt }"/> <span>건</span></em>
								
							</div>
							</c:forEach>
						</div>
					</div>
				</c:if>
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
