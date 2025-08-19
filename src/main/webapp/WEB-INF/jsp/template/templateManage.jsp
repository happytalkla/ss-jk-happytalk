<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
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
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.dotdotdot.js' />"></script>

	<script type="text/javascript">

		var ctgMgtDpt = "${defaultSet.ctg_mgt_dpt }";

		var templateListScroll = 0;
		var templateListScroll2 = 0;

		$(document).ready(function() {

			$('#schText').focus();

			$(".dev_category_pop").hide();
			$(".dev_category_del_pop").hide(); 
			$("#templateForm #tplNormal").hide();
			$(".dev_file_del_btn").hide();
			$(".dev_file_pdf_del_btn").hide();

			// 템플릿 목록 조회
			fn_selectTemplateList();

			// 등록대기중인 템플릿 목록 조회
			fn_selectConfirmTplList();

		});

		// 카테고리 추가 팝업 열기
		$(document).on("click", "#templateForm .dev_add_category", function() {
			$("#addPopForm [name='tplCtgNum']").val('');
			$("#addPopForm [name='tplCtgNm']").val('');			
			$(".dev_category_pop").show();
			$("#addPopForm [name='tplCtgNm']").focus();
		});

		// 카테고리 추가 팝업 닫기
		$(document).on("click", "#addPopForm .btn_popup_close", function() {
			fn_closeAddPop();
		});

		// 카테고리 추가 팝업 닫기
		$(document).on("click", "#addPopForm .btn_close_pop", function() {
			fn_closeAddPop();
		});

		// 카테고리 수정 팝업 열기
		$(document).on("click", "#templateForm .dev_edit_category", function() {
			$("#addPopForm [name='tplCtgNum']").val($(this).attr("data-tpl_ctg_num"));
			$("#addPopForm [name='tplCtgNm']").val($(this).attr("data-tpl_ctg_nm"));
			$(".dev_category_pop").show();
			$("#addPopForm [name='tplCtgNm']").focus();
		});

		// 카테고리 삭제 팝업 열기
		$(document).on("click", "#templateForm .dev_del_category", function() {
			$("#delPopForm [name='tplCtgNum']").val($(this).attr("data-tpl_ctg_num"));
			$("#delPopForm [name='tplCtgNm']").val($(this).attr("data-tpl_ctg_nm"));

			$.ajax({
				url : "<c:url value='/template/selectTplCtgCnt' />",
				data : $("#delPopForm").serialize(),
				type : "post",
				success : function(result) {
					$("#delPopForm #tplCount").text(result.tplCount);
					$(".dev_category_del_pop").show();
				},
				complete : function() {
				}
			});
		});

		// 카테고리 삭제 팝업 닫기
		$(document).on("click", "#delPopForm .btn_popup_close", function() {
			fn_closeDelPop();
		});

		// 카테고리 삭제 팝업 닫기
		$(document).on("click", "#delPopForm .btn_close_pop", function() {
			fn_closeDelPop();
		});

		$(function(){
			$("#addPopForm [name='tplCtgNm']").keyup(function (event){
				var regexp = /[a-z]\'\"[\[\]{}()<>?|`~!@#$%^&*-_+=,.;:\\]/g;
				v = $(this).val();
				if(regexp.test(v)){
					alert("한글과 숫자만 입력 가능합니다.");
					$(this).val(v.replace(regexp, ''));
				}
			});
		});
		
		// 카테고리 저장
		$(document).on("click", "#addPopForm .dev_btn_save_pop", function() {
			var tplCtgNm = $("#addPopForm [name='tplCtgNm']").val().trim();
			$("#addPopForm [name='tplCtgNm']").val(tplCtgNm);

			if(tplCtgNm == ""){
				alert("카테고리명을 입력해 주세요.");
				$("#addPopForm [name='tplCtgNm']").focus();
				return false;
			}

			$.ajax({
				url : "<c:url value='/template/saveTplCtg' />",
				data : $("#addPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							fn_closeAddPop();
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});


		// 카테고리 삭제
		$(document).on("click", "#delPopForm .dev_btn_del_pop", function() {
			$.ajax({
				url : "<c:url value='/template/deleteTplCtg' />",
				data : $("#delPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						setTimeout(function(){
							fn_closeDelPop();
							goSearch();
						}, 500);
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 카테고리 추가 팝업 닫기
		function fn_closeAddPop(){
			$("#delPopForm [name='tplCtgNum']").val("");
			$("#delPopForm [name='tplCtgNm']").val("");
			$(".dev_category_pop").hide();
		}

		// 카테고리 삭제 팝업 닫기
		function fn_closeDelPop(){
			$("#delPopForm [name='tplCtgNum']").val("");
			$("#delPopForm [name='tplCtgNm']").val("");
			$("#delPopForm #tplCount").text("");
			$(".dev_category_del_pop").hide();
		}

		// 카테고리 추가 엔터키 처리
		$(document).on("keypress", "#addPopForm [name='tplCtgNm']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#addPopForm .dev_btn_save_pop").trigger("click", "ADD CATEGORY");
			}
		});

		// 템플릿 목록 조회
		$(document).on("change", "#templateForm [name='schDeviceType']", function() {
			$("#templateForm .dev_search_btn").trigger("click", "CHANGE DEVICE");
		});

		// 템플릿 목록 조회
		$(document).on("click", "#templateForm .dev_tpl_ctg_sch_btn", function() {
			var schTplCtgNum = $(this).attr("data-tpl_ctg_num");
			$("#templateForm [name='schTplCtgNum']").val(schTplCtgNum);
			$("#templateForm [name='schText']").val("");

			fn_selectTemplateList();
		});

		// 검색 버튼 클릭
		$(document).on("click", "#templateForm .dev_search_btn", function(e) {
			var schText = $("#templateForm [name='schText']").val().trim();
			$("#templateForm [name='schText']").val(schText);
			/* if(schText == ""){
				alert("검색어를 입력해 주세요.");
				$("#templateForm [name='schText']").focus();
				return false;
			} */
			fn_selectTemplateList();
		});

		// 엔터시 검색 버튼 트리거
		$(document).on('keyup', '#schText', function(e) {
			if (e.keyCode === 13) {
				$('#templateForm .dev_search_btn').trigger('click', 'KEYWORD ENTER');
			}
		});

		// 템플릿 목록 조회 구현
		function fn_selectTemplateList(){
			$.ajax({
				url : "<c:url value='/template/selectTemplateList' />",
				data : $("#templateForm").serialize(),
				type : "post",
				success : function(result) {
					fn_displayTemplate(result.templateList, "template");
					
					setTimeout(function(){
						
						$(window).trigger("resize");
						
						if(templateListScroll !=0){
							$('#chat_body').scrollTop(templateListScroll);
							$('#admin').scrollTop(templateListScroll2);
						}

						templateListScroll = 0;
						templateListScroll2 = 0;
						
					}, 500);
					
				},
				complete : function() {
				}
			});
		}

		// 등록 대기중인 템플릿 목록 조회
		function fn_selectConfirmTplList(){
			$.ajax({
				url : "<c:url value='/template/selectConfirmTplList' />",
				data : $("#templateForm").serialize(),
				type : "post",
				success : function(result) {
					fn_displayTemplate(result.confirmTplList, "confirmTpl");
					setTimeout(function(){
						$(window).trigger("resize");
					}, 500);
				},
				complete : function() {
				}
			});
		}

		// 템플릿을 화면에 표시
		function fn_displayTemplate(templateList, type){
			// type : 표시 위치 (템플릿 목록:template, 등록대기중인 템플릿:confirmTpl)
			if(templateList != null && templateList.length > 0){
				var templateHtml = "";
				if(type == "template"){
					templateHtml += '<div id="chat_body" style="max-height:900px;">';
				}else if(type == "confirmTpl"){
					templateHtml += '<div id="chat_body" style="max-height:553px;">';
				}

				templateHtml += '<ul class="chat_temp_list">';
				templateHtml += '</ul>';
				templateHtml += '</div>';

				var templateObj;
				if(type == "template"){
					$("#templateForm .dev_template_list").html(templateHtml);
					templateObj = $("#templateForm .dev_template_list .chat_temp_list");
				}else if(type == "confirmTpl"){
					$("#templateForm .dev_confirm_tpl_list").html(templateHtml);
					templateObj = $("#templateForm .dev_confirm_tpl_list .chat_temp_list");
				}

				$.each(templateList, function(idx, item){
					
					var tmpHtml1 = fn_chatHtml(item, type);
					templateObj.append(tmpHtml1);
					var textObjList = templateObj.find(".tmpTextCheck");
					var textObj = textObjList.eq(textObjList.length-1);
					if(getBoxText200(textObj, 10)){
						textObj.addClass("text200");
						var tmpHtml = '<div class="inner_btn_area">';
						tmpHtml += '<button type="button" class="btn_view_detail dev_goDetail">자세히 보기</button>';
						tmpHtml += '</div>';
						textObj.after(tmpHtml);
					}
					textObj.removeClass("tmpTextCheck");
				});
			}else{
				if(type == "template"){
					$("#templateForm .dev_template_list").html('<p class="no_data">목록이 없습니다.</p>');
				}else if(type == "confirmTpl"){
					$("#templateForm .dev_confirm_tpl_list").html('<p class="no_data">목록이 없습니다.</p>');
				}
			}

		}

		// 템플릿 1개 표시
		function fn_chatHtml(item, type){
			var tplNum = item.tpl_num;
			var createDt = item.create_dt;
			var cstmQue = item.cstm_que;
			var replyCont = item.reply_cont;
			var kwdList = item.templatekwdlist;
			var tplMsgDivCd = item.tpl_msg_div_cd;
			var tplDivCd = item.tpl_div_cd;
			var tplTop = item.top_mark_yn;

			var rtnHtml = "";
			rtnHtml += '<li data-tpl_num="'+tplNum+'">';
			/* 고객 챗 영역:기본텍스트 */
			rtnHtml += '	<div class="left_area">';
			rtnHtml += '		<i class="';
			if(tplDivCd == "G" )	rtnHtml += '			icon_bot';
			else	rtnHtml += '			icon_individual';
			
			rtnHtml += '		 	customer">고객</i>';
			rtnHtml += '		<div class="top_write_info">';
			if(tplTop == 'Y') rtnHtml += '			<i class="icon_check_top"></i>';
			rtnHtml += '			<span>ID : '+tplNum+'</span> ';
			rtnHtml += '			<span>등록일 : '+createDt+'</span>';
			rtnHtml += '			<span>등록자 : '+item.member_nm+'</span>';
			rtnHtml += '		</div>';
			rtnHtml += '		<div class="text_box">';
			rtnHtml += '			<div class="inner_text">';
			rtnHtml += cstmQue.replace(/\n/g, "<br/>");
			rtnHtml += '			</div>';
			rtnHtml += '		</div>';
			rtnHtml += '	</div>';
			/* 고객 챗 영역:기본텍스트 */
			/* 상담직원 챗 영역 */
			rtnHtml += '	<div class="right-chat-wrap">';
			rtnHtml += fn_getCounselorHtml(replyCont, tplMsgDivCd);
			rtnHtml += '		<div class="tag_area">';

			$.each(kwdList, function(idx, item2){
				rtnHtml += '<span class="tag">' + item2.kwd_nm + '</span>';
			});

			rtnHtml += '		</div>';
			rtnHtml += '	</div>';
			/* 상담직원 챗 영역 */
			rtnHtml += '	<div class="btn_temp_area">';

			if(type == "confirmTpl"){
				rtnHtml += '		<button type="button" class="btn_write_temp dev_tpl_edit_btn '+type+'" data-share_req_num="'+item.share_req_num+'"><i>쓰기</i></button>';
			}else{
				rtnHtml += '		<button type="button" class="btn_write_temp dev_tpl_edit_btn '+type+'" data-share_req_num=""><i>쓰기</i></button>';
			}
			rtnHtml += '		<button type="button" class="btn_del_temp dev_tpl_del_btn '+type+'"><i>삭제</i></button>';

			rtnHtml += '	</div>';
			rtnHtml += '</li>';

			return rtnHtml;
		}

		// 템플릿의 상담직원 답변 표시
		function fn_getCounselorHtml(replyCont, tplMsgDivCd) {

			console.debug(replyCont, tplMsgDivCd);

			if (typeof replyCont === 'undefined') {
				return;
			}

			var chatContents = JSON.parse(replyCont);
			if (typeof chatContents.balloons === 'undefined'
				|| chatContents.balloons.length === 0) {
				return;
			}

			var balloon = chatContents.balloons[0];
			var rtnHtml = '<div class="bubble-wrap"><article class="inner-chat"><div class="bubble-templet type-2">';

			var fileExist = false;
			$.each(balloon.sections, function(idx, section) {
				if (section.type === 'file') {
					fileExist = true;
				}
			});

			$.each(balloon.sections, function(idx, section) {

				// 텍스트 섹션
				if (section.type ===  MESSAGE_SECTION_TYPE.TEXT) {
					if (section.data.length > 200) {
						if (fileExist) {
							rtnHtml += '<div class="mch type_text text200 text_top">';
						} else {
							rtnHtml += '<div class="mch type_text text200">';
						}
						rtnHtml += section.data.replace(/\n/g, '<br/>');
						rtnHtml += '</div>';
						/* rtnHtml += '<div class="inner_btn_area">';
						rtnHtml += '<button type="button" class="btn_view_detail dev_goDetail">자세히 보기</button>';
						rtnHtml += '</div>'; */
					} else {
						if (fileExist) {
							rtnHtml += '<div class="mch type_text text_top">';
						} else {
							rtnHtml += '<div class="mch type_text">';
						}
						rtnHtml += section.data.replace(/\n/g, '<br/>');
						rtnHtml += '</div>';
					}
				}
				// 파일 섹션
				else if (section.type === MESSAGE_SECTION_TYPE.FILE) {
					if(!section.display.includes("image")){ 
						rtnHtml += '<ul class="btn-wrap">';
						rtnHtml += '<li><a href="' + section.data + '" target="_blank" class="btn-bn-list-icon" title="새창열림">'+section.extra+'</a></li>';
						rtnHtml += '</ul>';						
					}else{
						rtnHtml += '<div class="tit">';
						rtnHtml += '<img src="' + section.data + '" class="chatImageCs" />';
						rtnHtml += '</div>';
					}
				}
				// 액션 섹션
				else if (section.type === MESSAGE_SECTION_TYPE.ACTION) {
					rtnHtml += '<ul class="btn-wrap">';
					$.each(section.actions, function(idx2, action) {
						// 링크 타입
						if (action.type === MESSAGE_ACTION_TYPE.LINK) {
							rtnHtml += '<li><a href="' + action.data + '" target="_blank" class="btn-bn-list-icon" title="새창열림">'
								+ action.name + '</a></li>';
						}
					});
					rtnHtml += '</ul>';
				}
			});
			rtnHtml += '</div></article></div>';

			return rtnHtml;
		}

		// 자세히 보기
		$(document).on("click", "#templateForm .dev_goDetail", function() {
			$(".dev_detail_pop .dev_detail_pop_cont").html($(this).parent().prev().html());
			$(".dev_detail_pop").show();
		});

		// 자세히 보기 닫기
		$(document).on("click", ".dev_detail_pop .dev_detail_pop_close", function() {
			$(".dev_detail_pop").hide();
		});

		// 템플릿 등록 구분 코드 변경
		$(document).on("change", "#templateForm #tplMsgDivCd", function() {
			var tplMsgDivCd = $(this).val();
			if(tplMsgDivCd == "NORMAL"){
				$("#templateForm #tplNormal").show();
			}else{
				$("#templateForm #tplNormal").hide();
				$("#templateForm [name='tplImgUrl']").val("");

			}
		});

		// 이미지 선택
		$(document).on("change", "#templateForm .dev_file", function(){
			if(window.FileReader){  // modern browser
				var filename = $(this)[0].files[0].name;
				var type = $(this)[0].files[0].type;
				console.log("type : " + type);
				if(type.indexOf("image") === -1 ){
					alert("이미지 파일이 아닙니다.");
					return false;
				}				
			}else {  // old IE
				var filename = $(this).val().split('/').pop().split('\\').pop();
			}

			//$(this).siblings('.dev_file_view_name').val(filename);
			$('.dev_file_view_name').val(filename);
			//alert(filename);
			$(this).siblings(".dev_file_del_btn").show();
		});

		// 이미지 삭제
		$(document).on("click", "#templateForm .dev_file_del_btn", function(){
			$(this).siblings(".dev_file").val("");
			//$(this).siblings('.dev_file_view_name').val("");
			//$('.dev_file').val("");
			$('.dev_file_view_name').val("");

			$("#templateForm [name='oldOrgImgDelYn']").val("Y");
			$("#templateForm [name='cnsFrtMsgImg']").val("");
			$("#templateForm [name='oldOrgImgPath']").val("");
			$(this).hide();
		});


		//이미지 보기  
		function fn_fileView() {
			var msgImgUrl = $("#templateForm [name='cnsFrtMsgImg']").val();
			if (msgImgUrl=='') {
				return false;
			}
			//alert (msgImgUrl);
			//location.href = msgImgUrl;
			window.open(msgImgUrl);
		}
		

		// pdf 선택
		$(document).on("change", "#templateForm .dev_file_pdf", function(){
			if(window.FileReader){  // modern browser
				var filename = $(this)[0].files[0].name;
				var type = $(this)[0].files[0].type;
				console.log("type : " + type);
				if(type.indexOf("pdf") === -1 ){
					alert("PDF파일이 아닙니다.");
					$("#tplForm [name='tplPdfUrl']").val("");
					return false;
				}
			}else {  // old IE
				var filename = $(this).val().split('/').pop().split('\\').pop();
			}
			console.log(filename);
			//$(this).siblings('.dev_file_view_name').val(filename);
			$('.dev_file_pdf_view_name').val(filename);
			//alert(filename);
			$(this).siblings(".dev_file_pdf_del_btn").show();
		});

		//  pdf 삭제
		$(document).on("click", "#templateForm .dev_file_pdf_del_btn", function(){
			$(this).siblings(".dev_file_pdf").val("");
			//$(this).siblings('.dev_file_view_name').val("");
			//$('.dev_file').val("");
			$('.dev_file_pdf_view_name').val("");

			$("#templateForm [name='oldMimeType']").val("");
			$("#templateForm [name='oldOrgPdfNm']").val("");
			$("#templateForm [name='oldOrgPdfPath']").val("");		
			$("#templateForm [name='tplPdfBtn']").val("");
			$("#templateForm [name='oldOrgPdfDelYn']").val("Y");
			$(this).hide();
		});


		// 템플릿 저장
		$(document).on("click", "#templateForm .dev_save_btn", function(){
			/*
			var androidYn = $("#templateForm [name='androidYn']").is(":checked");
			var iphoneYn = $("#templateForm [name='iphoneYn']").is(":checked");
			var webYn = $("#templateForm [name='webYn']").is(":checked");

			if(!androidYn && !iphoneYn && !webYn){
				alert("안드로이드, 아이폰, 웹 중 하나는 필수 체크 입니다.");
				return false;
			}
*/
			var cstmQue = $("#templateForm [name='cstmQue']").val().trim();
			if(cstmQue == ""){
				alert("고객질문을 입력하세요.");
				$("#templateForm [name='cstmQue']").focus();
				return false;
			}

			var replyContText = $("#templateForm [name='replyContText']").val().trim();
			if(replyContText == ""){
				alert("상담직원 답변을 입력하세요.");
				$("#templateForm [name='replyContText']").focus();
				return false;
			}

			var tplMsgDivCd = $("#templateForm [name='tplMsgDivCd'] option:selected").val();

			if(tplMsgDivCd == "NORMAL"){
				
				var checkFlag = false;
				var fileName = $("#templateForm [name='tplImgUrl']").val();
				var oldOrgImgPath = $("#templateForm [name='oldOrgImgPath']").val();
				var fileName1 = $("#templateForm [name='tplPdfUrl']").val();
				var oldOrgPdfPath = $("#templateForm [name='oldOrgPdfPath']").val();

				if(fileName != "" || oldOrgImgPath != "" || fileName1 != "" || oldOrgPdfPath != "" ){
					checkFlag = true;
				}
								
				var linkBtnNmArr = $("#templateForm [name='linkBtnNmArr']");
				var linkUrlArr = $("#templateForm [name='linkUrlArr']");
				
				var cntLinkBtn = 0;  //// null 체크를 위한 카운트
				$("#templateForm [name='linkBtnNmArr']").each(function(){
					 $(this).val($(this).val().trim());
					 $(this).siblings("[name='linkUrlArr']").val($(this).siblings("[name='linkUrlArr']").val().trim());

					 var linkBtnNm = $(this).val().trim();

					 var linkUrl = $(this).siblings("[name='linkUrlArr']").val().trim();
					 $(this).val(linkBtnNm);
					 $(this).siblings("[name='linkUrlArr']").val(linkUrl);

					 if(linkBtnNm != "" && linkUrl != ""){
						 cntLinkBtn = cntLinkBtn + 1;
					 }

					 
					 if(cntLinkBtn > 0){
							checkFlag = true;
					 }else{
//						 checkFlag = false;
						// return false;
					 }
					 
				});

				if(!checkFlag){
					alert("이미지 또는 링크 정보 또는 파일을 입력하세요.");
					return false;
				}
			}

			var kwdNmArr = $("#templateForm [name='kwdNmArr']").val().replace(/ /g, "");
			$("#templateForm [name='kwdNmArr']").val(kwdNmArr);

			var form = $("#templateForm")[0];
			var formData = new FormData(form);

			templateListScroll = $('#chat_body').scrollTop();
			templateListScroll2 = $('#admin').scrollTop();

			$.ajax({
				url : "<c:url value='/template/saveTemplate' />",
				processData : false,
				contentType : false,
				data : formData,
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);

						// 템플릿 목록 재조회
						$("#templateForm [name='tplNum']").val("");
						fn_selectTemplateList();
						fn_selectConfirmTplList();
						/*/ 공유 요청 번호 존재시 공유 요청 목록 재조회
						var shareReqNum = $("#templateForm [name='shareReqNum']").val();
						if(shareReqNum != ""){
							$("#templateForm [name='shareReqNum']").val("");
							fn_selectConfirmTplList();
						}
*/
						fn_templateClear();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});


		// 템플릿 입력창 초기화
		function fn_templateClear(){

			if(previewImageData){
				previewImageData = "";
			}
			
			$("#templateForm [name='tplNum']").val("");
			$("#templateForm [name='shareReqNum']").val("");
			$("#templateForm [name='oldOrgImgDelYn']").val("");
			$("#templateForm [name='oldOrgPdfDelYn']").val("");
			$("#templateForm [name='oldTplImgUrl']").val("");
			$("#templateForm [name='oldMimeType']").val("");
			$("#templateForm [name='oldOrgImgNm']").val("");
			$("#templateForm [name='oldOrgImgPath']").val("");
			
/*
			$("#templateForm [name='androidYn']").prop("checked", true);
			$("#templateForm [name='iphoneYn']").prop("checked", true);
			$("#templateForm [name='webYn']").prop("checked", true);
*/
			$("#templateForm [name='cstmQue']").val("");
			$("#templateForm [name='replyContText']").val("");

			$("#templateForm .dev_file").val("");
			$("#templateForm .dev_file_pdf").val("");
			$("#templateForm .dev_file_view_name").val("");
			$("#templateForm .dev_file_pdf_view_name").val("");
			$("#templateForm .dev_file_del_btn").hide();
			$("#templateForm .dev_file_pdf_del_btn").hide();
			$("#templateForm [name='tplPdfBtn']").val("");
			$("#templateForm [name='linkBtnNmArr']").each(function(){
				$(this).val("");
			});

			$("#templateForm [name='linkUrlArr']").each(function(){
				$(this).val("");
			});

			$("#templateForm [name='topMarkYn']").prop("checked", false);
			$("#templateForm [name='kwdNmArr']").val("");

			$("#templateForm select[name='tplMsgDivCd']").val("TEXT");
			$("#templateForm select[name='tplMsgDivCd']").trigger("change");
		}

		// 템플릿 삭제
		$(document).on("click", "#templateForm .dev_tpl_del_btn", function() {
			var type = "";
			if($(this).hasClass("template") == true){
				type = "template";
			}else if($(this).hasClass("confirmTpl") == true){
				type = "confirmTpl";
			}else{
				alert("오류입니다. 관리자에게 문의하세요.");
				return false;
			}

			if(confirm("해당 템플릿을 정말 삭제하시겠습니까?")){
				var tplNum = $(this).closest("li").attr("data-tpl_num");

				$.ajax({
					url : "<c:url value='/template/deleteTemplate' />",
					data : {"tplNum" : tplNum, "type" : type},
					type : "post",
					success : function(result) {
						if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
							// 성공
							fn_layerMessage(result.rtnMsg);
							goSearch();
						}else{
							fn_layerMessage(result.rtnMsg);
						}
					},
					complete : function() {
					}
				});
			}
		});

		// 템플릿 수정 버튼
		$(document).on("click", "#templateForm .dev_tpl_edit_btn", function() {
			fn_templateClear();
			var tplNum = $(this).closest("li").attr("data-tpl_num");
			var templateYn = $(this).hasClass("template");
			
			$.ajax({
				url : "<c:url value='/template/selectTemplateOne' />",
				data : {"tplNum" : tplNum},
				type : "post",
				success : function(result) {
					var tplNum = result.template.tpl_num;
					var tplCtgNum = result.template.tpl_ctg_num;
					var tplMsgDivCd = result.template.tpl_msg_div_cd;
					var cstmQue = result.template.cstm_que;
					var replyCont = result.template.reply_cont;
					var replyContText = result.template.reply_cont_text;
					var oldOrgImgNm = result.template.org_img_nm;
					var oldOrgImgPath = result.template.org_img_path;
					
					var oldOrgPdfNm = result.template.org_pdf_nm;
					var oldOrgPdfPath = result.template.org_pdf_path;
					var oldMimeType = result.template.org_mime_type;
					var tplPdfBtn = result.template.tpl_pdf_btn;
					/*
					var webYn = result.template.web_yn;
					var androidYn = result.template.android_yn;
					var iphoneYn = result.template.iphone_yn;
					*/
					var topMarkYn = result.template.top_mark_yn;
					var kwdList = result.template.templatekwdlist;
					var cns_frt_msg_img = result.template.cns_frt_msg_img;
					var tplDivcd = result.template.tpl_div_cd;
					var creater = result.template.creater;

//					if(templateYn == true){
						$("#templateForm [name='tplNum']").val(tplNum);
					//}else{
//						$("#templateForm [name='tplNum']").val("");
//					}

					$("#templateForm select[name='tplCtgNum']").val(tplCtgNum);
					/*
					if(webYn != null && webYn == "Y"){
						$("#templateForm [name='webYn']").prop("checked", true);
					}else{
						$("#templateForm [name='webYn']").prop("checked", false);
					}
					if(androidYn != null && androidYn == "Y"){
						$("#templateForm [name='androidYn']").prop("checked", true);
					}else{
						$("#templateForm [name='androidYn']").prop("checked", false);
					}
					if(iphoneYn != null && iphoneYn == "Y"){
						$("#templateForm [name='iphoneYn']").prop("checked", true);
					}else{
						$("#templateForm [name='iphoneYn']").prop("checked", false);
					}
	*/
					if(tplDivcd != null && tplDivcd == "G"){
						$("#templateForm [name='tplDivCd']").prop("checked", true);
					}else{
						$("#templateForm [name='tplDivCd']").prop("checked", false);
					}

					$("#templateForm select[name='tplMsgDivCd']").val(tplMsgDivCd);
					$("#templateForm select[name='tplMsgDivCd']").trigger("change");


					$("#templateForm [name='cstmQue']").val(cstmQue);
					//$("#templateForm [name='replyContText']").val(replyContText);
					$("#templateForm .dev_file_view_name").val(oldOrgImgNm);
					//$("#templateForm [name='oldTplImgUrl']").val(item.contents.fileNm);
					//$("#templateForm [name='oldMimeType']").val(item.contents.fileType);
					$("#templateForm [name='oldOrgImgNm']").val(oldOrgImgNm);
					$("#templateForm [name='oldOrgImgPath']").val(oldOrgImgPath);
					if (cns_frt_msg_img!='' && cns_frt_msg_img!=null) {
						$("#templateForm .dev_file_del_btn").show();
					}else {
						$("#templateForm .dev_file_del_btn").hide();
					}
					$("#templateForm [name='oldOrgImgDelYn']").val("N");
					$("#templateForm [name='cnsFrtMsgImg']").val(cns_frt_msg_img);
					$("#templateForm [name='wmemberUid']").val(creater);

					$("#templateForm .dev_file_pdf_view_name").val(oldOrgPdfNm);
					$("#templateForm [name='oldOrgPdfNm']").val(oldOrgPdfNm);
					$("#templateForm [name='oldOrgPdfPath']").val(oldOrgPdfPath);
					$("#templateForm [name='oldMimeType']").val(oldMimeType);
					$("#templateForm [name='tplPdfBtn']").val(tplPdfBtn);
					
					if (oldOrgPdfNm!='' && oldOrgPdfNm!=null) {
						$("#templateForm .dev_file_pdf_del_btn").show();
					}else {
						$("#templateForm .dev_file_pdf_del_btn").hide();
					}
					$("#templateForm [name='oldOrgPdfDelYn']").val("N");

					var jsonObject = JSON.parse(replyCont);
					var rtnHtml = "";

					var _sections = jsonObject.balloons[0].sections;
					console.log(_sections);	
					for (var i=0;i<_sections.length;i++) {
						if(_sections[i].type == "text"){
							$("#templateForm [name='replyContText']").val(_sections[i].data);
						}else if (_sections[i].type == "action") {
							var _actions = _sections[i].actions;
							for (var x=0;x<_actions.length;x++) {
								$("#templateForm [name='linkBtnNmArr']").eq(x).val(_actions[x].name);
								$("#templateForm [name='linkUrlArr']").eq(x).val(_actions[x].data);
							}

						}
					}
/** 왜 안되는지 모름... 그래서 위에 for로직으로 대체함
					$.each(_sections, function(idx, item2){
						if(item2.type == "text"){
							$("#templateForm [name='replyContText']").val(item2.contents);
						}else if(item2.type == "file"){
							$("#templateForm .dev_file_view_name").val(oldOrgImgNm);
							$("#templateForm [name='oldTplImgUrl']").val(item2.contents.fileNm);
							$("#templateForm [name='oldMimeType']").val(item2.contents.fileType);
							$("#templateForm [name='oldOrgImgNm']").val(oldOrgImgNm);
							$("#templateForm [name='oldOrgImgPath']").val(oldOrgImgPath);
							$("#templateForm .dev_file_del_btn").show();
							$("#templateForm [name='oldOrgImgDelYn']").val("N");
						}else if(item2.type == "action"){
							var i = 0;
							
							$.each(item2.contents, function(idx2, item22){
								$("#templateForm [name='linkBtnNmArr']").eq(i).val(item22.linkNm);
								$("#templateForm [name='linkUrlArr']").eq(i).val(item22.linkUrl);
								i++;
							});
						}
					});
**/

					var kwdNmArrVal = "";
					$.each(kwdList, function(idx, item){
						if(idx != 0){
							kwdNmArrVal += ",";
						}
						kwdNmArrVal += item.kwd_nm;
					});
					$("#templateForm [name='kwdNmArr']").val(kwdNmArrVal);

					if(topMarkYn != null && topMarkYn == "Y"){
						$("#templateForm [name='topMarkYn']").prop("checked", true);
					}else{
						$("#templateForm [name='topMarkYn']").prop("checked", false);
					}
					$("#templateForm [name='replyContText']").focus();
				},
				complete : function() {
				}
			});
		});

		// 템플릿 추가 버튼 - 템플릿 입력창을 초기화
		$(document).on("click", "#templateForm .dev_template_clean", function() {
			fn_templateClear();
		});

		// 템플릿 양식받기 버튼 클릭
		$(document).on("click", "#templateForm .dev_temp_down_btn", function() {
			$("#excelForm").submit();
		});

		// 템플릿 일괄등록 파일 선택시
		$(document).on("click", "#templateForm .dev_all_temp_btn", function() {
			var tplUploadFile = $("#templateForm [name='tplUploadFile']").val();
			if(tplUploadFile != ""){
				var form = $("#templateForm")[0];
				var formData = new FormData(form);

				$.ajax({
					url : "<c:url value='/template/updateTplExcel' />",
					processData : false,
					contentType : false,
					data : formData,
					type : "post",
					success : function(result) {
						if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
							// 성공
							fn_layerMessage(result.rtnMsg);

							// 템플릿 목록 재조회
							$("#templateForm [name='tplNum']").val("");
							fn_selectTemplateList();

							fn_templateClear();
						}else{
							fn_layerMessage(result.rtnMsg);
						}
					},
					complete : function() {
						$("#templateForm [name='tplUploadFile']").val("");
					}
				});
			}else{
				alert("등록할 파일을 선택해 주세요.");
			}
		});


		// 새창열기 버튼
		$(document).on("click", "#templateForm .dev_btn_link_open", function() {
			var linkUrl = $(this).parent().find("input[name='linkUrlArr']").val();
			if(linkUrl == ""){
				alert("URL을 입력해 주세요.");
				$(this).parent().find("input[name='linkUrlArr']").focus();
				return false;
			}

			window.open(linkUrl);
		});

		// 템플릿 x 버튼 - 템플릿 해당 입력창을 초기화
		$(document).on("click", "#templateForm .dev_btn_link_del", function() {
			var nm = $(this).parent().find("input[name='linkBtnNmArr']").val("");
			var link = $(this).parent().find("input[name='linkUrlArr']").val("");
		});

		function goSearch(){
			$("#templateForm").attr("action", "<c:url value='/template/selectTemplate' />");
			$("#templateForm").submit();
		}

		function onPreviewPop(){

			var cstmQue = $("#templateForm [name='cstmQue']").val().trim();
			if(cstmQue == ""){
				alert("고객질문을 입력하세요.");
				$("#templateForm [name='cstmQue']").focus();
				return false;
			}

			var replyContText = $("#templateForm [name='replyContText']").val().trim();
			if(replyContText == ""){
				alert("상담직원 답변을 입력하세요.");
				$("#templateForm [name='replyContText']").focus();
				return false;
			}

			var tplMsgDivCd = $("#templateForm [name='tplMsgDivCd'] option:selected").val();

			var actionsArr = [];
			
			if(tplMsgDivCd == "NORMAL"){
				
				var checkFlag = false;
				var fileName = $("#templateForm [name='tplImgUrl']").val();
				if(fileName === ""){
					fileName = $("#templateForm [name='cnsFrtMsgImg']").val();
				}
				var oldOrgImgPath = $("#templateForm [name='oldOrgImgPath']").val();
				var fileName1 = $("#templateForm [name='tplPdfUrl']").val();
				var oldOrgPdfPath = $("#templateForm [name='oldOrgPdfPath']").val();

				if(fileName != "" || oldOrgImgPath != "" || fileName1 != "" || oldOrgPdfPath != "" ){
					checkFlag = true;
				}
								
				var linkBtnNmArr = $("#templateForm [name='linkBtnNmArr']");
				var linkUrlArr = $("#templateForm [name='linkUrlArr']");
				
				var cntLinkBtn = 0;
				$("#templateForm [name='linkBtnNmArr']").each(function(){
					 $(this).val($(this).val().trim());
					 $(this).siblings("[name='linkUrlArr']").val($(this).siblings("[name='linkUrlArr']").val().trim());

					 var linkBtnNm = $(this).val().trim();
					 var linkUrl = $(this).siblings("[name='linkUrlArr']").val().trim();
					 $(this).val(linkBtnNm);
					 $(this).siblings("[name='linkUrlArr']").val(linkUrl);

					if(linkBtnNm != "" && linkUrl != ""){
						cntLinkBtn = cntLinkBtn + 1;

						var actionsJson = {};
						actionsJson.type = 'link';
						actionsJson.name = linkBtnNm;
						actionsJson.data = linkUrl;
						actionsArr.push(actionsJson);
					 }

					 if(cntLinkBtn > 0){
						checkFlag = true;
					 }
					 
				});

				if(!checkFlag){
					alert("이미지 또는 링크 정보 또는 파일을 입력하세요.");
					return false;
				}
			}

			var previewData = [];
			var previewJsonData = {};
			
			var previewReplyContJson = {};
			var previewBalloons = [];
			var previewBalloonsJson = {};
			var previewSections = [];
			var previewSectionsJson = {};

			previewSectionsJson.type = 'text';
			previewSectionsJson.data = replyContText;
			previewSections.push(previewSectionsJson);

			if(tplMsgDivCd == "NORMAL"){
				
				if(fileName != ''){
					previewSectionsJson = {};
					previewSectionsJson.type = 'file';
					previewSectionsJson.display = 'image/png';
					previewSectionsJson.extra = '';
					
					previewSections.push(previewSectionsJson);
				}
				
				if(actionsArr.length > 0){
					previewSectionsJson = {};
					previewSectionsJson.type = 'action';
					previewSectionsJson.actions = actionsArr;
					previewSections.push(previewSectionsJson);
				}
				
			}

			previewBalloonsJson.sections = previewSections;
			previewBalloons.push(previewBalloonsJson);

			previewReplyContJson.balloons = previewBalloons;

			var kwdNmTemp = $("#templateForm [name='kwdNmArr']").val().replace(/ /g, "");
			var kwdNmArr;
			if(kwdNmTemp && kwdNmTemp != ''){
				kwdNmArr = kwdNmTemp.split(",");
			}

			var previewTemplatekwdlist = [];
			var templatekwdlistJson = {};

			if(kwdNmArr && kwdNmArr.length > 0){
				for(var i=0;i<kwdNmArr.length;i++){
					templatekwdlistJson = {};
					templatekwdlistJson.kwd_nm = kwdNmArr[i];
					previewTemplatekwdlist.push(templatekwdlistJson);
				}
				previewJsonData.templatekwdlist = previewTemplatekwdlist;
			}
			
			previewJsonData.reply_cont = previewReplyContJson;
			previewJsonData.cstm_que = cstmQue;
			previewJsonData.reply_cont_text = replyContText;

			previewData.push(previewJsonData);
			

			fn_displayTemplate2(previewData,'template');
			
			$('#templatePreviewDiv').show();
			$('body').css("overflow", "hidden");
		}

		function templatePreviewClose(){
			$('#templatePreviewDiv').hide();
			$('body').css("overflow", "scroll");
		}

		function fn_displayTemplate2(templateList, type){
			
			if(templateList && templateList != null && templateList.length > 0){
				var templateHtml = "";
				templateHtml += '<div id="chat_body" style="max-height:900px;">';
				
				templateHtml += '<ul class="chat_temp_list">';
				templateHtml += '</ul>';
				templateHtml += '</div>';

				var templateObj;
				$(".previewTemplateDiv").html(templateHtml);
				templateObj = $(".previewTemplateDiv .chat_temp_list");
				
				$.each(templateList, function(idx, item){
					
					var tmpHtml1 = fn_chatHtml2(item, type);
					templateObj.append(tmpHtml1);
					var textObjList = templateObj.find(".tmpTextCheck");
					var textObj = textObjList.eq(textObjList.length-1);
					if(getBoxText200(textObj, 10)){
						textObj.addClass("text200");
						var tmpHtml = '<div class="inner_btn_area">';
						tmpHtml += '<button type="button" class="btn_view_detail dev_goDetail">자세히 보기</button>';
						tmpHtml += '</div>';
						textObj.after(tmpHtml);
					}
					textObj.removeClass("tmpTextCheck");
				});
				
			}else{
				$(".previewTemplateDiv").html('<p class="no_data">미리보기 데이터가 없습니다.</p>');
			}

		}

		function fn_chatHtml2(item, type){

			var tempNowDate = new Date();
			var tempNowYear = String(tempNowDate.getFullYear());
			var tempNowMon = String(tempNowDate.getMonth()+1);
			var tempNowDay = String(tempNowDate.getDate());
			if(tempNowMon.length == 1){
				tempNowMon = "0"+tempNowMon;
			}
			if(tempNowDay.length == 1){
				tempNowDay = "0"+tempNowDay;
			}
			var todayDataCheck = tempNowYear+"-"+tempNowMon+"-"+tempNowDay;
			
			var cstmQue = item.cstm_que;
			var replyCont = item.reply_cont;
			var kwdList = item.templatekwdlist;
			var tplMsgDivCd = item.tpl_msg_div_cd;
			var tplDivCd = item.tpl_div_cd;
			var tplTop = item.top_mark_yn;

			var rtnHtml = "";
			rtnHtml += '<li data-tpl_num="xxxx">';
			rtnHtml += '	<div class="left_area">';
			rtnHtml += '		<i class="';
			if(tplDivCd == "G" )	rtnHtml += '			icon_bot';
			else	rtnHtml += '			icon_individual';
			
			rtnHtml += '		 	customer">고객</i>';
			rtnHtml += '		<div class="top_write_info">';
			if(tplTop == 'Y') rtnHtml += '			<i class="icon_check_top"></i>';
			rtnHtml += '			<span>ID : xxxx</span> ';
			rtnHtml += '			<span>등록일 : '+todayDataCheck+'</span>';
			rtnHtml += '			<span>등록자 : '+'${sessionVo.name }'+'</span>';
			rtnHtml += '		</div>';
			rtnHtml += '		<div class="text_box">';
			rtnHtml += '			<div class="inner_text">';
			rtnHtml += cstmQue.replace(/\n/g, "<br/>");
			rtnHtml += '			</div>';
			rtnHtml += '		</div>';
			rtnHtml += '	</div>';
			rtnHtml += '	<div class="right-chat-wrap">';
			rtnHtml += fn_getCounselorHtml2(replyCont, tplMsgDivCd);
			rtnHtml += '		<div class="tag_area">';

			$.each(kwdList, function(idx, item2){
				rtnHtml += '<span class="tag">' + item2.kwd_nm + '</span>';
			});

			rtnHtml += '		</div>';
			rtnHtml += '	</div>';
			rtnHtml += '	<div class="btn_temp_area">';

			if(type == "confirmTpl"){
				rtnHtml += '		<button type="button" class="btn_write_temp '+type+'" data-share_req_num="'+item.share_req_num+'"><i>쓰기</i></button>';
			}else{
				rtnHtml += '		<button type="button" class="btn_write_temp '+type+'" data-share_req_num=""><i>쓰기</i></button>';
			}
			rtnHtml += '		<button type="button" class="btn_del_temp dev_tpl_del_btn '+type+'"><i>삭제</i></button>';

			rtnHtml += '	</div>';
			rtnHtml += '</li>';

			return rtnHtml;
		}

		function fn_getCounselorHtml2(replyCont, tplMsgDivCd) {

			if (!replyCont) {
				return;
			}

			var chatContents = replyCont;
			if (!chatContents.balloons || chatContents.balloons.length === 0) {
				return;
			}

			var balloon = chatContents.balloons[0];
			var rtnHtml = '<div class="bubble-wrap"><article class="inner-chat"><div class="bubble-templet type-2">';

			var fileExist = false;
			$.each(balloon.sections, function(idx, section) {
				if (section.type === 'file') {
					fileExist = true;

					rtnHtml += '<div class="tit">';
					if(previewImageData && previewImageData != ""){
						rtnHtml += '<img src="' + previewImageData + '" class="chatImageCs" />';
					}else{
						rtnHtml += '<img src="' + $("#templateForm [name='cnsFrtMsgImg']").val() + '" class="chatImageCs" />';
					}
					rtnHtml += '</div>';
				}
			});

			$.each(balloon.sections, function(idx, section) {

				if (section.type ===  MESSAGE_SECTION_TYPE.TEXT) {
					if (section.data.length > 200) {
						if (fileExist) {
							rtnHtml += '<div class="mch type_text text200 text_top">';
						} else {
							rtnHtml += '<div class="mch type_text text200">';
						}
						rtnHtml += section.data.replace(/\n/g, '<br/>');
						rtnHtml += '</div>';
					} else {
						if (fileExist) {
							rtnHtml += '<div class="mch type_text text_top">';
						} else {
							rtnHtml += '<div class="mch type_text">';
						}
						rtnHtml += section.data.replace(/\n/g, '<br/>');
						rtnHtml += '</div>';
					}
				}
				else if (section.type === MESSAGE_SECTION_TYPE.ACTION) {
					rtnHtml += '<ul class="btn-wrap">';
					$.each(section.actions, function(idx2, action) {
						if (action.type === MESSAGE_ACTION_TYPE.LINK) {
							rtnHtml += '<li><a href="' + action.data + '" target="_blank" class="btn-bn-list-icon" title="새창열림">'
								+ action.name + '</a></li>';
						}
					});
					rtnHtml += '</ul>';
				}
			});
			rtnHtml += '</div></article></div>';

			return rtnHtml;
		}

		var previewImageData;
		function imgPreviewFn(imgInput){
			if(imgInput.files && imgInput.files[0]){
				var fileRead = new FileReader();
				fileRead.onload = function(e){
					previewImageData = e.target.result;
				}

				fileRead.readAsDataURL(imgInput.files[0]);
			}
		}

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
			<form id="templateForm" name="templateForm" method="post" action="">
				<input type="hidden" name="schTplCtgNum" />
				<input type="hidden" name="tplNum" />
				<input type="hidden" name="shareReqNum" />
				<input type="hidden" name="oldOrgImgDelYn" />
				<input type="hidden" name="oldTplImgUrl" />
				<input type="hidden" name="oldOrgImgNm" />
				<input type="hidden" name="oldOrgImgPath" />				
				<input type="hidden" name="cnsFrtMsgImg" />				
				<input type="hidden" name="oldOrgPdfDelYn" />
				<input type="hidden" name="oldMimeType" />
				<input type="hidden" name="oldOrgPdfNm" />
				<input type="hidden" name="oldOrgPdfPath" />
				
				<input type="hidden" name="tplPageType" value="G" />

				<div class="left_content_head">
					<h2 class="sub_tit">템플릿 관리</h2>
					<!-- <button type="button" class="btn_go_help">도움말</button> -->
					<span class="left_top_text">상담시 유용하게 사용할 수 있도록 만들어 놓은 질문/답변입니다.</span>
				</div>
				<div class="inner_nobg">
					<div class="box_cont">
						<div class="box_head_area">
							<h3 class="sub_stit">템플릿 카테고리</h3>
							<button type="button" class="btn_plus_temp dev_add_category"><i></i>추가</button>
						</div>
						<c:forEach var="data" items="${categoryList }" varStatus="status">
							<div class="temp_basic_setting" data-tpl_ctg_num="${data.tpl_ctg_num }">
								<span><c:out value="${data.tpl_ctg_nm }"/></span>
								<button type="button" class="btn_write_temp dev_edit_category" data-tpl_ctg_num="${data.tpl_ctg_num }" data-tpl_ctg_nm="${data.tpl_ctg_nm }"><i>수정</i></button>
								<button type="button" class="btn_del_temp dev_del_category" data-tpl_ctg_num="${data.tpl_ctg_num }" data-tpl_ctg_nm="${data.tpl_ctg_nm }"><i>삭제</i></button>
							</div>
						</c:forEach>
					</div>
					<!-- 왼쪽 컨텐츠 -->
					<div class="box_cont_left">
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">
									템플릿
									<span class="text_sinfo">카테고리를 선택해서 리스트를 확인하세요.</span>
								</h3>
							</div>
							<div class="box_body">
								<div class="category_setting">
									<c:forEach var="data" items="${categoryList }" varStatus="status">
										<button type="button" class="btn_category dev_tpl_ctg_sch_btn" data-tpl_ctg_num="${data.tpl_ctg_num }">${data.tpl_ctg_nm }</button>
									</c:forEach>
									<button type="button" class="btn_category dev_tpl_ctg_sch_btn" data-tpl_ctg_num="">전체</button>
									<div class="category_radio_area">
										<!-- <input type="radio" class="radio_18" id="category01" name="schDeviceType" value="ANDROID">
										<label for="category01"><span></span>안드로이드</label>
										<input type="radio" class="radio_18" id="category02" name="schDeviceType" value="IPHONE">
										<label for="category02"><span></span>아이폰</label>
										<input type="radio" class="radio_18" id="category03" name="schDeviceType" value="WEB" checked>
										<label for="category03"><span></span>웹</label> -->
									</div>
								</div>
								<div class="search_area temp">
									<select name="schType">
										<option value="ANSWER">답변</option>
										<option value="ID">상담직원</option>
										<option value="QUESTION">질문</option>
										<option value="KEYWORD">키워드</option>
									</select>
									<div class="search_area_right">
										<input type="text" id="schText" name="schText"><button type="button" class="btn_search dev_search_btn">검색하기</button>
									</div>
								</div>
							</div>
							<div class="box_body_gray chat_temp_setting dev_template_list dev_template_check">
							</div>
						</div>
					</div>
					<!--// 왼쪽 컨텐츠 -->

					<!-- 오른쪽 컨텐츠 -->
					<div class="box_cont_right">
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">템플릿 등록/수정</h3><div class="right-chat-wrap" style="margin-bottom:0;"><button type="button" class="btn_plus_temp02 dev_template_clean"><i></i>템플릿 추가</button></div>
							</div>
							<div class="box_body">
								<table class="tCont setting">
									<caption>상담 템플릿 등록/수정 폼입니다.</caption>
									<colgroup>
										<col style="width:20%">
										<col style="width:80%">
									</colgroup>
									<tbody>
										<tr>
											<th>카테고리</th>
											<td class="textL">
												<select name="tplCtgNum">
													<c:choose>
														<c:when test="${empty categoryList }">
															<option value="">카테고리가 없습니다.</option>
														</c:when>
														<c:otherwise>
															<option value="">전체</option>
															<c:forEach var="data" items="${categoryList }" varStatus="status">
																<option value="${data.tpl_ctg_num }">${data.tpl_ctg_nm }</option>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</select>
												<div class="category_check_area">
													<!-- <input type="checkbox" class="checkbox_18" id="androidYn" name="androidYn" value="Y" checked>
													<label for="androidYn"><span></span>안드로이드</label>
													<input type="checkbox" class="checkbox_18" id="iphoneYn" name="iphoneYn" value="Y" checked>
													<label for="iphoneYn"><span></span>아이폰</label>
													<input type="checkbox" class="checkbox_18" id="webYn" name="webYn" value="Y" checked>
													<label for="webYn"><span></span>웹</label> -->
													<input type="hidden" name="androidYn" value="Y" >
													<input type="hidden" name="iphoneYn" value="Y" >
													<input type="hidden" name="webYn" value="Y">
													
												</div>
											</td>
										</tr>
										<tr>
											<th>구분</th>
											<td class="textL">
												<select id="tplMsgDivCd" name="tplMsgDivCd">
													<c:forEach var="data" items="${tplMsgDivCdList }" varStatus="status">
														<option value="${data.cd }">${data.cd_nm }</option>
													</c:forEach>
												</select>
												<span class="icon_info_text" title="- Text : 글, <br/>- Normal : 글, 이미지, 링크 템플릿을 구성합니다." data-html="true"></span>
											</td>
										</tr>
										<tr>
											<td class="textL" colspan="2">
												<p class="form_tit">고객질문</p>
												<textarea placeholder="100자 이내로 질문 내용을 작성해주세요." class="form_text" name="cstmQue" maxlength="100"></textarea>
											</td>
										</tr>
										<tr>
											<td class="textL" colspan="2">
												<p class="form_tit">상담직원 답변</p>
												<textarea placeholder="1000자 이내로 답변 내용을 작성해주세요." class="form_text" name="replyContText" maxlength="1000"></textarea>
											</td>
										</tr>
										<!-- normal 선택시 -->
										<tr id="tplNormal">
											<td class="textL" colspan="2">
												<p class="form_tit">이미지 (이미지 타입: jpg/png/gif, 최대 2MB)</p>
												<div class="filebox">
													<label for="ex_filename01">파일불러오기</label>
													<a href=#" onclick="javascript:fn_fileView();"><input disabled="disabled" class="upload-name dev_file_view_name" value=""></a>
													<input class="upload-hidden dev_file" id="ex_filename01" type="file" name="tplImgUrl" onchange="imgPreviewFn(this)">
													<button type="button" class="btn_del dev_file_del_btn">삭제</button><!--삭제버튼:불러온 파일명 바로 옆에 붙게 해주세요 -->
												</div>
												<ul class="file_img">
													<li>
														<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
														<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
														<button type="button" class="btn_del dev_btn_link_del">삭제</button>
													</li>
													<li>
														<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
														<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
														<button type="button" class="btn_del dev_btn_link_del">삭제</button>
													</li>
													<li>
														<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
														<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
														<button type="button" class="btn_del dev_btn_link_del">삭제</button>
													</li>
													<li>
														<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
														<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
														<button type="button" class="btn_del dev_btn_link_del">삭제</button>
													</li>
												</ul>
												<p class="form_tit">파일 첨부 (pdf, 최대 2MB)</p>
												<div class="filebox">
													<label for="ex_filename02">파일불러오기</label>
													<a href=#"><input disabled="disabled" class="upload-name dev_file_pdf_view_name" value=""></a>
													<input class="upload-hidden dev_file_pdf" id="ex_filename02" type="file" name="tplPdfUrl">
													<button type="button" class="btn_del dev_file_pdf_del_btn">삭제</button><!--삭제버튼:불러온 파일명 바로 옆에 붙게 해주세요 -->
													<input type="text" style="width:100%;" placeholder="다운로드 버튼명(최대 18자)" maxlength="18" name="tplPdfBtn">
												</div>												
											</td>
										</tr>
										<!--// normal 선택시 -->
										<tr>
											<td class="textL" colspan="2">
												<p class="form_tit">키워드<span class="icon_info_text" title="키워드에 등록된 단어는 채팅창에 노란색으로 표시되며,<br> 채팅도중 채팅창의 키워드를 클릭하면 해당 템플릿 리스트를 볼 수 있습니다.<br>입력은 최대 40자(공백, 특수문자포함)까지 지정가능합니다." data-html="true"></span></p>
												<input type="text" name="kwdNmArr" class="form_text" placeholder="쉼표(,)로 구분되며 공백과 특수문자 포함 최대 100자까지 입력 가능" maxlength="100"/>
											</td>
										</tr>
									</tbody>
								</table>
								<div class="temp_checkbox_area">
									<c:if test="${memberDivCd ne 'S' }">
										<input type="checkbox" class="checkbox_18" id="chat_info01" name="tplDivCd" value="G"
										<c:if test="${tplDivCd eq 'G'}">checked style="cursor:none;"</c:if>									>
										<label for="chat_info01"><span></span>다른 상담직원과 공유</label>
									</c:if>
									<input type="checkbox" class="checkbox_18" id="chat_info02" name="topMarkYn" value="Y">
									<label for="chat_info02"><span></span><i class="icon_check_top"></i>최상단 노출</label>
									<input type="hidden" name="ieInput" /><!-- ie일 경우 ajax file form submit시 마지막에 체크박스가 체크되지 않으면 오류 발생. -->
								</div>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_temp_preview" onclick="onPreviewPop(); return false;">미리보기</button>
								<button type="button" class="btn_temp_insert dev_save_btn"><i></i>저장</button>
							</div>
						</div>
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">
									등록 대기 중인 템플릿
									<span class="text_sinfo">상담직원이 신청한 템플릿을 보여줍니다.</span>
								</h3>
							</div>
							<div class="box_body_gray chat_temp_setting dev_confirm_tpl_list dev_template_check">
							</div>
						</div>
					</div>
					<!--// 오른쪽 컨텐츠 -->
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>


<!-- popup:카테고리 추가/수정 -->
<form id="addPopForm" name="addPopForm" method="post" action="">
	<div class="popup popup_temp_category dev_category_pop">
		<div class="inner">
			<div class="popup_head">
				<h1 class="popup_tit">카테고리 추가</h1>
				<button type="button" class="btn_popup_close">창닫기</button>
			</div>
			<div class="popup_body">
				<div class="popup_form_area">
					<span class="tit">카테고리명</span>
					<input type="text" name="tplCtgNm" maxlength="7">
					<input type="hidden" name="tplCtgNum">
					<input type="hidden" name="commonCtgYn" value="Y">
				</div>
				<!-- 카테고리 추가시 -->
				<p class="popup_text_bottom">
					* 최대 한글 7글자 이내 작성 가능합니다.<br>
				    * 카테고리는 최대 7개까지 추가 가능합니다.
				</p>
				<!--// 카테고리 추가시 -->

				<div class="btn_popup_area">
					<button type="button" class="btn_popup_ok dev_btn_save_pop">저장</button>
					<button type="button" class="btn_popup_ok btn_close_pop">취소</button>
				</div>
			</div>
		</div>
	</div>
</form>
<!--// popup:카테고리 추가/수정 -->

<!-- popup:카테고리 삭제 -->
<form id="delPopForm" name="delPopForm" method="post" action="">
	<div class="popup popup_temp_del dev_category_del_pop">
		<div class="inner">
			<div class="popup_head">
				<h1 class="popup_tit">카테고리 삭제</h1>
				<button type="button" class="btn_popup_close">창닫기</button>
			</div>
			<div class="popup_body">
				<div class="popup_form_area">
					<span class="tit">카테고리명</span>
					<input type="text" name="tplCtgNm" maxlength="7" readonly="readonly">
					<input type="hidden" name="tplCtgNum">
				</div>

				<!-- 카테고리 삭제시 -->
				<p class="popup_text_bottom del">
					* <b>카테고리에 등록된 템플릿이 <span id="tplCount"></span>개 있습니다.</b><br>
				    * 카테고리 삭제 시 해당 카테고리에 등록된 템플릿은 모두 삭제됩니다.<br>
					  <span class="marginL10">삭제처리 하시겠습니까?</span>
				</p>
				<!--// 카테고리 삭제시 -->

				<div class="btn_popup_area">
					<button type="button" class="btn_popup_ok dev_btn_del_pop">삭제</button>
					<button type="button" class="btn_popup_ok btn_close_pop">취소</button>
				</div>
			</div>
		</div>
	</div>
</form>
<form id="excelForm" name="excelForm" method="post" action="<c:url value='/template/downloadTplExcel' />" style="display:none;">
</form> 
<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

<!-- layer:자세히보기 -->
<div class="poopup_chat_detail dev_detail_pop" style="display:none;">
	<div class="inner">
		<div class="popup_head">
			<h2 class="popup_title dev_detail_pop_title"></h2>
			<button type="button" class="btn_close_detail dev_detail_pop_close">창닫기</button>
		</div>
		<div class="popup_body dev_detail_pop_cont">
			<div class="detail_contents"></div>
			<button type="button" class="btn_text_copy">복사하기</button>
		</div>
	</div>
</div>
<!-- layer:자세히보기 -->

<!-- popup:미리보기 -->
<div id="templatePreviewDiv" class="popup popup_temp_category" style="display: none;">
	<div class="inner2">
		<div class="popup_head">
			<h1 class="popup_tit">템플릿 미리보기</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<div class="box_body_gray chat_temp_setting previewTemplateDiv dev_template_check"></div>

			<div class="btn_popup_area">
				<button type="button" onclick="templatePreviewClose(); return false;" class="btn_popup_ok">닫기</button>
			</div>
		</div>
	</div>
</div>
<!--// popup:미리보기 -->

<c:if test="${!isProduction}">
<form id="debug" style="display: none;">
	<input type="hidden" name="profile" value="false" />
</form>
</c:if>
</body>
</html>
