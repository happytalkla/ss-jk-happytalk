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

	var validUid = '';

	$(document).ready(function() {

		fn_cookieCheck();
	});


	function fn_cookieCheck() {

	     //저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
	     key = "username";
	     var cookie = getCookie(key);
	     $('#username').val(cookie);
	     if($('#username').val() !='' ){  //그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
	         $('#idSaveCheck').attr("checked", true);  //ID 저장하기를 체크 상태로 두기.
	     }

	     $('#idSaveCheck').change(function(){  //체크박스에 변화가 있다면,
	         if($('#idSaveCheck').is(":checked")){  //ID 저장하기 체크했을 때,
	             setCookie(key, $('#username').val(), 7);  //7일 동안 쿠키 보관
	         }else{  //ID 저장하기 체크 해제 시,
	             deleteCookie(key);
	         }
	     });

	      //ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
	     $('#username').keyup(function(){  //ID 입력 칸에 ID를 입력할 때,
	         if($('#idSaveCheck').is(":checked")){  //ID 저장하기를 체크한 상태라면,
	             setCookie(key, $('#username').val(), 7); // 7일 동안 쿠키 보관
	         }
	     });
	}

	$(document).on("click", "#loginForm .btn", function() {
		if($("#username" ).val()==''){
			alert("ID를 입력해 주세요.")
			return false;
		}
		if($("#password" ).val()==''){
			alert("패스워드를 입력해 주세요.")
			return false;
		}
		if (!fn_selectValidCheck()) {
			return false;
		}
		if (!fn_selectFailCheck()) {
			return false;
		}
		if (!fn_selectIdPwdCheck()) {
			return false;
		}
		if (!fn_accessIPCheck()) {
			return false;
		}
		if (!fn_passwd90Check()) {
			var url = '${contextPath }/changePasswdView?memberUid='+$("#memberUid" ).val() +"&id="+$("#username").val()+ "&validUid=" + validUid;
			window.open("<c:url value='"+url+"'/>");
			return false;
		} else {
			fn_submit();
			return false;
		}
		//debugger;
	});

	/** 계정 활성화 체크**/
	function fn_selectValidCheck(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/selectValidCheck'/>",
			data : $("#loginForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					rtn=true;
				}else{
					if ('RESULT_CD_LOGIN_DEFAULT_PASSWD' === result.rtnCd) {
						alert(result.rtnMsg);
						var url = '${contextPath}/changePasswdView?cocId=' + $("#username").val() +"&id="+$("#username").val()+ "&validUid=" + result.validUid;
						window.open("<c:url value='"+url+"'/>");
					} else {
						alert(result.rtnMsg);
					}
					rtn=false;
				}
			},
			complete : function() {
			}
		});
		return rtn;
	}
	
	/** 로그인 실패횟수 체크 (6이상)**/
	function fn_selectFailCheck(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/selectFailCheck'/>",
			data : $("#loginForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					if (result.cnt < 6){
						rtn=true;
					}else {
						alert (result.rtnMsg)
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

	/** 아이디 비밀번호 체크 **/
	function fn_selectIdPwdCheck(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/selectIdPwdCheck'/>",
			data : $("#loginForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					if (result.cnt==1){
						rtn=true;
					}else if(result.cnt==2){
						alert(result.rtnMsg);
						var url = '${contextPath}/changePasswdView?cocId=' + $("#username").val() +"&id="+$("#username").val()+ "&validUid=" + result.validUid;
						window.open("<c:url value='"+url+"'/>");
					}else {
						alert (result.rtnMsg)
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

	/** 접근PC의 IP 체크 **/
	function fn_accessIPCheck(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/accessIPCheck'/>",
			data : $("#loginForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					if (result.cnt==1){
						$("#memberUid" ).val(result.memberUid);
						rtn=true;
					}else {
						alert(result.rtnMsg);
						$("#memberUid" ).val(result.memberUid);
						rtn=false;
						validUid = result.validUid;
					}
				}else{
					alert(result.rtnMsg);
					rtn=false;
				}
			},
			complete : function() {
			}
		});
	}
	
	/** 비밀번호 변경 90일이상 체크 **/
	function fn_passwd90Check(){
		var rtn;
		$.ajax({
			url : "<c:url value='/api/login/passwd90Check'/>",
			data : $("#loginForm").serialize(),
			type : "post",
			async : false,
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					if (result.cnt==1){
						$("#memberUid" ).val(result.memberUid);
						rtn=true;
					}else {
						alert(result.rtnMsg);
						$("#memberUid" ).val(result.memberUid);
						rtn=false;
						validUid = result.validUid;
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


	function fn_submit() {
		$("#loginForm").attr("action", "<c:url value='/login-processing' />");
		$("#loginForm").submit();
		return false;

	}


	</script>
</head>

<body id="error">
	<form id="loginForm" name="loginForm" method="post" >
	<input type="hidden" name="memberUid" id="memberUid" value="" />
	<div class="top-wrapper">
		<div class="logo">
			<%-- <img src="<c:url value='/images/happytalk-logo.png'/>" > --%><img src="<c:url value='/images/hk/logo.png' />" alt="삼성증권">
		</div>
	</div>


	<div class="login-wrapper">
		<div style="padding:74px 70px;">
			<div>
				<label>아이디</label><input type="text" name="username" id="username" placeholder="" />
			</div>
			<div>
				<label>비밀번호</label><input type="password" name="password" id="password"/>
			</div>
			<button class="btn" >로그인</button>

			<div class="login-menu">
				<label class="checkbox-wrap" style="width: 120px;">
				<input type="checkbox" name="idSaveCheck" id="idSaveCheck" value="" ><i class="check-icon"></i>ID 저장</label>
				<label class="manual" ><a href="<c:url value='/images/user_manual.pdf' />" target="_blank">사용자 매뉴얼</a></label>
			</div>
			<!-- <button class="btn" onclick="fn_submit();" >로그인</button>
 -->

		</div>
	</div>
	</form>
</body>
</html>
