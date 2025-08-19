<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
	<script type="text/javascript">
	$(document).ready(function() {
		$.ajax({
			url : "<c:url value='/menu/selectMenuListAjax' />",
			type : "get",
			success : function(result) {
				<c:if test="${not empty topMenu && topMenu eq 'serviceSet'}">
					fn_serviceSetMenu(result);
				</c:if>
				<c:if test="${not empty topMenu && topMenu eq 'counselManage'}">
					<c:choose>
						<c:when test="${not empty sessionVo.id && sessionVo.id eq 'ssmonitor'}">
							fn_monitorManageMenu(result);
						</c:when>
						<c:otherwise>
							fn_counselManageMenu(result);
						</c:otherwise>
					</c:choose>	
				</c:if>
								
			},
			complete : function() {
			}
		});
	});
	var defaultUrl = "javascript:void(0);";
	function fn_serviceSetMenu(result) {
		$('#snb').html('');
		var htmlTxt="";
		var flag ="C";
		var userId = '${sessionVo.id }';
		htmlTxt += '<button type="button" class="btn_all_close">전체메뉴 닫기</button> \n';
		htmlTxt += '<div class="snb_menu_area serviceSet"> \n';		
		htmlTxt += '<ul class="snb_menu"> \n';	

		for (i=0;i<result.length;i++) {
			var name = result[i].menuNm
			var url = result[i].menuPath=='' ? defaultUrl : result[i].menuPath.indexOf('happybot') > -1 ? "javascript:fn_happybot('"+result[i].menuPath+"');" : '${contextPath}'+result[i].menuPath;
			//url = "<c:url value='" + url+ "' />";

			//url = url;
			var lvl = result[i].menuLvl;
			if (lvl=='1' && flag=='C') {
				if(name == "계정 관리" && userId == 'ssservice'){
					
				}	
				else htmlTxt += '<li><a href="'+url+'">'+name+'</a></li> \n';
			}else if (lvl=='1' && flag=='O' ) {
				htmlTxt += '</ul> \n';	
				htmlTxt += '<li><a href="'+url+'">'+name+'</a></li> \n';	
				flag='C';
			}else if (lvl=='2' && flag=='C' ) {
				htmlTxt += '<ul class="snb_smenu"> \n';	
				htmlTxt += '<li><a href="'+url+'">'+name+'</a></li> \n';	
				flag='O';
			}else if (lvl=='2' && flag=='O' ) {
				htmlTxt += '<li><a href="'+url+'">'+name+'</a></li> \n';	
			}
			
		}
		htmlTxt += '</ul> \n';		
		htmlTxt += '</div> \n';			
		$('#snb').html(htmlTxt);

	}
	function fn_counselManageMenu(result) {
		$('#snb').html('');
		var htmlTxt="";
		var flag ="C";
		htmlTxt += '<button type="button" class="btn_all_close">전체메뉴 닫기</button> \n';
		htmlTxt += '<div class="snb_menu_area"> \n';		
		htmlTxt += '<ul class="snb_menu"> \n';	
		
		for (i=0;i<result.length;i++) {
			var name = result[i].menuNm
			var url = result[i].menuPath=='' ? defaultUrl :  '${contextPath}'+result[i].menuPath;
			//url = 'happytalk/' +url;
			var lvl = result[i].menuLvl;
			var chat = result[i].chat;
			var countTag='';
			var subChat = '';
			if (chat == 'N') {
				chat='';
			}else if (chat == 'Y') {
				chat="class=\"chat\"";
			}else {
				countTag='<span class="count badge">0</span>';
				subChat='chat';
			}
			if (lvl=='1' && flag=='C') {
				htmlTxt += '<li><a href="'+url+'"'+chat +'>'+name+'</a></li> \n';	
			}else if (lvl=='1' && flag=='O' ) {
				htmlTxt += '</ul> \n';	
				htmlTxt += '<li><a href="'+url+'"'+chat +'>'+name+'</a></li> \n';		
				flag='C';
			}else if (lvl=='2' && flag=='C' ) {
				htmlTxt += '<ul class="snb_smenu '+subChat+'"> \n';	
				htmlTxt += '<li class='+chat+'><a href="'+url+'"> '+countTag+name+'</a></li> \n';	
				flag='O';
			}else if (lvl=='2' && flag=='O' ) {
				htmlTxt += '<li class='+chat+'><a href="'+url+'"> '+countTag+name+'</a></li> \n';	
			}
			
		}
		
		htmlTxt += '</ul> \n</br></br>';		
		htmlTxt += '</div> \n';
					
		$('#snb').html(htmlTxt);
	}
	function fn_monitorManageMenu(result) {
		$('#snb').html('');
		var htmlTxt="";
		var flag ="C";
		htmlTxt += '<button type="button" class="btn_all_close">전체메뉴 닫기</button> \n';
		htmlTxt += '<div class="snb_menu_area"> \n';		
		htmlTxt += '<ul class="snb_menu"> \n';	
		
		for (i=0;i<result.length;i++) {
			var name = result[i].menuNm
			var url = result[i].menuPath=='' ? defaultUrl :  '${contextPath}'+result[i].menuPath;
			//url = 'happytalk/' +url;
			var lvl = result[i].menuLvl;
			var chat = result[i].chat;
			var countTag='';
			var subChat = '';
			if (chat == 'N') {
				chat='';
			}else if (chat == 'Y') {
				chat="class=\"chat\"";
			}
			if(name == "상담 내역"){
				htmlTxt += '<li class='+chat+'><a href="'+url+'"> '+countTag+name+'</a></li> \n';	
			}
			
		}
		
		htmlTxt += '</ul> \n</br></br>';		
		htmlTxt += '</div> \n';
					
		$('#snb').html(htmlTxt);
	}
	function fn_happybot(url) {
		window.open(url);
	}
	</script>
		<c:if test="${not empty topMenu && (topMenu eq 'serviceSet' || topMenu eq 'counselManage')}">
			<!-- >div class="btn_view_area">
				<button type="button" class="btn_close_snb"><i>사이드 메뉴 접기</i></button>
				<button type="button" class="btn_view_snb"><i>사이드 메뉴 열기</i></button>
			</div-->
			<!-- snb_area -->
			<c:if test="${not empty topMenu && topMenu eq 'serviceSet'}">
				<div id="snb" >
				</div>
			</c:if>
			<c:if test="${not empty topMenu && topMenu eq 'counselManage'}">
				<div id="snb" class="counseling dev_counselManage_menu">
				</div>
				<form id="user" style="display: none;">
					<input type="hidden" name="id" value="<c:out value="${sessionVo.memberUid}" />" />
					<input type="hidden" name="cocId" value="<c:out value="${sessionVo.cocId}" />" />
					<input type="hidden" name="nickName" value="<c:out value="${sessionVo.name}" />" />
					<input type="hidden" name="userType" value="<c:out value="${sessionVo.memberDivCd}" />" />
					<input type="hidden" name="departCd" value="<c:out value="${sessionVo.departCd}" />" />
					<input type="hidden" name="rollType" value="M" />
				</form>
				<script src="<c:url value="/js/sockjs.min.js" />"></script>
				<script src="<c:url value="/js/stomp.min.js" />"></script>
				<!--<script src="<c:url value="/js/clipboard.min.js" />"></script>-->
				<script src="<c:url value="/js/common.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
				<script src="<c:url value="/js/chatRoom.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
				<script src="<c:url value="/js/chatRoomList.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
				<script src="<c:url value="/js/client.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
				<script src="<c:url value="/js/manager.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script> 
			</c:if>
		</c:if>