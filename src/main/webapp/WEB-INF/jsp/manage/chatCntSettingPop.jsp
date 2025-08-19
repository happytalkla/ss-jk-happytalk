<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

	<div class="inner" style="width: 450px; height: 450px;">
		<div class="popup_head">
			<h1 class="popup_tit">
				상담직원 별 최대 상담 건수 설정
			</h1>
			<button type="button" class="btn_popup_close dev-btn_popup2_close">창닫기</button>
		</div>
		<div class="popup_body">
			<form id="chatCntSettingForm" name="chatCntSettingForm" method="post" action="">
				<input type="hidden" name="selDateStartTime" value="" />
				<table class="tCont">
					<caption>상담직원 배정 등록 표입니다.</caption>
					<colgroup>
						<col style="width:8%">
						<col style="width:25%">
						<col style="width:25%">
						<col style="">
					</colgroup>
					<thead>
						<tr>
							<th>
								<input type="checkbox" class="checkbox_18 notext" name="checkNumAll" id="checkNumAll" value="" />
								<label for="checkNumAll"><span></span></label>
							</th>
							<th>상담직원</th>
							<th>담당매니저</th>
							<th>최대건수</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="data" items="${counselorHoliday}" varStatus="status">
							<tr>
								<td>
									<c:if test="${data.member_uid == 'A'}"><span style="display: none;"></c:if>
									<input type="checkbox" class="checkbox_18 notext" name="checkNumArr" value="" id="checkNumArr_${status.count-1}" >
									<label for="checkNumArr_${status.count-1}"><span></span></label>
									<c:if test="${data.member_uid == 'A'}"></span></c:if>
								</td>
								<td>
									${data.name}
									<input type="hidden" name="memberUid" value="${data.member_uid}">
									<input type="hidden" name="counselorName" value="${data.name}">
									<input type="hidden" name="counselorId" value="${data.id}">
								</td>
								<td>
									${data.manager_name}
								</td>
								<td>
									<div align="center"><input type="text" name="cnsrMaxCnt" id="cnsrMaxCnt" style="width :50%;" value="${data.cnsr_max_cnt}">
										<button type="button" class="btn_go_insert"  name="updateBtn" style="width :40%;"><i></i>적용</button>
									</div>
								</td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
	</div>
