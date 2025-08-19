<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<div class="inner">
		<!-- left_area -->
		<form id="codePopForm" name="codePopForm" method="post" action="">
		<input type="hidden" name="updateChk" value="${updateChk}" />
			<div class="popup_head">
				<h1 class="popup_tit">부서코드 등록</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
			</div>
			<div class="popup_body">
				<table class="tCont setting">
					<caption>부서코드 등록 화면 입니다.</caption>
					<colgroup>
						<col style="width:">
						<col style="width:">
						<col style="width:">
					</colgroup>
					<tbody>
						<tr>
							<th>고유코드 *</th>
							<th>부서명 *</th>
							<th>설명 * </th>
						</tr>
						<tr>
							<td class="textL">
							<c:if test="${updateChk != 'Y' }">
								<input type="text" name="code" value="${codeDetail.cd }" maxlength="4" style="text-transform: uppercase;"/>
							</c:if>
							<c:if test="${updateChk == 'Y' }">
								${codeDetail.cd }
								<input type="hidden" name="code" value="${codeDetail.cd }"/>
							</c:if>
							</td>
						
							
							<td class="textL">
								<input type="text" name="cdNm" value="${codeDetail.cd_nm }" maxlength="10" />
							</td>
							
							<td class="textL">
								<input type="text" name="cdDesc" value="${codeDetail.cd_desc }" maxlength="100" style="width:100%"/>
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