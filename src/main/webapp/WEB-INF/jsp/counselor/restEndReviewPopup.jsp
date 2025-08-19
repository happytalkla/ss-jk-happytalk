<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/include.css"><c:param name="v" value="${staticUpdateParam}" /></c:url>" />
	<style type="text/css">
		.hidden {
			display: none;
		}
		.chat_list_top .list_filter li a i {
			background: none;
		}
		.chat_list_top .list_filter li a.asc i {
			background: url("<c:url value='/images/admin/list_down.png' />") no-repeat right 50% !important;
		}
		.chat_list_top .list_filter li a.desc i {
			background: url("<c:url value='/images/admin/list_up.png' />") no-repeat right 50% !important;
		}
	</style>
	<!--[if lt IE 9]>
	<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
	<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
	<![endif]-->
	<script src="<c:url value="/js/jquery-1.12.2.min.js" />"></script>
	<script src="<c:url value="/js/jquery-ui.1.9.2.min.js" />"></script>
	<script src="<c:url value="/js/swiper.js" />"></script>
	
</head>

<body>
	<div class="inner dev_review_div">
		<div class="popup_head">
			<h1 class="popup_tit">후처리 내역을 입력 하시겠습니까?</h1>
			<div id="modify_close">
			
			<button type="button" class="btn_popup_close">창닫기</button>

			</div>
		</div>
		<input type="hidden" id="chatRoomUid" name="chatRoomUid" value="${chatRoomUid}" />
		<input type="hidden" id="cstmUid" name="cstmUid" value="${cstmUid}" />
		<input type="hidden" name="linkIp" value="${customer.link_ip}" />
		<input type="hidden" name="cstmDivCd" value="${customer.cstm_div_cd}" />
		<input type="hidden" name="loginYn" value="${customer.login_yn}" />
		<input type="hidden" name="cstmDivNm" value="<c:out value="${customer.cstm_div_nm}" />"  />
		<input type="hidden" name="cocId"  value="<c:out value="${customer.coc_id}" />" />
		<input type="hidden" name="name" value="<c:out value="${customer.name}" />"  />				
		<div class="popup_body">
			<table class="tCont setting"> 
				<caption>후처리 등록 폼입니다.</caption>
				<colgroup>
					<col style="width:35%">
					<col>
				</colgroup>
				<tbody>
				<c:if test="${null ne customer.coc_id}"> 
					<tr>
						<td class="textL">
							<span class="form_tit">- 고객 ID : ${customer.coc_id}</span><span class="form_tit" style="line-height:150%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<span class="form_tit">- 고객 이름 : ${customer.name}</span>
						</td>
					</tr>
				</c:if>
					<tr>
						<td class="textL dev_keydown">
						
							<div>선택분류  
							<c:choose>
								<c:when test="${endReviewMode ne 'W'}">
									<select id="endCtg1" data-depth="1" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="" selected>대분류 선택</option>
										<c:forEach var="data" items="${endCtg1}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}">${data.ctg_nm}</option>
										</c:forEach>
									</select>
									<select id="endCtg2" data-depth="2" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">중분류 선택</option>
									</select>
									<select id="endCtg3" data-depth="3" style="width: 25%;height:100%;background: lightgray;" disabled>
										<option data-ctg-num="" data-ctg-nm="">소분류 선택</option>
									</select>
								</c:when>
								<c:otherwise>
									<select id="endCtg1" data-depth="1" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">대분류 선택</option>
										<c:forEach var="data" items="${endCtg1}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}" <c:if test="${fn:indexOf(data.ctg_nm, chatEndInfo.DEP_1_CTG_CD) eq 0 }">selected</c:if> >${data.ctg_nm}</option>
										</c:forEach>
									</select>
									<select id="endCtg2" data-depth="2" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">중분류 선택</option>
										<c:forEach var="data" items="${endCtg2}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}" <c:if test="${fn:indexOf(data.ctg_nm, chatEndInfo.DEP_2_CTG_CD) eq 0 }">selected</c:if> >${data.ctg_nm}</option>
										</c:forEach>				
																
									</select>
									<select id="endCtg3" data-depth="3" style="width: 25%;height:100%;background: lightgray;" disabled>
										<option data-ctg-num="" data-ctg-nm="">소분류 선택</option>
										<c:forEach var="data" items="${endCtg3}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}" <c:if test="${fn:indexOf(data.ctg_nm, chatEndInfo.DEP_3_CTG_CD) eq 0 }">selected</c:if> >${data.ctg_nm}</option>
										</c:forEach>
									</select>								
								</c:otherwise>
							</c:choose>								
							</div>														

						</td>
					</tr>
					<tr>
						<td class="textL" style="height:100px;">
							<c:choose>
							<c:when test="${endReviewMode eq 'V'}">
							<div  style="height:100px;overflow-y:auto;">
								<c:out value="${chatEndInfo.memo}" />
								</div>
							</c:when>
							<c:otherwise>
								<textarea name="memo" placeholder="메모를 작성하세요"
									class="form_text" rows='2' >${chatEndInfo.memo}</textarea>
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>

			<!-- 자동 완성 레이어 -->
			<div id="afterAutoCmp" class="text_box">
			</div>

			<div class="popup_btn_area"> 
			<c:if test="${endReviewMode ne 'V'}">
				<button type="button" class="btn_end_save">저장</button>
			</c:if>				
			
			</div>
		</div>
	</div>
</body>
</html>
	<script>
	var chatRoomUid;
	var autoCmpAfterList;
	var HT_APP_PATH = '/happytalk';
	$(window).on('load', function() {
		$('.dev_review_div .popup_body select[id=endCtg1]').focus();
		// 후처리 자동완성 목록
/*
		$.ajax({
			url: '<c:url value="/autoCmp/selectAutoCmpListAjax" />',
			data: {
				autoCmpDiv: 'A'
			},
			type: 'post'
		}).done(function(result) {
			autoCmpAfterList = result;
		});
*/
		// 후처리 자동완성 키 이벤트
		/*
		$('.popup_body').on('keyup', 'textarea[name=memo]', function(e) {

			e.preventDefault();
			console.debug(e.keyCode);

			if (e.keyCode === 27) { // ESC시 자동완성 레이어 가리기
				if ($('#afterAutoCmp').is(':visible')) {
					$('#afterAutoCmp').empty();
					$('#afterAutoCmp').hide();
				}
			} else if (e.keyCode === 13) { // 엔터시 첫번째 자동완성 컨텐츠 선택
				if ($('#afterAutoCmp').is(':visible')) {
					if ($('#afterAutoCmp .auto_cmp').length > 0) {
						$(this).val($('#afterAutoCmp .auto_cmp:eq(0)').text());
						$('#afterAutoCmp').empty();
						$('#afterAutoCmp').hide();
					}
				}
			} else {
				fn_autoCmp('afterAutoCmp', $(this).val().trim());
			}
		});

*/
/*		
		$(".dev_keydown").on('keypress', 'select', function (e) {
			console.log("event222: " +e.which );

			if(e.which && e.which > 47 && e.which < 58 ){
 					alert("숫자 : " + e.which);
			}

		});
*/

		// '상담내역 후처리' 분류 변경시
		$('.dev_review_div').on('change', '.popup_body select', function(e) {

			var maxDepth = $('.dev_review_div .popup_body select').length + 1;
			console.assert(maxDepth > 0);
			var currentDepth = $(this).data('depth');
			console.log("currentDepth : " + currentDepth);
			console.assert(currentDepth > 0);
			
			var currentCtgNum = $('option:checked', $(this)).data('ctg-num');
			
			if (currentDepth < maxDepth && currentCtgNum !== null && currentCtgNum !== '') {
				var currentCtgNum = $('option:checked', $(this)).data('ctg-num');
				$.ajax({
					url: HT_APP_PATH + '/api/category/end/' + currentCtgNum,
					data: {
						depth: currentDepth + 1,
					},
					dataType: 'html',
				}).done(function(data) {
					$('.dev_review_div .popup_body select[data-depth=' + (currentDepth + 1) + ']').html(data);
					/*
					 * IPCC_ADV 후처리 분류 변경 관련 주석 처리
					 */
					// 3차분류 갯수 저장
					//if(currentDepth + 1 == 3 && data.trim == '<option>소분류 선택</option>') {
					//	$("#modify_close").html('<button type="button" class="btn_popup_close">창닫기</button>');
					//	alert('3차분류가 없습니다.');
					//}
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: LOAD END CATEGORY: ', textStatus);
				});
			}

		});

		// '상담내역 후처리' 저장
		$('.dev_review_div').on('click', '.btn_end_save', function(e) {

			var dep1CtgCd = $('#endCtg1 option:selected').attr("data-ctg-num");
			var dep1CtgNm = $('#endCtg1 option:selected').attr("data-ctg-nm");
			var dep2CtgCd = $('#endCtg2 option:selected').attr("data-ctg-num");
			var dep2CtgNm = $('#endCtg2 option:selected').attr("data-ctg-nm");
			var dep3CtgCd = $('#endCtg3 option:selected').attr("data-ctg-num");
			var dep3CtgNm = $('#endCtg3 option:selected').attr("data-ctg-nm");

			//분류구분 반드시 필수 선택
			if (dep1CtgCd == null || dep1CtgCd == '' || dep1CtgCd == '대분류') {
				$('.dev_review_div .popup_body select[id=endCtg1]').focus();
				alert ("대분류를 선택해 주세요");
				return false;
			}
			if (dep2CtgCd == null || dep2CtgCd == '' || dep2CtgCd == '중분류') {
				$('.dev_review_div .popup_body input[id=endCtg2]').focus();
				alert ("중분류를 선택해 주세요");
				return false;
			}
			/*
			 * IPCC_MCH 후처리 분류 수정 관련 소분류 삭제
			 */
			/* if (dep3CtgCd == null || dep3CtgCd == '' || dep3CtgCd == '소분류') {
				$('.dev_review_div .popup_body input[id=endCtg3]').focus();
				alert ("소분류를 선택해 주세요");
				return false;
			} */

			$.ajax({
				url: HT_APP_PATH + '/api/chat/room/endReview',
				method: 'put',
				data: {
					chatRoomUid: $('.dev_review_div input[name=chatRoomUid]').val(),
					cstmUid: $('.dev_review_div input[name=cstmUid]').val(),
					cstmDivCd: $('.dev_review_div input[name=cstmDivCd]').val(),
					cocId: $('.dev_review_div input[name=cocId]').val(),
					name: $('.dev_review_div input[name=name]').val(),
					dep1CtgCd:dep1CtgCd,
					dep1CtgNm:dep1CtgNm,
					dep1CtgNum:dep1CtgCd,
					dep2CtgCd:dep2CtgCd,
					dep2CtgNm:dep2CtgNm,
					dep2CtgNum:dep2CtgCd,
					dep3CtgCd:dep3CtgCd,
					dep3CtgNm:dep3CtgNm,
					dep3CtgNum:dep3CtgCd,
					memo: $('.dev_review_div .popup_body textarea[name=memo]').val(),
				},
				//dataType: 'html',
			}).done(function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					// 성공
					//$('.dev_review_div').hide();
					window.opener.$('.chatting_list a[data-room-id=' + $('.dev_review_div input[name=chatRoomUid]').val() + ']').parent().css('background', '#ddd');
					fn_layerMessage(result.rtnMsg, 'close');

				}else{
					fn_layerMessage(result.rtnMsg);
				}			

			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: UPDATE END REVIEW: ', textStatus);
			});
		});

		//창닫기
		$('body').on('click', '.btn_popup_close', function(e) {
			self.close();
		});
		

	});
	var layerMessageTimeout = null;
	function fn_layerMessage(msg, close){
		clearTimeout(layerMessageTimeout);
		// 알림 영역 없을 경우 추가
		if ($('.layer_alert').length === 0) {
			$('body').append($('<div class="layer_alert" style="display: none;"><p class="alert_text"></p></div>'));
		}
		
		$(".alert_text").text(msg);
		$(".layer_alert").show();
		
		layerMessageTimeout = setTimeout(function(){
			$(".layer_alert").fadeOut(1000);
			if(close){
				self.close();
			}
		}, 2000);
	}
	
		/**
	 * 키입력시 자동완성 (답변용, 후처리용)
	 * @param String domId ('autoCmpContent': 답변용, 'afterAutoCmp': 후처리용)
	 */
	function fn_autoCmp(domId, inputText) {

		var autoCompleteList;
		
		autoCompleteList = autoCmpAfterList;
		

		$('#' + domId).html('');

		if (inputText.length > 1) {
			var html = '';
			var validCnt = 0;

			if (autoCompleteList.length > 0) {
				for (var i = 0; i < autoCompleteList.length; i++) {
					var content = autoCompleteList[i].content.match(inputText);
					if (content !== null) {
						var thisContent = autoCompleteList[i].content.replace(inputText,
								'<span class="highlight">' + inputText + "</span>");
						html += '<a onclick="javascript:fn_textChat('
							+ autoCompleteList[i].auto_cmp_id
							+ ', &#39;' + domId + '&#39;);"'
							+ ' class="auto_cmp inner_text type_text">';
						html += thisContent + '</a>';
						$('#' + domId).html(html);
						$('#' + domId).show();
						validCnt++;
					}
				}
				if (validCnt === 0) {
					$('#' + domId).hide();
				}
			}
		} else {
			$('#' + domId).hide();
		}
	}

	function fn_textChat(autoCmpId, domId) {
		
		for (i = 0; i < autoCmpAfterList.length; i++) {
			if (autoCmpId === autoCmpAfterList[i].auto_cmp_id) {
				console.debug(autoCmpAfterList[i].content);
				$('.popup_body .tCont textarea').val(autoCmpAfterList[i].content);
				$('#afterAutoCmp').hide();
			}
		}
	}

		
	</script>