'use strict';

// ////////////////////////////////////////////////////////////////////////////
// Main
// ////////////////////////////////////////////////////////////////////////////
$(document).ready(function() {

	var scenarioId = parseInt($('#list-search-scenario-id').val());
	var keyword  = $('#list-search-keyword').val().trim();
	var $name = $('#name');
	var $editor = $('#editor');
	var currentEntityId = $editor.data('id');
	var $balloonList = $('#balloon-list');
	var blockList;
	
	$.ajax({
		url: APP_PATH + '/api/data/block/search/findByBlockNameContainingAndUseYn',
		method: 'get',
		dataType: 'json',
		data: {
			'size': 1000,
			'page': 0,
			'sort': 'name,asc',
			'scenarioId': scenarioId,
			'keyword': keyword,
			'useYn': 'Y',
			'nameTriggerCodeYn': 'Y'
		}
	}).done(function(data) {
		if (data) {
			blockList = data;
		}
	}).fail(function(jqXHR, textStatus) {
		console.error('FAILED SEARCH BLOCK', textStatus);
		Utils.error(jqXHR);
	});

	// ////////////////////////////////////////////////////////////////////////
	// 목록 영역
	// ////////////////////////////////////////////////////////////////////////
	// 선택된 엔터티로 스크롤
	UI.scrollToSelected(currentEntityId);

	// 엔터티 검색, 키보드 트리거
	$('#list-search-keyword').on('keyup', function(e) {
		$('#btn-search-keyword').trigger('click');
	});

	// 엔터티 검색, 검색 버튼
	$('#btn-search-keyword').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();
		$('#list-contents').empty();
		
		var keyword = $('#list-search-keyword').val().trim();
		var regexp = /[\{\}\[\}\/?.,;:|\)*~'!^\-_+<>@\#$%&\\\=|(\'\"]/gi;
		$.each(blockList, function(i, block) {
			var content = block.name;
			keyword = keyword.replace(regexp,"");
			content = content.replace(regexp,"").match(keyword);
			if (content !== null) {
				var $blockItem = $('<li class="list-group-item list-group-item-action" data-id="' + block.id + '">');
				$blockItem.append($('<a href="javascript:void(0);" class="play fas fa-play"><span></span></a>'));
				$blockItem.append($('<a href="' + APP_PATH + '/editor/block/' + block.id + '" class="text-truncate" title="' + block.name +'">' + block.name + '</a>'));
				$('#list-contents').append($blockItem);
			}
		});
		UI.scrollToSelected(currentEntityId);
		UI.setHeightWhenResize();
	});

	// 시뮬레이션 버튼
	$('#list-contents .list-group-item .play').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var url = $('#simulate-url').val() + '&blockId=' + $(this).parent().data('id') + '&channel=C';
		console.info('POPUP URL', url);
		window.open(url, 'simulate', 'width=500,height=800,toolbar=no');
	});

	// ////////////////////////////////////////////////////////////////////////
	// 페이지 로드시, 엔터티 로드
	// ////////////////////////////////////////////////////////////////////////
	// 말풍선 템플릿으로 말풍선 한 개 생성
	$balloonList.append($('#template .balloon').clone());

	if (currentEntityId) {
		$.ajax({
			url: APP_PATH + '/api/block/' + currentEntityId,
			method: 'get',
			dataType: 'json'
		}).done(function(data) {
			console.info('LOAD ENTITY', data);
			var result = data.result;

			$('#id').val(result.id);
			$('#name').val(result.name);
			$('#type').val(result.type);
			$('#skill-id').val(result.skill_id).selectpicker('refresh');
			$('#trigger-code').val(result.trigger_code);
			$('#pre-trigger-code').val(result.trigger_code);
			$('#auto-block-id').val(result.auto_block_id).selectpicker('refresh');
			$('#intent').val(result.intent);
			$('#end-category-id').val(result.end_category_id);
			$('#use-yn').val(result.use_yn);
			$('#deletable-yn').val(result.deletable_yn);
			if (result.auto_call_api_url) {
				var autoCallApiUrls = result.auto_call_api_url.split('=');
				if (autoCallApiUrls.length === 3) {
					var autoCallApiUrlParam = autoCallApiUrls[2];
					$('#auto-call-api-param').val(autoCallApiUrlParam).selectpicker('refresh')
				}
			}
			
			$('#block-orient').val(result.orient).selectpicker('refresh');;
			
			var chatContents = JSON.parse(result.cont);
			chatContents.balloons.forEach(function(balloon, i) {

				console.debug('balloon', i, balloon);
				var $currentBalloon = $('.balloon:eq(' + i + ')', $balloonList);
				if ($currentBalloon.length === 0) {
					$currentBalloon = $('#template .balloon').clone();
					$balloonList.append($currentBalloon);
				}

				console.debug('$currentBalloon', $currentBalloon);
				balloon.sections.forEach(function(section, j) {
					console.debug(j, section);
					if (section.type === CONTENTS_SECTION_TYPE.FILE) { // 이미지

						$('input[name=section-file]', $currentBalloon).val(section.data);
						if (section.display && section.display.indexOf('image') === 0) {
							$('.img-viewer', $currentBalloon).attr('src', section.data);
							$('.img-file-name', $currentBalloon).text(section.data);
							$('.btn-delete-image', $currentBalloon).show();
							$('.btn-upload-image', $currentBalloon).hide();
						}
					}
					else if (section.type === CONTENTS_SECTION_TYPE.TEXT) { // 텍스트

						if ( section.data.length > 0 ) {
							$('#balloon-list .count').css('color', '#f55000');
						} else {
							$('#balloon-list .count').css('color', '#888');
						}
						$('#balloon-list .count').text(section.data.length + '자');
						$('textarea[name=section-text]', $currentBalloon).val(section.data);

					}
					else if (section.type === CONTENTS_SECTION_TYPE.ACTION) { // 액션

						var $actionList = $('.section.action-list .action-list-contents', $currentBalloon);
						section.actions.forEach(function(action, k) {
							console.debug('ACTION', k, action);
							var $currentAction = $('.action:eq(' + k + ')', $actionList);
							if ($currentAction.length === 0) {
								$currentAction = $('#template .action').clone();
								$actionList.append($currentAction);
							}

							console.debug('$currentAction', $currentAction);
							console.debug('ACTION[' + k + ']: ', action.type);

							$('[name=action-type]', $currentAction).val(action.type); // 액션 타입 세팅
							//$('.action-type', $currentAction).hide();

							if (action.type === CONTENTS_ACTION_TYPE.MESSAGE) { // 메세지 액션
								$('.action-type.message-action', $currentAction).show();
								$('.action-type.message-action [name=id]', $currentAction).val(action.id);
								$('.action-type.message-action [name=name]', $currentAction).val(action.name);
								$('.action-type.message-action [name=data]', $currentAction).val(action.data);
								// console.debug(action.extra);
								if (action.extra) {
									var extraAndParams = action.extra.split('?');
									$('.action-type.message-action [name=extra]', $currentAction).val(extraAndParams[0]);
									if (extraAndParams.length === 2) {
										$('.action-type.message-action [name=params]', $currentAction).val(extraAndParams[1]);
									}
								}
							}
							else if (action.type === CONTENTS_ACTION_TYPE.LINK) { // 링크 액션
								$('.action-type.link-action', $currentAction).show();
								$('.action-type.link-action [name=id]', $currentAction).val(action.id);
								$('.action-type.link-action [name=name]', $currentAction).val(action.name);
								$('.action-type.link-action [name=data]', $currentAction).val(action.data);
								$('.action-type.link-action [name=device-type]', $currentAction).val(action.device_type);
								$('.action-type.link-action [name=extra]', $currentAction).val(action.extra);
								$('.action-type.link-action [name=keyword]', $currentAction).val(action.keyword);
							}
							else if (action.type === CONTENTS_ACTION_TYPE.HOTKEY) { // 핫키 액션
								$('.action-type.hotkey-action', $currentAction).show();
								// 핫키 종류 선택
								$('.action-type.hotkey-action [name=data]', $currentAction).val(action.extra);
								// '전문상담직원 연결' 핫키 선택, TODO: 핫키 타입을 정의해야하나?!
								if (action.extra.indexOf(CONTENTS_HOTKEY_TYPE.HAPPY_TALK__REQUEST_COUNSELOR_WITH_CATEGORY) === 0) {
									$('.action-type.hotkey-action [name=data]', $currentAction).val(CONTENTS_HOTKEY_TYPE.HAPPY_TALK__REQUEST_COUNSELOR_WITH_CATEGORY);
								}
								$('.action-type.hotkey-action [name=id]', $currentAction).val(action.id);
								$('.action-type.hotkey-action [name=name]', $currentAction).val(action.name);
								$('.action-type.hotkey-action [name=extra]', $currentAction).val(action.extra);

								// '마지막 조건 블록' 핫키
								if (action.extra === CONTENTS_HOTKEY_TYPE.HAPPY_BOT__LAST_GREEN_CONDITION_BLOCK) {
									$('.hotkey-last-green-condition-block', $currentAction).show();
									$('.action-type.hotkey-action [name=lgcb-name]', $currentAction).val(action.name);
								}
								// '전문상담직원 연결' 핫키
								else if (action.extra.indexOf(CONTENTS_HOTKEY_TYPE.HAPPY_TALK__REQUEST_COUNSELOR_WITH_CATEGORY) === 0) {
									$('.hotkey-request-counselor-with-category', $currentAction).show();
									$('.action-type.hotkey-action [name=rcd-name]', $currentAction).val(action.name);
									$('.action-type.hotkey-action [name=rcd-extra]', $currentAction).val(action.extra);
								}
							}
							else {
								console.warn('INVALID ACTION', action);
							}

							$('select', $currentAction).selectpicker('refresh');
						});
					} else {
						console.warn('INVALID SECTION', section);
					}
				});
			});
			//선택이미지 변환
			$('.selectIcon [name=keyword]').trigger('change');

			// 종료 분류 표시
			if (typeof result.end_category_id !== 'undefined') {
				$.ajax({
					url: HAPPYTALK_APP_PATH + '/api/end-category/' + result.end_category_id,
					method: 'get',
					dataType: 'json'
				}).done(function(endData) {
					console.info(endData);
					$('#end-category-selected span').text(
							endData.depth1_cd_nm
							+ ' > ' + endData.depth2_cd_nm
							+ ' > ' + endData.depth3_cd_nm
							+ ' > ' + endData.depth4_cd_nm
						);
				}).fail(function(jqXHR, textStatus) {
					console.error('FAILED GET END CATEGORY', textStatus, jqXHR);
				});
			}
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED SAVE BLOCK', textStatus, jqXHR);
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 컨트롤 영역
	// ////////////////////////////////////////////////////////////////////////
	// 엔터티 저장 버튼
	$('#btn-save-block').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if ($(this).hasClass('btn-primary') === false) {
			return;
		}

		// 이름 필수
		if ($name.val() === '') {
			$name.focus();
			Utils.modal('블록명을 입력하세요.');
			return false;
		}

		// 이미지 텍스트 필수
		var isValid = true;
		$('.balloon', $balloonList).each(function() {
			var $image = $('input[name=section-file]', $(this));
			var $text = $('textarea[name=section-text]', $(this));
			// console.info('image', $image.val(), 'text', $text.val());
			if ($image.val() === '' && $text.val() === '') {
				$text.focus();
				isValid = false;
			}
		});
		if (isValid === false) {
			Utils.modal('"이미지 섹션"과 "텍스트 섹션" 중 하나는 필수 입니다.');
			return false;
		}

		// 말풍선 빌드
		var chatContents = {
			'v': CONTENTS_VERSION,
			'orient': $('#block-orient').val(),
			'balloons': []
		};

		$('.balloon', $balloonList).each(function() {

			var balloon = {};
			balloon.sections = [];
			$('.section', $(this)).each(function() {
				console.info('section', $(this));
				var section = {};

				if ($(this).hasClass('image') && $('input[name=section-file]', $(this)).val()) { // 이미지

					section.type = CONTENTS_SECTION_TYPE.FILE;
					section.data = $('input[name=section-file]', $(this)).val();
					section.display = 'image'; // TODO: MIME TYPE $('input[name=display]', $(this)).val();
				}
				else if ($(this).hasClass('text') && $('textarea[name=section-text]', $(this)).val()) { // 텍스트

					section.type = CONTENTS_SECTION_TYPE.TEXT;
					section.data = $('textarea[name=section-text]', $(this)).val();

				}
				else if ($(this).hasClass('action-list')) { // 액션

					section.type = CONTENTS_SECTION_TYPE.ACTION;
					section.actions = [];
					$('.action', $(this)).each(function() {
						var action = {};
						action.type = $('select[name=action-type]', $(this)).val();
						var $actionType = $('.action-type:visible', $(this));
						if ($actionType.hasClass('hotkey-action')) { // 핫키 액션

							if ($('[name=id]', $actionType).val()) {
								action.id = $('[name=id]', $actionType).val();
							}
							action.name = $('[name=name]', $actionType).val();
							action.extra = $('[name=extra]', $actionType).val();

							// '마지막 조건 블록' 핫키
							if (action.extra === CONTENTS_HOTKEY_TYPE.HAPPY_BOT__LAST_GREEN_CONDITION_BLOCK) {
								action.name = $('[name=lgcb-name]', $actionType).val();
								if (!action.name) {
									Utils.modal('버튼 이름은 필수값입니다.');
									$('[name=lgcb-name]', $actionType).focus();
									isValid = false;
									return false;
								}
							}
							// '전문상담직원 연결' 핫키
							else if (action.extra.indexOf(CONTENTS_HOTKEY_TYPE.HAPPY_TALK__REQUEST_COUNSELOR_WITH_CATEGORY) === 0) {
								action.name = $('[name=rcd-name]', $actionType).val();
								action.extra = $('[name=rcd-extra]', $actionType).val();
								if (!action.name) {
									Utils.modal('버튼 이름은 필수값입니다');
									$('[name=rcd-name]', $actionType).focus();
									isValid = false;
									return false;
								}
							}
						} else if ($actionType.hasClass('message-action')) { // 메세지 액션 (블록연결 버튼)

							if ($('[name=id]', $actionType).val()) {
								action.id = $('[name=id]', $actionType).val();
							}
							action.name = $('[name=name]', $actionType).val();
							action.data = $('[name=data]', $actionType).val();
							action.extra = $('[name=extra]', $actionType).val();

							// 버튼이름
							if (!action.name) {
								Utils.modal('버튼 이름은 필수값입니다.');
								$('[name=name]', $actionType).focus();
								isValid = false;
								return false;
							}
							// 블록 선택
							if (!action.extra) {
								Utils.modal('블록선택은 필수값입니다');
								$('[name=extra]', $actionType).focus();
								isValid = false;
								return false;
							}

							// 파라미터 추가
							var params = $('[name=params]', $actionType).val();
							if (params) {
								action.extra += '?' + params;
							}
							console.debug(action.extra);

						} else if ($actionType.hasClass('link-action')) { // 링크 액션

							if ($('[name=id]', $actionType).val()) {
								action.id = $('[name=id]', $actionType).val();
							}
							action.name = $('[name=name]', $actionType).val();
							action.data = $('[name=data]', $actionType).val();
							action.extra = $('[name=extra]', $actionType).val();
							action.device_type = $('[name=device-type]', $actionType).val();
							action.keyword = $('[name=keyword]', $actionType).val();
							
							// 버튼이름
							if (!action.name) {
								Utils.modal('버튼 이름은 필수값입니다.');
								$('[name=name]', $actionType).focus();
								isValid = false;
								return false;
							}
							
							// 블록 선택
							if (!action.device_type) {
								Utils.modal('디바이스 선택은 필수값입니다');
								$('[name=device-type]', $actionType).focus();
								isValid = false;
								return false;
							}
						} else {
							console.error('INVALID ACTION TYPE');
						}

						action.scenario_id = scenarioId;
						if (typeof action.device_type === 'undefined') {
							action.device_type = 'all';
						}
						if (typeof action.type !== 'undefined'
							&& typeof action.name !== 'undefined') {
							section.actions.push(action);
						}
					});
				} else {
					console.debug('SKIP SECTION: %o', $(this));
				}
				if (section.data
					|| (section.type === CONTENTS_SECTION_TYPE.ACTION && section.actions.length > 0)) {
					balloon.sections.push(section);
				}
			});

			chatContents.balloons.push(balloon);
		});

		console.info('chatContents', chatContents);
		console.info('chatContents', JSON.stringify(chatContents));

		if (isValid === false) {
			return false;
		}

		// 요청 파라미터
		var data = {
			'id': parseInt($('#id').val())
			, 'name': $('#name').val().trim()
			, 'type': $('#type').val().trim()
			, 'scenario_id': scenarioId
			, 'skill_id': $('#skill-id').val()
			, 'trigger_code': $('#trigger-code').val().trim()
			, 'pre_trigger_code': $('#pre-trigger-code').val().trim()
			, 'auto_block_id': parseInt($('#auto-block-id').val())
			, 'intent': $('#intent').val()
			, 'end_category_id': $('#end-category-id').val()
			, 'cont': JSON.stringify(chatContents)
			, 'use_yn': $('#use-yn').val()
			, 'deletable_yn': $('#deletable-yn').val()
			, 'orient': $('#block-orient').val()
		};

		// 자동 API 호출 파라미터 추가
		if ($('#auto-call-api-param').val()) {
			data.auto_call_api_url = $('#auto-call-api-url').val() + $('#auto-call-api-param').val();
		}

		console.debug(data);

		$.ajax({
			url: APP_PATH + '/api/block',
			method: 'post',
			dataType: 'json',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(data)
		}).done(function(data) {
			console.info(data);
			window.location.href = APP_PATH + '/editor/block/home';
			window.location.href = APP_PATH + '/editor/block/' + data.result.id;
			alert('저장되었습니다');
			window.location.href = APP_PATH + '/editor/block/home';
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED SAVE BLOCK', textStatus);
			Utils.error(jqXHR);
		});
	});

	// 엔터티 삭제 버튼
	$('#btn-delete-block').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$.ajax({
			url: APP_PATH + '/api/block/' + $('#id').val(),
			method: 'delete'
		}).done(function(data) {
			console.info(data);
			alert('삭제 되었습니다');
			window.location.href = APP_PATH + '/editor/block/home';
			//window.location.href = APP_PATH + '/editor/block/home?scenarioId=' + parseInt($('#scenario-list .active').data('id'));
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED DELETE BLOCK', textStatus);
			if (jqXHR && jqXHR.responseJSON) {
				var data = jqXHR.responseJSON;
				console.info(data);
				var linkedBlockList = data.result;
				if (linkedBlockList) {
					var blockNameList = $.map(linkedBlockList, function(item) {
						return '<a href="'+ APP_PATH + '/editor/block/' + item.id +'" title="' + item.name + '" target="_blank">' + item.name + '</a>';
					}).join('<br>');
					Utils.modal(data.message.replace(/(?:\r\n|\r|\n)/g, '<br>') + '<p>' + blockNameList + '</p>');
				} else {
					Utils.modal(data.message);
				}
			}
		});
	});

	// 엔터티 복사 버튼
	$('#btn-clone-block').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$.ajax({
			url: APP_PATH + '/api/block/clone/' + $('#id').val(),
			method: 'post'
		}).done(function(data) {
			console.info(data);
			alert('복사 되었습니다');
			window.location.href = APP_PATH + '/editor/block/home';
			window.location.href = APP_PATH + '/editor/block/' + data.result.id;
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED CLONE BLOCK', textStatus);
			Utils.error(jqXHR);
		});
	});

	// 버튼 활성화
	$editor.on('change', function() {
		$('#btn-test-block').removeClass('btn-secondary').addClass('btn-primary');
	});

	// ////////////////////////////////////////////////////////////////////////
	// 편집기 영역
	// ////////////////////////////////////////////////////////////////////////
	// 말풍선 추가 버튼
	var MAX_BALLOON = 10;
	$('#btn-create-balloon').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if ($('#skill-id').val()) {
			Utils.modal('스킬 사용시 말풍선은 스킬 결과에 따라 동적으로 구성되므로, 말풍선 한 개만 사용할 수 있습니다.');
			return false;
		}

		var currentCount = $('.balloon', $balloonList).length;
		console.info('currentCount', currentCount, 'maxCount', MAX_BALLOON);
		if (currentCount >= MAX_BALLOON) {
			Utils.modal('말풍선은 ' + MAX_BALLOON + '개 이상 만들 수 없습니다.');
		} else {
			$balloonList.append($('#template .balloon').clone());
			$(document).scrollTop($(document).height());
		}
	});

	// 말풍선 삭제 버튼
	$balloonList.on('click', '.btn-delete-balloon', function(e) {

		e.preventDefault();
		e.stopPropagation();
		console.info('DELETE BALLOON');

		if ($('.balloon', $balloonList).length === 1) {
			Utils.modal('마지막 말풍선은 삭제할 수 없습니다.');
		} else {
			$(this).closest('.balloon').remove();
		}

		return false;
	});

	// 스킬 선택
	$('#skill-id').on('change', function() {
		if ($(this).val()) {
			if ($('.balloon', $balloonList).length > 1) {
				Utils.modal('스킬 사용시 말풍선은 스킬 결과에 따라 동적으로 구성되므로, 말풍선 한 개만 사용할 수 있습니다.');
				$(this).val($('option:first', $(this)).val());
				return false;
			}
		}
	});

	// 액션 추가 버튼
	var MAX_ACTION = 5;
	$balloonList.on('click', '.btn-create-action', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var $balloon = $(this).closest('.balloon');
		var currentCount = $('.action', $balloon).length;
		console.info('currentCount', currentCount, 'maxCount', MAX_ACTION);
		if (currentCount >= MAX_ACTION) {
			Utils.modal('버튼은 ' + MAX_ACTION + '개 이상 만들 수 없습니다.');
		} else {
			var action = $('#template .action').clone();
			$('.action-list-contents', $balloon).append(action);
			$(action.find('select')).selectpicker('refresh');
		}
	});

	// 액션 변경
	$balloonList.on('change', '.action [name=action-type]', function() {
		var $action = $(this).closest('.action');
		$('.action-type', $action).hide();
		switch ($(this).val()) {
			case 'message':
				$('.action-type.message-action', $action).show();
				break;
			case 'link':
				$('.action-type.link-action', $action).show();
				break;
			case 'hotkey':
				$('.action-type.hotkey-action', $action).show();
				break;
			default:
				break;
		}
		$('select', $action).selectpicker('refresh');
	});
	
	$balloonList.on('change', '.selectIcon [name=keyword]', function(e) {
		var $action = $(this).closest('.action');
		var $selectIcon = $(this).closest('.selectIcon');
		var $iconView = $('.iconView',$action);
		
		$($iconView).attr('src','/happybot/static/img/icon_line_bottom_'+$selectIcon.val()+'.png');
		
		$($action.find('select')).selectpicker('refresh');
	});

	// 액션 삭제 버튼
	$balloonList.on('click', '.btn-delete-action', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$(this).closest('.action').remove();
	});

	// 핫키 종류 변경
	$balloonList.on('change', '.action .hotkey-action [name=data]', function(e) {;

		var $hotkey = $(this).closest('.hotkey-action');
		$('[name=name]', $hotkey).val($('option:checked', $(this)).text());
		$('[name=data]', $hotkey).val($(this).val());
		$('[name=extra]', $hotkey).val($(this).val());

		if ($(this).val() === CONTENTS_HOTKEY_TYPE.HAPPY_BOT__LAST_GREEN_CONDITION_BLOCK) {
			$('.hotkey', $hotkey).hide();
			$('.hotkey-last-green-condition-block', $hotkey).show();
		}
		else if ($(this).val() === CONTENTS_HOTKEY_TYPE.HAPPY_TALK__REQUEST_COUNSELOR_WITH_CATEGORY) {
			$('.hotkey', $hotkey).hide();
			$('.hotkey-request-counselor-with-category', $hotkey).show();
		}
		else {
			$('.hotkey', $hotkey).hide();
		}
	});

	// 이미지 업로드
	$balloonList.on('click', '.btn-upload-image', function(e) {
		var $parent = $(this).closest('.section.image');
		$('input[name=section-file]', $parent).val('');
//		$('.img-viewer', $parent).attr('src', '');
		$('.upload-image', $parent).val('');
		$('.img-file-name', $parent).text('선택된 이미지가 없습니다');

		$('.form-control-file', $parent).trigger('click');
	});
	$balloonList.on('change', '.section.image .upload-image', function(e) {
		e.preventDefault();
		e.stopPropagation();

		var $parent = $(this).closest('.section.image');
		$('.img-viewer', $parent).attr('src', '');
		
		var formData = new FormData();
		var file = $(this)[0].files;
		
		if(!/\.(jpeg|jpg|png|gif|bmp)$/i.test(file[0].name)){
			alert("이미지 파일만 업로드 가능합니다.");
		}else{
			formData.append('file', file[0]);

			Utils.uploadFile(formData, 'bot', function(data) {
				console.info('UPLOADED IMAGE', data);
				
				$('input[name=section-file]', $parent).val(data.url);
				$('.img-viewer', $parent).attr('src', data.url);
				$('.img-file-name', $parent).text(file[0].name);
			});
			$(this).blur();
			$('.btn-upload-image', $parent).hide();
			$('.btn-delete-image', $parent).show();
		}
		
	});

	// 이미지 삭제
	$balloonList.on('click', '.btn-delete-image', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var $parent = $(this).closest('.section.image');
		$('input[name=section-file]', $parent).val('');
		$('.img-viewer', $parent).attr('src', APP_PATH + '/static/img/no_image.png');
		$('.btn-upload-image', $parent).show();
		$('.btn-delete-image', $parent).hide();
	});

	// 메세지 1000자 체크
	$balloonList.on('change keyup', 'textarea', function(e) {
		if (e.target.value.length > 0)  {
			$('#balloon-list .count').css('color', '#f55000');
		} else {
			$('#balloon-list .count').css('color', '#888');
		}
		$('#balloon-list .count').text(e.target.value.length + '자');
	});

	// 블록 파일 업로드
	$('#btn-import-scenario').on('click', function(e) {
		$('#import-scenario').trigger('click');
	});
	$('#import-scenario').on('change', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var formData = new FormData();
		var file = $(this)[0].files;
		formData.append('file', file[0]);

		var $self = $(this);

		$.ajax({
			url: APP_PATH + '/api/scenario/import?scenarioId=' + scenarioId,
			type: 'post',
			data: formData,
			contentType: false,
			processData: false,
			dataType: 'json'
		}).done(function(data) {
			console.info('IMPORT SCENARIO', data);
			alert(data.message + '\n홈 페이지로 이동합니다.');
			window.location.href = APP_PATH + '/editor/block/home?scenarioId=' + scenarioId;
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED IMPORT SCENARIO', textStatus);
			Utils.error(jqXHR);
		}).always(function() {
			$self.val('');
		});

		$(this).blur();
	});

	console.debug('DOCUMENT READY');
	
});

$(window).on('load', function() {

	console.debug('WINDOW LOAD');
	// 말풍선 드래그 & 드랍
	new Sortable(document.getElementById('balloon-list'), {
		handle: '.btn-sort-balloon'
	});

	// ////////////////////////////////////////////////////////////////////////
	// 종료 분류 처리
	// ////////////////////////////////////////////////////////////////////////

	// 검색 필드에 포커스
	$('#end-category-modal').on('shown.bs.modal', function() {
		$(this).find('[autofocus]').focus();
	});

	// 종료 분류 검색
	$('#end-category-modal .modal-body .btn-search').on('click', function(e) {
		e.preventDefault();
		e.stopPropagation();

		var keyword = $('#end-category-keyword').val().trim();
		if (typeof keyword === 'undefined' || keyword.length < 2) {
			Utils.modal('분류 이름을 2자 이상 입력해주세요.');
		}
		else {
			$.ajax({
				url: HAPPYTALK_APP_PATH + '/api/end-category',
				// /chatManage/endReviewPopup?chatRoomUid=144a4231-c892-4610-9142-a83fd382e76f&cstmUid=6d342e05-f646-4f5e-a84f-3d7a78bc3f23
				type: 'get',
				data: {
					keyword: keyword,
				},
				dataType: 'json'
			}).done(function(data) {
				console.info('GET CATEGORY', data);
				if (data) {
					$('#end-category-list').empty();
					data.forEach(function(category, i) {
						var $item = $('<option>');
						$('#end-category-list').append($item);
						$item.attr('data-depth1-cd', category.depth1_cd)
							.attr('data-depth2-cd', category.depth2_cd)
							.attr('data-depth3-cd', category.depth3_cd)
							.attr('data-depth4-cd', category.depth4_cd)
							.attr('data-depth1-nm', category.depth1_cd_nm)
							.attr('data-depth2-nm', category.depth2_cd_nm)
							.attr('data-depth3-nm', category.depth3_cd_nm)
							.attr('data-depth4-nm', category.depth4_cd_nm)
							.text(category.depth1_cd_nm + ' > ' + category.depth2_cd_nm + ' > '
									+ category.depth3_cd_nm + ' > ' + category.depth4_cd_nm)
					});
				}
				$('#end-category-list').focus();
			}).fail(function(jqXHR, textStatus) {
				console.error('FAILED GET CATEGORY', textStatus);
				Utils.error(jqXHR);
			});
		}
	});

	// 종료 분류 검색, 키보드 트리거
	$('#end-category-keyword').on('keyup', function(e) {
		if (e.keyCode === 13) {
			$('#end-category-modal .modal-body .btn-search').trigger('click');
		}
	});

	// 종료 분류 선택
	$('#end-category-list').on('click', function(e) {
		var $selected = $('option:selected', $(this));
		$('#end-category-id').val($selected.data('depth1-cd')
				+ '|' + $selected.data('depth2-cd')
				+ '|' + $selected.data('depth3-cd')
				+ '|' + $selected.data('depth4-cd'));
		$('#end-category-selected span').text($selected.text());

		// 닫기 포커스
		$('#end-category-modal .modal-footer .btn-secondary').focus();
	});

	// 종료 분류 선택, 키보드 트리거
	$('#end-category-list').on('keyup', function(e) {
		if (e.keyCode === 13) {
			$('#end-category-list').trigger('click');
		}
	});
});
