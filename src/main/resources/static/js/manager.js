'use strict';

/**
 * 상담관리 페이지
 */
$(document).ready(function() {

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
			statusCounterContainer: $('#snb'),
		}));
		if (typeof chatRoomList != 'undefined') {
			htClient.setChatRoomList(chatRoomList);
		}
		
	})();

	// ////////////////////////////////////////////////////////////////////////
	// 채팅방 목록

	// 채팅 내역 요청 (jQuery.ajax Object)
	var requestChatList = null;

	// 방 목록 클릭
	$(document).on("click", "#roomListForm .dev_roomListLi", function(e) {

		e.preventDefault();
		e.stopPropagation();

		var liObj = $(this).closest("li");
		liObj.siblings("li").removeClass("active");
		liObj.addClass("active");

		// 채팅 내역 요청시 이전 요청있으면 무시
		if (requestChatList != null) {
			requestChatList.abort();
			requestChatList = null;
		}

		// 검토 요청 시퀀스
		var reviewReqNum = liObj.data('review-req-num');
		// 매니저 요청 시퀀스
		var chngReqNum = liObj.data('chng-req-num');

		// 채팅 내역 요청
		requestChatList = $.ajax({
			url: HT_APP_PATH + '/api/html/chat/room/' + liObj.data('chat-room-uid'),
			data: {
				rollType: htUser.getRollType(),
				withChatMessageList: 'true',
				withMetaInfo: 'true',
				statusMenu: $('.admin_counseling .sub_stit').data('select-status'),
			},
			dataType: 'html',
		}).done(function(data) {

			$('.counseling_mid').html(data).show();

			// 멀티형 메세지 슬라이더
			new Swiper($('.counseling_mid .chat_text_slide'), {
				slidesPerView: 'auto',
				centeredSlides: false,
				spaceBetween: 1,
			});

			// 복사
			if ($('.counseling_mid .btn_copy').length > 0) {
				var clipboard = new ClipboardJS('.counseling_mid .btn_copy');
				clipboard.on('success', function(e) {
					e.clearSelection();
					e.trigger.blur();
					htUtils.alert('고객ID를 복사했습니다.');
				});
			}

			if (reviewReqNum && $('.counseling_mid .btn_check_out').length === 1) {
				$('.counseling_mid .btn_check_out').attr('data-review-req-num', reviewReqNum);
			}
			if (chngReqNum && $('.counseling_mid .btn_check_out').length === 1) {
				$('.counseling_mid .btn_check_out').attr('data-chng-req-num', chngReqNum);
			}

			// 이미지 에러시, 노이미지
			$('.counseling_mid img').on('error', function(e) {
				console.info($(this).attr('src'));
				$(this).attr('src', HT_APP_PATH + '/images/chat/no_image.png').attr('alt', 'NO IMAGE').addClass('chatImageCs');
			});
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: REVIEW: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 버튼: 링크 (고객이 아니면 무효화)
	$('.counseling_mid').on('click', '#chat_body .btn_link', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if (htUser.getRollType() !== CHAT_ROLL_TYPE.CUSTOMER) {
			return false;
		}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 상담직원 변경

	// '변경 버튼' 클릭
	$(document).on('click', '.dev_change_cnsr_btn', function(e) {

		console.info('EVENT: ', '.dev_change_cnsr_btn');
		e.preventDefault();
		e.stopPropagation();

		if ($('.chatting_list li input[name=chatRoomUid]:checked').length === 0) {
			alert('접속할 채팅방을 선택하세요.');
			return;
		} 

		$('.popup').hide();
		$.ajax({
			url: HT_APP_PATH + '/api/member/available',
			data: {
				memberUid: htUser.getId(),
				rollType: htUser.getRollType(),
			},
			dataType: 'html',
		}).done(function(data) {
			$('.popup_change_staff .popup_body').html(data);
			$('.popup_change_staff').show();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: LOAD AVAILABLE MEMBER LIST: ', textStatus);
		});
	});

	// 상담직원 변경 적용
	$('.popup_change_staff').on('click', '.btn_save', function(e) {

		var chatRoomUid = [];
		$('.chatting_list li input[name=chatRoomUid]:checked').each(function() {
			chatRoomUid.push($(this).closest('li').data('chat-room-uid'));
		});

		var $staff = $('.popup_change_staff input[name=staff_select]:checked');
		var $parent = $staff.closest('tr');
		var changeMemberUid = $staff.val();
		var changeMemberName = $('.memberName', $parent).text();
		// 부서 이관 여부
		var isDepartChange = $staff.attr('id') === 'depart_select';
		var data = {
			command: CHAT_COMMAND.ASSIGN_COUNSELOR,
			userId: htUser.getId(),
			chatRoomUid: chatRoomUid
		};

		console.debug('isDepartChange', isDepartChange);

		if (isDepartChange) {
			data.departCd = changeMemberUid;
			data.departNm = changeMemberName;
		} else {
			data.changeMemberUid = changeMemberUid;
			data.changeMemberName = changeMemberName;
		}

		if (changeMemberUid) {
			$.ajax({
				url: HT_APP_PATH + '/api/chat/room',
				method: 'put',
				data: data,
			}).done(function(data, textStatus, jqXHR) {
				if (isDepartChange) {
					htUtils.alert('부서 변경이 완료되었습니다.');
				} else {
					htUtils.alert('상담직원 변경이 완료되었습니다.');
				}
				$('.chatting_list li input[name=chatRoomUid]').prop('checked', false);
				$('#checkAll').prop('checked', false);
				$('.popup_change_staff').hide();
				goSearch();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: ASSIGN COUNSELOR: ', textStatus);
			});
		} else {
			alert('변경할 상담직원을 선택하세요.');
		}
	});

	// 코끼리 등록
	$(document).on('click', '.dev_cstm_grade_btn', function(e) {

		var chatRoomUid = [];
		$('.chatting_list li input[name=chatRoomUid]:checked').each(function(idx, item) {
			chatRoomUid.push($('#dev_chatRoomListDiv li').eq(idx).attr("data-chat-room-uid"));
		});
		var chatRoomUidCheck = $('.chatting_list li input[name=chatRoomUid]:checked').val();

		if (chatRoomUidCheck) {
			$.ajax({
				url: HT_APP_PATH + '/api/chat/room',
				method: 'put',
				data: {
					command: CHAT_COMMAND.ASSIGN_CUSTOMER_GRADE,
					userId: htUser.getId(),
					chatRoomUid: chatRoomUid
				},
			}).done(function(data, textStatus, jqXHR) {
				htUtils.alert('코끼리 등록이 완료되었습니다.');
				$('.chatting_list li input[name=chatRoomUid]').prop('checked', false);
				$('#checkAll').prop('checked', false);
				goSearch();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: ASSIGN COUNSELOR: ', textStatus);
			});
		} else {
			alert('변경할 코끼리 등록건을 선택하세요.');
		}
	});

	// 상담직원 변경 취소
	$('.popup_change_staff').on('click', '.btn_cancel', function(e) {
		$('.popup_change_staff').hide();
	});
	
	// '대화방 접속' 클릭
	$(document).on('click', '.dev_change_manager_btn', function(e) {

		console.info('EVENT: ', '.dev_change_manager_btn');
		e.preventDefault();
		e.stopPropagation();

		if ($('.chatting_list li input[name=chatRoomUid]:checked').length === 0) {
			alert('접속할 채팅방을 선택하세요.');
			return;
		} 

		$('.popup').hide();
		$('.popup_manager_staff').show();
	});	
	
	// 대화방 접속 취소
	$('.popup_manager_staff').on('click', '.btn_cancel', function(e) {
		$('.popup_manager_staff').hide();
	});	
	// 대화방 접속
	$('.popup_manager_staff').on('click', '.btn_save', function(e) {

		var gradeMemoArr = [];
		var chatRoomUid = [];
		$('.chatting_list li input[name=chatRoomUid]:checked').each(function() {
			chatRoomUid.push($('#dev_chatRoomListDiv li').attr("data-chat-room-uid"));
			gradeMemoArr.push($('#dev_chatRoomListDiv li').attr("data-grade-memo"));
		});

		var chatRoomUidCheck = $('.chatting_list li input[name=chatRoomUid]:checked').val();
		var openChatRoomWithSystemMsg = $('#managerDisp').val()=='Y' ? true : false;
		
		var url = HT_APP_PATH + '/counselor?openChatRoom=' + chatRoomUid[0] + '&openChatRoomWithSystemMsg=' + openChatRoomWithSystemMsg;
		
		location.href=url;
		
	});		

	// ////////////////////////////////////////////////////////////////////////
	// 메세지 검토

	// 메모/메세지 편집
	$('.counseling_mid').on('click', '#chat_body .btn_chat_modify', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var $messageElement = $(this).closest('.left_area');
		var chatMessageId = $messageElement.data('message-id');
		var contents = $('.type_text', $messageElement).text();

		$('.popup').hide();
		$('.popup_memo').attr('data-message-id', chatMessageId);
		$('.popup_memo .form_text').val(contents);
		$('.popup_memo').show();
	});

	// 메모 편집 실행
	$('.popup_memo').on('click', '.btn_save', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var chatMessageId = $('.popup_memo').data('message-id');
		var contents = $('.popup_memo .form_text').val().trim();

		if (chatMessageId && contents) {
			$.ajax({
				url: HT_APP_PATH + '/api/chat/' + chatMessageId,
				method: 'put',
				data: {
					type: CHAT_MESSAGE_TYPE.EDIT,
					userId: htUser.getId(),
					cont: contents,
				},
			}).done(function(data) {
				$('#chat_body > div[data-message-id="' + chatMessageId + '"] .type_text').text(contents.trim());

				$('.popup_memo .form_text').val('');
				$('.popup_memo').hide();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: EDIT CHAT MESSAGE: ', textStatus);
			});
		} else {
			console.error('chatMessageId: ', chatMessageId, 'contents: ', contents);
		}
	});

	// 메모/메세지 삭제
	$('.counseling_mid').on('click', '#chat_body .btn_chat_del', function(e) {

		var $messageElement = $(this).closest('.left_area');
		var chatMessageId = $messageElement.data('message-id');

		if (confirm('메세지를 삭제하시겠습니까?')) {
			$.ajax({
				url: HT_APP_PATH + '/api/chat/' + chatMessageId,
				method: 'put',
				data: {
					type: CHAT_MESSAGE_TYPE.REMOVE,
					userId: htUser.getId(),
				},
			}).done(function(data) {
				$messageElement.remove();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: DELETE CHAT MESSAGE: ', textStatus);
			});
		}
	});

	// '검토완료', '반려' 버튼
	$('.counseling_mid').on('click', '.btn_check_out', function(e) {

		var chatRoomUid = $(this).closest('.counsel_title_area').data('chat-room-uid');
		var reviewReqNum = $(this).data('review-req-num');
		var chngReqNum = $(this).data('chng-req-num');
		var $self = $(this);

		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/' + chatRoomUid,
			method: 'put',
			data: {
				command: CHAT_COMMAND.COMPLETE_REVIEW,
				managerUid: htUser.getId(),
				memberUid: htUser.getId(),
				reviewReqNum: reviewReqNum,
				chngReqNum: chngReqNum,
			},
		}).done(function(data) {
			$('.btn_check_out').hide();
			// 메모
			if (chngReqNum) {
				if (data.chatRoom && data.chatRoom.memberUid) {
					console.warn(data);
					htClient.setChatRoom(new ChatRoom(data.chatRoom));
					console.warn($self.parent().data('member-uid'));
					htClient.sendMemo('매니저가 요청을 반려했습니다. ' + $self.text(), $self.parent().data('member-uid'));
					htClient.setChatRoom(null);
				}
			}
			htUtils.alert(data.returnMessage);
			setTimeout(function() {
				$(".counseling_mid").hide();
				goSearch();
//				location.reload();
			}, 500);
			
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: COMPLETE REVIEW: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 사용자 정보
	// '사용자 정보 상세 보기' 버튼
	$('.counseling_mid').on('click', '.btn_info_more', function(e) {
		$('.counsel_chat_info').css('height', '250px').addClass('active');
		$('.request_info').css("top", "298px");
		$(this).hide();
		$('.btn_info_close').show();
	});

	// '사용자 정보 상세 보기 닫기' 버튼
	$('.counseling_mid').on('click', '.btn_info_close', function(e) {
		$('.counsel_chat_info').css('height', '100px').removeClass('active');
		$('.request_info').css("top", "148px");
		$(this).hide();
		$('.btn_info_more').show();
	});

	// '메모 상세 보기' 버튼
	$('.counseling_mid').on('click', '.btn_request_more', function(e) {
		$('.request_info').css('height', '300px').addClass('active');
		$(this).hide();
		$('.btn_request_close').show();
	});

	// '메모 상세 보기 닫기' 버튼
	$('.counseling_mid').on('click', '.btn_request_close', function(e) {
		$('.request_info').css('height', '40px').removeClass('active');
		$(this).hide();
		$('.btn_request_more').show();
	});

	// 팝업 닫기 버튼
	$(document).on('click', '.btn_popup_close, .btn_close_detail', function(e) {
		$('body').css("overflow", "scroll");
		$('.popup').hide();
	});

	// ////////////////////////////////////////////////////////////////////////
	// 버튼: 자세히 보기
	$(document).on('click', '#chat_body .btn_view_detail', function(e) {

		e.preventDefault();
		e.stopPropagation();

		//if (htUser.getRollType() === CHAT_ROLL_TYPE.CUSTOMER) { // 고객 채팅창인 경우
			var $parent = $(this).parent();
			$('.poopup_chat_detail .popup_body .detail_contents').html($parent.prev().html());
			if ($(this).closest('.text_box').hasClass('slide')) {
				$('.poopup_chat_detail .btn_text_copy').hide();
			} else {
				$('.poopup_chat_detail .btn_text_copy').show();
			}
			$('.poopup_chat_detail').show();
		//}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 회원 이름 클릭시 분류 매칭 관리 팝업창 열기
	$(document).on('click', '.dev_category_table .member_name', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var memberUid = $(this).data('id');

		if (memberUid) {
			console.info('memberUid', memberUid);
			$.ajax({
				url: HT_APP_PATH + '/manage/selectMember',
				data: {
					rollType: htUser.getRollType(),
					memberUid: memberUid
				}
			}).done(function(data) {
				$('#member_popup').html(data);
				$('#member_popup').show();
				$('#member_popup .inner').css('height', '420px');
	
				//fn_changeSelectOption($("#memberPopForm [name='firstYn']:checked").val(), 'Y');
			}).fail(function(jqXHR, textStatus, errorThrown) {
				console.error('FAIL REQUEST: OPEN MEMBER INFO: ', textStatus);
			});
		}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 분류 매칭 수정 이벤트
	$(document).on('click', '#member_popup .dev_update_btn', function(e) {

		// 전체 체크 순회
		if ($('#member_popup input:radio[name=firstYn]:checked').length === 1) {
			var $parent = $('#member_popup input:radio[name=firstYn]:checked').closest('tr');
			if ($('input:checkbox[name=ctgNumArr]:checked', $parent).length !== 1) {
				alert('우선순위분류로 지정하려면 배정을 먼저 해야합니다.');
				return false;
			}
		}

		$.ajax({
			url: HT_APP_PATH + '/manage/matchMemberCategory',
			type: 'post',
			data: $('#memberPopForm').serialize()
		}).done(function(data) {
			fn_layerMessage(data.rtnMsg);
			$('#member_popup').hide();
			htUtils.alert(data.returnMessage);
			setTimeout(function() {
				$(".counseling_mid").hide();
				goSearch();
//				location.reload();
			}, 500);
			
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: OPEN MEMBER INFO: ', textStatus);
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 비밀번호 초기화 
	$(document).on('click', '#memberPopForm .dev_pwd_btn', function(e) {

		if (!confirm('비밀번호를 초기화 하시겠습니까?')) {
			return false;
		}

		var pwd= $('#memberPopForm [name=cocId]').val();
		$('#memberPopForm [name=pwd]').val(pwd);

		$.ajax({
			url : HT_APP_PATH + '/manage/changePasswd',
			data : $('#memberPopForm').serialize() + "&newPassword=" + pwd,
			type : 'post'
		}).done(function(result) {
			if (result != null && result.rtnCd != null && result.rtnCd == 'SUCCESS') {
				// 성공
				fn_layerMessage(result.rtnMsg);
			} else {
				fn_layerMessage(result.rtnMsg);
			}
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 어드민 / 매니저 비번 리셋
	// IPCC_MCH 고객 요청 : 매니저 비번 초기화 추가
	$('#resetAdminPasswd, #resetManagerPasswd').on('click', function(e) {

		var trgt = $(this).attr("id") == "resetAdminPasswd" ? "어드민" : "매니저";
		if (!confirm(trgt + ' 비밀번호를 초기화 하시겠습니까?')) {
			return false;
		}

		var cocId = $(this).data('id');

		$.ajax({
			url : HT_APP_PATH + '/manage/changePasswd',
			data : {
				cocId: cocId,
				pwd: cocId,
				newPassword: cocId,
				withValidate: true
			},
			type : 'post'
		}).done(function(result) {
			if (result != null && result.rtnCd != null && result.rtnCd == 'SUCCESS') {
				// 성공
				fn_layerMessage(result.rtnMsg);
			} else {
				fn_layerMessage(result.rtnMsg);
			}
		});
	});

	// ////////////////////////////////////////////////////////////////////////
	// 상담내역 후처리
	// '상담내역 후처리' 버튼 클릭시 팝업
	$(document).on('click', '#btn_end_modify', function(e) {

		$.ajax({
			url : HT_APP_PATH + '/api/chat/room/endReview',
			data : {
				chatRoomUid: $('#manchatRoomUid').val(),
				cstmUid: $('#mancstmUid').val(),
				modifyChk: 'E',
			},
			dataType : 'html',
		}).done(function(data) {
			$('.popup_end_modify').html(data).show();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: END REVIEW: ', textStatus);
		});
	});

	// 상담 후처리 윈도우 팝업용
	$(document).on("click", "#btn_end_popup", function() {

		var width = 600;
		var height = 550;
		var top = screen.width / 2 - width / 2;
		top = 200;
		var left = screen.height / 2 - height / 2;
		left = 200;
		var popup = window.open(HT_APP_PATH + '/chatManage/endReviewPopup?chatRoomUid='
				+ $('#manchatRoomUid').val() + '&cstmUid=' + $('#mancstmUid').val()
				, 'endInfo', 'width=' + width + ', height=' + height + ', top=' + top + ', left=' + left);
				//, 'endInfo', 'width=' + width + ', height=' + height + ', top=' + top);
		popup.focus();
	});

	// '상담내역 후처리' 분류 변경시
	$('.popup_end_modify').on('change', '.popup_body select', function(e) {

		var maxDepth = $('.popup_end_modify .popup_body select').length + 1;

		console.assert(maxDepth > 0);
		var currentDepth = $(this).data('depth');
		console.assert(currentDepth > 0);

		if (currentDepth < maxDepth
				&& $('option:checked', $(this)).data('ctg-num') != null) {
			var currentCtgNum = $('option:checked', $(this)).data('ctg-num');
			$.ajax({
				url: HT_APP_PATH
						+ '/api/category/end/'
						+ currentCtgNum,
				data: {
					depth : currentDepth + 1,
				},
				dataType : 'html',
			}).done(function(data) {
				$('.popup_end_modify .popup_body select[data-depth=' + (currentDepth + 1) + ']').html(data);
				// // 소분류 갯수 저장
				if (currentDepth + 1 == 3
					&& data == '<option>소분류</option>') {
					$("#modify_close").html('<button type="button" class="btn_popup_close">창닫기</button>');
					alert('분류가 없습니다.');
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

		var dep1CtgCd = $('#srcEndCtgResult option:selected').attr("data-ctg1-cd");
		var dep1CtgNm = $('#srcEndCtgResult option:selected').attr("data-ctg1-nm");
		var dep2CtgCd = $('#srcEndCtgResult option:selected').attr("data-ctg2-cd");
		var dep2CtgNm = $('#srcEndCtgResult option:selected').attr("data-ctg2-nm");
		var dep3CtgCd = $('#srcEndCtgResult option:selected').attr("data-ctg3-cd");
		var dep3CtgNm = $('#srcEndCtgResult option:selected').attr("data-ctg3-nm");
		var dep4CtgCd = $('#srcEndCtgResult option:selected').attr("data-ctg4-cd");
		var dep4CtgNm = $('#srcEndCtgResult option:selected').attr("data-ctg4-nm");
		

		//분류구분 반드시 필수 선택
		if (dep1CtgCd == null || dep1CtgCd == '' || dep1CtgCd == '1차분류') {
			$('.popup_end_modify .popup_body select[name=srcEndCtg]').focus();
			alert ("분류를 선택해 주세요");
			return false;
		}
		if (dep2CtgCd == null || dep2CtgCd == '' || dep2CtgCd == '2차분류') {
			$('.popup_end_modify .popup_body input[name=srcEndCtg]').focus();
			alert ("분류를 선택해 주세요");
			return false;
		}
		if (dep3CtgCd == null || dep3CtgCd == '' || dep3CtgCd == '3차분류') {
			$('.popup_end_modify .popup_body input[name=srcEndCtg]').focus();
			alert ("분류를 선택해 주세요");
			return false;
		}
		if (dep4CtgCd == null || dep4CtgCd == '' || dep4CtgCd == '4차분류') {
			$('.popup_end_modify .popup_body input[name=srcEndCtg]').focus();
			alert ("분류를 선택해 주세요");
			return false;
		}
		
		$.ajax({
			url: HT_APP_PATH + '/api/chat/room/endReview',
			method: 'put',
			data: {
				chatRoomUid: $('#manchatRoomUid').val(),
				cstmUid: $('#mancstmUid').val(),
				dep1CtgCd: $('.popup_end_modify .popup_body input[name=dep1CtgCd]').val(),
				dep1CtgNm: $('.popup_end_modify .popup_body input[name=dep1CtgNm]').val(),
				dep2CtgCd: $('.popup_end_modify .popup_body input[name=dep2CtgCd]').val(),
				dep2CtgNm: $('.popup_end_modify .popup_body input[name=dep2CtgNm]').val(),
				dep3CtgCd: $('.popup_end_modify .popup_body input[name=dep3CtgCd]').val(),
				dep3CtgNm: $('.popup_end_modify .popup_body input[name=dep3CtgNm]').val(),
				dep4CtgCd: $('.popup_end_modify .popup_body input[name=dep4CtgCd]').val(),
				dep4CtgNm: $('.popup_end_modify .popup_body input[name=dep4CtgNm]').val(),
				memo: $('.popup_end_modify .popup_body textarea[name=memo]').val()
			},
			dataType: 'html',
		}).done(function(data) {
			if (parseInt(data) === 1) {
				fn_get_chgchatRoom($('#manchatRoomUid').val());
				$('.popup_end_modify').hide();
				$('.counseling_left li[data-chat-room-uid=' + $('#manchatRoomUid').val() + ']').css('background', '#ddd');
			} else {
				console.error('FAIL REQUEST: UPDATE END REVIEW: ', data);
			}
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.error('FAIL REQUEST: UPDATE END REVIEW: ', textStatus);
		});
	});
});

// 자동 완성 목록 (후처리용)
var autoCmpAfterList;

$(window).on('load', function() {

	// 후처리 자동완성 목록, TODO: 상담종료 페이지에서만
	$.ajax({
		url : HT_APP_PATH + '/autoCmp/selectAutoCmpListAjax',
		data : {
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
 * 키입력시 자동완성 (후처리용)
 * @param String domId ('afterAutoCmp': 후처리용) 
 */
function fn_autoCmp(domId, inputText) {

	var autoCompleteList = autoCmpAfterList;

	$('#' + domId).html('');

	if (inputText.length > 1) {
		var html = '';
		var validCnt = 0;

		if (autoCompleteList.length > 0) {
			for (var i = 0; i < autoCompleteList.length; i++) {
				var content = autoCompleteList[i].content.match(inputText);
				if (content !== null) {
					var thisContent = autoCompleteList[i].content.replace(inputText,
							'<span class="highlight">' + inputText + "</span>");
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

	$('.popup_end_modify .tCont textarea').val(autoCmpAfterList[i].content);
	$('#afterAutoCmp').hide();
}

function fn_get_chgchatRoom(val) {
	$.ajax({
		url : HT_APP_PATH + '/api/html/chat/room/' + val,
		data: {
			rollType: 'M',
			withChatMessageList: 'true',
			withMetaInfo: 'true',
			statusMenu: $('.admin_counseling .sub_stit').data('select-status')
		},
		dataType : 'html',
	}).done(function(data) {
		$('.counseling_mid').html(data).show();
	});
}

//////////////////////////////////////////////////////////////////////////
// 매니저 - 상담정보 조회
// 팝업
$(document).on('click', '#btn_info_view', function(e) {

	$.ajax({
		url : HT_APP_PATH + '/chatCsnr/selectCustomerInfoAjaxManager',
		type : 'POST', 
		data : {
			chatRoomUid: $('#manchatRoomUid').val(),
		},
		dataType : 'html',
	}).done(function(data) {
		$('.popup_info_view').html(data).show();
	}).fail(function(jqXHR, textStatus, errorThrown) {
		console.error('FAIL REQUEST: END REVIEW: ', textStatus);
	});
});