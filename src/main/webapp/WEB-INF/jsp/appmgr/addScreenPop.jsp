<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<div class="inner">
		<!-- left_area -->
		<form id="screenPopForm" name="screenPopForm" method="post" action="">
		<input type="hidden" name="updateChk" value="${updateChk}" />
			<div class="popup_head">
				<h1 class="popup_tit">화면 번호 등록</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
			</div>
			<div class="popup_body">
				<table class="tCont setting">
					<caption>화면 번호 등록 화면 입니다.</caption>
					<colgroup>
						<col style="width:">
						<col style="width:">
						<col style="width:">
					</colgroup>
					<tbody>
						<tr>
							<th>화면번호 *</th>
							<th>화면명 *</th>
							<th>화면아이디 * </th>
						</tr>
						<tr>
							<td class="textL">
							<c:if test="${updateChk != 'Y' }">
								<input type="text" name="screenNum" value="${screenDetail.screen_num }" maxlength="4" />
							</c:if>
							<c:if test="${updateChk == 'Y' }">
								${screenDetail.screen_num }
								<input type="hidden" name="screenNum" value="${screenDetail.screen_num }"/>
							</c:if>
							</td>
						
							
							<td class="textL">
								<input type="text" name="screenName" value="${screenDetail.screen_name }" maxlength="50" />
							</td>
							
							<td class="textL">
								<input type="text" name="screenId" value="${screenDetail.screen_id }" maxlength="12" />
							</td>
						</tr>						
					</tbody>
				</table>
				<div class="popup_btn_area">
					<button type="button" class="btn_staff_plus dev_insert_btn">등록</button>
					<button type="button" class="btn_popup_close dev_cancel_pop">취소</button>			
				</div>
			</div>
		</form>
		<!--// left_area -->
	</div>