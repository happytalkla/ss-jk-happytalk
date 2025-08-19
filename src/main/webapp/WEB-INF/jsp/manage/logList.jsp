<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

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
	<script type="text/javascript" src="<c:url value='/js/common.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

	<script type="text/javascript">

		$(document).ready(function() {
		});


		// 검색창 엔터 처리
		$(document).on("keypress", "#logForm [name='schText']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#logForm .dev_search_btn").trigger("click");
			}
		});

		// 검색하기 클릭
		$(document).on("click", "#logForm .dev_search_btn", function() {
			/*
			var schText = $("#logForm [name='schText']").val().trim();
			$("#logForm [name='schText']").val(schText);
			if(schText == ""){
				alert("검색어를 입력해 주세요.");
				$("#logForm [name='schText']").focus();
				return false;
			}
			 */
			$("#logForm [name='nowPage']").val("1");
			goSearch();
		});

		// 이전 클릭
		$(document).on("click", "#logForm .dev_prev_btn", function() {
			var nowPage = $("#logForm [name='nowPage']").val()*1 - 1;
			if(nowPage <= 0){
				return false;
			}

			$("#logForm [name='nowPage']").val(nowPage);
			goSearch();
		});

		// 이후 클릭
		$(document).on("click", "#logForm .dev_next_btn", function() {
			var nowPage = $("#logForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#logForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}

			$("#logForm [name='nowPage']").val(nowPage);
			goSearch();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#logForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#logForm [name='inputNowPage']").trigger("change");
			}
		});

		// 페이지 입력창 입력
		$(document).on("change", "#logForm [name='inputNowPage']", function() {
			var nowPage = $("#logForm [name='nowPage']").val();
			var inputNowPage = $(this).val()*1;
			var totPage = $("#logForm [name='totPage']").val()*1;

			if(!$.isNumeric(inputNowPage)){
				alert("숫자만 입력해 주세요.");
				$("#logForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage <= 0){
				alert("1페이지 부터 입력해 주세요");
				$("#logForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage > totPage){
				alert("최대 페이지가 "+totPage+"페이지 입니다.");
				$("#logForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			$("#logForm [name='nowPage']").val(inputNowPage);
			goSearch();
		});

		// 검색하기
		function goSearch(){
			$("#logForm").attr("action", "<c:url value='/manage/selectLogList' />");
			$("#logForm").submit();
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
		<div class="left_content_area">
			<form id="logForm" name="logForm" method="post" action="">
				<input type="hidden" name="nowPage" value="${nowPage }" />
				<input type="hidden" name="totPage" value="${totPage }" />
				<div class="left_content_head">
					<h2 class="sub_tit">서비스 이용내역</h2>
				</div>
				<div class="inner">
					<div class="top_filter_area">
						<span class="dot_tit">검색어</span>
						<div class="search_area">
							<select class="w150" id="schLogDivCd" name="schLogDivCd">
								<option value="">전체</option>
								<c:forEach var="data" items="${logDivCdList }" varStatus="status">
									<option value="${data.cd }" <c:if test="${param.schLogDivCd eq data.cd }">selected</c:if> >${data.cd_nm }</option>
								</c:forEach>
							</select>
							<select name="schType">
								<option value="USER" <c:if test="${param.schType eq 'USER' }">selected</c:if> >신청자</option>
								<option value="CONT" <c:if test="${param.schType eq 'CONT' }">selected</c:if> >이용내역</option>
							</select>
							<div class="search_area_right">
								<input name="schText" type="text"  value="${schText}"><button type="button" class="btn_search dev_search_btn">검색하기</button>
							</div>
						</div>
					</div>

					<table class="tCont service">
						<caption>서비스 이용내역 목록입니다.</caption>
						<colgroup>
							<col style="width:15%">
							<col style="width:10%">
							<col style="width:15%">
							<col style="width:50%">
							<col style="width:10%">
						</colgroup>
						<thead>
							<tr>
								<th>신청일시<br>(작업시작일시)</th>
								<th>신청자</th>
								<th>이용 구분</th>
								<th>이용 내역</th>
								<th>이용 결과</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty logList}">
									<tr>
										<td colspan="5">조회된 내용이 없습니다.</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="data" items="${logList }" varStatus="status">
										<tr class="dev_click_tr" data-cstmUid="${data.cstm_uid }">
											<td>${data.create_dt }</td>
											<td>${data.name }</td>
											<td>${data.log_div_nm }</td>
											<td><c:out value="${data.log_cont }"/></td>
											<td>${data.succ_text }</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<div class="table_bottom_area">
						<!-- pager -->
						<div class="pager">
							<button type="button" class="btn_prev_page dev_prev_btn">이전으로</button>
							<input type="text" class="form_pager" value="${nowPage }" name="inputNowPage">
							<span class="page_no">/ ${totPage } page(s)</span>
							<button type="button" class="btn_next_page dev_next_btn">다음으로</button>
						</div>
						<!--// pager -->
						<div class="table_count">
							<span class="cont_text"><em>${totCount }</em> count(s)</span>
						</div>
					</div>
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

</body>
</html>
