<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %> 


					<!-- 왼쪽 테이블 영역 -->
					<div class="graph_table_area">
						
						<div class="graph_top_area">
							
							<h3 class="sub_stit">분류별 데이터
								<button type="button" class="btn_excel_down" id="ctgDown"><i></i>Excel Export</button>
							</h3>
								<input type="hidden" name="cnsr_div_cd_ctg" id="cnsr_div_cd_ctg" value="C">
								<input type="hidden" value="0" name="cstm_link_div_cd_ctg" id="cstm_link_div_cd_ctg" />
							
						</div>
						<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col>
										<col><col><col><col><col><col><col>
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<th>소분류</th>
											<th>상담 수</th>
											<th>상담직원당 평균 상담 수</th>
											<th>평균 상담 시간</th>
											<th>평가건수</th>
											<th>평균별점</th>	
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty ctgList}">
												<tr>
													<td colspan="8">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${ctgList}" varStatus="status">
													<tr>
														<td>${data.ctg1.replaceAll(">>", "")}</td>
														<td>${data.ctg2.replaceAll(">>", "")}</td>
														<td><c:if test="${data.ctg3 eq null}">-</c:if>${data.ctg3.replaceAll(">>", "")}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/>건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt }"/>건</td>
														<td>
														<c:if test="${data.avg_end_date *1 != 0}">
															${data.avg_end_date *1}일
														</c:if>
														<c:if test="${data.avg_end_hour *1 != 0}">
															${data.avg_end_hour *1}시
														</c:if>
														<c:if test="${data.avg_end_min *1 != 0}">
															${data.avg_end_min *1}분
														</c:if>
															${data.avg_end_sec *1}초
														</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.total_evl_cnt}"/>건</td>
														<td><fmt:formatNumber value="${data.avg_evl_score}" type="number" pattern="#.#" />점</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>

							</div>
					</div>
					<!--// 왼쪽 테이블 영역 -->

