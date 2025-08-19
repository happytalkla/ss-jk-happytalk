<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

						<input type="hidden" name="schRoomStatus" value="${schRoomStatus }" />
						<input type="hidden" name="schTerm" value="${schTerm }" />
						<input type="hidden" name="schSortColumn" value="${schSortColumn }" />
						<input type="hidden" name="schSortType" value="${schSortType }" />
						<input type="hidden" name="nowPage" value="${nowPage }" />
						<c:set var="totCount" value="0" />
						<c:if test="${not empty chatRoomList && chatRoomList.size() >= 0 }">
							<c:set var="totCount" value="${chatRoomList[0].tot_count }" />
						</c:if>
						<h3 class="sub_stit">
							<c:choose>
								<c:when test="${schRoomStatus eq 'botIng'}">봇상담 건</c:when>
								<c:when test="${schRoomStatus eq 'botEnd'}">봇상담 종료건</c:when>
								<c:when test="${schRoomStatus eq 'cnsrWait'}">상담직원 배정 대기</c:when>
								<c:when test="${schRoomStatus eq 'cnsrIng'}">진행중</c:when>
								<c:when test="${schRoomStatus eq 'cnstNotJoin'}">읽지않은 대화방</c:when>
								<c:when test="${schRoomStatus eq 'cnsrEnd'}">상담종료</c:when>
								<c:when test="${schRoomStatus eq 'cnsrChange'}">상담직원 변경요청</c:when>
								<c:when test="${schRoomStatus eq 'managerChange'}">매니저 상담 요청</c:when>
								<c:when test="${schRoomStatus eq 'contReview'}">상담내용 검토요청</c:when>
								<c:when test="${schRoomStatus eq 'cstmGrade'}">코끼리 등록 요청</c:when>
							</c:choose>
							(총 ${totCount}건)
						</h3>
						<div class="tab_icon_content">
							<div class="counseling_date_area" <c:if test="${schRoomStatus ne 'botEnd' && schRoomStatus ne 'cnsrEnd'}">style="display:none;"</c:if> >
								<input type="text" class="datepicker" name="schDate" id="schDate" value="${schDate }" style="text-align:center;" readonly="readonly">
								<button type="button" class="btn_go_search dev_date_search_btn">검색하기</button>
							</div>
							<div class="tabs_counseling_area" <c:if test="${schRoomStatus ne 'botEnd' && schRoomStatus ne 'cnsrEnd'}">style="display:none;"</c:if> >
								<ul class="tabs_menu_s">
									<li class="dev_termLi <c:if test='${schTerm eq \'toDay\' }'>active ${schSortType }</c:if> " data-schTerm="toDay">오늘</li>
									<li class="dev_termLi <c:if test='${schTerm eq \'yestDay\' }'>active ${schSortType }</c:if> " data-schTerm="yestDay">어제</li>
									<li class="dev_termLi <c:if test='${schTerm eq \'oneWeek\' }'>active ${schSortType }</c:if> " data-schTerm="oneWeek">1주일</li>
									<li class="dev_termLi <c:if test='${schTerm eq \'thisMonth\' }'>active ${schSortType }</c:if> " data-schTerm="thisMonth">${nowMonth.sel_day }</li>
									<li class="dev_termLi <c:if test='${schTerm eq \'oneMonthAgo\' }'>active ${schSortType }</c:if> " data-schTerm="oneMonthAgo">${onwMonthAgo.sel_day }</li>
									<li class="dev_termLi <c:if test='${schTerm eq \'twoMonthAgo\' }'>active ${schSortType }</c:if> " data-schTerm="twoMonthAgo">${twoMonthAgo.sel_day }</li>
								</ul>
							</div>
							<div class="chat_search_area">
								<select class="select_filter" name="schType">
									<option value="cont" <c:if test="${schType eq 'cont' }">selected</c:if> >상담내용</option><%--
									<option value="title" <c:if test="${schType eq 'title' }">selected</c:if> >상담제목</option>--%>
									<option value="roomId" <c:if test="${schType eq 'roomId' }">selected</c:if> >채팅방ID</option>
									<c:if test="${schRoomStatus ne 'botIng' && schRoomStatus ne 'botEnd'}">
										<option value="counselor" <c:if test="${schType eq 'counselor'}">selected</c:if> >상담직원</option>
									</c:if>
									<option value="cstmName" <c:if test="${schType eq 'cstmName' }">selected</c:if> >고객명</option>
									<option value="cstmId" <c:if test="${schType eq 'cstmId' }">selected</c:if> >고객ID</option><%--
									<option value="endDate" <c:if test="${schType eq 'endDate' }">selected</c:if> >종료일자</option>--%>
								</select>
								<input type="text" name="schText" value="${schText}" placeholder="검색어 입력, 날짜검색(ex:2018-01-31)" autocomplete="off" />
								<button type="button" class="btn_go_search dev_room_search_btn">검색하기</button>
							</div>
							<div class="chat_list_top">
								<ul class="list_filter">
									<li><a href="javascript:void(0);" data-schSortColumn="lastChat" class="dev_sortLi <c:if test='${schSortColumn eq \'lastChat\' }'>active ${schSortType }</c:if>" >최근대화순<c:if test="${schSortColumn eq 'lastChat' }"><i></i></c:if></a></li>
									<li><a href="javascript:void(0);" data-schSortColumn="name" class="dev_sortLi <c:if test='${schSortColumn eq \'name\' }'>active ${schSortType }</c:if>" >고객이름순<c:if test="${schSortColumn eq 'name' }"><i></i></c:if></a></li>
									<c:choose>
									<c:when test="${schRoomStatus eq 'botIng' || schRoomStatus eq 'botEnd'}">
									<li><a href="javascript:void(0);" data-schSortColumn="createTime" class="dev_sortLi <c:if test='${schSortColumn eq \'createTime\' }'>active ${schSortType }</c:if>" >방생성순<c:if test="${schSortColumn eq 'createTime' }"><i></i></c:if></a></li>
									</c:when>
									<c:otherwise>
									<li><a href="javascript:void(0);" data-schSortColumn="chatTime" class="dev_sortLi <c:if test='${schSortColumn eq \'chatTime\' }'>active ${schSortType }</c:if>" >최초응대순<c:if test="${schSortColumn eq 'chatTime' }"><i></i></c:if></a></li>
									</c:otherwise>
									</c:choose>
								</ul>
							</div>
							<div class="chat_top_btn" <c:if test="${schRoomStatus eq 'botIng' || schRoomStatus eq 'botEnd' || schRoomStatus eq 'cnsrEnd'}">style="display:none;"</c:if> >
								<input type="checkbox" class="checkbox_18" id="checkAll">
								<label for="checkAll"><span></span>전체 선택</label>
								<c:if test="${schRoomStatus == 'cnsrWait' || schRoomStatus == 'cnsrIng' || schRoomStatus == 'cnstNotJoin' || schRoomStatus == 'cnsrChange'}">
								<button type="button" class="btn_change_staff dev_change_cnsr_btn"><i></i>상담직원 변경</button>
								</c:if>
							<c:choose>
								<c:when test="${schRoomStatus eq 'managerChange'}"><button type="button" class="btn_change_staff dev_change_manager_btn" style="margin-right: 3px;"><i></i>대화방 접속</button></c:when>
								<c:when test="${schRoomStatus eq 'cstmGrade'}"><button type="button" class="btn_change_staff dev_cstm_grade_btn" style="margin-right: 3px;"><i></i>코끼리 등록</button></c:when>
							</c:choose>
							</div>
							<div id="dev_chatRoomListDiv" class="chatting_list_area <c:if test="${schRoomStatus eq 'botEnd' || schRoomStatus eq 'cnsrEnd'}">end</c:if> ">
								<ul class="chatting_list" id="dev_chatRoomListUl">
									<c:forEach var="data" items="${chatRoomList }" varStatus="status">
										<li data-chat-room-uid="${data.chat_room_uid}" data-cstm-uid="${data.cstm_uid}"
											<c:if test="${not empty data.chng_req_num}">data-chng-req-num="${data.chng_req_num}"</c:if>
											<c:if test="${not empty data.review_req_num}">data-review-req-num="${data.review_req_num}"</c:if>
											style="<c:if test="${data.end_info_yn eq 'Y'}">background-color: #f5f5f5</c:if><c:if test="${data.end_info_yn ne 'Y'}">background-color: #ffb28e</c:if>"
											>
											<div class="icon_area">
												<c:if test="${schRoomStatus ne 'botIng' && schRoomStatus ne 'botEnd' && schRoomStatus ne 'cnsrEnd'}">
													<div class="floatL">
														<input type="checkbox" class="checkbox_18" name="chatRoomUid" id="cr_${data.chat_room_uid}"><!-- id="check_staff01_${status.index}" -->
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
												<p class="chat_tit">${data.cstm_nm }</p><%--
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
								</ul>
							</div>
						</div>
