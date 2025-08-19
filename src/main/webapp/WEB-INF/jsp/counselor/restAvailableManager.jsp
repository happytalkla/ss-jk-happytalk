<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<div class="popup_head">
			<h1 class="popup_tit">매니저 상담 요청</h1> 
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text">매니저상담 요청합니다.<br>
			매니저 상담 사유를 입력해 주세요.</p>
			<div class="popup_input_area">
				<textarea class="form_text" flagTag="MANAGER_COUNSEL" placeholder="매니저 상담 사유를 입력하세요."></textarea>
				<button type="button" class="btn_save">요청하기</button>
			</div>
		</div>