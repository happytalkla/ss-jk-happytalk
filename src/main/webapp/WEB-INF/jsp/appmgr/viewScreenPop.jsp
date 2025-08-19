<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<div class="inner">
		<!-- left_area -->
		<form id="screenViewPopForm" name="screenViewPopForm" method="post" action="">
				<input type="hidden" name="screenNum" value="${screenDetail.screen_num}" />
			<div class="popup_head">
				<h1 class="popup_tit">용어 사전 등록</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
			</div>
				<div class="popup_btn_area" style="text-align: right;">
					<button type="button" class="dev_delete_pop" style="margin:0 20px;">삭제</button>	
				</div>			
			<div class="popup_body">
				<table class="tCont setting">
					<caption>화면 코드 등록 화면 입니다.</caption>
					<colgroup>
						<col style="width:">
						<col style="width:">
						<col style="width:">
					</colgroup>
					<tbody>
						<tr>
							<th>화면번호 *</th>
							<th>화면이름 *</th>
							<th>화면 ID *</th>
							
														
						</tr>					
						<tr>
							<td class="textL">${screenDetail.screen_num }</td>
							<td class="textL"><c:out value="${screenDetail.screen_name }"/></td>
							<td class="textL"><c:out value="${screenDetail.screen_id }"/></td>
						</tr>
					</tbody>
				</table>
				<div class="popup_btn_area">
					<button type="button" class="btn_staff_plus dev_openEditPop">수정</button>
					<button type="button" class="btn_popup_close dev_close_pop">닫기</button>			
				</div>
			</div>
		</form>
		<!--// left_area -->
	</div>