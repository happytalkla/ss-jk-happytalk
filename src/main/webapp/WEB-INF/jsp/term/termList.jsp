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
			$(".dev_term_pop").hide();
		});
	
		// 검색창 엔터 처리
		$(document).on("keypress", "#termForm [name='schText']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#termForm .btn_search").trigger("click");
			}
		});
		
		//페이지별 갯수 클릭
		$(document).on("change", "#termForm [name='pageListCount']", function(e) {
			$("#termForm [name='nowPage']").val("1");
			goSearch();
		});		
		
		// 초기화 하기 클릭
		$(document).on("click", "#termForm .dev_reset_btn", function() {
			$("#termForm [name='schType']").val("A");
			$("#termForm [name='schText']").val("");
			$("#termForm [name='nowPage']").val("1");
			goSearch();
		});
		
		// 검색하기 클릭
		$(document).on("click", "#termForm .btn_search", function() {
			/*
			var schText = $("#termForm [name='schText']").val().trim();
			$("#termForm [name='schText']").val(schText);
			if(schText == ""){
				alert("검색어를 입력해 주세요.");
				$("#termForm [name='schText']").focus();
				return false;
			}
			 */

			//$("#termForm [name='termDivCdPageVal']").val($("#termForm [name='termDivCd']:checked").val());
			$("#termForm [name='nowPage']").val("1");
			goSearch();
		});
		
		// 검색하기
		function goSearch(){
			$("#termForm").attr("action", "<c:url value='/term/termList' />");
			$("#termForm").submit();
		}

		// 용어 사전 등록 팝업창 열기
		$(document).on("click", "#termForm .dev_openInsertPop", function(e) {
			
			$.ajax({
				url : "<c:url value='/term/addTerm' />",
				type : "post",
				success : function(result) {
					$(".dev_term_pop").html(result);
					$(".dev_term_pop").show();
					
					$(".dev_term_pop .inner").css("height","420px");
				},
				complete : function() {
				}
			});
		});

		// 용어 사전 수정 팝업창 열기
		$(document).on("click", "#termViewPopForm .dev_openEditPop", function(e) {
			var termNum = $("#termViewPopForm [name='termNum']").val();

			$.ajax({
				url : "<c:url value='/term/editTerm' />",
				type : "post",
				data : {"termNum" : termNum },
				success : function(result) {
					$(".dev_term_pop").html(result);
					$(".dev_term_pop").show();
					
					$(".dev_term_pop .inner").css("height","420px");
				},
				complete : function() {
				}
			});
		});

		//삭제버튼
		$(document).on("click", ".dev_delete_pop", function(e) {
			if(confirm("이 용어를 사전에서 삭제하시겠습니까?")){
				var termNum = $("#termViewPopForm [name='termNum']").val();
	
				$.ajax({
					url : "<c:url value='/term/deleteTerm' />",
					data : {"termNum" : termNum },
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
				$(".dev_term_pop").hide();
			}
		});
		
		// 팝업창 닫기
		$(document).on("click", ".dev_close_pop", function(e) {
			$(".dev_term_pop").hide();
		});


		// 이전 클릭
		$(document).on("click", "#termForm .dev_prev_btn", function() {
			var nowPage = $("#termForm [name='nowPage']").val()*1 - 1;
			if(nowPage <= 0){
				return false;
			}
			//$("#termForm [name='searchVal']").val($("#termForm [name='searchValPage']").val());
			$("#termForm [name='nowPage']").val(nowPage);
			goSearch();
		});

		// 이후 클릭
		$(document).on("click", "#termForm .dev_next_btn", function() {
			var nowPage = $("#termForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#termForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}
			//$("#termForm [name='searchVal']").val($("#termForm [name='searchVal']").val());
			$("#termForm [name='nowPage']").val(nowPage);
			goSearch();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#termForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#termForm [name='nowPage']").val($(this).val());
				goSearch();
			}
		});
		
		//용어사전 상세보기
		$(document).on("click", "#termForm .dev_click_tr", function() {
			var termNum = $(this).attr("data-termuid");

			$.ajax({
				url : "<c:url value='/term/viewTerm' />",
				type : "post",
				data : {"termNum" : termNum },
				success : function(result) {
					$(".dev_term_pop").html(result);
					$(".dev_term_pop").show();
					$(".dev_term_pop .inner").css("height","420px");
				},
				complete : function() {
				}
			});
		});

		// 용어 사전 등록
		$(document).on("click", "#termPopForm .dev_insert_btn", function(e) {
			goSave();
		});

		// 용어 사전 수정
		$(document).on("click", "#termPopForm .dev_update_btn", function(e) {
			goSave();
		});

		//엑셀 다운로드
		$(document).on("click", "#excelDown", function(e){
			$("#termForm").attr("action", "<c:url value='/term/downloadTermExcel' />");
			$("#termForm").submit();
		});

		function goSave(){
			
			if ($("#termPopForm [name='termDivNm']").val().trim() == '' || $("#termPopForm [name='termDivNm']").val().trim() == 'null') {
				alert("구분항목을 입력해주세요.");
				return;
			}
			if ($("#termPopForm [name='title']").val().trim() == '' || $("#termPopForm [name='title']").val().trim() == 'null') {
				alert("제목을 입력해주세요.");
				return;
			}
			if ($("#termPopForm [name='cont']").val().trim() == '' || $("#termPopForm [name='cont']").val().trim() == 'null') {
				alert("내용을 입력해세요.");
				return;
			}
			if($("#termPopForm [name='cont']").val().length >= 2000){
				alert("용어사전의 내용은 2,000자 이하로 입력해주세요.");
				return;
			}

			$.ajax({
				url : "<c:url value='/term/saveTerm' />",
				data : $("#termPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_term_pop").hide();
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
	 .text_term_ellipsis{
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
				<h2 class="sub_tit">용어 사전 관리</h2>
			</div>	
			<form id="termForm" name="termForm" method="post" action="">
				<input type="hidden" name="nowPage" value="${nowPage }" />
				<input type="hidden" name="totPage" value="${totPage }" />
				
				<div class="inner">
					<div class="top_filter_area">
						<span class="dot_tit">검색어</span>
						<div class="search_area">
							<select name="schType" style="width:270px;">
								<option value="A" <c:if test="${param.schType eq 'A' }">selected</c:if> >전체</option>							
								<option value="I" <c:if test="${param.schType eq 'I' }">selected</c:if> >ID</option>
								<option value="D" <c:if test="${param.schType eq 'D' }">selected</c:if> >구분</option>
								<option value="T" <c:if test="${param.schType eq 'T' }">selected</c:if> >제목</option>
								<option value="C" <c:if test="${param.schType eq 'C' }">selected</c:if> >내용</option>
								<option value="G" <c:if test="${param.schType eq 'G' }">selected</c:if> >태그</option>								
							</select>
							<div class="search_area_right">
								<input type="text" name="schText" value="${schText}" /><button type="button" class="btn_search dev_search_btn">검색하기</button>
							</div>
						</div>
						&nbsp;&nbsp;&nbsp;	<button type="button" class="btn_go_insert dev_reset_btn">초기화</button>
					</div>
					<div class="btn_staff_area">
						<button type="button" class="btn_plus_manager dev_openInsertPop"><i></i>용어 신규 등록</button>
							<select name="pageListCount" style="width:200px; height: 32px;">
								<option value="20" <c:if test="${param.pageListCount eq '20' }">selected</c:if> >20개 보기</option>							
								<option value="50" <c:if test="${param.pageListCount eq '50' }">selected</c:if> >50개 보기</option>
								<option value="100" <c:if test="${param.pageListCount eq '100' }">selected</c:if> >100개 보기</option>							
							</select>			
						<div style="float:right">
							<button type="button" class="btn_excel_down" id="excelDown" style="padding: 0 5px 0 5px; width: auto;">
								<i></i>Excel Download
							</button>
						</div>
					</div>			
								
					<table class="tCont service">
						<caption>용어 사전 목록입니다.</caption>
						<colgroup>
							<col style="width:5%">
							<col style="width:10%">
							<col style="width:15%">
							<col style="width:">
							<col style="width:10%">
						</colgroup>
						<thead>
							<tr>
								<th>ID</th>
								<th>구분</th>
								<th>제목</th>
								<th>내용</th>
								<th>업데이트 일자</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
									<c:when test="${empty termList}">
										<tr>
											<td colspan="9">조회된 내용이 없습니다.</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach var="data" items="${termList }" varStatus="status">
											<tr class="dev_click_tr" data-termuid="${data.term_num }">
												<td>${data.term_num }</td>
												<td style="text-align: left !important;"><c:out value="${data.term_div_nm }"/></td>
												<td><div class="text_term_ellipsis"><c:out value="${data.title }"/></div></td>
												<td><div class="text_term_ellipsis"><c:out value="${data.cont }"/></div></td>
												<td>${data.update_dt }</td>
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
					</div>
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>
<!-- popup:신규 등록 -->
<div class="popup dev_term_pop">
</div>
<!--// popup:신규 등록  -->

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->
</body>
</html>
