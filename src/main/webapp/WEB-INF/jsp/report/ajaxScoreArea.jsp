<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
						<h3 class="sub_stit" id="sub_stit_score">2018년 09월 19일 Today 상담 만족도</h3>
						<p class="dash_time" id="dash_time_score">수집시간 : AM 00 : 00 ~ PM 02 : 11</p>
	
						<div class="graph_table_area">
						
							<div class="counseling_star_area">
							<c:set var="isDot" value="false" />
							<c:set var="compareDot" value="false" />
							<c:forEach var="i" begin="1" end="5">
								<c:choose>
									<c:when test="${i <= scoreAvg}">
									<span class="star_full"><i></i>
									<p style="width: 100%"></p></span>
									</c:when>
									<c:when test="${isDot == compareDot}">
									<c:set var="isDot" value="true" />
									<span class="star_full"><i></i>
									<p style="width:${data.avg_evl_score_dot}0%"></p></span>
									</c:when>
									<c:otherwise>
									<span class="star_full"><i></i>
									<p style="width: 0%"></p></span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						<!-- 		<span class="star_full"><i></i>
								<p style="width: 100%"></p></span> <span class="star_full"><i></i>
								<p style="width: 80%"></p></span> <span class="star_full"><i></i>
								<p style="width: 60%"></p></span> <span class="star_full"><i></i>
								<p style="width: 40%"></p></span> <span class="star_full"><i></i>
								<p style="width: 20%">
						
									</p></span> -->
								<p class="star_full_text">매우 만족해요 (${scoreAvg}점/5점)</p>
							</div>
							<table class="tCont service">
								<caption>통계테이블 목록입니다.</caption>
								<colgroup>
									<col style="width: 12%">
									<col style="width: 12%">
									<col style="width: 12%">
									<col style="width: 18%">
									<col style="width: 14%">
									<col style="width: 20%">
									<col style="width:">
								</colgroup>
								<thead>
									<tr>
										<th>평가입력 날짜</th>
										<th>상담종료 날짜</th>
										<th>별점</th>
										<th>한줄평가</th>
										<th>상담직원</th>
										<th>대화방</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${empty scoreList}">
											<tr>
												<td colspan="9">조회된 내용이 없습니다.</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach var="data" items="${scoreList}" varStatus="status">
												<tr>
													<td>${data.create_dt }</td>
													<td>${data.room_end_dt }</td>
													<td>
														<div class="reporting_list_star">
															<c:set var="isDot" value="false" />
															<c:set var="compareDot" value="false" />
															<c:forEach var="i" begin="1" end="5">
																<c:choose>
																	<c:when test="${i <= data.evl_score}">
																		<span class="star_full"><i></i>
																			<p style="width: 100%"></p></span>
																	</c:when>
																	<c:when test="${isDot == compareDot}">
																		<c:set var="isDot" value="true" />
																		<span class="star_full"><i></i>
																			<p style="width:${data.avg_evl_score_dot}0%"></p></span>
																	</c:when>
																	<c:otherwise>
																		<span class="star_full"><i></i>
																			<p style="width: 0%"></p></span>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</div>
													</td>
													<td>${data.evl_cont }</td>
													<td>${data.name }</td>
													<td><a href="javascript:openChatRoom('${data.chat_room_uid}')" class="link_report_title">${data.chat_room_uid}</a></td>
													<td></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<div class="table_bottom_area">
								<!-- pager -->
								<input type="hidden" name="nowPage" id="nowPage" value="${nowPage }" /> 
								<input type="hidden" name="totPage"	id="totPage" value="${totPage }" />
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
									
								<!--// pager -->
								<div class="table_count">
									<span class="cont_text"><em>${totCount }</em> count(s)</span>
								</div>
							</div>
						</div>