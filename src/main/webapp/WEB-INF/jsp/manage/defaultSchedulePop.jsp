<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">
				기본 스케줄 관리 (${sessionVo.departNm})
			</h1>
			<button type="button" class="btn_popup_close dev-btn_popup2_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text"><b><!--<c:if test="${dateCheck < 0 || workMap.work_yn eq 'N' }">상담직원 상태 변경 불가상태</c:if>--></b></p>
			<form id="defaultScheduleForm" name="defaultScheduleForm" method="post" action="">
				<input type="hidden" name="departCd" id="departCd" value="${departCd}" /><%--
				<input type="hidden" name="flag" id="flag" value="Y" />--%>
				<input type="hidden" name="schYear" value="" />
				<input type="hidden" name="schMonth" value="" />
				<div class="box_body">
					<ul class="dot_list">
						<li>
							<span class="tit">평일</span>
							<div class="form_area">
								<input type="hidden" name="weekStartTime" value="" />
								<input type="hidden" name="weekEndTime" value="" />
								<select name="weekStartTime1">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="23" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.weekday_start_time3 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>시
								<select name="weekStartTime2">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="59" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.weekday_start_time4 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>분
								~
								<select name="weekEndTime1">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="23" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.weekday_end_time3 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>시
								<select name="weekEndTime2">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="59" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.weekday_end_time4 eq time }">selected="selected"</c:if> >${time }</option>
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
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="23" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sat_start_time3 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>시
								<select name="satStartTime2">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="59" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sat_start_time4 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>분
								~
								<select name="satEndTime1">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="23" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sat_end_time3 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>시
								<select name="satEndTime2">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="59" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sat_end_time4 eq time }">selected="selected"</c:if> >${time }</option>
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
									<input type="hidden" name="sunWorkYn" class="dev-switch-input-value" value="${workTime.sat_work_yn }">
								</label>
							</div>
							<div class="form_area" style="width:calc(100% - 150px);">
								<input type="hidden" name="sunStartTime" value="" />
								<input type="hidden" name="sunEndTime" value="" />
								<select name="sunStartTime1">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="23" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sun_start_time3 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>시
								<select name="sunStartTime2">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="59" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sun_start_time4 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>분
								~
								<select name="sunEndTime1">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="23" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sun_end_time3 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>시
								<select name="sunEndTime2">
									<option selected="selected" value="00">00</option>
									<c:forEach var="data" begin="1" end="59" >
										<c:set var="time" value="${data }" />
										<c:if test="${data < 10 }"><c:set var="time" value="0${data }" /></c:if>
										<option value="${time }" <c:if test="${workTime.sun_end_time4 eq time }">selected="selected"</c:if> >${time }</option>
									</c:forEach>
								</select>분
							</div>
						</li>
					</ul>
				</div>
			</form>
			<div class="btn_popup_area">
				<button type="button" id="insertSchedule2" class="btn_popup_ok">확인</button>
			</div>
		</div>
	</div>
