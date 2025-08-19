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
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/login_style.css' />" />
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

	$(document).on("click", "#passwdForm .btn", function() {
		if($("#oldPassword" ).val()==''){
			alert("기존 비밀번호를 입력해 주세요.")
			return false;
		} 
		
		if($("#newPassword" ).val()==''){
			alert("신규 비밀번호를 입력해 주세요.")
			return false;
		}
		if($("#newPassword2" ).val()==''){
			alert("신규 비밀번호를 확인해 주세요.")
			return false;
		}
		<c:if test="${nonHnet ne 'Y' }">
		if($("#honorsPassword" ).val()==''){
			alert("아너스넷 비밀번호를 확인해 주세요.")
			return false;
		}
		</c:if>
		//if (!fn_selectOldPwdCheck()) { //기존 비밀번호 확인
			//return false;
		//}
		//if (!fn_oldPwdNewPwdCheck()) {    //기존 비밀번호와 새로운 비밀번호 같은지 체크
			//return false;
		//}
		if (!fn_confirmPassword()) {    //패스워드 확인
			return false;
		}
		//if (!fn_passwd3DuplCheck()) {    //기존 비밀번호 3회 중복 변경 체크
			//return false;
		//}
		if (!fn_honorsChangePasswd()) {  //아너스넷 패스워드 변경
			return false;
		}
		if (!fn_loginChangePasswd()) {  //패스워드 변경
			return false;
		}
		
		alert("비밀번호 변경이 완료되었습니다.");
		fn_loginPage();
		return false;
	});

	/** 신규 비밀번호 확인 **/
	function fn_confirmPassword() {
		var newPassword = $('#newPassword').val();
		var newPassword2 = $('#newPassword2').val();		
		
		if (newPassword != newPassword2) {
			alert ("신규비밀번호가 맞지 않습니다.");
			return false;
		}
		return true;
	}
	
	/** 기존 비밀번호 확인 **/
	function fn_selectOldPwdCheck(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/selectOldPwdCheck'/>",
			data : $("#passwdForm").serialize(),	
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					if (result.cnt==1){
						rtn=true;
					}else {
						alert(result.rtnMsg);
						rtn=false;
					}
				}else{
					alert(result.rtnMsg);
					rtn=false;
				}							
			},
			complete : function() {
			}
		});
		return rtn;
	}

	/** 기존비밀번호와 새로운 비밀번호 체크 **/
	function fn_oldPwdNewPwdCheck() {
		var oldPassword = $('#oldPassword').val();
		var newPassword = $('#newPassword').val();		
		
		if (oldPassword == newPassword) {
			alert ("기존비밀번호와 같은 비밀번호는 사용하실 수 없습니다.");
			return false;
		}
		return true;
	}
	/** 기존 비밀번호 3회 중복체크 **/
	function fn_passwd3DuplCheck(){
		var rtn;
		var pwd = $('#newPassword').val();
		$('#pwd').val(pwd); 
		$.ajax({
			url : "<c:url value='/api/login/passwd3DuplCheck'/>",
			data : $("#passwdForm").serialize(),	
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					if (result.cnt==0){
						rtn=true;
					}else {
						alert(result.rtnMsg);
						rtn=false;
					}
				}else{
					alert(result.rtnMsg);
					rtn=false;
				}							
			},
			complete : function() {
			}
		});
		return rtn;
	}	

	/** 비밀번호 update **/
	function fn_loginChangePasswd(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/loginChangePasswd'/>",
			data : $("#passwdForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					rtn=true;
				}else{
					alert(result.rtnMsg);
					rtn=false;
				}
			},
			complete : function() {
			}
		});
		return rtn;
	}	

	function fn_loginPage() {
		try {
			window.close();
		} catch (e) {}
	}

	/** 아너스넷 비밀번호 update **/
	function fn_honorsChangePasswd(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/honorsChangePasswd'/>",
			data : $("#passwdForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					rtn=true;
				}else{
					alert(result.rtnMsg);
					rtn=false;
				}
			},
			complete : function() {
			}
		});
		return rtn;
	}	

	function fn_loginPage() {
		try {
			window.close();
		} catch (e) {}
	}


	</script>


	</script>
</head>

<body id="error">
	<form id="passwdForm" name="passwdForm" method="post" action="<c:url value='/login-processing' />">
	<input type="hidden" name="cocId" id="cocId" value="${cocId}" />
	<input type="hidden" name="id" 	id="id" value="${id}" />
	<input type="hidden" name="pwd" id="pwd" value="${pwd}" />
	<input type="hidden" name="memberUid" id="memberUid" value="${memberUid}" />
	
	<div class="top-wrapper">
		<div class="logo">
			<img src="<c:url value='/images/hk/logo.png'/>" >
			<%-- <img src="<c:url value='/images/happytalk-logo.png'/>" > --%>
		</div>
	</div>


	<div class="cpw-wrapper">
		<div style="padding:60px 60px;">
			<div>
				<label>현재 비밀번호</label><input type="password" name="oldPassword" id="oldPassword" />
			</div>
			<div>
				<label>신규 비밀번호</label><input type="password" name="newPassword" id="newPassword" onblur="javascript:passwordCheck(this.value, this);"/>
			</div>
			<div>
				<label>신규 비밀번호 확인</label><input type="password" name="newPassword2" id="newPassword2" />
			</div>
			<c:if test="${nonHnet ne 'Y' }">
			<div>
				<label>아너스넷 비밀번호</label><input type="password" name="honorsPassword" id="honorsPassword" />
			</div>
 			</c:if>
			
			<button class="btn">비밀번호 변경</button>

			<div class="description">
				<span><i></i> 비밀번호 규칙을 확인해 주세요!</span>
				<ul>
					<li>※ 최근 설정한 3개의 비밀번호는 사용이 불가합니다.</li>
					<li>※ 3자 이상의 연속문자 및 동일문자는 사용이 불가합니다.</li>
					<li>※ 영문, 숫자, 특수문자를 모두 조합하여 8~20자로 생성해야 합니다.</li>
				</ul>

			</div>


		</div>
	</div>
	</form>
</body>
</html>
