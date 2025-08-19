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

	<script>

		var nowYear = "${nowYear}";
		var nowMonth = "${nowMonth}";
		var nowDay = "${nowDay}";

		$(document).ready(function() {
		});

		// 휴일 등록
		$(document).on("click", "#newHolidayForm #insertHoliday", function() {
			if($("#newHolidayForm input[name='holidayDate']").val() == ""){
				alert("휴일을 입력해 주세요.");
				return false;
			}

			var holidayDate = $("#newHolidayForm input[name='holidayDate']").val();
			var holidayDateArr = holidayDate.split("-");

			var nowDateCompare = new Date(nowYear, parseInt(nowMonth)-1, nowDay);
			var holidayDateCompare = new Date(holidayDateArr[0], parseInt(holidayDateArr[1])-1, holidayDateArr[2])

			if(nowDateCompare.getTime() >= holidayDateCompare.getTime()){
				alert("휴일은 내일 날짜부터 입력해 주세요.");
				return false;
			}

			if($("#newHolidayForm input[name='memo']").val() == ""){
				alert("메모를 입력해 주세요.");
				return false;
			}

			//var workYn = $("#newHolidayForm input[name='workYn']").val();
/*
			var startTime1 = $("#newHolidayForm [name='startTime1'] option:selected").val();
			var startTime2 = $("#newHolidayForm [name='startTime2'] option:selected").val();
			var endTime1 = $("#newHolidayForm [name='endTime1'] option:selected").val();
			var endTime2 = $("#newHolidayForm [name='endTime2'] option:selected").val();

			var startTime = "0000";
			var endTime = "2359";

			//if(workYn == "Y"){
			if(startTime*1 >= endTime*1){
				alert("근무 종료시간이 시작시간보다 커야 합니다.");
				return false;
			}
			//}
			*/
///시간 선택 입력창 제거 후 고정처리 ---qq
			var startTime = "0000";
			var endTime = "2359";
			
			$("#newHolidayForm input[name='startTime']").val(startTime);
			$("#newHolidayForm input[name='endTime']").val(endTime);

			/*
			* IPCC_MCH 휴일 등록 오류 수정
			*/
			var tempChannel = $("#channel option:selected").val();
			$("#newHolidayForm input[name='channel']").val(tempChannel);
			
			$.ajax({
				url : "<c:url value='/manage/insertHoliday' />",
				data : $("#newHolidayForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});

		});


		$(document).on("change", "#holidayForm #checkAll", function() {
			var chk = $(this).is(":checked");
			if(chk){
				$("#holidayForm input[name='delHolidayDate']").prop("checked", true);
			}else{
				$("#holidayForm input[name='delHolidayDate']").prop("checked", false);
			}
		});


		// 휴일 삭제
		$(document).on("click", "#holidayForm #deleteHoliday", function() {
			// 선택된 체크 박스 확인
			var checkedLen = $("#holidayForm input[name='delHolidayDate']:checked").length;
			if(checkedLen < 1){
				alert("삭제할 휴일을 선택해 주세요.");
				return false;
			}

			$.ajax({
				url : "<c:url value='/manage/deleteHoliday' />",
				data : $("#holidayForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});

		});


		// 이전달
		$(document).on("click", "#searchForm #searchSchPrev", function() {
			var year = $("#searchForm input[name='schYear']").val();
			var month = $("#searchForm input[name='schMonth']").val();

			month = month*1 - 1;

			if(month == 0){
				year = year*1 - 1;
				month = 12;
			}

			if(month < 10){
				month = "0" + month;
			}

			$("#searchForm input[name='schYear']").val(year);
			$("#searchForm input[name='schMonth']").val(month);

			goSearch();
		});

		// 다음달
		$(document).on("click", "#searchForm #searchSchNext", function() {
			var year = $("#searchForm input[name='schYear']").val();
			var month = $("#searchForm input[name='schMonth']").val();

			month = month*1 + 1;

			if(month == 13){
				year = year*1 + 1;
				month = 1;
			}

			if(month < 10){
				month = "0" + month;
			}

			$("#searchForm input[name='schYear']").val(year);
			$("#searchForm input[name='schMonth']").val(month);

			goSearch();
		});

		// 이번달
		$(document).on("click", "#searchForm #searchSchNow", function() {
			$("#searchForm input[name='schYear']").val("");
			$("#searchForm input[name='schMonth']").val("");

			goSearch();
		});

		function goSearch(){
			$("#searchForm").attr("action", "<c:url value='/manage/workManage' />");
			$("#searchForm").submit();
		}

		// 부서별 스케줄 등록 (기본 스케쥴 관리 -> 등록)
		$(document).on("click", "#insertSchedule2", function() {
			var year = $("#searchForm input[name='schYear']").val();
			var month = $("#searchForm input[name='schMonth']").val();
			$("#defaultScheduleForm input[name='schYear']").val(year);
			$("#defaultScheduleForm input[name='schMonth']").val(month);			
			$.ajax({
				url : "<c:url value='/manage/insertSchedule'/>",
				data : $("#defaultScheduleForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 스케줄 등록
		$(document).on("click", "#insertSchedule", function() {
			$.ajax({
				url : "<c:url value='/manage/insertSchedule' />",
				data : $("#searchForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							goSearch();
						}, 500);
					} else {
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 스케줄 삭제
		$(document).on("click", "#deleteSchedule", function() {
			$.ajax({
				url : "<c:url value='/manage/deleteSchedule' />",
				data : $("#searchForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});


		$(document).on("change", "#newHolidayForm .dev-switch-input", function() {
			if($(this).prop("checked")){
				$(this).closest("label").find(".dev-switch-input-value").val("Y");
			}else{
				$(this).closest("label").find(".dev-switch-input-value").val("N");
			}
		});

		$(document).on("change", "#defaultScheduleForm .dev-switch-input", function() {
			if($(this).prop("checked")){
				$(this).closest("label").find(".dev-switch-input-value").val("Y");
			}else{
				$(this).closest("label").find(".dev-switch-input-value").val("N");
			}
		});

		/************* dateSchListPop 관련 스크립트 *************/

		// 특정일의 상담직원별 근무 설정 팝업 오픈
		$(document).on("click", ".dev-openCounselorHoliday", function() {
			$.ajax({
				url : "<c:url value='/manage/selectCounselorHoliday' />",
				data : {"schDate" : $(this).attr("data-selDate"), "channel" : $("#channel option:selected").val()},
				type : "post",
				success : function(result) {
					$("#counselorHoliday").html(result);
					$("#counselorHoliday").show();
					$('#cnsrHolidayForm tbody tr:gt(0) select').attr('disabled', 'disabled');
				},
				complete : function() {
				}
			});
		});

		// 특정일의 상담직원별 근무 설정 팝업 닫기
		$(document).on("click", ".dev-btn_popup_close", function() {
			$("#counselorHoliday").hide();
			$("#counselorHoliday").html("");
			// 아래 함수는 무조건 생성
			goSearch();
		});
		$(document).on("click", ".dev-btn_popup_ok", function() {
			$("#counselorHoliday").hide();
			$("#counselorHoliday").html("");
			// 아래 함수는 무조건 생성
			goSearch();
		});

		//  개별 상담수 설정
		$(document).on("click", ".dev-chat-cnt-setting", function() {
			$.ajax({
				url : "<c:url value='/manage/selectChatCntSetting' />",
				type : "post",
				success : function(result) {
					$("#chatCntSetting").html(result);
					$("#chatCntSetting").show();
				},
				complete : function() {
				}
			});
		});

		// 개별 상담수 설정
		$(document).on("click", ".dev-btn_popup2_close", function() {
			$("#chatCntSetting").hide();
			$("#chatCntSetting").html("");
			// 아래 함수는 무조건 생성
			//goSearch();
		});

		// 개별 상담수 update
		$(document).on("click", "#chatCntSettingForm [name='updateBtn']", function() {
			var memberUid =$(this).closest("tr").find("input[name='memberUid']").val();
			var cnsrMaxCnt =$(this).closest("tr").find("input[name='cnsrMaxCnt']").val();
			if (memberUid !='A') { //일괄적용 이외일경우 바로 update	
				fn_updateChatCntSetting(memberUid, cnsrMaxCnt);
			}else {                //일괄적용일 경우
				$("#chatCntSettingForm input[name='checkNumArr']").each(function(index, item) {
					if (item.checked && index != 0) {
						$("#chatCntSettingForm [name='cnsrMaxCnt']:eq("+index+")").val(cnsrMaxCnt);
						$("#chatCntSettingForm [name='updateBtn']:eq("+index+")").trigger("click");
						
					}
				});						
			}
		});

		function fn_updateChatCntSetting(memberUid, cnsrMaxCnt){
			$.ajax({
				url : "<c:url value='/manage/updateChatCntSetting' />",
				data : {"memberUid":memberUid, "cnsrMaxCnt":cnsrMaxCnt },
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
				}
			});
		}

		//  기본 스케쥴 관리
		$(document).on("click", ".dev-default-schedule", function() {
			$.ajax({
				url : "<c:url value='/manage/selectDefaultSchedule' />",
				data : {"schDate" : $(this).attr("data-selDate")},
				type : "post",
				success : function(result) {
					$("#defaultSchedule").html(result);
					$("#defaultSchedule").show();
				},
				complete : function() {
				}
			});
		});

		// 기본 스케쥴 관리
		$(document).on("click", ".dev-btn_popup3_close", function() {
			$("#defaultSchedule").hide();
			$("#defaultSchedule").html("");
			// 아래 함수는 무조건 생성
			//goSearch();
		});
		$(document).on("click", ".dev-btn_popup3_ok", function() {
			$("#defaultSchedule").hide();
			$("#defaultSchedule").html("");
			// 아래 함수는 무조건 생성
			goSearch();
		});

		// 상담직원 근무여부 설정
		$(document).on("change", "#cnsrHolidayForm .dev-switch-input", function() {
			var memberUid =$(this).closest("tr").find("input[name='memberUid']").val();
			if (memberUid !='A') { //일괄적용 이외일경우 바로 update
				fn_updateWorkYn($(this));
			}else {                //일괄적용일 경우
				$("#cnsrHolidayForm input[name='checkNumArr']").each(function(index, item) {
					if (item.checked && index != 0) {
						var chk= $("#cnsrHolidayForm .dev-switch-input:eq(0)").is(":checked");
						if (chk !=$("#cnsrHolidayForm .dev-switch-input:eq("+index+")").is(":checked")) {
							$("#cnsrHolidayForm .dev-switch-input:eq("+index+")").trigger("click");
						}
						
					}
				});
			}
		});

		function fn_updateWorkYn(obj){
			var cnsPossibleYn;
			if(obj.prop("checked")){
				cnsPossibleYn = "Y";
			}else{
				cnsPossibleYn = "N";
			}

			obj.closest("label").find(".dev-switch-input-value").val(cnsPossibleYn);
			var memberUid =obj.closest("tr").find("input[name='memberUid']").val();
			var schDate = obj.closest("tr").find("input[name='schDate']").val();   
			var counselorName = obj.closest("tr").find("input[name='counselorName']").val();
			var counselorId = obj.closest("tr").find("input[name='counselorId']").val();

			$.ajax({
				url : "<c:url value='/manage/updateCnsrHoliday' />",
				data : {"updateType":"workYn", "memberUid":memberUid, "schDate":schDate, "cnsPossibleYn":cnsPossibleYn, "counselorName":counselorName, "counselorId":counselorId},
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
				}
			});
		}

		// 상담직원 근무시간 설정
		$(document).on("change", "#cnsrHolidayForm [name='startTime1']", function() {
			var memberUid = $(this).closest("tr").find("input[name='memberUid']").val();
			var allValue = $(this).val();

			if (memberUid !=='A') { // 개별적용
				fn_updateCnsrWorkTime($(this));
			} else { // 선택적용
				$("#cnsrHolidayForm input[name='checkNumArr']").each(function(index, item) {
					if (item.checked && index !== 0) {
						$("#cnsrHolidayForm [name='startTime1']:eq("+index+")").val(allValue);
						$("#cnsrHolidayForm [name='startTime1']:eq("+index+")").trigger("change");
					}
				});
				alertVal="N";
			}
		});
		$(document).on("change", "#cnsrHolidayForm [name='startTime2']", function() {
			var memberUid =$(this).closest("tr").find("input[name='memberUid']").val();
			var allValue = $(this).val();

			if (memberUid !=='A') { // 개별적용
				fn_updateCnsrWorkTime($(this));
			} else { // 선택적용
				$("#cnsrHolidayForm input[name='checkNumArr']").each(function(index, item) {
					if (item.checked && index !== 0) {
						$("#cnsrHolidayForm [name='startTime2']:eq("+index+")").val(allValue);
						$("#cnsrHolidayForm [name='startTime2']:eq("+index+")").trigger("change");
					}
				});
				alertVal="N";
			}
		});
		$(document).on("change", "#cnsrHolidayForm [name='endTime1']", function() {
			var memberUid =$(this).closest("tr").find("input[name='memberUid']").val();
			var allValue = $(this).val();

			if (memberUid !=='A') { // 일괄적용 이외일경우 바로 update	
				fn_updateCnsrWorkTime($(this));
			} else { // 일괄적용일 경우
				$("#cnsrHolidayForm input[name='checkNumArr']").each(function(index, item) {
					if (item.checked && index !== 0) {
						$("#cnsrHolidayForm [name='endTime1']:eq("+index+")").val(allValue);
						$("#cnsrHolidayForm [name='endTime1']:eq("+index+")").trigger("change");
					}
				});
				alertVal="N";
			}
		});
		$(document).on("change", "#cnsrHolidayForm [name='endTime2']", function() {
			var memberUid =$(this).closest("tr").find("input[name='memberUid']").val();
			var allValue = $(this).val();
			
			if (memberUid !=='A') { // 일괄적용 이외일경우 바로 update	
				fn_updateCnsrWorkTime($(this));
			} else { // 일괄적용일 경우
				$("#cnsrHolidayForm input[name='checkNumArr']").each(function(index, item) {
					if (item.checked && index !== 0) {
						$("#cnsrHolidayForm [name='endTime2']:eq("+index+")").val(allValue);
						$("#cnsrHolidayForm [name='endTime2']:eq("+index+")").trigger("change");
					}
				});
				alertVal="N";
			}
		});

		var alertVal="N";
		// 상담직원 근무시간 설정
		function fn_updateCnsrWorkTime(obj){
			var startTime1 = obj.closest("tr").find("[name='startTime1'] option:selected").val();
			var startTime2 = obj.closest("tr").find("[name='startTime2'] option:selected").val();
			var endTime1 = obj.closest("tr").find("[name='endTime1'] option:selected").val();
			var endTime2 = obj.closest("tr").find("[name='endTime2'] option:selected").val();

			var startTime = startTime1 + startTime2;
			var endTime = endTime1 + endTime2;

			if(endTime*1 > 0){
				if(startTime*1 >= endTime*1){
					if (alertVal=="N") alert("근무 종료시간이 시작시간보다 커야 합니다.");
					alertVal="Y";
					return false;
				}
			}

			// 기본 근무시간 체크. 근무외 상담접수 활성일 경우 예외
			var dayStartTime = $("#cnsrHolidayForm [name='selDateStartTime']").val();
			var dayEndTime = $("#cnsrHolidayForm [name='selDateEndTime']").val();
			var unsocialAcceptYn = $("#unsocialAcceptYn").val();

			var layerMessage;
			if(dayStartTime.length == 4 && dayEndTime.length == 4){
				layerMessage = "근무 시간은 " + dayStartTime.substr(0,2) + ":" + dayStartTime.substr(2,2) + " ~ " + dayEndTime.substr(0,2) + ":" + dayEndTime.substr(2,2) + "입니다.";
			}else{
				layerMessage = "근무 시간을 정확하게 입력해 주세요.";
			}

			if(startTime*1 < dayStartTime*1 && unsocialAcceptYn == "N"){
				if (alertVal=="N") alert(layerMessage);
				alertVal="Y";
				return false;
			}

			if(endTime*1 > dayEndTime*1 && unsocialAcceptYn == "N"){
				if (alertVal=="N") alert(layerMessage);
				alertVal="Y";
				return false;
			}

			var memberUid = obj.closest("tr").find("input[name='memberUid']").val();
			var schDate = obj.closest("tr").find("input[name='schDate']").val();   			
			var counselorName = obj.closest("tr").find("input[name='counselorName']").val();
			var counselorId = obj.closest("tr").find("input[name='counselorId']").val();
			

			$.ajax({
				url : "<c:url value='/manage/updateCnsrHoliday' />",
				data : {"updateType":"time", "memberUid":memberUid, "schDate":schDate, "startTime":startTime, "endTime":endTime, "counselorName":counselorName, "counselorId":counselorId},
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
				}
			});
		}

		/************* dateSchListPop 관련 스크립트 *************/

		// 휴무일 양식 받기 버튼 클릭
		$(document).on("click", "#holidayForm .dev_temp_down_btn", function() {
			$("#excelForm").submit();
		});

		// 휴무일 일괄 등록
		$(document).on("click", "#holidayForm .dev_all_temp_btn", function() {
			var tplUploadFile = $("#holidayForm [name='manageUploadFile']").val();
			if(tplUploadFile != ""){
				var form = $("#holidayForm")[0];
				var formData = new FormData(form);

				$.ajax({
					url : "<c:url value='/manage/updateManageExcel' />",
					processData : false,
					contentType : false,
					data : formData,
					type : "post",
					success : function(result) {
						if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
							// 성공
							fn_layerMessage(result.rtnMsg);
							setTimeout(function(){
								goSearch();
							}, 500);
						}else{
							fn_layerMessage(result.rtnMsg);
						}
					},
					complete : function() {
						$("#holidayForm [name='manageUploadFile']").val("");
					}
				});
			}else{
				alert("등록할 파일을 선택해 주세요.");
			}
		});

		// 전체선택 클릭
		$(document).on("click", "#cnsrHolidayForm #checkNumAll", function() {
			var chk = $(this).is(":checked");
			if(chk){
				$("#cnsrHolidayForm input[name='checkNumArr']").prop("checked", true);
			}else{
				$("#cnsrHolidayForm input[name='checkNumArr']").prop("checked", false);
			}
		});

		// 전체선택 클릭
		$(document).on("click", "#chatCntSettingForm #checkNumAll", function() {
			var chk = $(this).is(":checked");
			if(chk){
				$("#chatCntSettingForm input[name='checkNumArr']").prop("checked", true);
			}else{
				$("#chatCntSettingForm input[name='checkNumArr']").prop("checked", false);
			}
		});		

		// 채널변경
		$(document).on("change", "#channel", function() {
			$("#searchForm").attr("action", "<c:url value='/manage/workManage' />");
			$("#searchForm").submit();
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
			<div class="left_content_head">
				<h2 class="sub_tit">휴무일/근무 관리<%--(${member.departCd})--%></h2>
				<!-- <button type="button" class="btn_go_help">도움말</button> -->
				<span class="left_top_text">달력에서 날짜를 클릭하여 상담직원 개개인의 근무여부와 휴무시간을 설정합니다.</span>
			</div>
			<div class="inner">
				<!-- calendar_area --->
				<div class="calendar_area">
					<div class="calendar_head">
						<form id="searchForm" name="searchForm" method="post" action="<c:url value='/manage/workManage' />">
							<div class="left_btn_area">
								<button type="button" class="btn_holiday_plus dev-chat-cnt-setting">개별 상담수 설정</button>
								채널 선택 : <select name="channel" id="channel" style="line-height:100%;height:28px;">
									<!-- IPCC_MCH 채널 추가 및 공통코드 사용 -->
									<c:forEach var="ch" items="${channelList}" >
										<option value="<c:out value="${ch.cd}"/>"<c:if test="${channel == ch.cd}"> selected</c:if>>${ch.cd_nm}</option>
									</c:forEach>
									<%-- IPCC_MCH 채널 추가 --%>
									<%--
									<option value="A"<c:if test="${channel == 'A'}"> selected</c:if>>홈페이지</option>
									<option value="B"<c:if test="${channel == 'B'}"> selected</c:if>>카카오채널</option>
									<option value="C"<c:if test="${channel == 'C'}"> selected</c:if>>O2</option>
									<option value="D"<c:if test="${channel == 'D'}"> selected</c:if>>mPOP</option>
									 --%>
								</select>
								<!-- <button type="button" class="btn_holiday_plus dev-default-schedule" >기본 스케쥴 관리</button> -->
							<h2 class="calendar_tit">${schYear }. ${schMonth }</h2>
							</div>	
							<div class="right_btn_area">

								<button type="button" class="btn_today" id="searchSchNow">Today</button>
								<button type="button" class="btn_prev_date" id="searchSchPrev"><i class="">지난달 보기</i></button>
								<button type="button" class="btn_next_date" id="searchSchNext"><i class="">다음달 보기</i></button>
								<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A'  || sessionVo.memberDivCd eq 'M' )}"><!-- 관리자만 보이는 영역 -->
									<!-- <button type="button" class="btn_day_plus" id="deleteSchedule">스케쥴 삭제</button> -->
								<%-- 	<c:if test="${member.departCd == 'CS'}"> --%>
									<button type="button" class="btn_holiday_plus" id="insertSchedule" style="float:right;">스케쥴 등록</button>
									<%-- </c:if> --%>
								</c:if>
								<input type="hidden" name="schYear" value="${schYear }" />
								<input type="hidden" name="schMonth" value="${schMonth }" />
								<input type="hidden" id="unsocialAcceptYn" value="${unsocialAcceptYn }" />
							</div>
						</form>
					</div>
					<table class="tCalendar">
						<caption>휴무일 근무 관리 달력표입니다.</caption>
						<colgroup>
							<col style="width:14.28571%">
							<col style="width:14.28571%">
							<col style="width:14.28571%">
							<col style="width:14.28571%">
							<col style="width:14.28571%">
							<col style="width:14.28571%">
							<col style="width:14.28571%">
						</colgroup>
						<thead>
							<tr>
								<th>Sun</th>
								<th>Mon</th>
								<th>Tue</th>
								<th>Wed</th>
								<th>Thu</th>
								<th>Fri</th>
								<th>Sat</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="nowMonthYn" value="N" />
							<c:forEach var="data" items="${monthCalendar }" varStatus="status">
								<tr>
									<c:choose>
										<c:when test="${empty data.sun}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var="thisDay" type="number" value="${data.sun}" />
											<c:choose>
												<c:when test="${holidayMap[data.sun].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sun }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sun }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.sun].start_time } ~ ${holidayMap[data.sun].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.sun].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sun }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sun }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.sun].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:when test="${workTime.sun_work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sun }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sun }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.sun_start_time2 } ~ ${workTime.sun_end_time2 })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${workTime.sun_work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sun }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sun }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(일요일)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														${data.sun }
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty data.mon}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var = "thisDay" type = "number" value = "${data.mon}" />
											<c:choose>
												<c:when test="${holidayMap[data.mon].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.mon }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.mon }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.mon].start_time } ~ ${holidayMap[data.mon].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.mon].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.mon }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.mon }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.mon].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.mon }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.mon }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.weekday_start_time2 } ~ ${workTime.weekday_end_time2 })</p>
														</div>
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty data.tue}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var = "thisDay" type = "number" value = "${data.tue}" />
											<c:choose>
												<c:when test="${holidayMap[data.tue].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.tue }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.tue }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.tue].start_time } ~ ${holidayMap[data.tue].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.tue].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.tue }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.tue }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.tue].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.tue }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.tue }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.weekday_start_time2 } ~ ${workTime.weekday_end_time2 })</p>
														</div>
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty data.wed}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var = "thisDay" type = "number" value = "${data.wed}" />
											<c:choose>
												<c:when test="${holidayMap[data.wed].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.wed }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.wed }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.wed].start_time } ~ ${holidayMap[data.wed].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.wed].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.wed }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.wed }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.wed].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.wed }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.wed }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.weekday_start_time2 } ~ ${workTime.weekday_end_time2 })</p>
														</div>
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty data.thu}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var = "thisDay" type = "number" value = "${data.thu}" />
											<c:choose>
												<c:when test="${holidayMap[data.thu].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.thu }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.thu }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.thu].start_time } ~ ${holidayMap[data.thu].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.thu].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.thu }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.thu }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.thu].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.thu }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.thu }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.weekday_start_time2 } ~ ${workTime.weekday_end_time2 })</p>
														</div>
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty data.fri}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var = "thisDay" type = "number" value = "${data.fri}" />
											<c:choose>
												<c:when test="${holidayMap[data.fri].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.fri }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.fri }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.fri].start_time } ~ ${holidayMap[data.fri].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.fri].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.fri }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.fri }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.fri].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.fri }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.fri }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.weekday_start_time2 } ~ ${workTime.weekday_end_time2 })</p>
														</div>
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty data.sat}">
											<c:set var="nowMonthYn" value="N" />
										</c:when>
										<c:otherwise>
											<c:set var="nowMonthYn" value="Y" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${nowMonthYn eq 'Y'}">
											<fmt:parseNumber var = "thisDay" type = "number" value = "${data.sat}" />
											<c:choose>
												<c:when test="${holidayMap[data.sat].work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sat }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sat }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${holidayMap[data.sat].start_time } ~ ${holidayMap[data.sat].end_time })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${holidayMap[data.sat].work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sat }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sat }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(<c:out value="${holidayMap[data.sat].memo }"/>)</p>
														</div>
													</td>
												</c:when>
												<c:when test="${workTime.sat_work_yn eq 'Y' }">
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sat }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sat }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(${workTime.sat_start_time2 } ~ ${workTime.sat_end_time2 })</p>
														</div>
													</td>
												</c:when>
												<c:when test="${workTime.sat_work_yn eq 'N' }">
													<td class="dev-openCounselorHoliday <c:choose><c:when test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sat }'>today</c:when><c:otherwise>holiday</c:otherwise></c:choose>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														<span class="day_text">${data.sat }</span>
														<div class="event_content">
															<p>상담 가능 : <span class="no_blue">${monthSchList[thisDay - 1].cns_possible_cnt }</span></p>
															<p>상담 불가 : <span class="no_red">${monthSchList[thisDay - 1].cns_not_cnt }</span></p>
															<p class="event_time">(토요일)</p>
														</div>
													</td>
												</c:when>
												<c:otherwise>
													<td class="dev-openCounselorHoliday <c:if test='${nowYear eq schYear and nowMonth eq schMonth and nowDay eq data.sat }' >today</c:if>" data-selDate="${monthSchList[thisDay - 1].sel_date }">
														${data.sat }
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td>
											</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</c:forEach>
						</tbody>
					</table>


				</div>
				<!--// calendar_area --->

				<!-- calendar_option_area --->
				<div class="calendar_option_area">
					<div class="option_box" style="margin-bottom:15px;">
						<h3 class="sub_stit">휴무일 리스트</h3>
						<p class="option_info_text">기본적으로 공휴일도 근무일로 설정되어있으므로, 매달 휴일을 반드시 체크하시기 바랍니다.</p>
						<form id="holidayForm" name="holidayForm" method="post" action="<c:url value='/manage/deleteHoliday' />" style="margin:0px;">
							<div class="table_area">
								<div class="table_area_scroll">
	  								<table class="tCont">
	  									<caption>휴무일 리스트 목록입니다.</caption>
	  									<colgroup>
	  										<col style="">
	  										<col style="">
	  										<col style="">
	  									</colgroup>
	  									<thead>
	  										<tr>
	  											<th>
	  												<input type="checkbox" class="checkbox_18 notext" id="checkAll">
	  												<label for="checkAll"><span></span></label>
	  											</th>
	  											<th>휴무일</th>
	  											<!--  <th>근무여부</th> -->
	  											<th>내용</th>
	  										</tr>
	  									</thead>
	  									<tbody>
				              				<c:forEach var="data" items="${holidayList }" varStatus="status">
				              					<tr>
				              						<td>
				              							<input type="checkbox" class="checkbox_18 notext" name="delHolidayDate" value="${data.holiday_date }" id="delHolidayDate_${status.index }" />
				              							<label for="delHolidayDate_${status.index }"><span></span></label>
				              						</td>
				              						<td>
				              							${data.holiday }
				              						</td>
				              							<!--  <td>
				              							${data.work_yn }
				              						</td> -->
				              						<td>
				              							<c:out value="${data.memo }"/>
				              						</td>
				              					</tr>
				              				</c:forEach>
	  									</tbody>
	  								</table>
								</div>
								<div class="table_count">
									<span class="cont_text"><em>${holidayList.size() }</em> count(s)</span>
								</div>
							</div>
							<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
								<div class="btn_left_area tm_disp_none">
									<button type="button" class="btn_holiday_del" id="deleteHoliday"><i></i>휴무일 삭제</button>
								</div>
								<div class="btn_left_area tm_disp_none">
									<input type="file" name="manageUploadFile" accept="application/vnd.ms-excel" onclick="javascript:void(0);" />
								</div>
								<div class="btn_left_area tm_disp_none">
									<button type="button" class="btn_all_temp dev_all_temp_btn"><i></i>휴무일 일괄 등록</button>
									<button type="button" class="btn_temp_dowmload dev_temp_down_btn"><i></i>휴무일 양식 받기</button>
								</div>
							</c:if>
						</form>
					</div>

					<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
						
							<h3 class="sub_stit tm_disp_none">휴무일 등록</h3>
						<div class="table_area">
							<form id="newHolidayForm" name="newHolidayForm" method="post" action="<c:url value='/manage/insertHoliday' />" class="tm_disp_none">
							<input type="hidden" name="startTime" value="" />
							<input type="hidden" name="endTime" value="" />
							<input type="hidden" name="channel" value="" />
								<table>
									<colgroup>
										<col style="">
										<col style="">
									</colgroup>
									<tbody>
										<tr>
											<td>휴무일자</td>
											<td>내용</td>
										</tr>
										<tr>
											<td><input type="text" name="holidayDate" id="holidayDate" value="" readonly="readonly" class="datepicker" style="width:100%;"></td>
											<td><input type="text" name="memo" value="" maxlength="100" style="width:100%;"></td>
										</tr>
										<tr>
											<td colspan="2"><button type="button" class="btn_holiday_plus" id="insertHoliday">등록</button></td>
										</tr>
									</tbody>
								</table>
		            				
							</div>
						</form>
					</c:if>
				</div>
				<!--// calendar_option_area --->
			</div>
		</div>
		<!--// left_area -->
	</div>

<!-- popup:휴무일 세팅 -->
<div class="popup popup_event_setting" style="display:none !important" id="counselorHoliday">
</div>
<div class="popup popup_event_setting" style="display:none !important" id="chatCntSetting">
</div>
<div class="popup popup_event_setting" style="display:none !important" id="defaultSchedule">
</div>



<!--// popup:휴무일 세팅 -->
<form id="excelForm" name="excelForm" method="post" action="<c:url value='/manage/downloadManageExcel' />" style="display:none;">
</form>
<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->
<c:if test="${!isProduction}">
<form id="debug" style="display: none;">
	<input type="hidden" name="profile" value="false" />
</form>
</c:if>
</body>
</html>
