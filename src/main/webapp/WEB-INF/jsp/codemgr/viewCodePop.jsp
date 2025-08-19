<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<div class="inner">
		<!-- left_area -->
		<form id="codeViewPopForm" name="codeViewPopForm" method="post" action="">
				<input type="hidden" name="cd" value="${codeDetail.cd}" />
			<div class="popup_head">
				<h1 class="popup_tit">부서 코드 등록</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
			</div>
				<div class="popup_btn_area" style="text-align: right;">
					<button type="button" class="dev_delete_pop" style="margin:0 20px;">삭제</button>	
				</div>			
			<div class="popup_body">
				<table class="tCont setting">
					<caption>부서 코드 등록 화면 입니다.</caption>
					<colgroup>
						<col style="width:">
						<col style="width:">
						<col style="width:">
					</colgroup>
					<tbody>
						<tr>
							<th>고유코드 *</th>
							<th>코드명칭 *</th>
							<th>설명 *</th>
							
														
						</tr>					
						<tr>
							<td class="textL">${codeDetail.cd }</td>
							<td class="textL"><c:out value="${codeDetail.cd_nm }"/></td>
							<td class="textL"><c:out value="${codeDetail.cd_desc }"/></td>
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