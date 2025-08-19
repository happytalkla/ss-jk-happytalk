<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>


							<h3 class="sub_stit">
								상담직원 일자 테이블
							<!-- 	<button type="button" class="btn_excel_down floatR"
									id="downAdviserAll">
									<i></i>Excel Export
								</button>
							-->
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="width: 15%">
										<col style="width: 15%">
										<col style="width: 15%">
										<col style="width: 15%">
										<col style="width: 10%">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>일자</th>
											<th>상담신청</th>
											<th>상담종료</th>
											<th>평균 종료 시간</th>
											<th>평균상담종료</th>
											<th>상담직원수</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty ctgList}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${ctgList}" varStatus="status">
													<tr >
														<td>${data.reg_date}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/></td>
														<td>
														<c:if test="${data.avg_end_date != '0'}">
															${data.avg_end_date}일
														</c:if>
														<c:if test="${data.avg_end_hour != '00'}">
															${data.avg_end_hour}시
														</c:if>
														<c:if test="${data.avg_end_min != '00'}">
															${data.avg_end_min}분
														</c:if>
															${data.avg_end_sec}초
														</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_cnt}"/></td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>

