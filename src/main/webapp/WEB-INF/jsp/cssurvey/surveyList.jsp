<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>삼성증권 자동차 보상 고객만족도 조사</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/data_style.css' />" />
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.dotdotdot.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/common.js' />"></script>


</head>

<body>
<form id="cstmSinfoForm" name="cstmSinfoForm" method="post" action="">
	<input type="hidden" name="rcivYmd" id="rcivYmd" value="${rcivYmd}" /> <!-- 고객아이디 - 키값 -->
	<input type="hidden" name="rcivSeqn" id="rcivSeqn" value="${rcivSeqn}" />
	<input type="hidden" name="seqNum" id="seqNum" value="${seqNum}" />
	<input type="hidden" name="depositCode" id="depositCode" value="${depositCode}" />
	<input type="hidden" name="damageSeqNum" id="damageSeqNum" value="${damageSeqNum}" />
	<input type="hidden" name="insaId" id="insaId" value="${insaId}" />
	<input type="hidden" name="closeYmd" id="closeYmd" value="${closeYmd}" />
	
	<input type="hidden" name="hidque1" name="hidque1">
	<input type="hidden" name="hidque2" name="hidque2">
	<input type="hidden" name="hidque3" name="hidque3">
	<input type="hidden" name="hidque4" name="hidque4">
	<input type="hidden" name="hidque5" name="hidque5">
	<input type="hidden" name="hidque6" name="hidque6">
	<input type="hidden" name="hidque4-1" name="hidque4-1">
	<input type="hidden" name="hidque5-1" name="hidque5-1">
	<input type="hidden" name="hidque6-1" name="hidque6-1">
	<input type="hidden" name="hidque7" name="hidque7">
	</form>
	<div class="top">
		<img src="<c:url value='/images/hk/logo.png' />" alt="삼성증권">
	</div>
	<div class="contents">
		<div class="title-btn">
			<div >
				<span style="color:white">고객 만족도 조사</span>
			</div>
		</div>
		<div class="top-txt notap" >
			<p style="text-align:center;">자동차보상서비스 만족도 조사입니다. 
			</br>설문에 답해주시면 더 큰 고객서비스로 보답하겠습니다.</p>
		</div>
		
		<div class="data-area">
			<div id="warnnigDiv" class="alertBox">
			</div>
			<div class="info-box">
				<div class="questionbox" id="que_1">
					1. <span class="no">고객님께서는 사고 접수 후 신속하게 보상직원으로부터 최초 안내 전화를 받으셨나요?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que1_1" name="que1" value="1" onclick="fn_getExam('1')">
						<label for="que1_1"><span></span>예, 그렇습니다.</label>
						<input type="radio" class="radio_18" id="que1_2" name="que1" value="2" onclick="fn_getExam('1')">
						<label for="que1_2"><span></span>아니오</label> 
					</div>
				</div>
				
				<div class="questionbox disable" id="que_2">
					2. <span class="no">고객님께서는 보상 처리 진행중에 중간과정에 대해 안내전화를 받으셨나요?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que2_1" name="que2" value="1" onclick="fn_getExam('2')">
						<label for="que2_1"><span></span>예, 그렇습니다.</label>
						<input type="radio" class="radio_18" id="que2_2" name="que2" value="2" onclick="fn_getExam('2')">
						<label for="que2_2"><span></span>아니오</label> 
					</div>
				</div>
				
				<div class="questionbox disable" id="que_3">
					3. <span class="no">고객님께서는 보상 처리 최종 완료 후 이에 대한 안내 전화를 받으셨나요?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que3_1" name="que3" value="1" onclick="fn_getExam('3')">
						<label for="que3_1"><span></span>예, 그렇습니다.</label>
						<input type="radio" class="radio_18" id="que3_2" name="que3" value="2" onclick="fn_getExam('3')">
						<label for="que3_2"><span></span>아니오</label> 
					</div>
				</div>
				<div class="questionbox disable" id="que_4">
					4. <span class="no">고객님께서는 이번 사고처리 보상담당자와의 연락에 대한 만족도 평가를 해주신다면 매우만족, 만족, 보통, 불만족, 매우불만족 어디에 해당하십니까?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que4_1" name="que4" value="1" onclick="fn_getExam('4')">
						<label for="que4_1"><span></span>매우만족</label>
						<input type="radio" class="radio_18" id="que4_2" name="que4" value="2" onclick="fn_getExam('4')">
						<label for="que4_2"><span></span>만족</label>
						<input type="radio" class="radio_18" id="que4_3" name="que4" value="3" onclick="fn_getExam('4')">
						<label for="que4_3"><span></span>보통</label>
						<input type="radio" class="radio_18" id="que4_4" name="que4" value="4" data-point="2" onclick="fn_getExam('4')">
						<label for="que4_4"><span></span>불만족</label>
						 <input type="radio" class="radio_18" id="que4_5" name="que4" value="5" data-point="1" onclick="fn_getExam('4')">
						<label for="que4_5"><span></span>매우불만족</label>
					</div>
					<div class="disable" id="sub_4">
						<div class="subquestionbox">
							4-1. <span class="no">불만족하셨다면 그 내용은 무엇입니까?</span>
						</div>
						<div class="staff_radio_area subexambox">
							<input type="radio" class="radio_18" id="que4-1_1" name="que4-1" value="1. 담당자 통화연결 어려움" onclick="fn_getExam('4-1')">
							<label for="que4-1_1"><span></span>담당자 통화연결 어려움</label>
							<input type="radio" class="radio_18" id="que4-1_2" name="que4-1" value="2. 처리과정에 대한 안내 부족" onclick="fn_getExam('4-1')">
							<label for="que4-1_2"><span></span>처리과정에 대한 안내 부족</label>
							<input type="radio" class="radio_18" id="que4-1_3" name="que4-1" value="3" onclick="fn_getExam('4-1')">
							<label for="que4-1_3"><span></span>기타</label>
							<input type="text" placeholder="의견을 적어주세요" name="que4-1-text" id="que4-1-text" disabled>
						</div>
					</div>
				</div>
				
				<div class="questionbox disable" id="que_5">
					5. <span class="no">고객님께서는 이번 사고처리 보상담당자와의 업무능력에 대한 만족도 평가를 해주신다면 매우만족, 만족, 보통, 불만족, 매우불만족 어디에 해당하십니까?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que5_1" name="que5" value="1" onclick="fn_getExam('5')">
						<label for="que5_1"><span></span>매우만족</label>
						<input type="radio" class="radio_18" id="que5_2" name="que5" value="2" onclick="fn_getExam('5')">
						<label for="que5_2"><span></span>만족</label>
						<input type="radio" class="radio_18" id="que5_3" name="que5" value="3" onclick="fn_getExam('5')">
						<label for="que5_3"><span></span>보통</label>
						<input type="radio" class="radio_18" id="que5_4" name="que5" value="4" data-point="2" onclick="fn_getExam('5')">
						<label for="que5_4"><span></span>불만족</label>
						 <input type="radio" class="radio_18" id="que5_5" name="que5" value="5" data-point="1" onclick="fn_getExam('5')">
						<label for="que5_5"><span></span>매우불만족</label>
					</div>
					<div class="disable" id="sub_5">
						<div class="subquestionbox">
							5-1. <span class="no">불만족하셨다면 그 내용은 무엇입니까?</span>
						</div>
						<div class="staff_radio_area subexambox">
							<input type="radio" class="radio_18" id="que5-1_1" name="que5-1" value="1. 사고조사 및 과실에 대한 불만" onclick="fn_getExam('5-1')">
							<label for="que5-1_1"><span></span>사고조사 및 과실에 대한 불만</label>
							<input type="radio" class="radio_18" id="que5-1_2" name="que5-1" value="2. 고객과의 소통부족" onclick="fn_getExam('5-1')">
							<label for="que5-1_2"><span></span>고객과의 소통부족</label>
							<input type="radio" class="radio_18" id="que5-1_3" name="que5-1" value="3" onclick="fn_getExam('5-1')">
							<label for="que5-1_3"><span></span>기타</label>
							<input type="text" placeholder="의견을 적어주세요" name="que5-1-text" id="que5-1-text" disabled>
						</div>
					</div>
				</div>
				
				<div class="questionbox disable" id="que_6">
					6. <span class="no">고객님께서는 이번 사고처리에 대한 전반에 대한 종합만족도 평가를 해주신다면 매우만족, 만족, 보통, 불만족, 매우불만족 어디에 해당하십니까?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que6_1" name="que6" value="1" onclick="fn_getExam('6')">
						<label for="que6_1"><span></span>매우만족</label>
						<input type="radio" class="radio_18" id="que6_2" name="que6" value="2" onclick="fn_getExam('6')">
						<label for="que6_2"><span></span>만족</label>
						<input type="radio" class="radio_18" id="que6_3" name="que6" value="3" onclick="fn_getExam('6')">
						<label for="que6_3"><span></span>보통</label>
						<input type="radio" class="radio_18" id="que6_4" name="que6" value="4" onclick="fn_getExam('6')">
						<label for="que6_4"><span></span>불만족</label>
						 <input type="radio" class="radio_18" id="que6_5" name="que6" value="5" onclick="fn_getExam('6')">
						<label for="que6_5"><span></span>매우불만족</label>
					</div>
					<div class="disable" id="sub_6">
						<div class="subquestionbox">
							6-1. <span class="no">불만족하셨다면 그 내용은 무엇입니까?</span>
						</div>
						<div class="staff_radio_area subexambox">
							<input type="radio" class="radio_18" id="que6-1_1" name="que6-1" value="1. 불친절" onclick="fn_getExam('6-1')">
							<label for="que6-1_1"><span></span>불친절</label>
							<input type="radio" class="radio_18" id="que6-1_2" name="que6-1" value="2. 보상처리금액" onclick="fn_getExam('6-1')">
							<label for="que6-1_2"><span></span>보상처리금액</label>
							<input type="radio" class="radio_18" id="que6-1_3" name="que6-1" value="3" onclick="fn_getExam('6-1')">
							<label for="que6-1_3"><span></span>기타</label>
							<input type="text" placeholder="의견을 적어주세요" name="que6-1-text" id="que6-1-text" disabled>
						</div>
					</div>
					
				</div>
				<div class="questionbox disable" id="que_7">
					7. <span class="no">보상처리 불만족 관련하여 담당자에게 연락을 받으시겠습니까?</span>
					<div class="staff_radio_area exambox">
						<input type="radio" class="radio_18" id="que7_1" name="que7" value="1" onclick="fn_getExam('7')">
						<label for="que7_1"><span></span>예, 그렇습니다.</label>
						<input type="radio" class="radio_18" id="que7_2" name="que7" value="2" onclick="fn_getExam('7')">
						<label for="que7_2"><span></span>아니오</label> 
					</div>
				</div>
				<div class="questionbox disable" id="que_8">
					<span class="no">설문에 참여하여 주셔서 감사합니다.</span>

				</div>
			</div>
		</div>
		<div class="btn">
			<input type="hidden" id="procNum" value="1">
			<input type="hidden" id="lastQue" value="0">
			<div  id="btn_next" onclick="javascript:fn_nextque();" class="disable">
				<a href="#" >다음</a>
			</div>
		</div>
		<div class="btn">
			<div  id="sendBtn" onclick="javascript:fn_save();"  class="disable">
				<a href="#" >완료</a>
			</div>
		</div>
	</div>
	<div class="footer">
		<img src="<c:url value='/images/hk/bottom.jpg' />" width="100%">
	</div>
<script type="text/javascript">
function fn_save(){
	
	$.ajax({
		url : "<c:url value='/hk_survey/sendServeyResult' />",
		data : $("#cstmSinfoForm").serialize(),	
		type : "post",
		success : function(result) {
			if (result.returnMsg == null){
				alert("설문조사에 응답하여 주셔서 감사합니다.")
			}
			else alert(result.returnMsg);							
		},
		complete : function() {
			window.open('about:blank', '_self').close();
		}
	});
}
function fn_getExam(val){
	var sendBtn = $("#sendBtn");
	var nextBtn = $("#btn_next");
	var examVal = $('input[name="que' + val + '"]:checked').val();		//선택보기
	$('input[name="hidque' + val + '"').val(examVal);
	if(examVal < 4) $("#sub_"+val).hide();
	
	if ($("#lastQue").val() == "0" && val == "6") {		//마지막 문항 미노출
		sendBtn.show();
		nextBtn.hide();
	}
	else if ($("#lastQue").val() == "1" && val == "7") {		//마지막 문항 노출
		sendBtn.show();
		nextBtn.hide();
	}
	else nextBtn.show();
	
}
function fn_nextque(){
	var thisQueNum = String($('#procNum').val());						//문항 순차
	var nextQueNum = String($('#procNum').val() * 1+1);			//다음 문항
	var examVal = $('input[name="que' + thisQueNum + '"]:checked').val();		//선택보기
	var subExamVal = $("input[name='que" + thisQueNum+"-1']:checked").val();		//추가문항 선택
	var warnBox = $("#warnnigDiv");						//경고창
	var sendBtn = $("#sendBtn");
	var nextBtn = $("#btn_next");
	nextBtn.hide();
	if(typeof examVal != "undefined"){

		if((thisQueNum * 1) > 3){
			if(examVal > 3) {
				nextBtn.hide();
				$("#sub_" + thisQueNum ).show();
				$("#lastQue").val('1');
				if( typeof subExamVal == "undefined"){
					warnBox.html("설문 문항에 답변을 부탁드립니다.");
					warnBox.show();
				}
				else if(subExamVal == "3" && $("#que" + thisQueNum+"-1-text").val() == ""){
					warnBox.html("기타를 선택하신 경우 세부사항에 대하여 입력하여 주세요.");
					$("#que" + thisQueNum+"-1-text").focus();
					nextBtn.show();
					warnBox.show();
				}
				else {
					$("#que_"+thisQueNum).hide();
					$("#que_"+nextQueNum).show();
					$("#procNum").val(nextQueNum);
					if(subExamVal == "3") $('input[name="hidque' + thisQueNum + '-1"').val("3. "+$("#que" + thisQueNum+"-1-text").val());
					warnBox.hide();
				}
			}
			else {
				$("#que_"+thisQueNum).hide();
				$("#que_"+nextQueNum).show();
				$("#procNum").val(nextQueNum);
				warnBox.hide();
			}
		}
		else {
			$("#que_"+thisQueNum).hide();
			$("#que_"+nextQueNum).show();
			$("#procNum").val(nextQueNum);
			warnBox.hide();
		}
	}
	else{
		warnBox.html("설문 문항에 답변을 부탁드립니다.");
		warnBox.show();
		nextBtn.hide();
	}
}

$(document).on("click", "input[name='que4']", function() {
	
	if($(this).val()>3) {
		$("#sub_4").show();
	}
});

$(document).on("click", "input[name='que5']", function() {
	
	if($(this).val()>3) {
		$("#sub_5").show();
	}
});
$(document).on("click", "input[name='que6']", function() {
	
	if($(this).val()>3) {
		$("#sub_6").show();
	}
});
$(document).on("click", "input[name='que4-1']", function() {
	
	if($(this).val()==3) {
		$("#que4-1-text").prop("disabled", false);
		$("#que4-1-text").focus();
	}
	else {
		$("#que4-1-text").val("");
		$("#que4-1-text").prop("disabled", true);
	}
});
$(document).on("click", "input[name='que5-1']", function() {
	
	if($(this).val()==3) {
		$("#que5-1-text").prop("disabled", false);
		$("#que5-1-text").focus();
	}
	else {
		$("#que5-1-text").val("");
		$("#que5-1-text").prop("disabled", true);
	}
});

$(document).on("click", "input[name='que6-1']", function() {
	
	if($(this).val()==3) {
		$("#que6-1-text").prop("disabled", false);
		$("#que6-1-text").focus();
	}
	else {
		$("#que6-1-text").val("");
		$("#que6-1-text").prop("disabled", true);
	}
});
$(document).ready(function (){
	var closeDate = '${closeYn}';
	if (closeDate == 'Y'){
		alert('설문 참여는 시작일로부터 5일간 참여 가능합니다.');
		window.open('about:blank', '_self').close();
	}
});
$(document).ready(function (){
	var newSurvey = ${newSurvey};
	if (newSurvey > 0){
		alert('이미 설문에 참여하셨습니다.');
		window.open('about:blank', '_self').close();
	}
});

</script>

</body>
</html>
