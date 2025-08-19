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
	var conditionList ;
	
	// 스킬 선택
	$('#skill-id').on('change', function() {
		
		var $skillpropertyid = $('#skill-property-id');
		
		if ($(this).val() === '') {
			$skillpropertyid.children('option:not(:first)').remove();
			$skillpropertyid.selectpicker('refresh');
		}else{
			$.ajax({
				url: APP_PATH + '/api/condition/skill/' + $(this).val(),
				method: 'get',
				dataType: 'json',
				data: {}
			}).done(function(data) {
				if (data.result.bot_skill_property_list) {
					$skillpropertyid.children('option:not(:first)').remove();
					$.each(data.result.bot_skill_property_list, function(index, item) {
						$skillpropertyid.append(new Option(item.name,item.id));
					});
					$skillpropertyid.selectpicker('refresh');
				}
			}).fail(function(jqXHR, textStatus) {
				console.error('FAILED GET SKILL-PROPERTY-LIST', textStatus, jqXHR);
			});
		}
		
	});
	
	$.ajax({
		url: APP_PATH + '/api/data/condition/search/findConditionList',
		method: 'get',
		dataType: 'json',
		data: {}
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
				$blockItem.append($('<a href="' + APP_PATH + '/editor/condition/' + block.id + '" class="text-truncate" title="' + block.name +'">' + block.name + '</a>'));
				$('#list-contents').append($blockItem);
			}
		});
		UI.scrollToSelected(currentEntityId);
		UI.setHeightWhenResize();
	});

	// ////////////////////////////////////////////////////////////////////////
	// 페이지 로드시, 엔터티 로드
	// //////////////////////////////////////////////////////////////////
	if (currentEntityId) {
		$.ajax({
			url: APP_PATH + '/api/condition/' + currentEntityId,//////
			method: 'get',
			dataType: 'json'
		}).done(function(data) {
			var result = data.result;
			
			$('#id').val(result.id);
			$('#name').val(result.name);
			$('#skill-id').val(result.skill_id).selectpicker('refresh');
			$('#skill-property-id').val(result.skill_property_id).selectpicker('refresh');
			if (typeof result.skill_condition_property_list !== 'undefined') {
				
				result.skill_condition_property_list.forEach(function (property) {
					var $currentConditionValue = $('#template .property').clone();
					$propertyList.append($currentConditionValue);
					$('[name=id]', $currentConditionValue).val(property.id);
					$('[name=expect]', $currentConditionValue).val(property.expect);
					$('[name=name]', $currentConditionValue).val(property.name);
					$('[name=description]', $currentConditionValue).val(property.description);
				});
				
			}
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED GET CONDITION', textStatus, jqXHR);
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	// 컨트롤 영역
	// ////////////////////////////////////////////////////////////////////////
	// 엔터티 저장 버튼
	$('#btn-save').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		
		if(isValidation()){return false;}

		// 요청 파라미터
		var data = {
			'id': parseInt($('#id').val())
			, 'name': $('#name').val().trim()
			, 'skill_id': $('#skill-id').val().trim()
			, 'skill_property_id': $('#skill-property-id').val().trim()
			, 'sort': 1
			, 'skill_condition_property_list': []
		};

		// 조건 값 목록
		$('.property', $propertyList).each(function(i) {
			var property = {
				'id': $(this).find('input[name=id]').val()
				, 'condition_id': data.id
				, 'name': $(this).find('input[name=name]').val().trim()
				, 'expect': $(this).find('input[name=expect]').val().trim()
				, 'description': $(this).find('input[name=description]').val().trim()
				, 'use_yn': 'Y'
				, 'sort': i + 1
			};

			if (typeof property.expect !== 'undefined'
				&& typeof property.name !== 'undefined') {
				data.skill_condition_property_list.push(property);
			}
		});

		$.ajax({
			url: APP_PATH + '/api/condition',
			method: 'post',
			dataType: 'json',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(data)
		}).done(function(data) {
			console.info(data);
			window.location.href = APP_PATH + '/editor/condition/' + data.result.id;
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED SAVE CONDITION', textStatus);
			Utils.error(jqXHR);
		});
	});

	// 엔터티 삭제 버튼
	$('#btn-delete').on('click', function(e) {

		e.preventDefault();
		e.stopPropagation();

		$.ajax({
			url: APP_PATH + '/api/condition/' + $('#id').val(),
			method: 'delete'
		}).done(function(data) {
			console.info(data);
			window.location.href = APP_PATH + '/editor/condition';
		}).fail(function(jqXHR, textStatus) {
			console.error('FAILED DELETE CONDITION', textStatus);
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

function isValidation(){
	// 조건명 필수
	
	if ($('#name').val() === '') {
		$('#name').focus();
		Utils.modal('조건명을 입력하세요.');
		return true;
	}
	
	if ($("#skill-id").val() === '') {
		$("#skill-id").focus();
		Utils.modal('스킬을 선택하세요.');
		return true;
	}
	
	if ($("#skill-property-id").val() === '') {
		$("#skill-property-id").focus();
		Utils.modal('스킬속성을 선택하세요.');
		return true;
	}
	var $propertyList = $('#property-list');
	var isValid = false;
	$('.property', $propertyList).each(function(i) {
		var expect 		= $('input[name=expect]', $(this)).val();
		var name 		= $('input[name=name]', $(this)).val();
		var description = $('input[name=description]', $(this)).val();
		
		if ( expect ==='') {
			Utils.modal('속성값을 입력하세요.');
			$('input[name=expect]', $(this)).focus();
			isValid = true;
			return true;
		}
		if ( name ==='') {
			Utils.modal('이름을 입력하세요.');
			$('input[name=name]', $(this)).focus();
			isValid = true;
			return true;
		}
		if ( description ==='') {
			Utils.modal('설명을 입력하세요.');
			$('input[name=description]', $(this)).focus();
			isValid = true;
			return true;
		}
	});
	
	return isValid;

}

$(window).on('load', function() {

	console.debug('WINDOW LOAD');
});
