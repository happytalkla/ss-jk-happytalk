<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
						<h3 class="sub_stit">상담 그래프</h3>
							<div class="graph_view_area">
							<canvas id="canvas_Line4" height="90"></canvas>
							</div>

							<h3 class="sub_stit">평균 종료 시간 그래프</h3>
							<div class="graph_view_area">
							<canvas id="canvas_Line5" height="90"></canvas>
							</div>

							<h3 class="sub_stit">
								상담원 시간 테이블
								<button type="button" class="btn_excel_down floatR" id="downTimeAdviser">
									<i></i>Excel Export
								</button>
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="width: 10%">
										<col style="width: 15%">
										<col style="width: 15%">
										<col style="width: 15%">
										<col style="width: 10%">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>시간</th>
											<th>상담신청</th>
											<th>상담종료</th>
											<th>평균 종료 시간</th>
											<th>평가건수</th>
											<th>평균별점</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty adviserList}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${adviserList}" varStatus="status">
													<tr>
														<td>${data.reg_time}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}" /></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}" /></td>
														<td>
														<c:if test="${data.avg_end_date *1 != 0}">
															${data.avg_end_date *1}<span>일</span>
														</c:if>
														<c:if test="${data.avg_end_hour *1 != 0}">
															${data.avg_end_hour *1}<span>시</span>
														</c:if>
														<c:if test="${data.avg_end_min *1 != 0}">
															${data.avg_end_min *1}<span>분</span>
														</c:if>
														${data.avg_end_sec *1}<span>초</span>
														</td>
														<td>${data.evl_cnt}</td>
														<td>
														<div class="reporting_list_star">
															<c:set var="isDot" value="false"/>	
															<c:set var="compareDot" value="false"/>	
															<c:forEach var="i" begin="1" end="5">	
																<c:choose>
																	<c:when test="${i <= data.avg_evl_score}">
																		<span class="star_full"><i></i><p style="width:100%"></p></span> 
																	</c:when>
																	<c:when test="${isDot == compareDot}">
																	<c:set var="isDot" value="true"/>	
																		<span class="star_full"><i></i><p style="width:${data.avg_evl_score_dot}0%"></p></span> 
																	</c:when>
																	<c:otherwise>
																		<span class="star_full"><i></i><p style="width:0%"></p></span> 
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</div>
														</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
										
									</tbody>
								</table>

							</div>