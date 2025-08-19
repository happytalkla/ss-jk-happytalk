<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<script src="/happytalk/js/clipboard.min.js"></script>

<% pageContext.setAttribute("newLineChar", "\n"); %>

<div class="inner" style="width:700px;">
		<div class="popup_head">
			<h1 class="popup_tit">상담정보</h1>
			<div id="modify_close">
			<c:if test="${modifyChk != 'W'}">
			<button type="button" class="btn_popup_close">창닫기</button>
			</c:if>
			</div>
		</div>
		<div class="popup_body" style="height:400px;">
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
										<div>● 고객 유형 정보</div>
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
										</table>
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
	</div>
					
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

	$('#gridLon tbody tr').click(function() {

		var tr = $(this);
		var td = tr.children();

		$('#lonInfo tr:eq(0) td:eq(0)').html(td.eq(0).text());  // 대출번호
		$('#lonInfo tr:eq(0) td:eq(1)').html(td.eq(1).text());  // 상품명
		$('#lonInfo tr:eq(1) td:eq(0)').html(td.eq(2).text());  // 상환방법
		$('#lonInfo tr:eq(1) td:eq(1)').html(td.eq(3).text());  // 금리
		$('#lonInfo tr:eq(2) td:eq(0)').html(td.eq(4).text());  // 약정일
		$('#lonInfo tr:eq(2) td:eq(1)').html(td.eq(5).text());  // 대출일자
		$('#lonInfo tr:eq(3) td:eq(0)').html(td.eq(6).text());  // 대출조정금액
		$('#lonInfo tr:eq(3) td:eq(1)').html(td.eq(7).text());  // 만기일자
		$('#lonInfo tr:eq(4) td:eq(0)').html(td.eq(8).text());  // 계약기간
		$('#lonInfo tr:eq(4) td:eq(1)').html(td.eq(9).text());  // 최종납입회차
		$('#lonInfo tr:eq(5) td:eq(0)').html(td.eq(10).text()); // 출금은행명
		$('#lonInfo tr:eq(5) td:eq(1)').html(td.eq(11).text()); // 자동이체계좌
		$('#lonInfo tr:eq(6) td:eq(0)').html(td.eq(12).text()); // 계약서수령여부
		$('#lonInfo tr:eq(6) td:eq(1)').html(td.eq(13).text()); // 마케팅동의여부
		$('#lonInfo tr:eq(7) td:eq(0)').html(td.eq(14).text()); // 관리지점
		$('#lonInfo tr:eq(7) td:eq(1)').html(td.eq(15).text()); // 담당자
		//// 입금정보
		$('#deposit tr:eq(0) td:eq(0)').html(td.eq(16).text());  // 대출조정잔액
		$('#deposit tr:eq(0) td:eq(1)').html(td.eq(17).text());  // 원리금 합계
		$('#deposit tr:eq(1) td:eq(0)').html(td.eq(18).text());  // 총발생이자(청구)	
		$('#deposit tr:eq(1) td:eq(1)').html(td.eq(19).text());  // 총발생이자(완납)	
		$('#deposit tr:eq(2) td:eq(0)').html(td.eq(20).text());  // 회차상환원금
		$('#deposit tr:eq(2) td:eq(1)').html(td.eq(21).text());  // 회차상환원리금
		$('#deposit tr:eq(3) td:eq(0)').html(td.eq(22).text());  // 부족금
		$('#deposit tr:eq(3) td:eq(1)').html(td.eq(23).text());  // 차기입금일
		$('#deposit tr:eq(4) td:eq(0)').html(td.eq(24).text());  // 연체일수
		$('#deposit tr:eq(4) td:eq(1)').html('');  // null
		$('#deposit tr:eq(5) td:eq(0)').html(td.eq(26).text()); // 은행명
		$('#deposit tr:eq(5) td:eq(1)').html(td.eq(27).text()); // 가상계좌
		$('#deposit tr:eq(6) td:eq(0)').html(td.eq(28).text()); // 중도상환수수료
		$('#deposit tr:eq(6) td:eq(0)').html(td.eq(29).text()); // 만기경과일수
		
	});
	
});
</script>						
