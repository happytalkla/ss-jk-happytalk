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
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/common.js' />"></script>
	<script type="text/javascript">

		$(document).ready(function() {
			$(".dev_member_pop").hide();
		});


		// 검색창 엔터 처리
		$(document).on("keypress", "#memberForm [name='schText']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#memberForm .dev_search_btn").trigger("click");
			}
		});

		// 검색하기 클릭
		$(document).on("click", "#memberForm .dev_search_btn", function() {
			/*
			var schText = $("#memberForm [name='schText']").val().trim();
			$("#memberForm [name='schText']").val(schText);
			if(schText == ""){
				alert("검색어를 입력해 주세요.");
				$("#memberForm [name='schText']").focus();
				return false;
			}
			 */
			$("#memberForm [name='nowPage']").val("1");
			goSearch();
		});

		// 이전 클릭
		$(document).on("click", "#memberForm .dev_prev_btn", function() {
			var nowPage = $("#memberForm [name='nowPage']").val()*1 - 1;
			if(nowPage <= 0){
				return false;
			}

			$("#memberForm [name='nowPage']").val(nowPage);
			goSearch();
		});

		// 이후 클릭
		$(document).on("click", "#memberForm .dev_next_btn", function() {
			var nowPage = $("#memberForm [name='nowPage']").val()*1 + 1;
			var totPage = $("#memberForm [name='totPage']").val()*1;
			if(nowPage > totPage){
				return false;
			}

			$("#memberForm [name='nowPage']").val(nowPage);
			goSearch();
		});


		// 페이지 입력창 키 입력 처리
		$(document).on("keypress", "#memberForm [name='inputNowPage']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#memberForm [name='inputNowPage']").trigger("change");
			}
		});

		// 페이지 입력창 입력
		$(document).on("change", "#memberForm [name='inputNowPage']", function() {
			var nowPage = $("#memberForm [name='nowPage']").val();
			var inputNowPage = $(this).val()*1;
			var totPage = $("#memberForm [name='totPage']").val()*1;

			if(!$.isNumeric(inputNowPage)){
				alert("숫자만 입력해 주세요.");
				$("#memberForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage <= 0){
				alert("1페이지 부터 입력해 주세요");
				$("#memberForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			if(inputNowPage > totPage){
				alert("최대 페이지가 "+totPage+"페이지 입니다.");
				$("#memberForm [name='inputNowPage']").val(nowPage);
				return false;
			}

			$("#memberForm [name='nowPage']").val(inputNowPage);
			goSearch();
		});

		// 검색하기
		function goSearch(){
			$("#memberForm").attr("action", "<c:url value='/manage/selectMemberList' />");
			$("#memberForm").submit();
		}


		// 회원 등록 팝업창 열기
		$(document).on("click", "#memberForm .dev_openInsertPop", function(e) {
			var cstmUid = $(this).attr("data-cstmUid");
			
			
			$.ajax({
				url : "<c:url value='/manage/selectMember' />",
				data: {
					rollType: 'S'
				},
				type : "post",
				success : function(result) {
					$(".dev_member_pop").html(result);
					$(".dev_member_pop").show();
					$("#memberPopForm .dev_sch_member_list").hide();
					$(".dev_member_pop .inner").css("height","420px");
					$("#memberPopForm #memberDivCd_C").prop("checked", true);
					fn_changeSelectOption($("#memberPopForm [name='memberDivCd']:checked").val(), 'N');
					$("#memberPopForm [name='pwd']").val('');
				},
				complete : function() {
				}
			});
		});

		// 회원 수정 팝업창 열기
		$(document).on("click", "#memberForm .dev_openEditPop", function(e) {
			var memberUid = $(this).attr("data-memberUid");
			var memberDivCd = $(this).attr("data-memberDivCd");
			
			$.ajax({
				url : "<c:url value='/manage/selectMember' />",
				data : {
					rollType: 'S',
					memberUid: memberUid
				},
				type : "post",
				success : function(result) {
					$(".dev_member_pop").html(result);
					$(".dev_member_pop").show();
					$("#memberPopForm .dev_sch_member_list").hide();
					$(".dev_member_pop .inner").css("height","420px");
					fn_changeSelectOption(memberDivCd, 'Y');
					
					//fn_changeSelectOption($("#memberPopForm [name='firstYn']:checked").val(), 'Y');
				},
				complete : function() {
				}
			});
		});

		// 회원 수정 팝업창 닫기
		$(document).on("click", ".dev_close_pop", function(e) {
			$(".dev_member_pop").hide();
		});

		// 회원 수정 팝업창 닫기
		$(document).on("click", ".dev_cancel_pop", function(e) {
			$(".dev_member_pop").hide();
		});

		//담당매니저 선택
		$(document).on("change", ".select_admin", function(e) {
			var _this = $(this).children("option:selected");
			var departCd = _this.attr("data-depart-cd");
			var departNm = _this.attr("data-depart-nm");
			$("#memberPopForm [name='departCd']").val(departCd);
			$("#memberPopForm [name='departNm']").val(departNm);
			$("#memberPopForm .depart_nm_text").text(departNm);
		});

		$(document).on("change", ".select_depart", function(e) {
			$("#memberPopForm [name='departCd']").val($(this).val());
		});
		
		// 팝업 권한 변경
		$(document).on("change", "#memberPopForm [name='memberDivCd']", function(e) {
			fn_changeSelectOption($("#memberPopForm [name='memberDivCd']:checked").val(), 'N');
		});
		/**************************** 상담 업무 로직 주석처리함()   ****************************/
		function fn_changeSelectOption(memberDivCd, editYn){
			console.log("memberDivCd : "+memberDivCd);
			$("#memberPopForm [name='upperMemberUid'] option").remove();

			if(memberDivCd == "A"){
				// 기본으로 상담 진행 안함 체크
				if(editYn != "Y"){
					//$("#memberPopForm #cnsPossibleYn_N").prop("checked", true);
				}

				//$("#memberPopForm .dev_cns_possible").hide();
				//$("#memberPopForm .dev_cns_possible_text").show();
				//$("#memberPopForm .dev_cns_possible_text").text("상담 업무 안함");
				$("#memberPopForm .trDepartList").hide();
				$("#memberPopForm .trManagerList").hide();
				$("#memberPopForm .workPart").hide();
			}else if(memberDivCd == "S"){
				// 기본으로 상담 진행 체크
				if(editYn != "Y"){
					//$("#memberPopForm #cnsPossibleYn_Y").prop("checked", true);
				}

				//$("#memberPopForm .dev_cns_possible").show();
				//$("#memberPopForm .dev_cns_possible_text").hide();
				//$("#memberPopForm .dev_cns_possible_text").text("");
				$("#memberPopForm .trDepartList").hide();
				$("#memberPopForm .trManagerList").hide();
				$("#memberPopForm .workPart").hide();
			}else if(memberDivCd == "M"){
				// 기본으로 상담 진행 체크
				if(editYn != "Y"){
					//$("#memberPopForm #cnsPossibleYn_Y").prop("checked", true);
				}

				//$("#memberPopForm .dev_cns_possible").show();
				//$("#memberPopForm .dev_cns_possible_text").hide();
				//$("#memberPopForm .dev_cns_possible_text").text("");
				
				$("#memberPopForm [name='departCd']").val("");
				$("#memberPopForm [name='departNm']").val("");		
				$("#memberPopForm .depart_nm_text").text("");

						
				$("#memberPopForm .trManagerList").hide();
				$("#memberPopForm .workPart").hide();
				$("#memberPopForm .trDepartList").show();
				
				var hidDepartCd = $("#memberPopForm [name='hidDepartCd']").val();
				$("#memberPopForm [name='departCd']").empty();
				$("#memberPopForm [name='departCd']").append("<option value='' selected></option>");	//디폴트 값 입력
				$("#departList").children().each(function(i){
					if(hidDepartCd == $(this).val()){				
						$("#memberPopForm [name='departCd']").append("<option value='"+$(this).val()+"' selected>"+$(this).text()+"</option>");
					}else{
						if ($("#memberPopForm .depart_nm_text").text() == "") {
							$("#memberPopForm .depart_nm_text").text($(this).text());
						}
						$("#memberPopForm [name='departCd']").append("<option value='"+$(this).val()+"'>" +$(this).text()+"</option>");
					}
				})
				
				
			}else if(memberDivCd == "C"){
				// 기본으로 상담 진행 체크
				if(editYn != "Y"){
					//$("#memberPopForm #cnsPossibleYn_Y").prop("checked", true);
				}

				//$("#memberPopForm .dev_cns_possible").hide();
				//$("#memberPopForm .dev_cns_possible_text").show();
				//$("#memberPopForm .dev_cns_possible_text").text("상담 진행");

				//managerList
				$("#memberPopForm [name='departCd']").val("");
				$("#memberPopForm [name='departNm']").val("");
				$("#memberPopForm .depart_nm_text").text("");
				
				$("#memberPopForm .trDepartList").hide();
				$("#memberPopForm .workPart").show();
				$("#memberPopForm .trManagerList").show();
				
				var hidUpperMemberUid = $("#memberPopForm [name='hidUpperMemberUid']").val();
				$("#memberPopForm [name='upperMemberUid']").append("<option value='' selected></option>");	//디폴트 값 입력
				$("#managerList").children().each(function(i){
					var departCd = $(this).attr("data-depart-cd");
					var departNm = $(this).attr("data-depart-nm");
					if(hidUpperMemberUid == $(this).val()){
						$("#memberPopForm [name='departCd']").val(departCd);
						$("#memberPopForm [name='departNm']").val(departNm);		
						$("#memberPopForm .depart_nm_text").text(departNm);
						$("#memberPopForm [name='upperMemberUid']").append("<option value='"+$(this).val()+"' data-depart-cd='" + departCd + "' data-depart-nm='" + departNm + "' selected>"+$(this).text()+"</option>");
					} else if ($("#memberPopForm [name='departCd']").val() == null || $("#memberPopForm [name='departCd']").val() == "") {
						$("#memberPopForm [name='departCd']").val(departCd);
						$("#memberPopForm [name='departNm']").val(departNm);
						$("#memberPopForm .depart_nm_text").text(departNm);
						$("#memberPopForm [name='upperMemberUid']").append("<option value='"+$(this).val()+"' data-depart-cd='" + departCd + "' data-depart-nm='" + departNm + "' selected>"+$(this).text()+"</option>");
					} else{
						$("#memberPopForm [name='upperMemberUid']").append("<option value='"+$(this).val()+"' data-depart-cd='" + departCd + "' data-depart-nm='" + departNm + "'>"+$(this).text()+"</option>");
					}
				});		
			}
		}

		// 아이디 중복 체크
		$(document).on("click", "#memberPopForm .dev_check_id_dupl", function(e) {
			var id = $("#memberPopForm [name='id']").val().trim();
			$("#memberPopForm [name='id']").val(id);
			if(id == ""){
				alert("아이디를 입력해 주세요.");
				$("#memberPopForm [name='id']").focus();
				return false;
			}

			$.ajax({
				url : "<c:url value='/manage/checkIdDuplication' />",
				data : {"id" : id},
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$("#memberPopForm [name='idDupliCheck']").val("Y");
					}else{
						fn_layerMessage(result.rtnMsg);
						$("#memberPopForm [name='idDupliCheck']").val("N");
					}
				},
				complete : function() {
				}
			});
		});
		/**
		// 회원 검색
		$(document).on("click", "#memberPopForm .dev_sch_member_btn", function(e) {
			var name = $("#memberPopForm [name='name']").val().trim();
			$("#memberPopForm [name='name']").val(name);
			if(name == ""){
				alert("이름을 입력해 주세요.");
				$("#memberPopForm [name='name']").focus();
				return false;
			}
			if(name.length < 2){
				alert("이름은 2자 이상 입력해 주세요.");
				$("#memberPopForm [name='name']").focus();
				return false;
			}

			$.ajax({
				url : "<c:url value='/manage/selectTmpMemberList' />",
				data : {"name" : name},
				type : "post",
				success : function(result) {
					$("#memberPopForm .dev_sch_member_list").html(result);
					$("#memberPopForm .dev_sch_member_list").show();
					$(".dev_member_pop .inner").css("height","584px");
				},
				complete : function() {
				}
			});
		});
		**/
		// 회원 검색
		$(document).on("keypress", "#memberPopForm [name='name']", function(e) {
			if(e.keyCode == 13){
				e.preventDefault();
				$("#memberPopForm .dev_sch_member_btn").trigger("click");
			}
		});

		// 회원명 변경
		$(document).on("change", "#memberPopForm [name='name']", function(e) {
			$("#memberPopForm [name='coc_id']").val("");
			$("#memberPopForm [name='departCd']").val("");
			$("#memberPopForm [name='departNm']").val("");

			$("#memberPopForm .id_text").text("");
			$("#memberPopForm .departCd_text").text("");
			$("#memberPopForm .workAreaNm").text("");
		});

		// 회원 선택
		$(document).on("click", "#memberPopForm .dev_tmp_member_tr", function(e) {
			var id = $(this).attr("data-id");
			var name = $(this).attr("data-name");
			var departCd = $(this).attr("data-departCd");
			var workAreaNm = $(this).attr("data-work-area-nm");

			$("#memberPopForm [name='coc_id']").val(id);
			$("#memberPopForm [name='name']").val(name);
			$("#memberPopForm [name='departCd']").val(departCd);
			$("#memberPopForm [name='departNm']").val(departNm);

			$("#memberPopForm .id_text").text(id);
			$("#memberPopForm .departCd").text(departCd);
			$("#memberPopForm .workAreaNm").text(workAreaNm);

			$("#memberPopForm .dev_sch_member_list").hide();
			$(".dev_member_pop .inner").css("height","420px");
		});

		// 회원 등록
		$(document).on("click", "#memberPopForm .dev_insert_btn", function(e) {
			
			var memberDivCd =  $('input:radio[name="memberDivCd"]:checked').val();
			
			/* if ($("#memberPopForm [name='name']").val() == '' || $("#memberPopForm [name='name']").val() == 'null') {
				alert("사원검색을 먼저 진행하여 주시기 바랍니다.");
				return;
			} */

			if ($("#memberPopForm [name='cocId']").val() == '' || $("#memberPopForm [name='cocId']").val() == 'null') {
				alert("사번을 입력해 주시기 바랍니다.");
				return;
			}

			var id = $("#memberPopForm [name='cocId']").val().trim();
			$("#memberPopForm [name='id']").val(id);

			if ($("#memberPopForm [name='name']").val() == '' || $("#memberPopForm [name='name']").val() == 'null') {
				alert("이름을 입력해 주세요.");
				return;
			}

/* 			if ($("#memberPopForm [name='memberDivCd']").val() == 'C' && ($("#memberPopForm [name='upperMemberUid']").val() == '' || $("#memberPopForm [name='upperMemberUid']").val() == 'null')) {
				alert("담당 매니저를 선택해 주시기 바랍니다.");
				return;
			} */

			var name = $("#memberPopForm [name='name']").val().trim();
			$("#memberPopForm [name='name']").val(name);

			//선택된 부서 세팅
		/* 	var departCd = $("#memberPopForm [name='upperMemberUid'] option:selected").attr("data-depart-cd");
			$("#memberPopForm [name='departCd']").val(departCd);
 */
			
			if(($("select.select_depart").val() == "") && memberDivCd== "M"){
				alert("부서를 선택해 주시기 바랍니다.");
				return;
			}
			if(($("select.select_admin").val() == "") && memberDivCd== "C"){
				alert("담당 매니저를 선택해 주시기 바랍니다.");
				return;
			}
 			var departCd =""
			if(memberDivCd== "M"){
				departCd = $("#memberPopForm .select_depart option:selected").val()
			}else if(memberDivCd== "C"){
				departCd = $("#memberPopForm [name='upperMemberUid'] option:selected").attr("data-depart-cd");
			}
			$("#memberPopForm [name='departCd']").eq(0).val(departCd)
			$("#memberPopForm [name='departCd']").eq(1).val(departCd)
			
			// 전체 체크 순회
			var firstYn =  $('input:radio[name="firstYn"]:checked').val();
			var checkValue="";
			$("input:checkbox[name=ctgNumArr]").each(function(item, index) {
				if (this.checked) {
					if (firstYn==this.value)  checkValue='Y';
				}
			});

			if (typeof firstYn !=  'undefined') {
				if (checkValue !='Y') {
					alert("우선여부를 배정에 따라 선택해주세요.");
					return false;
				}
			}

			var tel = $("#memberPopForm [name='tel']").val().trim();
			$("#memberPopForm [name='tel']").val(tel);

			var email = $("#memberPopForm [name='email']").val().trim();
			$("#memberPopForm [name='email']").val(email);

			//비밀번호 초기화값 넣기
/* 			var pwd= $("#memberPopForm [name='cocId']").val();
			$("#memberPopForm [name='pwd']").val(pwd);  */

			$.ajax({
				url : "<c:url value='/manage/insertMember' />",
				data : $("#memberPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_member_pop").hide();
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 회원 수정
		$(document).on("click", "#memberPopForm .dev_update_btn", function(e) {
			var name = $("#memberPopForm [name='name']").val().trim();
			$("#memberPopForm [name='name']").val(name);
			if(name == ""){
				alert("이름을 입력해 주세요.");
				$("#memberPopForm [name='name']").focus();
				return false;
			}

			// 전체 체크 순회
			var firstYn =  $('input:radio[name="firstYn"]:checked').val();
			var checkValue="";
			$("input:checkbox[name=ctgNumArr]").each(function(item, index) {
				if (this.checked) {
					if (firstYn==this.value)  checkValue='Y';
				}
			});

			if (typeof firstYn !=  'undefined') {
				if (checkValue != 'Y') {
					alert("우선여부를 배정에 따라 선택해주세요.");
					return false;
				}
			}
			//선택된 부서 세팅
			var memberDivCd =  $('input:radio[name="memberDivCd"]:checked').val();
			var departCd =""
			if(memberDivCd== "M"){
				departCd = $("#memberPopForm .select_depart option:selected").val()
			}else if(memberDivCd== "C"){
				departCd = $("#memberPopForm [name='upperMemberUid'] option:selected").attr("data-depart-cd");
			}
			$("#memberPopForm [name='departCd']").eq(0).val(departCd)
			$("#memberPopForm [name='departCd']").eq(1).val(departCd)

			var tel = $("#memberPopForm [name='tel']").val().trim();
			$("#memberPopForm [name='tel']").val(tel);

			var email = $("#memberPopForm [name='email']").val().trim();
			$("#memberPopForm [name='email']").val(email);

			$.ajax({
				url : "<c:url value='/manage/updateMember' />",
				data : $("#memberPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_member_pop").hide();
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		//체크박스 체크시 radio버튼 활성화 
		$(document).on("click", "#memberPopForm [name='ctgNumArr']", function(e) {
			$("input:checkbox[name=ctgNumArr]").each(function(index, item) {
				//alert($("#memberPopForm #firstYn_"+index).val());
				
				if (this.checked) {
					$("#memberPopForm #firstYn_"+index).attr("disabled", false); 
				}else {
					$("#memberPopForm #firstYn_"+index).attr("disabled", true); 
					$("#memberPopForm #firstYn_"+index).attr("checked", false); 
				}
			});		
		});				

		// 비밀번호 초기화 
		$(document).on("click", "#memberPopForm .dev_pwd_btn", function(e) {

			if(!confirm("비밀번호를 초기화 하시겠습니까? ")){
				return false;
			}

			/* var pwd= $("#memberPopForm [name='cocId']").val();
			$("#memberPopForm [name='pwd']").val(pwd); */

			$.ajax({
				url : "<c:url value='/manage/changePasswd' />",
				/* data : $("#memberPopForm").serialize() + "&newPassword=" + pwd, */
				data : $("#memberPopForm").serialize(), 
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
				}
			});
		});

		

		// 회원 삭제
		$(document).on("click", "#memberPopForm .dev_delete_btn", function(e) {
			var name = $("#memberPopForm [name='name']").val();

			if(!confirm(name + " 계정을 삭제하시면 \n더 이상 채팅상담시스템에 접근 할 수 없습니다. \n삭제하시겠습니까?")){
				return false;
			}

			$.ajax({
				url : "<c:url value='/manage/deleteMember' />",
				data : $("#memberPopForm").serialize(),
				type : "post",
				success : function(result) {
					if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
						// 성공
						fn_layerMessage(result.rtnMsg);
						$(".dev_member_pop").hide();
						goSearch();
					}else{
						fn_layerMessage(result.rtnMsg);
					}
				},
				complete : function() {
				}
			});
		});

		// 회원 계정 활성화
		$(document).on("click", "#memberForm .dev_validMember_btn", function(e) {
			var memberUid = $(this).attr("data-memberUid");
			var id = $(this).attr("data-id");
			var name = $(this).closest("tr").find(".dev_member_name").text();

			if(!confirm(name + " 계정을 인증하시겠습니까?")){
				return false;
			}

			$.ajax({
				url : "<c:url value='/manage/updateMemberValid' />",
				data : {"memberUid" : memberUid, "id" : id, "name" : name, "validYn" : "Y"},
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
		});

		// 회원 계정 삭제
		$(document).on("click", "#memberPopForm .dev_valid_n_btn", function(e) {
			var memberUid = $("#memberPopForm [name='memberUid']").val();
			var name = $("#memberPopForm [name='name']").val();
			var id = $("#memberPopForm [name='id']").val();

			if(!confirm(name + " 님의 계정을 중지 하시겠습니까? \n중지하시면 더 이상 로그인 및 상담이 불가능합니다.")){
				return false;
			}

			$.ajax({
				url : "<c:url value='/manage/updateMemberValid' />",
				data : {"memberUid" : memberUid, "id" : id, "name" : name, "validYn" : "N"},
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
		});

		// 정렬
		$(document).on("change", "#memberForm [name='sortType']", function(e) {
			goSearch();
		});

		function goSearch(){
			$("#memberForm").attr("action", "<c:url value='/manage/selectMemberList' />");
			$("#memberForm").submit();
		}

		//사번조회
		$(document).on("click", "#memberPopForm .dev_sch_member_btn", function(e) {
			var cocId= $("#memberPopForm [name='cocId']").val();
			if (cocId) {
				$.ajax({
					url: "<c:url value='/manage/selectTempMember' />",
					data: {"searchText" : cocId},
					type: 'post'
				}).done(function(result, textStatus, jqXHR) {
					if (result && result.data) {
						// 성공
						if (result.data.length == 0) {
							fn_layerMessage('사번에 해당하는 정보를 조회할수 없습니다.');
						} else {
							var cocId = $("#memberPopForm [name='cocId']").val();
							//ToDo : data 변경
							$("#memberPopForm [name='name']").val((result.data.name == null) ? '' : result.data.name);
							$("#memberPopForm .id_text").text((result.data.name == null) ? '' : result.data.name);
							$("#memberPopForm [name=workAreaCd]").val((result.data.work_area_code == null) ? '' : result.data.work_area_code);
							$("#memberPopForm [name=workAreaNm]").val((result.data.work_area == null) ? '' : result.data.work_area);
							$("#memberPopForm [name=workPartCd]").val((result.data.work_part_code == null) ? '' : result.data.work_part_code);
							$("#memberPopForm [name=workPartNm]").val((result.data.work_part == null) ? '' : result.data.work_part);
							$("#memberPopForm .workAreaNm").text((result.data.work_area == null) ? '' : result.data.work_area);
							$("#memberPopForm [name='pwd']").val(cocId); //비밀번호 사번셋팅
						}
					} else {
						fn_layerMessage('사번에 해당하는 정보를 조회할수 없습니다.');
					}
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: SUBMIT CHATROOM: ', textStatus);
				});
			}
			else {fn_layerMessage('사번을 입력하세요');}
		});

		// 패스워드 3회 중복체크 
		$(document).on("click", "#memberPopForm .dev_pwd_xxx_btn", function(e) {

			//var pwd= $("#memberPopForm [name='cocId']").val();
			$("#memberPopForm [name='pwd']").val(pwd);

			$.ajax({
				url : "<c:url value='/manage/passwd3DuplCheck' />",
				data : $("#memberPopForm").serialize(),
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
				}
			});
		});		

		
		


	</script>

	<style>
		.dev_member_set tr {height:46px;}
	</style>

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
			<form id="memberForm" name="memberForm" method="post" action="">
				<input type="hidden" name="nowPage" value="${nowPage }" />
				<input type="hidden" name="totPage" value="${totPage }" />
				<div class="left_content_head">
					<h2 class="sub_tit">계정 관리</h2>
					<!-- <button type="button" class="btn_go_help">도움말</button> -->
				</div>
				<div class="inner">
					<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
						<div class="id_info_area">
<!-- 
							<div class="id_box">
								<span class="tit">관리자</span>
								<em>${memberTypeCount.admin_cnt } <span>개</span></em>
							</div> -->
							<div class="id_box">
								<span class="tit">활성 매니저</span>
								<em>${memberTypeCount.manager_cnt } <span>개</span></em>
							</div>
							<div class="id_box" >
								<span class="tit">활성 상담직원</span>
								<em>${memberTypeCount.counselor_cnt } <span>개</span></em>
							</div>
								<div class="id_box w100" style="float:right;">
								<span class="tit">활성 계정수 / 총 가용 계정 수</span>
								<em>${memberTypeCount.available_cnt} <span>개 /</span>${memberTypeCount.liscence_cnt } <span>개</span></em>
								<%-- <em>${memberTypeCount.tm_cnt +  memberTypeCount.cs_cnt} <span>개 /</span>${memberTypeCount.liscence_cnt } <span>개</span></em> --%>
							</div>					
							<%-- <div class="id_box" style="float:right;">
								<span class="tit">활성 CS계정</span>
								<em>${memberTypeCount.cs_cnt } <span>개</span></em>
							</div>			 --%>				

							<%-- <div class="id_box" style="display: none; float:right;">
								<span class="tit">활성 TM계정</span>
								<em>${memberTypeCount.tm_cnt } <span>개</span></em>
							</div> --%>
								
							
						<!-- 
							<div class="id_box">
								<span class="tit">인증계정</span>
								<em>${memberTypeCount.valid_cnt } <span>개</span></em>
							</div>						
							<div class="id_box">
								<span class="tit">미인증계정</span>
								<em>${memberTypeCount.not_valid_cnt } <span>개</span></em>
							</div>
						 -->
						</div>
					</c:if>
					<div class="top_filter_area">
						<span class="dot_tit">검색어</span>
						<div class="search_area">
							<select name="schOpt" style="width:160px;">
								<option value="A" <c:if test="${param.schOpt eq 'A' }">selected</c:if> >본인</option>
								<option value="B" <c:if test="${param.schOpt eq 'B' }">selected</c:if> >담당매니저</option>
								<option value="C" <c:if test="${param.schOpt eq 'C' }">selected</c:if> >매니저가 없는 경우</option>
							</select>
							<select name="schType" style="width:90px;">
								<option value="NAME" <c:if test="${param.schType eq 'NAME' }">selected</c:if> >이름</option>
								<option value="COC_ID" <c:if test="${param.schType eq 'COC_ID' }">selected</c:if> >사번</option>
								<%-- <option value="TEL" <c:if test="${param.schType eq 'TEL' }">selected</c:if> >전화번호</option> --%>
							</select>
							<div class="search_area_right">
								<input type="text" name="schText" value="${schText}" /><button type="button" class="btn_search dev_search_btn">검색하기</button>
							</div>
						</div>
					</div>
					<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
						<div class="btn_staff_area">
							<button type="button" class="btn_plus_manager dev_openInsertPop"><i></i>회원 등록</button>
						</div>
					</c:if>

					<table class="tCont service">
						<caption>계정 관리 목록입니다.</caption>
						<colgroup><%--
							<col style="width:10%">--%>
							<col style="width:11%">
							<col style="width:7%">
							<col style="width:7%">
							<col style="width:10%"><%--
							<col style="width:13%">
							<col style="width:12%">--%>
							<col style="width:13%">
							<col style="width:7%">
							<col style="width:7%">
							<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
								<col style="width:9%">
							</c:if>
						</colgroup>
						<thead>
							<tr><%--
								<th>회원번호</th>--%>
								<th>구분</th>
								<th>담당매니저</th>
								<th>이름</th>
								<th>사번</th><%--
								<th>부서</th>
								<th>전화번호</th>--%>
								<th>수정일자</th>
								<th>계정상태</th><%--
								<th>상담</th>--%>
								<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
									<th>수정</th>
								</c:if>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty memberList}">
									<tr>
										<td colspan="9">조회된 내용이 없습니다.</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="data" items="${memberList }" varStatus="status">
										<tr>
											<%-- <td>${data.member_uid }</td> --%>
											<td>${data.member_div_nm }</td>
											<td>
												<c:choose>
													<c:when test="${data.member_div_cd eq 'C'}">
														${data.manager_nm }
													</c:when>
													<c:otherwise>
														-
													</c:otherwise>
												</c:choose>
											</td>
											<td class="dev_member_name"><c:out value="${data.name }"/> ${data.hnet_chk}</td>
											<td><c:out value="${data.coc_id }"/></td>
											<%-- <td>${data.depart_nm }</td> --%>
											<%-- <td>${data.tel }</td> --%>
											<td>${data.update_dt }</td>
											<td>
												<c:choose>
													<c:when test="${data.valid_yn eq 'Y'}">
														계정사용
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
																<button type="button" class="btn_id_go dev_validMember_btn" data-memberUid="${data.member_uid }" data-id="${data.id }">계정활성</button>
															</c:when>
															<c:otherwise>
																미인증
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</td><%--
											<td>
												<c:choose>
													<c:when test="${data.cns_possible_yn eq 'Y'}">
														진행
													</c:when>
													<c:otherwise>
														<span style="color:red;">안함</span>
													</c:otherwise>
												</c:choose>
											</td>--%>
											<c:if test="${not empty sessionVo && (sessionVo.memberDivCd eq 'S' || sessionVo.memberDivCd eq 'A' || sessionVo.memberDivCd eq 'M')}"><!-- 관리자만 보이는 영역 -->
												<td><button type="button" class="btn_s_form dev_openEditPop" data-memberUid="${data.member_uid }" data-memberDivCd="${data.member_div_cd }">수정</button></td>
											</c:if>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<div class="table_bottom_area">
						<!-- pager -->
						<div class="pager">
							<button type="button" class="btn_prev_page dev_prev_btn">이전으로</button>
							<input type="text" class="form_pager" value="${nowPage }" name="inputNowPage">
							<span class="page_no">/ ${totPage } page(s)</span>
							<button type="button" class="btn_next_page dev_next_btn">다음으로</button>
						</div>
						<!--// pager -->
						<div class="table_count">
							<span class="cont_text"><em>${totCount }</em> count(s)</span>
						</div>
					</div>
				</div>
			</form>
		</div>
		<!--// left_area -->
	</div>

<!-- popup:매니저 등록 -->
<div class="popup popup_manager dev_member_pop">
</div>
<!--// popup:매니저 등록  -->

<!-- 팝업용 임시 -->
<div style="display:none;">
	<select id="managerList">
		<c:forEach var="data" items="${managerList }" varStatus="status">
			<option value="${data.member_uid }" data-depart-cd="${data.depart_cd }" data-depart-nm="${data.depart_nm }" >${data.name }</option>
		</c:forEach>
	</select>
</div>

<!-- 팝업용 임시 -->
<div style="display:none;">
	<select id="departList">
		<c:forEach var="data" items="${departList }" varStatus="status">
			<option value="${data.depart_cd }" >${data.depart_nm }</option>
		</c:forEach>
	</select>
</div>

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

</body>
</html>
