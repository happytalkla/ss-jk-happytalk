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
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

	<script type="text/javascript">

		$(document).ready(function() {
			$(".dev_grade_pop").hide();
		});


		// 검색창 엔터 처리
		$(document).on("keypress", "#gradeForm [name='schText']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#gradeForm .dev_search_btn").trigger("click");
			}
		});

		// 검색하기 클릭
		$(document).on("click", "#gradeForm .dev_search_btn", function() {
			/*
			var schText = $("#gradeForm [name='schText']").val().trim();
			$("#gradeForm [name='schText']").val(schText);
			if(schText == ""){
				alert("검색어를 입력해 주세요.");
				$("#gradeForm [name='schText']").focus();
				return false;
			}
			 */
			$("#gradeForm [name='nowPage']").val("1");
			goSearch();
		});

		// 이전 클릭
		$(document).on("click", "#gradeForm .dev_prev_btn", function() {
			var nowPage = $("#gradeForm [name='nowPage']").val()*1 - 1;
			if(nowPage <= 0){
				return false;
			}

			$("#gradeForm [name='nowPage']").val(nowPage);
			goSearch();
		});

		// 이후 클릭
		$(document).on("click", "#gradeForm .dev_next_btn", function() {
			var nowPage = $("#gradeForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#gradeForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}

			$("#gradeForm [name='nowPage']").val(nowPage);
			goSearch();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#gradeForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#gradeForm [name='inputNowPage']").trigger("change");
			}
		});

		// 페이지 입력창 입력
		$(document).on("change", "#gradeForm [name='inputNowPage']", function() {
			var nowPage = $("#gradeForm [name='nowPage']").val();
			var inputNowPage = $(this).val()*1;
			var totPage = $("#gradeForm [name='totPage']").val()*1;

			if(!$.isNumeric(inputNowPage)){
				alert("숫자만 입력해 주세요.");
				$("#gradeForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage <= 0){
				alert("1페이지 부터 입력해 주세요");
				$("#gradeForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage > totPage){
				alert("최대 페이지가 "+totPage+"페이지 입니다.");
				$("#gradeForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			$("#gradeForm [name='nowPage']").val(inputNowPage);
			goSearch();
		});

		// 검색하기
		function goSearch(){
			$("#gradeForm").attr("action", "<c:url value='/manage/selectUserGradeList' />");
			$("#gradeForm").submit();
		}

		// 코끼리 정보 팝업 열기
		$(document).on("click", "#gradeForm .dev_click_tr", function() {
			var cstmUid = $(this).attr("data-cstmUid");
			$.ajax({
				url : "<c:url value='/manage/selectUserGrade' />",
				data : {"cstmUid" : cstmUid},
				type : "post",
				success : function(result) {
					$(".dev_grade_pop").html(result);
					$(".dev_grade_pop").show();
				},
				complete : function() {
				}
			});
		});

		// 코끼리 정보 팝업 닫기
		$(document).on("click", "#popupForm .btn_popup_close", function() {
			$(".dev_grade_pop").hide();
		});

		// 팝업 등록 사유 수정
		$(document).on("click", "#popupForm .dev_edit_btn", function() {
			$.ajax({
				url : "<c:url value='/manage/updateUserGrade' />",
				data : $("#popupForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_grade_pop").hide();
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 팝업 등록 사유 삭제
		$(document).on("click", "#popupForm .dev_delete_btn", function() {
			$.ajax({
				url : "<c:url value='/manage/deleteUserGrade' />",
				data : $("#popupForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_grade_pop").hide();
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});



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
			<form id="gradeForm" name="gradeForm" method="post" action="">
				<input type="hidden" name="nowPage" value="${nowPage }" />
				<input type="hidden" name="totPage" value="${totPage }" />
				<div class="left_content_head">
					<h2 class="sub_tit">코끼리 관리</h2>
				</div>
				<div class="inner">
					<div class="top_filter_area">
						<span class="dot_tit">검색어</span>
						<div class="search_area">
							<select name="schType">
								<option value="CSTM" <c:if test="${param.schType eq 'CSTM' }">selected</c:if> >고객명</option>
								<option value="CNSR" <c:if test="${param.schType eq 'CNSR' }">selected</c:if> >상담직원명</option>
							</select>
							<div class="search_area_right">
								<input type="text" name="schText" value="${schText}"><button type="button" class="btn_search dev_search_btn" />검색하기</button>
							</div>
						</div>
					</div>

					<table class="tCont service black">
						<caption>블랙리스트 고객 관리 목록입니다.</caption>
						<colgroup>
							<col style="width:10%">
							<col style="width:20%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:30%">
							<col style="">
						</colgroup>
						<thead>
							<tr>
								<th>등록일시</th>
								<th>고객번호</th>
								<th>고객명</th>
								<th>상담직원</th>
								<th>코끼리명</th>
								<th>코끼리 등록사유</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty gradeList}">
									<tr>
										<td colspan="6">조회된 내용이 없습니다.</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="data" items="${gradeList }" varStatus="status">
										<tr class="dev_click_tr" data-cstmUid="${data.cstm_uid }">
											<td>${data.grade_reg_dt }</td>
											<td>${data.view_id }</td>
											<td>${data.cstm_nm }</td>
											<td>${data.member_nm }</td>
											<td>${data.grade_nm }</td>
											<td><c:out value="${data.grade_memo }"/></td>
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

<!-- popup:코끼리 정보 변경-->
<div class="popup popup_black dev_grade_pop">
</div>
<!--// popup:코끼리 정보 변경 -->

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

</body>
</html>
