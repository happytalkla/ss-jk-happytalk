<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<script src="/happytalk/js/clipboard.min.js"></script>

<% pageContext.setAttribute("newLineChar", "\n"); %>
						<div class="tmenu_top_cont">
							<div class="tabs_s_content" style="height:320px;">
								<div class="wScroll">
									<form id="customerInfoForm" name="customerInfoForm">
									<input type="hidden" name="chatRoomUid" value="${chatRoomUid}" />
									<div class="s_content_head tmenu02">
										<ul class="tabs_menu_s03 sub_memu_list">
											<li class="sub_memu active" id="1">고객정보</li>
											<li class="sub_memu" id="2">계좌정보</li>
											<!-- <li class="sub_memu" id="3">입금</li> -->
											<li class="sub_memu" id="4">접촉정보</li>
											<!-- <li class="sub_memu" id="5" style="color:green;" >정보갱신</li> -->
										</ul>
									</div>
									<!-- tabs_s02:고객정보 -->
									<div id="customerInfo">
									<div style="inline-size: fit-content;">● 고객 기본 정보</div>
										<table class="tCont service" style="margin: 0 5px 10px 5px">
											<colgroup>
												<col width="15%">
												<col width="30%">
												<col width="15%">
												<col width="30%">
												<col>
											</colgroup>
											<tbody>
											<tr>
												<th>고객명(성별)</th>
												<td>${customerSinfo.CLNT_NAME} (${customerSinfo.A_GNDR_SECT_NAME})</td>
												<th>고객ID</th>
												<td><span class="target_customer_id">${customerSinfo.ROOM_ENTITY_ID}</span>
												    <button type="button" class="btn_copy target_customer_id" data-clipboard-target=".target_customer_id">복사</button>
												</td>
											</tr>
											<tr>
												<th>투자정보</th>
												<td style="<c:if test="${custTypeInfo.BG_COLOR eq 'Y'}">background-color: #ffb28e</c:if>">${custTypeInfo.CLNT_POPN_CTNT}</td>
												<th>고객등급</th>
												<td>${custTypeInfo.CLNT_CLSN_NAME}</td>
											</tr>
											<tr>
												<th>투자성향</th>
												<td colspan="3">${custTypeInfo.TRDG_CLNT_POPN_CTNT}</td>
											</tr>
											<tr>
												<th>연령(생년월일)</th>
												<td>${customerSinfo.AGE}<br /> (${customerSinfo.BRDT})</td>
												<th>연락처</th>
												<td>${customerSinfo.MOBL_PHON_NO}</td>
											</tr>
											<tr>
												<th>자택주소</th>
												<td colspan="3">${customerSinfo.HOME_ADRS}</td>
											</tr>
											<tr>
												<th>직장주소</th>
												<td colspan="3">${customerSinfo.SITE_ADRS}</td>
											</tr>
											<tr>
												<th>이메일</th>
												<td colspan="3">${customerSinfo.EMAL_ADRS}</td>
											</tr>
											</tbody>
										</table>
										<%-- <div>● 고객 유형 정보</div>
										<table class="tCont service" style="margin: 0 5px 10px 5px">
											<colgroup>
												<col width="15%">
												<col width="30%">
												<col width="15%">
												<col width="30%">
												<col>
											</colgroup>
											<tbody>
											<tr>
												<th>투자정보</th>
												<td style="<c:if test="${custTypeInfo.BG_COLOR eq 'Y'}">background-color: #ffb28e</c:if>">${custTypeInfo.CLNT_POPN_CTNT}</td>
												<th>고객등급</th>
												<td>${custTypeInfo.CLNT_CLSN_NAME}</td>
											</tr>
											<tr>
												<th>투자성향</th>
												<td colspan="3">${custTypeInfo.TRDG_CLNT_POPN_CTNT}</td>
											</tr>
											</tbody>
										</table> --%>
										<div>● 고객 약정 정보</div>
										<table class="tCont service" style="margin: 0 5px 10px 5px">
											<colgroup>
												<col width="15%">
												<col width="30%">
												<col width="15%">
												<col width="30%">
												<col>
											</colgroup>
											<tbody>
											<tr>
												<th>온라인서비스</th>
												<td>${custContInfo.A_HTS_CTRT_YN }</td>
												<th></th>
												<td></td>
											</tr>
											<tr>
												<th rowspan="2">이체한도</th>
												<td>1일 : <fmt:formatNumber  pattern="#,###.#"  value="${custContInfo.A_BANK_TNFR_LMAM}"/></td>
												<th>보안카드</th>
												<td>${custContInfo.A_SCRY_CRD_USE_YN }</td>
											</tr>
											<tr>
												<td>1회 : <fmt:formatNumber  pattern="#,###.#"  value="${custContInfo.A_TMS_BNTR_LMAM}"/></td>											
												<th>OTP</th>
												<td>${custContInfo.A_OTP_USE_YN }</td>
											</tr>
											<tr>
												<th>증권담보대출</th>
												<td>${custContInfo.A_DPSD_MRTG_LN_CTRT_YN }</td>
												<th>대차</th>
												<td>${custContInfo.A_SLB_CTRT_YN }</td>
											</tr>
											</tbody>
										</table>
									</div>
									<div id="customerInfo1" class="area">		
										<div style="inline-size: fit-content;">● 계좌정보 (${custAccntCnt })</div>
										<c:forEach var="custAccntInfo" items="${custAccntList}" varStatus="status">
											<table class="tCont service" style="margin: 0 5px 10px 5px" id="lonInfo">
											    <colgroup>
													<col style="width: 25%">
													<col style="width: 25%">
													<col style="width: 25%">
													<col style="width: 25%">
												</colgroup>
												<tbody>
												<pre>   계좌 ${status.index + 1 }</pre>
												<tr>
													<th>계좌번호</th>
													<td colspan="3"><span class="target_acnt_no">${custAccntInfo.ACNT_NO}</span>
												    <button type="button" class="btn_copy_acnt_no" data-clipboard-target=".target_acnt_no">복사</button>
												</td>												    
												</tr>
												<tr>
													<th>계좌유형</th>
													<td>${custAccntInfo.ACNT_NAME }</td>
													<th>계좌상태</th>
													<td>${custAccntInfo.A_ACNT_STAS_NAME }</td>
												</tr>
												<tr>
													<td colspan="4" style="background: #f3f3f5;">상세정보</td>
												</tr>
												<tr>
													<th>관리점</th>
													<td>${custAccntInfo.ACNT_MNGG_BRNH_ENTY_NAME }</td>
													<th>실적점</th>
													<td>${custAccntInfo.PRFT_CNTR_ENTY_NAME }</td>
												</tr>
												<tr>
													<th>관리자</th>
													<td>${custAccntInfo.A_MNGR_EMPY_NAME }</td>
													<th>계좌개설일</th>
													<td>${custAccntInfo.ACNT_OPNG_DATE }</td>
												</tr>
												<tr>
													<th>반송</th>
													<td>
														<c:choose>
															<c:when test="${custAccntInfo.SNBC_YN eq 'R' }">O</c:when>
															<c:otherwise>X</c:otherwise>
														</c:choose>													
													</td>
													<th>E-MAIL 반송</th>
													<td>
														<c:choose>
															<c:when test="${custAccntInfo.EMAL_SNBC_YN eq 'R' }">O</c:when>
															<c:otherwise>X</c:otherwise>
														</c:choose>															
													</td>
												</tr>
												<tr>
													<th>신용 약정</th>
													<td>
														<c:choose>
															<c:when test="${custAccntInfo.A_CRDT_CTRT_RGSN_DATE eq '' }">X</c:when>
															<c:otherwise>O</c:otherwise>
														</c:choose>															
													</td>
													<th></th>
													<td></td>													
												</tr>
												<tr>
													<th>증거금 100%</th>
													<!-- 출력값 100% 징수인 경우만 Y -->
													<td>
														<c:choose>
															<c:when test="${custAccntInfo.A_MRGN_CLTN_SECT_CODE_NAME eq '100%징수' }">O</c:when>
															<c:otherwise>X</c:otherwise>
														</c:choose>
													</td>
													<th>사고 등록 여부</th>
													<td>
														<c:choose>
															<c:when test="${custAccntInfo.ACCT_YN eq 'Y' }">O</c:when>
															<c:otherwise>X</c:otherwise>
														</c:choose>													
													</td>
												</tr>
												<tr>
													<th>근거계좌</th>
													<td>${custAccntInfo.A_ORTR_ACNT_NO }</td>
													<!-- <th>근거계좌</th>
													<td>${custAccntInfo.A_ORTR_ACNT_NO }</td> -->
													<th>가상계좌</th>
													<td>${custAccntInfo.A_VRAC_NO }</td>
												</tr>
												<tr>
													<th>개설 구분</th>
													<td>${custAccntInfo.A_OPNG_SECT_NAME }</td>
													<th>협의 수수료</th>
													<td>${custAccntInfo.A_DSCS_FEE_SECT_CODE_NAME }</td>
												</tr>
												<c:if test="${custAccntInfo.ACNT_NAME eq '연금저축' }">
												<tr>
													<th>계약일자</th>
													<td>${custAccntInfo.CTRC_DATE }</td>
													<th>만기일자</th>
													<td>${custAccntInfo.A_PRDT_MTRY_DATE }</td>
												</tr>
												<tr>
													<th>계약금액</th>
													<td>${custAccntInfo.CTRC_AMNT }</td>
													<th>저축기간</th>
													<td>미확인</td>
												</tr>
												</c:if>
												</tbody>
											</table>
										</c:forEach>
									</div>

									<div id="customerInfo3" class="area">
										<table class="tCont service" style="margin: 0 5px 10px 5px">
											<caption>접촉정보</caption>
											<colgroup>
													<col>
													<col>
													<col>
													<col>
													<col>
												</colgroup>
											<thead>
												<tr>
													<th>접촉일자</th>
													<th>접촉</th>
													<th>접촉구분</th>
													<th>처리자</th>
													<th>상담내용</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="data" items="${callHistList}" varStatus="status">
													<tr>
														<td>${data.H_otch_date}</td>
														<td>${data.H_oclnt_name}</td>
														<td>${data.H_oa_sect_code}</td>
														<td>${data.H_oprce_empy_name}</td>
														<td>${data.H_oprce_acti_ctnt}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<!--// tabs_s02:고객정보 -->
							</form>
						</div>
						
		<!-- tab01_02: 상담템플릿 관리 -->
		<ul class="tabs_menu02" style="margin-top: 50px;">
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
								<li class="text" data-tplNum="${template.tpl_num}" style="display:none;">
									<div class="right-chat-wrap">
										<div class="bubble-wrap ">
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
		
<script>						
$(document).ready(function() {
	$('#customerInfo').show();
	$('#customerInfo1').hide();
	$('#customerInfo2').hide();
	$('#customerInfo3').hide();

	$('.sub_memu').on('click', function(e, trigger) {
		
		var clickSubTabMenu = $(this).attr('id');
		var data = {};
		
		$(this).siblings('li').removeClass('active');
		$(this).addClass('active');
		
		if(clickSubTabMenu == "1") // 고객정보
		{
			$('#customerInfo').show();
			$('#customerInfo1').hide();
			$('#customerInfo2').hide();
			$('#customerInfo3').hide();
		}
		else if(clickSubTabMenu == "2") // 계약정보
		{
			$('#customerInfo').hide();
			$('#customerInfo1').show();
			$('#customerInfo2').hide();
			$('#customerInfo3').hide();
		}
		else if(clickSubTabMenu == "4") // 콜상담내역
		{
			$('#customerInfo').hide();
			$('#customerInfo1').hide();
			$('#customerInfo2').hide();
			$('#customerInfo3').show();
		}
		else if(clickSubTabMenu == "5") // 정보갱신
		{
			data.chatRoomUid = htClient.getChatRoom().getId();
			data.refresh = 'true';

			$.ajax({
				url: HT_APP_PATH + '/chatCsnr/selectCustomerInfoAjax',
				data: data,
				type: 'post'
			}).done(function (result) {
				$('.dev_tab_contents').html(result);
				
				// 계약정보, 입금정보, 콜상담내역 숨김
				$('#customerInfo1').hide();
				$('#customerInfo2').hide();
				$('#customerInfo3').hide();
			});
			
		}
	});
});

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
