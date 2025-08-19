<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		.text_box .type_text {
			z-index: 1;
		}
	</style>
</head>

<body id="admin" style="max-height:99%;">
	<!-- head -->
	<div class="head">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
	</div>
	<!--// head -->

	<div class="container2" style="min-height:750px;">
		<!-- 왼쪽: 대화방 목록 -->
		<div class="counseling_left w932">
			<ul class="tabs_icon">
				<li class="wait_counselor" style="display: none;">
					<a href="javascript:void(0);">
						<span class="badge">0</span>
						<i class="icon01">미배정</i>
					</a>
				</li>
				<li class="assign_counselor">
					<a href="javascript:void(0);">
						<span class="badge">0</span>
						<i class="icon01">미접수</i>
					</a>
				</li><%--
				<li>
					<a href="javascript:void(0);">
						<span>0</span>
						<i class="icon02">알림설정</i>
					</a>
				</li>--%>
				<li class="need_answer">
					<a href="javascript:void(0);" class="active">
						<span class="badge">0</span>
						<i class="icon03">응대필요</i>
					</a>
				</li>
				<li class="wait_reply">
					<a href="javascript:void(0);">
						<span class="badge">0</span>
						<i class="icon04">고객 답변 대기중</i>
					</a>
				</li><%--
				<li>
					<a href="javascript:void(0);">
						<span>0</span>
						<i class="icon05">협렵업체 응답 확인</i>
					</a>
				</li>
				<li>
					<a href="javascript:void(0);">
						<span>0</span>
						<i class="icon06">협렵업체 답변 확인</i>
					</a>
				</li>--%>
				<li class="end_counselor">
					<a href="javascript:void(0);">
						<span class="badge">0</span>
						<i class="icon07">상담 종료</i>
					</a>
				</li><%--
				<c:if test="${'M' == user.member_div_cd}">
				<li class="manager_counsel">
					<a href="javascript:void(0);">
						<span class="badge">0</span>
						<i class="icon06">매니저 상담중</i>
					</a>
				</li>
				</c:if>--%>
				<li class="all">
					<a href="javascript:void(0);">
						<i class="icon08">전체 목록</i>
					</a>
				</li>
			</ul>
			<div class="tab_icon_content counseling">
				<div class="title_area">
					<span>응대필요</span>
					<button type="button" class="btn_close_tabcont">창닫기</button>
				</div>
				<div class="chat_search_area">
					<input type="text" id="q" name="q" placeholder="채팅 내용 검색" autocomplete="off" />
					<button type="button" class="btn_go_search">검색하기</button><%--
					<button type="button" class="btn_go_refresh">다시하기</button>--%>
				</div>
				<div class="chat_list_top">
					<ul class="list_filter" data-order-current="chat+desc">
						<li><a href="javascript:void(0);" class="active order_default desc" data-order-default="chat+desc" data-order-reverse="chat+asc">최근대화순<i></i></a></li>
						<li><a href="javascript:void(0);" data-order-default="cstm+asc" data-order-reverse="cstm+desc">고객이름순</a></li>
						<li><a href="javascript:void(0);" data-order-default="cnsr_time+desc" data-order-reverse="cnsr_time+asc">최초응대순</a></li>
						<li class="end_review" style="display: none;"><a href="javascript:void(0);" data-order-default="end_review+desc" data-order-reverse="end_review+asc">후처리순</a></li>
						<li><a href="javascript:void(0);"  data-order-default="channel+desc" data-order-reverse="channel+asc">채널순<i></i></a></li>
					</ul><%--
					<select class="select_filter">
						<option>All</option>
					</select>--%>
				</div>
				<div class="chatting_list_area">
					<ul class="chatting_list">
					</ul>
				</div>
			</div>
		</div>
		<!--// 왼쪽: 대화방 목록 -->

		<!-- 중간: 대화창 영역 -->
		<div class="counseling_mid dev_mid_manual">
			<!-- 상담하기 안내 슬라이더 -->
			<div class="counselling_manual">
				<div class="counselling_app_download">
					<p><a href="/happytalk/images/user_manual.pdf" target="_blank">상담직원님! <span style="color:#368eff;">[사용법]</span>을 참조하세요.</a></p><%--
					<p><a href="<c:url value='/images/user_manual.pdf' />" target="_blank">상담직원님! 아래 사용법을 참조하세요.</a></p>--%>
					<div class="dowonload_area" style="display:none;">
						다운로드 링크
						<button type="button" class="btn_go_google">구글플레이</button>
						<button type="button" class="btn_go_appstore">앱스토어</button>
					</div>
				</div>
				<div class="manual_slide">
					<div class="swiper-wrapper">
						<%--<div class="swiper-slide"><img src="<c:url value='/images/admin/logo_OK.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help02.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help03.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help04.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help05.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help06.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help07.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help08.png' />" alt=""></div>
						<div class="swiper-slide"><img src="<c:url value='/images/admin/happytalk_help09.png' />" alt=""></div>--%>
					</div>
				</div><%--
				<div class="swiper-pagination"></div>
				<div class="chat_slide_controls"><%--
					<a href="#" class="btn_manual_prev">이전으로</a>
					<a href="#" class="btn_manual_next">다음으로</a>
				</div>--%>
			</div>
			<!--// 상담하기 안내 슬라이더 -->
		</div>
		<!--// 중간: 대화창 영역 -->

		<!-- 중간: 대화창 영역 -->
		<div class="counseling_mid dev_mid_chat" style="display:none;">
			<div class="counseling_mid_top">
				<a href="javascript:void(0);"><i></i><span id="titleCocNm">고객</span></a>
				<button type="button" class="btn_end_modify" style="line-height: normal;float: right;margin: 5px 10px 0 0;"><i></i>후처리 입력 </button>
			</div>
			<div class="counsel_title_area">
				<span class="labelBlue">접수대기</span><span class="room_title"></span>
				<span class="start_date">상담시작 : <span class="room_start_datetime"></span></span>
			</div>	
			<div class="counsel_chat_info">
				<div style="float: left; width: 48%;">
				<ul class="info_list">
					<li class="customer_info">
						<p class="label_area"><span class="labelBlue">고객이름</span></p>
						<p class="text_area">
							<span class="target_customer_name"></span>
							(<span class="target_customer_id"></span>)
							<button type="button" class="btn_copy target_customer_id" data-clipboard-target=".customer_info .target_customer_id">복사</button>							
							<span class="target_auth_info"></span>
							<span class="target_elf_info"></span>
						</p>
					</li>
					<li class="customer_info">
						<p class="label_area"><span class="labelBlue">고객기기</span></p>
						<p class="text_area"><span class="target_customer_device"></span></p>
					</li>
					<li class="customer_info">
						<p class="label_area"><span class="labelBlue">고객구분</span></p>
						<p class="text_area"><span class="target_customer_type"></span></p>
					</li>
				</ul>
				</div>
				<div style="float: right; width: 44%;">
				<ul class="info_list">
					<li class="customer_info">
						<p class="label_area"><span class="labelBlue">인입채널</span></p>
						<p class="text_area"><span class="target_customer_channel"></span></p>
					</li>
					<li class="customer_info">
						<p class="label_area"><span class="labelBlue">고객ID</span></p>
						<p class="text_area"><span class="target_customer_id"></span><%--
							<button type="button" class="btn_chat_s btn_id" title="해당 상담 고객을 구분할 수 있는 유니크한 ID 혹은 Key를 입력하세요.<br/>한번입력된 값은 수정하지 않으면 계속 유지 되어 고객기 누구인지 구분하게 됩니다." data-html="true">TODO:고객 ID입력</button>--%>
							<button type="button" class="btn_copy target_customer_id" data-clipboard-target=".customer_info .target_customer_id">복사</button>
						</p>
					</li>
					<li class="dev_elephant_area" style="display:none;">
						<p class="label_area"><span class="labelBlue">코끼리사유</span></p>
						<p class="text_area"><span class="target_elf_info"></span></p>
					</li>		
				</ul>
				</div>
				
				<button type="button" class="btn_info_more">상세보기</button>
				<button type="button" class="btn_info_close" style="display: none;">상세보기 닫기</button>
			</div>

			<!-- 메모 목록 표시-->
			<div class="request_info hidden">
				<ul class="info_list">
				</ul>
				<button type="button" class="btn_request_more">상세보기</button>
				<button type="button" class="btn_request_close" style="display: none;">상세보기 닫기</button>
			</div>
			<!--// 메모 목록 표시 -->

			<!-- 대화창 영역 -->
			<div class="chatting_area">
				<div id="chat_body">
				</div>
				<div id="chat_bottom">
					<!-- 템플릿 선택 보기 창 -->
					<div class="layer_selected_template" style="display: none">
						<button type="button" class="btn_close_temp">레이어닫기</button>
						<div class="right-chat-wrap">
						</div>
						<div class="left-chat-wrap">
						</div>
					</div>
					<!--// 템플릿 선택 보기 창 -->
					<div class="bottom_top_area">
						<div class="left">
							<button type="button" class="btn_send_file02" style="display:none;"><i></i>이미지전송</button>
							<input type="file" class="hidden" id="upload_file" />
							<i class="state_customer idle" title="고객상태">고객상태</i>
						</div>
						<!-- <div style="float:right; margin-right:20px;">
							<ul class="etc_plus_select">
								<li><button type="button" class="btn_etc15 btn_cstm_sinfo" data="1"><i></i></button>연락처 정보 요청</li>
							</ul>
						</div> -->
						<div id="autoCmpContent" class="text_box" style="display:none;">
						</div>
						<div class="right">
							<!-- <button type="button" class="btn_etc dev_btn_cstm_sinfo" style="float:right; margin-top:3px"><i></i>정보요청</button> -->
							<c:if test="${!empty openChatRoom}">
							<button type="button" class="btn_etc btn_end_manager_counsel" style="float:right; width: 80px; margin-top:3px; margin-right: 3px;"><i></i>매니저 종료</button>
							</c:if>
							<!-- <button type="button" class="btn_end_modify" style="float:right; margin: 3px;"><i></i>후처리 입력</button> -->
						</div>
					</div>

					<div class="textarea_area">
					<!-- placeholder="채팅" -->
						<textarea style="width:100%"></textarea>
						<div class="btn_area">
							<button type="button" class="btn_send02">보내기</button>
						</div>
					</div>
					<div class="bottom_down_area">
						<div class="left">
							<input type="checkbox" class="checkbox_18" id="check_enter_key" checked="checked" />
							<label for="check_enter_key"><span></span>엔터로 전송</label>
							<input type="checkbox" class="checkbox_18" id="check_auto_cmp" checked="checked" />
							<label for="check_auto_cmp"><span></span>자동완성 사용</label>
						</div>
						<div class="right">
							<button type="button" class="btn_end">상담종료</button>
							<button type="button" class="btn_etc dev_btn_etc">부가기능</button>
							<button type="button" class="btn_meno"><i></i>메모</button>


						</div>
					</div>
				</div>
			</div><!--// chat_body -->
			<!--// 대화창 영역 -->
		</div>
		<!--// 중간: 대화창 영역 -->

		<!-- 오른쪽: 탭메뉴 -->
		<div class="counseling_right">
			<ul class="tabs_menu">
				<li class="dev_tabMenu" id="dev_tabMenu1">상담이력</li>
				<li class="dev_tabMenu" id="dev_tabMenu2">상담템플릿 관리</li><%--
				<li class="dev_tabMenu" id="dev_tabMenu3">지식화 관리</li>
				<li class="dev_tabMenu" id="dev_tabMenu4">공지사항</li>--%>
				<li class="dev_tabMenu" id="dev_tabMenu5">상담정보</li>
			</ul>
			<!-- tab01: 상담정보 -->
			<div class="tabs_menu_content dev_tab_contents">
			</div>
			<!--// tab01: 상담정보 -->

		</div>
		<!--// 오른쪽: 탭메뉴 -->
	</div>

<!-- popup:고객아이디 입력 -->
<div class="popup popup_id" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">고객아이디 입력</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text">해당 상담 고객을 구분할 수 있는 유니크한 ID 혹은 Key를 입력하세요.<br>
			한번 입력된 값은 수정하지 않으면 계속 유지 되어 고객이 누구인지 구분 하게 됩니다.</p>
			<div class="popup_input_area">
				<textarea class="form_text"></textarea>
				<button type="button" class="btn_save">저장</button>
			</div>
		</div>
	</div>
</div>
<!--// popup:고객아이디 입력 -->

<!-- popup:부가기능 -->
<div class="popup popup_etc" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">부가기능</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<ul class="etc_plus_select">
				<li><a class="btn_change_request"><button type="button" class="btn_etc01"><i></i></button>상담직원 변경 요청</a></li>
				<c:if test="${user.member_div_cd ne 'M'}">
				<li><a class="btn_manager_request"><button type="button" class="btn_etc05"><i></i></button>매니저 상담 요청</a></li>
				</c:if>
				
				<li id="btnEndKakao"><a class="btn_kakao_info"><button type="button" class="btn_etc11 btn_cstm_kakao"><i></i></button>고객 종료(인증정보삭제)</a></li>
				<li><a class="btn_kakao_info"><button type="button" class="btn_etc11 btn_cstm_kakao"><i></i></button>고객 종료(인증정보삭제)</a></li>
				
				<li><a class="btn_check_request"><button type="button" class="btn_etc02"><i></i></button>상담내용 검토 요청</a></li>
				<li><a class="btn_grade_request"><button type="button" class="btn_etc03 no"><i></i></button>코끼리 등록 요청</a><a href="javascript:void(0);" class="icon_info_text" title="채팅상담 시스템에서 관리할 블랙리스트 고객을 등록합니다."></a></li>
				<li><a class="btn_flag_request"><button type="button" class="btn_etc04"><i></i></button>Flag 달기</a> <a href="javascript:void(0);" class="icon_info_text" title="상대방을 flag로 표시해 방을 쉽게 찾아볼 수 있습니다."></a></li>
			</ul>
			<ul class="flag_select">
				<li><a href="javascript:void(0);"><i class="icon_flag_cancel">선택취소</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_red">flag 빨강색</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_orange">flag 오렌지색</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_yellow">flag 노란색</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_green">flag 녹색</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_blue">flag 파란색</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_violet">flag 보라색</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_atten">주의</i></a></li>
				<li><a href="javascript:void(0);"><i class="icon_flag_warning">경고</i></a></li>
			</ul>
		</div>
	</div>
</div>
<!--// popup:부가기능 -->

<!-- popup:고객정보요청 -->
<div class="popup popup_cstm_sinfo" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">고객정보요청</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<ul class="etc_plus_select">
				<li><button type="button" class="btn_etc11 btn_phone_info"><i></i></button>휴대폰 본인인증 안내</li>
				<li><button type="button" class="btn_etc11 btn_kakao_info"><i></i></button>카카오싱크 본인인증 안내</li>
				<!--<li><button type="button" class="btn_etc13 btn_simple_lon"><i></i></button>간편 대출 신청 링크 안내</li>
				<li><button type="button" class="btn_etc14 btn_check_account"><i></i></button>한도조회 링크 안내</li>
				<li class="require-auth"><button type="button" class="btn_etc12 btn_add_lon"><i></i></button>추가대출 신용조회 동의 안내</li>
				<li class="require-auth"><button type="button" class="btn_etc13 btn_cstm_sinfo" data="3"><i></i></button>고객카드정보 요청(민감정보)</li>
				<li class="require-auth"><button type="button" class="btn_etc14 btn_cstm_sinfo" data="2"><i></i></button>고객 계좌정보 요청(민감정보)</li>
				<li class="require-auth"><button type="button" class="btn_etc15 btn_cstm_sinfo" data="1"><i></i></button>연락처 정보 요청</li>
				<li><button type="button" class="btn_etc16 btn_cstm_sinfo" data="4"><i></i></button>기타 정보 요청</li>-->
			</ul>
		</div>
	</div>
</div>
<!--// popup:고객정보요청  -->

<!-- popup:메모 -->
<div class="popup popup_memo" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">메모남기기</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body" style="overflow:hidden;">
			<div class="popup_input_area">
				<textarea class="form_text"></textarea>
				<button type="button" class="btn_save">저장</button>
			</div>
		</div>
	</div>
</div>
<!--// popup:메모 -->

<!-- popup:상담직원 변경 요청 -->
<div class="popup popup_change_request" style="display: none">
	<div class="inner">
	</div>
</div>
<!--// popup:상담직원 변경 요청 -->

<!-- popup:상담내용 검토 요청 -->
<div class="popup popup_check_request" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">상담내용 검토 요청</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text">매니저에게 상담직원 내용을 검토 요청합니다.<br>
			검토 요청 내용을 입력하세요.</p>
			<div class="popup_input_area">
				<textarea class="form_text" placeholder="검토가 필요한 내용을 입력하세요."></textarea>
				<button type="button" class="btn_save">요청하기</button>
			</div>
		</div>
	</div>
</div>
<!--// popup:상담내용 검토 요청 -->

<!-- popup:코끼리 등록 -->
<div class="popup popup_grade_request" style="display: none">
	<div class="inner">
		<div class="popup_head">
			<h1 class="popup_tit">코끼리 등록</h1>
			<button type="button" class="btn_popup_close">창닫기</button>
		</div>
		<div class="popup_body">
			<p class="popup_text">코끼리 고객 등록
			
			<div class="staff_radio_area">
				<input type="radio" class="radio_18" id="flag_e" name="flagType" value="elephant">
				<label for="flag_e"><span></span>코끼리</label>
				<input type="radio" class="radio_18" id="flag_v" name="flagType" value="VIP">
				<label for="flag_v"><span></span>VIP</label>
				<input type="radio" class="radio_18" id="flag_b" name="flagType" value="Black">
				<label for="flag_b"><span></span>블랙</label>
			</div>
			
			<div class="popup_input_area">
				<textarea class="form_text" placeholder="코끼리 등록 사유를 입력하세요."></textarea>
				<button type="button" class="btn_save">등록하기</button>
			</div>
		</div>
	</div>
</div>
<!--// popup:코끼리 등록 -->

<!-- popup:후처리 내역 -->
<div class="popup popup_end_modify" style="display: none">
</div>
<!--// popup:후처리 내역 -->

<!-- popup:상담내역 -->
<div class="popup popup_counseling" style="display: none">
</div>
<!--// popup:상담내역 -->

<!-- layer:자세히보기 -->
<div class="popup poopup_chat_detail dev_detail_pop" style="display:none;z-index:600">
	<div class="inner">
		<div class="popup_head">
			<h2 class="popup_title dev_detail_pop_title"></h2>
			<button type="button" class="btn_close_detail dev_detail_pop_close">창닫기</button>
		</div>
		<div class="popup_body dev_detail_pop_cont">
			<div class="detail_contents"></div><%--
			<button type="button" class="btn_text_copy">복사하기</button>--%>
		</div>
	</div>
</div>



<form id="user" style="display: none;">
	<input type="hidden" name="id" value="<c:out value="${user.member_uid}" />" />
	<input type="hidden" name="cocId" value="<c:out value="${user.coc_id}" />" />
	<input type="hidden" name="nickName" value="<c:out value="${user.name}" />" />
	<input type="hidden" name="userType" value="<c:out value="${user.member_div_cd}" />" />
	<input type="hidden" name="managerId" value="<c:out value="${user.upper_member_uid}" />" />
	<input type="hidden" name="departCd" value="<c:out value="${user.depart_cd}" />" />
	<input type="hidden" name="rollType" value="C" />
</form>
<form id="initialActions" style="display: none;">
	<%-- 진입시 필요 액션 --%>
	<input type="hidden" name="openChatRoom" value="${openChatRoom}" />
	<input type="hidden" name="openChatRoomWithSystemMsg" value="${openChatRoomWithSystemMsg}" />
</form>
<!--[if lt IE 9]>
<script type="text/javascript" src="<c:url value="/js/html5shiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/html5shiv.printshiv.js" />"></script>
<![endif]-->
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
<script>

var chatRoomUid;
var autoCmpList;
var autoCmpAfterList;

$(window).on('load', function() {

	// 답변용 자동완성 목록
	$.ajax({
		url: '<c:url value="/autoCmp/selectAutoCmpListAjax" />',
		data: {
			autoCmpDiv: 'C'
		},
		type: 'post'
	}).done(function(result) {
		autoCmpList = result;
	});

	// 후처리 자동완성 목록
	$.ajax({
		url: '<c:url value="/autoCmp/selectAutoCmpListAjax" />',
		data: {
			autoCmpDiv: 'A'
		},
		type: 'post'
	}).done(function(result) {
		autoCmpAfterList = result;
	});

	// 후처리 자동완성 키 이벤트
	$('.popup_end_modify').on('keyup', 'textarea[name=memo]', function(e) {

		e.preventDefault();
		console.debug(e.keyCode);

		if (e.keyCode === 27) { // ESC시 자동완성 레이어 가리기
			if ($('#afterAutoCmp').is(':visible')) {
				$('#afterAutoCmp').empty();
				$('#afterAutoCmp').hide();
			}
		} else if (e.keyCode === 13) { // 엔터시 첫번째 자동완성 컨텐츠 선택
			if ($('#afterAutoCmp').is(':visible')) {
				if ($('#afterAutoCmp .auto_cmp').length > 0) {
					$(this).val($('#afterAutoCmp .auto_cmp:eq(0)').text());
					$('#afterAutoCmp').empty();
					$('#afterAutoCmp').hide();
				}
			}
		} else {
			fn_autoCmp('afterAutoCmp', $(this).val().trim());
		}
	});
});

/**
 * 키입력시 자동완성 (답변용, 후처리용)
 * @param String domId ('autoCmpContent': 답변용, 'afterAutoCmp': 후처리용)
 */
function fn_autoCmp(domId, inputText) {

	// 답변용 자동완성 옵션 판별
	if (domId === 'autoCmpContent'
			&& $('#check_auto_cmp').length === 1
			&& !$('#check_auto_cmp').is(":checked")) {
		$('#autoCmpContent').empty();
		return;
	}

	var autoCompleteList;
	if (domId === 'autoCmpContent') { // 답변용
		autoCompleteList = autoCmpList;
	} else { //후처리용
		autoCompleteList = autoCmpAfterList;
	}

	$('#' + domId).html('');

	if (inputText.length > 1) {
		var html = '';
		var validCnt = 0;

		if (autoCompleteList.length > 0) {
			for (var i = 0; i < autoCompleteList.length; i++) {
				var Lcontest = autoCompleteList[i].content.toLowerCase();
				var content = Lcontest.match(inputText.toLowerCase());
				if (content !== null) {
					var thisContent = autoCompleteList[i].content.replace(inputText,
							'<span class="point-blue-bg">' + inputText + "</span>");
					html += '<a onclick="javascript:fn_textChat('
						+ autoCompleteList[i].auto_cmp_id
						+ ', &#39;' + domId + '&#39;);"'
						+ ' class="auto_cmp inner_text type_text">';
					html += thisContent + '</a>';
					$('#' + domId).html(html);
					$('#' + domId).show();
					validCnt++;
				}
			}
			if (validCnt === 0) {
				$('#' + domId).hide();
			}
		}
	} else {
		$('#' + domId).hide();
	}
}

function fn_textChat(autoCmpId, domId) {
	if (domId === 'autoCmpContent') {
		for (i = 0; i < autoCmpList.length; i++) {
			if (autoCmpId === autoCmpList[i].auto_cmp_id) {
				console.debug(autoCmpList[i].content);
				$('#chat_bottom .textarea_area textarea').val(autoCmpList[i].content);
				$('#autoCmpContent').hide();
			}
		}
	} else {
		for (i = 0; i < autoCmpAfterList.length; i++) {
			if (autoCmpId === autoCmpAfterList[i].auto_cmp_id) {
				console.debug(autoCmpAfterList[i].content);
				$('.popup_end_modify .tCont textarea').val(autoCmpAfterList[i].content);
				$('#afterAutoCmp').hide();
			}
		}
	}
}

$(document).on("click", ".layer_selected_template .left_area .type_text", function() {

	$('#chat_bottom .layer_selected_template').hide();
	$('#chat_bottom .textarea_area textarea').val($(this).text());

});

// 채팅방 목록 클릭 시 우측 상담 이력 클릭 이벤트 발생
$(document).on("click", ".chatting_list li", function() {
	chatRoomUid = $(this).find(".chat-room").attr("data-room-id");
	$("#dev_tabMenu5").trigger("click", 'from CHAT ROOM LIST');

	$(this).closest("ul").find("li").removeClass("active");
	$(this).addClass("active");
});

// 템플릿 클릭 시 가운데 채팅창에 layer 표시
$(document).on("click", ".dev_dplListClick", function() {
	// 템플릿 목록 펼치기
	$(this).toggleClass("active");
	$(this).parent().next(".template_list li.text").slideToggle();
	var tplNum = $(this).attr("data-tplNum");

	$.ajax({
		url : "<c:url value='/chatCsnr/selectTemplateOne' />",
		data : {"tplNum" : tplNum},
		type : "post",
		success : function(result) {
			var replyCont = result.template.reply_cont;
			// TODO: 채팅창에 layer 표시
		},
		complete : function() {
		}
	});
});

// 카테고리 클릭
$(document).on("click", "#tplForm .dev_ctgList .dev_category_li", function() {
	var tplCtgNum = $(this).attr("data-tplCtgNum");
	$("#tplForm [name='schTplCtgNum']").val(tplCtgNum);

	$(this).siblings("li").removeClass("active");
	$(this).addClass("active");

	fn_searchTplList();

	$("#tplForm .dev_tabCtgList").show();
	$("#tplForm .dev_tabCtgEdit").hide();
});

// 웹/안드로이드/아이폰 클릭
/*
$(document).on("click", "#tplForm .dev_deviceTypeList li a", function() {
	var schDeviceType = $(this).attr("data-schDeviceType");

	$("#tplForm [name='schDeviceType']").val(schDeviceType);

	$(this).parent().siblings("li").find("a").removeClass("active");
	$(this).addClass("active");

	fn_searchTplList();
});
*/

// 검색 클릭
$(document).on('click', '#tplForm .btn_search', function(e) {

	e.preventDefault();
	e.stopPropagation();

	fn_searchTplList();
});

// 템플릿 폼 서브밋 방지
$(document).on('submit', '#tplForm', function(e) {

	e.preventDefault();
	e.stopPropagation();
});

// 템플릿 검색창 엔터
$(document).on('keyup', '#tplForm [name=schText], #autoCmpForm [name=schText]', function(e) {

	e.preventDefault();
	e.stopPropagation();

	if (e.keyCode === 13) {
		$('.btn_search', $(this).parent()).trigger('click', 'ENTER FROM INPUT');
	}

	return false;
});

// 템플릿 목록 조회
function fn_searchTplList(){

	$.ajax({
		url : "<c:url value='/chatCsnr/selectTemplateAjax' />",
		data : $("#tplForm").serialize(),
		type : "post",
		success : function(result) {
			$("#tplForm .dev_templateList").html(result);
			$('.bubble-templet img').on('error', function(e) {
				console.info($(this).attr('src'));
				$(this).attr('src', HT_APP_PATH + '/images/chat/no_image.png').attr('alt', 'NO IMAGE').addClass('chatImageCs');
			});			
		},
		complete : function() {
			$(".template_list li.text").hide();
			$("#tplForm .dev_tabCtgList").show();
			$("#tplForm .dev_tabCtgEdit").hide();
		}
	});
}

// 템플릿 입력창 초기화
function fn_templateClear(){
	$("#tplForm [name='tplNum']").val("");
	$("#tplForm [name='shareReqNum']").val("");
	$("#tplForm [name='oldOrgImgDelYn']").val("");
	$("#tplForm [name='oldOrgPdfDelYn']").val("");	
	$("#tplForm [name='oldTplImgUrl']").val("");
	$("#tplForm [name='oldMimeType']").val("");
	$("#tplForm [name='oldOrgImgNm']").val("");
	$("#tplForm [name='oldOrgImgPath']").val("");

	$("#tplForm select[name='tplCtgNum'] option:eq(0)").prop("selected", "selected");

//	$("#tplForm [name='androidYn']").prop("checked", true);
//	$("#tplForm [name='iphoneYn']").prop("checked", true);
//	$("#tplForm [name='webYn']").prop("checked", true);

	$("#tplForm [name='cstmQue']").val("");
	$("#tplForm [name='replyContText']").val("");

	$("#tplForm .dev_file").val("");
	$("#tplForm .dev_file_view_name").val("");
	$("#tplForm .dev_file_del_btn").hide();
	$("#tplForm .dev_file_pdf_del_btn").hide();
	$("#tplForm [name='tplPdfBtn']").val("");	

	$("#tplForm [name='linkBtnNmArr']").each(function(){
		$(this).val("");
	});

	$("#tplForm [name='linkUrlArr']").each(function(){
		$(this).val("");
	});

	$("#tplForm [name='shareYn']").prop("checked", false);
	$("#tplForm [name='topMarkYn']").prop("checked", false);
	$("#tplForm [name='kwdNmArr']").val("");

	$("#tplForm select[name='tplMsgDivCd']").val("TEXT");
	$("#tplForm select[name='tplMsgDivCd']").trigger("change");
}

//목록에서 수정하기 버튼 클릭
$(document).on("click", "#tplForm .dev_list_edit_btn", function() {
	fn_templateClear();

	var tplNum = $(this).attr("data-tplNum");
	var shareReqNum = $(this).attr("data-share_req_num")

	$.ajax({
		url : "<c:url value='/chatCsnr/selectTemplateOne' />",
		data : {"tplNum" : tplNum},
		type : "post",
		success : function(result) {
			var tplNum = result.template.tpl_num;
			var tplCtgNum = result.template.tpl_ctg_num;
			var tplMsgDivCd = result.template.tpl_msg_div_cd;
			var cstmQue = result.template.cstm_que;
			var replyCont = result.template.reply_cont;
			var oldOrgImgNm = result.template.org_img_nm;
			var oldOrgImgPath = result.template.org_img_path;
			
			var oldOrgPdfNm = result.template.org_pdf_nm;
			var oldOrgPdfPath = result.template.org_pdf_path;
			var oldMimeType = result.template.org_mime_type;
			var tplPdfBtn = result.template.tpl_pdf_btn;
/*
			var webYn = result.template.web_yn;
			var androidYn = result.template.android_yn;
			var iphoneYn = result.template.iphone_yn;
*/
			var shareYn = result.template.share_yn;
			var topMarkYn = result.template.top_mark_yn;
			var kwdList = result.template.templatekwdlist;
			var shareReqNum = result.template.share_req_num;
			var cns_frt_msg_img = result.template.cns_frt_msg_img;
			$("#tplForm [name='tplNum']").val(tplNum);
			$("#tplForm [name='shareReqNum']").val(shareReqNum);

			$("#tplForm select[name='tplCtgNum']").val(tplCtgNum);
/*			if(webYn != null && webYn == "Y"){
				$("#tplForm [name='webYn']").prop("checked", true);
			}else{
				$("#tplForm [name='webYn']").prop("checked", false);
			}
			if(androidYn != null && androidYn == "Y"){
				$("#tplForm [name='androidYn']").prop("checked", true);
			}else{
				$("#tplForm [name='androidYn']").prop("checked", false);
			}
			if(iphoneYn != null && iphoneYn == "Y"){
				$("#tplForm [name='iphoneYn']").prop("checked", true);
			}else{
				$("#tplForm [name='iphoneYn']").prop("checked", false);
			}
*/
			$("#tplForm select[name='tplMsgDivCd']").val(tplMsgDivCd);
			$("#tplForm select[name='tplMsgDivCd']").trigger("change");

			$("#tplForm [name='cstmQue']").val(cstmQue);
			//$("#tplForm [name='replyContText']").val(replyContText);
			$("#tplForm .dev_file_view_name").val(oldOrgImgNm);
			//$("#tplForm [name='oldTplImgUrl']").val(item.contents.fileNm);
			//$("#tplForm [name='oldMimeType']").val(item.contents.fileType);
			$("#tplForm [name='oldOrgImgNm']").val(oldOrgImgNm);
			$("#tplForm [name='oldOrgImgPath']").val(oldOrgImgPath);
			if (cns_frt_msg_img!='' && cns_frt_msg_img!=null) {
				$("#tplForm .dev_file_del_btn").show();
			}else {
				$("#tplForm .dev_file_del_btn").hide();
			}
			$("#tplForm [name='oldOrgImgDelYn']").val("N");
			$("#tplForm [name='cnsFrtMsgImg']").val(cns_frt_msg_img);

			$("#tplForm .dev_file_pdf_view_name").val(oldOrgPdfNm);
			$("#tplForm [name='oldOrgPdfNm']").val(oldOrgPdfNm);
			$("#tplForm [name='oldOrgPdfPath']").val(oldOrgPdfPath);
			$("#tplForm [name='oldMimeType']").val(oldMimeType);
			$("#tplForm [name='tplPdfBtn']").val(tplPdfBtn);
			
			if (oldOrgPdfNm!='' && oldOrgPdfNm!=null) {
				$("#tplForm .dev_file_pdf_del_btn").show();
			}else {
				$("#tplForm .dev_file_pdf_del_btn").hide();
			}
			$("#tplForm [name='oldOrgPdfDelYn']").val("N");			

			var jsonObject = JSON.parse(replyCont);
			var rtnHtml = "";
			var _sections = jsonObject.balloons[0].sections;
					for (var i=0;i<_sections.length;i++) {
						if(_sections[i].type == "text"){
							$("#tplForm [name='replyContText']").val(_sections[i].data);
						}else if (_sections[i].type == "action") {
							var _actions = _sections[i].actions;
							for (var x=0;x<_actions.length;x++) {
								$("#tplForm [name='linkBtnNmArr']").eq(x).val(_actions[x].name);
								$("#tplForm [name='linkUrlArr']").eq(x).val(_actions[x].data);
							}

						}
					}


			var kwdNmArrVal = "";
			$.each(kwdList, function(idx, item){
				if(idx != 0){
					kwdNmArrVal += ",";
				}
				kwdNmArrVal += item.kwd_nm;
			});
			$("#tplForm [name='kwdNmArr']").val(kwdNmArrVal);

			if(shareYn != null && shareYn == "Y"){
				$("#tplForm [name='shareYn']").prop("checked", true);
			}else{
				$("#tplForm [name='shareYn']").prop("checked", false);
			}

			if(topMarkYn != null && topMarkYn == "Y"){
				$("#tplForm [name='topMarkYn']").prop("checked", true);
			}else{
				$("#tplForm [name='topMarkYn']").prop("checked", false);
			}
//			$("#tplForm [name='replyContText']").focus();
		},
		complete : function() {
			$("#tplForm .dev_tabCtgList").hide();
			$("#tplForm .dev_tabCtgEdit").show();
		}
	});

});

//목록에서 삭제하기 버튼 클릭
$(document).on("click", "#tplForm .dev_list_del_btn", function() {

	if(confirm("해당 템플릿을 정말 삭제하시겠습니까?")){
		var tplNum = $(this).attr("data-tplNum");
		type = "template";
		$.ajax({
			url : "<c:url value='/chatCsnr/deleteTemplate' />",
			data : {"tplNum" : tplNum, "type" : type},
			type : "post",
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					// 성공
					fn_layerMessage(result.rtnMsg);
					// 템플릿 목록 재조회
					fn_searchTplList();
				}else{
					fn_layerMessage(result.rtnMsg);
				}
			},
			complete : function() {
			}
		});
	}
});

//이미지 보기
function fn_fileView() {
	var msgImgUrl = $("#tplForm [name='cnsFrtMsgImg']").val();
	if (msgImgUrl=='') {
		return false;
	}
	//alert (msgImgUrl);
	//location.href = msgImgUrl;
	window.open(msgImgUrl);
}

// 템플릿 추가하기 버튼 클릭
$(document).on("click", "#tplForm .dev_add_tpl_btn", function() {
	fn_templateClear();
	$("#tplForm .dev_tabCtgList").hide();
	$("#tplForm .dev_tabCtgEdit").show();
});

//템플릿 수정 취소하기 버튼
$(document).on("click", "#tplForm .dev_cancel_tbl_btn", function() {
	$("#tplForm .dev_tabCtgList").show();
	$("#tplForm .dev_tabCtgEdit").hide();
});

//템플릿 저장 버튼
$(document).on("click", "#tplForm .dev_save_tpl_btn", function(e) {
/*
	var androidYn = $("#tplForm [name='androidYn']").is(":checked");
	var iphoneYn = $("#tplForm [name='iphoneYn']").is(":checked");
	var webYn = $("#tplForm [name='webYn']").is(":checked");

	if(!androidYn && !iphoneYn && !webYn){
		alert("안드로이드, 아이폰, 웹 중 하나는 필수 체크 입니다.");
		return false;
	}
*/
	var cstmQue = $("#tplForm [name='cstmQue']").val().trim();
	if(cstmQue == ""){
		alert("고객질문을 입력하세요.");
		$("#tplForm [name='cstmQue']").focus();
		return false;
	}

	var replyContText = $("#tplForm [name='replyContText']").val().trim();
	if(replyContText == ""){
		alert("상담직원 답변을 입력하세요.");
		$("#tplForm [name='replyContText']").focus();
		return false;
	}

	var tplMsgDivCd = $("#tplForm [name='tplMsgDivCd'] option:selected").val();
	if(tplMsgDivCd == "NORMAL"){
		var checkFlag = false;
		var fileName = $("#tplForm [name='tplImgUrl']").val();
		var oldOrgImgPath = $("#tplForm [name='oldOrgImgPath']").val();
		var fileName1 = $("#tplForm [name='tplPdfUrl']").val();
		var oldOrgPdfPath = $("#tplForm [name='oldOrgPdfPath']").val();

		if(fileName != "" || oldOrgImgPath != "" || fileName1 != "" || oldOrgPdfPath != "" ){
			checkFlag = true;
		}

		var linkBtnNmArr = $("#tplForm [name='linkBtnNmArr']");
		var linkUrlArr = $("#tplForm [name='linkUrlArr']");


		$("#tplForm [name='linkBtnNmArr']").each(function(){
			 $(this).val($(this).val().trim());
			 $(this).siblings("[name='linkUrlArr']").val($(this).siblings("[name='linkUrlArr']").val().trim());

			 var linkBtnNm = $(this).val().trim();
			 var linkUrl = $(this).siblings("[name='linkUrlArr']").val().trim();
			 $(this).val(linkBtnNm);
			 $(this).siblings("[name='linkUrlArr']").val(linkUrl);

			 if(linkBtnNm != "" && linkUrl != ""){
					checkFlag = true;
			 }
		});

		if(!checkFlag){
			alert("이미지 또는 링크 정보 또는 파일을 입력하세요.");
			return false;
		}
	}

	var kwdNmArr = $("#tplForm [name='kwdNmArr']").val().replace(/ /g, "");
	$("#tplForm [name='kwdNmArr']").val(kwdNmArr);

	var form = $("#tplForm")[0];
	var formData = new FormData(form);

	e.preventDefault();

	$.ajax({
		url : "<c:url value='/chatCsnr/saveTemplate' />",
		processData : false,
		contentType : false,
		cache:false,
		data : formData,
		type : "post",
		success : function(result) {
			if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
				// 성공
				fn_layerMessage(result.rtnMsg);

				fn_templateClear();

				// 템플릿 목록 재조회
				fn_searchTplList();
				$("#tplForm .dev_tabCtgList").show();
				$("#tplForm .dev_tabCtgEdit").hide();
			}else{
				fn_layerMessage(result.rtnMsg);
			}
		},
		complete : function() {
		}
	});
});


//이미지 선택
$(document).on("change", "#tplForm .dev_file", function(){
	if(window.FileReader){  // modern browser
		var filename = $(this)[0].files[0].name;
		var type = $(this)[0].files[0].type;
		console.log("type : " + type);
		if(type.indexOf("image") === -1 ){
			alert("이미지파일이 아닙니다.");
			return false;
		}		
	}else {  // old IE
		var filename = $(this).val().split('/').pop().split('\\').pop();
	}

	//$(this).siblings('.dev_file_view_name').val(filename);
	$('.dev_file_view_name').val(filename);
	$(this).siblings(".dev_file_del_btn").show();
});

// 이미지 삭제
$(document).on("click", "#tplForm .dev_file_del_btn", function(){
	$(this).siblings(".dev_file").val("");
	//$(this).siblings('.dev_file_view_name').val("");
	$('.dev_file_view_name').val("");
	$("#tplForm [name='oldOrgImgDelYn']").val("Y");
	$("#tplForm [name='cnsFrtMsgImg']").val("");
	$("#tplForm [name='oldOrgImgPath']").val("");
	$(this).hide();
});


// pdf 선택
$(document).on("change", "#tplForm .dev_file_pdf", function(){
	if(window.FileReader){  // modern browser
		var filename = $(this)[0].files[0].name;
		var type = $(this)[0].files[0].type;
		console.log("type : " + type);
		if(type.indexOf("pdf") === -1 ){
			alert("PDF파일이 아닙니다.");
			$("#tplForm [name='tplPdfUrl']").val("");
			return false;
		}		
	}else {  // old IE
		var filename = $(this).val().split('/').pop().split('\\').pop();
	}

	//$(this).siblings('.dev_file_view_name').val(filename);
	$('.dev_file_pdf_view_name').val(filename);
	//alert(filename);
	$(this).siblings(".dev_file_pdf_del_btn").show();
});

//  pdf 삭제
$(document).on("click", "#tplForm .dev_file_pdf_del_btn", function(){
	$(this).siblings(".dev_file_pdf").val("");
	//$(this).siblings('.dev_file_view_name').val("");
	//$('.dev_file').val("");
	$('.dev_file_pdf_view_name').val("");

	$("#tplForm [name='oldMimeType']").val("");
	$("#tplForm [name='oldOrgPdfNm']").val("");
	$("#tplForm [name='oldOrgPdfPath']").val("");		
	$("#tplForm [name='tplPdfBtn']").val("");
	$("#tplForm [name='oldOrgPdfDelYn']").val("Y");
	$(this).hide();
});


// 새창열기 버튼
$(document).on("click", "#tplForm .dev_btn_link_open", function() {
	var linkUrl = $(this).parent().find("input[name='linkUrlArr']").val();
	if(linkUrl == ""){
		alert("URL을 입력해 주세요.");
		$(this).parent().find("input[name='linkUrlArr']").focus();
		return false;
	}

	window.open(linkUrl);
});

// 템플릿 등록 구분 코드 변경
$(document).on("change", "#tplForm #tplMsgDivCd", function() {
	var tplMsgDivCd = $(this).val();
	if(tplMsgDivCd == "NORMAL"){
		$("#tplForm #tplNormal").show();
	}else{
		$("#tplForm #tplNormal").hide();
		$("#tplForm [name='tplImgUrl']").val("");

	}
});

//고객 입력 화면의 하이라이트 키워드 클릭 시 키워드 조회
$(document).on('click', '.type_text span.highlight', function() {

	// TODO: 상담이력 탭으로 변경

	var keyword = $(this).text().trim();
	$("#tplForm [name='schTplCtgNum']").val("");
	$("#tplForm [name='schType']").val("KEYWORD");
	$("#tplForm [name='schText']").val(keyword);

	var categoryCnt = $("#tplForm .dev_category_li").length - 1;

	$('.tabs_menu02').siblings("li").removeClass("active");
	$('#dev_tabMenuSub2').removeClass("active");
	$('#dev_tabMenuSub1').addClass("active");

	$("#tplForm .dev_category_li").removeClass("active");
	$("#tplForm .dev_category_li:eq("+categoryCnt+")").addClass("active");

	$(".dev_tab_sub_contents01" ).show();
	$(".dev_tab_sub_contents02").hide();

	fn_searchTplList();
});

//지식화 관리 초기화
$(document).on("click", "#knowForm .dev_know_new_btn", function() {
	fn_knowledgeClear();
});
$(document).on("click", "#knowForm .dev_know_clean_btn", function() {
	fn_knowledgeClear();
});

// 지식화 관리 초기화
function fn_knowledgeClear(){
	$("#knowForm select[name='botProjectId'] option:eq(0)").prop("selected", "selected");
	$("#knowForm [name='knowNum']").val("");
	$("#knowForm [name='cstmQue']").val("");
	$("#knowForm [name='replyContText']").val("");
}

// 지식화 관리 검색창 엔터
$(document).on("keyup", "#knowForm [name='schText']", function(e) {
	if(e.keyCode == 13){
		e.preventDefault();
		$("#knowForm .dev_know_search_btn").trigger("click");
	}
});

// 지식화 관리 검색 버튼 클릭
$(document).on("click", "#knowForm .dev_know_search_btn", function() {
	fn_knowledgeClear();
	fn_searchKnowList(1);
});

// 지식화 관리 목록 조회
function fn_searchKnowList(nowPage){
	$("#knowForm").find("[name='nowPage']").val(nowPage);
	$.ajax({
		url : "<c:url value='/chatCsnr/selectKnowListAjax' />",
		data : $("#knowForm").serialize(),
		type : "post",
		success : function(result) {
			if(nowPage == 1){
				$("#knowForm .dev_knowledgeList").html(result);
			}else{
				$("#knowForm .dev_knowledgeList").append(result);
			}
		},
		complete : function() {
			$(".template_list li.text").hide();
			// 지식화 관리 목록 스크롤 이벤트 적용
			$("#dev_knowledgeList").scroll(scrollHandlerKnow);
		}
	});
}

// 지식화 관리 목록 스크롤 이벤트
var scrollHandlerKnow = function(){
	var scrollT = $("#dev_knowledgeList").scrollTop();
	var scrollH = $("#dev_knowledgeList").height();
	var contentH = $("#dev_knowledgeList").find("ul").height();

	if(scrollT + scrollH >= contentH*0.9){
		// 스크롤 이벤트 중지
		$("#dev_knowledgeList").off("scroll", scrollHandlerKnow);

		var nowPage = $("#knowForm [name='nowPage']").val();
		fn_searchKnowList(nowPage*1 + 1);
	}
}


// 지식화 관리 목록에서 삭제하기 버튼 클릭
$(document).on("click", "#knowForm .dev_know_del_btn", function() {

	if(confirm("해당 지식을 정말 삭제하시겠습니까?")){
		var knowNum = $(this).attr("data-knowNum");
		$.ajax({
			url : "<c:url value='/chatCsnr/deleteKnowledge' />",
			data : {"knowNum" : knowNum},
			type : "post",
			success : function(result) {
				if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
					// 성공
					fn_layerMessage(result.rtnMsg);

					// 지식화 관리 목록 재조회
					fn_searchKnowList(1);
				}else{
					fn_layerMessage(result.rtnMsg);
				}
			},
			complete : function() {
			}
		});
	}
});

// 지식화 관리 목록에서 수정하기 버튼 클릭
$(document).on("click", "#knowForm .dev_know_edit_btn", function() {
	fn_knowledgeClear();

	var knowNum = $(this).attr("data-knowNum");

	$.ajax({
		url : "<c:url value='/chatCsnr/selectKnowledgeAjax' />",
		data : {"knowNum" : knowNum},
		type : "post",
		success : function(result) {
			var botProjectId = result.knowledge.bot_project_id;
			var knowNum = result.knowledge.know_num;
			var cstmQue = result.knowledge.cstm_que;
			var replyContText = result.knowledge.reply_cont_text;

			$("#knowForm [name='botProjectId']").val(botProjectId);
			$("#knowForm [name='knowNum']").val(knowNum);
			$("#knowForm [name='cstmQue']").val(cstmQue);
			$("#knowForm [name='replyContText']").val(replyContText);
			$("#knowForm [name='cstmQue']").focus();
			$("#knowForm [name='cstmQue']").css('background-color', '#d8fbd7');
			setTimeout(function() {
				$("#knowForm [name='cstmQue']").css('background-color', '');
			}, 500);
		},
		complete : function() {
		}
	});

});


// 지식화 관리 저장 버튼
$(document).on("click", "#knowForm .dev_know_save_btn", function() {
	var cstmQue = $("#knowForm [name='cstmQue']").val().trim();
	if(cstmQue == ""){
		alert("고객질문을 입력하세요.");
		$("#knowForm [name='cstmQue']").focus();
		return false;
	}

	var replyContText = $("#knowForm [name='replyContText']").val().trim();
	if(replyContText == ""){
		alert("상담직원 답변을 입력하세요.");
		$("#knowForm [name='replyContText']").focus();
		return false;
	}

	$.ajax({
		url : "<c:url value='/chatCsnr/saveKnowledge' />",
		data : $("#knowForm").serialize(),
		type : "post",
		success : function(result) {
			if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
				// 성공
				fn_layerMessage(result.rtnMsg);

				fn_knowledgeClear();

				// 지식화 관리 목록 재조회
				fn_searchKnowList(1);
			}else{
				fn_layerMessage(result.rtnMsg);
			}
		},
		complete : function() {
		}
	});
});

/* 메뉴얼 */
/*
var swiper02 = new Swiper('.manual_slide', {
	  pagination: {
		el: '.swiper-pagination',
		type: 'fraction',
	  },
	  navigation: {
		nextEl: '.btn_manual_next',
		prevEl: '.btn_manual_prev',
	  },
});

$(document).on("click", ".dev_mid_manual .btn_manual_prev", function() {
	var current = Manualslider.getCurrentSlide();
	Manualslider.goToPrevSlide(current) - 1;
});

$(document).on("click", ".dev_mid_manual .btn_manual_next", function() {
	var current = Manualslider.getCurrentSlide();
	Manualslider.goToNextSlide(current) + 1;
});
*/


// 자세히 보기
$(document).on("click", "#tplForm .tmenu02 .dev_goDetail", function() {
	$(".dev_detail_pop .dev_detail_pop_cont").html($(this).parent().prev().html());
	$(".dev_detail_pop").show();
});

// 자세히 보기 닫기
$(document).on("click", ".dev_detail_pop .dev_detail_pop_close", function() {
	$(".dev_detail_pop").hide();
});

</script>
<script src="<c:url value="/js/dayjs.min.js" />"></script>
<script src="<c:url value="/js/lodash.min.js" />"></script>
<form id="config" style="display: none;">
	<input type="hidden" name="happyTalkHostName" id="happyTalkHostName" value="${happyTalkHostName}" />
	<input type="hidden" name="happyTalkUrl" value="${happyTalkUrl}" />
	<input type="hidden" name="heungkukUrl" value="${heungkukUrl}" />
</form>
<c:if test="${!isProduction}">
<form id="debug" style="display: none;">
	<input type="hidden" name="profile" value="false" />
</form>
</c:if>
</body>
</html>
