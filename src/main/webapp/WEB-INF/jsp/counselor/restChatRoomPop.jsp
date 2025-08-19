<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<html lang="ko"> 
<head>
	<title>디지털채팅상담시스템</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/include.css" />" />
	<style type="text/css">
		.hidden {
			display: none;
		}
		.text_box .type_text {
			z-index: 1;
		}
	</style>
</head>
<script src="<c:url value="/js/jquery-1.12.2.min.js" />"></script>
<script src="<c:url value="/js/jquery-ui.1.9.2.min.js" />"></script>
<script src="<c:url value="/js/sockjs.min.js" />"></script>
<script src="<c:url value="/js/stomp.min.js" />"></script>
<script src="<c:url value="/js/jquery.cookie.js" />"></script>
<script src="<c:url value="/js/clipboard.min.js" />"></script>
<script src="<c:url value="/js/ifvisible.min.js" />"></script>
<script src="<c:url value="/js/swiper.js" />"></script>
<script src="<c:url value="/js/ion.sound.min.js" />"></script>
<script src="<c:url value="/js/menu.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/common.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/chatRoom.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/chatRoomList.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/client.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<script src="<c:url value="/js/counselor.js"><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
<body id="admin">
<div class="popup popup_counseling" style="display: block">
	<div class="inner">
	<!-- 
		<div class="popup_head">
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
 	-->
		<div class="popup_counseling_area">
			<!-- 중간: 대화창 영역 -->
			<div class="counseling_mid">
				<div class="counsel_title_area">
					<span class="labelBlue">${chatRoom.chatRoomStatusCdNm}</span>${chatRoom.roomTitle}<%--
					<div class="counsel_title_date">
						<span class="tit_dot">상담시작</span> : ${chatMessage.roomCreateDateTime}
					</div>--%>
				</div>

				<!-- 대화창 영역 -->
				<div class="chatting_area">
					<div id="chat_body">
					<c:forEach var="chatMessage" items="${chatRoom.chatMessageList}" varStatus="i">
						<c:set var="chatMessage" value="${chatMessage}" scope="request" />
						<c:import url="/WEB-INF/jsp/common/chatMessage.jsp" />
					</c:forEach>
					</div>
				</div>
				<!--// 대화창 영역 -->
			</div>
			<!--// 중간: 대화창 영역 -->

			<div class="counseling_info_right">
				<table class="tCont">
					<caption>상담내역 입니다.</caption>
					<colgroup>
						<col style="width:25%">
						<col style="width:75%">
					</colgroup>
					<tbody>
						<tr>
							<th>시작 시간</th>
							<td class="textL">${chatRoom.roomCreateDateTime}</td>
						</tr>
						<tr>
							<th>종료 시간</th>
							<td class="textL">${chatRoom.roomEndDateTime}</td>
						</tr>
						<tr>
							<th>상담 분류1</th>
							
							<%-- <c:if test="${chatRoom.endCtgNm1 != null}">
							<td class="textL">${chatRoom.endCtgNm1} &gt; ${chatRoom.endCtgNm2}
								<c:if test="${chatRoom.endCtgNm3 != null}"> &gt; ${chatRoom.endCtgNm3}</c:if>
							</td>
							</c:if>
							<c:if test="${chatRoom.endCtgNm1 == null}">
							<td class="textL">${chatRoom.ctgNm1} &gt; ${chatRoom.ctgNm2}</td>
							</c:if> --%>
							<c:if test="${chatRoom.ctgNm1 != null}">
							<td class="textL">${chatRoom.ctgNm1} &gt; ${chatRoom.ctgNm2}</td>
							</c:if>
						</tr>
						<tr>
							<th>담당 상담직원</th>
							<td class="textL">${chatRoom.departNm} ${chatRoom.memberName}</td>
						</tr><%--
						<tr>
							<th>고객ID</th>
							<td class="textL">${chatRoom.cstmCocId}</td>
						</tr>
						<tr>
							<th>고객이름</th>
							<td class="textL">${chatRoom.cstmName}</td>
						</tr>--%>
						<tr>
							<th>인입채널</th>
							<td class="textL">${chatRoom.cstmLinkDivCdNm}</td>
						</tr>
						<tr>
							<th>고객기기</th>
							<td class="textL">${chatRoom.cstmOsDivCdNm}</td>
						</tr>
						<tr>
							<th>고객민감정보</th>
							<td class="textL">
							<c:if test="${not empty cstmSinfo}">
								<c:if test="${not empty cstmSinfo.tel_no1}">
									연락처: ${cstmSinfo.name} / ${cstmSinfo.tel_no1}-${cstmSinfo.tel_no2}-${cstmSinfo.tel_no3}
									<br />
								</c:if>
								<c:if test="${not empty cstmSinfo.card_nm}">
									카드정보: ${cstmSinfo.card_cstm} / ${cstmSinfo.card_nm} /
									${cstmSinfo.card_no1}-${cstmSinfo.card_no2}-${cstmSinfo.card_no3}-${cstmSinfo.card_no4} /
									${cstmSinfo.check_month}월${cstmSinfo.check_year}년
									<br />
								</c:if>
								<c:if test="${not empty cstmSinfo.bank_nm}">
									계좌정보: ${cstmSinfo.account_nm} / ${cstmSinfo.bank_nm} /
									${cstmSinfo.account_no}
									<br />
								</c:if>
								<c:if test="${not empty cstmSinfo.etc}">
									기타: ${cstmSinfo.etc}
								</c:if>
							</c:if>
							</td>
						</tr>
						<tr>
							<th>평가점수</th>
							<td class="textL">${chatRoom.evlScore}</td>
						</tr>
						<tr>
							<th>평가내용</th>
							<td class="textL">${chatRoom.evlCont}</td>
						</tr>
						<tr>
							<th>인증고객명</th>
							<td class="textL">
								${chatRoom.roomCocNm}
							</td>
						</tr>
						<tr>
							<th>인증고객ID</th>
							<td class="textL">
								${chatRoom.roomCocId}
							</td>
						</tr><%--
						<tr>
							<th>기간계 <br>호출내역</th>
							<td class="textL">
							<c:if test="${not empty infraLogList}">
							<c:forEach var="data" items="${infraLogList}">
								<c:if test="${not empty data.req_date}">
									[${data.req_date}]
									[${data.username}]
									${data.auth_position_nm}
									${data.auth_type_nm} - ${data.interface_id_nm}
									(${data.rescd_nm})<br>
								</c:if>
							</c:forEach>
							</c:if>
							<c:if test="${empty infraLogList}">
							조회할 데이타가 없습니다.
							</c:if>
							</td>
						</tr>--%>
						<tr>
							<th>상담직원 메모</th>
							<td class="textL">${chatRoom.endMemo}</td>
						</tr>
						<%--
						<tr>
							<th class="vaT">첨부파일</th>
							<td class="textL file_up_area vaT">
								<ul>
									<li>파일명</li>
									<li>파일명</li>
								</ul>
							</td>
						</tr>--%>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>	
</body>
</html>	
	<script>
	/*$(document).ready(function() {
		if ($('.popup_counseling_area .chat_text_slide').length > 0) {
			new Swiper($('.popup_counseling_area .chat_text_slide'), {
					slidesPerView: 1,
					centeredSlides: false,
					spaceBetween: 10,
			});
		}
	});*/
	</script>
