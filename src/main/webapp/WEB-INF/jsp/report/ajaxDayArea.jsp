<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<script type="text/javascript">
<c:choose>
	<c:when test="${empty selectTotalGroupReport}">
		room_waiting_cnt = 0;
		room_ing_cnt = 0;
		room_end_cnt = 0;
	</c:when>
	<c:otherwise>
		<c:forEach var="data" items="${selectTotalGroupReport}" varStatus="status">
			room_waiting_cnt = ${data.wait_cnt};
			room_ing_cnt = ${data.prsc_cnt};
			room_end_cnt = ${data.end_cnt};
		
		</c:forEach>
	</c:otherwise>
</c:choose>
var chart_data = [];
chart_data.push({
	value : room_waiting_cnt,
	color : "#ffa941",
	highlight : "#ffa941",
	label : "상담대기"
});
chart_data.push({
	value : room_ing_cnt,
	color : "#417dd6",
	highlight : "#417dd6",
	label : "상담진행중"
});
chart_data.push({
	value : room_end_cnt,
	color : "#00ba63",
	highlight : "#00ba63",
	label : "상담완료"
});

</script>
					<div class="inner">
						<div class="top_datepicker_area">
							<em class="dash" id="dash_day">2018년 09월 11일 <span>Today
									요약</span></em>
							<button type="button" class="btn_view_day">Today</button>
						</div>
						<p class="dash_time" id="dash_time_day">수집시간 : AM 00 : 00 ~ PM
							02 : 11</p>
						<div class="id_info_area">
							<c:forEach var="data" items="${basicList}" varStatus="status">
	
								<div class="id_box">
									<span class="tit">전체 상담</span> <em>${data.total_cns} <span>건</span></em>
								</div>
								<div class="id_box">
									<span class="tit">상담 대기</span> <em>${data.wait_cns} <span>건</span></em>
								</div>
								<div class="id_box">
									<span class="tit">상담직원 상담진행</span> <em>${data.prsc_cns} <span>건</span></em>
								</div>
								<div class="id_box">
									<span class="tit">상담종료</span> <em>${data.end_cns} <span>건</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit">평균 종료 시간</span> <em> <c:if
											test="${data.avg_time_cns_date != '0'}">
							${data.avg_time_cns_date} <span>일</span>
										</c:if> <c:if test="${data.avg_time_cns_hour != '0'}">
							${data.avg_time_cns_hour} <span>시</span>
										</c:if> <c:if test="${data.avg_time_cns_min != '0'}">
							${data.avg_time_cns_min} <span>분</span>
										</c:if> ${data.avg_time_cns_sec} <span>초</span>
	
									</em>
								</div>
								<div class="id_box">
									<span class="tit">투입 상담직원 수</span> <em>${data.cnt_adv} <span>명</span></em>
								</div>
								<div class="id_box w100">
									<span class="tit">상담직원당 평균 상담종료</span> <em> <c:choose>
											<c:when test="${data.cnt_adv != 0}">
									${data.end_cns / data.cnt_adv}
								</c:when>
											<c:otherwise>
									0
								</c:otherwise>
										</c:choose> <span>건</span></em>
								</div>
							</c:forEach>
						</div>
	
						<div class="talk_info_area">
	
							<c:forEach var="data" items="${linkDivList}" varStatus="status">
								<div class="id_box kyobo">
									<span class="tit"><i></i>삼성증권 채팅</span> <span class="text">(상담/종료)</span>
									<em>${data.web_cnt }</em>
								</div>
							<!-- 
								<div class="id_box naver">
									<span class="tit"><i></i>네이버톡톡</span> <span class="text">(상담/종료)</span>
									<em>${data.naver_cnt }</em>
								</div>
								<div class="id_box kakao">
									<span class="tit"><i></i>카카오톡</span> <span class="text">(상담/종료)</span>
									<em>${data.kakao_cnt }</em>
								</div>
							 -->
							</c:forEach>
							<c:if test="${!empty ctgList}">
								<c:forEach var="data" items="${ctgList}" varStatus="status">
									<div class="id_box">
										<span class="tit">${data.ctg_nm }</span> <span class="text">(상담/종료)</span>
										<em>${data.cnt }</em>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>
	
					<div class="inner">
						<div class="graph_area">
							<!-- 왼쪽 테이블 영역 -->
							<div class="left_table_area">
								<h3 class="sub_stit" id="sub_stit_cnsr">2018년 09월 19일 Today 상담직원별 처리 현황</h3>
								<p class="dash_time" id="dash_time_cnsr">수집시간 : AM 00 : 00 ~ PM 02 : 11 </p>
								<div class="graph_table_area dash_left_table">
									<table class="tCont service">
										<caption>통계테이블 목록입니다.</caption>
										<colgroup>
											<col style="width:16%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
											<col style="width:14%">
										</colgroup>
										<thead>
											<tr>
												<th>상담직원</th>
												<th>상담대기</th>
												<th>상담진행중</th>
												<th>상담완료</th>
												<th>평균 상담종료 시간</th>
												<th>상담 종료 미처리</th>
												<th>휴식여부</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
											<c:when test="${empty selectTotalGroupReport}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${selectTotalGroupReport}"
													varStatus="status">
													<tr  data-adviser="${data.member_uid}" class="adviserTr" id="adviserTr">
														<td>${data.name }</td>
														<td>${data.wait_cnt }</td>
														<td>${data.prsc_cnt }</td>
														<td>${data.end_cnt }</td>
														<td><c:if test="${data.cns_date != 0 }">
													${data.cns_date }일
												</c:if> <c:if test="${data.cns_hour != 0 }">
													${data.cns_hour }시
												</c:if> <c:if test="${data.cns_min != 0 }">
													${data.cns_min }분
												</c:if> ${data.cns_sec }초</td>
														<td>${data.no_pcs_cnt }</td>
														<td>${data.work_yn }</td>
													</tr>
												</c:forEach>
											</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
								<div class="table_bottom_area">
									<div class="table_count">
										<span class="cont_text"><em>${totCount}</em> count(s)</span>
									</div>
								</div>
							</div>
						
							
							<!--// 왼쪽 테이블 영역 -->
	
							<!-- 오른쪽 그래프 영역 -->
							<div class="right_graph_area" id="graph_area">
								<h3 class="sub_stit">appliance 상담직원</h3>
								<div class="graph_view_area">
									<canvas id="canvas_Line1" height="90"></canvas>
								</div>
								<table class="tCont">
									<caption>상담직원별 처리 상황 목록입니다.</caption>
									<colgroup>
										<col style="width: 25%">
										<col style="width: 25%">
										<col style="width: 25%">
										<col style="width: 25%">
									</colgroup>
									<tbody>
										<tr>
											<th>전체상담</th>
											<td>1건</td>
											<th>상담대기</th>
											<td>0건</td>
										</tr>
										<tr>
											<th>상담진행중</th>
											<td>1건</td>
											<th>상담완료</th>
											<td>0건</td>
										</tr>
									</tbody>
								</table>
							</div>
							<!--// 오른쪽 그래프 영역 -->
						</div>
					</div>
	
					<div class="inner" id="bottom_score_area">
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
					</div>