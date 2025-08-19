<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta charset="utf-8" />
	<meta name="Author" content="">
	<meta name="Keywords" content="">
	<meta name="Description" content="">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<c:url value='${skinCssUrl}' />" />
	<style type="text/css">
	.swiper-container, .swiper-slide { /* 캐러셀 높이 자동 조절 */
		height: auto;
	}
	 .inHighLigth {
	 	color: #286df0 !important;
    	background-color: #d3e1fc;
	 }
	 .text_ellipsis{
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: normal;
		text-align: left;
		word-wrap: break-word;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
	 }
	</style>
</head>

<body id="chat" class="layer-on">
	<div class="chat-area"> <!-- 종료 클래스 end -->

		<!-- 00.채팅창:head-->
		<%-- <div id="chat_head" class="chat-top">
			 
			<button type="button" class="btn_go_history">상담내역 가기</button>
			<!--button type="button" class="btn_go_prev">뒤로가기</button-->
			<h1 class="chat_logo"><img src="<c:url value="/images/happytalk-logo.png" />" alt="O2Talk">
			</h1>
			
			<div class="chat-top-inner">
			
			
				<div class="left">
					<a href="javascript:void(0);" title="이전" class="dptable type-1 btn_back"><span class="btn-before">이전</span></a>
					<h1 class="dptable">
					<c:choose>
						<c:when test="${user.cstm_link_div_cd == 'A'}"><span class="tit-h1">삼성증권 TALK</span></c:when>
						<c:when test="${user.cstm_link_div_cd == 'B'}"><span class="tit-h1">카카오톡</span></c:when>
						<c:when test="${user.cstm_link_div_cd == 'C'}"><span class="tit-h1">채팅상담</span></c:when>
						<c:when test="${user.cstm_link_div_cd == 'D'}"><span class="tit-h1">업무상담</span></c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					</h1>
				</div>
				<div class="right">
					<!-- <button type="button" class="btn_cs_call" 		title="상담직원 통화"><a href="tel:1588-2323"><i></i>상담직원 통화</a></button> -->
					<c:if test="${!hideCallBtn}">
						<!-- <span class="dptable"><a href="tel:1588-2323" title="전화" class="btn-tel">상담직원 통화</a></span> -->
						<span class="dptable"><a href="mpopcmd://tel?number=15882323" title="전화" class="btn-tel">상담직원 통화</a></span>
					</c:if>
					<!-- <button type="button" class="btn_chat_search" 	title="검색"><i></i>검색</button> -->
					<span class="dptable btn_chat_search"><a href="javascript:void(0);" title="검색" class="btn-search">검색</a></span>
				</div>
				<!-- 
				<div class="search_div" style="float:right; display: none;" >
					<button type="button" class="btn_search_exid">닫기</button>
					<input type="text" class="chat_search">
					<button type="button" class="btn_search_clear">제거</button>
					<button type="button" class="btn_search_prev">이전</button>
					<button type="button" class="btn_search_next">다음</button>
				</div>
				 -->
			</div>
			<div class="chat-top-inner search_div" style="display: none;" >
				<div class="left">
					<a href="javascript:void(0);" title="이전" class="dptable type-1 btn_search_exid"><span class="btn-before">이전</span></a>
					<h1 class="dptable">
						<input type="text" class="search-chat-top chat_search"placeholder="대화 내용 검색">
					</h1>
				</div>
				<div class="right">
					<span class="dptable"><a href="javascript:void(0);" title="up" class="btn-cls btn_search_clear" style="display:none">close</a></span>
					<span class="dptable"><a href="javascript:void(0);" title="up" class="btn-up btn_search_prev">up</a></span>
					<span class="dptable"><a href="javascript:void(0);" title="down" class="btn-down btn_search_next">down</a></span><!-- 활성화 class on -->
				</div>
			</div>
			
		</div> --%>
		<!--// 00.채팅창:head-->
	
		<!-- 00.채팅창:컨텐츠영역-->
		<div id="chat_body"  class="chat-body" ></div>
		<!--// 00.채팅창:컨텐츠영역 -->
	
		<!-- 00.채팅창:bottom -->
		<div id="chat_bottom" class="chat-bottom">
			<div class="qna-line-wrap type-ing">
				<%-- <c:if test="${user.cstm_div_cd ne 'CSTM'}">
					<div class="icon btn_chat_term" title="용어검색"><span class="bg">용어 검색</span></div>
				</c:if> --%>
				<!-- 채팅중 -->
				<div class="inner" style="width:100%">
					<textarea class="input-connect" placeholder="궁금한 내용을 질문해 주세요."
					data-placeholder="궁금한 내용을 질문해 주세요." id="autosize_form01" onkeydown="if(event.keyCode === 13)return false;" readonly></textarea>
					<div class="btn_send_area">
						<a href="javascript:void(0);" class="btn-connect out" title="메세지 보내기">메세지 보내기</a>
					</div>
				</div>
				<!-- 종료 -->
				<div class="inner review_inner" id="review_inner" style="width:100%; display:none;">
					<textarea class="input-connect" placeholder="한 줄 평가 부탁드려요. (100자 이내)" maxlength="100" title="한 줄 평가 부탁드려요. (100자 이내)"></textarea>
					<div class="btn_send_area" >
						<a href="javascript:void(0);" class="btn-connect out" title="완료">완료</a>
					</div>
				</div>
			</div>
			<input type="checkbox" id="check_enter_key" value="1" style="display:none;height:0px;width:0px;"/>
		</div>
		
		<!-- 00.alret -->
		<div class="layer-area" id="alret_layer" style="display:none;">
			<dl class="inner">
				<dt class="tit" id="alret_desc">검색결과가 없습니다.</dt>
				<dd class="btn-wrap" id="alret_confirm">
					<a href="javascript:void(0);" title="확인버튼" class="ok">확인</a>
				</dd>
			</dl>
		</div>
		<!-- 00.alret -->
		
		<!-- 00.채팅창:bottom -->
		
		<!--// 00.채팅창:별점 -->
		<%-- <c:if test="${ siteSetting.cns_evl_use_yn eq 'Y' }">
		<div id="chat_bottom_end" style="display: none;">
			<div class="star_area">
				<a href="javascript:void(0);" tabindex="0" class="icon_star active" title="별점 1점">1</a>
				<a href="javascript:void(0);" tabindex="0" class="icon_star active" title="별점 2점">2</a>
				<a href="javascript:void(0);" tabindex="0" class="icon_star active" title="별점 3점">3</a>
				<a href="javascript:void(0);" tabindex="0" class="icon_star active" title="별점 4점">4</a>
				<a href="javascript:void(0);" tabindex="0" class="icon_star active" title="별점 5점">5</a>
				<div class="star_text">${fn:replace(siteSetting.cns_evl_guide_msg, newLineChar, '<br/>')}</div>
			</div>
			<textarea tabindex="" class="form_star_send" placeholder="한줄평가도 부탁해요 (100자 이내)" maxlength="100" title="한줄평가도 부탁해요 (100자 이내)"></textarea>
			<div class="btn_send_area">
				
			</div>
		</div>
		</c:if> --%>
		<!--// 00.채팅창:별점 -->
	</div>
	<!-- layer:용어검색 -->
	<!-- <div class="poopup_chat_detail search-word-area poopup_chat_term" style="display: none;">
		<div class="inner">
			<div class="search-top">
				<h1 class="tit-h1">알기 쉬운 금융 사전</h1><a href="javascript:void(0);" title="창닫기" class="btn-close btn_close_detail">창닫기</a>
			</div>
			<div class="search-line on"> 활성화 클래스 on
				<div class="popup_inner">
					<input type="text" placeholder="검색할 키워드를 입력하세요." id="" value="" class="input term_search" name="schText" maxlength="10">
					<span class="btn-close btn_search_term_close" style="display:none;">닫기</span>
					<span class="btn-search btn_search_term">검색</span>
				</div>
			</div>
			자동검색
			<ul id="termAutoCmp" class="relation-wrap" style="position:absolute;background-color: white; overflow: auto;width: 90%;height: 83%;"></ul>
			
			<div class="result-wrap" style="display:none;  height: 100%;">
				<dl class="result-inner" style="height: 80%; overflow-y: auto;">
					<dt class="count-number">검색결과( <span class="term_cnt"></span> 건)</dt>
					<dd class="info-list" style=""></dd>
				</dl>
			</div>
			
			no data
			<div class="no-data-search" style="display:none;">
				<div class="popup_inner">
					<img src="../images/icon/bg_no_s_data.png" alt="아이콘" class="icon" />
					<p class="text">검색결과가 없습니다.</p>
				</div>
			</div>
		</div>
	</div> -->
	<!--// layer:용어검색 -->
	
	<!--// layer:검색 버튼 레이어 -->
	<!-- <div class="chatting popup_chat_search" style="display: none;">
		<div style="margin-buttom:50px;">	
			<input type="text" class="chat_search">
			<button type="button" class="btn_search_exid">닫기</button>
			<button type="button" class="btn_search_prev">이전</button> 
			<button type="button" class="btn_search_next">다음</button>
		</div>
	</div> -->
	<!--// layer:검색 버튼 레이어 -->
	<!-- <a href="javascript:void(0);" id="moveBtn02">위로 이동</a> -->
	<!-- <a href="javascript:void(0);" id="moveBtn">아래로 이동</a> -->
	<!-- <a href="javascript:void(0);" class="to-the-up">위로 이동</a> HappyBot/Block/1345 -->
	<a href="javascript:void(0);" class="to-the-down">아래로 이동</a>
<c:if test="${!isProduction}">
<form id="fileForm" style="display: none;">
	<input type="file" name="file" class="hidden" />
</form>
</c:if>
<form id="chatRoom" style="display: none;">
	<input type="hidden" name="id" value="<c:out value="${ chatRoom.chatRoomUid }" />" />
</form>
<form id="siteSetting" style="display: none;">
	<input type="hidden" name="workYn" value="<c:out value="${ siteSetting.work_yn }" />" />
	<input type="hidden" name="startTime" value="<c:out value="${ siteSetting.start_time }" />" />
	<input type="hidden" name="endTime" value="<c:out value="${ siteSetting.end_time }" />" />
	<input type="hidden" name="unsocialAcceptYn" value="<c:out value="${ siteSetting.unsocial_accept_yn }" />" />
	<input type="hidden" name="cns_evl_guide_msg" value="<c:out value="${ siteSetting.cns_evl_guide_msg }" />" />
	<input type="hidden" name="cns_evl_use_yn" value="<c:out value="${ siteSetting.cns_evl_use_yn }" />" />
</form>
<form id="user" style="display: none;">
	<input type="hidden" name="id" value="<c:out value="${ user.cstm_uid }" />" />
	<input type="hidden" name="nickName" value="<c:out value="${ user.name }" />" />
	<input type="hidden" name="userType" value="U" />
	<input type="hidden" name="rollType" value="U" />
	<input type="hidden" name="cstmLinkDivCd" value="${ user.cstm_link_div_cd }" />
</form>
<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
<script src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
<script src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
<script src="<c:url value="/js/sockjs.min.js" />"></script>
<script src="<c:url value="/js/stomp.min.js" />"></script><%--
<script src="<c:url value="/js/jquery.cookie.js" />"></script>--%>
<%--<script src="<c:url value="/js/clipboard.min.js" />"></script>--%>
<script src="<c:url value="/js/ifvisible.min.js" />"></script>
<script src="<c:url value="/js/swiper.js" />"></script>
<script src="<c:url value="/js/autosize.min.js" />"></script><%--
<script src="<c:url value="/js/dayjs.min.js" />"></script>--%>

<script src="<c:url value="/js/jquery-1.11.1.min.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/jquery.placeholder.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>

<script src="<c:url value="/js/common.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/chatRoom.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/client.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/customer.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script>
$(document).ready(function() {

	<c:if test="${useFrame == 'Y'}">
	document.domain = "tchat.samsungpop.com";
	</c:if>

	// 채팅방 목록 이동
	$('.btn_back').on('click', function() {
		var appcode = $("#appCode").val();
		var data = {"comkey":"2000","data":{"type":"happytalk","appCode":"${appCode}"}};
		data = JSON.stringify(data);
		
		var mobile = navigator.userAgent;
		
		if( /iPhone|iPad|iPod|iOS/i.test(mobile) ){
			var dic = {'handlerInterface':'mba','function':'sendToApp','parameters':data};
			//alert("ios --> \n"+JSON.stringify(dic));
			console.log("ios");
			window.webkit.messageHandlers['mba'].postMessage(dic);
		}else if( /Android/i.test( mobile )){
			//alert("android --> \n"+JSON.stringify(data));
			console.log("android");
			window['mba']['sendToApp']( data );
		}else{
			console.log("pc");
			//alert("pc");
		}
		
	});

	var varUA = navigator.userAgent.toLowerCase();
	/*mobile*/
	if(varUA.match("mobile") != null){
		$(".btn_back").show();
	}else{/*pc*/
		$(".btn_back").hide();
	}
	// TODO: DELETEME, 임시오픈시만 사용
	// 챗봇 답변 평가 버튼
	$(document).on('click', '#chat_body .evl_bot span', function(e) {
		
		e.preventDefault();
		e.stopPropagation();
		var obj = $(this);

		var chatNum = $(this).closest('.message_box').data('message-id');
		var evl = $(this).data('evl');
		console.debug(chatNum, evl);

		if (chatNum && evl) {
			$.ajax({
				url: HT_APP_PATH + '/api/chat/evl/' + chatNum,
				data: {
					evl: evl,
				},
				method: 'post',
			}).done(function(data) {
				console.debug(data);
				$('span', obj.closest('.evl_bot')).css('background' ,'#4a9fc1');
				obj.css('background', '#66af6c');
			}).fail(function(jqXHR, textStatus, errorThrown) {
		    	console.error('FAIL REQUEST: EVALUATE BOT RESPONSE: ', textStatus);
		    });
		}
	});
});




////////////////////////mPOP //////////////////////////////
function receiveFromApp(resHashTable){
	var userStatus = null;
	if(resHashTable["RETVALUE"] == "2000"){ //// onpause
		htClient.sendActive(USER_ACTIVE_STATUS.IDLE);
		userStatus = USER_ACTIVE_STATUS.IDLE;
	}
	else {
		htClient.sendActive(USER_ACTIVE_STATUS.ACTIVE);
		userStatus = USER_ACTIVE_STATUS.ACTIVE;
	}
	<c:if test="${isDevelope}">
	alert("[[mPOP]] RETVALUE : " + resHashTable["RETVALUE"] +" >>> send status : " + userStatus);
	</c:if>
}
</script>
<c:if test="${!isProduction}">
<form id="debug" style="display: none;">
	<input type="hidden" name="profile" value="false" />
</form>
</c:if>
<form id="data" style="display: none;">
	<input type="hidden" name="happyBotBuilderUrl" value="${happyBotBuilderUrl}" />
	<input type="hidden" id="appCode"name="appCode" value="${appCode}" />
</form>
</body>
</html>
