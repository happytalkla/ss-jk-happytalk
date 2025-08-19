<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
							<p class="counsel_title"><em><span class="customer_id"><c:out value="${customer.cstm_uid}" /></span>
								<c:if test="${customer.name != null}"><span class="customer_name"><c:out value="${customer.name}" /></span></c:if></em>님의
								상담 이력 조회(총 <span class="customer_history_count"><c:out value="${customer.history_count}" /></span>건)</p>
							<c:if test="${!empty customer.history_list}">
							<table class="tCont"> 
								<caption>상담정보 이력 테이블입니다.</caption>
								<colgroup>
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
									<col style="">
								</colgroup>
								<thead>
									<tr>
										<th>번호</th>
										<th>채널</th>
										<th>대분류</th>
										<th>중분류</th>
										<th>종료분류 대</th>
										<th>종료분류 중</th>
										<th>종료분류 소</th>
										<th>종료메모</th>
										<th>상담시작</th>
										<th>상담상태</th>
										<th>고객사사용자</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${customer.history_list}" var="item">
									<tr>
										<td>1</td>
										<td><c:out value="${item.cstmLinkDivCd}" /></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td><c:out value="${ViewerConstants.COUNSELOR_CUSTOMER_HISTORY.get(item.cnsrLinkDt)}" /></td>
										<td><c:out value="${item.chatRoomStatusCd}" /></td>
										<td><c:out value="${customer.cstm_div_cd}" /></td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
							</c:if>