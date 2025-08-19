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
<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#delPopForm [name='tplCtgNum']").val("");
		$("#delPopForm [name='tplCtgNm']").val("");
		$("#delPopForm #tplCount").text("");
		$(".dev_category_del_pop").hide();
	});

	//삭제버튼 클릭
	$(document).on(
			"click",
			"#noticeForm .btn_go_cancel",
			function() {

				$("#delPopForm [name='tplCtgNum']").val(
						$("#noticeForm [name='title']").val());
				$("#delPopForm [name='tplCtgNm']").val(
						$("#noticeForm [name='title']").val());
				$(".dev_category_del_pop").show();

			});

	//삭제확정버튼 클릭
	$(document).on(
			"click",
			"#delPopForm .dev_btn_del_pop",
			function() {
				$("#noticeForm").attr("action",
						"<c:url value='/notice/deleteNotice' />");
				$("#noticeForm").submit();
			});

	// 카테고리 삭제 팝업 닫기
	$(document).on("click", "#delPopForm .btn_popup_close", function() {
		fn_closeDelPop();
	});

	// 카테고리 삭제 팝업 닫기
	$(document).on("click", "#delPopForm .btn_close_pop", function() {
		fn_closeDelPop();
	});

	// 목록버튼 클릭
	$(document).on(
			"click",
			"#btn_go_back",
			function() {
				$("#noticeForm").attr("action",
						"<c:url value='/notice/noticeList' />");
				$("#noticeForm").submit();
			});
	// 수정버튼 클릭
	$(document).on(
			"click",
			"#btn_go_modify",
			function() {
				$("#noticeForm").attr("action",
						"<c:url value='/notice/editNotice' />");
				$("#noticeForm").submit();
			});




	// 카테고리 삭제 팝업 닫기
	function fn_closeDelPop() {
		$("#delPopForm [name='tplCtgNum']").val("");
		$("#delPopForm [name='tplCtgNm']").val("");
		$("#delPopForm #tplCount").text("");
		$(".dev_category_del_pop").hide();
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
			</div>
			<form id="noticeForm" name="noticeForm" method="post" action="">
				<input type="hidden" name="nowPage" value="${nowPage }" /> <input
					type="hidden" name="totPage" value="${totPage }" /> <input
					type="hidden" name="searchVal" value="${searchVal}" /> <input
					type="hidden" name="noticeDivCd" value="${noticeDivCd}" /> <input
					type="hidden" name="noticeDivCdPageVal" value="${noticeDivCd}" />


				<div class="inner">

						<table class="tCont setting">
							<caption>공지사항 읽기 화면 입니다.</caption>
							<colgroup>
								<col style="width: 10%">
								<col style="width:">
							</colgroup>
							<tbody>


								<tr>
									<th>공지</th>
									<td class="textL">${noticeDetail.noticeDivNm }</td>
								</tr>
								<tr>
									<th>제목</th>
									<td class="textL">${noticeDetail.title }<input type="hidden"
										name="title" value="${noticeDetail.title}" />
										<input type="hidden"
										name="notice_num" value="${noticeDetail.noticeNum }" />
									</td>
								</tr>
								<tr>
									<th>내용</th>
									<td class="textL">${noticeDetail.cont }</td>
								</tr>


							</tbody>
						</table>
						<div class="btn_center_area">
							<c:if
								test="${sessionMemberDivCd eq 'A' or sessionMemberDivCd == 'S' or sessionMemberUid eq noticeDetail.memberUid}">
								<button type="button" class="btn_go_save" id="btn_go_modify">수정</button>
								<button type="button" class="btn_go_cancel">삭제</button>
							</c:if>
							<button type="button" class="btn_go_save" id="btn_go_back">목록</button>
						</div>

				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>

	<form id="delPopForm" name="delPopForm" method="post" action="">
		<div class="popup popup_temp_del dev_category_del_pop">
			<div class="inner">
				<div class="popup_head">
					<h1 class="popup_tit">공지사항 삭제</h1>
					<button type="button" class="btn_popup_close">창닫기</button>
				</div>
				<div class="popup_body">
					<div class="popup_form_area">
						<span class="tit">공지사항 제목</span> <input type="text"
							name="tplCtgNm" maxlength="50" readonly="readonly"> <input
							type="hidden" name="tplCtgNum">
					</div>
					<p class="popup_text_bottom del">
						* <b>등록된 공지사항이 삭제됩니다..</b><br> <span class="marginL10">삭제처리
							하시겠습니까?</span>
					</p>

					<div class="btn_popup_area">
						<button type="button" class="btn_popup_ok dev_btn_del_pop">삭제</button>
						<button type="button" class="btn_popup_ok btn_close_pop">취소</button>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
