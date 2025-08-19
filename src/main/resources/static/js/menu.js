
$(document).ready(function() {
	if($(".left_content_area").length > 0){
		/*$(".left_content_area").css({"height":($(window).height()) - "128" +"px"});
		$("#snb").css({"height":($(document).height()) - "128" +"px"});
	    $(window).resize(function(){
			$(".left_content_area").css({"height":($(window).height()) - "128" +"px"});
			$("#snb").css({"height":($(document).height()) - "128" +"px"});
	    });*/

		/*$(".left_content_area").css({"height":($(window).height()) - "128" +"px"});
		$("#snb").css({"height":($(document).height()) - "128" +"px"});
	    $(window).resize(function(){
			$(".left_content_area").css({"height":($(window).height()) - "128" +"px"});
			$("#snb").css({"height":($(document).height()) - "128" +"px"});
	    });*/
	}
	
	$(".left_content_area").css("min-height",$(".snb_menu_area").css("height"));

	/*
	$(window).resize(function(){
		
		alert($(".snb_menu_area").css("height"));
		alert($(".btn_all_menu").css("display"));
		
		$(".left_content_area").css("min-height",$(".snb_menu_area").css("height"));
		
		console.info($(".btn_all_menu").css("display"));

		if($(".btn_all_menu").css("display") == "block"){
			$("#snb").hide();
		}else{
			$("#snb").show();
		}
	});
	*/
	
    
	$(".btn_view_snb").hide();
	$(".layer_alert").hide();
	$(document).tooltip();
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
	
	// 좌측 메뉴 session 저장 후 화면 refresh 할때 유지
	if(window.sessionStorage){
		var snbMenu = sessionStorage.getItem("snb_menu");
		if(snbMenu == "close"){
			$(".btn_close_snb").trigger("click");
		}
	}else{
		sessionStorage.setItem("snb_menu", "view");
	}

	// 창 작아질 경우 전체 메뉴 버튼 표시
    if (window.matchMedia('(max-width: 1024px)').matches) { 
// 		$("#snb").hide();
    }else {
// 		$("#snb").show();
    }
});

/**
 * ajax session 만료시 로그인 페이지로 이동.
 */
$.ajaxSetup({
	error: function(jqXHR, textStatus, errorThrown) {
		if (jqXHR.status === 401) {
			alert('세션이 만료되었습니다.'); 
			window.location.href = $('input[name="hidConetextPath"]').val() + '/login';
		} else if (jqXHR.status === 404) {
			console.error(jqXHR);
			console.error(errorThrown);
		} else if (errorThrown === 'abort') {
			console.info('=== ABORT AJAX REQUEST');
		} else {
			console.error(jqXHR);
			console.error(errorThrown);
			if (jqXHR.responseJSON && jqXHR.responseJSON.returnMessage) {
				fn_layerMessage(jqXHR.responseJSON.returnMessage);
			} else {
				fn_layerMessage("처리중 오류가 발생하였습니다.\n관리자에게 문의하세요.");
			}
		}
	}
});

$(document).on("click", ".btn_all_menu", function() {
	$("#snb").show();
});
$(document).on("click", ".btn_all_close", function() {
	$("#snb").hide();
});


$(document).on("click", ".btn_close_snb", function() {
	$(this).hide();
	$(".btn_view_snb").show();
	$("#snb").hide();
	$(".left_content_area").css('width', '100%');
	
	sessionStorage.setItem("snb_menu", "close");
});

$(document).on("click", ".btn_view_snb", function() {
	$(this).hide();
	$(".btn_close_snb").show();
	$("#snb").show();
	$(".left_content_area").css('width', 'calc(100% - 233px)');

	sessionStorage.setItem("snb_menu", "view");
});

var layerMessageTimeout = null;
function fn_layerMessage(msg){
	clearTimeout(layerMessageTimeout);
	$(".alert_text").text(msg);
	$(".layer_alert").show();
	
	layerMessageTimeout = setTimeout(function(){
		$(".layer_alert").fadeOut(1000);
	}, 2000);
}


// 회원 정보 조회 팝업창 열기
$(document).on("click", "#dev_memberInfo", function(e) {
	$("#dev_header_member_pop").show();
});

// 회원 정보 조회 팝업창 닫기
$(document).on("click", ".dev_close_header_pop", function(e) {
	$("#dev_header_member_pop").hide();
});

//회원 정보 조회 팝업창 닫기
$(document).on("click", ".dev_cancel_header_pop", function(e) {
	$("#dev_header_member_pop").hide();
});

//비밀번호 변경페이지
$(document).on('click', '.dev_password_change', function(e) {

	e.preventDefault();
	
	var memberUid = $(this).attr("data-memberUid");
	var id = $(this).attr("data-id");
	var nHnet = $(this).attr("data-nHnet");
	var dataUrl = $(this).attr("data-url");
	
	var url = dataUrl + '/changePasswdView?memberUid=' + memberUid+'&id='+id+'&nHnet='+nHnet;
	window.open(url);
	return false;
});
