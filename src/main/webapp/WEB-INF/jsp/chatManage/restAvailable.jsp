<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
			<table class="tCont">
				<caption>상담직원 배정표입니다.</caption>
				<colgroup>
					<col style="width:10%">
						<col style="width:25%">
						<col style="">
						<col style="width:25%">
						<col style="width:25%">
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
				<c:forEach var="member" items="${availableMemberList}" varStatus="i">
					<tr>
						<td class="memberUid">
							<input type="radio" class="radio_18 no_text" id="staff_select_${i.index}" name="staff_select" value="<c:out value="${member.member_uid}" />" />
							<label for="staff_select_${i.index}"><span></span></label>
						</td>
						<td><c:out value="${member.depart_nm}" /></td>
						<td class="memberName"><c:out value="${member.name}" /></td>
						<td><c:out value="${member.counseling_count}" /></td>
						<td><c:out value="${member.assigned_count}" /></td>
					</tr>
				</c:forEach><%--
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
			<div class="btn_popup_area">
				<button type="button" class="btn_popup_ok btn_save">확인</button>
				<button type="button" class="btn_popup_ok btn_cancel">취소</button>
			</div>