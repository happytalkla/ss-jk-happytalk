<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${sessionVo.departCd eq 'TM' }">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/department_tm.css' />" />
</c:if>


<div class="popup popup_manager" id="dev_header_member_pop" style="display:none;">

	<div class="inner" style="height:250px;">
		<div class="popup_head">
			<h1 class="popup_tit">회원 정보</h1>
			<button type="button" class="btn_popup_close dev_close_header_pop">창닫기</button>
		</div>
		<div class="popup_body">
			<table class="tCont setting">
				<form id="hPasswdForm" name="hPasswdForm" method="post" >
				<input type="hidden" name="memberUid" value="${sessionVo.memberUid }" />
				<input type="hidden" name="username" value="${sessionVo.id }" />
				<caption>회원 정보 조회 폼입니다.</caption>
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
				</colgroup>
				<tbody>
					<tr>
						<th>권한</th>
						<td class="textL">
							${sessionVo.memberDivNm }
						</td>
						<th>부서</th>
						<td class="textL">
							${sessionVo.departNm }
						</td>
					</tr>
					<tr>
						<th>이름</th>
						<td class="textL">
							${sessionVo.name }
						</td>
						<th>아이디</th>
						<td class="textL">
							${sessionVo.id }
							<input type="hidden" name="sessionUserId" value="${sessionVo.id }" />
							<input type="hidden" name="sessionUserUid" value="${sessionVo.memberUid }" />
						</td>
					</tr>
					<tr>
						<th>공통계정 여부</th>
						<td class="textL">
							${sessionVo.NHnet }
						</td>
						<th>담당매니저</th>
						<td class="textL">
							${sessionVo.upperMemberNm }
						</td>
					</tr>
					<!-- <tr>
						<th>아너스넷</br>비밀번호</th>
						<td colspan="3">
							<input style="float:left;width:210px;" type="password" name="honorsPwd" id="honorsPwd">
							<button type="button" class="btn_go_cancel" style="width:200px; height:35px; float:right;"
								onClick="javascript:fnHonorPwdChange()">아너스넷 비밀번호 변경</button>
						</td>
					</tr> -->
				</tbody>
				</form>
			</table>
			<div class="popup_btn_area">
				<button type="button" class="btn_go_cancel dev_password_change" 
					data-memberUid="${sessionVo.memberUid}" data-url="${contextPath}" data-id="${sessionVo.id}" 
					style="width:125px" >비밀번호 변경</button>
				<button type="button" class="btn_go_cancel dev_cancel_header_pop">닫기</button>
			</div>
		</div>
	</div>

</div>
		<div class="logo_area"><%--
 		<c:if test="${requestUri ne '/happytalk/counselor' }">
			<button type="button" class="btn_all_menu">전체메뉴</button>
		</c:if>--%>
			<h1 class="logo"><a href="<c:url value='/admin' />"><img src="<c:url value='/images/hk/logo.png' />" alt="삼성증권"></a></h1>
			<span class="login_info">
				<a href="javascript:void(0);" id="dev_memberInfo" title="사용자 정보를 조회합니다." data-memberUid="${sessionVo.memberUid }">${sessionVo.name } ${sessionVo.memberDivNm }</a>
				<input type="hidden" name="hidConetextPath" value="${contextPath }" />
			</span>
			<div class="login_detail">
				<div>마지막 접속 시간: ${sessionVo.prevLoginDate}</div>
				<div>마지막 접속 IP: ${sessionVo.prevLinkIp}</div>
			</div>
<!--
			<button type="button" class="btn_happytalk">해피톡 문의하기</button>
 -->
 			<c:if test="${requestUri eq '/happytalk/counselor' }">
				<label class="switch w90 chat break_time">
					<input class="switch-input" id="workPoss" type="checkbox" />
					<span class="switch-label" data-on="상담가능" data-off="상담불가"></span>
					<span class="switch-handle"></span>
				</label>
 			</c:if>
		</div>
		<ul class="gnb_area">
			<c:choose>
				<c:when test="${sessionVo.memberDivCd eq 'S' }">
					<li><a href="<c:url value='/set/selectSet?topMenu=serviceSet' />"><i class="icon01"></i>서비스설정</a></li>
					<li><a href="<c:url value='/report/managerMain?topMenu=counselManage' />"><i class="icon02"></i>상담관리</a></li>
				</c:when>
				<c:when test="${sessionVo.memberDivCd eq 'A' }">
					<li><a href="<c:url value='/set/selectSet?topMenu=serviceSet' />"><i class="icon01"></i>서비스설정</a></li>
				</c:when>
				<c:when test="${sessionVo.memberDivCd eq 'M' }">
					<%--<li><a href="<c:url value='/set/selectSet?topMenu=serviceSet' />"><i class="icon01"></i>서비스설정</a></li>--%>
					<li><a href="<c:url value='/report/managerMain?topMenu=counselManage' />"><i class="icon02"></i>상담관리</a></li>
				</c:when>
				<c:when test="${sessionVo.memberDivCd eq 'C' }">
				</c:when>
			</c:choose>
			<c:if test="${sessionVo.cnsPossibleYn eq 'Y' }">
				<li><a href="<c:url value='/counselor?userId=${sessionVo.id }' />"><i class="icon03"></i>상담하기</a></li>
			</c:if>
			<%-- <li><a href="<c:url value='/logout' />"><i class="icon04"></i>로그아웃</a></li> --%>
			<li><a href="javascript:fnLogoutConfirm()"><i class="icon04"></i>로그아웃</a></li>
		</ul>
<script>
function fnLogoutConfirm(){
	if ($('#workPoss').prop('checked')){

/* 		$.ajax({
			url: HT_APP_PATH + '/api/logoutCheck/${sessionVo.memberUid }' ,
			method: 'post',
			data: {
				memberUid: ${sessionVo.memberUid },
			}
		}).done(function(data) { 
			if(data != "OK"){
				alert(data);
				return
			}

			var result = confirm("퇴근처리 하시겠습니까?");
			if (result){
				$.ajax({
					url: HT_APP_PATH + '/api/setting/${sessionVo.memberUid }' ,
					method: 'post',
					data: {
						workStatusCd: 'R',
					}
				}).done(function(data) {
					alert("처리되었습니다. 오늘도 수고 많으셨습니다.");
					console.log(data);
					console.log("---------------------------------------------");
					location.replace("<c:url value='/logout' />");
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: SAVE SETTING: ', textStatus);
				});
				 			}else {*/
				location.replace("<c:url value='/logout' />");
				/*}

		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: SAVE SETTING: ', textStatus);
		});
 */

	}
	else{
		location.replace("<c:url value='/logout' />");
	}
}
</script>
