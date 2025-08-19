'use strict';

var HT_APP_URL = null;
var HK_APP_URL = null;
var HAPPYTALK_HOST_NAME = null;

/**
 * 상담하기 페이지
 */
$(document).ready(function() {

	if ($('#config input[name=happyTalkHostName]').length === 1) {
		HAPPYTALK_HOST_NAME = $('#config input[name=happyTalkHostName]').val();
		console.debug(HAPPYTALK_HOST_NAME);
	}

	if ($('#config input[name=happyTalkUrl]').length === 1) {
		HT_APP_URL = $('#config input[name=happyTalkUrl]').val();
		console.debug(HT_APP_URL);
	}

	if ($('#config input[name=heungkukUrl]').length === 1) {
		HK_APP_URL = $('#config input[name=heungkukUrl]').val();
		console.debug(HK_APP_URL);
	}

	htUtils.setHtUser();

	if (htUser === null) {
		console.error('NO USER');
		return;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 채팅 클라이언트 설정
	(function() {
		htClient = new Client();

		// ////////////////////////////////////////////////////////////////////
		// 채팅방 목록
		var chatRoomList = new ChatRoomList(new ChatRoomListViewer({
			statusCounterContainer: $('#admin .counseling_left .tabs_icon'),
			chatRoomListTitleContainer: $('#admin .counseling_left .title_area'),
			searchContainer: $('#admin .counseling_left .chat_search_area'),
			searchOptionContainer: $('#admin .counseling_left .chat_list_top'),
			chatRoomListContainer: $('#admin .counseling_left .chatting_list'),
			chatRoomListContainerWrapper: $('#admin .counseling_left .chatting_list_area'),
		}));
		htClient.setChatRoomList(chatRoomList);

		// ////////////////////////////////////////////////////////////////////
		// 전역 및 사용자 설정 정보
		$.ajax({
			url: HT_APP_PATH + '/api/setting/' + htUser.getId(),
		}).done(function(data) {
			htUser.setSetting(data);
			// '상담가능/불가' 설정 적용
			$('.break_time .switch-input').prop('checked', data.member_work_status_cd === 'W');
			// '엔터로 전송' 설정 적용
			$('#check_enter_key').prop('checked', data.member_enter_use_yn === 'Y');
			// 수동, 자동 배정인지에 따라 미배정/미접수 숨김
//			if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {
//				if (data.auto_mat_use_yn === 'N' && self_choi_use_yn === 'Y') {
//					$('.tabs_icon .assign_counselor').hide();
//					$('.tabs_icon .wait_counselor').show();
//				}
//			}
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: LOAD SETTING: ', textStatus);
		});;
	})();

	// ////////////////////////////////////////////////////////////////////////
	// 대화 종료
	$('#chat_bottom .btn_end').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if (htClient.canSendMessage()) {
			htClient.sendEnd(htUser.getSetting().cnsr_end_msg);
		}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 사용자 정보
	// '사용자 정보 상세 보기' 버튼
	$('.btn_info_more').on('click', function(e) {
		$('.counsel_chat_info').css('height', '300px').addClass('active');
		$('.request_info').css('top', '390px');
		if ($('.request_info').css('height') > "40"){
			$('.request_info').css('overflow-y', 'hidden');
		}
		$(this).hide();
		$('.btn_info_close').show();
	});

	// '사용자 정보 상세 보기 닫기' 버튼
	$('.btn_info_close').click(function(e) {
		$('.counsel_chat_info').css('height', '100px').removeClass('active');
		$('.request_info').css('top', '190px').addClass('active');
		$(this).hide();
		$('.btn_info_more').show();
	});

	// ////////////////////////////////////////////////////////////////////////
	// 메모
	// '메모' 버튼
	$('#chat_bottom .btn_meno').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();
		$('.popup_memo').show();
	});

	// 메모 전송
	$('.popup_memo').on('click', '.btn_save', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var contents = $('.popup_memo .form_text').val().trim();
		console.error('MANAGER: ', htUser.getManagerId());
		htClient.sendMemo('메모: ' + contents, htUser.getManagerId());

		$('.popup_memo .form_text').val('');
		$('.popup_memo').hide();
	});

	// '메모 상세 보기' 버튼
	$('.btn_request_more').on('click', function(e) {
		$('.request_info').css('height', '300px').addClass('active');
		$('.request_info').css('overflow-y', 'auto');
		$(this).hide();
		$('.btn_request_close').show();
	});

	// '메모 상세 보기 닫기' 버튼
	$('.btn_request_close').on('click', function(e) {
		$('.request_info').css('height', '40px').removeClass('active');
		$('.request_info').css('overflow', 'hidden');
		$(this).hide();
		$('.btn_request_more').show();
	});

	// ////////////////////////////////////////////////////////////////////////
	// '정보요청' 버튼
	$('.dev_btn_cstm_sinfo').on('click', function(e) {

		console.debug('EVENT: ', '.dev_btn_cstm_sinfo');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();

		var isCertificated = false;

		$.ajax({
			url: HT_APP_PATH + '/api/chatCsnr/isCertificated',
			method: 'get',
			data: {
				chatRoomUid: htClient.getChatRoom().getId()
			},
		}).done(function(data, textStatus, jqXHR) {
			isCertificated = true;
		}).fail(function(jqXHR, textStatus, errorThrown) {
			;
		}).always(function() {
			console.info('isCertificated', isCertificated);
			$('.popup_cstm_sinfo .popup_body li').show();
			if (!isCertificated) {
				$('.popup_cstm_sinfo .popup_body li.require-auth').hide();
			}
			$('.popup_cstm_sinfo').show();
		});
	});

	// '휴대폰 인증 요청' 버튼
	$('.btn_phone_info').on('click', function(e) {

		console.debug('EVENT: ', '.btn_phone_info');
		e.preventDefault();
		e.stopPropagation();

		var channelId = htClient.getChatRoom()._cstmLinkDivCd;
		var cstmUid = htClient.getChatRoom()._cstmLinkCustomerUid;
		var deviceType = htClient.getChatRoom()._cstmOsDivCd;
		var url = 'https://' + LEGACY_MOBILE_HOST +'/#/cmn_lgin_nmbr_self_athz?landingFrom=oktalk&roomId='
				+ htClient.getChatRoom().getId() + '&custUuid=' + htClient.getChatRoom().getId() + '&selfAthzTpCd=00001';
		if (deviceType === 'pc') {
			url = 'https://' + LEGACY_PC_HOST + '/#/cmnWinMbphAthz?landingFrom=oktalk&roomId='
				+ htClient.getChatRoom().getId() + '&custUuid=' + htClient.getChatRoom().getId() + '&selfAthzTpCd=00001';
		}
		$('.popup').hide();

		var sendData = {
				v: '0.1',
				balloons: [
					{
						sections: [
							{
								type: 'text',
								data: '정확한 상담을 위해 본인인증이 필요합니다.\n\n'
									+ '본인인증은 본인명의 휴대폰으로 진행되며, 연체 또는 정지된 경우 인증이 불가합니다.\n\n'
									+ '주민등록법 제 37조에 의해 타인의 주민등록번호를 도용하였을 경우 형사처벌을 받을 수 있으니 참고 부탁 드립니다.'
							},
							{
								type: 'action',
								actions: [
									{
										type: 'link',
										name: '휴대폰 본인인증 하기',
										deviceType: deviceType,
										data: url
									}
								]
							}
						]
					}
				]
		};
		htClient.sendMessage(JSON.stringify(sendData), true);
	});

	// '카카오 싱크 인증 요청' 버튼  => 카카오 인증정보 삭제 요청 버튼
	$('.btn_kakao_info').on('click', function(e) {

		console.debug('EVENT: ', '.btn_kakao_info');
		e.preventDefault();
		e.stopPropagation();

		var channelId = htClient.getChatRoom()._cstmLinkDivCd;
		var cstmUid = htClient.getChatRoom()._cstmLinkCustomerUid;
		var chatroomUid = htClient.getChatRoom().getId();
		var htProtocol = location.protocol;
		var url = HT_APP_URL + '/api/customer/kakao/cstmExpire?cstmUid=' + cstmUid + '&chatRoomUid=' + chatroomUid;
		$('.popup').hide();

		var sendData = {
				v: '0.1',
				balloons: [
					{
						sections: [
							{
								type: 'text',
								data: '상담을 종료하고 싶으시면 아래 버튼을 눌러주세요.\n\n'
									+ '최초 상담시 우수고객 인증 정보가 삭제되며 상담이 종료됩니다.\n\n'
									+ '다시 상담하고 싶으시면 상담직원 연결을 눌러주세요.'
							},
							{
								type: 'action',
								actions: [
									{
										type: 'link',
										name: '상담 종료',
										deviceType: 'all',
										data: url,
										orient : 'V'
									}
								]
							}
						]
					}
				]
		};
		htClient.sendMessage(JSON.stringify(sendData), true);
	});

	// '간편 대출 신청' 버튼
	$('.btn_simple_lon').on('click', function(e) {

		console.debug('EVENT: ', '.btn_simple_lon');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();

		var channelId = htClient.getChatRoom()._cstmLinkDivCd;
		var cstmUid = htClient.getChatRoom()._cstmLinkCustomerUid;
		var deviceType = htClient.getChatRoom()._cstmOsDivCd;
		var url = 'https://' + LEGACY_MOBILE_HOST +'/#/gds_easy_intr_lmt100?landingFrom=oktalk&roomId='
		+ htClient.getChatRoom().getId() + '&custUuid=' + htClient.getChatRoom().getId() + '&selfAthzTpCd=00003';
		if (deviceType === 'pc') {
			url = 'https://' + LEGACY_PC_HOST +'/#/lon/esnsLonRqus000?menuCd=00411&landingFrom=oktalk&roomId='
			+ htClient.getChatRoom().getId() + '&custUuid=' + htClient.getChatRoom().getId() + '&selfAthzTpCd=00003';
		}

		var sendData = {
				v: '0.1',
				balloons: [
					{
						sections: [
							{
								type: 'text',
								data: '아래 메뉴를 선택하시면 간편대출 신청 페이지로 이동합니다.\n\n'
									+ '일정 시간이 경과되면 자동으로 상담이 종료되오니, 정상적인 접수를 위해 지금 바로 진행해 주세요.\n\n'
									+ '주민등록법 제 37조에 의해 타인의 주민등록번호를 도용하였을 경우 형사처벌을 받을 수 있으니 참고 부탁 드립니다.'
							},
							{
								type: 'action',
								actions: [
									{
										type: 'link',
										name: '간편대출 신청하기',
										deviceType: deviceType,
										data: url
									}
								]
							}
						]
					}
				]
		};
		htClient.sendMessage(JSON.stringify(sendData), true)
	});

	// '한도 조회' 버튼
	$('.btn_check_account').on('click', function(e) {

		console.debug('EVENT: ', '.btn_check_account');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();

		var channelId = htClient.getChatRoom()._cstmLinkDivCd;
		var cstmUid = htClient.getChatRoom()._cstmLinkCustomerUid;
		var deviceType = htClient.getChatRoom()._cstmOsDivCd;
		var url = 'https://' + LEGACY_MOBILE_HOST +'/#/gds_intr_lmt000?landingFrom=oktalk&roomId='
		+ htClient.getChatRoom().getId() + '&custUuid=' + htClient.getChatRoom().getId() + '&selfAthzTpCd=00002';
		if (deviceType === 'pc') {
			url = 'https://' + LEGACY_PC_HOST +'/#/lon/gdsIntrLmt100?landingFrom=oktalk&roomId='
			+ htClient.getChatRoom().getId() + '&custUuid=' + htClient.getChatRoom().getId() + '&selfAthzTpCd=00002&rcvPathCd=00178';
		}

		var sendData = {
				v: '0.1',
				balloons: [
					{
						sections: [
							{
								type: 'text',
								data: '아래 메뉴를 선택하시면 한도조회부터 송금까지 한번에 가능한 페이지로 이동합니다.\n\n'
									+ '한도 확인을 위해서는 신용조회가 필요하며, 대출가능여부 및 한도조회를 위한 신용조회는 신용등급에 어떠한 영향을 미치지 않습니다.\n\n'
									+ '주민등록법 제 37조에 의해 타인의 주민등록번호를 도용하였을 경우 형사처벌을 받을 수 있으니 참고 부탁 드립니다.'
							},
							{
								type: 'action',
								actions: [
									{
										type: 'link',
										name: '한도조회 하기',
										deviceType: deviceType,
										data: url
									}
								]
							}
						]
					}
				]
		};
		htClient.sendMessage(JSON.stringify(sendData), true)
	});

	// '공인인증서 인증 요청' 버튼
	$('.btn_author_info').on('click', function(e) {

		console.debug('EVENT: ', '.btn_author_info');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();

		var channelId = htClient.getChatRoom()._cstmLinkDivCd;
		var cstmUid = htClient.getChatRoom()._cstmLinkCustomerUid;
		var deviceType = htClient.getChatRoom()._cstmOsDivCd;
		var url = HK_APP_URL + '/CBM/mobileAuthSignProc.do?cbUserId=&position=T&type=A&uuid=' + cstmUid + '&channel=' + channelId;

		var sendData = {
				v: '0.1',
				balloons: [
					{
						sections: [
							{
								type: 'text',
								data: '개인화 서비스는 본인인증이 필요 합니다.\n본인인증은 본인명의의 수단을 이용하여 진행해주세요.\n\n★ 인증완료 후 아래 "인증완료했어요!!"를 눌러주세요~'
							},
							{
								type: 'action',
								actions: [
									{
										type: 'link',
										name: '공인인증서 인증 요청',
										deviceType: deviceType,
										data: url
									},
									{
										type: 'message',
										name: '인증완료했어요!!',
										extra: 'HappyTalk/CompleteCustomerAuth/' + htClient.getChatRoom().getId()
									}
								]
							}
						]
					}
				]
		};
		htClient.sendMessage(JSON.stringify(sendData), true)
	});

	// '민감 정보 요청' 버튼
	$('.btn_cstm_sinfo').on('click', function(e) {

		console.debug('EVENT: ', '.btn_cstm_sinfo');
		e.preventDefault();
		e.stopPropagation();

		var part = $(this).attr('data');
		var chatRoomUid = htClient.getChatRoom().getId();
		var deviceType = htClient.getChatRoom()._cstmOsDivCd;

		$('.popup').hide();

		var url = HT_APP_URL + '/customer/selectCstmSinfo?part=' + part + '&chatRoomUid=' + chatRoomUid;

		var sendData = {
				v: '0.1',
				balloons: [
					{
						sections: [
							{
								type: 'text',
								data: '안전한 정보 수집을 위해 새창을 통해 고객님의 정보를 입력받고 있어요.\n아래 버튼을 눌러 고객님의 소중한 정보를 입력해주세요.\n\n★ 입력완료 후 "입력했어요!!"도 눌러주세요~'
							},
							{
								type: 'action',
								actions: [
									{
										type: 'link',
										name: '민감정보 요청',
										deviceType: deviceType,
										data: url
									},
									{
										type: 'message',
										name: '입력했어요!!',
										extra: 'HappyTalk/CompleteCustomerAuth/' + htClient.getChatRoom().getId()
									}
								]
							}
						]
					}
				]
		};
		htClient.sendMessage(JSON.stringify(sendData), true);
	});
	
	// ////////////////////////////////////////////////////////////////////////
	// 부가기능
	// '부가기능' 버튼
	$('.dev_btn_etc').on('click', function(e) {

		console.info('EVENT: ', '.dev_btn_etc');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();
		$('.popup_etc').show();

		// 코끼리 등록 버튼 활성/비활성
		if (htClient.getChatRoom().getCustomerGradeName() === null) {
			$('.btn_grade_request').removeClass('no');
		} else {
			$('.btn_grade_request').addClass('no');
		}
	});

	// '상담직원 변경 요청' 버튼
	$('.btn_change_request').on('click', function(e) {

		console.info('EVENT: ', '.popup_change_request');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();
		$.ajax({
			url: HT_APP_PATH + '/api/member/available',
			data: {
				memberUid: htUser.getId(),
				rollType: htUser.getRollType(),
			},
			dataType: 'html',
		}).done(function(data) {
			$('.popup_change_request .inner').html(data);
			$('.popup_change_request').show();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: LOAD AVAILABLE MEMBER LIST: ', textStatus);
		});
	});

	// '매니저 상담 요청' 버튼
	$('.btn_manager_request').on('click', function(e) {
		console.info('EVENT: ', '.popup_change_request');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();
		$.ajax({
			url: HT_APP_PATH + '/api/member/available',
			data: {
				memberUid: htUser.getId(),
				rollType: htUser.getRollType(),
				managerFlag: 'M'
			},
			dataType: 'html',
		}).done(function(data) {
			$('.popup_change_request .inner').html(data);
			$('.popup_change_request').show();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: LOAD AVAILABLE MEMBER LIST: ', textStatus);
		});
	});

	// '상담직원 변경 요청' 실행 (관리자에게 요청 / 직접 배정) - 매니저 상담 요청 포함 (flagTag값)
	$('.popup_change_request').on('click', '.btn_save', function(e) {

		console.debug('EVENT: ', '.popup_change_request .btn_save');
		e.preventDefault();
		e.stopPropagation();

		var memo = $('.popup_change_request .form_text').val().trim();
		//상담직원 변경 요청인지 매니저 상담 요청인지 구분
		var flagTag = $('.popup_change_request .form_text').attr('flagTag');
		if (memo === '') {
			alert('요청 사유를 입력해주세요.');
			return;
		}

		var $staff = $('.popup_change_request input[name=staff_select]:checked');
		var $parent = $staff.closest('tr');
		var changeMemberUid = $staff.val();
		var changeMemberName = $('.memberName', $parent).text();
		// 부서 이관 여부
		var isDepartChange = $staff.attr('id') === 'depart_select';
		var data = {
			command: flagTag,
			managerUid: htUser.getManagerId(),
			memberUid: htUser.getId(),
			memo: memo,
			departCd: null,
			departNm: null,
			changeMemberUid: null,
			changeMemberName: null
		};

		if (isDepartChange) {
			data.departCd = changeMemberUid;
			data.departNm = changeMemberName;
		} else {
			data.changeMemberUid = changeMemberUid;
			data.changeMemberName = changeMemberName;
		}
		if (flagTag === 'MANAGER_COUNSEL') {
			data.changeMemberUid = htUser.getManagerId();
		}

		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
			method: 'put',
			data: data,
		}).done(function(data) {
			htUtils.alert(data.returnMessage);
			// 상담직원 변경 메모 메세지 전달
			if (flagTag === 'CHANGE_COUNSELOR') {
				// '자동 변경 승인'상태가 아니면 요청
				if ('N' === htUser.getSetting().auto_chng_appr_yn) {
					memo = '상담직원 변경 요청: ' + memo;
				} else {
					memo = '상담직원 변경: ' + memo;
				}
				if (isDepartChange) {
					memo += ' (' + changeMemberName + '(으)로 부서 이관 요청)';
				} else {
					if (changeMemberUid) {
						memo += ' (' + htUser.getNickName() + ' > ' + changeMemberName + ')';
					}
				}

				// 메모 보냄
				if ('N' === htUser.getSetting().auto_chng_appr_yn) { // 매니저에게 변경 요청
					htClient.sendMemo(memo, htUser.getManagerId());
				} else { // 자동 변경 승인
					if (isDepartChange) {
						htClient.sendMemo(memo, htUser.getManagerId()); // 부서 변경
					} else {
						htClient.sendMemo(memo, changeMemberUid); // 상담직원 변경
					}
				}
//				htClient.getChatRoomList().removeChatRoom(htClient.getChatRoom().getId());

			} else if (flagTag === 'MANAGER_COUNSEL') {
				memo = '매니저 상담 요청: ' + memo;
				htClient.sendMemo(memo, htUser.getManagerId());
			}

			// '자동 변경 승인'상태일때 요청 후 채팅방 닫기
			if (flagTag === 'CHANGE_COUNSELOR' && 'Y' === htUser.getSetting().auto_chng_appr_yn) {
				htClient.getChatRoom().closeChatRoom(htClient.getChatRoom().getId());
				/*$('.dev_mid_manual').show();*/
				$('.dev_mid_chat').hide();
				location.reload();
				
			}

			// 상태 메뉴 클릭
			$('.counseling_left .tabs_icon > li > a.active').trigger('click', 'from REQUEST MANAGER');
			$('.popup_change_request .form_text').val('');
			$('.popup_change_request').hide();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: CHANGE COUNSELOR: ', textStatus);
			if (jqXHR.responseJSON && jqXHR.responseJSON.returnMessage) {
				htUtils.alert(jqXHR.responseJSON.returnMessage);
			}
		}).always(function() {
			//$('.popup_change_request .form_text').val('');
			//$('.popup_change_request').hide();	//2020-05-28 오류생겨도 팝업 유지
		});
	});

	// '상담내용 검토 요청' 버튼
	$('.popup_etc').on('click', '.btn_check_request', function(e) {

		console.info('EVENT: ', '.btn_check_request');
		e.preventDefault();
		e.stopPropagation();

		$('.popup').hide();
		$('.popup_check_request').show();
	});

	// 관리자에게 상담내용 검토 요청
	$('.popup_check_request').on('click', '.btn_save', function(e) {

		console.info('EVENT: ', '.btn_save');
		e.preventDefault();
		e.stopPropagation();

		var memo = $('.popup_check_request .form_text').val().trim();

		// 검토 요청
		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
			method: 'put',
			data: {
				command: CHAT_COMMAND.REQUEST_REVIEW,
				managerUid: htUser.getManagerId(),
				memberUid: htUser.getId(),
				memo: memo,
				departCd: null,
				departNm: null,
				changeMemberUid: null,
				changeMemberName: null
			},
		}).done(function(data) {
			console.info(data);
			htUtils.alert(data.returnMessage);
			// 검토 요청 메모 메세지 전달
			htClient.sendMemo('검토 요청: ' + memo, htUser.getManagerId());
		}).fail(function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.status === 406) {
				var responseText = JSON.parse(jqXHR.responseText);
				if (responseText) {
					fn_layerMessage(responseText.returnMessage);
				}
			} else {
				console.error('FAIL REQUEST: REQUEST REVIEW: ', textStatus);
			}
		}).always(function() {
			$('.popup_check_request .form_text').val('');
			$('.popup_check_request').hide();
		});
	});

	// '코끼리 등록' 버튼
	$('.btn_grade_request').on('click', function(e) {

		console.info('EVENT: ', '.btn_grade_request');
		e.preventDefault();
		e.stopPropagation();
		if (htClient.getChatRoom().getCustomerGradeName() !== null) {
			return;
		}

		$('.popup').hide();
		$('.popup_grade_request').show();
	});

	// 코끼리 등록 요청
	$('.popup_grade_request').on('click', '.btn_save', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var memo = $('.popup_grade_request .form_text').val().trim();
		var $parent = $('.popup_change_request .staff_select_area input[name=staff_select]:checked').closest('tr');
		var flag = $('.popup_grade_request .staff_radio_area input[name=flagType]:checked').val();
		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
			method: 'put',
			data: {
				command: CHAT_COMMAND.ASSIGN_CUSTOMER_GRADE,
				managerUid: htUser.getManagerId(),
				memberUid: htUser.getId(),
				flagName: flag, // 20.05.29 yak
				memo: memo,
			},
		}).done(function(data) {
			console.info(data);
			// '코끼리 등록 요청' 메모 메세지로 발송
			htClient.sendMemo('코끼리 등록 요청: ' + memo, htUser.getManagerId());
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: REQUEST ASSIGN CUSTOMER GRADE: ', textStatus);
		}).always(function() {
			$('.popup_grade_request .form_text').val('');
			$('.popup_grade_request').hide();
		});
	});

	// Flag 달기
	$('.popup_etc').on('click', '.flag_select i', function(e) {

		console.debug('EVENT: ', '.flag_select i');
		e.preventDefault();
		e.stopPropagation();

		var flagName = htUtils.getWordStartWith($(this).attr('class').trim(), 'icon_flag_');
		console.debug('Flag 달기:', flagName)

		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
			method: 'put',
			data: {
				command: CHAT_COMMAND.MARK_CHATROOM_FLAG,
				userId: htUser.getId(),
				flagName: flagName,
			},
		}).done(function(data) {
			console.info(data);
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: REQUEST MARK CHATROOM FLAG: ', textStatus);
		}).always(function() {
			$('.popup').hide();
		});
	});

	// ////////////////////////////////////////////////////////////////////
	// 상담내역 후처리
	// '상담내역 후처리' 버튼 클릭시 팝업
	
	$('.btn_end_modify').on('click', function(e, val) {

		var width = 600;
		var height = 550;
		var top = screen.width / 2 - width / 2;
		top = 200;
		var left = screen.height / 2 - height / 2;
		left = 200;
		var popup = window.open(HT_APP_PATH + '/chatManage/endReviewPopup?chatRoomUid='
				+ htClient.getChatRoom().getId() + '&cstmUid=' + htClient.getChatRoom().getCustomerId()
				, 'endInfo', 'width=' + width + ', height=' + height + ', top=' + top + ', left=' + left);
				//, 'endInfo', 'width=' + width + ', height=' + height + ', top=' + top);
		popup.focus();


		if(!val) val='B';
/*
		$.ajax({
			url: HT_APP_PATH + '/chatManage/endReviewPopup',
			data: {
				chatRoomUid: htClient.getChatRoom().getId(),
				cstmUid: htClient.getChatRoom().getCustomerId(),
				modifyChk: val, // 후처리 닫기 버튼 관련
			},
			dataType: 'html',
		}).done(function(data) {			
			$('.popup_end_modify').html(data).show();

		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: END REVIEW: ', textStatus);
		});
	*/
		
	});

	// '상담내역 후처리' 분류 변경시

	$('.popup_end_modify').on('change', '.popup_body select', function(e) {

		var maxDepth = $('.popup_end_modify .popup_body select').length + 1;
		console.assert(maxDepth > 0);
		var currentDepth = $(this).data('depth');
		console.log("currentDepth : " + currentDepth);
		console.assert(currentDepth > 0);

		if (currentDepth < maxDepth && $('option:checked', $(this)).data('ctg-num') !=null) {
			var currentCtgNum = $('option:checked', $(this)).data('ctg-num');
			$.ajax({
				url: HT_APP_PATH + '/api/category/end/' + currentCtgNum,
				data: {
					depth: currentDepth + 1,
				},
				dataType: 'html',
			}).done(function(data) {
				$('.popup_end_modify .popup_body select[data-depth=' + (currentDepth + 1) + ']').html(data);
				// 3차분류 갯수 저장
				if(currentDepth + 1 == 3 && data=='<option>3차분류</option>') {
					$("#modify_close").html('<button type="button" class="btn_popup_close">창닫기</button>');
					alert('3차분류가 없습니다.');
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: LOAD END CATEGORY: ', textStatus);
			});
		}

	});
	
	// '상담내역 후처리' 분류 검색
	$('.popup_end_modify').on('keyup', '#srcEndCtg', function(e) {

		var maxDepth = 4;
		var srcKeyword = $('#srcEndCtg').val();
		if (srcKeyword.length > 1){
			$.ajax({
				url: HT_APP_PATH + '/api/category/end/search' ,
				data: {
					"srcKeyword" : srcKeyword,
					"depth": maxDepth,
				},
				dataType: 'html',
			}).done(function(data) {
				$('#srcDiv').show();
				$('#srcEndCtgResult').html(data);

			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: LOAD END CATEGORY: ', textStatus);
			});
		}
	});

	// '상담내역 후처리' 분류 검색결과 선택 및 전달
	$('.popup_end_modify').on('change', '#srcEndCtgResult', function(e) {
		$('#selEndCtg').text($('#srcEndCtgResult option:selected').text());
		$('.popup_end_modify .popup_body input[name=dep1CtgCd]').val($('#srcEndCtgResult option:selected').attr("data-ctg1-cd"));
		$('.popup_end_modify .popup_body input[name=dep1CtgNm]').val($('#srcEndCtgResult option:selected').attr("data-ctg1-nm"));
		$('.popup_end_modify .popup_body input[name=dep2CtgCd]').val($('#srcEndCtgResult option:selected').attr("data-ctg2-cd"));
		$('.popup_end_modify .popup_body input[name=dep2CtgNm]').val($('#srcEndCtgResult option:selected').attr("data-ctg2-nm"));
		$('.popup_end_modify .popup_body input[name=dep3CtgCd]').val($('#srcEndCtgResult option:selected').attr("data-ctg3-cd"));
		$('.popup_end_modify .popup_body input[name=dep3CtgNm]').val($('#srcEndCtgResult option:selected').attr("data-ctg3-nm"));
		$('.popup_end_modify .popup_body input[name=dep4CtgCd]').val($('#srcEndCtgResult option:selected').attr("data-ctg4-cd"));
		$('.popup_end_modify .popup_body input[name=dep4CtgNm]').val($('#srcEndCtgResult option:selected').attr("data-ctg4-nm"));
		$('#srcDiv').hide();
	});

	// '상담내역 후처리' 저장
	$('.popup_end_modify').on('click', '.btn_end_save', function(e) {

		var dep1CtgCd = $('#endCtg1 option:selected').attr("data-ctg-num");
		var dep1CtgNm = $('#endCtg1 option:selected').attr("data-ctg-nm");
		var dep2CtgCd = $('#endCtg2 option:selected').attr("data-ctg-num");
		var dep2CtgNm = $('#endCtg2 option:selected').attr("data-ctg-nm");
		var dep3CtgCd = $('#endCtg3 option:selected').attr("data-ctg-num");
		var dep3CtgNm = $('#endCtg3 option:selected').attr("data-ctg-nm");

		//분류구분 반드시 필수 선택
		if (dep1CtgCd == null || dep1CtgCd == '' || dep1CtgCd == '대분류') {
			$('.popup_end_modify .popup_body select[id=endCtg1]').focus();
			alert ("대분류를 선택해 주세요");
			return false;
		}
		if (dep2CtgCd == null || dep2CtgCd == '' || dep2CtgCd == '중분류') {
			$('.popup_end_modify .popup_body input[id=endCtg2]').focus();
			alert ("중분류를 선택해 주세요");
			return false;
		}
		if (dep3CtgCd == null || dep3CtgCd == '' || dep3CtgCd == '소분류') {
			$('.popup_end_modify .popup_body input[id=endCtg3]').focus();
			alert ("소분류를 선택해 주세요");
			return false;
		}

		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/endReview',
			method: 'put',
			data: {
				chatRoomUid: htClient.getChatRoom().getId(),
				creater: htUser.getId(),
				updater: htUser.getId(),
				cstmUid: htClient.getChatRoom().getCustomerId(),
				cstmDivCd: $('.popup_end_modify .popup_body input[name=cstmDivCd]').val(),
				cocId: $('.popup_end_modify .popup_body input[name=cocId]').val(),
				name: $('.popup_end_modify .popup_body input[name=name]').val(),
				dep1CtgCd:dep1CtgCd,
				dep1CtgNm:dep1CtgNm,
				dep2CtgCd:dep2CtgCd,
				dep2CtgNm:dep2CtgNm,
				dep3CtgCd:dep3CtgCd,
				dep3CtgNm:dep3CtgNm,
				memo: $('.popup_end_modify .popup_body textarea[name=memo]').val(),
			},
			//dataType: 'html',
		}).done(function(result) {
			if(result != null && result.rtnCd != null && result.rtnCd == "SUCCESS"){
				// 성공
				$('.popup_end_modify').hide();
				$('.chatting_list a[data-room-id=' + htClient.getChatRoom().getId() + ']').parent().css('background', '#ddd');
				fn_layerMessage(result.rtnMsg);

			}else{
				fn_layerMessage(result.rtnMsg);
			}			

		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: UPDATE END REVIEW: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////
	// '엔터로 전송' 설정 변경시 저장
	$('#check_enter_key').on('change', function(e) {
		var enterUseYn = $('#check_enter_key').prop('checked') ? 'Y' : 'N';

		$.ajax({
			url: HT_APP_PATH + '/api/setting/' + htUser.getId(),
			method: 'post',
			data: {
				enterUseYn: enterUseYn,
			}
		}).done(function(data) {
			console.info(data);
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: SAVE SETTING: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////
	// '상담가능/불가' 설정 변경시 저장
	$(document).on('change', '.break_time .switch-input', function(e) {
		var workStatusCd = $(this).prop('checked') ? 'W' : 'R';

		$.ajax({
			url: HT_APP_PATH + '/api/setting/' + htUser.getId(),
			method: 'post',
			data: {
				workStatusCd: workStatusCd,
			}
		}).done(function(data) {
			console.info(data);
			//설정 변경시 상담원status 재설정
			htClient.getChatRoomList().getStatusCounter();
			$(".need_answer .badge").trigger("click");
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: SAVE SETTING: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 우측, 정보 관리 영역
	// 우측 탭 클릭시
	$('.dev_tabMenu').on('click', function(e, trigger) {

		$('#tplForm .dev_tabCtgEdit').hide();

		if (trigger) {
			console.info('TRIGGER: ', trigger);
		}

		var clickTabMenu = $(this).attr('id');
		var url;
		var data = {};

		// 채팅방 목록 클릭 시 $("#dev_tabMenu1").trigger("click");

		$(this).siblings('li').removeClass('active');
		$(this).addClass('active');

		if (clickTabMenu === 'dev_tabMenu1') { // 상담 이력
			$.ajax({
				url: HT_APP_PATH + '/chatCsnr/selectCnsInfoAjax',
				data: {
					chatRoomUid: htClient.getChatRoom() ? htClient.getChatRoom().getId() : null
				},
				type: 'post'
			}).done(function(result) {
				$('.dev_tab_contents').html(result);
			}).always(function() {
				$('.template_list li.text').hide();
			});
		} else if (clickTabMenu === 'dev_tabMenu2') { // 템플릿 관리
			$.ajax({
				url: HT_APP_PATH + '/chatCsnr/selectCnsrTplPageAjax',
				type: 'post'
			}).done(function(result) {
				$('.dev_tab_contents').html(result);
				// 이미지 에러시, 노이미지
				$('.bubble-templet img').on('error', function(e) {
					console.info($(this).attr('src'));
					$(this).attr('src', HT_APP_PATH + '/images/chat/no_image.png').attr('alt', 'NO IMAGE').addClass('chatImageCs');
				});

			}).always(function() {
				$('.template_list li.text').hide();
				$('#tplForm .dev_tabCtgEdit').hide();
			});
		} else if (clickTabMenu === 'dev_tabMenu3') { // 지식화 관리
			$.ajax({
				url: HT_APP_PATH + '/chatCsnr/selectKnowAjax',
				type: 'post'
			}).done(function(result) {
				$('.dev_tab_contents').html(result);
			}).always(function() {
				$('.template_list li.text').hide();
				// 지식화 목록 스크롤 이벤트 적용
				$('#dev_knowledgeList').scroll(scrollHandlerKnow);
			});
		} else if (clickTabMenu === 'dev_tabMenu4') { // 공지사항
			$.ajax({
				url: HT_APP_PATH + '/chatCsnr/selectNoticeListAjax',
				type: 'post'
			}).done(function(result) {
				$('.dev_tab_contents').html(result);
			});
		} else if (clickTabMenu === 'dev_tabMenu5') { // 상담정보
			console.debug(htClient.getChatRoom().getId());
			if (htClient.getChatRoom() && htClient.getChatRoom().getId()) {
				data.chatRoomUid = htClient.getChatRoom().getId();
				$.ajax({
					url: HT_APP_PATH + '/chatCsnr/selectCustomerInfoAjax',
					data: data,
					type: 'post'
				}).done(function (result) {
					$('.dev_tab_contents').html(result);
					
					// 이미지 에러시, 노이미지
					$('.bubble-templet img').on('error', function(e) {
						console.info($(this).attr('src'));
						$(this).attr('src', HT_APP_PATH + '/images/chat/no_image.png').attr('alt', 'NO IMAGE').addClass('chatImageCs');
					});

					// 계약정보, 입금정보, 콜상담내역 숨김
					$('#customerInfo1').hide();
					$('#customerInfo2').hide();
					$('#customerInfo3').hide();
				});
			}
		}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 기타
	// 복사 - ID
	if ($('.btn_copy').length > 0) {
		var clipboard = new ClipboardJS('.btn_copy');
		clipboard.on('success', function(e) {
			e.clearSelection();
			e.trigger.blur();
			htUtils.alert('고객ID를 복사했습니다.');
		});
	}

	// 복사 - 계좌번호
	if ($('.btn_copy_acnt_no').length >= 0) {
		var clipboard = new ClipboardJS('.btn_copy_acnt_no');
		clipboard.on('success', function(e) {
			e.clearSelection();
			e.trigger.blur();
			htUtils.alert('계좌번호를 복사했습니다.');
		});
	}
	

	// 팝업 닫기 버튼
	$('body').on('click', '.btn_popup_close', function(e) {
		$('body').css("overflow", "scroll");
		$('.popup').hide();
	});

	// 과거 상담 이력
	$('.counseling_right .tabs_menu_content').on('click', '.dev_cnsHisTable tbody > tr', function() {
		var chatRoomUid = $(this).data('chatroomuid');
		console.info('GET CHAT LIST: ', chatRoomUid);
		$.ajax({
			url: HT_APP_PATH + '/api/html/chat/room/' + chatRoomUid,
			method: 'get',
			data: {
				rollType: htUser.getRollType(),
				withChatMessageList: 'true',
				withMetaInfo: 'true',
			},
			dataType: 'html',
		}).done(function(data) {
			$('.popup').hide();
			$('.popup_counseling').html(data).show();

			// 멀티형 메세지 슬라이더
			new Swiper($('.popup_counseling .chat_text_slide'), {
				slidesPerView: 'auto',
				centeredSlides: false,
				spaceBetween: 1,
			});

		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: REVIEW: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 템플릿
	// 템플릿 채우기
//	if (htUser.getRollType() === CHAT_ROLL_TYPE.COUNSELOR) {

	// 메세지 선택 가능
	$(document).on('mouseover', '#chat_body .text_box', function(e) {
		if ($('#tplForm .dev_tabCtgEdit:visible').length === 1 // 템플릿 화면
			|| $('#knowForm').length === 1) { // 지식화 화면
			if ($('.type_text', $(this)).length > 0) {
				$(this).addClass('copy');
			}
		}
	}).on('mouseleave', '.text_box', function(e) {
		$(this).removeClass('copy');
		
	});

	// 메세지 선택
	$(document).on('click', '#chat_body .copy', function(e) {
		var contents = $('.type_text', $(this)).map(function() {
			return $(this).text();
		}).get().join(' ');

		console.debug($('#tplForm .dev_tabCtgEdit').css('display'),
				$('#tplForm .dev_tabCtgEdit').css('display') === 'block');

		// 고객 메세지
		if ($(this).closest('div.left-chat-wrap').length === 1) {
			if ($('#tplForm .dev_tabCtgEdit').css('display') === 'block') { // 템플릿 화면
				$('textarea[name=cstmQue]').val(contents);
			} else { // 지식화 화면
				if ($('textarea[name=cstmQue]').val().length === 0) {
					$('textarea[name=cstmQue]').val(contents);
				} else {
					$('textarea[name=cstmQue]').val($('textarea[name=cstmQue]').val() + ' ' + contents);
				}
			}
		}
		// 상담직원 메세지
		if ($(this).closest('div.right-chat-wrap').length === 1) {
			if ($('#tplForm .dev_tabCtgEdit').css('display') === 'block') { // 템플릿 화면
				$('textarea[name=replyContText]').val(contents);
			} else { // 지식화 화면
				if ($('textarea[name=replyContText]').val().length === 0) {
					$('textarea[name=replyContText]').val(contents);
				} else {
					$('textarea[name=replyContText]').val($('textarea[name=replyContText]').val() + ' ' + contents);
				}
			}
		}
	});

	// 템플릿 미리보기 사용  
	// TODO :: 수정 예정
	$('.counseling_right').on('click', '#tmenu011 .template_list > .text', function(e) {
		if (e.clientX === 0) { // 챗리스트 검색인경우
			return;
		}
		e.preventDefault();
		e.stopPropagation();

		var tplNum = $(this).attr('data-tplNum');
		//var $preview = $('.right-chat-wrap', $(this));
		var $preview = $('.right-chat-wrap', $(this));

		$('#chat_bottom .layer_selected_template .right-chat-wrap').remove();
		$('#chat_bottom .layer_selected_template .right-chat-wrap').remove();
		$('#chat_bottom .layer_selected_template .left-chat-wrap').remove();
		$('#chat_bottom .layer_selected_template').append($preview.clone()).attr('data-tpl-num', tplNum);
		$('#chat_bottom .layer_selected_template .right-chat-wrap .tag_area').remove();
		$('#chat_bottom .layer_selected_template').show();

		var $img_area= $('#chat_bottom .layer_selected_template .right-chat-wrap img');
		var $link_area= $('#chat_bottom .layer_selected_template .right-chat-wrap .btn-wrap');

		if ($img_area.length === 0 && $link_area.length === 0) {
			var text= $('#chat_bottom .layer_selected_template .right-chat-wrap .type_text').html();
			text = text.replace(/<br\s*[^>]*>/gi, '\n');
			console.debug(text);
			$('#chat_bottom .textarea_area textarea').val(text);
			$('#chat_bottom .layer_selected_template').hide();
		}
	});

	// 템플릿 미리보기 닫기
	$('#chat_bottom').on('click', '.btn_close_temp', function() {
		$('#chat_bottom .layer_selected_template').hide();
	});

	// 매니저 상담 종료
	$('#chat_bottom .btn_end_manager_counsel').on('click', function() {
		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + htClient.getChatRoom().getId(),
			method: 'put',
			data: {
				command: CHAT_COMMAND.LEAVE_MANAGER_COUNSEL,
				managerUid: htUser.getId()
			}
		}).done(function(data) {
			console.info(data);
			window.location.href = HT_APP_PATH + '/counselor';
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: REVIEW: ', textStatus);
		});
	});
	
	//상담직원 상태창 자동업데이트
	if($("#happyTalkHostName").val() == "localhost:8080"){
		return false;
	}
	else {
		setInterval(function(){
			htClient.getChatRoomList().getStatusCounter();
		},2000);
	}
		
//	}
});

$(window).on('load', function() {

	// ////////////////////////////////////////////////////////////////////////
	// 하이라이트 단어 저장
	$.ajax({
		url: HT_APP_PATH + '/api/highlight/' + htUser.getId()
	}).done(function(data) {
		htHighlight = data;
		console.debug('HIGHLIGHT WORDS', htHighlight);
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: LOAD HIGHLIGHT', textStatus);
	});

	// ////////////////////////////////////////////////////////////////////////
	// 우측 초기 탭 클릭
	if ($('#dev_tabMenu4').length === 1) { // 공지사항
		$('#dev_tabMenu4').trigger('click');
	} else if ($('#dev_tabMenu2').length === 1) { // 템플릿 관리
		$('#dev_tabMenu2').trigger('click');
	}

	// ////////////////////////////////////////////////////////////////////////
	// 브라우저 활성화 이벤트
	ifvisible.setIdleDuration(HT_USER_IDLE_TIME_SECONDS);
	ifvisible.blur(function() {
		htBrowserActive = false;
		console.debug('BROWSER IS BLUR');
	}).focus(function() {
		htBrowserActive = true;
		console.debug('BROWSER IS FOCUS');
	});

	// ////////////////////////////////////////////////////////////////////////
	// 매니저 상담
	$('#initialActions input').each(function() {
		if ('openChatRoom' === $(this).attr('name') && $(this).val()) {
			// 탭 변경 (전체 목록)
			$('.counseling_left .tabs_icon li.manager_counsel a').trigger('click', 'openChatRoom');

			// 메뉴얼 숨기기
			$('.dev_mid_manual').hide();
			$('.dev_mid_chat').show();

			// 채팅방 열기
			console.debug('START CHAT MANAGER', $(this).val());
			var chatRoomUid = $(this).val();
			var chatRoom = htClient.getChatRoomList().getChatRoom(chatRoomUid);
			if (chatRoom === null) {
				$.ajax({
					url: HT_APP_PATH + '/api/chat/room/' + chatRoomUid,
					data: {
						withChatMessageList: 'true',
					},
				}).fail(function(jqXHR, textStatus, errorThrown) {
					console.error('FAIL REQUEST: GET CHAT ROOM: ', textStatus);
				}).done(function(data) {
					console.info('ChatRoom', new ChatRoom(data));
					if (data) {
						chatRoom = new ChatRoom(data);
						var chatRoomViewer = new ChatRoomViewer({
							chatMessageViewerContainer: $('#chat_body'),
							chatControlContainer: $('#chat_bottom'),
							messageArea: $('#chat_bottom textarea'),
							messageSendButton: $('#chat_bottom .btn_area button'),
							chatRoomTitleContainer: $('.counseling_mid .counsel_title_area'),
							chatRoomMetaInfoContainer: $('.counseling_mid .counsel_chat_info'),
						});
						chatRoom.setViewer(chatRoomViewer);
						chatRoom.getViewer().openChatRoom();

						// 현재 진행중인 채팅창 변경
						htClient.setChatRoom(chatRoom);
						// 새 채팅방에 가입
						htClient.subscribeChatRoom();
					}
				});
			} else {
				var chatRoomViewer = new ChatRoomViewer({
					chatMessageViewerContainer: $('#chat_body'),
					chatControlContainer: $('#chat_bottom'),
					messageArea: $('#chat_bottom textarea'),
					messageSendButton: $('#chat_bottom .btn_area button'),
					chatRoomTitleContainer: $('.counseling_mid .counsel_title_area'),
					chatRoomMetaInfoContainer: $('.counseling_mid .counsel_chat_info'),
				});
				chatRoom.setViewer(chatRoomViewer);
				chatRoom.getViewer().openChatRoom();

				// 현재 진행중인 채팅창 변경
				htClient.setChatRoom(chatRoom);
				// 새 채팅방에 가입
				htClient.subscribeChatRoom();
			}
		}
	});
});
