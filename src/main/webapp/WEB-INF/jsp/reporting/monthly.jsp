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
<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/ChartNew.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/swiper.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/MonthPicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>
<script type="text/javascript">

		$(window).load(function(){
			$("#snb").css("height",($("#area1").height()+347)+"px");
			$(".left_content_area").css('height',($("#area1").height()+347)+"px");

		});

		$(document).ready(function() {
			$(document).on("click", ".btn_holiday_plus", function() {
				$("#monthlyForm").attr("action", "<c:url value='/reporting/monthly' />");
				$("#monthlyForm").submit();
				}
);

			//탭 버튼 동작설정
			$(document).on("click", "li", function() {
				 $('li').removeClass('active');
				 $(this).addClass('active');
				 $('.graph_area').css('display','none');
				 $("#area"+$(this).attr("data-page")).css('display','block');

				 var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
				 var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
				 var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);
				 var myLine4 = new Chart(document.getElementById("canvas_Line4").getContext("2d")).Bar(data4,option1);//막대로 변경
				 var myLine7 = new Chart(document.getElementById("canvas_Line7").getContext("2d")).Bar(data7,option1);//막대로 변경 추가

				 
				 $('#adviserTr td:first').click();
				 $('.ctgTr td:first').click();
				 $("#snb").css({"height":$('.left_content_area').height()+"px"});
				// 팝업 닫기 버튼
				 $('body').on('click', '.btn_popup_close', function(e) {
						$('.popup').hide();
				 });

			});

			$(".monthpicker").MonthPicker({
				Button:false,
				MonthFormat:'yy-mm'

			});

			//alert($('li:first').click());
			$(document).on("blur",".monthpicker",function(){
				var selectMonth = $(this).val();
				var selectMonthArr = $(this).val().split("-");
				var oldMonth = $(this).attr("data-month");
				if(selectMonth != oldMonth){
					var selectDate = new Date();
					selectDate.setYear(Number(selectMonthArr[0])+1900);
					selectDate.setMonth(Number(selectMonthArr[1]));
					selectDate.setDate(1-1);
					
					var lastDate = selectDate.getDate();

					if($(this).val().indexOf('undefined') < 0){
						$("#startDate").val(selectMonth+"-01");
						$("#endDate").val(selectMonth+"-"+lastDate);
						
					}else{
						$(this).val($(this).val().replace('undefined-',''));
					}
					$("#monthlyForm").attr("action", "<c:url value='/reporting/monthly' />");
					$("#monthlyForm").submit();
				}
			});



			//기간검색
			/*$(document).on("click", ".btn_search_date", function() {

				$("#monthlyForm").attr("action", "<c:url value='/reporting/monthly' />");
				$("#monthlyForm").submit();

			});	*/

			//일자별 검색
			$(document).on("click", "#daySearch", function() {
				searchDay()
			});

			//일자별 엑셀
			$(document).on("click", "#dayDown", function() {
				downloadMonthlyDayReportExcel()
			});

			//요일별 검색
			$(document).on("click", "#weekSearch", function() {
				searchWeek()
			});


			//요일별 엑셀
			$(document).on("click", "#weekDown", function() {
				downloadMonthlyWeekReportExcel()
			});

			//별점 엑셀
			$(document).on("click", "#scoreDown", function() {
				downloadMonthlyScoreListExcel();

			});

			//상담직원 엑셀
			$(document).on("click", "#downAdviser", function() {
				downloadMonthlyAdviserListExcel()
			});

			//상담직원 일자별 엑셀
			$(document).on("click", "#downAdviserAll", function() {
				downloadMonthlyAdviserReportAllExcel()
			});

			//상담직원 클릭
			$(document).on("click", "#adviserTr", function() {
				$('#member_uid').val($(this).attr('data-adviser'));
				selectAdviser($(this).attr('data-adviser'));
				$('.adviserTr').removeClass('active');
				$(this).addClass('active')
			});
			//// 테이블 채널 선택
			$(document).on("change", "#cstm_link_div_cd", function() {
				searchDay();
			});

			$(document).on("change", "#cnsr_div_cd_week", function() {
				searchWeek();
			});

			$(document).on("change", "#cnsr_div_cd_ctg", function() {
				searchCtg();
			});

			$(document).on("click", ".ctgTr", function() {
				selectCtgDetail($(this).attr('data-ctg'));

				$('.ctgTr').removeClass('active');
				$(this).addClass('active');
			});

			$(document).on("click", "#ctgDown", function() {
				downloadCtgReportExcel();
			});


		});

		// 이전 클릭
		$(document).on("click", "#monthlyForm .dev_prev_btn", function() {
			var nowPage = $("#monthlyForm [name='nowPage']").val()*1 - 1;
			var totPage = $("#monthlyForm [name='totPage']").val()*1;
			if(nowPage <= 0){
				return false;
			}
			$("#monthlyForm [name='nowPage']").val(nowPage);
			goPaging();
		});

		// 이후 클릭
		$(document).on("click", "#monthlyForm .dev_next_btn", function() {
			var nowPage = $("#monthlyForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#monthlyForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}
			$("#monthlyForm [name='nowPage']").val(nowPage);
			goPaging();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#monthlyForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#monthlyForm [name='inputNowPage']").trigger("change");
			}
		});

		// 페이지 입력창 입력
		$(document).on("change", "#monthlyForm [name='inputNowPage']", function() {
			var nowPage = $("#monthlyForm [name='nowPage']").val();
			var inputNowPage = $(this).val()*1;
			var totPage = $("#monthlyForm [name='totPage']").val()*1;

			if(!$.isNumeric(inputNowPage)){
				alert("숫자만 입력해 주세요.");
				$("#monthlyForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage <= 0){
				alert("1페이지 부터 입력해 주세요");
				$("#monthlyForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage > totPage){
				alert("최대 페이지가 "+totPage+"페이지 입니다.");
				$("#monthlyForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			$("#monthlyForm [name='nowPage']").val(inputNowPage);
			goPaging();
		});




		// 일자별 통계
		var dayLabels = new Array(); //일별라벨
		var dayRoomStart = new Array(); // 상담신청 데이터
		var dayRoomEnd = new Array(); // 상담종료 데이터
		var dayAvgEnd = new Array(); // 평균종료시간 데이터
		var dayAvgEndPerMem = new Array(); // 상담직원당 평균 상담 종료 데이터


		///임시
		var dayRoomAll = new Array(); //일별라벨
		//요일별 통계
		var weekAvgEnd = new Array(); // 평균종료시간 그래프



		<c:forEach var="data" items="${dayList}" varStatus="status">
			dayLabels[${status.index}] = '${data.day}';
			dayRoomStart[${status.index}] = '${data.cns_request_cnt}';
			dayRoomEnd[${status.index}] = '${data.cns_end_cnt}';
			dayAvgEnd[${status.index}] = '<fmt:formatNumber value="${data.avg_end_time_excel / 60}" type="number" pattern="#.#" />';
			dayAvgEndPerMem[${status.index}] = '${data.avg_end_cnt}';
		</c:forEach>
		var dateCountlabel = new Array();
		var datelabel = new Array();
		<c:forEach var="data" items="${weekList}" varStatus="status">
			datelabel[${status.index}] = '${data.day}';
			dateCountlabel[${status.index}] = ${data.cns_request_cnt};
			weekAvgEnd[${status.index}] = '<fmt:formatNumber value="${data.avg_end_time_excel  / 60 }" type="number" pattern="#.#" />';
		</c:forEach>


		var data1 ={
			labels : dayLabels,
			datasets : [
				{
					fillColor           : "rgba(255,38,0,0)",
					strokeColor         : "rgba(255,38,0,1)",
					pointColor          : "rgba(255,38,0,1)",
					pointStrokeColor    : "#ff2600",
					pointHighlightFill  : "#ff2600",
					pointHighlightStroke: "rgba(255,38,0,1)",
					data : dayRoomEnd,
					title : "상담수"
				}
			]
		};
		var data2 ={
			labels : dayLabels,
			datasets : [
				{
					fillColor           : "rgba(255,200,112,0)",
					strokeColor         : "rgba(255,200,112,0.5)",
					pointColor          : "rgba(255,200,112,0.5)",
					pointStrokeColor    : "#fff",
					pointHighlightFill  : "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data : dayAvgEnd,
					title : "평균 상담 시간 (분)"
				}
			]
		};

		var data3 ={
				labels : dayLabels,
				datasets : [
					{
						fillColor           : "rgba(75,117,255,0)",
						strokeColor         : "rgba(75,117,255,0.5)",
						pointColor          : "rgba(75,117,255,0.5)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(220,220,220,1)",
						data : dayAvgEndPerMem,
						title : "상담직원당 평균 상담 종료"
					}
				]
			};

		var data4 ={
				labels : datelabel,
				datasets : [
					{
						fillColor           : "rgba(0,220,0,1)",
						strokeColor         : "rgba(0,220,0,1)",
						pointColor          : "rgba(0,220,0,1)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(0,220,0,1)",
						data : weekAvgEnd,
						title : "평균 상담 시간(분)"
					}
				]
			};
		var data7 ={
				labels : datelabel,
				datasets : [
					{
						fillColor           : "rgba(75,117,255,1)",
						strokeColor         : "rgba(75,117,255,1)",
						pointColor          : "rgba(75,117,255,1)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(220,220,220,1)",
						data : dateCountlabel,
						title : "평균상담수"
					}
				]
			};
		var data8 ={
				labels : dateCountlabel,
				datasets : [
					{
						fillColor           : "rgba(75,117,255,1)",
						strokeColor         : "rgba(75,117,255,1)",
						pointColor          : "rgba(75,117,255,1)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(220,220,220,1)",
						data : weekAvgEnd,
						title : "일 평균 별점"
					}
				]
			};
		var data5 ={
				labels : dateCountlabel,
				datasets : [
					{
						fillColor           : "rgba(0,220,0,1)",
						strokeColor         : "rgba(0,220,0,1)",
						pointColor          : "rgba(0,220,0,1)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(0,220,0,1)",
						data : weekAvgEnd,
						title : "일별 상담 수"
					}
				]
			};
		var data6 ={
				labels : dateCountlabel,
				datasets : [
					{
						fillColor           : "rgba(0,220,0,1)",
						strokeColor         : "rgba(0,220,0,1)",
						pointColor          : "rgba(0,220,0,1)",
						pointStrokeColor    : "#fff",
						pointHighlightFill  : "#fff",
						pointHighlightStroke: "rgba(0,220,0,1)",
						data : weekAvgEnd,
						title : "일 평균 상담 시간"
					}
				]
			};
		var option1 = {
				responsive: true,
				// legend
				legend : true,
				datasetFill: false,
				showSingleLegend: true,
				legendBorders: false,
				// annotate
				annotateDisplay: true,
				annotateRelocate: true,
				detectAnnotateOnFullLine: true,
				annotateFontSize: 10,
				annotateBorderRadius: '5px',
				annotatePadding: "10px",
				annotateBackgroundColor: 'rgba(0,0,0,0.7)'

		};

	
		window.onload = function(){
			var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
			var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
			var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);
			var myLine4 = new Chart(document.getElementById("canvas_Line4").getContext("2d")).Bar(data4,option1);
			var myLine7 = new Chart(document.getElementById("canvas_Line7").getContext("2d")).Bar(data7,option1);
//			var myLine5 = new Chart(document.getElementById("canvas_Line5").getContext("2d")).Line(data5,option1);
//			var myLine6 = new Chart(document.getElementById("canvas_Line6").getContext("2d")).Line(data6,option1);
//			var myLine8 = new Chart(document.getElementById("canvas_Line8").getContext("2d")).Line(data8,option1);
			console.log("char_data" + data1);
			 $('#area1').css('display','block');
			 $('#area2').css('display','none');
			 $('#area3').css('display','none');
			 $('#area4').css('display','none');
			 $('#area5').css('display','none');

		}

		function periodDayCheck(){
			var iDay = 1000 * 60 * 60 * 24;
			var startDateArray = $('#startDate').val().split('-');
			var startDateObj = new Date(startDateArray[0],Number(startDateArray[1])-1,startDateArray[2]);
			var endDateArray = $('#endDate').val().split('-');
			var endDateObj = new Date(endDateArray[0],Number(endDateArray[1])-1,endDateArray[2]);

			var betweenDay = (endDateObj.getTime()-startDateObj.getTime()) / 1000 / 60/60/ 24+1;

			if( betweenDay > 91){
				alert('최대 90일까지 조회 가능합니다.');
				return false;
			}else if(betweenDay <1){
				alert('최소 1일까지 조회 가능합니다.');
				return false;
			}else{
				return true;
			}


		}

		function searchDay(){
			$.ajax({
				url : "<c:url value='/reporting/selectDayReport' />",
				data : {
						 "startDate" : $('#startDate').val(),
						 "endDate" : $('#endDate').val(),
						 "cnsr_div_cd" : "C",
						 "cstm_link_div_cd" : $('#cstm_link_div_cd').val()
					   },
				type : "get",
				success : function(result,textStatus,jqXHR) {

					$("#area1").html(result);
					var myLine1 = new Chart(document.getElementById("canvas_Line1").getContext("2d")).Line(data1,option1);
					var myLine2 = new Chart(document.getElementById("canvas_Line2").getContext("2d")).Line(data2,option1);
					var myLine3 = new Chart(document.getElementById("canvas_Line3").getContext("2d")).Line(data3,option1);

					

				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});
				}
			});

		}

		function searchWeek(){
			$.ajax({
				url : "<c:url value='/reporting/selectWeekReport' />",
				data : {
						 "startDate" : $('#startDate').val(),
						 "endDate" : $('#endDate').val(),
						 "cnsr_div_cd_week" : $('#cnsr_div_cd_week').val(),
						 "cstm_link_div_cd_week" : $('#cstm_link_div_cd_week').val()
					   },
				type : "post",
				success : function(result) {
					$("#area2").html(result);
					var myLine4 = new Chart(document.getElementById("canvas_Line4").getContext("2d")).Bar(data4,option1);
					var myLine7 = new Chart(document.getElementById("canvas_Line7").getContext("2d")).Bar(data7,option1);



				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});

				}
			});

		}

		function searchCtg(){

			$.ajax({
				url : "<c:url value='/reporting/selectCtgMonthlyReport' />",
				data : {
						 "startDate" : $('#startDate').val(),
						 "endDate" : $('#endDate').val(),
						 "cnsr_div_cd_ctg" : $('#cnsr_div_cd_ctg').val(),
						 "cstm_link_div_cd_ctg" : $('#cstm_link_div_cd_ctg').val()
					   },
				type : "get",
				success : function(result,textStatus,jqXHR) {

					$("#area5").html(result);
					$('.ctgTr td:first').click();

				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});

				}
			});

		}

		function goPaging(){
			$.ajax({
				url : "<c:url value='/reporting/selectScoreList' />",
				data : {
						 "startDate" : $('#startDate').val(),
						 "endDate" : $('#endDate').val(),
						 "nowPage" : $('#nowPage').val(),
						 "totPage" : $('#totPage').val()
					   },
				type : "post",
				success : function(result) {
					$("#area4").html(result);


				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});

				}
			});

		}

		function selectAdviser(member_uid){
			$.ajax({
				url : "<c:url value='/reporting/selectAdviserDetailList' />",
				data : {
						 "startDate" : $('#startDate').val(),
						 "endDate" : $('#endDate').val(),
						 "member_uid" : member_uid
					   },
				type : "post",
				success : function(result) {
						console.log(result);
					$("#adviserRight").html(result);
					var myLine5 = new Chart(document.getElementById("canvas_Line5").getContext("2d")).Line(data5,option1);
					var myLine6 = new Chart(document.getElementById("canvas_Line6").getContext("2d")).Line(data6,option1);
					var myLine8 = new Chart(document.getElementById("canvas_Line8").getContext("2d")).Line(data8,option1);

				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});

				}
			});
		}

		function selectCtgDetail(ctg_num){
			$.ajax({
				url : "<c:url value='/reporting/selectCtgDetailReport' />",
				data : {
						 "startDate" : $('#startDate').val(),
						 "endDate" : $('#endDate').val(),
						 "ctgNum" : ctg_num,
						 "cnsr_div_cd_ctg" : $('#cnsr_div_cd_ctg').val(),
						 "cstm_link_div_cd_ctg" : $('#cstm_link_div_cd_ctg').val()
					   },
				type : "post",
				success : function(result) {
					$("#ctgRight").html(result);


				},
				complete : function() {
					 $("#snb").css({"height":$('.left_content_area').height()+"px"});


				}
			});

		}

		function downloadMonthlyDayReportExcel(){

				$("#monthlyForm").attr("action", "<c:url value='/reporting/downloadMonthlyDayReportExcel' />");
				$("#monthlyForm").submit();
		}

		function downloadMonthlyWeekReportExcel(){

			$("#monthlyForm").attr("action", "<c:url value='/reporting/downloadMonthlyWeekReportExcel' />");
			$("#monthlyForm").submit();
		}

		function downloadMonthlyScoreListExcel(){

			$("#monthlyForm").attr("action", "<c:url value='/reporting/downloadScoreListExcel' />");
			$("#monthlyForm").submit();
		}

		function downloadMonthlyAdviserListExcel(){

			$("#monthlyForm").attr("action", "<c:url value='/reporting/downloadMonthlyAdviserReportExcel' />");
			$("#monthlyForm").submit();
		}

		function downloadMonthlyAdviserReportAllExcel(){
			$("#monthlyForm").attr("action", "<c:url value='/reporting/downloadMonthlyAdviserReportAllExcel' />");
			$("#monthlyForm").submit();
		}

		function downloadCtgReportExcel(){
			$("#monthlyForm").attr("action", "<c:url value='/reporting/downloadCtgReportExcel' />");
			$("#monthlyForm").submit();
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
			}).done(function(data) {
				$('.popup').hide();
				$('.popup_counseling').html(data).show();

				// 멀티형 메세지 슬라이더
				new Swiper($('.popup_counseling .chat_text_slide'), {
					slidesPerView: 'auto',
					centeredSlides: false,
					spaceBetween: 30,
				});
		    }).fail(function(jqXHR, textStatus, errorThrown) {
		    	console.error('FAIL REQUEST: REVIEW: ', textStatus);
		    });
		}
	</script>

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
		<form id="monthlyForm" name="monthlyForm" method="post" action="">
			<input type="hidden" name="member_uid" id="member_uid" />
			<div class="left_content_area">
				<div class="left_content_head">
					<h2 class="sub_tit">Monthly Reporting<%--(${sessionVo.departCd})--%></h2>
					<span class="left_top_text">월간 상담직원의 실적을 보여줍니다.</span>
				</div>
				<div class="inner">
					<div class="top_filter_area" >
						<span class="dot_tit" style="width:80px">월 검색</span>
						<div class="search_area" style="width:800px;">
							<input type="text" class="monthpicker" name="monthpicker" id="selectMonth" data-month="${monthpicker}" value="${monthpicker}" style="text-align:center;" readonly>
							<input type="hidden" name="startDate" id="startDate" value="${startDate}">
							<input type="hidden" name="endDate" id="endDate" value="${endDate}">
							<span class="option_info_text" style="margin-left:10px;"> ※ 당월 데이터의 경우 1일부터 전일까지의 데이터로 조회됩니다.</span>
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
							<button type="button" class="btn_holiday_plus" style="width:200px;">적용</button>
						</div>
					</div>
					<div class="id_info_area">
						<c:forEach var="data" items="${basicList}" varStatus="status">
							<div class="id_box">
								<span class="tit">${sessionVo.departCd} 상담 인입 수</span> <em><span><fmt:formatNumber  pattern="#,###.#" value="${empty data.cns_request_cnt ? 0 : data.cns_request_cnt}"/> 
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit"  style="background-color:#ddd">근무 시간 외</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.OUT_SERVICE_CNT ? 0 : data.OUT_SERVICE_CNT}"/> 
									<span>건</span>
								</em>
							</div>
							<div class="id_box" >
								<span class="tit" style="background-color:#ddd">배정전 상담포기 </span> <em>
								<span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_bquit_cnt ? 0 : data.cns_bquit_cnt}"></fmt:formatNumber>건</span>
								</em>
							</div>
							<div class="id_box" >
								<span class="tit" style="background-color:#ddd">배정후 상담포기</span> <em>
								<span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_quit_cnt ? 0 : data.cns_quit_cnt}"></fmt:formatNumber>건</span>
								</em>
							</div>
							<div class="id_box" >
								<span class="tit" style="background-color:#ddd">바쁜시간 종료</span> <em>
								<span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.sys_full_cnt ? 0 : data.sys_full_cnt}"></fmt:formatNumber>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">상담 진행(정상종료)수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_cnt ? 0 : data.cns_end_cnt}" />
									<span>건</span>
								</em>
							</div>
							
						</div>
						<div class="id_info_area">
							
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">자동종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_auto ? 0 : data.cns_end_auto}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">고객종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_cstm ? 0 : data.cns_end_cstm}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit" style="background-color:#ddd">상담원종료 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cns_end_cnsr ? 0 : data.cns_end_cnsr}"/>
									<span>건</span>
								</em>
							</div>
							<div class="id_box w100">
								<span class="tit">평균 상담 시간</span> <em><span>
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
								</em>
							</div>
							<div class="id_box">
								<span class="tit">상담원당 평균 상담 수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.avg_end_cnt ? 0 : data.avg_end_cnt}"/>
									건</span>
								</em>
							</div>
							<div class="id_box">
								<span class="tit">일 평균 근무 상담원</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty data.cnsr_cnt ? 0 : data.cnsr_cnt}"/>
									명</span>
								</em>
							</div>
						</c:forEach>
							<div class="id_box">
								<span class="tit">카카오톡 인입수</span> <em><span><fmt:formatNumber  pattern="#,###.#"  value="${empty kakaoDivCnt ? 0 : kakaoDivCnt}" />
									<span>건</span>
								</em>
							</div>
					</div>

					<ul class="tabs_menu_s graph">
						<li class="active" data-page="1">일자별 추이</li>
						<li data-page="2">요일별 통계</li>
						<li data-page="3">상담직원 통계</li>
						<!-- <li data-page="4">평가내역</li> -->
						<!-- <li data-page="5">분류별 통계</li>  -->
						<li data-page="6">검색어 통계</li>
					</ul>
					<!-- 일자별 통계 -->
					
					<div class="graph_area" id="area1">
						<!-- 왼쪽 테이블 영역 -->
						<div class="left_table_area">
							
							<div class="graph_top_area">
								<div class="left" style="margin-right:15px;"><h3 class="sub_stit">일별데이터</h3></div>
								<button type="button" class="btn_excel_down" id="dayDown">
									<i></i>Excel Export
								</button>
							</div>
							<div class="graph_table_area">
								<table class="tCont service">
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
									</colgroup>
									<thead>
										<tr>
											<th>상담일자</th>
											<th>상담 인입 수</th>
											<th>근무외</th>
											<th>배정전 포기</th>
											<th>배정후 포기</th>
											<th>바쁜시간 종료</th>
											<th>상담종료 수</th>
											<th>평균 상담 시간</th>
											<th>상담원수</th>
											<th>상담원당 </br> 평균 상담 수</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty dayList}">
												<tr>
													<td colspan="10">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${dayList}" varStatus="status">
													<tr>
														<td>${data.day}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.OUT_SERVICE_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_bquit_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_aquit_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.sys_full_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/> 건</td>
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
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_cnt}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt}"/> 건</td>
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
						<div class="right_graph_area">
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
					</div>
					<!--// 일자별 통계 -->

					<!-- 요일별 통계 -->
					<div class="graph_area" id="area2">
						<!-- 왼쪽 테이블 영역 -->
						<div class="left_table_area">
							
							<div class="graph_top_area">
								<div class="left">
									<h3 class="sub_stit">요일별 평균 데이터</h3>
								</div>
								<div class="left">
									<input type="hidden" name="cstm_link_div_cd_week" id="cstm_link_div_cd_week" value="0"/>
									<input type="hidden" name="cnsr_div_cd_week" id="cnsr_div_cd_week" value="C">
								</div>
								<button type="button" class="btn_excel_down" id="weekDown">
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
											<th>요일</th>
											<th>상담 인입 수</th>
											<th>근무 외</th>
											<th>배정전 포기</th>
											<th>배정후 포기</th>
											<th>바쁜시간 종료</th>
											<th>상담종료 수</th>
											<th>평균 상담 시간</th>
											<th>상담원수</th>
											<th>상담원 당 평균 상담 수</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${empty weekList}">
												<tr>
													<td colspan="10">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${weekList}" varStatus="status">
													<tr>
														<td>${data.day}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_request_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.OUT_SERVICE_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.CNS_BQUIT_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.CNS_AQUIT_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.SYS_FULL_CNT}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/> 건</td>
														<td>
														<c:if test="${data.avg_end_date *1 != 0}">
															${data.avg_end_date *1}일
														</c:if>
														<c:if test="${data.avg_end_hour != 0}">
															${data.avg_end_hour *1}시
														</c:if>
														<c:if test="${data.avg_end_min *1 != 0}">
															${data.avg_end_min *1}분
														</c:if>
															${data.avg_end_sec *1}초
														</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cnsr_cnt}"/> 명</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt}"/> 건</td>
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
						<div class="right_graph_area">
							<h3 class="sub_stit">평균 상담 수</h3>
							<canvas id="canvas_Line7" height="90"></canvas>
							<h3 class="sub_stit">평균 상담 시간</h3>
							<canvas id="canvas_Line4" height="90"></canvas>
						</div>

						<!--// 오른쪽 그래프 영역 -->
					</div>
					<!--// 요일별 통계 -->

					<!-- 상담직원 통계 -->
					<div class="graph_area" id="area3">
						<!-- 상담직원 통계 첫 리스트 단락 -->
						<h3 class="sub_stit">
							상담직원별 데이터 <span style="font-size:12px;">※상담직원명을 클릭하면 하단에서 해당 상담직원의 일별 데이터와 추이 그래프 확인이 가능합니다.</span>
							<button type="button" class="btn_excel_down floatR" id="downAdviserAll">
								<i></i>Excel Export
							</button>
						</h3>
							<div class="graph_top_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col><col><col><col><col><col><col><col><col><col>
										
									</colgroup>
									<thead>
										<tr>
											<th>담당 매니저</th>
											<th>상담직원</th>
											<th>상담진행수</th>
											<th>총 상담 시간</th>
											<th>상담 후처리 수</th>
											<th>근무일수</th>
											<th>평균 상담 시간</th>
											<th>일 평균 상담 수</th>
											<!-- <th>평가건수</th>
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
												<c:forEach var="data" items="${adviserList}"
													varStatus="status">
													<tr id="adviserTr" data-adviser="${data.member_uid}" class="adviserTr">
														<td><c:if test="${data.manager_name eq null}">-</c:if>${data.manager_name}</td>
														<td>${data.name}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}" /> 건</td>
														<td>${data.total_use_time}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.after_cnt}" /> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.work_yn}" /> 일</td>
														
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
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_evl_end_cnt}" /> 건</td>
														<%-- <td><fmt:formatNumber  pattern="#,###.#"  value="${data.evl_cnt}" /> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_evl_score}" />	점</td> --%>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						<!-- 왼쪽 테이블 영역 -->
						<div id="adviserRight">
						</div>
						
					</div>
					<!--// 상담직원 통계 -->

					<!-- 평가 통계 -->
					<div class="graph_area" id="area4">
						<input type="hidden" name="nowPage" id="nowPage" 	value="${nowPage }" /> <input type="hidden" name="totPage"
							id="totPage" value="${totPage }" />
						<h3 class="sub_stit">
							평가 데이터
							<button type="button" class="btn_excel_down floatR"
								id="scoreDown">
								<i></i>Excel Export
							</button>
						</h3>
						<div class="graph_table_area">
							<table class="tCont service">
								<caption>통계테이블 목록입니다.</caption>
								<colgroup>
									<col style=""><col style=""><col style=""><col style=""><col style=""><col style=""><col style="">
								</colgroup>
								<thead>
									<tr>
										<th>No.</th>
										<th>평가입력 일시</th>
										<th>상담종료 일시</th>
										<th>별점</th>
										<th>평가내용</th>
										<th>상담직원</th>
										<th>대화방</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${empty scoreList}">
											<tr>
												<td colspan="7">조회된 내용이 없습니다.</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach var="data" items="${scoreList}" varStatus="status">
												<tr>
													<td>${data.rnum}</td>
													<td>${data.create_dt}</td>
													<td>${data.room_end_dt}</td>
													<td>${data.evl_score} 점</td>
													<td>${data.evl_cont}</td>
													<td>${data.name}</td>
													<td>${data.chat_room_uid}</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<div class="table_bottom_area">
								<div class="btn_center_area">
									<!-- pager -->
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
									<div class="table_count">
										<span class="cont_text"><em>${totCount }</em> count(s)</span>
									</div>
									<!-- pager -->
								</div>
							</div>
						</div>


					</div>
					<!--// 별점 통계 -->

					<!-- 분류별 통계 -->
					<!-- 
					<div class="graph_area" id="area5">
					 -->
						<!-- 왼쪽 테이블 영역 -->
					<!--	
							<h3 class="sub_stit">분류별 데이터</h3>
							<div class="graph_top_area">
								<div class="left">
									<input type="hidden" name="cnsr_div_cd_ctg" id="cnsr_div_cd_ctg" value="0">
									<input type="hidden" name="cstm_link_div_cd_ctg" id="cstm_link_div_cd_ctg" value="A"/>
					 -->
									<!--
									<select name="cstm_link_div_cd" id="cstm_link_div_cd">
										<option value="0">전체</option>
										<option value="A">WEB</option>
										<option value="B">카카오톡</option>
										<option value="C">네이버톡톡</option>
									</select>
									<button type="button" class="btn_search" id="daySearch">검색</button>
									-->
					<!--
								</div>
								<button type="button" class="btn_excel_down" id="ctgDown">
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
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>대분류</th>
											<th>중분류</th>
											<th>소분류</th>
											<th>상담수</th>
											<th>상담직원당 평균 상담 수</th>
											<th>상담직원당 평균 상담 시간</th>
											<th>평가 건수</th>
											<th>평균 별점</th>
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
													<tr class="ctgTr" data-ctg="${data.ctg_num}">
														<td>${data.ctg1.replaceAll(">>", "")}</td>
														<td>${data.ctg2.replaceAll(">>", "")}</td>
														<td><c:if test="${data.ctg3 eq null}">-</c:if>${data.ctg3.replaceAll(">>", "")}</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.cns_end_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_end_cnt}"/> 건</td>
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
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.total_evl_cnt}"/> 건</td>
														<td><fmt:formatNumber  pattern="#,###.#"  value="${data.avg_evl_score}"/>점</td>
														
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>

							</div>
						-->
						<!--// 왼쪽 테이블 영역 -->

					<!-- </div>  -->
					<!-- 분류별 통계 -->
					
					<!-- 검색어 통계 -->
					<div class="graph_area" id="area6" style="display:none;">
							<h3 class="sub_stit">
								용어검색 개수가 0인 데이터
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>순위</th>
											<th>검색어</th>
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty TermRankMonZList}">
												<tr>
													<td colspan="2">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${TermRankMonZList}" varStatus="status">
													<tr>
														<td>${data.ranking}</td>
														<td>${data.search_text}</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
							
							<h3 class="sub_stit">
								용어검색 개수가 0보다 큰 데이터
							</h3>
							<div class="graph_table_area">
								<table class="tCont service">
									<caption>통계테이블 목록입니다.</caption>
									<colgroup>
										<col style="">
										<col style="">
									</colgroup>
									<thead>
										<tr>
											<th>순위</th>
											<th>검색어</th>
										</tr>
									</thead>
									<tbody>

										<c:choose>
											<c:when test="${empty TermRankMonUList}">
												<tr>
													<td colspan="2">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${TermRankMonUList}" varStatus="status">
													<tr>
														<td>${data.ranking}</td>
														<td>${data.search_text}</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</tbody>
								</table>
							</div>
						
						<!--// 왼쪽 테이블 영역 -->
					</div>
					<!--// 검색어 통계 -->
					
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
