<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
	var dayAdviserLabels = new Array();
	var dayAdviserAvgTime = new Array(); // 상담신청 데이터
	var dayAdviserRoomEnd = new Array(); // 상담종료 데이터
	var dayAdviserAvgEnd = new Array(); // 평균종료시간 데이터
	<c:forEach var="data" items="${adviserList}" varStatus="status">
		dayAdviserLabels[${status.index}] = '${data.reg_date}';
		dayAdviserRoomEnd[${status.index}] = '${data.cns_end_cnt}';
		dayAdviserAvgTime[${status.index}] = '${data.evl_end_time / 60}';
		dayAdviserAvgEnd[${status.index}] = '${data.avg_evl_score}';
	</c:forEach>
	var data5 ={
			labels : dayAdviserLabels,
			datasets : [
				
				{
					fillColor           : "rgba(255,38,0,0)", // 鍮④컯
					strokeColor         : "rgba(255,38,0,0.5)",
					pointColor          : "rgba(255,38,0,0.5)",
					pointStrokeColor    : "#fff",
					pointHighlightFill  : "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data : dayAdviserRoomEnd,
					title : "일별 상담 수"
				}
			]
		};
	var data6 ={
			labels : dayAdviserLabels,
			datasets : [
				{
					fillColor           : "rgba(75,117,255,0)", // �뚮옉
					strokeColor         : "rgba(75,117,255,0.5)",
					pointColor          : "rgba(75,117,255,0.5)",
					pointStrokeColor    : "#fff",
					pointHighlightFill  : "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data : dayAdviserAvgTime,
					title : "일 평균 상담 시간(분)"
				}
			]
		};
	var data8 ={
			labels : dayAdviserLabels,
			datasets : [
				{
					fillColor           : "rgba(75,117,255,0)", // �뚮옉
					strokeColor         : "rgba(75,117,255,0.5)",
					pointColor          : "rgba(75,117,255,0.5)",
					pointStrokeColor    : "#fff",
					pointHighlightFill  : "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data : dayAdviserAvgEnd,
					title : "일 평균별점"
				}
			]
		};
</script>

<div>
	<div class="left" style="width:49%;float:left;">
		<h3 class="sub_stit">
			${adviserInfo.name }  상담원 일자 테이블
			<button type="button" class="btn_excel_down floatR" id="downAdviser">
				<i></i>Excel Export
			</button>
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
						<th>상담일자</th>
						<th>상담진행 수</th>
						<th>총 상담 시간</th>
						<th>상담후처리 수</th>
						<th>평균 상담 시간</th>
<!-- 						<th>평가건수</th>
						<th>평균별점</th> -->
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
									<td>${data.reg_date}</td>
									<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/> 건</td>
									<td>${data.total_use_time}</td>
									<td><fmt:formatNumber  pattern="#,###.#"  value="${data.after_cnt}"/> 건</td>
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
									<%-- <td><fmt:formatNumber  pattern="#,###.#"  value="${data.evl_cnt}"/> 건</td>
									<td>
											<fmt:formatNumber value="${data.avg_evl_score}" type="number" pattern="#.#" /> 점
									</td> --%>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
	<div class="left" style="width:50%;float:right;">
		<h3 class="sub_stit">${adviserInfo.name} 일별 상담수</h3>
		<div class="graph_view_area">
			<canvas id="canvas_Line5" height="90"></canvas>
		</div>
		
		<h3 class="sub_stit">${adviserInfo.name } 일 평균 상담 시간(분)</h3>
		<div class="graph_view_area">
			<canvas id="canvas_Line6" height="90"></canvas>
		</div>
		
<%-- 		<h3 class="sub_stit">${adviserInfo.name } 일 평균별점</h3>
		<div class="graph_view_area">
			<canvas id="canvas_Line8" height="90"></canvas>
		</div> --%>
	</div>
</div>



