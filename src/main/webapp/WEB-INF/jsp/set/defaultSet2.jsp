<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
	<script type="text/javascript" src="<c:url value='/js/common.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

	<script type="text/javascript">

		// on, off 버튼 클릭
		$(document).on("change", "#settingForm .dev-switch-input", function() {
			var cnsPossibleYn;
			if($(this).prop("checked")){
				cnsPossibleYn = $(this).attr("data-on-value");
			}else{
				cnsPossibleYn = $(this).attr("data-off-value");
			}

			$(this).closest("label").find(".dev-switch-input-value").val(cnsPossibleYn);
		});

		// 상담직원 근무여부 설정
		$(document).on("click", "#settingForm [name='updateBtn']", function() {
			var selBtn = $(this).attr("data-btn");

			if(selBtn == "workStatusCd"){
				var workStatusCd = $("#settingForm [name='workStatusCd']").val();
				if(workStatusCd == "R"){
					if(!confirm("전 상담직원이 추가 상담을 할 수 없습니다.\n계속 진행하시겠습니까?")){
						$("#settingForm #workStatusCd_W").prop("checked", true);
						return false;
					}
				}
			}

			if(selBtn == "cnsrMaxCnt"){
				var cnsrMaxCnt = $("#settingForm [name='cnsrMaxCnt']").val().trim();
				$("#settingForm [name='cnsrMaxCnt']").val(cnsrMaxCnt);
				if(!$.isNumeric(cnsrMaxCnt)){
					alert("숫자만 입력해 주세요.");
					$("#settingForm [name='cnsrMaxCnt']").select();
					return false;
				}
			}

			if(selBtn == "cnsrOnceMaxCnt"){
				var cnsrOnceMaxCnt = $("#settingForm [name='cnsrOnceMaxCnt']").val().trim();
				$("#settingForm [name='cnsrOnceMaxCnt']").val(cnsrOnceMaxCnt);
				if(!$.isNumeric(cnsrOnceMaxCnt)){
					alert("숫자만 입력해 주세요.");
					$("#settingForm [name='cnsrOnceMaxCnt']").select();
					return false;
				}
			}

			if(selBtn == "pwdTerm"){
				var pwdTermUseYn = $("#settingForm [name='pwdTermUseYn']").val();
				if(pwdTermUseYn == "Y"){
					var pwdTerm = $("#settingForm [name='pwdTerm']").val().trim();
					$("#settingForm [name='pwdTerm']").val(pwdTerm);
					if(!$.isNumeric(pwdTerm)){
						alert("숫자만 입력해 주세요.");
						$("#settingForm [name='pwdTerm']").select();
						return false;
					}
				}else{
					$("#settingForm [name='pwdTerm']").val("0");
				}
			}

			if(selBtn == "passTimeUseYn"){
				var passTimeUseYn = $("#settingForm [name='passTimeUseYn']").val();

				if(passTimeUseYn == "Y"){
					var checkedLen = $("#settingForm input[name='markNum']:checked").length;
					if(checkedLen < 1){
						fn_layerMessage("상담 경과 표시 시간을 1개 이상 선택해 주세요.");
						return false;
					}
				}
			}
			// 자동메세지로 변경됨 수정
			if(selBtn == "unsocialAcceptYn"){
				var isValid = true;
				$("#settingForm div[name='divWorkTime']").each(function(idx, val) {
					var channel = $(this).find("[name='channel']").val();
					var channelNm = $(this).find("[name='channelNm']").val();

					// 평일 근무 시간 체크
					var weekStartTime1 = $(this).find("select[name='weekStartTime1'] option:selected").val();
					var weekStartTime2 = $(this).find("select[name='weekStartTime2'] option:selected").val();
					var weekEndTime1 = $(this).find("select[name='weekEndTime1'] option:selected").val();
					var weekEndTime2 = $(this).find("select[name='weekEndTime2'] option:selected").val();

					var weekStartTime = weekStartTime1 + weekStartTime2;
					var weekEndTime = weekEndTime1 + weekEndTime2;

					if (weekStartTime * 1 >= weekEndTime * 1) {
						alert("[" + channelNm + "] 평일 근무 종료시간이 시작시간보다 커야 합니다.");
						isValid = false;
						return false;
					}

					$(this).find("input[name='weekStartTime']").val(weekStartTime);
					$(this).find("input[name='weekEndTime']").val(weekEndTime);

					// 토요일 근무 시간 체크
					var satStartTime1 = $(this).find("select[name='satStartTime1'] option:selected").val();
					var satStartTime2 = $(this).find("select[name='satStartTime2'] option:selected").val();
					var satEndTime1 = $(this).find("select[name='satEndTime1'] option:selected").val();
					var satEndTime2 = $(this).find("select[name='satEndTime2'] option:selected").val();

					var satStartTime = satStartTime1 + satStartTime2;
					var satEndTime = satEndTime1 + satEndTime2;

					var satWorkYn = $(this).find("input[name='satWorkYn" + channel + "']").val();
					if (satWorkYn == "Y") {
						if (satStartTime * 1 >= satEndTime * 1) {
							alert("[" + channelNm + "] 토요일 근무 종료시간이 시작시간보다 커야 합니다.");
							isValid = false;
							return false;
						}
					}

					$(this).find("input[name='satStartTime']").val(satStartTime);
					$(this).find("input[name='satEndTime']").val(satEndTime);

					// 일요일 근무 시간 체크
					var sunStartTime1 = $(this).find("select[name='sunStartTime1'] option:selected").val();
					var sunStartTime2 = $(this).find("select[name='sunStartTime2'] option:selected").val();
					var sunEndTime1 = $(this).find("select[name='sunEndTime1'] option:selected").val();
					var sunEndTime2 = $(this).find("select[name='sunEndTime2'] option:selected").val();

					var sunStartTime = sunStartTime1 + sunStartTime2;
					var sunEndTime = sunEndTime1 + sunEndTime2;

					var sunWorkYn = $(this).find("input[name='sunWorkYn']").val();
					if (sunWorkYn == "Y") {
						if (sunStartTime * 1 >= sunEndTime * 1) {
							alert("[" + channelNm + "] 일요일 근무 종료시간이 시작시간보다 커야 합니다.");
							isValid = false;
							return false;
						}
					}
					$(this).find("input[name='sunStartTime']").val(sunStartTime);
					$(this).find("input[name='sunEndTime']").val(sunEndTime);
				});

				if (!isValid) {
					return false;
				}
			}
			$("#settingForm [name='selBtn']").val(selBtn);

			console.log($("#settingForm").serialize());
			$.ajax({
				url : "<c:url value='/set/updateSet2' />",
				data : $("#settingForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
					$("#settingForm [name='selBtn']").val("");
				}
			});
		});


		// up button 클릭
		$(document).on("click", "#settingForm .dev-btn_go_up", function() {
			var cnsrMaxCnt = $("#settingForm input[name='cnsrMaxCnt']").val();
			cnsrMaxCnt = cnsrMaxCnt*1 + 1;
			$("#settingForm input[name='cnsrMaxCnt']").val(cnsrMaxCnt);
		});

		// down button 클릭
		$(document).on("click", "#settingForm .dev-btn_go_down", function() {
			var cnsrMaxCnt = $("#settingForm input[name='cnsrMaxCnt']").val();
			cnsrMaxCnt = cnsrMaxCnt*1 - 1;
			if(cnsrMaxCnt < 0){
				cnsrMaxCnt = 0;
			}
			$("#settingForm input[name='cnsrMaxCnt']").val(cnsrMaxCnt);
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
			<form id="settingForm" name="settingForm" method="post" action="<c:url value='/set/updateSet' />">
				<input type="hidden" name="siteId" value="${defaultSet.site_id }" />
				<input type="hidden" name="selBtn" value="" />
				<div class="left_content_head">
					<h2 class="sub_tit">기본조건 설정</h2>
				</div>
				<div class="inner_nobg tm_disabled">
					<!-- 왼쪽 컨텐츠 -->
					<div class="box_cont_left">
						
						<div class="box_cont">
							<c:forEach items="${workTimeList}" var="workTime">
								<div class="box_head_area">
									<h3 class="sub_stit">
										<c:out value="${workTime.channel_nm}"/> 근무 시간 관리
									</h3>
								</div>
								<div class="box_body" name="divWorkTime">
									<ul class="dot_list">
										<li>
											<span class="tit">평일</span>
											<div class="form_area">
												<input type="hidden" name="channel" value="<c:out value="${workTime.channel}"/>" />
												<input type="hidden" name="channelNm" value="<c:out value="${workTime.channel_nm}"/>" />
												<input type="hidden" name="weekStartTime" value="" />
												<input type="hidden" name="weekEndTime" value="" />
												<select name="weekStartTime1">
													<c:if test="${empty workTime.weekday_start_time3}">
														<c:set target="${workTime}" property="weekday_start_time3" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="23" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.weekday_start_time3 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>시
												<select name="weekStartTime2">
													<c:if test="${empty workTime.weekday_start_time4}">
														<c:set target="${workTime}" property="weekday_start_time4" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="59" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.weekday_start_time4 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>분
												~
												<select name="weekEndTime1">
													<c:if test="${empty workTime.weekday_end_time3}">
														<c:set target="${workTime}" property="weekday_end_time3" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="23" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.weekday_end_time3 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>시
												<select name="weekEndTime2">
													<c:if test="${empty workTime.weekday_end_time4}">
														<c:set target="${workTime}" property="weekday_start_time4" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="59" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.weekday_end_time4 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>분
											</div>
										</li>
										<li>
											<span class="tit">토요일</span>
											<div class="form_area textR" style="width:70px;">
												<label class="switch w70">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${workTime.sat_work_yn eq 'Y' }">checked</c:if>>
													<span class="switch-label" data-on="근무" data-off="휴일"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="satWorkYn" class="dev-switch-input-value" value="${workTime.sat_work_yn }">
												</label>
											</div>
											<div class="form_area" style="width:calc(100% - 150px);">
												<input type="hidden" name="satStartTime" value="" />
												<input type="hidden" name="satEndTime" value="" />
												<select name="satStartTime1">
													<c:if test="${empty workTime.sat_start_time3}">
														<c:set target="${workTime}" property="sat_start_time3" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="23" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sat_start_time3 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>시
												<select name="satStartTime2">
													<c:if test="${empty workTime.sat_start_time4}">
														<c:set target="${workTime}" property="sat_start_time4" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="59" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sat_start_time4 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>분
												~
												<select name="satEndTime1">
													<c:if test="${empty workTime.sat_end_time3}">
														<c:set target="${workTime}" property="sat_end_time3" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="23" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sat_end_time3 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>시
												<select name="satEndTime2">
													<c:if test="${empty workTime.sat_end_time4}">
														<c:set target="${workTime}" property="sat_start_time4" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="59" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sat_end_time4 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>분
											</div>
										</li>
										<li>
											<span class="tit">일요일</span>
											<div class="form_area textR" style="width:70px;">
												<label class="switch w70">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${workTime.sun_work_yn eq 'Y' }">checked</c:if>>
													<span class="switch-label" data-on="근무" data-off="휴일"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="sunWorkYn" class="dev-switch-input-value" value="${workTime.sun_work_yn}">
												</label>
											</div>
											<div class="form_area" style="width:calc(100% - 150px);">
												<input type="hidden" name="sunStartTime" value="" />
												<input type="hidden" name="sunEndTime" value="" />
												<select name="sunStartTime1">
													<c:if test="${empty workTime.sun_start_time3}">
														<c:set target="${workTime}" property="sun_start_time3" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="23" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sun_start_time3 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>시
												<select name="sunStartTime2">
													<c:if test="${empty workTime.sun_start_time4}">
														<c:set target="${workTime}" property="sun_start_time4" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="59" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sun_start_time4 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>분
												~
												<select name="sunEndTime1">
													<c:if test="${empty workTime.sun_end_time3}">
														<c:set target="${workTime}" property="sun_end_time3" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="23" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sun_end_time3 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>시
												<select name="sunEndTime2">
													<c:if test="${empty workTime.sun_end_time4}">
														<c:set target="${workTime}" property="sun_start_time4" value="00"/>
													</c:if>
													<c:forEach var="data" begin="0" end="59" >
														<option value="<fmt:formatNumber type="number" value="${data}" pattern="00"/>" <c:if test="${workTime.sun_end_time4 * 1 eq data}">selected="selected"</c:if>><fmt:formatNumber type="number" value="${data}" pattern="00"/></option>
													</c:forEach>
												</select>분
											</div>
										</li>
									</ul>
									<ul class="dot_list">
										<li>
											<span class="tit" style="width:150px;">근무시간 외 상담 접수</span>
											<div class="form_area textR">
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${workTime.unsocial_accept_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="접수가능" data-off="접수불가"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="unsocialAcceptYn" class="dev-switch-input-value" value="${workTime.unsocial_accept_yn }">
												</label>
											</div>
										</li>
									</ul>
								</div>
							</c:forEach>
							<div class="box_body_gray">
								<p class="text_info">※ 특정 휴일은 [상담관리 - 휴무일/근무 관리] 메뉴에서 지정 가능합니다.</p>
								<p class="text_info">※ [적용] 클릭 시 설정된 근무시간 기준으로 익일부터 다음달 스케줄을 등록합니다. (기존 등록된 스케줄이 있는 경우 업데이트)</p>
								<p class="text_info">※ 근무시간 외 상담 "접수가능" 설정 시 "상담가능" 상태의 상담직원이 있는 경우 상담을 배정합니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="unsocialAcceptYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<%-- <div class="box_cont" style="display:none;">
							<div class="box_head_area">
								<h3 class="sub_stit">비밀번호 변경 주기</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N"  <c:if test="${defaultSet.pwd_term_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="pwdTermUseYn" class="dev-switch-input-value" value="${defaultSet.pwd_term_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">고객 답변 대기 중인 상담이 아래 설정 시간 경과 후에도 답변이 없을 경우 종료 메세지와 함께 자동 종료됩니다.</p>
								<ul class="dot_list">
									<li>
										<span class="tit">변경주기</span>
										<div class="form_aread">
											<input type="text" class="form_pw_day" name="pwdTerm" style="text-align:right;" value="${defaultSet.pwd_term }">
											일
										</div>
									</li>
								</ul>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="pwdTerm" name="updateBtn"><i></i>적용</button>
							</div>
						</div> --%>
						<!-- 기능변경으로 삭제
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">상담직원당 최대 상담 건수</h3>
							</div>
							<div class="box_body">
								<p class="text_info02">
									상담직원이 가질 수 있는 최대 상담건(미접수+상담진행)수를 지정합니다.<br>
									해당 상담 개수에 도달하면 해당 상담직원은 신규 상담을 받지 못하는 상담 불가로 판단합니다.
								</p>
								<div class="form_up_down">
									<input type="text" name="cnsrMaxCnt" value="${defaultSet.cnsr_max_cnt }">
									<button type="button" class="btn_go_up dev-btn_go_up">UP</button>
									<button type="button" class="btn_go_down dev-btn_go_down">DOWN</button>
								</div>
								건
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cnsrMaxCnt" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						 -->
						<!-- 삭제됨
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">상담직원 매핑 카테고리 사용</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.ctg_mapping_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="사용안함"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="ctgMappingUseYn" class="dev-switch-input-value" value="${defaultSet.ctg_mapping_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">'상담직원 자동 매칭' 기능 사용 시 '상담 배분 관리'에서 설정한 분류에 해당하는 상담직원에게 상담이 지정됩니다.</p>
								<p class="text_info">※ '상담직원 자동 매칭'을 사용안함으로 설정하시면 매핑 카테고리 사용이 적용되지 않습니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="ctgMappingUseYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div>	
						 -->
					</div>
					<!--// 왼쪽 컨텐츠 -->

					<!-- 오른쪽 컨텐츠 -->
					<div class="box_cont_right">
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">전 상담직원 상담가능 설정</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="W" data-off-value="R" <c:if test="${defaultSet.work_status_cd eq 'W' }">checked</c:if> >
									<span class="switch-label" data-on="상담가능" data-off="상담불가"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="workStatusCd" class="dev-switch-input-value" value="${defaultSet.work_status_cd }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">전체 상담직원에 대해서 상담불가 설정을 적용합니다.</p>
								<p class="text_info">※ 상담불가로 설정할 경우 전체 상담직원에 대해서 상담접수를 받지 않습니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="workStatusCd" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<%-- <div class="box_cont" style="display:none;">
							<div class="box_head_area">
								<h3 class="sub_stit">상담직원 채팅 선택</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.self_choi_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="선택가능" data-off="선택불가"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="selfChoiUseYn" class="dev-switch-input-value" value="${defaultSet.self_choi_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">상담직원이 지정되지 않은 상담을 상담직원이 직접 선택해서 상담할 수 있는 기능입니다.</p>
								<p class="text_info">※ 매니저 부재시 상담직원이 직접 상담을 선택할 수 있는 기능입니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="selfChoiUseYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div> --%>
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">챗봇(해피봇) 사용여부 설정</h3>
								
							</div>
							<div class="box_body">
								<table width="100%" >
									<colgroup>
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
										<col width="10%">
									</colgroup>
									<tr>
										<c:forEach items="${workTimeList}" var="workTime">
											<td height="45px" align="right">
												<span class="tit"><c:out value="${workTime.channel_nm}"/> : </span>
											</td>
											<td align="left">
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${workTime.chatbot_use_yn eq 'Y' }">checked</c:if>>
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUseYn" class="dev-switch-input-value" value="${workTime.chatbot_use_yn }">
												</label>
											</td>
										</c:forEach>
									</tr>
									<%-- <tr>
										<td height="45px" align="right">
											<span class="tit" >홈페이지 : </span>
										</td>
										<td align="left">
											<label class="switch w90">
												<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.a_chatbot_use_yn eq 'Y' }">checked</c:if> >
												<span class="switch-label" data-on="사용" data-off="사용안함"></span>
												<span class="switch-handle"></span>
												<input type="hidden" name="chatbotUseYn" class="dev-switch-input-value" value="${defaultSet.a_chatbot_use_yn }">
											</label>
										</td>
										<td align="right">
											<span class="tit">카카오채널 : </span>
										</td>
										<td>
											<label class="switch w90">
												<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.b_chatbot_use_yn eq 'Y' }">checked</c:if> >
												<span class="switch-label" data-on="사용" data-off="사용안함"></span>
												<span class="switch-handle"></span>
												<input type="hidden" name="b_chatbotUseYn" class="dev-switch-input-value" value="${defaultSet.b_chatbot_use_yn }">
											</label>
										</td>
										<td align="right">
											<span class="tit">O2 : </span>
										</td>
										<td>
											<label class="switch w90">
												<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.c_chatbot_use_yn eq 'Y' }">checked</c:if> >
												<span class="switch-label" data-on="사용" data-off="사용안함"></span>
												<span class="switch-handle"></span>
												<input type="hidden" name="c_chatbotUseYn" class="dev-switch-input-value" value="${defaultSet.c_chatbot_use_yn }">
											</label>
										</td>
										<td align="right">
											<span class="tit">mPOP : </span>
										</td>
										<td>
											<label class="switch w90">
												<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.d_chatbot_use_yn eq 'Y' }">checked</c:if> >
												<span class="switch-label" data-on="사용" data-off="사용안함"></span>
												<span class="switch-handle"></span>
												<input type="hidden" name="d_chatbotUseYn" class="dev-switch-input-value" value="${defaultSet.d_chatbot_use_yn }">
											</label>
										</td>
										<td align="right">
											<span class="tit">CTI : </span>
										</td>
										<td>
											<label class="switch w90">
												<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.e_chatbot_use_yn eq 'Y'}">checked</c:if> >
												<span class="switch-label" data-on="사용" data-off="사용안함"></span>
												<span class="switch-handle"></span>
												<input type="hidden" name="e_chatbotUseYn" class="dev-switch-input-value" value="${defaultSet.e_chatbot_use_yn}">
											</label>
										</td>
									</tr> --%>
								</table>
								<p class="text_info02">고객 문의 시 챗봇이 먼저 고객 응대를 시작할 지 설정합니다.</p>
								<p class="text_info02">'사용안함'으로 설정할 경우 채팅방 생성 시 바로 상담직원으로 연결됩니다.</p>
								<!--<p class="text_info">※ 오픈빌더는 |오픈빌더 관리자|에서 별도 설정이 필요합니다.</p>-->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="chatbotUseYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">상담직원 변경 자동승인</h3>
								<label class="switch w90">
												<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N"  <c:if test="${defaultSet.auto_chng_appr_yn eq 'Y' }">checked</c:if> >
												<span class="switch-label" data-on="자동승인" data-off="수동승인"></span>
												<span class="switch-handle"></span>
												<input type="hidden" name="autoChngApprYn" class="dev-switch-input-value" value="${defaultSet.auto_chng_appr_yn }">
											</label>
							</div>
							<div class="box_body">
								</br>
								<p class="text_info02">상담직원이 '상담직원변경 신청시' 자동으로 승인되어 지정된 상담직원에게 상담이 이관됩니다.<br>해당 기능은 상담직원들끼리 계속 상담건을 미룰 수 있으니 엄격한 규칙아래서 사용 부탁드립니다.</p>
								<p class="text_info">※ 상담직원 변경으로 지정 가능한 상담직원에게 이관됩니다.</p>
								<p class="text_info">※ "수동승인"<a id="secretBatchList" href="javascript:void(0);">&nbsp;</a>설정 시 담당 매니저에게 변경 요청이 접수되며 매니저가 이관할 상담직원을 선택합니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="autoChngApprYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div>								
						<!-- 추가됬다 다 삭제됨 chatbotTimeYn등 테이블안에 버튼들 다 추가 
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">챗봇(해피봇) 상담시간 설정</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="사용안함"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="chatbotTimeYn" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">고객의 채팅상담 요청 시 원하는 시점에 챗봇이 호출되도록 설정할 수 있습니다.</p>
								<p class="text_info02">개별 설정을 원하시는 경우 챗봇 상담시간 설정을  '사용'상태로 변경한 후 이용 가능합니다.</p>
								<p class="text_info02">'사용안함'으로 설정하는 경우 상담시간과 관계없이 항상 챗봇 응대를 실행합니다.</p>
								<table class="tCont dev_category_table" style="margin-top:30px;" >
								<caption></caption>
									<colgroup>
										<col style="width:15%">
										<col style="width:15%">
										<col style="width:20%">
										<col style="width:20%">
									</colgroup>
									<thead>
										<tr>
											<th colspan="2">시간대</th>
											<th>챗봇 사용</th>
											<th>상담직원 연결 버튼 노출</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td rowspan="2">평일</td>
											<td>근무시간 내</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUse1" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="consul1" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
										</tr>	
										<tr>
											<td>근무시간 외</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUse2" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="consul2" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
										</tr>	
										<tr>
											<td rowspan="2">토요일</td>
											<td>근무시간 내</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUse3" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="consul3" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
										</tr>
										<tr>
											<td>근무시간 외</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUse4" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="consul4" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
										</tr>																											
										<tr>
											<td rowspan="2">일요일</td>
											<td>근무시간 내</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUse5" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="consul5" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
										</tr>
										<tr>
											<td>근무시간 외</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="chatbotUse6" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
											<td>
												<label class="switch w90">
													<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.chatbot_use_yn eq 'Y' }">checked</c:if> >
													<span class="switch-label" data-on="사용" data-off="사용안함"></span>
													<span class="switch-handle"></span>
													<input type="hidden" name="consul6" class="dev-switch-input-value" value="${defaultSet.chatbot_use_yn }">
												</label>
											</td>
										</tr>
									</tbody>
								</table>								
								
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="chatbotTimeYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div>						
						-->
						<!-- 기능변경으로 삭제 
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 경과 시간 표시</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${defaultSet.pass_time_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="표시" data-off="표시안함"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="passTimeUseYn" class="dev-switch-input-value" value="${defaultSet.pass_time_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">상담시간이 지속됨에 따라 표시되는 아이콘을 설정합니다.</p>
								<c:forEach var="data" items="${chatPassTimeList }" varStatus="status">
									<p class="text_info02">
										<input type="checkbox" class="checkbox_18" name="markNum" value="${data.mark_num }" id="markNum_${status.index }" <c:if test="${data.use_yn eq 'Y' }">checked</c:if> >
				              			<label for="markNum_${status.index }"><span></span>${data.pass_time_nm }</label>
									</p>
								</c:forEach>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="passTimeUseYn" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						-->
					</div>
					<!--// 오른쪽 컨텐츠 -->
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
<script>
$(document).ready(function() {
	$('#secretBatchOff').on('click', function() {
		$.ajax({
			url: HT_APP_PATH + '/api/test/batch/off',
		}).done(function(data) {
			alert("BATCH OFF");
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: BATCH OFF: ', textStatus);
		});;
	});
	$('#secretBatchOn').on('click', function() {
		$.ajax({
			url: HT_APP_PATH + '/api/test/batch/on',
		}).done(function(data) {
			alert("BATCH ON");
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: BATCH ON: ', textStatus);
		});;
	});
	$('#secretBatchList').on('click', function() {
		$.ajax({
			url: HT_APP_PATH + '/api/test/batch',
		}).done(function(data) {
			;
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: BATCH ON: ', textStatus);
		});;
	});
});
</script>
</body>
</html>
