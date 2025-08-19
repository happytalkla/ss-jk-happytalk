<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %> 

									<c:forEach var="data" items="${chatRoomList }" varStatus="status">
										<li data-chat-room-uid="${data.chat_room_uid}" data-cstm-uid="${data.cstm_uid}"
											<c:if test="${not empty data.chng_req_num}">data-chng-req-num="${data.chng_req_num}"</c:if>
											<c:if test="${not empty data.review_req_num}">data-review-req-num="${data.review_req_num}"</c:if>
											style="<c:if test="${data.end_info_yn eq 'Y'}">background-color: #f5f5f5</c:if><c:if test="${data.end_info_yn ne 'Y'}">background-color: #ffb28e</c:if>"
											>
											<div class="icon_area">
											<c:if test="${schRoomStatus ne 'botIng' && schRoomStatus ne 'botEnd' && schRoomStatus ne 'cnsrEnd'}">
												<div class="floatL">
													<input type="checkbox" class="checkbox_18" name="chatRoomUid" id="cr_${data.chat_room_uid}"><!-- id="check_staff01_${status.index }" -->
													<label for="cr_${data.chat_room_uid}"><span></span></label>
												</div>
											</c:if>
												<c:choose>
													<c:when test="${data.cstm_link_div_cd eq 'B' }"><i class="icon_kakao" title="네이버톡톡">네이버톡톡</i></c:when>
													<c:when test="${data.cstm_link_div_cd eq 'C' }"><i class="icon_o2" title="O2톡">O2톡</i></c:when>
													<c:when test="${data.cstm_link_div_cd eq 'D' }"><i class="icon_mpop" title="mPOP톡">mPOP톡</i></c:when>
													<c:otherwise><i class="icon_happytalk" title="삼성증권 웹채팅">삼성증권 웹채팅</i></c:otherwise>
												</c:choose>
												<c:if test="${not empty data.pass_time_nm }">
													<i class="icon_time" title="상담 시작 시간 : ${data.room_create_datetime }">${data.pass_time_nm }</i>
												</c:if>
												<!-- <i class="icon_alarm">알람</i> -->
												<c:if test="${not empty data.room_mark_class && data.room_mark_class ne '' }">
													<i class="${data.room_mark_class }" title="${data.mark_desc}">${data.mark_desc }</i>
												</c:if>
												<c:if test="${not empty data.grade_class_name && data.grade_class_name ne '' }">
													<i class="${data.grade_class_name }" title="${data.grade_memo}">${data.grade_desc }</i>
												</c:if>
												<span class="right_id">
													<c:choose>
														<c:when test="${empty data.manager_nm }">(#${data.counselor_nm } > ${data.counselor_nm })</c:when>
														<c:otherwise>(#${data.manager_nm } > ${data.counselor_nm })</c:otherwise>
													</c:choose>
												</span>
											</div>
											<a href="javascript:void(0);" class="dev_roomListLi">
												<p class="chat_tit">${data.cstm_nm } </p><%--
												<p class="chat_tit">${data.room_title }</p>--%>
												<c:if test="${not empty data.lastchatcont && fn:length(data.lastchatcont) > 0 && not empty data.lastchatcont[0].getContentsJson() }">
													<c:set var="listTitleYn" value="N" />
													<c:forEach var="data2" items="${data.lastchatcont[0].getContentsJson() }" varStatus="status">
														<c:if test="${listTitleYn eq 'N' }">
															<c:if test="${data2.type eq 'text' }">
																<p class="chat_tit02">${data2.contents }</p>
																<c:set var="listTitleYn" value="Y" />
															</c:if>
														</c:if>
													</c:forEach>
												</c:if>
												<span class="date" title="${data.last_chat_datetime }">${data.last_chat_date }</span>
											</a>
										</li>
									</c:forEach>
