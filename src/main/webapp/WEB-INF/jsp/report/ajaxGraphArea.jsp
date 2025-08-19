<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
<c:forEach var="data" items="${selectTotalGroupReport}" varStatus="status">
	room_waiting_cnt = ${data.wait_cnt};
	room_ing_cnt = ${data.prsc_cnt};
	room_end_cnt = ${data.end_cnt};

</c:forEach>
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
									<c:forEach var="data" items="${selectTotalGroupReport}" varStatus="status">
										<tr>
											<th>전체상담</th>
											<td>${data.total_cnt}건</td>
											<th>상담대기</th>
											<td>${data.wait_cnt}건</td>
										</tr>
										<tr>
											<th>상담진행중</th>
											<td>${data.prsc_cnt}건</td>
											<th>상담완료</th>
											<td>${data.end_cnt}건</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>