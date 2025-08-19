<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/include.css' />" />
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

	<script type="text/javascript">
 
		$(document).ready(function() {
			$(".dev_screen_pop").hide();
		});
	
		// 검색창 엔터 처리
		$(document).on("keypress", "#screenForm [name='schText']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#screenForm .btn_search").trigger("click");
			}
		});
		
		// 초기화 하기 클릭
		$(document).on("click", "#screenForm .dev_reset_btn", function() {
			$("#screenForm [name='schType']").val("A");
			$("#screenForm [name='schText']").val("");
			$("#screenForm [name='nowPage']").val("1");
			goSearch();
		});
		
		// 검색하기 클릭
		$(document).on("click", "#screenForm .btn_search", function() {
			goSearch();
		});
		function goSearch(){
			$("#screenForm").attr("action", "<c:url value='/appMgr/screenList' />");
			$("#screenForm").submit();
		}
		// 화면 번호 등록 팝업창 열기
		$(document).on("click", "#screenForm .dev_openInsertPop", function(e) {
			
			$.ajax({
				url : "<c:url value='/appMgr/addScreen' />",
				type : "post",
				success : function(result) {
					$(".dev_screen_pop").html(result);
					$(".dev_screen_pop").show();
					
					$(".dev_screen_pop .inner").css("height","250px");
					$(".dev_screen_pop .inner").css("width","620px");
				},
				complete : function() {
				}
			});
		});

		// 화면 번호 수정 팝업창 열기
		$(document).on("click", "#screenViewPopForm .dev_openEditPop", function(e) {
			var screenNum = $("#screenViewPopForm [name='screenNum']").val();

			$.ajax({
				url : "<c:url value='/appMgr/editScreen' />",
				type : "post",
				data : {"screenNum" : screenNum },
				success : function(result) {
					$(".dev_screen_pop").html(result);
					$(".dev_screen_pop").show();
					
					$(".dev_screen_pop .inner").css("height","250px");
					$(".dev_screen_pop .inner").css("width","620px");
				},
				complete : function() {
				}
			});
		});

		//삭제버튼
		$(document).on("click", ".dev_delete_pop", function(e) {
			if(confirm("이 화면를 사전에서 삭제하시겠습니까?")){
				var screenNum = $("#screenViewPopForm [name='screenNum']").val();
	
				$.ajax({
					url : "<c:url value='/appMgr/deleteScreen' />",
					data : {"screenNum" : screenNum },
					type : "post",
					success : function(result) {
						if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
							// 성공
							//fn_layerMessage(result.rtnMsg);
							goSearch();
						}else{
							fn_layerMessage(result.rtnMsg);
						}
					},
					complete : function() {
					}
				});
			}
		});
		
		//취소버튼 닫기			
		$(document).on("click", ".dev_cancel_pop", function(e) {
			if(confirm("저장하지 않고 닫으시겠습니까?")){
				$(".dev_screen_pop").hide();
			}
		});
		
		// 팝업창 닫기
		$(document).on("click", ".dev_close_pop", function(e) {
			$(".dev_screen_pop").hide();
		});


		// 이전 클릭
		$(document).on("click", "#screenForm .dev_prev_btn", function() {
			var nowPage = $("#screenForm [name='nowPage']").val()*1 - 1;
			if(nowPage <= 0){
				return false;
			}
			//$("#screenForm [name='searchVal']").val($("#screenForm [name='searchValPage']").val());
			$("#screenForm [name='nowPage']").val(nowPage);
			goSearch();
		});

		// 이후 클릭
		$(document).on("click", "#screenForm .dev_next_btn", function() {
			var nowPage = $("#screenForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#screenForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}
			//$("#screenForm [name='searchVal']").val($("#screenForm [name='searchVal']").val());
			$("#screenForm [name='nowPage']").val(nowPage);
			goSearch();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#screenForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#screenForm [name='nowPage']").val($(this).val());
				goSearch();
			}
		});
		
		//화면사전 상세보기
		$(document).on("click", "#screenForm .dev_click_tr", function() {
			var screenNum = $(this).attr("data-screenuid");

			$.ajax({
				url : "<c:url value='/appMgr/viewScreen' />",
				type : "post",
				data : {"screenNum" : screenNum },
				success : function(result) {
					$(".dev_screen_pop").html(result);
					$(".dev_screen_pop").show();
					$(".dev_screen_pop .inner").css("height","250px");
					$(".dev_screen_pop .inner").css("width","620px");
				},
				complete : function() {
				}
			});
		});

		// 화면 번호 등록
		$(document).on("click", "#screenPopForm .dev_insert_btn", function(e) {
			goSave();
		});

		// 화면 번호 수정
		$(document).on("click", "#screenPopForm .dev_update_btn", function(e) {
			goSave();
		});

		//엑셀 다운로드
		$(document).on("click", "#excelDown", function(e){
			$("#screenForm").attr("action", "<c:url value='/appMgr/downloadScreenExcel' />");
			$("#screenForm").submit();
		});

		function goSave(){
			
			if ($("#screenPopForm [name='screenNum']").val().trim() == '' || $("#screenPopForm [name='screenNum']").val().trim() == 'null') {
				alert("화면 번호를 입력해주세요.");
				return;
			}
			if ($("#screenPopForm [name='screenName']").val().trim() == '' || $("#screenPopForm [name='screenName']").val().trim() == 'null') {
				alert("화면 이름을 입력해주세요.");
				return;
			}
			
			if ($("#screenPopForm [name='screenId']").val().trim() == '' || $("#screenPopForm [name='screenId']").val().trim() == 'null') {
				alert("화면 아이디를 입력해주세요.");
				return;
			}

			$.ajax({
				url : "<c:url value='/appMgr/saveScreen' />",
				data : $("#screenPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_screen_pop").hide();
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		}
		
	</script>
	<style>
	 .text_screen_ellipsis{
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: normal;
		text-align: left !important;
		word-wrap: break-word;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
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
		<div class="left_content_area">
			<div class="left_content_head">
				<h2 class="sub_tit">화면 번호 관리</h2>
			</div>	
			<form id="screenForm" name="screenForm" method="post" action="">
				<div class="inner">
					<div class="top_filter_area">
						<span class="dot_tit">검색어</span>
						<div class="search_area">
							<select name="schType" style="width:120px;">
								<option value="">:: 선택 ::</option>							
								<option value="I" <c:if test="${param.schType eq 'I' }">selected</c:if> >화면번호</option>
								<option value="T" <c:if test="${param.schType eq 'T' }">selected</c:if> >화면명</option>
							</select>
							<div class="search_area_right">
								<input type="text" name="schText" value="${schText}" /><button type="button" class="btn_search dev_search_btn">검색하기</button>
							</div>
						</div>

					</div>
					<div class="btn_staff_area">
						<button type="button" class="btn_plus_manager dev_openInsertPop"><i></i>화면 신규 등록</button>
						<div style="float:right">
							
						</div>
					</div>			
								
					<table class="tCont service">
						<caption>화면 번호 목록입니다.</caption>
						<colgroup>
							<col style="width:15%">
							<col style="width:">
							<col style="width:15%">
						</colgroup>
						<thead>
							<tr>
								<th>화면번호</th>
								<th>화면이름</th>
								<th>화면ID</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
									<c:when test="${empty screenList}">
										<tr>
											<td colspan="3">조회된 내용이 없습니다.</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach var="data" items="${screenList }" varStatus="status">
											<tr class="dev_click_tr" data-screenuid="${data.screen_num }">
												<td>${data.screen_num }</td>
												<td style="text-align: left !important;"><c:out value="${data.screen_name }"/></td>
												<td><div class="text_screen_ellipsis"><c:out value="${data.screen_id }"/></div></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
						</tbody>
					</table>

					<div class="btn_center_area">
					</div>
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>
<!-- popup:신규 등록 -->
<div class="popup dev_screen_pop">
</div>
<!--// popup:신규 등록  -->

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->
</body>
</html>
