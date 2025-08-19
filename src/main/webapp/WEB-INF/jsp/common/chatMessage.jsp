<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<div data-message-id="${requestScope.chatMessage.chatNum}" data-message-sender="${requestScope.chatMessage.senderUid}"
	 class="${requestScope.chatMessage.messageClassName}">
	<c:if test="${!empty requestScope.chatMessage.avatarClassName}">
		<i class="${requestScope.chatMessage.avatarClassName}"></i>
	</c:if>
	<%-- <div class="${requestScope.chatMessage.messageBoxClassName}"> --%>
		<chat:message chatMessage="${requestScope.chatMessage}" />
		<span class="date"><c:out value="${requestScope.chatMessage.regDtPretty}" /></span>
	<!-- </div> -->
</div>
