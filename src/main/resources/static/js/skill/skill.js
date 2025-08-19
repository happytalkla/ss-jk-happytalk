'use strict';

// ////////////////////////////////////////////////////////////////////////////
// Main
// ////////////////////////////////////////////////////////////////////////////
$(document).ready(function() {

	var $name = $('#name');
	var $editor = $('#editor');
	var currentEntityId = $editor.data('id');
	var $propertyList = $('#property-list');
	var keyword = $('#list-search-keyword').val().trim();
	var skillList ;
	
	$.ajax({
		url: APP_PATH + '/api/data/skill/search/findConditionList',
		method: 'get',
		dataType: 'json',
		data: {}
	}).done(function(data) {
		if (data) {
			skillList = data;
			console.log(skillList);
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
		
		$.each(skillList, function(i, block) {
			var content = block.name;
			keyword = keyword.replace(regexp,"");
			content = content.replace(regexp,"").match(keyword);
			if (content !== null) {
				var $blockItem = $('<li class="list-group-item list-group-item-action" data-id="' + block.id + '">');
				$blockItem.append($('<a href="javascript:void(0);" class="play fas fa-play"><span></span></a>'));
				$blockItem.append($('<a href="' + APP_PATH + '/editor/skill/' + block.id + '" class="text-truncate" title="' + block.name +'">' + block.name + '</a>'));
				$('#list-contents').append($blockItem);
			}
		});
		UI.scrollToSelected(currentEntityId);
		UI.setHeightWhenResize();
	});

	// ////////////////////////////////////////////////////////////////////////
	// 페이지 로드시, 엔터티 로드
	// ////////////////////////////////////////////////////////////////////////
	if (currentEntityId) {
		$.ajax({
			url: APP_PATH + '/api/skill/' + currentEntityId,
			method: 'get',
			dataType: 'json'
		}).done(function(data) {
			console.info('LOAD ENTITY', data);
			var result = data.result;

			$('#id').val(result.id);
			$('#name').val(result.name);
			$('#resources').val(result.resources);
			$('#code').val(result.code);
			if (typeof result.bot_skill_property_list !== 'undefined') {
				result.bot_skill_property_list.forEach(function (property) {
					var $property = $('#template .property').clone();
					$propertyList.append($property);
					$('[name=id]', $property).val(property.id);
					$('[name=key]', $property).val(property.key);
					$('[name=key]', $property).attr('value', property.key);
					$('[name=name]', $property).val(property.name);
					//$('[name=format]', $property).val(property.format);
				});
			}
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED GET SKILL', textStatus, jqXHR);
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 컨트롤 영역
	// ////////////////////////////////////////////////////////////////////////
	// 엔터티 저장 버튼
	$('#btn-save').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		// 이름 필수
		if ($name.val() === '') {
			$name.focus();
			Utils.modal('스킬명을 입력하세요.');
			return false;
		}

		// 요청 파라미터
		var data = {
			'id': parseInt($('#id').val())
			, 'resources': $('#resources').val().trim()
			, 'name': $('#name').val().trim()
			, 'code': $('#code').val().trim()
			, 'type': 'rest'
			, 'use_yn': 'Y'
			, 'sort': 1
			, 'bot_skill_property_list': []
		};

		// 스킬 속성 목록
		$('.property', $propertyList).each(function(i) {
			var property = {
				'id': $(this).find('input[name=id]').val()
				, 'key': $(this).find('input[name=key]').val().trim()
				, 'name': $(this).find('input[name=name]').val().trim()
				, 'format': ''
				, 'type': 'strings'
				, 'use_yn': 'Y'
				, 'sort': i + 1
			};

			if (property.key) {
				data.bot_skill_property_list.push(property);
			}
		});

		console.info(data);

		$.ajax({
			url: APP_PATH + '/api/skill',
			method: 'post',
			dataType: 'json',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(data)
		}).done(function(data) {
			console.info(data);
			window.location.href = APP_PATH + '/editor/skill/' + data.result.id;
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED SAVE SKILL', textStatus);
			Utils.error(jqXHR);
		});
	});

	// 엔터티 삭제 버튼
	$('#btn-delete').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$.ajax({
			url: APP_PATH + '/api/skill/' + $('#id').val(),
			method: 'delete'
		}).done(function(data) {
			console.info(data);
			window.location.href = APP_PATH + '/editor/skill';
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED DELETE SKILL', textStatus);
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

	// 예제 버튼
	$('.modal-format .btn-primary').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var stringJSON = $('.modal-format textarea').val();
		try {
			var flattenJSON = Utils.flatJSON(JSON.parse(stringJSON));

			// console.info(flattenJSON);
			// $('.modal-format textarea').val(JSON.stringify(flattenJSON));

			$.each(flattenJSON, function(key, value) {
				if ($('.property [name=key][value="' + key + '"]', $propertyList).length === 0) {
					if (typeof value === 'string' || typeof value === 'number') {
						var $currentProperty = $('#template .property').clone();
						$propertyList.append($currentProperty);
						$currentProperty.find('[name=key]').val(key);
					}
				} else {
					console.info('SKIP ALREADY EXIST PROPERTY');
				}
			});
			$('.modal-format .btn-secondary').trigger('click'); // 닫기
		} catch (e) {
			console.error(e);
		}
	});

	// ////////////////////////////////////////////////////////////////////////
	// 편집기 영역
	// ////////////////////////////////////////////////////////////////////////
	// 포맷 추가 버튼
	$('#btn-create-property').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		var $currentProperty = $('#template .property').clone();
		$propertyList.append($currentProperty);
		$(document).scrollTop($(document).height());
	});

	// 포맷 삭제 버튼
	$propertyList.on('click', '.btn-delete-property', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$(this).closest('.property').remove();
	});

	console.debug('DOCUMENT READY');
});

$(window).on('load', function() {

	console.debug('WINDOW LOAD');
});
