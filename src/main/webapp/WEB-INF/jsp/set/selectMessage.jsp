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
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/include.css' />" />
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/common.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>

	<script type="text/javascript">

		$(document).ready(function() {
			// 전체 메세지 글자수 셋팅
			$('#messageForm .dev-messageCheckClass').each(function() {
				$(this).trigger('keyup');
			});

			if ($('#messageForm [name=cnsFrtMsgImg]').val() === '') {
				$('.dev_file_del_btn').hide();
			}
		});

		// on, off 버튼 클릭
		$(document).on("change", "#messageForm .dev-switch-input", function() {
			var cnsPossibleYn;
			if($(this).prop("checked")){
				cnsPossibleYn = $(this).attr("data-on-value");
			}else{
				cnsPossibleYn = $(this).attr("data-off-value");
			}

			$(this).closest("label").find(".dev-switch-input-value").val(cnsPossibleYn);
		});

		//off 시 disable 처리
		$(document).on("change", "#messageForm .dev-switch-input", function() {
			var name = $(this).attr("id");
			
			//사용 여부
			var useYn = false;
			if ($("input[name="+name +"]").val() == 'Y') {
				useYn = false;
			} else if($("input[name="+name +"]").val() == 'N') {
				useYn = true;
			}
			
			//고객 답변 지연 안내
			if (name == 'delayGuideUseYn') {
				$('.input_delay_guide').attr('disabled', useYn);
			} else if (name == 'delayGuideKoUseYn') {
				$('.input_ko_delay_guide').attr('disabled', useYn);
			} else if (name == 'delayGuideO2UseYn') {
				$('.input_o2_delay_guide').attr('disabled', useYn);
			} else if (name == 'delayGuideMpopUseYn') {
				$('.input_mpop_delay_guide').attr('disabled', useYn);

			//고객 답변 지연 종료
			} else if (name == 'delayStopUseYn') {
				$('.input_delay_stop').attr('disabled', useYn);
			} else if (name == 'delayStopKoUseYn') {
				$('.input_ko_delay_stop').attr('disabled', useYn);
			} else if (name == 'delayStopO2UseYn') {
				$('.input_o2_delay_stop').attr('disabled', useYn);
			} else if (name == 'delayStopMpopUseYn') {
				$('.input_mpop_delay_stop').attr('disabled', useYn);
			}
		});

		// 메세지 글자수 체크
		$(document).on("keyup", "#messageForm .dev-messageCheckClass", function() {
			var msgLen = $(this).val().length;
			var maxLen = $(this).closest("div").find("[name='maxLen']").text();
			var objMsgLen = $(this).closest("div").find("[name='msgLen']");

			if(msgLen*1 > maxLen*1){
				$(this).val($(this).val().substr(0,maxLen*1));
				objMsgLen.text(maxLen);
			}else{
				objMsgLen.text(msgLen);
			}
		});


		// 메세지 수정
		$(document).on("click", "#messageForm [name='updateBtn']", function() {
			var selBtn = $(this).attr("data-btn");
			var objMsg = $(this).closest("div").find(".dev-messageCheckClass");

			if($(this).closest("div").find("select option:selected").val() == "Y"){
				if(objMsg.val().length < 5){
					alert("메세지는 5자 이상 입력해 주세요.");
					objMsg.focus();
					return false;
				}
			}

			//고객답변 지연시간안내 고객답변 지연종료시간 validation체크
			if (selBtn == 'delayGuideMsg' || selBtn == 'delayStopMsg' ) {
				
				//채널별 지연안내시간 취합
				delayGuideDay = Number($('#messageForm [name=delayGuideDay]').val()) * 24 * 60;
				delayGuideHr = Number($('#messageForm [name=delayGuideHr]').val()) * 60;
				delayGuideMin = Number($('#messageForm [name=delayGuideMin]').val());
				delayGuideTime = delayGuideDay + delayGuideHr + delayGuideMin;
				delayGuideUseYn = $('#messageForm [name=delayGuideUseYn]').val();
				
				

				//채널별 지연종료시간 취합
				delayStopDay = Number($('#messageForm [name=delayStopDay]').val()) * 24 * 60;
				delayStopHr = Number($('#messageForm [name=delayStopHr]').val()) * 60;
				delayStopMin = Number($('#messageForm [name=delayStopMin]').val());
				delayStopTime = delayStopDay + delayStopHr + delayStopMin;
				delayStopUseYn = $('#messageForm [name=delayStopUseYn]').val();
				
				

				//시간 체크
				if (Number(delayGuideTime) >= Number(delayStopTime) ) {
					alert("웹 채널 고객답변 지연 종료 시간은 고객답변 지연 안내시간보다 커야됩니다.");
					return false;
				}
				

				//최소 설정시간 1분
				//batch 지연메시지 오류 방지
				if (Number(delayGuideTime) == 0 ) {
					alert("최소 1분 이상 세팅해야 합니다.");
					return false;
				}
				

				//최소 설정시간 1분
				//batch 지연메시지 오류 방지
				if (Number(delayStopTime) == 0 ) {
					alert("최소 1분 이상 세팅해야 합니다.");
					return false;
				}
				
				
				//use Yn 체크
/* 				if (delayGuideUseYn == 'N' && delayStopUseYn == 'Y') {
					alert("웹 채널 고객 답변 지연안내 기능을 활성화 해야 지연종료 메시지를 사용 할 수 있습니다.");
					return false;
				}
				if (delayGuideKoUseYn == 'N' && delayStopKoUseYn == 'Y') {
					alert("카카오 채널 고객 답변 지연안내 기능을 활성화 해야 지연종료 메시지를 사용 할 수 있습니다.");
					return false;
				}
				if (delayGuideO2UseYn == 'N' && delayStopO2UseYn == 'Y') {
					alert("O2 채널 고객 답변 지연안내 기능을 활성화 해야 지연종료 메시지를 사용 할 수 있습니다.");
					return false;
				}
				if (delayGuideMpopUseYn == 'N' && delayStopMpopUseYn == 'Y') {
					alert("mPOP 채널 고객 답변 지연안내 기능을 활성화 해야 지연종료 메시지를 사용 할 수 있습니다.");
					return false;
				} */
			}

			$("#messageForm [name='selBtn']").val(selBtn);

			$.ajax({
				url : "<c:url value='/set/updateMessage' />",
				data : $("#messageForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
					$("#messageForm [name='selBtn']").val("");
				}
			});
		});

		// 인사말 이미지 버튼
		$(document).on('click', '#messageForm label[for=cnsFrtMsgImgFile]', function(e) {
			$('#messageForm [name=cnsFrtMsgImgFile]').trigger('click');
		});

		// 인사말 이미지 선택시
		$(document).on('change', '#messageForm [name=cnsFrtMsgImgFile]', function(e) {
			var formData = new FormData();
		    var file = $(this)[0].files;
		    formData.append('file', file[0]);

			htUtils.uploadFile(formData, 'first', null, null, function(data) {
				if (data && data.title) {
					$('#messageForm [name=cnsFrtMsgImg]').val(data.url);
					$('#messageForm [name=cnsFrtMsgImgUseYn]').val('Y');
					$('#messageForm .image_preview').attr('href', data.url);
					$('#messageForm .dev_file_del_btn').show();
				} else {
					htUtils.alert('처리중 오류가 발생했습니다.');
				}
			});
		});

		// 이미지 삭제 버튼
		$(document).on('click', '#messageForm .dev_file_del_btn', function(e) {
			$('#messageForm [name=cnsFrtMsgImg]').val('');
			$('#messageForm [name=cnsFrtMsgImgUseYn]').val('');
			$(this).hide();
		});
	</script>

</head>


<body id="admin">
	<!-- head -->
	<div class="head">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
	</div>
	<!--// head -->

	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/menu.jsp" />

		<!-- left_area -->
		<div class="left_content_area">
			<form id="messageForm" name="messageForm" method="post" action="<c:url value='/set/updateMessage' />">
				<input type="hidden" name="siteId" value="${messageSet.site_id }" />
				<input type="hidden" name="selBtn" value="" />
				<input type="hidden" name="selBtn" value="" />
				<input type="hidden" name="orgFileNm" value="${messageSet.org_file_nm}" />				
		<div class="left_content_head">
					<h2 class="sub_tit">
						<c:forEach var="data" items="${channelList}" varStatus="status">
							${data.cd_nm}
							<c:set var="channelNm" value="${data.cd_nm}" />
							<c:set var="channel" value="${data.cd}" />
							<input type="hidden" name="channel" value="${data.cd}" />
						</c:forEach>
						 - 메세지 관리
					</h2>
					<!-- <button type="button" class="btn_go_help">도움말</button> -->
				</div>
				<div class="inner_nobg">
					<!-- 왼쪽 컨텐츠 -->
					<div class="box_cont_left">
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 인사말</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.cns_frt_msg_text_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="cnsFrtMsgTextUseYn" class="dev-switch-input-value" value="${messageSet.cns_frt_msg_text_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">상담 시작 인사말을 구성합니다.<br>
								해피봇 시작 인사말은 시나리오 관리 메뉴에서 가능합니다.
								</p>
								<div class="form_textarea">
									<textarea name="cnsFrtMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cns_frt_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 상담직원 배정 프로세스 시작 전 고객에게 가장 먼저 보여지는 메세지로 상담 분류에 따라 부서 별로 발송됩니다.</p>
								<p class="text_info" style="display: none;">* 조건 : 상담직원이 배정된 후 자동으로 발송됩니다.</p>
								<div class="filebox">
									<label for="cnsFrtMsgImgFile">이미지첨부</label>
									<input type="file" accept="image/*" name="cnsFrtMsgImgFile" value="" onclick="javascript:;" />
									<input type="hidden" name="cnsFrtMsgImg" value="${messageSet.cns_frt_msg_img}" />
									<input type="hidden" name="cnsFrtMsgImgUseYn" value="${messageSet.cns_frt_msg_img_use_yn}">
									<a class="image_preview" href="${messageSet.cns_frt_msg_img}" target="popup">이미지 보기</a>
									<button type="button" class="btn_del dev_file_del_btn">삭제</button>
								</div>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cnsFrtMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">근무 시간 외 안내 메세지</h3>
								<%--
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.unsocial_msg_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="unsocialMsgUseYn" class="dev-switch-input-value" value="${messageSet.unsocial_msg_use_yn }">
								</label>								
 								--%>

							</div>
							<div class="box_body">
								<div class="form_textarea">
									<textarea name="unsocialMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.unsocial_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 조건 : [정보 일괄 설정] 메뉴에서 근무시간 외 상담 "접수불가" 설정된 상태에서 근무 외 시간에 상담직원 연결한 경우</p>
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="unsocialMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 대기 안내 메세지</h3>
<%--
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.cns_wait_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="cnsWaitUseYn" class="dev-switch-input-value" value="${messageSet.cns_wait_use_yn }">
								</label>	
--%>																
							</div>
							<div class="box_body">
								<div class="form_textarea">
									<textarea name="cnsWaitMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cns_wait_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 조건 : 상담직원 즉시 배정 실패로 배정 대기 상태가 된 경우</p>
								<p class="text_info">※ 메세지에 {COUNT} 사용 시 해당 고객 이전에 배정 대기 중인 고객 수를 안내할 수 있습니다.</p>
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cnsWaitMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>						
						
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">바쁜 시간 메세지</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.busy_msg_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="busyMsgUseYn" class="dev-switch-input-value" value="${messageSet.busy_msg_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<div class="form_textarea">
									<textarea name="busyMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.busy_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 조건 : 배정 대기 상태의 고객이 메세지를 전송하는 경우</p>
								<p class="text_info">※ 고객에게 반복적으로 응답하는 자동 메세지로 상담직원 배정 후에는 중단됩니다.</p>
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="busyMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>				
						<!-- 추가 -->		
						<c:if test="${channel ne 'B'}">
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">고객 금지어 사용 메세지</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.cstm_proh_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="cstmProhUseYn" class="dev-switch-input-value" value="${messageSet.cstm_proh_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">웹채팅에서 고객이 금지어를 사용한 경우 금지어를 포함한 메세지 대신 보여질 메세지를 설정합니다.</p>
								
								<div class="form_textarea">
									<textarea name="cstmProhMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cstm_proh_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 조건 : 고객이 금지어 관리에 등록된 키워드를 포함한 메세지를 전송하는 경우</p>
								<p class="text_info">※ "미사용" 설정한 경우 기본적으로 금지어만 * 처리되며, 상담직원 대화창에서는 사용여부와 상관없이 금지어만 * 처리되어 보입니다.</p>
								<p class="text_info_star">해당 기능은 웹, O2, mPOP에만 적용됩니다. (카카오 상담톡 외부 채널에는 적용되지 않습니다.)</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cstmProhMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>							
						</c:if>
						<!-- 추가2 -->		
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 대기 인원 설정</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.cns_wait_pers_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="cnsWaitPersUseYn" class="dev-switch-input-value" value="${messageSet.cns_wait_pers_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">최대상담건수 초과로 상담 가능한 상담직원이 없을 경우 상담을 대기하는 인원을<br>
								제한하고 초과 접수 시 상담 불가 상태를 안내할 메세지를 설정합니다.</p>
								<div class="form_textarea">
									<textarea name="cnsWaitPersMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cns_wait_pers_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">
									<div class="form_aread">
										 &nbsp;∙ 대기인원 <input type="text" name="cnsWaitCnt" style="width:50px; height:25px; margin-left: 10px;" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.cns_wait_cnt }');" value="${messageSet.cns_wait_cnt }">
									</div>
									<font color="#dc1000">※ 조건 : 고객이 선택한 상담 분류의 모든 상담직원이 최대 상담건수의 상담을 진행 중이며 설정한 대기인원만큼 배정 대기 중인 경우</font><br />
									<font color="#dc1000">※ 메세지 발송 후 해당 채팅상담방은 바로 종료 처리됩니다.</font>
								</p>
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cnsWaitPersMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>	

					</div>
					<!--// 왼쪽 컨텐츠 -->

					<!-- 오른쪽 컨텐츠 -->
					<div class="box_cont_right">

						 <div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 불가 안내 메세지</h3>
								<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.not_cns_msg_use_yn eq 'Y' }">checked</c:if> >
<%--
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.not_cns_msg_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="notCnsMsgUseYn" class="dev-switch-input-value" value="${messageSet.not_cns_msg_use_yn }">
								</label>
 --%>
							</div>
							<div class="box_body">
								<div class="form_textarea">
									<textarea name="notCnsMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.not_cns_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 조건 : [정보 일괄 설정] 메뉴에서 전 상담직원 "상담불가" 설정한 경우 또는 고객이 선택한 상담분류에 상담 가능한 상담직원이 없는 경우</p>
								<p class="text_info">※ 근무시간 외 상담 "접수가능" 설정 상태에서 근무 외 시간에 상담직원 연결한 경우에도 상담 불가 안내 메세지가 보여지며 상담 분류에 따라 부서 별로 발송됩니다.</p>
								<p class="text_info">※ 메세지 발송 후 해당 채팅상담방은 바로 종료 처리됩니다.</p>
								<!-- <p class="text_info">※ 해당 메세지는 카카오 상담톡에서 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="notCnsMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">고객 답변 지연 안내</h3>
								
								<label class="switch w120">
									
									<input class="switch-input dev-switch-input" id="delayGuideUseYn" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.delay_guide_use_yn eq 'Y' }">checked</c:if> >
									<input type="hidden" name="delayGuideUseYn" class="dev-switch-input-value" value="${messageSet.delay_guide_use_yn }">
									<span class="switch-label" data-on="${channelNm} - 사용" data-off="${channelNm} - 미사용"></span>
									<span class="switch-handle"></span>
									
								</label>
								
							</div>
							<div class="box_body">
								<p class="text_info02">상담직원 대화 후 특정 시간동안 고객의 답변이 없을경우 보여지는 메세지를 설정합니다.</p>
								<div class="form_textarea">
									<textarea name="delayGuideMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.delay_guide_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<P CLASS="TEXT_INFO" style="text-align:right;">
								
									<input class="input_delay_guide" type="text" name="delayGuideDay" value="${messageSet.delay_guide_day }" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.delay_guide_day }');" style="width:50px"<c:if test="${messageSet.delay_guide_use_yn ne 'Y' }"> disabled</c:if>>일
									<input class="input_delay_guide" type="text" name="delayGuideHr" value="${messageSet.delay_guide_hr }" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.delay_guide_hr }');" style="width:50px"<c:if test="${messageSet.delay_guide_use_yn ne 'Y' }"> disabled</c:if>>시간
									<input class="input_delay_guide" type="text" name="delayGuideMin" value="${messageSet.delay_guide_min }" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.delay_guide_min }');" style="width:50px"<c:if test="${messageSet.delay_guide_use_yn ne 'Y' }"> disabled</c:if>>분
								</P>
								<p class="text_info">※ 조건 : 채팅상담 중 마지막 메세지 기준으로 설정한 지연시간이 경과한 경우</p>
								<p class="text_info">※ 고객 메세지가 마지막인 경우는 응대가 필요한 상태이므로 제외됩니다.</p>
								<p class="text_info">※ 해당 메세지는 카카오 상담톡에서 적용되지 않습니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="delayGuideMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">고객 답변 지연 종료</h3>
								<label class="switch w120">
									
									<input class="switch-input dev-switch-input" id="delayStopUseYn" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.delay_stop_use_yn eq 'Y' }">checked</c:if> >
									<input type="hidden" name="delayStopUseYn" class="dev-switch-input-value" value="${messageSet.delay_stop_use_yn }">
									<span class="switch-label" data-on="${channelNm} - 사용" data-off="${channelNm} - 미사용"></span>
									<span class="switch-handle"></span>
									
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">상담직원 대화 후 특정 시간동안 고객의 답변이 없을경우 메세지를 보여주고 상담을 종료하는 기능입니다.</p>
								<div class="form_textarea">
									<textarea name="delayStopMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.delay_stop_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<P CLASS="TEXT_INFO" style="text-align:right;">
								
									<input class="input_delay_stop" type="text" name="delayStopDay" value="${messageSet.delay_Stop_day }" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.delay_Stop_day }');" style="width:50px"<c:if test="${messageSet.delay_Stop_use_yn ne 'Y' }"> disabled</c:if>>일
									<input class="input_delay_stop" type="text" name="delayStopHr" value="${messageSet.delay_Stop_hr }" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.delay_Stop_hr }');" style="width:50px"<c:if test="${messageSet.delay_Stop_use_yn ne 'Y' }"> disabled</c:if>>시간
									<input class="input_delay_stop" type="text" name="delayStopMin" value="${messageSet.delay_Stop_min }" onblur="javascript:fn_numberOnlyReturnValue(this, '${messageSet.delay_Stop_min }');" style="width:50px"<c:if test="${messageSet.delay_Stop_use_yn ne 'Y' }"> disabled</c:if>>분
								</P>
								
								<p class="text_info">※ 조건 : 채팅상담 중 고객 답변 지연 안내를 제외한 마지막 메세지 기준으로 설정한 종료시간이 경과한 경우</p>
								<p class="text_info">※ 고객 메세지가 마지막인 경우는 응대가 필요한 상태이므로 제외됩니다.</p>
								<p class="text_info">※ 메세지 발송 후 해당 채팅상담방은 바로 종료 처리됩니다.</p>
								<p class="text_info">※ 해당 메세지는 카카오 상담톡에서 적용되지 않습니다.</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="delayStopMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">고객 종료 메세지</h3>
								<input type="hidden" name="cstmEndMsgUseYn" class="dev-switch-input-value" value="${messageSet.cstm_end_msg_use_yn }">
							</div>
							<div class="box_body">
								<p class="text_info02">고객이 상담을 종료하였을 경우 보여지는 메세지를 설정합니다.</p>
								<div class="form_textarea">
									<textarea name="cstmEndMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cstm_end_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">※ 조건 : 채팅상담을 종료한 경우</p>
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cstmEndMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">상담직원 종료 메세지</h3>
								<label class="switch w120">
									
									<input class="switch-input dev-switch-input" id="cnsrEndMsgUseYn" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.cnsr_end_msg_use_yn eq 'Y' }">checked</c:if> >
									<input type="hidden" name="cnsrEndMsgUseYn" class="dev-switch-input-value" value="${messageSet.cnsr_end_msg_use_yn }">
									<span class="switch-label" data-on="${channelNm} - 사용" data-off="${channelNm} - 미사용"></span>
									<span class="switch-handle"></span>
									
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">상담직원이 상담을 종료하였을 경우 보여지는 메세지를 설정합니다.</p>
								<div class="form_textarea">
									<textarea name="cnsrEndMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cnsr_end_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">* 조건 : 상담중 상담직원이 상담을 종료할 경우</p>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cnsrEndMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>						
						<c:if test="${channel ne 'B'}">
						<div class="box_cont ">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 평가 안내 메세지</h3>
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${messageSet.cns_evl_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="cnsEvlUseYn" class="dev-switch-input-value" value="${messageSet.cns_evl_use_yn }">
								</label>
							</div>
							<div class="box_body">
								<p class="text_info02">웹채팅에서 채팅상담 후 평가 요청 시 보여질 메세지를 설정합니다.</p>
								<p class="text_info02">별점 및 한줄평가로 평가가 가능합니다.</p>
								<div class="form_textarea">
									<textarea name="cnsEvlGuideMsg" class="dev-messageCheckClass" rows="5" cols="100">${messageSet.cns_evl_guide_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">230</span>
								</div>
								<p class="text_info">* 조건 : 상담이 종료되는 경우(웹채팅만 적용)</p>
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="cnsEvlGuideMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>						
						</c:if>
						</div>
					</div>
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

</body>
</html>
