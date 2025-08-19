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

		var ctgMgtDpt = "3"; //"${defaultSet.ctg_mgt_dpt }";
		var selectedChannel = "";
		
		$(document).ready(function() {
			if(ctgMgtDpt == "2"){
				$(".dev-depth-3").hide();
				$(".dev-depth-var-3").hide();
			}

		});


		// on, off 버튼 클릭
		$(document).on("change", "#categoryForm .dev-switch-input", function() {
			var cnsPossibleYn;
			if($(this).prop("checked")){
				cnsPossibleYn = $(this).attr("data-on-value");
			}else{
				cnsPossibleYn = $(this).attr("data-off-value");
			}

			$(this).closest("label").find(".dev-switch-input-value").val(cnsPossibleYn);
		});

		// 소분류 사용여부 설정
		$(document).on("click", "#categoryForm [name='updateBtn']", function() {
			if(!confirm("소분류 사용 여부를 변경 하시면\n현재 분류에 매핑된  정보는 초기화 됩니다.\n계속 진행하시겠습니까?")){
				return false;
			}

			var selBtn = $(this).attr("data-btn");

			$("#categoryForm [name='selBtn']").val(selBtn);

			$.ajax({
				url : "<c:url value='/set/updateSet' />",
				data : $("#categoryForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
					$("#categoryForm [name='selBtn']").val("");
				}
			});
		});

		// 대분류 클릭
		$(document).on("click", "#categoryForm .step_list.step01 li", function() {
			$(this).toggleClass("active");
			$(".step_list.step01 li").not(this).removeClass("active");
			var ctgNum = $(this).attr("data-ctg-num");
			/* var departCd = $(this).attr("data-departCd"); */
			var useYn = $(this).attr("data-useYn");

			//채널정보 세팅
			var cstmLinkDivCd = $(this).attr("data-cstmLinkDivCd");
			$("#categoryForm [name='cstmLinkDivCd']").val(cstmLinkDivCd);
			
			$("#categoryForm [name='ctgNum1']").val(ctgNum);
			$("#categoryForm [name='ctgNm1']").val($(this).find("p").text());
			if(useYn == 'Y') {
				$("#useYn").prop("checked",true);
			} else {
				$("#useYn").prop("checked",false);
			}
			/* if(departCd == 'F1' ){
				$("#departCd1").prop("checked", true);
				$("#categoryForm [name='departCd']").val("F1");
			} else if (departCd == 'F2' ){
				$("#departCd2").prop("checked", true);
				$("#categoryForm [name='departCd']").val("F2");
			} else if (departCd == 'DC' ){
				$("#departCd3").prop("checked", true);
				$("#categoryForm [name='departCd']").val("DC");
			} else if (departCd == 'FM' ){
				$("#departCd4").prop("checked", true);
				$("#categoryForm [name='departCd']").val("FM");
			} */
			
/* 			$("#departCd1").prop("disabled", "true");
			$("#departCd2").prop("disabled", "true");
			$("#departCd3").prop("disabled", "true");
			$("#departCd4").prop("disabled", "true"); */
			selectCategory(ctgNum, 2);
			clearCategory(1);
		});

		// 중분류 클릭
		$(document).on("click", "#categoryForm .step_list.step02 li", function() {
			$(this).toggleClass("active");
			$(".step_list.step02 li").not(this).removeClass("active");
			$("#categoryForm [name='ctgNum2']").val($(this).attr("data-ctg-num"));

			var ctgNum = $(this).attr("data-ctg-num");
			$("#categoryForm [name='ctgNum2']").val(ctgNum);
			$("#categoryForm [name='ctgNm2']").val($(this).find("p").text());
			$("#categoryForm [name='useYn']").val("Y");

			// 소분류를 관리할 경우만 조회
			if(ctgMgtDpt == "3"){
				selectCategory(ctgNum, 3);
			}
			clearCategory(2);
		});

		// 소분류 클릭
		$(document).on("click", "#categoryForm .step_list.step03 li", function() {
			$(this).toggleClass("active");
			$(".step_list.step03 li").not(this).removeClass("active");
			$("#categoryForm [name='ctgNum3']").val($(this).attr("data-ctg-num"));

			var ctgNum = $(this).attr("data-ctg-num");
			$("#categoryForm [name='ctgNum3']").val(ctgNum);
			$("#categoryForm [name='ctgNm3']").val($(this).find("p").text());
			$("#categoryForm [name='useYn']").val("Y");
			clearCategory(3);
		});

		// 분류 목록 조회
		function selectCategory(ctgNum, step){

			var cstmLinkDivCd = "";
			
			//채널 정보 파라미터 추가
			if (step == '2' || step == '3') {
				cstmLinkDivCd = $("#categoryForm [name='cstmLinkDivCd']").val();
			}
			
			$.ajax({
				url : "<c:url value='/category/selectCategoryAjax' />",
				data : {"ctgNum" : ctgNum,
						"cstmLinkDivCd" : cstmLinkDivCd},
				type : "post",
				success : function(result) {
					if(step == 1){
						$(".step_list.step01").html("");
						$(".step_list.step02").html("");
						$(".step_list.step03").html("");
						$.each(result, function(idx, item){
							var li = '<li data-useYn="'+item.use_yn+'" data-ctg-num="'+item.ctg_num+'" data-departCd="'+item.depart_cd+'" data-cstmLinkDivCd="'+item.cstm_link_div_cd+'"><span class="no">'+item.cstm_link_div_nm+'</span><p class="text">'+item.ctg_nm+'</p>';
							if (item.use_yn == 'N') {
								li += '<span style="font-size:10px;color:#aaa;float:left;margin-left:-50px;">(미사용)</span>';
							}
								li += '</li>'; 
							$(".step_list.step01").append(li);
						});
					}else if(step == 2){
						$(".step_list.step02").html("");
						$(".step_list.step03").html("");

						$.each(result, function(idx, item){
							var ctg_num_reface;
							if(item.ctg_num == '9999999992') ctg_num_reface = item.cstm_link_div_nm;
							else if(item.ctg_num == '9999999993') ctg_num_reface = item.cstm_link_div_nm;
							else if(item.ctg_num == '9999999994') ctg_num_reface = item.cstm_link_div_nm;
							else if(item.ctg_num == '9999999995') ctg_num_reface = item.cstm_link_div_nm;
							else ctg_num_reface = item.ctg_num;
							
							var li = '<li data-ctg-num="'+item.ctg_num+'"><span class="no">'+ctg_num_reface+'</span><p class="text">'+item.ctg_nm+'</p></li>';
							$(".step_list.step02").append(li);
						});
					}else if(step == 3){
						$(".step_list.step03").html("");

						$.each(result, function(idx, item){
							var li = '<li data-ctg-num="'+item.ctg_num+'"><span class="no">'+item.ctg_num+'</span><p class="text">'+item.ctg_nm+'</p></li>';
							$(".step_list.step03").append(li);
						});
					}
				},
				complete : function() {
				}
			});
		}

		// 카테고리 추가 버튼 클릭
		$(document).on("click", "#categoryForm .dev-insert-btn", function() {
			var ctgNum1 = $("#categoryForm [name='ctgNum1']").val();
			var ctgNum2 = $("#categoryForm [name='ctgNum2']").val();
			var ctgNum3 = $("#categoryForm [name='ctgNum3']").val();

			var categoryDpt = $(this).closest(".dev-ctg-step").attr("data-dev-depth");
			if(categoryDpt == "2"){
				if(ctgNum1 == ""){
					alert("대분류를 먼저 선택해 주세요.");
					return false;
				}
			}
			if(categoryDpt == "3"){
				if(ctgNum2 == ""){
					alert("중분류를 먼저 선택해 주세요.");
					return false;
				}
			}

			if(categoryDpt == "1"){
				var ctgNm = $("#categoryForm [name='ctgNm1']").val().trim();
				$("#categoryForm [name='ctgNm1']").val(ctgNm);

				if(ctgNm == ""){
					alert("분류명을 입력해 주세요.");
					$("#categoryForm [name='ctgNm1']").focus();
					return false;
				}

				$("#categoryForm [name='ctgDpt']").val("1");
				$("#categoryForm [name='ctgNm']").val($("#categoryForm [name='ctgNm1']").val());
				$("#categoryForm [name='upperCtgNum']").val("0");

/* 				if($("#departCd1").prop("checked")){
					$("#categoryForm [name='departCd']").val("F1");
				}else if($("#departCd2").prop("checked")){
					$("#categoryForm [name='departCd']").val("F2");
				}else if($("#departCd3").prop("checked")){
					$("#categoryForm [name='departCd']").val("DC");
				}else if($("#departCd4").prop("checked")){
					$("#categoryForm [name='departCd']").val("FM");
				} */
			}else if(categoryDpt == "2"){
				var ctgNm = $("#categoryForm [name='ctgNm2']").val().trim();
				$("#categoryForm [name='ctgNm2']").val(ctgNm);

				if(ctgNm == ""){
					alert("분류명을 입력해 주세요.");
					$("#categoryForm [name='ctgNm2']").focus();
					return false;
				}

				$("#categoryForm [name='ctgDpt']").val("2");
				$("#categoryForm [name='ctgNm']").val($("#categoryForm [name='ctgNm2']").val());
				$("#categoryForm [name='upperCtgNum']").val(ctgNum1);
				$("#categoryForm [name='useYn']").val("Y");
			}else if(categoryDpt == "3"){
				var ctgNm = $("#categoryForm [name='ctgNm3']").val().trim();
				$("#categoryForm [name='ctgNm3']").val(ctgNm);

				if(ctgNm == ""){
					alert("분류명을 입력해 주세요.");
					$("#categoryForm [name='ctgNm3']").focus();
					return false;
				}

				$("#categoryForm [name='ctgDpt']").val("3");
				$("#categoryForm [name='ctgNm']").val($("#categoryForm [name='ctgNm3']").val());
				$("#categoryForm [name='upperCtgNum']").val(ctgNum2);
				$("#categoryForm [name='useYn']").val("Y");
			}else{
				fn_layerMessage("정상적인 호출이 아닙니다.");
				return false;
			}

			$.ajax({
				url : "<c:url value='/category/insertCategory' />",
				data : $("#categoryForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						if(categoryDpt == "1"){
							$("#categoryForm [name='ctgNm1']").val("");
							selectCategory("0", 1); 
							clearCategory(1);
						}else if(categoryDpt == "2"){
							$("#categoryForm [name='ctgNm2']").val("");
							selectCategory(ctgNum1, 2);
							clearCategory(2);
						}else if(categoryDpt == "3"){
							$("#categoryForm [name='ctgNm3']").val("");
							selectCategory(ctgNum2, 3);
							clearCategory(3);
						}
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 카테고리 수정 버튼 클릭
		$(document).on("click", "#categoryForm .dev-update-btn", function() {
			var ctgNum1 = $("#categoryForm [name='ctgNum1']").val();
			var ctgNum2 = $("#categoryForm [name='ctgNum2']").val();
			var ctgNum3 = $("#categoryForm [name='ctgNum3']").val();
			var ctgNm1 = $("#categoryForm [name='ctgNm1']").val();
			var ctgNm2 = $("#categoryForm [name='ctgNm2']").val();
			var ctgNm3 = $("#categoryForm [name='ctgNm3']").val();

			var categoryDpt = $(this).closest(".dev-ctg-step").attr("data-dev-depth");
			var cstmLinkDivCd = $("#categoryForm [name='cstmLinkDivCd']").val();
			
			if(categoryDpt == "1"){
				if(ctgNum1 == ""){
					alert("수정할 분류를 선택해 주세요.");
					return false;
				}

				ctgNm1 = ctgNm1.trim();
				if(ctgNm1 == ""){
					alert("분류명을 입력해 주세요.");
					$("#categoryForm [name='ctgNm1']").focus();
					return false;
				}

				$("#categoryForm [name='ctgNm']").val(ctgNm1);
				$("#categoryForm [name='ctgNum']").val(ctgNum1);

/* 				if($("#departCd1").prop("checked")){
					$("#categoryForm [name='departCd']").val("F1");
				}else if($("#departCd2").prop("checked")){
					$("#categoryForm [name='departCd']").val("F2");
				}else if($("#departCd3").prop("checked")){
					$("#categoryForm [name='departCd']").val("DC");
				}else if($("#departCd4").prop("checked")){
					$("#categoryForm [name='departCd']").val("FM");
				} */
			}else if(categoryDpt == "2"){
				if(ctgNum2 == ""){
					alert("수정할 분류를 선택해 주세요.");
					return false;
				}

				ctgNm2 = ctgNm2.trim();
				if(ctgNm2 == ""){
					alert("분류명을 입력해 주세요.");
					$("#categoryForm [name='ctgNm2']").focus();
					return false;
				}

				$("#categoryForm [name='ctgNm']").val(ctgNm2);
				$("#categoryForm [name='ctgNum']").val(ctgNum2);
			}else if(categoryDpt == "3"){
				if(ctgNum3 == ""){
					alert("수정할 분류를 선택해 주세요.");
					return false;
				}

				ctgNm3 = ctgNm3.trim();
				if(ctgNm3 == ""){
					alert("분류명을 입력해 주세요.");
					$("#categoryForm [name='ctgNm3']").focus();
					return false;
				}

				$("#categoryForm [name='ctgNm']").val(ctgNm3);
				$("#categoryForm [name='ctgNum']").val(ctgNum3);
			} else {
				alert("정상적인 호출이 아닙니다.");
				return false;
			}

			$.ajax({
				url : "<c:url value='/category/updateCategory' />",
				data : $("#categoryForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						if(categoryDpt == "1"){
							$("#categoryForm [name='ctgNm1']").val("");
							$("#categoryForm [name='ctgNum1']").val("");
							clearCategory(1);
							selectCategory("0", 1);
						}else if(categoryDpt == "2"){
							$("#categoryForm [name='ctgNm2']").val("");
							$("#categoryForm [name='ctgNum2']").val("");
							selectCategory(ctgNum1, 2);
							clearCategory(2);
						}else if(categoryDpt == "3"){
							$("#categoryForm [name='ctgNm3']").val("");
							$("#categoryForm [name='ctgNum3']").val("");
							selectCategory(ctgNum2, 3);
							clearCategory(3);
						}
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 카테고리 삭제 버튼 클릭
		$(document).on("click", "#categoryForm .dev-delete-btn", function() {
			var ctgNum1 = $("#categoryForm [name='ctgNum1']").val();
			var ctgNum2 = $("#categoryForm [name='ctgNum2']").val();
			var ctgNum3 = $("#categoryForm [name='ctgNum3']").val();

			var categoryDpt = $(this).closest(".dev-ctg-step").attr("data-dev-depth");

			if(categoryDpt == "1"){
				if(ctgNum1 == ""){
					alert("삭제할 분류를 선택해 주세요.");
					return false;
				}
				$("#categoryForm [name='ctgNum']").val(ctgNum1);
			}else if(categoryDpt == "2"){
				if(ctgNum2 == ""){
					alert("삭제할 분류를 선택해 주세요.");
					return false;
				}
				$("#categoryForm [name='ctgNum']").val(ctgNum2);
			}else if(categoryDpt == "3"){
				if(ctgNum3 == ""){
					alert("삭제할 분류를 선택해 주세요.");
					return false;
				}
				$("#categoryForm [name='ctgNum']").val(ctgNum3);
			}else{
				alert("정상적인 호출이 아닙니다.");
				return false;
			}

			$.ajax({
				url : "<c:url value='/category/deleteCategory' />",
				data : $("#categoryForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						if(categoryDpt == "1"){
							$("#categoryForm [name='ctgNm1']").val("");
							$("#categoryForm [name='ctgNum1']").val("");
							selectCategory("0", 1);
							clearCategory(1);
						}else if(categoryDpt == "2"){
							$("#categoryForm [name='ctgNm2']").val("");
							$("#categoryForm [name='ctgNum2']").val("");
							selectCategory(ctgNum1, 2);
							clearCategory(2);
						}else if(categoryDpt == "3"){
							$("#categoryForm [name='ctgNm3']").val("");
							$("#categoryForm [name='ctgNum3']").val("");
							selectCategory(ctgNum2, 3);
							clearCategory(3);
						}
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		function clearCategory(step){
			if(step == 1){
				$("#categoryForm [name='ctgNm2']").val("");
				$("#categoryForm [name='ctgNm3']").val("");

				$("#categoryForm [name='ctgNum2']").val("");
				$("#categoryForm [name='ctgNum3']").val("");

				$("#categoryForm [name='ctgNm']").val("");
				$("#categoryForm [name='ctgNum']").val("");
				$("#categoryForm [name='ctgDpt']").val("");
				$("#categoryForm [name='upperCtgNum']").val("");

				$("#categoryForm [name='cstmYnCheck']").prop("checked", false);
				$("#categoryForm [name='cstmYn']").val("N");
			}else if(step == 2){
				$("#categoryForm [name='ctgNm3']").val("");

				$("#categoryForm [name='ctgNum3']").val("");

				$("#categoryForm [name='ctgNm']").val("");
				$("#categoryForm [name='ctgNum']").val("");
				$("#categoryForm [name='ctgDpt']").val("");
				$("#categoryForm [name='upperCtgNum']").val("");
			}else if(step == 3){
				$("#categoryForm [name='ctgNm']").val("");
				$("#categoryForm [name='ctgNum']").val("");
				$("#categoryForm [name='ctgDpt']").val("");
				$("#categoryForm [name='upperCtgNum']").val("");
			}
		}

		function goSearch(){
			$("#categoryForm").attr("action", "<c:url value='/category/selectCategory' />");
			$("#categoryForm").submit();
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
			<form id="categoryForm" name="categoryForm" method="post" action="">
				<input type="hidden" name="siteId" value="${defaultSet.site_id }" />
				<input type="hidden" name="selBtn" value="" />
				<input type="hidden" name="ctgNum" value="" />
				<input type="hidden" name="upperCtgNum" value="" />
				<input type="hidden" name="ctgNm" value="" />
				<input type="hidden" name="ctgDpt" value="" />
				<input type="hidden" name="ctgNum1" value="" />
				<input type="hidden" name="ctgNum2" value="" />
				<input type="hidden" name="ctgNum3" value="" />
				<input type="hidden" name="cstmYn" value="" />
				<input type="hidden" name="departCd" value="" />
				<div class="left_content_head">
					<h2 class="sub_tit">상담 분류 관리</h2>
					<!-- <button type="button" class="btn_go_help">도움말</button>  -->
					<span class="left_top_text">상담을 분류합니다. 고객이 상담 전에 분류를 선택하고 과 채팅하게 됩니다.</span>
				</div>
				<div class="inner tm_disabled">
				<!-- 소분류 사용여부 없앰  
					<div class="box_head_area">
						<span style="float:right;margin-left:20px;"><button type="button" class="btn_go_insert" data-btn="ctgMgtDpt" name="updateBtn"><i></i>적용</button></span>
						<label class="switch w120">
							<input class="switch-input dev-switch-input" type="checkbox" data-on-value="3" data-off-value="2" <c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">checked</c:if> >
							<span class="switch-label" data-on="소분류 사용" data-off="소분류 미사용"></span>
							<span class="switch-handle"></span>
							<input type="hidden" name="ctgMgtDpt" class="dev-switch-input-value" value="${defaultSet.ctg_mgt_dpt }">
						</label>
					</div>
				 -->
				 	<input type="hidden" name="ctgMgtDpt" class="dev-switch-input-value" value="3">
					<!-- 대분류 -->
					<div class="step_area dev-ctg-step" data-dev-depth="1">
						<h3 class="sub_stit">대분류</h3>
						<div class="box_scroll">
							<ul class="step_list step01"><!-- 단계별 class명 추가 -->
								<c:forEach var="data" items="${categoryList }" varStatus="status">
									<li data-ctg-num="${data.ctg_num }" data-departCd="${data.depart_cd }" data-useYn="${data.use_yn}" data-cstmLinkDivCd="${data.cstm_link_div_cd}" >
										<span class="no">${data.cstm_link_div_nm }</span>
										<p class="text">${data.ctg_nm }</p> 
										<c:if test="${data.use_yn eq 'N' }"><span style="font-size:10px;color:#aaa;float:left;margin-left:-50px;">(미사용)</span></c:if>
									</li>
								</c:forEach>
							</ul>
						</div>
						<div class="box_scroll_bottom">
							<%-- <h4 class="sub_cont_tit">
								대분류 관리
								<span style="float:right;">
									<input type="radio" class="radio_18 no_text" id="departCd1" name="departCdCheck" value="F1" <c:if test="${sessionVo.departCd eq 'F1' }">checked </c:if> />
									<label for="departCd1"><span></span>FC1</label>
									<input type="radio" class="radio_18 no_text" id="departCd2" name="departCdCheck" value="F2" <c:if test="${sessionVo.departCd eq 'F2' }">checked </c:if> />
									<label for="departCd2"><span></span>FC2</label>
									<input type="radio" class="radio_18 no_text" id="departCd3" name="departCdCheck" value="DC" <c:if test="${sessionVo.departCd eq 'DC' }">checked </c:if> />
									<label for="departCd3"><span></span>DC</label>
									<input type="radio" class="radio_18 no_text" id="departCd4" name="departCdCheck" value="FM" <c:if test="${sessionVo.departCd eq 'FM' }">checked </c:if> />
									<label for="departCd4"><span></span>FM</label>
								</span>
							</h4> --%>
							<input type="text" name="ctgNm1" class="form_step_plus" placeholder="분류명을 입력하세요." maxlength="14">
							<label class="switch w90" style="float:right;'">
								<input class="switch-input dev-switch-input" type="checkbox" id="useYn" data-on-value="Y" data-off-value="N" >
								<span class="switch-label" data-on="사용" data-off="사용안함"></span>
								<span class="switch-handle"></span>
								<input type="hidden" name="useYn" class="dev-switch-input-value" value="N">
							</label>
							<select name="cstmLinkDivCd">
								<c:forEach var="data" items="${selectChannelList }" varStatus="status">
									<option value="${data.cd }" >${data.cd_nm }</option>
								</c:forEach>
							</select>
							<div class="btn_left_area">
								<button type="button" class="btn_step_plus dev-insert-btn"><i></i>추가</button>
								<button type="button" class="btn_step_modify dev-update-btn"><i></i>수정</button>
								<button type="button" class="btn_step_del dev-delete-btn"><i></i>삭제</button>
							</div>
							<p class="text_info_star">추가 - 최대 14자리까지 입력 가능하며 최초 등록 시 "사용안함"으로 설정됩니다.</p>
							<p class="text_info_star">수정 - 원하는 대분류를 클릭한 후 분류명, 사용옵션 수정이 가능합니다. (인입 채널 정보는 수정 불가)</p>
							<p class="text_info_star">삭제 - 원하는 대분류를 클릭한 후 삭제 가능하며 하위 분류까지 모두 삭제됩니다.</p>
						</div>
					</div>
					<!--// 대분류 -->

					<button type="button" class="btn_move"><i>선택</i></button>

					<!-- 중분류 -->
					<div class="step_area dev-ctg-step" data-dev-depth="2">
						<h3 class="sub_stit">중분류</h3>
						<div class="box_scroll">
							<ul class="step_list step02"><!-- 단계별 class명 추가 -->
							</ul>
						</div>
						<div class="box_scroll_bottom">
							<h4 class="sub_cont_tit">중분류 관리</h4>
							<!-- <span class="tit_step_plus dev-step1-text"></span> -->
							<input type="text" name="ctgNm2" class="form_step_plus" placeholder="분류명을 입력하세요." maxlength="14">
							<div class="btn_left_area">
								<button type="button" class="btn_step_plus dev-insert-btn"><i></i>추가</button>
								<button type="button" class="btn_step_modify dev-update-btn"><i></i>수정</button>
								<button type="button" class="btn_step_del dev-delete-btn"><i></i>삭제</button>
							</div>
							<p class="text_info_star">대분류 선택 후 중분류 추가/수정/삭제가 가능합니다.</p>
							<p class="text_info_star">추가/수정 - 최대 14자리까지 입력 가능합니다.</p>
							<p class="text_info_star">삭제 - 원하는 중분류를 클릭한 후 삭제 가능하며 하위 분류까지 모두 삭제됩니다.</p>
						</div>
					</div>
					<!--// 중분류 -->

					<button type="button" class="btn_move dev-depth-var-3"><i>선택</i></button>

					<!-- 소분류 -->
					<div class="step_area dev-ctg-step dev-depth-3" data-dev-depth="3">
						<h3 class="sub_stit">소분류</h3>
						<div class="box_scroll">
							<ul class="step_list step03"><!-- 단계별 class명 추가 -->
							</ul>
						</div>
						<div class="box_scroll_bottom">
							<h4 class="sub_cont_tit">소분류 관리</h4>
							<!-- <span class="tit_step_plus dev-step2-text"></span> -->
							<input type="text" name="ctgNm3" class="form_step_plus" placeholder="분류명을 입력하세요." maxlength="14">
							<div class="btn_left_area">
								<button type="button" class="btn_step_plus dev-insert-btn"><i></i>추가</button>
								<button type="button" class="btn_step_modify dev-update-btn"><i></i>수정</button>
								<button type="button" class="btn_step_del dev-delete-btn"><i></i>삭제</button>
							</div>
							<p class="text_info_star">중분류 선택 후 소분류 추가/수정/삭제가 가능합니다.</p>
							<p class="text_info_star">추가/수정 - 최대 14자리까지 입력 가능합니다.</p>
							<p class="text_info_star">삭제 - 원하는 소분류를 클릭한 후 바로 삭제가 가능합니다.</p>
						</div>
					</div>
					<!--// 소분류 -->

					<div class="step_bottom">
						<p class="text_info_star">인입 채널 구분 및 전문  배정을 위해 대/중분류가 사용되며, "사용" 설정된 대분류는 고객에게 노출되오니 주의바랍니다.</p>
						<!--<p class="text_info_star">소분류는 상담 후처리 및 통계를 위한 용도로 소분류가 없는 중분류는 후처리 저장이 불가하오니 꼭 소분류를 1개 이상 생성해 주시기 바랍니다.</p>-->
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
