<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
<c:if test="${isMemberVo == true}">
alert('로그인 유효시간이 지났습니다. \n 다시 로그인 하시기 바랍니다.');
location.replace('/happytalk/loginPage');
</c:if>
</script>
<!-- Tablesorter: required -->	
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.default.css' />" />
<script src="<c:url value='/js/jquery.tablesorter.js' />"></script>
<script src="<c:url value='/js/jquery.tablesorter.widgets.js' />"></script>
<script>
	$(function() {
		$.extend( $.tablesorter.defaults, {
			theme: 'default',
			widthFixed: true
		});
		$(".compatibility").tablesorter();
		$("#tCont").tablesorter({sortList: [[1,0]] });
		$("table.options, table.api").tablesorter({widgets:['stickyHeaders']});
	});
</script>
		<div class="popup_head">
			<h1 class="popup_tit">상담직원 변경 요청</h1> 
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text">매니저에게 상담직원 변경을 요청합니다.<br>
			상담직원 변경 사유를 입력해 주세요.</p>
			<div class="popup_input_area">
				<textarea class="form_text" flagTag="CHANGE_COUNSELOR" placeholder="상담직원 변경 요청 사유를 입력하세요."></textarea>
				<button type="button" class="btn_save">요청하기</button>
			</div> 
			<c:if test="${isAutoPermitForRequestChangeCounselor}"><%--
			<c:if test="${!empty availableMemberList}">--%>
			<div class="staff_select_area" data-auto-permit="${isAutoPermitForRequestChangeCounselor}"><%-- 프론트에서 해당 태그 있으면 '자동 변경 승인'으로 간주함 --%>
			<table id="tCont" class="tCont">
				<caption>상담직원 배정표입니다.</caption>
				<colgroup>
					<col style="width: 12%">
					<col style="width: 25%">
					<col style="">
					<col style="width: 16%">
					<col style="width: 16%">
				</colgroup>
				<thead>
					<tr>
						<th>선택</th>
						<th>부서명</th>
						<th>상담직원</th>
						<th>진행중 대화방수</th>
						<th>미접수 대화방수</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="member" items="${availableMemberList}"
						varStatus="i">
						<tr>
							<td class="memberUid"><input type="radio"
								class="radio_18 no_text" id="staff_select_${i.index}"
								name="staff_select"
								value="<c:out value="${member.member_uid}" />" /> <label
								for="staff_select_${i.index}"><span></span></label></td>
							<td><c:out value="${member.depart_nm}" /></td>
							<td class="memberName"><c:out value="${member.name}" /></td>
							<td><c:out value="${member.counseling_count}" /></td>
							<td><c:out value="${member.assigned_count}" /></td>
						</tr>
					</c:forEach>
					<%--
						<tr>
							<td class="memberUid">
								<input type="radio" class="radio_18 no_text" id="depart_select" name="staff_select" value="<c:out value="${counterPartDepartCd}" />" />
								<label for="depart_select"><span></span></label>
							</td>
							<td class="memberName"><c:out value="${counterPartDepartNm}" /></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>--%>
				</tbody>
			</table>
		</div>
			</c:if>
		</div>