<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %> 
<script type="text/javascript">
	<c:forEach var="data" items="${dayList}" varStatus="status">
		dayLabels[${status.index}] = '${data.day}';
		dayRoomStart[${status.index}] = '${data.cns_request_cnt}';
		dayRoomEnd[${status.index}] = '${data.cns_end_cnt}';
		dayAvgEnd[${status.index}] = ${data.avg_end_time }  / 3600;
		dayAvgEndPerMem[${status.index}] = '${data.avg_end_cnt}'; 
	</c:forEach>
</script>

					<!-- 왼쪽 테이블 영역 -->
					<!-- 왼쪽 테이블 영역 -->
						<div class="left_table_area">
							
							<div class="graph_top_area">
								<div class="left" style="margin-right:15px;"><h3 class="sub_stit">일별데이터</h3></div>
								<div class="left">
									<select name="cstm_link_div_cd" id="cstm_link_div_cd" style="line">
										<option value="0">채널 전체</option>
										<option value="A" <c:if test="${cstm_link_div_cd eq 'A'}">selected</c:if>>WEB</option>
									<option value="B" <c:if test="${cstm_link_div_cd eq 'B'}">selected</c:if>>카카오톡</option>
									<option value="C" <c:if test="${cstm_link_div_cd eq 'C'}">selected</c:if>>네이버톡톡</option>
									</select>
									<input type="hidden" name="cnsr_div_cd" id="cnsr_div_cd" value="C">
								</div>
								<button type="button" class="btn_excel_down" id="dayDown">
									<i></i>Excel Export
								</button>
							</div>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
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
											<th>상담일자</th>
											<th>상담 인입 수</th>
											<th>상담 포기 수</th>
											<th>상담종료 수</th>
											<th>평균 상담 시간</th>
											<th>상담직원수</th>
											<th>상담직원당 평균 상담 수</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty dayList}">
												<tr>
													<td colspan="7">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${dayList}" varStatus="status">
													<tr>
														<td>${data.day}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_quit_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/></td>
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
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_cnt}"/></td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt}"/></td>
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
					<div class="right_graph_area" >
						<h3 class="sub_stit">상담 수</h3>
						<canvas id="canvas_Line1" height="90"></canvas>
					</div>
					<div class="right_graph_area">
						<h3 class="sub_stit">평균 상담 시간</h3>
					<canvas id="canvas_Line2" height="90"></canvas>
					</div>
					<div class="right_graph_area">
						<h3 class="sub_stit">상담직원당 평균 상담 수</h3>
					<canvas id="canvas_Line3" height="90"></canvas>
					</div>
					<!--// 오른쪽 그래프 영역 -->