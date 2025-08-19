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

		// 단어추가 버튼
		$(document).on("click", "#forbiddenForm .dev_insert_btn", function() {
			$.ajax({
				url : "<c:url value='/set/insertForbidden' />",
				data : $("#forbiddenForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 단어에서 엔터 처리
		$(document).on("keypress", "#forbiddenForm [name='forbidden']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#forbiddenForm .dev_insert_btn").trigger("click");
			}
		});

		// 단어삭제 버튼
		$(document).on("click", "#forbiddenForm .dev_delete_btn", function() {
			var forbiddenNum = $(this).attr("data-forbidden_num");
			var forbidden = $(this).attr("data-forbidden");
			$.ajax({
				url : "<c:url value='/set/deleteForbidden' />",
				data : {"forbiddenNum" : forbiddenNum, "forbidden" : forbidden},
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		function goSearch(){
			$("#forbiddenForm").attr("action", "<c:url value='/set/selectForbidden' />");
			$("#forbiddenForm").submit();
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
			<form id="forbiddenForm" name="forbiddenForm" method="post" action="<c:url value='/set/updateSet' />" class=" tm_disabled">
				<div class="left_content_head">
					<h2 class="sub_tit">금지어 관리</h2>
				</div>
				<div class="inner">
					<h3 class="sub_stit">단어 추가</h3>
					<div class="bad_plus_area">
						<input type="text" placeholder="0/20자" name="forbidden" maxlength="20"><button type="button" class="btn_word_plus dev_insert_btn">단어추가</button>
					</div>
					<div class="bad_text_area">
						<p class="text_info_star">추가된 단어는 상담직원에게 별포 " * "로 대체되어 보여집니다.</p>
						<p class="text_info_star">해당 기능은 웹, O2, mPOP에만 적용됩니다. (카카오 상담톡 외부 채널에는 적용되지 않습니다.)</p>
					</div>
				</div>
				<div class="inner">
					<h3 class="sub_stit">등록된 단어 리스트</h3>
					<div class="bad_word_area">
						<c:forEach var="data" items="${forbiddenList }" varStatus="status">
							<span class="bad_word"><c:out value="${data.forbidden }"/><button type="button" class="btn_del dev_delete_btn" data-forbidden="${data.forbidden }" data-forbidden_num="${data.forbidden_num }">삭제</button></span>
						</c:forEach>
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
