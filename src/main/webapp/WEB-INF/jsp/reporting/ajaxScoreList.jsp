<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 

<input type="hidden" name="nowPage" id="nowPage" value="${nowPage }" />
<input type="hidden" name="totPage" id="totPage" value="${totPage }" />
<h3 class="sub_stit">
	별점 테이블
	<button type="button" class="btn_excel_down floatR" id="scoreDown">
		<i></i>Excel Export
	</button>
</h3>
<div class="graph_table_area">
	<table class="tCont service">
		<caption>통계테이블 목록입니다.</caption>
		<colgroup>
			<col>
			<col>
			<col>
			<col>
			<col>
			<col>
			<col>
		</colgroup>
		<thead>
			<tr>
				<th>No.</th>
				<th>평가입력 날짜</th>
				<th>상담종료 날짜</th>
				<th>별점</th>
				<th>한줄평가</th>
				<th>상담원</th>
				<th>대화방</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${empty scoreList}">
					<tr>
						<td colspan="7">조회된 내용이 없습니다.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="data" items="${scoreList}" varStatus="status">
						<tr>
							<td>${data.rnum}</td>
							<td>${data.create_dt}</td>
							<td>${data.room_end_dt}</td>
							<td>${data.evl_score}</td>
							<td>${data.evl_cont}</td>
							<td>${data.name}</td>
							<td><a href="javascript:openChatRoom('${data.chat_room_uid}')" class="link_report_title">${data.chat_room_uid}</a></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<div class="table_bottom_area">
		<div class="btn_center_area">
			<!-- pager -->
			<div class="pager">
				<button type="button" class="btn_prev_page dev_prev_btn"
					id="dev_prev_btn">이전으로</button>
				<input type="text" class="form_pager" value="${nowPage }"
					name="inputNowPage"> <span class="page_no">/
					${totPage } page(s)</span>
				<button type="button" class="btn_next_page dev_next_btn"
					id="dev_next_btn">다음으로</button>
			</div>
			<!--// pager -->
			<div class="table_count">
				<span class="cont_text"><em>${totCount }</em> count(s)</span>
			</div>
			<!-- pager -->
		</div>
	</div>
</div>