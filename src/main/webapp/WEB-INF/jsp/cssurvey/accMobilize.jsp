<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>삼성증권 자동차 보상 사고접수</title>
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
	<script type="text/javascript">
	var data = new Array();
	<c:forEach var="data" items="${listStaff}" varStatus="status">
	data[${data.cmpsPschCrno}] = {
			"pschCrno" : "${data.pschCrno}"
			, "cvrClcd" : "${data.cvrClcd}"
			, "cmpsPschCrno" : "${data.cmpsPschCrno}"
			, "pschNm" : "${data.pschNm}"
			, "rcivYmd" : "${data.rcivYmd}"
			, "orgnNm" : "${data.orgnNm}"
			, "clerkTelNo3" : "${data.clerkTelNo3}"
			, "clerkTelNo1" : "${data.clerkTelNo1}"
			, "rcivSeqn" : "${data.rcivSeqn}"
			, "clerkTelNo2" : "${data.clerkTelNo2}"
			, "cmpsOgid" : "${data.cmpsOgid}"
			, "uprOrgnNm" : "${data.uprOrgnNm}"
			, "damRkno" : "${data.damRkno}"
			, "cmpsUprOgid" : "${data.cmpsUprOgid}"
			, "seqn" : "${data.seqn}"
	};
	</c:forEach>
	
	function fn_save(){

		var thisVal = $('input:radio[name=pschNum]:checked').val();
		
		if(!thisVal){
			alert('담당자를 선택하여 주시기 바랍니다.');
			return false;
		}
		else {
			var arrParam = data[thisVal];
			arrParam['slightAccdPtcd'] = $('input:radio[name=slightAccdPtcd]:checked').val();
			arrParam['rcivYmd'] = $('#rcivYmd').val();
			arrParam['rcivSeqn'] = $('#rcivSeqn').val();
			arrParam['mvoRn'] = $('#mvoRn').val();
			$.ajax({
				url : "<c:url value='/hk_survey/sendMCISMS' />",
				data : arrParam,	
				type : "post",
				success : function(result) {
					alert("전송이 완료되었습니다.")							
				},
				complete : function() {
					
				}
			});
		}
	}

	function fn_sLight( val ){
		$("#sLight").val(val);
		}
	function fn_windowClose(){
		window.open('about:blank', '_self').close();
	}
	function fn_openFileup(){
		var url = 'https://${actUrl}.heungkukfire.co.kr/FRM/chatbot/claim/fileupload.do';
		var accdRcivNo = '${AccYmd}${AccSeqn}';
		var cmpsPschCrno = $('input:radio[name=pschNum]:checked').val();
		url = url + "?" + "AccdRcivNo=" + accdRcivNo + "&cmpsPschCrno=" +cmpsPschCrno + "&type=em";
		if(!cmpsPschCrno) {
			alert('담당자를 선택하여 주십시오.');
			return false;
		}
		else window.open( url, "_blank" );
	}

	$(document).ready(function (){
		var closeYn = '${closeYn}';
		if (closeYn == 'Y'){
			alert('해당사고의 사고접수 시작일로부터 30일까지 확인 가능합니다.');
			window.open('about:blank', '_self').close();
		}
	});
	</script>

</head>

<body>
<form id="cstmSinfoForm" name="cstmSinfoForm" method="post" >
	<input type="hidden" name="rcivYmd" id="rcivYmd" value="${AccYmd}">
	<input type="hidden" name="rcivSeqn" id="rcivSeqn" value="${AccSeqn}">
	<input type="hidden" name="mvoRn" id="mvoRn" value="${AccmvoRn}">
	<div class="top">
		<img src="<c:url value='/images/hk/logo.png' />" alt="삼성증권">
	</div>
	<div class="accident contents" style="height: 580px;">
		<div class="title-btn">
			<div  style="color:white;">사고내용 전송</div>
		</div>
		<div class="data-area">
			<div class="info-box">
				<div>사고번호</div><input type="text" name="accNum" id="accNum" value="${AccYmd}-${AccSeqn}" readonly nofo/>
			</div>
			<div class="info-box">
				<div>담당자 유형</div>
				<div class="check-item"> 
					<ul>
					<c:forEach var="data" items="${listStaff}" varStatus="status">
						<li><label class="radio"><input type="radio" name="pschNum"  id="psch_${status.count}" value="${data.cmpsPschCrno}" / ><span class="ico"></span>
						<span class="radio-txt18" for="psch_${status.count}"  style="font-size:1.2em;">
							<c:choose>
							<c:when test="${data.cvrClcd eq '10'}"> 대인담당자 : </c:when>
							<c:when test="${data.cvrClcd eq '20'}"> 대물담당자 : </c:when>
							<c:when test="${data.cvrClcd eq '30'}"> 대인담당자 : </c:when>
							<c:when test="${data.cvrClcd eq '40'}"> 대물담당자 : </c:when>
							<c:when test="${data.cvrClcd eq '50'}"> 대인담당자 : </c:when>
							<c:when test="${data.cvrClcd eq '60'}"> 대인담당자 : </c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						${data.pschNm } T:<a href="tel:${data.clerkTelNo1 }-${data.clerkTelNo2 }-${data.clerkTelNo3 }" style="color:#333">${data.clerkTelNo1 }-${data.clerkTelNo2 }-${data.clerkTelNo3 }</a>
						</span></label></li>
					</c:forEach>
						
					</ul>
				</div>
			</div>
			<div class="info-box">
				<div>경미손상</div>
				<div class="btn_group"> 
					<label class="labl" >
					<input type="radio" name="slightAccdPtcd" value="Y" checked="checked"/>
					    <div>YES</div>
					</label>
					<label class="labl" >
					<input type="radio" name="slightAccdPtcd" value="N" />
					    <div>NO</div>
					</label>
				</div>
			</div>
			<div class="btn">
				<div  onclick="fn_save();">
					<a href="#">전송</a>
				</div>
			</div>
			<div class="info-box input-phnum">
				<div>사진/영상 전송</div>
			</div>
			<div class="input-phnum-btn" onclick="fn_openFileup();">
				<a href="#" >
					<div>파일전송</div>
				</a>
			</div>
			<div class="description notap point_color">
				<p>* 파일첨부를 위한 페이지로 이동합니다.</p>
			</div>
			<div class="input-phnum-btn" onclick="fn_windowClose();">
				<a href="#" >
					<div>닫기</div>
				</a>
			</div>
			<div class="description notap point_color">
			</div>
		</div>
	</div>
	<div class="footer">
		<img src="<c:url value='/images/hk/bottom.jpg' />" width="100%">
	</div>
</body>
</html>

