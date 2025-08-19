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
		var ctgMgtDpt = "${defaultSet.ctg_mgt_dpt}";

		$(document).ready(function() {

		});


		$(document).on("click", "#categoryMatchForm .dev_select_btn", function() {
			var ctgNum = $(this).attr("data-ctg_num");
			$("#categoryMatchForm [name='ctgNum']").val(ctgNum);

			$(".dev_category_table tbody tr td").removeClass("selected");
			$(this).closest("td").addClass("selected");

			var linkDivNm = $(this).closest("td").attr("data-linkDivNm");
			var ctgNm1 = $(this).closest("td").attr("data-ctgNm1");
			var ctgNm2 = $(this).closest("td").attr("data-ctgNm2");
			var ctgNm3 = $(this).closest("td").attr("data-ctgNm3");
			var ctgMenuHtml = "";
			ctgMenuHtml += linkDivNm + ' > ' + ctgNm1

			if(ctgMgtDpt == "3"){
				ctgMenuHtml += ' > ';
				ctgMenuHtml += ctgNm2;
				ctgMenuHtml += ' > ';
				ctgMenuHtml += '<em class="selected">'+ctgNm3+'</em>';
			}else if(ctgMgtDpt == "2"){
				ctgMenuHtml += ' > ';
				ctgMenuHtml += '<em class="selected">'+ctgNm2+'</em>';
			}

			$(".dev_select_category").html(ctgMenuHtml);

			$.ajax({
				url : "<c:url value='/category/selectMatchCnsrList' />",
				data : {"ctgNum" : ctgNum},
				type : "post",
				success : function(result) {
					$("#devCounselorDiv").html(result);
				},
				complete : function() {
				}
			});
		});

		$(document).on("click", "#categoryMatchForm .dev_match_btn", function() {
			var ctgNum = $("#categoryMatchForm [name='ctgNum']").val();
			if(ctgNum == ""){
				alert("분류코드가 없습니다. 분류를 먼저 선택하여 주시기 바랍니다.");
				return false;
			}

			$.ajax({
				url : "<c:url value='/category/insertMatchCnsr' />",
				data : $("#categoryMatchForm").serialize(),
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
				}
			});
		});

		$(document).on("click", "#categoryMatchForm .dev_click_counselor", function() {
			$(this).closest("tr").find("[name='memberUidArr']").trigger("click");
		});

		function goSearch(){
			$("#categoryMatchForm").attr("action", "<c:url value='/category/selectCategoryMatch' />");
			$("#categoryMatchForm").submit();
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
			<form id="categoryMatchForm" name="categoryMatchForm" method="post" action="">
				<input type="hidden" name="ctgNum" />
				<div class="left_content_head">
					<h2 class="sub_tit">상담 배분 관리</h2>
					<span class="left_top_text">고객이 상담 분류를 선택했을 때 배정될 상담직원을 설정합니다.</span>
				</div>
				<div class="inner">
					<h3 class="sub_stit">
						다이렉트 상담직원 지정
						<!--
						<label class="switch w90 floatR">
							<input class="switch-input" type="checkbox">
							<span class="switch-label" data-on="사용" data-off="미사용"></span>
							<span class="switch-handle"></span>
						</label>
						 -->
					</h3>
					<div class="text_top_area">
						<button type="button" class="btn_holiday_plus" id="resetAdminPasswd" style="float:right;" data-id="${member.departCd == 'CS' ? 'csadmin' : 'tmadmin'}">어드민 비번 리셋</button>
						<%-- IPCC_MCH 고객 요청 :매니저 비번 초기화 추가 --%>
						<button type="button" class="btn_holiday_plus" id="resetManagerPasswd" style="float:right;" data-id="ssmanager">매니저 비번 리셋</button>
						<p>중분류를 선택하고 지정할 상담직원을 체크한 후 [상담직원 배정 등록] 버튼을 클릭하면 즉시 적용됩니다.</p>
						<p>'대상지정현황'에서 상담직원명을 클릭하면 분류/우선배정 등 상담직원 관리가 가능하며, 담당 부서의 슈퍼관리자 비밀번호 분실 시 [어드민 비번 리셋] 버튼 통해 초기화가 가능합니다.</p>
						<p style="color:#dc1000;">※ [서비스설정 - 상담 분류 설정] 메뉴에서 "사용" 설정된 대분류의 하위 중분류는 고객이 선택할 수 있으므로 상담직원 배분 관리 시 유의해주세요.</p>
						<p style="color:#dc1000;">※ 중분류 중 '기본분류'는 고객에게 보여지지 않으나 타 부서 이관 시 배정되는 기준이므로 1명 이상의 상담직원을 꼭! 배정해주세요.</p>
					</div>

					<div class="select_member">
						<h4 class="sub_cont_tit">
							대상지정 현황
						</h4>
						<table class="tCont dev_category_table">
							<caption>대상지정 현황 목록입니다.</caption>
							<colgroup>
								<col style="width:10%">
								<col style="width:16.55%">
								<col style="width:16.55%">
								<c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">
									<col style="width:16.55%">
								</c:if>
								<col style="width:13.22%">
								<col style="width:13.22%">
								<col style="width:13.22%">
							</colgroup>
							<thead>
								<tr>
									<th>채널명</th>
									<th>대분류</th>
									<th>중분류</th>
									<c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">
										<th>소분류</th>
									</c:if>
									<th>부서명</th>
									<th>매니저</th>
									<th>상담직원</th>
								</tr>
							</thead>
							<tbody>
								<c:set var="ctgNum1" value="" />
								<c:set var="ctgNum2" value="" />
								<c:set var="ctgNum3" value="" />
								<c:set var="ctgNm1" value="" />
								<c:set var="ctgNm2" value="" />
								<c:set var="ctgNm3" value="" />
								<c:set var="channelCd" value= "" />
								<c:forEach var="data" items="${categoryList }" varStatus="status">
									<%-- <c:if test="${data.depart_cd eq sessionVo.departCd }"> --%>
									<tr>
										<c:if test="${channelCd ne data.cstm_link_div_cd }">
											<c:set var="channelCd" value= "${data.cstm_link_div_cd }" />
											<td rowspan="${data.cstm_link_div_cd_count }">${data.cstm_link_div_nm }</td>
										</c:if>
										<c:if test="${ctgNum1 eq '' || ctgNum1 ne data.ctg_num1 }">
											<c:set var="ctgNum1" value="${data.ctg_num1 }" />
											<c:set var="ctgNm1" value="${data.ctg_nm1 }" />
											<c:choose>
												<c:when test="${data.ctg_dpt1_count eq '0' }">
													<td>${data.ctg_nm1 }</td>
												</c:when>
												<c:otherwise>
													<td rowspan="${data.ctg_dpt1_count }">${data.ctg_nm1 }</td>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${defaultSet.ctg_mgt_dpt eq '2' }">
											<c:if test="${ctgNum2 eq '' || empty data.ctg_num2 || ctgNum2 ne data.ctg_num2 }">
												<c:set var="ctgNum2" value="${data.ctg_num2 }" />
												<c:set var="ctgNm2" value="${data.ctg_nm2 }" />
												<c:set var="linkDivNm" value="${data.cstm_link_div_nm }" />
												<c:choose>
													<c:when test="${data.ctg_dpt2_count eq '0' }">
														<td data-linkDivNm="${linkDivNm }" data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }">
													</c:when>
													<c:otherwise>
														<td rowspan="${data.ctg_dpt2_count }" data-linkDivNm="${linkDivNm }" data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }">
													</c:otherwise>
												</c:choose>
													<a href="javascript:void(0);" class="link_text dev_select_btn" data-ctg_num="${data.ctg_num2 }">${data.ctg_nm2 }</a>
												</td>
											</c:if>
										</c:if>
											
										<c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">
											<c:if test="${ctgNum2 eq '' || empty data.ctg_num2 || ctgNum2 ne data.ctg_num2 }">
												<c:set var="ctgNum2" value="${data.ctg_num2 }" />
												<c:set var="ctgNm2" value="${data.ctg_nm2 }" />
												<c:choose>
													<c:when test="${data.ctg_dpt2_count eq '0' }">
														<td>${data.ctg_nm2 }</td>
													</c:when>
													<c:otherwise>
														<td rowspan="${data.ctg_dpt2_count }">${data.ctg_nm2 }</td>
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:if test="${ctgNum3 eq '' || empty data.ctg_num3 || ctgNum3 ne data.ctg_num3 }">
												<c:set var="ctgNum3" value="${data.ctg_num3 }" />
												<c:set var="ctgNm3" value="${data.ctg_nm3 }" />
												<c:choose>
													<c:when test="${data.ctg_dpt3_count eq '0' }">
														<td data-linkDivNm="${linkDivNm }" data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }" data-ctgNm3="${ctgNm3 }">
													</c:when>
													<c:otherwise>
														<td rowspan="${data.ctg_dpt3_count }" data-linkDivNm="${linkDivNm }" data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }" data-ctgNm3="${ctgNm3 }">
													</c:otherwise>
												</c:choose>
													<a href="javascript:void(0);" class="link_text dev_select_btn" data-ctg_num="${data.ctg_num3 }">${data.ctg_nm3 }</a>
												</td>
											</c:if>
										</c:if>
										<td>${data.depart_nm }</td>
										<td>${data.manager_nm }</td>
										<td><a href="javascript:void(0);" class="member_name" data-id="${data.member_uid}">${data.member_nm }</a></td>
									</tr>
									<%-- </c:if> --%>
								</c:forEach>
							</tbody>
						</table>
					</div>

					<div class="select_btn_area">
						<button type="button" class="btn_select_member dev_match_btn"><i></i>상담직원<br>배정 등록</button>
					</div>

					<div class="select_member">
						<h4 class="sub_cont_tit dev_select_category">
							&nbsp;<!-- 신규대출고객 > <em class="selected">신용대출</em> > <em class="selected">신용대출</em> -->
						</h4>
						<table class="tCont">
							<caption>상담직원 현황 목록입니다.</caption>
							<colgroup>
								<col style="width:12%">
								<col style="width:22%">
								<col style="width:22%">
								<col style="width:22%">
								<col style="width:22%">
							</colgroup>
							<thead>
								<tr>
									<th>선택</th>
									<th>부서명</th>
									<th>매니저</th>
									<th>상담직원</th>
									<th>구분</th>
								</tr>
							</thead>
							<tbody id="devCounselorDiv">
							</tbody>
						</table>
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
<!-- 회원 정보 수정 팝업 -->
<div id="member_popup" class="popup popup_manager" style="display: none;">
</div>
<c:if test="${!isProduction}">
<form id="debug" style="display: none;">
	<input type="hidden" name="profile" value="false" />
</form>
</c:if>
</body>
</html>
