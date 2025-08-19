<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<script>
$(document).on('click', '.dev_tabMenuSub', function() {

	var clickTabMenu = $(this).attr('id');
	var url;
	var data = {};

	// 채팅방 목록 클릭 시 $('#dev_tabMenu1').trigger('click');
	$(this).siblings('li').removeClass('active');
	$(this).addClass('active');

	if (clickTabMenu == 'dev_tabMenuSub1') {
		$('.dev_tab_sub_contents01').show();
		$('.dev_tab_sub_contents02').hide();
	} else if (clickTabMenu == 'dev_tabMenuSub2') {
		$('.dev_tab_sub_contents02').show();
		$('.dev_tab_sub_contents01').hide();
	}
});

//검색창 엔터
$(document).on('keyup', '#autoCmpForm [name=schContent]', function(e) {

	e.preventDefault();
	e.stopPropagation();

	if (e.keyCode === 13) {
		fn_goSearch();
	}
});

function fn_goSearch() {

	console.warn('SEARCH');
	$('#autoCmpId').val('');
	var html = '';

	$.ajax({
		url: '<c:url value="/autoCmp/selectAutoCmpListAjax" />',
		data: $('#autoCmpForm').serialize(),
		type: 'post'
	}).done(function(result) {
		if (result.length > 0) {
			for (var i = 0; i < result.length; i++) {
				html += '<li class="tit">';
				html += '<a href="javascript:void(0);" class="dev_dpl active" data-id="' + result[i].auto_cmp_id + '">';
				html += '<div>';
				html += '	<div class="text_box">';
				html += result[i].content.replace(/\n/g, '<br/>');
				html += '	</div>';
				html += '</div>';
				html += '<button type="button" class="'+(result[i].auto_cmp_cus == "P"? "btn_person":"btn_common")+'">'+result[i].auto_cmp_cus_nm+'</button>';
				if (result[i].member_uid == ${sessionVo.memberUid}) {
					html += '<button type="button" class="btn_del_temp2" onclick="javascript:fn_delete('
						+ result[i].auto_cmp_id + ');"><i>삭제</i></button>';
					html += '<button type="button" class="btn_write_temp2" onclick="javascript:fn_edit('
						+ result[i].auto_cmp_id + ');"><i>쓰기</i></button>';
				}
				html += '</a>';
				html += '</li>';
				//console.log(html);
				$('#autoCmpListContent').html('');
				$('#autoCmpListContent').append(html);
			}
		} else {
			html += '<p class="no_data">목록이 없습니다.</p>';
			$('#autoCmpListContent').html('');
			$('#autoCmpListContent').append(html);
		}
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL SEARCH: AUTO COMPLETE LIST: ', textStatus);
	});
}

function fn_edit(autoCmpId) {

	if (autoCmpId > 0) {
		var $target = $('#autoCmpList li a[data-id=' + autoCmpId + ']');
		if ($target.length === 1) {
			$('#autoCmpId').val(autoCmpId);

			var $content = $('.text_box', $target).html().trim();
			console.debug('CONTENT: ', $content);
			var content = $content.replace(/<br\s*[\/]?>/gi ,'');
			console.debug('CONTENT: ', content);
			$("#content").val(content);
		} else {
			console.error('NOT FOUND AUTO COMPLETE MESSAGE', autoCmpId);
		}
	} else {
		$('#autoCmpId').val('');
		$('#content').val('');
	}

	$('#autoCmpList').hide();
	$('#autoCmpWrite').show();	
	$('#autoCmpSearch').hide();
	$('#autoCmpInfo').hide();
}

function fn_cancel() {

	$('#autoCmpList').show();
	$('#autoCmpWrite').hide();	
	$('#autoCmpSearch').show();
	$('#autoCmpInfo').show();
	$('#content').val('');
}

function fn_save() {

	if ($('#content').val() === '') {
		alert('자동완성 메세지를 입력해 주세요');
		return false;
	}
	if( ($('#content').val().length + ($('#content').val().split('\n').length * 2)) > 1000){
		alert('자동완성 메세지는 1000자 이하(공백 1자, 한글 2자, 줄바꿈 2자)만 저장 가능합니다. 현재 : ' + ($('#content').val().length + ($('#content').val().split('\n').length * 2)) + "자");
		return false;
	}
	$.ajax({
		url: '<c:url value="/autoCmp/saveAutoCmp" />',
		data: $('#autoCmpForm').serialize(),
		type: 'post'
	}).done(function(result) {
		fn_layerMessage(result.returnMessage);
		if (result.returnCode === 'SUCCEED') {
			$('#autoCmpId').val('');
			$('#autoCmpList').show();
			$('#autoCmpWrite').hide();	
			$('#autoCmpSearch').show();
			$('#autoCmpInfo').show();
			$('#content').val('');
			fn_goSearch();
			console.debug(autoCmpList);
		}
	});
}

function fn_delete(autoCmpId) {

	if (!confirm('등록된 자동완성 메세지를 삭제하시겠습니까?')) {
		return false;
	}

	$.ajax({
		url: '<c:url value="/autoCmp/deleteAutoCmp" />',
		data: {
			autoCmpId: autoCmpId
		},
		type: 'post'
	}).done(function(result) {
		fn_layerMessage(result.rtnMsg);
		setTimeout(function(){
			fn_goSearch();
		}, 500);

	});
}
</script>
<c:choose>
	<c:when test="${empty param.chatRoomUid}">
	<div style="padding: 10px;">
	상담을 시작하시면, 정보가 표기됩니다.
	</div>
	</c:when>
	<c:otherwise>
		<c:set var="cstmUid" value="" />
		<c:set var="listCount" value="0" />
		<c:if test="${not empty chatHisList && fn:length(chatHisList) > 0}">
			<c:set var="cstmUid" value="${chatHisList[0].cstm_uid}" />
			<c:set var="listCount" value="${fn:length(chatHisList)}" />
		</c:if>
			<!-- tab01_01: 상담정보 -->
		<div class="tmenu_top_cont">
			<!-- tabs_s02:상담이력 -->
			<div class="tabs_s_content" style="height: 280px;">
				<div class="wScroll">
					<p class="counsel_title">
					<c:set var="cstmName" value="익명" />
					<c:if test="${not empty customer.room_coc_nm}">
						<c:set var="cstmName" value="${customer.room_coc_nm}" />
					</c:if>
						<em>${cstmName}<c:if test="${not empty customer.room_coc_id}"> (${customer.room_coc_id})</c:if></em>
						님의 상담 이력 조회(총 ${listCount}건)</p>
					<table class="tCont service dev_cnsHisTable">
						<caption>상담정보 이력 테이블입니다.</caption>
						<colgroup>
							<col style="">
							<col style="">
							<col style="">
							<col style="">
							<col style="">
						</colgroup>
						<thead>
							<tr>
								<th>시작시간</th>
								<th>채널</th>
								<th>분류</th>
								<th>상담직원</th>
								<th>상태</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="data" items="${chatHisList}" varStatus="status1">
								<tr data-chatRoomUid="${data.chat_room_uid}">
									<td>${data.room_create_dt}</td>
									<td>${data.cstm_link_div_nm}</td>
									<td>${data.ctg_nm_1}
									<c:if test="${not empty data.ctg_nm_2}">
									 > ${data.ctg_nm_2}
									</c:if> 
									<c:if test="${not empty data.ctg_nm_3}">
									 > ${data.ctg_nm_3}
									</c:if> 
									</td>
									<td>${data.name}</td>
									<td>${data.chat_room_status_nm}</td>
								</tr>
							</c:forEach>
					</table>
				</div>
			</div>
			<!--// tabs_s02:상담이력 -->
		</div>
		<!--// tab01_01: 상담정보 -->

		<!-- tab01_02: 상담템플릿 관리 -->
		<ul class="tabs_menu02">
			<li class="dev_tabMenuSub active" id="dev_tabMenuSub1" rel="tmenu011">상담템플릿</li>
			<li class="dev_tabMenuSub" id="dev_tabMenuSub2">자동완성</li>
			<!-- <li rel="tmenu012">자동 시나리오</li> -->
		</ul>
		<!-- tab01_02_01:상담템플릿 -->
		<div class="tabs_menu_content02 dev_tab_sub_contents01" id="tmenu011" >
		<form id="tplForm" name="tplForm">
			<input type="hidden" name="schTplCtgNum" value="${schTplCtgNum}" />
			<input type="hidden" name="schDeviceType" value="${schDeviceType}" />
			<input type="hidden" name="templateEditYn" value="N" />
			<input type="hidden" name="tplNum" value="" />
		<div class="s_content_head tmenu02">
			<ul class="tabs_menu_s03 dev_ctgList">
				<li class="dev_category_li" data-tplCtgNum="" <c:if test="${data.tpl_ctg_num eq '' }">class="active"</c:if> >전체</li>
				<c:forEach var="data" items="${categoryList}" varStatus="status8">
					<li class="dev_category_li <c:if test="${data.tpl_ctg_num eq schTplCtgNum}">active</c:if> " data-tplCtgNum="${data.tpl_ctg_num}" >
						${data.tpl_ctg_nm}
					</li>
				</c:forEach>
			</ul>
		</div><%--
				<ul class="chatbot_smenu dev_deviceTypeList">
					<li><a href="javascript:void(0);" data-schDeviceType="ANDROID" <c:if test="${schDeviceType eq 'ANDROID' }">class="active"</c:if> >안드로이드</a></li>
					<li><a href="javascript:void(0);" data-schDeviceType="IPHONE" <c:if test="${schDeviceType eq 'IPHONE' }">class="active"</c:if> >아이폰</a></li>
					<li><a href="javascript:void(0);" data-schDeviceType="WEB" <c:if test="${schDeviceType eq 'WEB' }">class="active"</c:if> >웹</a></li>
				</ul>--%>
			<div class="icon_info_area" style="width:100%;">
				<span class=left>* 템플릿을 카테고리 별로 확인해서 사용하세요.</span><%--
				<div class="right">
					<i class="icon_individual_s">개인템플릿</i>
					<i class="icon_group_s">상담직원 공유 템플릿</i>
				</div>--%>
			</div>

			<div class="search_area">
				<select name="schType">
					<option value="ALL">전체</option>
					<option value="KEYWORD">키워드</option>
					<option value="ANSWER">답변</option>
					<option value="QUESTION">질문</option>
				</select>
				<div class="search_area_right">
					<input type="text" name="schText" />
					<button type="button" class="btn_search dev_search_btn">검색하기</button>
					<input type="text" name="fake" style="display: none;" />
				</div>
			</div>
			<div class="template_list_area dev_templateList">
				<c:choose>
					<c:when test="${empty templateList}">
						<p class="no_data">목록이 없습니다.</p>
					</c:when>
					<c:otherwise>
						<ul class="template_list layer_template_list" >
							<c:forEach var="template" items="${templateList}" varStatus="status3">
								<%-- 고객질문 영역: 텍스트만 가능 --%>
								<li class="tit">
									<a href="javascript:void(0);" class="dev_dplListClick" data-tplNum="${template.tpl_num }"><%--
										<c:choose>
											<c:when test="${template.tpl_div_cd eq 'P' }">
												<i class="icon_individual">개인템플릿</i>
											</c:when>
											<c:when test="${template.tpl_div_cd eq 'G' }">
												<i class="icon_group">상담직원 공유 템플릿</i>
											</c:when>
										</c:choose>--%>
										<c:choose>
											<c:when test="${template.tpl_msg_div_cd eq 'TEXT' }">
												<i class="icon_T">T</i>
											</c:when>
											<c:when test="${template.tpl_msg_div_cd eq 'NORMAL' }">
												<i class="icon_N">N</i>
											</c:when>
										</c:choose>
										<div>
											<div class="text_box">
												${fn:replace(template.cstm_que, newLineChar, '<br/>')}
											</div>
										</div>
									</a>
								</li>
								<%-- 상담직원 답변 영역--%>
								<li class="text" data-tplNum="${template.tpl_num}">
									<div class="right-chat-wrap">
										<div class="bubble-wrap">
											<article class="inner-chat">
												<div class="bubble-templet type-2">
													<chat:template jsonContents="${template.reply_cont}" />
												</div>
											</article>
										</div>
										<div class="tag_area">
											<c:forEach var="keyword" items="${template.templatekwdlist}">
												<span class="tag">${keyword.kwd_nm}</span>
											</c:forEach>
										</div>
									</div>
								</li>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>
		</form>
		</div>
		<!--// tab01_02_01:상담템플릿 -->

		<!-- tab01_02_02:자동완성 -->
		<div class="tabs_menu_content02 dev_tab_sub_contents02" id="tmenu011" style="display:none;">
		<form id="autoCmpForm" name="autoCmpForm">
			<input type="hidden" name="autoCmpDiv" id="autoCmpDiv" value="C" />
			<input type="hidden" name="autoCmpCus" id="autoCmpCus" value="P" />
			<input type="hidden" name="autoCmpId" id="autoCmpId" value="" />
			<input type="hidden" name="departCd" id="departCd" value="${departCd}" />
			<input type="hidden" name="memberUid" id="memberUid" value="${sessionMemberUid}" />

			<div class="s_content_head">
			</div>

			<div class="icon_info_area" id="autoCmpInfo" style="display:block;">
				<span class="left">공용 100개, 개인 100개까지 등록할 수 있습니다.</span>
				<span class="right"><button type="button" class="btn_go_save"
					onclick="javascript:fn_edit();">등록</button>
				</span>
			</div>
			<div class="search_area" id="autoCmpSearch" style="display:block;">
				<select name="schAutoCmpCus" id="schAutoCmpCus">
					<option value="">전체</option>
					<option value="C">공용</option>
					<option value="P">개인</option>
				</select>
				<div class="search_area_right">
					<input type="text" name="schText" id="schContent" />
					<button type="button" class="btn_search" onclick="javascript:fn_goSearch()">검색하기</button>
					<input type="text" name="fake" style="display: none;" />
				</div>
			</div>
			<div class="template_list_area" id="autoCmpList" style="display:block;">
				<c:choose>
					<c:when test="${empty autoCmpList}">
						<p class="no_data">목록이 없습니다.</p>
					</c:when>
					<c:otherwise>
						<ul class="template_list template_list2 layer_template_list" id="autoCmpListContent">
							<c:forEach var="autoCmp" items="${autoCmpList}" varStatus="status3">
								<%-- 고객질문 영역: 텍스트만 가능 --%>
								<li class="tit">
									<a href="javascript:void(0);" class="dev_dpl active" data-id="${autoCmp.auto_cmp_id}">
										<div>
											<div class="text_box">
												${fn:replace(autoCmp.content, newLineChar, '<br/>')}
											</div>
										</div>
										<button type="button" 
											<c:choose>
												<c:when test="${autoCmp.auto_cmp_cus eq 'P' }">class="btn_person"</c:when>
												<c:otherwise>class="btn_common"</c:otherwise>
											</c:choose>
										>${autoCmp.auto_cmp_cus_nm }</button>
										<c:if test="${autoCmp.member_uid eq sessionVo.memberUid}">
										<button type="button" class="btn_del_temp2"
											onclick="javascript:fn_delete(${autoCmp.auto_cmp_id});"><i>삭제</i></button>
										<button type="button" class="btn_write_temp2"
											onclick="javascript:fn_edit(${autoCmp.auto_cmp_id});"><i>쓰기</i></button>
										</c:if>
									</a>
								</li>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
			</div >
			<div id="autoCmpWrite" style="display:none;">
				<textarea id="content" name="content" placeholder="자동완성 메세지를 등록할 수 있습니다." class="form_text" maxlength="1000"></textarea>
				<div class="btn_tmenu_area">
					<button type="button" class="btn_go_save" onclick="javascript:fn_save()">저장</button>
					<button type="button" class="btn_go_cancel" onclick="javascript:fn_cancel()">취소</button>
				</div>
			</div>
		</form>
		</div>
		<!--// tab01_02_02:자동완성 -->
	</c:otherwise>
</c:choose>
