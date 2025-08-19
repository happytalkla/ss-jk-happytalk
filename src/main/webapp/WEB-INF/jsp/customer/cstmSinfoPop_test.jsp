<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>삼성증권</title>
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
	<script type="text/javascript" src="<c:url value='/js/jquery.dotdotdot.js' />"></script>

	<script type="text/javascript">

	$(document).ready(function() {

	});

	function fn_save(){
		if($("name" ).val()==''){
			alert("이름을 입력해 주세요.")
			return false;
		}
		if($("#telNo1" ).val()=='' || $("#telNo2" ).val()=='' || $("#telNo3" ).val()==''){
			alert("연락처를 입력해 주세요.")
			return false;
		}	
		$("#part" ).val('P1');	
		$.ajax({
			url : "<c:url value='/customer/saveCstmSinfo' />",
			data : $("#cstmSinfoForm").serialize(),	
			type : "post",
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					fn_layerMessage(result.rtnMsg);
					//fn_goSearch();	
				}else{
					fn_layerMessage(result.rtnMsg);
				}							
			},
			complete : function() {
			}
		});
	}



	function fn_testSearch(){
	
		$.ajax({
			url : "<c:url value='/customer/testInterface' />",
			data : $("#cstmSinfoForm").serialize(),	
			type : "post",
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					var testData = JSON.stringify(result.resultList);
					$('#etc2').text(testData);
					fn_layerMessage(result.rtnMsg);
				}else{
					fn_layerMessage(result.rtnMsg);
				}							
			},
			complete : function() {
			}
		});
	}	
	
	/**
		$(document).ready(function() {
			fn_clear();
			var schAutoCmpDiv = '${schAutoCmpDiv}';
			if (schAutoCmpDiv =='A') {
				fn_changeSelect1();

			}else if (schAutoCmpDiv=='C') {
				fn_changeSelect2();
			}

			//$(this).closest("tr").find("[name='memberUidArr']").trigger("click");
		});

		function fn_goSearch(){
			$("#autoCmpId").val('');
			$("#autoCmpForm").attr("action", "<c:url value='/autoCmp/selectAutoCmpList' />");
			$("#autoCmpForm").submit();
		}

		function fn_saveView(id,div,content){
			$("#autoCmpId").val(id);
			$("#autoCmpDiv").val(div);
			if (div=='A') {
				$("#autoCmpDiv1").prop("checked", true);	
				$("#autoCmpCus").val("C");
			}else if (div=='C') {
				$("#autoCmpDiv2").prop("checked", true);	
				$("#autoCmpCus").val("");
			}
	
			$("#content").val(content);
			
		}		
		function fn_save(){
			if($("#content" ).val()==''){
				alert("자동메세지를 입력해 주세요")
				return false;
			}
			$.ajax({
				url : "<c:url value='/autoCmp/saveAutoCmp' />",
				data : $("#autoCmpForm").serialize(),	
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						fn_layerMessage(result.rtnMsg);
						fn_goSearch();	
					}else{
						fn_layerMessage(result.rtnMsg);
					}							
				},
				complete : function() {
				}
			});
		}	
		
		function fn_delete(id){
			if(!confirm(name + " 등록된 자동완성 메세지를 삭제하시겠습니까?")){
				return false;
			}

			$("#autoCmpId").val(id);
			$.ajax({
				url : "<c:url value='/autoCmp/deleteAutoCmp' />",
				data : $("#autoCmpForm").serialize(),	
				type : "post",
				success : function(result) {
					fn_layerMessage(result.rtnMsg);
					$("#autoCmpId").val('');
					fn_goSearch();		
				},
				complete : function() {
				}
			});
			
		}	

		function fn_clear() {
			$("#autoCmpId").val('');
			$("#autoCmpDiv1").prop("checked", true);		
			$("#content").val('');
			$("#autoCmpCus").val('');
		}


		function fn_changeSelect1() {
			$("#schAutoCmpCus").val('');
			$("#schAutoCmpCus").html('');
			$("#schAutoCmpCus").append('<option value="">전체</option>');

		}
		
		function fn_changeSelect2() {
			$("#schAutoCmpCus").val('');
			$("#schAutoCmpCus").html('');
			$("#schAutoCmpCus").append('<option value="">전체</option>');
			$("#schAutoCmpCus").append('<option value="C">공용</option>');
			$("#schAutoCmpCus").append('<option value="P">개인</option>');
			
		}	
		
		function fn_changeAutoCmpCusValue1() {
			$("#autoCmpCus").val('');
		}	
			
		function fn_changeAutoCmpCusValue2() {
			$("#autoCmpCus").val('C');
		}			
	**/

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
			<form id="cstmSinfoForm" name="cstmSinfoForm" method="post" action="">
				<div class="left_content_head">
					<h2 class="sub_tit">민감정보 수집</h2>
				</div>
				<div class="inner_nobg">

					<!-- 왼쪽 컨텐츠 -->
					<div class="box_cont_left">
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">
									민감정보 수집
								</h3>
							</div>
							<div class="box_body">
							
								<input type="hidden" name="cstmUid" id="cstmUid" value="${cstmUid}" /> <!-- 고객아이디 - 키값 -->
								<input type="hidden" name="part" id="part" value="P1" />
								<div class="category_setting">
									<div class="category_radio_area"> <!-- 이름 -->
										이름<input type="text" name="name" id="name" value="${cstmSinfoVO.name}">
									</div>
									<div class="category_radio_area"> <!-- 연락처 -->
										연락처<input type="text" name="telNo1" id="telNo1" value="${cstmSinfoVO.telNo1}">										
										<input type="text" name="telNo2" id="telNo2" value="${cstmSinfoVO.telNo2}">		
										<input type="text" name="telNo3" id="telNo3" value="${cstmSinfoVO.telNo3}">		
									</div>									
									<div class="category_radio_area"> <!-- 예금주 -->
										예금주<input type="text" name="accountNm" id="accountNm" value="${cstmSinfoVO.accountNm}">										
									</div>	
									<div class="category_radio_area"> <!-- 은행코드 -->
										은행코드<select name="bankCd" id="bankCd">
											<option value="">전체</option>
										<c:forEach var="data" items="${bankList }" varStatus="status1">
											<option value="${data.bankCd }" <c:if test="${data.bankCd eq cstmSinfoVO.bankCd }">selected</c:if> >${data.bankNm }</option>
										</c:forEach>											
											
												</select>									
									</div>	
								
									<div class="category_radio_area"> <!-- 계좌번호 -->
										계좌번호<input type="text" name="accountNo" id="accountNo" value="${cstmSinfoVO.accountNo}">										
									</div>	
									<div class="category_radio_area"> <!-- 카드주 -->
										카드주<input type="text" name="cardCstm" id="cardCstm" value="${cstmSinfoVO.cardCstm}">										
									</div>									
									<div class="category_radio_area"> <!-- 카드코드 -->
										카드코드<select name="cardCd" id="cardCd">
											<option value="">전체</option>
										<c:forEach var="data" items="${cardList }" varStatus="status1">
											<option value="${data.cardCd }" <c:if test="${data.cardCd eq cstmSinfoVO.cardCd }">selected</c:if> >${data.cardNm }</option>
										</c:forEach>											
											
												</select>										
									</div>	
									<div class="category_radio_area"> <!-- 카드번호 -->
										카드번호<input type="text" name="cardNo1" id="cardNo1" style="width:100px;" value="${cstmSinfoVO.cardNo1}">										
										<input type="text" name="cardNo2" id="cardNo2" style="width:100px;" value="${cstmSinfoVO.cardNo2}">		
										<input type="text" name="cardNo3" id="cardNo3" style="width:100px;" value="${cstmSinfoVO.cardNo3}">												
										<input type="text" name="cardNo4" id="cardNo4" style="width:100px;" value="${cstmSinfoVO.cardNo4}">		

									</div>									
									<div class="category_radio_area"> <!-- 유효기간 -->
										유효기간<input type="text" name="checkMonth" id="checkMonth" value="${cstmSinfoVO.checkMonth}">										
										<input type="text" name="checkYear" id="checkYear" value="${cstmSinfoVO.checkYear}">	
									</div>	
									<div class="category_radio_area"> <!-- 기타 -->
										기타<textarea name="etc" id="etc" style="width:99%; height:100px">${cstmSinfoVO.etc}</textarea>									
									</div>									
									<button type="button" class="btn_temp_insert dev_save_btn" id="saveBtn" onclick="javascript:fn_save();"><i></i>전송</button>
								</div>
								<div class="temp_list_area">
								</div>
							</div>
							
							
						</div>
					</div>
					<!--// 왼쪽 컨텐츠 -->

					<!-- 오른쪽 컨텐츠 -->
					<div class="box_cont_right">
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">HTCO001VO 인터페이스</h3>
							</div>
							<div class="box_body">
							
								<div class="category_setting">
									<div class="category_radio_area"> 
										검색조건<input type="text" name="search_type" id="search_type" value="counselor_id">
									</div>
									<div class="category_radio_area"> 
										고객ID or 상담직원ID<input type="text" name="search_value" id="search_value" value="00000004">
									</div>									
									<div class="category_radio_area"> 
										채널<input type="text" name="channel" id="channel" value="A">
									</div>	
									<div class="category_radio_area">
										페이지번호<input type="text" name="page_no" id="page_no" value="1">
									</div>	
									<div class="category_radio_area"> 
										페이지 처리건수<input type="text" name="page_size" id="page_size" value="20">
									</div>	
									<div class="category_radio_area"> 
										검색 시작일<input type="text" name="start_date" id="start_date" value="">
									</div>	
									<div class="category_radio_area"> 
										검색 종료일<input type="text" name="end_datetime" id="end_datetime" value="">
									</div>	
									<div class="category_radio_area"> <!-- 기타 -->
										결과값<textarea name="etc2" id="etc2" style="width:99%; height:250px"></textarea>									
									</div>										
								</div>
								<button type="button" class="btn_temp_insert dev_save_btn" id="searchBtn" onclick="javascript:fn_testSearch();"><i></i>테스트</button>
							</div>
							
						</div>

					</div>
					<!--// 오른쪽 컨텐츠 -->
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
