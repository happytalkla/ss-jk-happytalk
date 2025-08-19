<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<div class="inner">
		<!-- left_area -->
		<form id="termViewPopForm" name="termViewPopForm" method="post" action="">
				<input type="hidden" name="termNum" value="${termDetail.term_num}" />
			<div class="popup_head">
				<h1 class="popup_tit">용어 사전 등록</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
			</div>
				<div class="popup_btn_area" style="text-align: right;">
					<button type="button" class="dev_delete_pop" style="margin:0 20px;">삭제</button>	
				</div>			
			<div class="popup_body">
				<table class="tCont setting">
					<caption>용어 사전 등록 화면 입니다.</caption>
					<colgroup>
						<col style="width:10%">
						<col style="width:">
					</colgroup>
					<tbody>
						<tr>
							<th>구분 *</th>
							<td class="textL"><c:out value="${termDetail.term_div_nm }"/></td>
							<th>ID</th>
							<td class="textL">${termDetail.term_num }</td>							
						</tr>					
						<tr>
							<th>제목 *</th>
							<td colspan="3" class="textL"><c:out value="${termDetail.title }"/></td>
						</tr>
						<tr>
							<th>내용 *</th>
							<td colspan="3" style="white-space: pre-line; text-align:left;"><c:out value="${termDetail.cont }"/></td>
						</tr>
						<tr>
							<th>태그</th>
							<td colspan="3" class="textL"><c:out value="${termDetail.term_tag }"/></td>
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