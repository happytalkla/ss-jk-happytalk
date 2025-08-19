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
	function fn_save() {

		if($('#content').val() === '') {
			alert('자동완성 메세지를 입력해 주세요');
			return false;
		}
		if( ($('#content').val().length + ($('#content').val().split('\n').length * 2)) > 1000){
			alert('자동완성 메세지는 1000자 이하(공백 1자, 한글 2자, 줄바꿈 2자)만 저장 가능합니다. 현재 : ' + ($('#content').val().length + ($('#content').val().split('\n').length * 2)) + "자");
			return false;
		}

		var autoCmpDiv = $("#autoCmpSaveForm [name=autoCmpDiv]").val();

		$.ajax({
			url: "<c:url value='/autoCmp/saveAutoCmp' />",
			data: $("#autoCmpSaveForm").serialize(),
			type: "post",
			success: function(result) {
				if (result.returnCode === 'SUCCEED') {
					$('#autoCmpForm').submit();
				} else {
					fn_layerMessage(result.returnMessage);
				}
			},
			complete : function() {
			}
		});
	}

	function fn_delete(autoCmpId) {
		if (!confirm(name + " 등록된 자동완성 메세지를 삭제하시겠습니까?")) {
			return false;
		}

		$("#autoCmpForm [name=autoCmpId]").val(autoCmpId);
		$.ajax({
			url : "<c:url value='/autoCmp/deleteAutoCmp' />",
			data : {
				autoCmpId: autoCmpId
			},
			type : "post"
		}).done(function(result) {
			$('#autoCmpForm').submit();
			//location.reload();
		}).fail(function() {
			fn_layerMessage(result.returnMessage);
		});
	}

	function fn_edit(autoCmpId, autoCmpDiv, autoCmpCus) {

		if (autoCmpId > 0) {
			var $target = $('.chat_temp_list li[data-id=' + autoCmpId + ']');
			if ($target.length === 1) {
				$('#autoCmpSaveForm [name=autoCmpId]').val(autoCmpId);
				$('#autoCmpSaveForm [name=autoCmpDiv][value=' + autoCmpDiv + ']').prop('checked', true);
				if (autoCmpCus === 'P' && autoCmpDiv === 'C') {
					$('#autoCmpSaveForm [name=autoCmpCus]').val('P');
				} else {
					$('#autoCmpSaveForm [name=autoCmpCus]').val('C');
				}

				var memberUid = $target.data('member-uid');
				$('#autoCmpSaveForm [name=memberUid]').val(memberUid);

				var $content = $('.inner_text', $target).html().trim();
				var content = $content.replace(/<br\s*[\/]?>/gi ,'');
				$('#autoCmpSaveForm #content').val(content);
			} else {
				console.error('NOT FOUND AUTO COMPLETE MESSAGE', autoCmpId);
			}
		}
	}

	$(document).ready(function() {
		$('#schText').focus();
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
				<div class="left_content_head">
					<h2 class="sub_tit">자동완성 관리</h2>
				</div>
				<div class="inner_nobg">

					<!-- 왼쪽 컨텐츠 -->
					<div class="box_cont_left">
					<form id="autoCmpForm" name="autoCmpForm" method="post" action="<c:url value='/autoCmp/selectAutoCmpList' />">
		 				<input type="hidden" name="departCd" value="${departCd}" />
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">
									자동완성 관리
								</h3>
							</div>
							<div class="box_body">
								<div class="category_setting">
									<div class="category_radio_area">
										<input type="radio" class="radio_18" id="autoCmpDivC" name="autoCmpDiv" value="C"
											<c:if test="${autoCmpDiv == 'C'}">checked="checked"</c:if> />
										<label for="autoCmpDivC"><span></span>답변용</label>
										<input type="radio" class="radio_18" id="autoCmpDivA" name="autoCmpDiv" value="A"
											<c:if test="${autoCmpDiv == 'A'}">checked="checked"</c:if> />
										<label for="autoCmpDivA"><span></span>후처리용</label>
									</div>
								</div>
								<div class="temp_list_area">
								</div>
								<div class="search_area temp">
									<select name="autoCmpCus" id="autoCmpCus">
										<option value="">전체</option>
										<option value="C"<c:if test="${autoCmpCus == 'C'}"> selected="selected"</c:if>>공용</option>
										<option value="P"<c:if test="${autoCmpCus == 'P'}"> selected="selected"</c:if>>개인</option>
									</select>
									<div class="search_area_right">
										<input type="text" id="schText" name="schText" value="${schText}" />
										<button type="submit" class="btn_search dev_search_btn">검색하기</button>
									</div>
								</div>
							</div>
							<div class="box_body_gray chat_temp_setting dev_template_list dev_template_check">
							 	<c:if test="${fn:length(resultList) > 0}">
								<div id="chat_body" style="max-height:900px;">
									<ul class="chat_temp_list">
										<c:forEach var="result" items="${resultList}" varStatus="status">
										<li data-id="${result.auto_cmp_id}" data-member-uid="${result.member_uid}">
											<div class="left_area">
											<c:if test="${result.auto_cmp_cus eq 'P'}">
												<i class="icon_individual customer">고객</i>
											</c:if>
											<c:if test="${result.auto_cmp_cus ne 'P'}">
												<i class="icon_bot customer">고객</i>
											</c:if>
												<div class="top_write_info">
													<span>ID: ${result.auto_cmp_id}</span>
													<span>등록일: ${result.create_date}</span>
													<span>대상: ${result.auto_cmp_div_nm}, ${result.auto_cmp_cus_nm}</span>
													<span>등록자: ${result.name}</span>
												</div>
												<div class="text_box">
													<div class="inner_text">
														${fn:replace(result.content, newLineChar, '<br/>')}
													</div>
												</div>
											</div>
											<div class="btn_temp_area">
												<button type="button" class="btn_write_temp dev_tpl_edit_btn template"
													onclick="javascript:fn_edit(${result.auto_cmp_id}, '${result.auto_cmp_div}', '${result.auto_cmp_cus}');"><i>쓰기</i></button>
												<button type="button" class="btn_del_temp dev_tpl_del_btn template"
													onclick="javascript:fn_delete(${result.auto_cmp_id});"><i>삭제</i></button>
											</div>
										</li>
							 			</c:forEach>
									</ul>
								</div>
								</c:if>
							 	<c:if test="${fn:length(resultList) == 0}">
								<p class="no_data">목록이 없습니다.</p>
							 	</c:if>

							</div>
						</div>
					</form>
					</div>
					<!--// 왼쪽 컨텐츠 -->

					<!-- 오른쪽 컨텐츠 -->
					<div class="box_cont_right">
					<form id="autoCmpSaveForm" name="autoCmpSaveForm" method="post" action="">
		 				<input type="hidden" name="autoCmpId" />
		 				<input type="hidden" name="autoCmpCus" value="C" />
		 				<input type="hidden" name="memberUid" value="${member.memberUid}" />
		 				<input type="hidden" name="departCd" value="${member.departCd}" />
						<div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">자동완성 등록/수정</h3>
							</div>
							<div class="box_body">
								<div class="category_setting">
									<div class="category_radio_area">
										<input type="radio" class="radio_18" id="autoCmpDivSaveC" name="autoCmpDiv" value="C" checked="checked">
										<label for="autoCmpDivSaveC"><span></span>답변용</label>
										<input type="radio" class="radio_18" id="autoCmpDivSaveA" name="autoCmpDiv" value="A">
										<label for="autoCmpDivSaveA"><span></span>후처리용</label>
									</div>
								</div>
								<div class="temp_list_area">
								</div>
								<div class="search_area temp">
									<textarea name="content" id="content" style="width:98%; height:100px" maxlength="1000"></textarea>
								</div>
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_temp_insert dev_save_btn" onclick="javascript:fn_save();"><i></i>저장</button>
								<button type="button" class="btn_temp_insert dev_cancel" onclick="javascript:location.reload();"><i></i>취소</button>
							</div>
						</div>
					</form>
					</div>
					<!--// 오른쪽 컨텐츠 -->
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
