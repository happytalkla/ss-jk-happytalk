<!DOCTYPE html>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%! Logger log = LoggerFactory.getLogger("selectChatStatusListJsp"); %>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/include.css"><c:param name="v" value="${staticUpdateParam}" /></c:url>" />
	<style type="text/css">
		.hidden {
			display: none;
		}
		.chat_list_top .list_filter li a i {
			background: none;
		}
		.chat_list_top .list_filter li a.asc i {
			background: url("<c:url value='/images/admin/list_down.png' />") no-repeat right 50% !important;
		}
		.chat_list_top .list_filter li a.desc i {
			background: url("<c:url value='/images/admin/list_up.png' />") no-repeat right 50% !important;
		}
	</style>
	<!--[if lt IE 9]>
	<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
	<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
	<![endif]-->
	<script src="<c:url value="/js/jquery-1.12.2.min.js" />"></script>
	<script src="<c:url value="/js/jquery-ui.1.9.2.min.js" />"></script>
	<script src="<c:url value='/js/menu.js'><c:param name="v" value="${staticUpdateParam}" /></c:url>"></script>
	<script src="<c:url value="/js/clipboard.min.js" />"></script>
	<script src="<c:url value="/js/swiper.min.js" />"></script>
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
		<div class="left_content_area counseling">
			<div class="left_content_head">
				<h2 class="sub_tit">대화방 조회</h2>
			</div>
			<div class="inner_nobg admin_counseling">
				<!-- 왼쪽: 대화방 목록 -->
				<form id="roomListForm" name="roomListForm" method="post" action="">
					<div class="counseling_left" id="dev_roomList">
						<input type="hidden" name="schRoomStatus" value="${schRoomStatus }" />
						<input type="hidden" name="schTerm" value="${schTerm }" />
						<input type="hidden" name="schSortColumn" value="${schSortColumn }" />
						<input type="hidden" name="schSortType" value="${schSortType }" />
						<input type="hidden" name="nowPage" value="${nowPage }" />
						<c:set var="totCount" value="0" />
						<c:if test="${not empty chatRoomList && chatRoomList.size() >= 0 }">
							<c:set var="totCount" value="${chatRoomList[0].tot_count }" />
						</c:if>
						<h3 class="sub_stit" data-select-status="${schRoomStatus}">
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
										<option value="counselor" <c:if test="${schType eq 'counselor' or schRoomStatus eq 'cnsrIng'}">selected</c:if> >상담직원</option>
									</c:if>
									<option value="cstmName" <c:if test="${schType eq 'cstmName' }">selected</c:if> >고객명</option>
									<option value="cstmId" <c:if test="${schType eq 'cstmId' }">selected</c:if> >고객ID</option><%--
									<c:if test="${schRoomStatus eq 'botEnd' || schRoomStatus eq 'cnsrEnd' }">
										<option value="endDate" <c:if test="${schType eq 'endDate' }">selected</c:if> >종료일자</option>
									</c:if>--%>
								</select>
								<input name="schText" type="text" value="${schText}" placeholder="검색어 입력, 날짜검색(ex:2018-01-31)" autocomplete="off" />
								<button type="button" class="btn_go_search dev_room_search_btn">검색하기</button>
							</div>
							<div class="chat_list_top">
								<ul class="list_filter">
									<li><a href="javascript:void(0);" data-schSortColumn="lastChat" class="dev_sortLi <c:if test='${schSortColumn eq \'lastChat\' }'>active ${schSortType }</c:if>" >최근대화순<c:if test="${schSortColumn eq 'lastChat' }"><i></i></c:if></a></li>
									<li><a href="javascript:void(0);" data-schSortColumn="name" class="dev_sortLi <c:if test='${schSortColumn eq \'name\' }'>active ${schSortType }</c:if>" >고객이름순<c:if test="${schSortColumn eq 'name' }"><i></i></c:if></a></li>
									<%-- <li><a href="javascript:void(0);" data-schSortColumn="title" class="dev_sortLi <c:if test='${schSortColumn eq \'title\' }'>active ${schSortType }</c:if>" >상담제목순<c:if test="${schSortColumn eq 'title' }"><i></i></c:if></a></li> --%>
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
								<c:if test="${schRoomStatus ne 'managerChange'}">
									<label for="checkAll"><span></span>전체 선택</label>
								</c:if>
								<c:if test="${schRoomStatus == 'cnsrWait' || schRoomStatus == 'cnsrIng' || schRoomStatus == 'cnstNotJoin' || schRoomStatus == 'cnsrChange'|| schRoomStatus == 'cstmIng'}">
								<button type="button" class="btn_change_staff dev_change_cnsr_btn"><i></i>상담직원 변경</button>
								</c:if>
							<c:choose>
								<c:when test="${schRoomStatus eq 'managerChange'}"><button type="button" class="btn_change_staff dev_change_manager_btn" style="margin-right: 3px;"><i></i>대화방 접속</button></c:when>
								<c:when test="${schRoomStatus eq 'cstmGrade'}"><button type="button" class="btn_change_staff dev_cstm_grade_btn" style="margin-right: 3px;"><i></i>코끼리 등록</button></c:when>
							</c:choose>
							</div>
							<div id="dev_chatRoomListDiv" style="height:700px" class="chatting_list_area <c:if test="${schRoomStatus eq 'botEnd' || schRoomStatus eq 'cnsrEnd'}">end</c:if> ">
								<ul class="chatting_list" id="dev_chatRoomListUl">
									<c:forEach var="data" items="${chatRoomList }" varStatus="status">
										<li data-chat-room-uid="${data.chat_room_uid}" data-cstm-uid="${data.cstm_uid}"
											style="<c:if test="${data.end_info_yn eq 'Y'}">background-color: #f5f5f5</c:if><c:if test="${data.end_info_yn ne 'Y'}">background-color: #ffb28e</c:if>"
											data-grade-memo="${data.grade_memo}"
											<c:if test="${not empty data.chng_req_num}">data-chng-req-num="${data.chng_req_num}"</c:if>
											<c:if test="${not empty data.review_req_num}">data-review-req-num="${data.review_req_num}"</c:if>
											>
											<div class="icon_area">
												<c:if test="${schRoomStatus ne 'botIng' && schRoomStatus ne 'botEnd' && schRoomStatus ne 'cnsrEnd'}">
													<div class="floatL">
														<input type="checkbox" class="checkbox_18" name="chatRoomUid" id="cr_${data.chat_room_uid}"><!-- id="check_staff01_${status.index}" -->
														<label for="cr_${data.chat_room_uid}"><span></span></label>
													</div>
												</c:if>
												<c:choose>
													<c:when test="${data.cstm_link_div_cd eq 'B' }"><i class="icon_kakao" title="카카오톡">카카오톡</i></c:when>
													<c:when test="${data.cstm_link_div_cd eq 'C' }"><i class="icon_o2" title="O2톡">O2톡</i></c:when>
													<c:when test="${data.cstm_link_div_cd eq 'D' }"><i class="icon_mpop" title="mPOP톡">mPOP톡</i></c:when>
													<c:otherwise><i class="icon_happytalk" title="삼성증권 웹채팅">삼성증권 웹채팅</i></c:otherwise>
												</c:choose>
												<c:if test="${not empty data.pass_time_nm && data.pass_time_nm ne '' }">
													<i class="icon_time" title="상담 시작 시간 : ${data.room_create_datetime }">${data.pass_time_nm }</i>
												</c:if>
												<!-- <i class="icon_alarm">알람</i> -->
												<c:if test="${not empty data.room_mark_class && data.room_mark_class ne '' }">
													<i class="${data.room_mark_class }" title="${data.mark_desc}">${data.mark_desc }</i>
												</c:if>
												<c:if test="${not empty data.grade_class_name && data.grade_class_name ne '' }">
													<i class="${data.grade_class_name }" title="${data.grade_memo }">${data.grade_desc }</i>
												</c:if>
												<span class="right_id">
													<c:choose>
														<c:when test="${empty data.manager_nm }">(#${data.counselor_nm } > ${data.counselor_nm })</c:when>
														<c:otherwise>(#${data.manager_nm } > ${data.counselor_nm })</c:otherwise>
													</c:choose>
												</span>
											</div>
											<a href="javascript:void(0);" class="dev_roomListLi">
												<c:if test="${not empty data.room_coc_nm}">
												<p class="chat_tit">${data.room_coc_nm}</p>
												</c:if>
												<c:if test="${empty data.room_coc_nm}">
												<p class="chat_tit">익명</p>
												</c:if>
												<span class="date" title="${data.last_chat_datetime }">${data.last_chat_date }</span>
											</a>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</div>
				</form>
				<!--// 왼쪽: 대화방 목록 -->

				<!-- 중간: 대화창 영역 -->
				<div class="counseling_mid" style="display:none;">
					<div class="counsel_title_area">
						<span class="labelBlue">진행상태</span><span class="room_title"></span>
						<button type="button" class="btn_check_out hidden" data-review-req-num><i></i>검토완료</button>
						<span class="start_date">상담시작 : <span class="room_start_datetime"></span></span>
					</div>
					<div class="counsel_chat_info">
						<ul class="info_list">
							<li class="category_info">
								<p class="label_area"><span class="labelBlue">상담구분</span></p>
								<p class="text_area"><span></span><%--<button type="button" class="btn_chat_s">#TODO:태그등록</button>--%></p>
							</li>
							<li class="customer_info">
								<p class="label_area"><span class="labelBlue">고객ID</span></p>
								<p class="text_area"><span class="target_customer_id">D</span><%--
									<button type="button" class="btn_chat_s btn_id" title="해당 상담 고객을 구분할 수 있는 유니크한 ID 혹은 Key를 입력하세요. 한번입력된 값은 수정하지 않으면 계속 유지 되어 고객기 누구인지 구분하게 됩니다." data-html="true">고객 ID입력</button>--%>
									<button type="button" class="btn_copy" data-clipboard-target=".customer_info .target_customer_id">복사</button></p>
							</li>
							<li class="customer_info">
								<p class="label_area"><span class="labelBlue">고객이름</span></p>
								<p class="text_area"><span class="target_customer_name"></span>
							</li>
							<li class="customer_info">
								<p class="label_area"><span class="labelBlue">고객생일</span></p>
								<p class="text_area"><span class="target_customer_birthday"></span>
							</li>
							<li class="customer_info">
								<p class="label_area"><span class="labelBlue">고객성별</span></p>
								<p class="text_area"><span class="target_customer_gender"></span>
							</li><%--
							<li>
								<p class="label_area"><span class="labelBlue">파라미터</span></p>
								<p class="text_area rows">
									<span class="text_parameter">[상담직원]최선혁<button type="button" class="btn_copy">복사</button></span>
									<span class="text_parameter">[UUID]1000000000_GpesF20180813113134<button type="button" class="btn_copy">복사</button></span>
									<span class="text_parameter">[파라미터1]최선혁<button type="button" class="btn_copy">복사</button></span>
									<span class="text_parameter">[파라미터1]최선혁<button type="button" class="btn_copy">복사</button></span>
								</p>
							</li>--%>
						</ul>
						<button type="button" class="btn_info_more">상세보기</button>
						<button type="button" class="btn_info_close" style="display: none;">상세보기 닫기</button>
					</div>

					<!-- 검토요청 글등록시 -->
					<div class="request_info hidden">
						<ul class="info_list">
						</ul>
						<button type="button" class="btn_request_more">상세보기</button>
						<button type="button" class="btn_request_close" style="display: none;">상세보기 닫기</button>
					</div>
					<!--// 검토요청 글등록시 -->

					<!-- 대화창 영역 -->
					<div class="chatting_area">
						<div id="chat_body">
						</div>
					</div>
					<!--// 대화창 영역 -->
				</div>
				<!--// 중간: 대화창 영역 -->
			</div>
		</div>
		<!--// left_area -->
	</div>

<!-- alret 창 -->
<div class="layer_alert" style="display:none;">
	<p class="alert_text"></p>
</div>
<!--// alret 창 -->

<!-- popup:상담직원 변경 -->
<div class="popup popup_change_staff" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">상담직원 변경하기 (재 배정)</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
		</div>
	</div>
</div>
<!-- // popup:상담직원 변경 -->

<!-- popup:대화방 접속 -->
<div class="popup popup_manager_staff" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">대화방 접속 옵션</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<div class="box_head_area">
				<h3 class="sub_stit">매니저 표시</h3>
				<label class="switch w90">
					<input class="switch-input dev-switch-input" type="checkbox" data-on-value="Y" data-off-value="N" checked >
					<span class="switch-label" data-on="사용" data-off="사용안함"></span>
					<span class="switch-handle"></span>
					<input type="hidden" id="managerDisp" name="managerDisp" class="dev-switch-input-value" value="Y">
				</label>
			</div>
			<div class="btn_popup_area">
				<button type="button" class="btn_popup_ok btn_save">확인</button>
				<button type="button" class="btn_popup_ok btn_cancel">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- // popup:대화방 접속 -->

<!-- popup:메모 -->
<div class="popup popup_memo" style="display: none" data-message-id="">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">메세지 수정</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<div class="popup_input_area">
				<textarea class="form_text"></textarea>
				<button type="button" class="btn_save">저장</button>
			</div>
		</div>
	</div>
</div>
<!--// popup:메모 -->
<!-- popup:자세히보기 -->
<div class="popup poopup_chat_detail" style="display: none;">
	<div class="inner">
		<div class="popup_head">
			<h2 class="popup_title"></h2>
			<button type="button" class="btn_close_detail">창닫기</button>
		</div>
		<div class="popup_body">
			<div class="detail_contents"></div>
		</div>
	</div>
</div>
<!-- popup:자세히보기 -->

<!-- popup:후처리 내역 -->
<div class="popup popup_end_modify" style="display: none">
</div>
<!--// popup:후처리 내역 -->
<!-- popup:상담정보-->
<div class="popup popup_info_view" style="display: none">
</div>
<!--// popup:상담정보 -->

<script type="text/javascript">

	$(document).on("change", ".popup_manager_staff .dev-switch-input", function() {
		var cnsPossibleYn;
		if($(this).prop("checked")){
			cnsPossibleYn = $(this).attr("data-on-value");
		}else{
			cnsPossibleYn = $(this).attr("data-off-value");
		}

		$(this).closest("label").find(".dev-switch-input-value").val(cnsPossibleYn);
	});

	// 채팅방 목록 스크롤 이벤트
	var scrollHandler = function(){
		var scrollT = $("#dev_chatRoomListDiv").scrollTop();
		var scrollH = $("#dev_chatRoomListDiv").height();
		var contentH = $("#dev_chatRoomListDiv").find("ul").height();
		if(scrollT + scrollH >= contentH*0.9){
			// 스크롤 이벤트 중지
			$("#dev_chatRoomListDiv").off("scroll", scrollHandler);

			var nowPage = $("#roomListForm [name='nowPage']").val();
			$("#roomListForm [name='nowPage']").val(nowPage*1 + 1);

			$.ajax({
				url : "<c:url value='/chatManage/selectRoomListMoreAjax' />",
				data : $("#roomListForm").serialize(),
				type : "post",
				success : function(result) {
					if(result.length > 100){
						$("#roomListForm #dev_chatRoomListUl").append(result);
						// 채팅방 목록 스크롤 이벤트 적용
						$("#dev_chatRoomListDiv").scroll(scrollHandler);
					}
				},
				complete : function() {
				}
			});
		}
	}

	$(document).ready(function() {

		// 채팅방 목록 스크롤 이벤트 적용
		$("#dev_chatRoomListDiv").scroll(scrollHandler);

		$(".datepicker").datepicker({
			dateFormat:"yy-mm-dd",
			prevText: '이전달',
			nextText: '다음달',
			monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			dayNames: ['일', '월', '화', '수', '목', '금', '토'],
			dayNamesShorts: ['일', '월', '화', '수', '목', '금', '토'],
			dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
			showMonthAfterYear: true,
			yearSuffix: '년'

		});
	});


	// 날짜 조건 보이기/숨기기
	function viewSchLayer(){
		var schRoomStatus = $("#roomListForm [name='schRoomStatus']").val();
		if(schRoomStatus == "botEnd" || schRoomStatus == "cnsrEnd"){
			$("#roomListForm .dev_view_date_sch").show();
			$("#roomListForm .dev_view_term_sch").show();
		}else{
			$("#roomListForm .dev_view_date_sch").hide();
			$("#roomListForm .dev_view_term_sch").hide();
		}
	}

	// 날짜 검색하가 버튼 클릭
	$(document).on("click", "#roomListForm .dev_date_search_btn", function() {
		var schDate = $("#roomListForm [name='schDate']").val();
		if(schDate == ""){
			alert("날짜를 선택해 주세요.");
			return false;
		}
		$("#roomListForm [name='schTerm']").val("");

		goSearch();
	});

	// 기간 검색 탭 클릭
	$(document).on("click", "#roomListForm .dev_termLi", function() {
		var schTerm = $(this).attr("data-schTerm");
		$("#roomListForm [name='schTerm']").val(schTerm);
		$("#roomListForm [name='schDate']").val("");
		$("#roomListForm select[name='schType'] option:eq(0)").prop("selected", "selected");
		$("#roomListForm [name='schText']").val("");
		$("#roomListForm [name='schSortColumn']").val("");
		$("#roomListForm [name='schSortType']").val("");

		goSearch();
	});

	// 검색창 엔터 처리
	$(document).on("keypress", "#roomListForm [name='schText']", function(e) {
		if(e.keyCode == 13){
			e.preventDefault();
			$("#roomListForm .dev_room_search_btn").trigger("click");
		}
	});

	// 검색하기 클릭
	$(document).on("click", "#roomListForm .dev_room_search_btn", function() {
		var schType = $("#roomListForm select[name='schType']").val();
		if(schType == "endDate"){
			var schText = $("#roomListForm [name='schText']").val();
			if(!fn_checkDate(schText)){
				alert("날짜 형식을 정확하게 입력해 주세요.");
				$("#roomListForm [name='schText']").val("");
				$("#roomListForm [name='schText']").focus();
				return false;
			}
		}

		goSearch();
	});

	// 날짜 형식 체크
	function fn_checkDate(inDate){
		var pattern1 = /[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])/;
		var pattern2 = /[0-9]{4}.(0[1-9]|1[0-2]).(0[1-9]|[1-2][0-9]|3[0-1])/;
		var pattern3 = /[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])/;
		if(pattern1.test(inDate)){
			return true;
		}
		if(pattern2.test(inDate)){
			return true;
		}
		if(pattern3.test(inDate)){
			return true;
		}
		return false;
	}

	// 정렬 클릭
	$(document).on("click", "#roomListForm .dev_sortLi", function() {
		var schSortColumn = $(this).attr("data-schSortColumn");
		$("#roomListForm [name='schSortColumn']").val(schSortColumn);

		$(this).closest("ul").find("a").removeClass("active");
		$(this).closest("ul").find("a").find("i").remove();

		$(this).html($(this).text()+"<i></i>");
		$(this).addClass("active");
		if($(this).hasClass("asc")){
			$(this).removeClass("asc").addClass("desc");
			$("#roomListForm [name='schSortType']").val("desc");
		}else{
			$(this).removeClass("desc").addClass("asc");
			$("#roomListForm [name='schSortType']").val("asc");
		}
		goSearch();
	});

	// 검색하기
	function goSearch(){
		$("#roomListForm [name='nowPage']").val("1");
		$.ajax({
			url : "<c:url value='/chatManage/selectRoomListAjax' />",
			data : $("#roomListForm").serialize(),
			type : "post",
			success : function(result) {
				$("#roomListForm #dev_roomList").html(result);
			},
			complete : function() {
				$("#dev_chatRoomListDiv").off("scroll", scrollHandler);
				$("#dev_chatRoomListDiv").scroll(scrollHandler);
				$(".datepicker").datepicker({
					dateFormat:"yy-mm-dd",
					prevText: '이전달',
					nextText: '다음달',
					monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
					monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
					dayNames: ['일', '월', '화', '수', '목', '금', '토'],
					dayNamesShorts: ['일', '월', '화', '수', '목', '금', '토'],
					dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
					showMonthAfterYear: true,
					yearSuffix: '년'

				});
			}
		});
	}

	// 전체선택 클릭
	$(document).on("click", "#roomListForm #checkAll", function() {
		var chk = $(this).is(":checked");
		if(chk){
			$("#roomListForm input[name='chatRoomUid']").prop("checked", true);
		}else{
			$("#roomListForm input[name='chatRoomUid']").prop("checked", false);
		}
	});
	//매니저 상담일 경우 각 항목 체크할때 모두 체크해제
	<c:if test="${schRoomStatus eq 'managerChange'}">
		$(document).on("click", "#roomListForm input[name='chatRoomUid']", function() {
			var chk = $(this).is(":checked");
			if(chk){
				$("#roomListForm input[name='chatRoomUid']").prop("checked", false);
				$(this).prop("checked", true);
			}else{
				$("#roomListForm input[name='chatRoomUid']").prop("checked", false);
			}
		});
	</c:if>



</script>
<c:if test="${!isProduction}">
<form id="debug" style="display: none;">
	<input type="hidden" name="profile" value="false" />
</form>
</c:if>
</body>
</html>
<% log.info("selectChatStatusListJsp: {}", System.currentTimeMillis()); %>