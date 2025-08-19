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
		var edit='N'; 
		var index=0; 
		$(document).ready(function() {
			// 전체 메세지 글자수 셋팅
			$("#happyBotForm .dev-happyBotCheckClass").each(function(){
				$(this).trigger("keyup");
			});
			var bot_org_file_nm ="${messageSet.bot_org_file_nm}";
			if (bot_org_file_nm =='') {
				$(".dev_file_del_btn").hide();
			}

		});

		// on, off 버튼 클릭
		$(document).on("change", "#happyBotForm .dev-switch-input", function() {
			var cnsPossibleYn;
			if($(this).prop("checked")){
				cnsPossibleYn = $(this).attr("data-on-value");
			}else{
				cnsPossibleYn = $(this).attr("data-off-value");
			}

			$(this).closest("label").find(".dev-switch-input-value").val(cnsPossibleYn);
		});

		// 메세지 글자수 체크
		$(document).on("keyup", "#happyBotForm .dev-happyBotCheckClass", function() {
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
		$(document).on("click", "#happyBotForm [name='updateBtn']", function() {
			var selBtn = $(this).attr("data-btn");
			var objMsg = $(this).closest("div").find(".dev-happyBotCheckClass");

			if($(this).closest("div").find("select option:selected").val() == "Y"){
				if(objMsg.val().length < 5){
					alert("메세지는 5자 이상 입력해 주세요.");
					objMsg.focus();
					return false;
				}
			}


			$("#happyBotForm [name='selBtn']").val(selBtn);

			$.ajax({
				url : "<c:url value='/set/updateHappyBot' />",
				data : $("#happyBotForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						if (selBtn=='scenario') {
							fn_goSearch();
						}
						
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
					$("#happyBotForm [name='selBtn']").val("");
				}
			});
		});

		// 인사말 이미지 수정
		$(document).on("change", "#happyBotForm [name='botFrtMsgImgTmp']", function() {
			var form = $("#happyBotForm")[0];
			var formData = new FormData(form);

			$.ajax({
				url : "<c:url value='/set/updateHappyBotImg' />",
				processData : false,
				contentType : false,
				data : formData,
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
							// 성공
							$("#happyBotForm [name='botFrtMsgImg']").val(result.botFrtMsgImgUrl);
							console.log ("111 " + result.botFrtMsgImgUrl);
							//$(this).siblings('.dev_file_view_name').val(result.fileNm);
							$("#happyBotForm .dev_file_view_name").val(result.fileNm);
							$("#happyBotForm [name='botOrgFileNm']").val(result.fileNm);
							$("#happyBotForm .dev_file_del_btn").show();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 이미지 삭제
		$(document).on("click", "#happyBotForm .dev_file_del_btn", function(){
			//$(this).siblings(".dev_file").val("");
			//$(this).siblings('#happyBotForm .dev_file_view_name').val("");
			$('.dev_file_view_name').val("");
			$("#happyBotForm [name='botFrtMsgImg']").val("");
			$("#happyBotForm [name='botOrgFileNm']").val("");
			
			//$("#happyBotForm [name='oldOrgImgDelYn']").val("Y");
			$(this).hide();
		});

		//이미지 보기  
		function fn_fileView() {
			var botImgUrl = $("#happyBotForm [name='botFrtMsgImg']").val();
			if (botImgUrl=='') {
				return false;
			}
			//alert (msgImgUrl);
			//location.href = msgImgUrl;
			window.open(botImgUrl);
		}

		// 회원 수정 팝업창 닫기
		$(document).on("click", ".dev_close_pop", function(e) {
			$(".dev_member_pop").hide();
		});

		// 회원 수정 팝업창 닫기
		$(document).on("click", ".dev_cancel_pop", function(e) {
			$(".dev_member_pop").hide();
		});

		//추가
		$(document).on("click", "#happyBotForm .dev_add_btn", function() {
			$(".dev_member_pop").show();
			$("#happyBotForm [name='title'] ").val('');
			edit='N';
		});

		
		//편집
		$(document).on("click", "#happyBotForm .dev_edit_btn", function() {
			//alert($(".dev_edit_btn").index(this));
			index = $(".dev_edit_btn").index($(this));
			$(".dev_member_pop").show();
			var title=$(this).closest("tr").find("td:eq(1)").text();
			$("#happyBotForm [name='title']").val(title);
			edit='Y';
		});
		//삭제
		$(document).on("click", "#happyBotForm .dev_del_btn", function() {
			//debugger;
			var delIndex=$(".dev_del_btn").index($(this));
			$(".dev_category_table > tbody:last > tr:eq("+delIndex+")").remove();

		});
		//복사
		$(document).on("click", "#happyBotForm .dev_copy_btn", function() {
			var copyIndex=$(".dev_copy_btn").index($(this));
			var htmlTag=$(".dev_category_table > tbody > tr:eq("+copyIndex+")").html();
			$(".dev_category_table > tbody:last").append("<tr>"+htmlTag+"</tr>");
			var leng=$(".dev_category_table tr").length;
			leng= leng-2;
			var no=$(".dev_category_table tr:eq("+leng+")").find("td:eq(0)").text();
			var nextNo = Number(no) + 1;	
			leng= leng+1;
			$(".dev_category_table tr:eq("+leng+")").find("td:eq(0)").text(nextNo);	
			//$(".dev_category_table tr:eq("+leng+")").find("[name='nameArr']").val(title);	
			
		});		

		//확인
		$(document).on("click", ".dev_save_pop", function() {
			var title=$("input[name='title']").val();
			var ind= Number(index)+1;
			if (edit=='Y') {
				$(".dev_category_table tr:eq("+ind+")").find("td:eq(1)").text(title);
				$(".dev_category_table tr:eq("+ind+")").find("[name='nameArr']").val(title);
				
				//$(".dev_category_table tr:eq(0)").text();
			}else {
				var leng=$(".dev_category_table tr").length;
				leng= leng-1;
				var no=$(".dev_category_table tr:eq("+leng+")").find("td:eq(0)").text();
				var nextNo = Number(no) + 1;
				var htmlTag='';
				htmlTag+='		<td>'+nextNo+'</td>                                                                                                                      \n'; 
				htmlTag+='		<td>'+title+'</td>                                                                                                              \n'; 
				htmlTag+='		<td>								                                                                                            \n'; 
				htmlTag+='			<label class="switch w90">                                                                                                  \n'; 
				htmlTag+='				<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N">                      \n'; 
				htmlTag+='				<span class="switch-label" data-on="사용" data-off="미사용"></span>                                                     \n'; 
				htmlTag+='				<span class="switch-handle"></span>                                                                                     \n'; 
				htmlTag+='				<input type="hidden" name="useYnArr" class="dev-switch-input-value" value="">                                             \n'; 
				htmlTag+='			</label>                                                                                                                    \n'; 
				htmlTag+='		</td>                                                                                                                           \n'; 
				htmlTag+='		<td></td>                                                                                                                       \n'; 
				htmlTag+='		<td>                                                                                                                            \n'; 
				htmlTag+='			<button type="button" class="btn_view_day dev_edit_btn" style="width:50px; height:25px;">편집</button>                      \n'; 
				htmlTag+='			<button type="button" class="btn_view_day dev_del_btn" style="width:50px; height:25px; margin-left: 1px;">삭제</button>     \n'; 
				htmlTag+='			<button type="button" class="btn_view_day dev_copy_btn" style="width:50px; height:25px; margin-left: 1px;">복사</button>	\n';											
				htmlTag+='			<input type="hidden" name="nameArr" value="'+title+'">                                                                         \n'; 
				htmlTag+='			<input type="hidden" name="modifiedArr" value="">                                                                              \n'; 
				htmlTag+='			<input type="hidden" name="modifierArr" value="">	                                                                            \n'; 
				htmlTag+='		</td>                                                                                                                           \n'; 
				$(".dev_category_table > tbody:last").append("<tr>"+htmlTag+"</tr>");		

			}
			$(".dev_member_pop").hide();
		});		

		function fn_goSearch(){
			$("#happyBotForm").attr("action", "<c:url value='/set/selectHappyBot' />");
			$("#happyBotForm").submit();
		}
		

	</script>

</head>


<body id="admin">
	<form id="happyBotForm" name="happyBotForm" method="post" action="<c:url value='/set/updateHappyBot' />">
		<input type="hidden" name="siteId" value="${messageSet.site_id }" />
		<input type="hidden" name="selBtn" value="" />
		<input type="hidden" name="selBtn" value="" />
		<input type="hidden" name="botOrgFileNm" value="${messageSet.bot_org_file_nm}" />		
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
					<h2 class="sub_tit">해피봇 시나리오 관리</h2> 
					<!-- <button type="button" class="btn_go_help">도움말</button>  -->
				</div>
				<div class="inner_nobg">
					<!-- 왼쪽 컨텐츠 -->
					<div class="box_cont_left">
						<div class="box_cont tm_disabled">
							<div class="box_head_area">
								<h3 class="sub_stit">상담 인사말</h3>
								<!-- 
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${happyBotSet.first_msg_text_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="firstMsgTextUseYn" class="dev-switch-input-value" value="${happyBotSet.first_msg_text_use_yn }">
								</label>
								 -->
							</div>
							<div class="box_body">
								<p class="text_info02">해피봇 시작 인사말을 구성합니다.<br>
								웹채팅, 네이버톡톡 등으로 챗봇 상담 시작 시 가장 먼저 보여질 채팅 내용입니다.
								</p>
								<div class="form_textarea">
									<textarea name="botFrtMsg" class="dev-happyBotCheckClass" rows="5" cols="100">${messageSet.bot_frt_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">100</span>
								</div>
								<div class="filebox">
									<label for="ex_filename01">이미지첨부</label>
									<input type="hidden" name="botFrtMsgImg" value="${messageSet.bot_frt_msg_img}">
									<a href=#" onclick="javascript:fn_fileView();"><input disabled="disabled" class="upload-name dev_file_view_name" value="${messageSet.bot_org_file_nm}"  ></a>
									<input class="upload-hidden dev_file" id="ex_filename01" type="file" name="botFrtMsgImgTmp">
									<button type="button" class="btn_del dev_file_del_btn">삭제</button><!--삭제버튼:불러온 파일명 바로 옆에 붙게 해주세요 -->
								</div>	
								
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="botFrtMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
						<div class="box_cont tm_disabled">
							<div class="box_head_area">
								<h3 class="sub_stit">고객 입력 오류 메세지</h3>
								<%--
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${happyBotSet.unsocial_msg_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="unsocialMsgUseYn" class="dev-switch-input-value" value="${happyBotSet.unsocial_msg_use_yn }">
								</label>								
 								--%>

							</div>
							<div class="box_body">
								<p class="text_info02">고객이 메세지 입력 또는 버튼 등을 잘못 선택 시 챗봇이 발송하는 메세지입니다.<br>
								챗봇 메세지 발송 후 이전 시나리오도 같이 노출됩니다.
								</p>							
								<div class="form_textarea">
									<textarea name="botCstmErrMsg" class="dev-happyBotCheckClass" rows="5" cols="100">${messageSet.bot_cstm_err_msg}</textarea>
									<span class="text_count" name="msgLen">0</span>자 / <span class="text_count" name="maxLen">100</span>
								</div><!--
								<p class="text_info">* 시스템 호출 단어를 고객이 입력 시 정해진 프로세스를 실행합니다.<br>
								* &처음 : 처음 시나리오를 강제 호출 | &종료 : 챗봇 가동 종료</p>-->
								<!-- <p class="text_info">* 해당 메세지는 카카오 상담톡에는 적용되지 않습니다.</p> -->
							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="botCstmErrMsg" name="updateBtn"><i></i>적용</button>
							</div>
						</div>

					</div>
					<!--// 왼쪽 컨텐츠 -->

					<!-- 오른쪽 컨텐츠 -->
					<div class="box_cont_right">

						 <div class="box_cont">
							<div class="box_head_area">
								<h3 class="sub_stit">시나리오 리스트</h3>
								<span style="float:right;">
									<button type="button" class="btn_plus_temp dev_add_btn"><i></i>추가</button>
								</span>
<%--
								<label class="switch w90">
									<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${happyBotSet.not_cns_msg_use_yn eq 'Y' }">checked</c:if> >
									<span class="switch-label" data-on="사용" data-off="미사용"></span>
									<span class="switch-handle"></span>
									<input type="hidden" name="notCnsMsgUseYn" class="dev-switch-input-value" value="${happyBotSet.not_cns_msg_use_yn }">
								</label>
 --%>


							</div>
							<div class="box_body">
 								<p class="text_info02">해피봇 시작 인사말을 구성합니다.<br>
 								웹채팅, 네이버톡톡 등으로 챗봇 상담 시작 시 가장 먼저 보여질 채팅 내용입니다.</p>							
								<div class="form_textarea">
									<table class="tCont dev_category_table" name="notCnsMsg" style="margin-top:20px;" >
									<caption></caption>
										<colgroup>
											<col style="width:10%">
											<col style="width:28%">
											<col style="width:12%">
											<col style="width:22%">	
											<col style="width:28%">
										</colgroup>
										<thead>
											<tr>
												<th >NO</th>
												<th>시나리오명</th>
												<th >사용여부</th>
												<th>등록일</th>
												<th >실행</th>
									
											</tr>
										</thead>
										<tbody>
										
										<c:forEach var="data" items="${happyBotSetList }" varStatus="status">
											<tr>
												<td >${status.count }</td>
												<td>${data.name}</td>
												<td>								
													<label class="switch w90">
														<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" <c:if test="${data.use_yn eq 'Y' }">checked</c:if> >
														<span class="switch-label" data-on="사용" data-off="미사용"></span>
														<span class="switch-handle"></span>
														<input type="hidden" name="useYnArr" class="dev-switch-input-value" value="${data.use_yn }">
													</label>
												</td>
												<td>${data.modified}</td>
												<td >
													<button type="button" class="btn_view_day dev_edit_btn" style="width:50px; height:25px;">편집</button>
													<button type="button" class="btn_view_day dev_del_btn" style="width:50px; height:25px; margin-left: 1px;">삭제</button>
													<button type="button" class="btn_view_day dev_copy_btn" style="width:50px; height:25px; margin-left: 1px;">복사</button>	
													<input type="hidden" name="nameArr" value="${data.name}">
													<input type="hidden" name="modifiedArr" value="${data.modified}">
													<input type="hidden" name="modifierArr" value="${data.modifier}">											
												</td>
											  </tr>
											</c:forEach>	
										</tbody>
									</table>									
								</div>
								<p class="text_info">* 시나리오 "사용"설정할 경우 입력창 상단의 슬라이딩 메뉴 영역에 노출됩니다.<br>
								* "사용"중인 시나리오는 셀을 클릭하여 마우스로 노출 순서를 변경할 수 있습니다.</p>

							</div>
							<div class="box_btn_area">
								<button type="button" class="btn_go_insert" data-btn="scenario" name="updateBtn"><i></i>적용</button>
							</div>
						</div>
					</div>
				</div>

		</div>
		<!--// left_area -->
	</div>
<div class="popup popup_manager dev_member_pop" style="display:none;">
	<div class="inner" style="width: 450px; height: 180px;">
	
		<div class="popup_head">
			<h1 class="popup_tit">시나리오 추가</h1>
			<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
		</div>
		<div class="popup_body">
		<p class="text_info">시나리오명 <input type="text" name="title" style="width:75%;  margin-left: 20px;"></p>
		
		<p class="text_info" style="margin-top: 10px;"><font color="#dc1000">* 시나리오 "사용"시 슬라이딩 메뉴에 시나리오명이 노출됩니다.</font></p>
		<div class="box_btn_area" style="text-align:center">
			<button type="button" class="btn_view_day dev_save_pop" style="width:100px"><i></i>확인</button>
			<button type="button" class="btn_view_day dev_cancel_pop" style="width:100px; margin-left: 10px;"><i></i>취소</button>
		</div>	
		</div>
	
	</div>
</div>
<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->
</form>
</body>
</html>
