<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>


	<div class="inner">
		<form id="popupForm" name="popupForm" method="post" action="">
			<input type="hidden" name="cstmUid" value="${userGrade.cstm_uid }" />
			<div class="popup_head">
				<h1 class="popup_tit">코끼리 정보 변경</h1>
				<button type="button" class="btn_popup_close">창닫기</button>
			</div>
			<div class="popup_body">
				<table class="tCont setting">
					<caption>코끼리 정보 변경 등록/수정 폼입니다.</caption>
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>
					<tbody>
						<tr>
							<th>고객명</th>
							<td class="textL">${userGrade.cstm_nm }</td>
						</tr>
						<tr>
							<th>상담직원</th>
							<td class="textL">${userGrade.member_nm }</td>
						</tr>
						<tr>
							<th>등록사유</th>
							<td class="textL"><textarea class="form_black" name="gradeMemo">${userGrade.grade_memo }</textarea></td>
						</tr>							
					</tbody>
				</table>
				<div class="popup_btn_area">
					<button type="button" class="btn_black_modify dev_edit_btn"><i></i>등록사유 수정</button>
					<button type="button" class="dev_delete_btn">코끼리 삭제</button>
				</div>
			</div>
		</form>
	</div>