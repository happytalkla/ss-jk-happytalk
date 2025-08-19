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

	<link rel="stylesheet" type="text/css" href="<c:url value='/tinymce/skins/lightgray/skin.min.css' />" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/tinymce/skins/lightgray/content.min.css' />" />
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/tinymce/tinymce.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/tinymce.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/menu.js' />"></script>
 
	<script type="text/javascript">
	// 저장버튼 클릭
	$(document).on(
			"click",
			"#btn_save",
			function() {

				$("#noticeForm").attr("action",
						"<c:url value='/notice/modifyNotice' />");
				$("#noticeForm").submit();
	});

	// 목록버튼 클릭
	$(document).on(
			"click",
			"#btn_list",
			function() {
				$("#noticeForm").attr("action",
						"<c:url value='/notice/noticeList' />");
				$("#noticeForm").submit();
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
		<form id="noticeForm" name="noticeForm" method="post" action="">
		<input type="hidden" name="nowPage" value="${nowPage }" />
		<input type="hidden" name="totPage" value="${totPage }" />
		<input type="hidden" name="searchVal" value="${searchVal}" />
		<input type="hidden" name="noticeDivCd" value="${noticeDivCd}" />
		<input type="hidden" name="notice_num" value="${notice_num}" />
		<input type="hidden" name="noticeDivCdPageVal" value="${noticeDivCd}" />


		<div class="left_content_area">
			<div class="left_content_head">
				<h2 class="sub_tit">공지사항</h2>
			</div>
			<div class="inner">
				<table class="tCont setting">
					<caption>공지사항 읽기 화면 입니다.</caption>
					<colgroup>
						<col style="width:10%">
						<col style="width:">
					</colgroup>
					<tbody>
						<tr>
							<th>제목</th>
							<td class="textL">
								<input type="text" name="title" value="${noticeDetail.title }" maxlength="50" style="width:100%;"/>
							</td>
						</tr>
						<tr>
							<th>공지</th>
							<td class="textL">
								<select name="noticeDivVal">
								<c:choose>
									<c:when test="${empty noticeDivList}">
									</c:when>
									<c:otherwise>
										<c:forEach var="data" items="${noticeDivList }" varStatus="status">

											<c:if test="${sessionMemberDivCd eq 'M' and data.cd eq 'C'}">
												<option value="${data.cd}" <c:if test="${noticeDetail.noticeDivCd eq data.cd}">selected</c:if>>${data.cd_nm }</option>
											</c:if>

											<c:if test="${sessionMemberDivCd eq 'S' or sessionMemberDivCd eq 'A'}">
												<option value="${data.cd}" <c:if test="${noticeDetail.noticeDivCd eq data.cd}">selected</c:if>>${data.cd_nm }</option>
											</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								</select>

							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td class="textL">
								<textarea name="cont">${noticeDetail.cont} </textarea>
								<input type="file" accept="image/*" style="display:none;" id="imageFile" onclick="javascript:void(0);" />
								<script>
									tinymce.init({
										selector : 'textarea',
										plugins : 'table textcolor code',
										toolbar : 'undo redo | styleselect | forecolor | fontcolor bold italic | alieft aligncenter alignright aligncenter alignjustify | outdent indent | presentation | table | mybutton | code | text color',
										menubar : 'none',
										height : 400,
										setup : function(editor){
											editor.addButton('mybutton',{
											text : '',
											icon : 'image',
											menubar : 'image',
											onclick:function(e){
												$('#imageFile').click();
												$('#imageFile').change(function(){
													input = document.getElementById('imageFile');
													file = input.files[0]
													fr = new FileReader();
													fr.onload = createImage;
													fr.readAsDataURL(file);

													function createImage(){
														img = new Image();
														img.src = fr.result;
														/*
														이미지 업로드 확인
														*/
														editor.insertContent('<img src="'+img.src+'"/>');
													}
												})

											}
											})
										},
										//automatic_uploads:true,
										//images_upload_url:"<c:url value='/notice/noticeImgUpload'/>"
									});


								</script>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="btn_center_area">
					<button type="button" class="btn_go_save" id="btn_save">저장</button>
					<button type="button" class="btn_go_save" id="btn_list">목록</button>
				</div>
			</div>
		</div>
		</form>
		<!--// left_area -->
	</div>


</body>
</html>
