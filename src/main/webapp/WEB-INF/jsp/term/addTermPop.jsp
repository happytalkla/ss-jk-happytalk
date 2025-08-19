<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<div class="inner">
		<!-- left_area -->
		<form id="termPopForm" name="termPopForm" method="post" action="">
		<input type="hidden" name="termNum" value="${termDetail.term_num}" />
			<div class="popup_head">
				<h1 class="popup_tit">용어 사전 등록</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
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
							<td class="textL">
								<input type="text" name="termDivNm" value="${termDetail.term_div_nm }" maxlength="50" style="width:100%;"/>
							</td>
						</tr>					
						<tr>
							<th>제목 *</th>
							<td class="textL">
								<input type="text" name="title" value="${termDetail.title }" maxlength="50" style="width:100%;"/>
							</td>
						</tr>
						<tr>
							<th>내용 *</th>
							<td class="textL">
								<textarea name="cont"  style="width:99%; height:254px">${termDetail.cont }</textarea>
							</td>
						</tr>
						<tr>
							<th>태그</th>
							<td class="textL">
							쉼표로 구분하여 작성하세요. (예: 금융상품, 펀드)
								<input type="text" name="termTag" value="${termDetail.term_tag }" maxlength="1000" style="width:100%;"/>
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