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
 

		// 검색창 엔터 처리
		$(document).on("keypress", "#noticeForm [name='searchVal']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#noticeForm .btn_search").trigger("click");
			}
		});

		// 검색하기 클릭
		$(document).on("click", "#noticeForm .btn_search", function() {
			/*
			var schText = $("#noticeForm [name='schText']").val().trim();
			$("#noticeForm [name='schText']").val(schText);
			if(schText == ""){
				alert("검색어를 입력해 주세요.");
				$("#noticeForm [name='schText']").focus();
				return false;
			}
			 */

			$("#noticeForm [name='noticeDivCdPageVal']").val($("#noticeForm [name='noticeDivCd']:checked").val());
			$("#noticeForm [name='nowPage']").val("1");
			goSearch();
		});

		// 등록하기 클릭
		$(document).on("click", "#noticeForm .btn_holiday_plus", function() {

			goAddNotice();

		});

		// 이전 클릭
		$(document).on("click", "#noticeForm .dev_prev_btn", function() {
			var nowPage = $("#noticeForm [name='nowPage']").val()*1 - 1;
			if(nowPage <= 0){
				return false;
			}
			//$("#noticeForm [name='searchVal']").val($("#noticeForm [name='searchValPage']").val());
			$("#noticeForm [name='nowPage']").val(nowPage);
			goSearch();
		});

		// 이후 클릭
		$(document).on("click", "#noticeForm .dev_next_btn", function() {
			var nowPage = $("#noticeForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#noticeForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}
			//$("#noticeForm [name='searchVal']").val($("#noticeForm [name='searchVal']").val());
			$("#noticeForm [name='nowPage']").val(nowPage);
			goSearch();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#noticeForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#noticeForm [name='inputNowPage']").trigger("change");
			}
		});



		// 공지 분류 클릭
		$(document).on("click", "#noticeForm [name='noticeDivCd']", function() {
			$("#noticeForm [name='noticeDivCdPageVal']").val($("#noticeForm [name='noticeDivCd']:checked").val());
			$("#noticeForm [name='searchVal']").val('');
			$("#noticeForm [name='nowPage']").val('1');

			goSearch();
		});


		// 페이지 입력창 입력
		$(document).on("change", "#noticeForm [name='inputNowPage']", function() {
			var nowPage = $("#noticeForm [name='nowPage']").val();
			var inputNowPage = $(this).val()*1;
			var totPage = $("#noticeForm [name='totPage']").val()*1;

			if(!$.isNumeric(inputNowPage)){
				alert("숫자만 입력해 주세요.");
				$("#noticeForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage <= 0){
				alert("1페이지 부터 입력해 주세요");
				$("#noticeForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage > totPage){
				alert("최대 페이지가 "+totPage+"페이지 입니다.");
				$("#noticeForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			$("#noticeForm [name='nowPage']").val(inputNowPage);
			goSearch();
		});

		//공지사항 상세보기
		$(document).on("click", "#noticeForm .dev_click_tr", function() {
			var notice_num = $(this).attr("data-noticeuid");
			$("#noticeForm [name='notice_num']").val(notice_num);
			$("#noticeForm").attr("action", "<c:url value='/notice/viewNotice' />");
			$("#noticeForm").submit();
		});

		// 검색하기
		function goSearch(){
			$("#noticeForm").attr("action", "<c:url value='/notice/noticeList' />");
			$("#noticeForm").submit();
		}

		//등록하기
		function goAddNotice(){
			$("#noticeForm").attr("action", "<c:url value='/notice/addNotice' />");
			$("#noticeForm").submit();
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
			<div class="left_content_head">
				<h2 class="sub_tit">공지사항</h2>
				<%-- 테스트 <h1><spring:message code="greeting" text="default"/></h1>  --%>
				 
			</div>
			<form id="noticeForm" name="noticeForm" method="post" action="">
				<input type="hidden" name="nowPage" value="${nowPage }" />
				<input type="hidden" name="totPage" value="${totPage }" />
				<input type="hidden" name="notice_num" value="" />
				<input type="hidden" name="noticeDivCdPageVal" value="${noticeDivCd }" />
				<input type="hidden" name="searchValPage" value="${searchValPage }" />
				<div class="inner">
					<div class="top_filter_area">
						<ul class="fliter_row">
							<li>
								<span class="dot_tit02">공지</span>
								<div class="search_area">
									<div class="category_radio_area">
										<input type="radio" class="radio_18" id="notice01" name="noticeDivCd" value="ALL" <c:if test="${param.noticeDivCd eq 'ALL' or param.noticeDivCd == null}">checked</c:if>>
										<label for="notice01"><span></span>전체</label>
										<c:choose>
											<c:when test="${empty noticeDivList}">
												<tr>
													<td colspan="9">조회된 내용이 없습니다.</td>
												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="data" items="${noticeDivList }" varStatus="status">
													<input type="radio" class="radio_18" id="notice${status.index}" name="noticeDivCd" value="${data.cd }" <c:if test="${param.noticeDivCd eq data.cd }">checked</c:if>>
													<label for="notice${status.index}"><span></span>${data.cd_nm }</label>
												</c:forEach>
											</c:otherwise>
										</c:choose>

									</div>
								</div>
							</li>
							<li>
								<span class="dot_tit02">검색어</span>
								<div class="search_area">
									<div class="search_area_right">
										<input type="text" name="searchVal" value="${searchVal}" maxlength=50 />
										<button type="button" class="btn_search">검색하기</button>
									</div>
								</div>
							</li>
						</ul>
					</div>

					<table class="tCont service">
						<caption>공지사항 목록입니다.</caption>
						<colgroup>
							<col style="width:15%">
							<col style="width:">
							<col style="width:15%">
							<col style="width:15%">
						</colgroup>
						<thead>
							<tr>
								<th>공지</th>
								<th>제목</th>
								<th>등록일</th>
								<th>등록자</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
									<c:when test="${empty noticeList}">
										<tr>
											<td colspan="9">조회된 내용이 없습니다.</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach var="data" items="${noticeList }" varStatus="status">
											<tr class="dev_click_tr" data-noticeuid="${data.notice_num }">
												<td>${data.notice_div_nm }</td>
												<td>${data.title }</td>
												<td>${data.reg_dt }</td>
												<td>${data.member_name }</td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
						</tbody>
					</table>

					<div class="btn_center_area">
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
						<button type="button" class="btn_holiday_plus">등록</button>
					</div>
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>


</body>
</html>
