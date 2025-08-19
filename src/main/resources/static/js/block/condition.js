'use strict';

// ////////////////////////////////////////////////////////////////////////////
// Main
// ////////////////////////////////////////////////////////////////////////////
$(document).ready(function() {
	
	var scenarioId = $('#list-search-scenario-id').val();
	var $name = $('#name');
	var $editor = $('#editor');
	var currentEntityId = $editor.data('id');
	var $conditionList = $('#condition-list');
	var keyword = $('#list-search-keyword').val().trim();
	var conditionList ;
	
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
			conditionList = data;
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
		
		var keyword = $('#list-search-keyword').val();
		var regexp = /[\{\}\[\}\/?.,;:|\)*~'!^\-_+<>@\#$%&\\\=|(\'\"]/gi;
		
		$.each(conditionList, function(i, block) {
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

		var url = $('#simulate-url').val() + '&blockId=' + $(this).parent().data('id') + '&channel=A';
		console.info('POPUP URL', url);
		window.open(url, 'simulate', 'width=500,height=800,toolbar=no');
	});

	// ////////////////////////////////////////////////////////////////////////
	// 페이지 로드시, 엔터티 로드
	// ////////////////////////////////////////////////////////////////////////
	// 조건 템플릿으로 조건 한 개 생성
	$conditionList.append($('#template .condition').clone());
	$('select', $conditionList).each(function() {
		$(this).selectpicker('refresh');
	});

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
			$('#trigger-code').val(result.trigger_code);
			$('#pre-trigger-code').val(result.trigger_code);
			$('#default-condition-block-id').val(result.default_condition_block_id).selectpicker('refresh');
			$('#intent').val(result.intent);
			$('#use-yn').val(result.use_yn);
			$('#deletable-yn').val(result.deletable_yn);

			if (result.block_condition_list) {
				result.block_condition_list.forEach(function(blockCondition, i) {
					console.info('blockCondition', i, blockCondition);
					var $currentCondition = $('.condition:eq(' + i + ')', $conditionList);
					if ($currentCondition.length === 0) {
						$currentCondition = $('#template .condition').clone();
						$conditionList.append($currentCondition);
					}
					$currentCondition.attr('data-id', blockCondition.id);
					$currentCondition.attr('data-block-id', blockCondition.block_id);
					$('select[name=condition-id]', $currentCondition).val(blockCondition.id).selectpicker('refresh');
					
					// 해당 키 외의 상태값 감추기
					/*if (typeof blockCondition.id !== 'undefined') {
						$('select[name=condition-property-id] option', $currentCondition).each(function() {
							console.info($(this).data('condition-id'), blockCondition.id);
							if (typeof $(this).data('condition-id') === 'undefined') {
								return true;
							} else if ($(this).data('condition-id') !== blockCondition.id) {
								$(this).hide();
							}
						});
					}*/
					
					$('select[name=condition-id]', $currentCondition).val(blockCondition.condition_id).selectpicker('refresh');
					$('select[name=target-block-id]', $currentCondition).val(blockCondition.target_block_id).selectpicker('refresh');
					
					// 해당 키 외의 상태값 감추기
					if (blockCondition.id) {
						var conditionId = $('select[name=condition-id]').val();
	
						var $currentCondition2 = $('select[name=condition-id]').closest('.condition');
						$('select[name=condition-property-id] option', $currentCondition2).show();
						$('select[name=condition-property-id] option', $currentCondition2).each(function() {
							if (typeof $(this).data('condition-id') === 'undefined') {
								return true;
							} else if ($(this).data('condition-id') !== parseInt(conditionId)) {
								$(this).hide();
							}
						});
						$('select[name=condition-property-id]', $currentCondition2).val(blockCondition.condition_property_id).selectpicker('refresh');
					}
				});

				// selectpicker가 아래로 내려가는 이슈
//				$('.condition', $conditionList).each(function() {
//					$(this).attr('style', '');
//				});
			}

			// 툴팁 세팅
			$(function() {
				$('[data-toggle="tooltip"]').tooltip();
			});
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

		// 조건 목록
		var blockConditionList = $('.condition', $conditionList).map(function(i) {
			return {
				'id': $(this).data('id'),
				'block_id': $(this).data('block-id'),
				'condition_id': $('select[name=condition-id]', $(this)).val(),
				'condition_property_id': $('select[name=condition-property-id]', $(this)).val(),
				'target_block_id': parseInt($('select[name=target-block-id]', $(this)).val()),
				'sort': i + 1
			};
		}).get();

		console.info('blockConditionList', blockConditionList);

		// 조건 목록 유효성 검증
		var isValidBlockConditionList = true;
		blockConditionList.forEach(function(blockCondition, i) {
			if (typeof blockCondition.condition_id === 'undefined'
				|| typeof blockCondition.condition_property_id === 'undefined'
				|| typeof blockCondition.target_block_id  === 'undefined') {
				isValidBlockConditionList = false;
			}
		});
		if (!isValidBlockConditionList) {
			Utils.modal('조건 목록에 선택하지 않은 값이 있습니다.');
			return;
		}

		// 말풍선 빌드
		var chatContents = {
			'v': CONTENTS_VERSION,
			'balloons': [{
				'sections': [{
					type: 'text',
					data: '조건 블록'
				}]
			}]
		};

		// 요청 파라미터
		var data = {
			'id': parseInt($('#id').val())
			, 'name': $('#name').val().trim()
			, 'type': $('#type').val().trim()
			, 'scenario_id': parseInt($('#scenario-list .active').data('id'))
			, 'trigger_code': $('#trigger-code').val().trim()
			, 'default_condition_block_id': parseInt($('#default-condition-block-id').val())
			, 'intent': $('#intent').val()
			, 'cont': JSON.stringify(chatContents)
			, 'use_yn': $('#use-yn').val()
			, 'deletable_yn': $('#deletable-yn').val()
			, 'pre_trigger_code': $('#pre-trigger-code').val().trim()	
			, 'block_condition_list': blockConditionList
		}

		$.ajax({
			url: APP_PATH + '/api/block',
			method: 'post',
			dataType: 'json',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(data)
		}).done(function(data) {
			console.info(data);
			window.location.href = APP_PATH + '/editor/block/' + data.result.id;
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED SAVE CONDITION BLOCK', textStatus);
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
			window.location.href = APP_PATH + '/editor/block/home?scenarioId=' + parseInt($('#scenario-list .active').data('id'));
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED DELETE CONDITION BLOCK', textStatus);
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

	// ////////////////////////////////////////////////////////////////////////
	// 편집기 영역
	// ////////////////////////////////////////////////////////////////////////

	// 조건 이름 변경시
	$conditionList.on('change', 'select[name=condition-id]', function(e) {

		e.preventDefault();
		e.stopPropagation();

		// 해당 키 외의 상태값 감추기
		var conditionId = $(this).val();
		// if (conditionId === '') {
		// 	conditionId = $('option:first', $(this)).attr('value');
		// }

		console.info('conditionId', conditionId, conditionId === '', typeof conditionId);

		var $currentCondition = $(this).closest('.condition');
		$('select[name=condition-property-id] option', $currentCondition).show();
		$('select[name=condition-property-id] option', $currentCondition).each(function() {
			console.info("option", $(this).data('condition-id'), $(this).data('condition-id') === conditionId, $(this).val());
			console.info('typeof $(this).data(\'condition-id\')', typeof $(this).data('condition-id'));
			if (typeof $(this).data('condition-id') === 'undefined') {
				return true;
			} else if ($(this).data('condition-id') !== parseInt(conditionId)) {
				$(this).hide();
			}
		});
		$('select[name=condition-property-id]', $currentCondition).val('').selectpicker('refresh');
	});

	// 조건 추가 버튼
	$('#btn-create-condition').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$conditionList.append($('#template .condition').clone());

		$('select', $conditionList).each(function() {
			$(this).selectpicker('refresh');
		});

		// 툴팁 세팅
		$(function() {
			$('[data-toggle="tooltip"]').tooltip();
		});

		$(document).scrollTop($(document).height());
	});

	// 조건 삭제 버튼
	$conditionList.on('click', '.btn-delete-condition', function(e) {

		e.preventDefault();
		e.stopPropagation();

		if ($('.condition', $conditionList).length === 1) {
			Utils.modal('마지막 조건은 삭제할 수 없습니다.');
		} else {
			$(this).closest('.condition').remove();
		}
	});
});

$(window).on('load', function() {

	var $conditionList = $('#condition-list');

	console.debug('WINDOW LOAD');
	// 조건 드래그 & 드랍
	new Sortable(document.getElementById('condition-list'), {
		handle: '.btn-sort-condition',
		onEnd: function(e) {
			// $('select', $conditionList).each(function() {
			// 	$(this).selectpicker('refresh');
			// });
			// selectpicker가 아래로 내려가는 이슈
			$('.condition', $conditionList).each(function() {
				$(this).attr('style', '');
			});
		}
	});
	// TODO: 드레그 후에 셀렉트 박스 리로드
});
