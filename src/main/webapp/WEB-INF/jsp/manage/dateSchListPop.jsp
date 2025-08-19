<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">
				상담직원 배정
				${fn:substring(param.schDate, 0, 4)}-${fn:substring(param.schDate, 4, 6)}-${fn:substring(param.schDate, 6, 8)}
				(${fn:substring(workMap.start_time, 0, 2)}:${fn:substring(workMap.start_time, 2, 4)} ~ ${fn:substring(workMap.end_time, 0, 2)}:${fn:substring(workMap.end_time, 2, 4)})
				<script>
					if($("#unsocialAcceptYn").val() == "Y"){
						$(".popup_tit").append(" - 근무시간 외 접수가능");
						}
				</script>
			</h1>
			<button type="button" class="btn_popup_close dev-btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text"><b><c:if test="${dateCheck < 0 || workMap.work_yn eq 'N'}">상담직원 상태 변경 불가상태</c:if></b></p>
			<form id="cnsrHolidayForm" name="cnsrHolidayForm" method="post" action="">
				<input type="hidden" name="selDateStartTime" value="${workMap.start_time}" />
				<input type="hidden" name="selDateEndTime" value="${workMap.end_time}" />
				<table class="tCont">
					<caption>상담직원 배정 등록 표입니다.</caption>
					<colgroup>
						<col style="width:4%">
						<col style="width:12%">
						<col style="width:12%">
						<col style="width:15%">
						<col style="width:11%">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th>
								<input type="checkbox" class="checkbox_18 notext" name="checkNumAll" id="checkNumAll" value="" > 
								<label for="checkNumAll"><span></span></label>
							</th>
							<th>상담직원명</th>
							<th>담당매니저</th>
							<th>상태</th>
							<th>개별설정</th>
							<th>근무시간</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="data" items="${counselorHoliday}" varStatus="status">
							<tr>
								<td>
									<c:if test="${data.member_uid == 'A'}"><span style="display: none;"></c:if>
									<input type="checkbox" class="checkbox_18 notext" name="checkNumArr" value="" id="checkNumArr_${status.count-1}" />
									<label for="checkNumArr_${status.count-1}"><span></span></label>
									<c:if test="${data.member_uid == 'A'}"></span></c:if>
								</td>
								<td>
									${data.name}
									<input type="hidden" name="memberUid" value="${data.member_uid}">
									<input type="hidden" name="schDate" value="${data.sch_date}">
									<input type="hidden" name="counselorName" value="${data.name}">
									<input type="hidden" name="counselorId" value="${data.id}">
								</td>
								<td>
									${data.manager_name }
								</td>
								<td>
									<label class="switch w90">
										<input class="switch-input dev-switch-input" type="checkbox" <c:if test="${data.cns_possible_yn eq 'Y'}">checked</c:if> <c:if test="${dateCheck < 0 || workMap.work_yn eq 'N'}">disabled</c:if>>
										<span class="switch-label" data-on="상담가능" data-off="상담불가" ></span> 
										<span class="switch-handle"></span>
										<input type="hidden" name="cnsPossibleYn" class="switch-input-value" value="${data.cns_possible_yn}" />
									</label>
								</td>
								<td>
									<c:choose>
										<c:when test="${data.work_status_cd eq 'W'}">
											상담가능
										</c:when>
										<c:when test="${data.work_status_cd eq 'X'}">
										</c:when>
										<c:otherwise>
											<span style="color:red;">개별상담불가</span>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<select name="startTime1" <c:if test="${dateCheck < 0 || workMap.work_yn eq 'N'}">disabled</c:if>>
										<option selected="selected" value="00">00</option>
										<c:forEach var="item" begin="1" end="23" >
											<c:set var="time" value="${item}" />
											<c:if test="${item < 10}"><c:set var="time" value="0${item}" /></c:if>
											<option value="${time}" <c:if test="${data.start_time1 eq time}">selected</c:if> >${time}</option>
										</c:forEach>
									</select>시
									<select name="startTime2" <c:if test="${dateCheck < 0 || workMap.work_yn eq 'N'}">disabled</c:if>>
										<option selected="selected" value="00">00</option>
										<c:forEach var="item" begin="1" end="59" >
											<c:set var="time" value="${item}" />
											<c:if test="${item < 10}"><c:set var="time" value="0${item}" /></c:if>
											<option value="${time}" <c:if test="${data.start_time2 eq time}">selected</c:if> >${time}</option>
										</c:forEach>
									</select>분
									~
									<select name="endTime1" <c:if test="${dateCheck < 0 || workMap.work_yn eq 'N'}">disabled</c:if>>
										<option selected="selected" value="00">00</option>
										<c:forEach var="item" begin="1" end="23" >
											<c:set var="time" value="${item}" />
											<c:if test="${item < 10}"><c:set var="time" value="0${item}" /></c:if>
											<option value="${time}" <c:if test="${data.end_time1 eq time}">selected</c:if> >${time}</option>
										</c:forEach>
									</select>시
									<select name="endTime2" <c:if test="${dateCheck < 0 || workMap.work_yn eq 'N'}">disabled</c:if>>
										<option selected="selected" value="00">00</option>
										<c:forEach var="item" begin="1" end="59" >
											<c:set var="time" value="${item}" />
											<c:if test="${item < 10}"><c:set var="time" value="0${item}" /></c:if>
											<option value="${time}" <c:if test="${data.end_time2 eq time}">selected</c:if> >${time}</option>
										</c:forEach>
									</select>분
								
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<p class="popup_text_bottom">* 상담직원이 상담하기 페이지에서 개별 상담 불가 설정되면 근무시간이여도 상담이 배정되지 않습니다.</p>
			<p class="popup_text_bottom" style="color:red;">* 상담직원별 근무시간은 근무시간 외 접수 가능한 경우에만 근무시간 이외의 시간으로 설정 가능합니다. 근무시간외 접수가 불가능할 경우는 근무시간 내에서만 시간을 조절할 수 있습니다.</p>
			<div class="btn_popup_area">
				<button type="button" class="btn_popup_ok dev-btn_popup_ok">닫기</button>
			</div>
		</div>
	</div>
