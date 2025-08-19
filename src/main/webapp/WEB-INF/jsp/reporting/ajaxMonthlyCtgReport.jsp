<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %> 

					<!-- 왼쪽 테이블 영역 -->
					<div class="left_table_area">
						<h3 class="sub_stit">통계 테이블</h3>
						<div class="graph_top_area">
							<div class="left">
								<select name="cnsr_div_cd_ctg" id="cnsr_div_cd_ctg">
									<option value = "0">전체</option>
									<option value = "R" <c:if test="${cnsr_div_cd_ctg eq 'R'}">selected</c:if>>로봇</option>
									<option value = "C" <c:if test="${cnsr_div_cd_ctg eq 'C'}">selected</c:if>>상담직원</option>
								</select>
								<input type="hidden" value="A" name="cstm_link_div_cd_ctg" id="cstm_link_div_cd_ctg" />
								<!--  
								<select name="cstm_link_div_cd" id="cstm_link_div_cd">
									<option value = "0">전체</option>
									<option value="A" <c:if test="${cstm_link_div_cd eq 'A'}">selected</c:if>>WEB</option>
									<option value="B" <c:if test="${cstm_link_div_cd eq 'B'}">selected</c:if>>카카오톡</option>
									<option value="C" <c:if test="${cstm_link_div_cd eq 'C'}">selected</c:if>>네이버톡톡</option>	
								</select>
								<button type="button" class="btn_search" id="daySearch">검색</button>
								-->
							</div>
							<button type="button" class="btn_excel_down" id="ctgDown"><i></i>Excel Export</button>
						</div>
						<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="width: 10%">
										<col style="width: 10%">
										<c:if test="${ctgDept == 'true'}">
											<col style="width: 10%">
										</c:if>
										<col style="width: 11%">
										<col style="width: 11%">
										<col style="width: 18%">
										<col style="width: 20%">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<c:if test="${ctgDept == 'true'}">
											<th>소분류</th>
											</c:if>
											<th>상담신청</th>
											<th>상담종료</th>
											<th>평균 종료 시간</th>
											<th>상담직원당 평균 상담 종료</th>
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
													<tr class="ctgTr" data-ctg="${data.ctg_num}">
														<td>${data.ctg1}</td>
														<td>${data.ctg2}</td>
														<c:if test="${ctgDept == 'true'}">
														<td>${data.ctg3}</td>
														</c:if>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"></fmt:formatNumber></td>
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
					</div>
					<!--// 왼쪽 테이블 영역 -->
					<!-- 오른쪽 그래프 영역 -->
					<div class="right_graph_area" id="ctgRight">
							
					</div>
					<!--// 오른쪽 그래프 영역 -->

