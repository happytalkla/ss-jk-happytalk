<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<%@ page import="ht.constants.CommonConstants"%>
					<div class="counsel_title_area" data-chat-room-uid="${chatRoom.chatRoomUid}"
						data-member-uid="${chatRoom.memberUid}">
						<span class="labelBlue">${chatRoom.chatRoomStatusCdNm}</span><span class="room_title">${chatRoom.roomTitle}</span>
						<div style="float:right;">
							<input type="hidden" name="chatRoomUid" id="manchatRoomUid" value="${chatRoom.chatRoomUid}" />
							<input type="hidden" name="cstmUid" id="mancstmUid" value="${chatRoom.cstmUid}" />
							<button type="button" class="btn_sm_default " id="btn_end_popup" >후처리 수정
							<%-- <c:choose>
								<c:when test="${endYn eq 'Y'}">후처리 수정</c:when>
								<c:otherwise>후처리 입력</c:otherwise>
							</c:choose> --%>
							</button>
							<c:if test="${chatRoom.chk30days eq 'n'}">
								<button type="button" class="btn_sm_default" id="btn_info_view" style="margin-right:5px;">상담정보</button>
							</c:if>
						</div>
						<c:if test="${param.statusMenu eq 'contReview'}">
						<button type="button" class="btn_check_out" data-review-req-num=""><i></i>검토 완료</button>
						</c:if>
						<c:if test="${param.statusMenu eq 'managerChange'}">
						<button type="button" class="btn_check_out" data-review-req-num=""><i></i>상담 반려</button>
						</c:if>
						<c:if test="${param.statusMenu eq 'cstmGrade'}">
						<button type="button" class="btn_check_out" data-review-req-num=""><i></i>코끼리 반려</button>
						</c:if><%--
						<span class="start_date">상담시작 : <span class="room_start_datetime">${chatMessage.roomCreateDateTime}</span></span>--%>
					</div>
					<div class="counsel_chat_info">
						<div style="float: left; width: 48%;">
							<ul class="info_list">
								<li class="category_info">
									<p class="label_area"><span class="labelBlue">상담구분</span></p>
									<c:if test="${defaultCtgNum == chatRoom.ctgNum}">
									<p class="text_area">분류 미선택</span>
									</c:if>
									<c:if test="${defaultCtgNum != chatRoom.ctgNum}">
									<p class="text_area"><span>${chatRoom.ctgNm1} &gt; ${chatRoom.ctgNm2}<c:if test="${chatRoom.ctgNm3} != null"> &gt; ${chatRoom.ctgNm3}</c:if></span>
									</c:if>
									
									</p>
								</li>
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">고객이름</span></p>
									<p class="text_area">
										<span>${chatRoom.roomCocNm}</span>
										<c:if test="${!empty chatRoom.authTypeName}">
										<span class="target_auth_info">(인증: ${chatRoom.authTypeName})</span>
										</c:if>
									</p>
								</li>
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">고객기기</span></p>
									<p class="text_area"><span>${chatRoom.cstmOsDivCdNm}</span></p>
								</li>
	
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">고객ID</span></p>
									<p class="text_area"><span class="target_customer_id">${chatRoom.roomCocId}</span>
										<c:if test="${not empty chatRoom.cstmCocId}">
										<button type="button" class="btn_copy" data-clipboard-target=".customer_info .target_customer_id">복사</button></p>
										</c:if>
								</li>
							</ul>	
						</div>
						<div style="float: right; width: 44%;">
							<ul class="info_list">
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">인입채널</span></p>
									<p class="text_area"><span>${chatRoom.cstmLinkDivCdNm} <c:if test="${chatRoom.screenName != '' and chatRoom.screenName != null}"> - ${chatRoom.screenName }</c:if></span></p>
								</li>
								
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">고객구분</span></p>
									<p class="text_area"><span>${chatRoom.cstmCocTypeNm}</span></p>
								</li>
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">평가점수</span></p>
									<p class="text_area"><span >${chatRoom.evlScore}</span></p>
								</li>
								<li class="customer_info">
									<p class="label_area"><span class="labelBlue">평가내용</span></p>
									<p class="text_area"><span >${chatRoom.evlCont}</span></p>
								</li>
							</ul>	
						</div>
						
						<button type="button" class="btn_info_more">상세보기</button>
						<button type="button" class="btn_info_close" style="display: none;">상세보기 닫기</button>
					</div>

					<c:if test="${chatRoom.hasMemo}">
					<!-- 검토요청 글등록시 -->
					<div class="request_info">
						<ul class="info_list">
							<c:forEach var="chatMessage" items="${chatRoom.chatMessageList}">
							<c:if test="${chatMessage.contDivCd eq 'M'}">
							<li data-message-id="${chatMessage.chatNum}">
								<p class="label_area"><span class="labelGray">${chatMessage.regDtPretty}</span></p>
								<p class="text_area">${chatMessage.memoMessages}</p>
							</li>
							</c:if>
							</c:forEach>
						</ul>
						<button type="button" class="btn_request_more">상세보기</button>
						<button type="button" class="btn_request_close" style="display: none;">상세보기 닫기</button>
					</div>
					<!--// 검토요청 글등록시 -->
					</c:if>

					<!-- 대화창 영역 -->
					<div class="chatting_area">
						<div id="chat_body" class="chat-body" style="<c:if test="${chatRoom.hasMemo}">height: calc(100% - 202px); margin-top: 140px;</c:if><c:if test="${!chatRoom.hasMemo}">height: calc(100% - 162px); margin-top: 100px;</c:if>">
						<c:forEach var="chatMessage" items="${chatRoom.chatMessageList}" varStatus="i">
							<div data-message-id="${chatMessage.chatNum}" data-message-sender="${chatMessage.senderUid}" class="${chatMessage.messageClassName}">
								<div class="bubble-wrap">
									<article class="inner-chat">
								<c:choose>
									<c:when test="${chatMessage.senderDivCd eq 'S' and chatMessage.contDivCd eq 'L'}">
										<div class="bubble-arrow-gray">
									</c:when>
									<c:otherwise>
										<div class="${chatMessage.bubbleClassName}">
									</c:otherwise>
								</c:choose>
											<chat:message chatMessage="${chatMessage}" />
											<div class="time-chat"><%-- 시간 --%>
												<%-- 											
												<c:if test="${param.statusMenu eq 'contReview'}">편집 버튼
												<chat:editButton chatMessage="${chatMessage}" />
												</c:if>
												 --%>
												<c:out value="${chatMessage.regDtPretty}" />
												<c:if test="${chatMessage.senderDivCd ne 'R'}">
												<c:out value="${chatMessage.memberName}" />
												</c:if>
											</div>
										</div>
									</article>
								</div>
							</div>
						</c:forEach>
						</div>
					</div>
					<!--// 대화창 영역 -->
					
