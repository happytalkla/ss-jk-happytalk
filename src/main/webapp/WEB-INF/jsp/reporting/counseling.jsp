<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<title>디지털채팅상담시스템</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/include.css' />" />
<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
<script type="text/javascript"
	src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/js/ChartNew.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(document)
								.on(
										"click",
										".btn_holiday_plus",
										function() {
											if (periodDayCheck()) {
												// 조회 시작
												$("#nowPage").val('');
												goPaging();
											}
										});

						// 이전 클릭
						$(document).on(
								"click",
								"#counselingForm .dev_prev_btn",
								function() {
									var nowPage = $(
											"#counselingForm [name='nowPage']")
											.val() * 1 - 1;
									var totPage = $(
											"#counselingForm [name='totPage']")
											.val() * 1;
									if (nowPage <= 0) {
										return false;
									}
									$("#counselingForm [name='nowPage']").val(
											nowPage);
									goPaging();
								});

						// 이후 클릭
						$(document).on(
								"click",
								"#counselingForm .dev_next_btn",
								function() {
									var nowPage = $(
											"#counselingForm [name='nowPage']")
											.val() * 1 + 1;
									var totPage = $(
											"#counselingForm [name='totPage']")
											.val() * 1;
									if (nowPage > totPage) {
										return false;
									}
									$("#counselingForm [name='nowPage']").val(
											nowPage);
									goPaging();
								});

						// 페이지 입력창 키 입력 처리
						$(document)
								.on(
										"keypress",
										"#counselingForm [name='nowPage']",
										function(e) {
											if (e.keyCode == 13) {
												e.preventDefault();
												$(
														"#counselingForm [name='nowPage']")
														.trigger("change");
											}
										});

						// 페이지 입력창 입력
						$(document)
								.on(
										"change",
										"#counselingForm [name='nowPage']",
										function() {
											var nowPage = $(
													"#counselingForm [name='nowPage']")
													.val();
											var inputNowPage = $(this).val() * 1;
											var totPage = $(
													"#counselingForm [name='totPage']")
													.val() * 1;

											if (!$.isNumeric(inputNowPage)) {
												alert("숫자만 입력해 주세요.");
												$(
														"#counselingForm [name='nowPage']")
														.val(nowPage);
												return false;
											}

											if (inputNowPage <= 0) {
												alert("1페이지 부터 입력해 주세요");
												$(
														"#counselingForm [name='nowPage']")
														.val(nowPage);
												return false;
											}

											if (inputNowPage > totPage) {
												alert("최대 페이지가 " + totPage
														+ "페이지 입니다.");
												$(
														"#counselingForm [name='nowPage']")
														.val(nowPage);
												return false;
											}

											$(
													"#counselingForm [name='nowPage']")
													.val(inputNowPage);
											goPaging();
										});

						// 이전 클릭
						$(document).on(
								"click",
								"#excelDown",
								function() {
									$("#counselingForm").attr("action", "<c:url value='/reporting/counselingExcel' />");
									$("#counselingForm").submit();
								});

						// 팝업 닫기 버튼
						$('body').on('click', '.btn_popup_close', function(e) {
							$('.popup').hide();
						});


					});

	function goPaging() {
		$("#counselingForm").attr("action", "<c:url value='/reporting/counseling' />");
		$("#counselingForm").submit();
	}

	function periodDayCheck(){
		var iDay = 1000 * 60 * 60 * 24;
		var startDateArray = $('#startDate').val().split('-');
		var startDateObj = new Date(startDateArray[0],Number(startDateArray[1])-1,startDateArray[2]);
		var endDateArray = $('#endDate').val().split('-');
		var endDateObj = new Date(endDateArray[0],Number(endDateArray[1])-1,endDateArray[2]);

		var betweenDay = (endDateObj.getTime()-startDateObj.getTime()) / 1000 / 60/60/ 24+1;

		if( betweenDay > 31){
			alert('최대 30일까지 조회 가능합니다.');
			return false;
		}else if(betweenDay <1){
			alert('최소 1일까지 조회 가능합니다.');
			return false;
		}else{
			return true;
		}


	}

	function openChatRoom(chatRoomUid){
		$.ajax({
			url: "<c:url value='/api/html/chat/room/'/>" + chatRoomUid,
			data: {
				rollType: "C",
				withChatMessageList: 'true',
				withMetaInfo: 'true',
			},
			dataType: 'html',
			success : function(result) {

				$('.popup_counseling').css('display','block');
				$('.popup_counseling').html(result);

				// 멀티형 메세지 슬라이더
				new Swiper($('.popup_counseling .chat_text_slide'), {
					slidesPerView: 'auto',
					centeredSlides: false,
					spaceBetween: 30,
				});
			}
		})
	}
</script> 

<style>

.top_filter_area .dot_tit  {
	float: left;
    width: 100px;
    display: inline-block;
    height: 100%;
    line-height: 100%;
    padding-left: 10px;
    background: url(../images/admin/dot_tit.gif) no-repeat 0 50%;
}
.top_filter_area .search_area {
	padding-left:10px;
	float:left;
	width:600px;
	margin:0
}
</style>
</head>

<body id="admin">
	<!-- head -->
	<div class="head">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
	</div>
	<!--// head -->

	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/menu.jsp" />

		<!-- left_area -->
		<form id="counselingForm" name="dailyForm" method="post" action="">
			<div class="left_content_area">
				<div class="left_content_head">
					<h2 class="sub_tit">상담내역 Reporting<%--(${sessionVo.departCd})--%></h2>
					<span class="left_top_text">선택된 기간의 상담내역을 보여줍니다.</span>
				</div>
				<div class="inner">
					<div class="top_filter_area">
						<span class="dot_tit">기간</span>
						<div class="search_area" style="width:80%">
							<input type="text" class="datepicker" name="startDate" id="startDate" value="${startDate}">
							~ 
							<input type="text" class="datepicker" name="endDate" id="endDate" value="${endDate}">
							<input type="hidden" name="totPage"  id="totPage" value="${totPage }" />
						</div>
					</div>
					
					<div class="top_filter_area">
						<span class="dot_tit">채널</span>
						<div class="search_area" style="width:80%">
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdE" value="E"${linkCdE }>
			              	<label for="linkDivCdE"><span></span>ARS</label>
			              	<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdD" value="D"${linkCdD }>
			              	<label for="linkDivCdD"><span></span>mPOP</label>
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdA" value="A"${linkCdA}>
			              	<label for="linkDivCdA"><span></span>홈페이지</label>
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdC" value="C"${linkCdC }>
			              	<label for="linkDivCdC"><span></span>O2</label>
							<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdB" value="B"${linkCdB }>
			              	<label for="linkDivCdB"><span></span>카카오</label>		              	
			              	<!--<input type="checkbox" class="checkbox_18" name="cstm_link_cd" id="linkDivCdN" value="C"${linkCdN}>
			              	<label for="linkDivCdN"><span></span>네이버톡</label>-->
						</div>
					</div>
					<div class="top_filter_area">
						<span class="dot_tit">
							<select name="customerType" id="customerType">
								<option value="CID" <c:if test="${customerType == 'CID'}"> selected="selected"</c:if> >고객번호</option>
								<option value="CNM" <c:if test="${customerType == 'CNM'}"> selected="selected"</c:if> >고객명</option>
								<option value="MNM" <c:if test="${customerType == 'MNM'}"> selected="selected"</c:if> >상담직원</option>
							</select>
						</span>
						<div class="search_area" style="width:80%">
							<input type="text" id="customerText" name="customerText" value="${customerText}">
						</div>
					</div>
					<div class="top_filter_area">
						<button type="button" class="btn_holiday_plus" style="width:500px;">검색</button>
					</div>
					<%-- <div class="id_info_area">

						<c:forEach var="data" items="${selectCounselingCnt}" varStatus="status">
							<div class="id_box">
								<span class="tit">채팅상담 인입수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.all_cnt}"/> <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit">${sessionVo.departCd }상담 인입수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.depart_all_cnt}"/> <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit">상담 포기 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.cstm_quit_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">상담 진행(정상종료) 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_end_cnt + data.cmr_end_cnt + data.bat_end_cnt}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">자동 종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.bat_end_cnt}"/> <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">고객 종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.cmr_end_cnt}"/> <span>건</span></em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">상담직원 종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_end_cnt}"/> <span>건</span></em>
							</div>
							<div class="id_box w100">
									<span class="tit" >전체 상담직원 평균 상담 시간</span> <em><span>${data.total_avg_talk_time}</span></em>
								</div>
						</c:forEach>
					</div> --%>

					<div class="graph_area">
						<div class="report_table_top">
							<button type="button" class="btn_excel_down floatR" 	id="excelDown">
								<i></i>Excel Export
							</button>
							<p class="info">
								<i></i>상담내역 다운로드시 생성되는 엑셀 파일은 보안이 걸려 있습니다. 다운로드 받은 파일을 마우스 오른쪽
								버튼을 클릭하여 속성창을 연후 최하단의 보안차단해제를 하여야 내용이 정상적으로 보입니다.
							</p>
						</div>
						<div class="report_table_area" style="min-height:0px;">
							<table class="tCont service" style="min-width:100%;width:max-content;">
								<caption>상담내역 목록입니다.</caption>
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
										<col style="">
										<col style="">
										<col style="">
										<col style="">
										<col style="">
									</colgroup>
								<thead>
									<tr>
										<th>No.</th>
										<th>상담방</th>
										<th>채팅방 생성일시</th>
										<th>상담원 배정일시</th>
										<th>상담 시작일시</th>
										<th>상담 종료일시</th>
										<th>채팅방 종료일시</th>
										<th>상담 이용시간</th>
										<th>채널</th>
										<th>고객번호</th>
										<th>고객명</th>
										<th>고객user_key</th>
										<th>대분류</th>
										<th>중분류</th>
										<th>소분류</th>
										<th>종료구분</th>
										<th>상담원</th>
										<th>담당매니저</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="data" items="${counselingList}" varStatus="status">
										<tr>
											<td>${data.rnum}</td>
											<td class="textL"><a href="javascript:openChatRoom('${data.chat_room_uid}')" class="link_report_title">${data.chat_room_uid}</a></td>
											<td>${data.room_create_dt}</td>
											<td>${data.cnsr_assign_dt}</td>
											<td>${data.cnsr_submit_dt}</td>
											<td>${data.last_chat_dt}</td>
											<td>${data.room_end_dt}</td>
											<td>
												<c:if test="${data.use_time_hour * 1 != 0}">
													${data.use_time_hour * 1}<span>시</span>
												</c:if>
												<c:if test="${data.use_time_min * 1 != 0}">
												${data.use_time_min * 1}<span>분</span>
												</c:if>
												${data.use_time_sec * 1}<span>초</span>
											</td>
											<td>${data.cstm_link_div_nm}</td>
											<td>${data.coc_id}</td>											
											<td>${data.c_name}</td>
											<td>${data.cstm_user_key}</td>
											<td>${data.ctg1}</td>
											<td>${data.ctg2}</td>
											<td>${data.ctg3}</td>
											<td>${data.end_div_nm}</td>
											<td>${data.m_name}</td>
											<td><c:if test="${data.manager_nm eq null}">-</c:if>${data.manager_nm}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="table_bottom_area">
							<!-- pager -->
							<div class="pager">
								<button type="button" class="btn_prev_page dev_prev_btn" id="dev_prev_btn">이전으로</button>
								<input type="text" class="form_pager" value="${nowPage }" name="nowPage" id="nowPage"> 
								<span class="page_no">/	${totPage } page(s)</span>
								<button type="button" class="btn_next_page dev_next_btn" id="dev_next_btn">다음으로</button>
							</div>
							<!--// pager -->
							<div class="table_count">
								<span class="cont_text"><em><fmt:formatNumber  pattern="#,###.#"  value="${totCount }"/></em> count(s)</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<!--// left_area -->
	</div>

	<!-- alret 창 -->
	<div class="layer_alert" style="display:none;">
		<p class="alert_text"></p>
	</div>
	<!--// alret 창 -->

	<!-- popup:상담내역 -->
	<div class="popup popup_counseling" style="display: none">
	</div>
	<!--// popup:상담내역 -->

</body>
</html>
