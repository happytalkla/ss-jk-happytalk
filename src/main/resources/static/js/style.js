
/* === view more === */
$(document).ready(function() {
	$(".poopup_chat_detail").hide();
	$(".btn_view_more").click(function() {	
		$(".poopup_chat_detail").show();
	});
	$(".btn_close_detail").click(function() {
		$(".poopup_chat_detail").hide();
	});

	var swiper = new Swiper('.chat_text_slide', { /*20181015*/
		  slidesPerView: 'auto',
		  centeredSlides: false,
		  spaceBetween: 1,
		  pagination: {
			el: '.swiper-pagination',
			clickable: false,
		},
	});
	var swiper02 = new Swiper('.manual_slide', { /*20181015*/
		  pagination: {
			el: '.swiper-pagination',
			type: 'fraction',
		  },
		  navigation: {
			nextEl: '.btn_manual_next',
			prevEl: '.btn_manual_prev',
		  },
	 });
});


/* === 상담관리 === */
$(document).ready(function() {
	$(".tabs_icon li a").click(function() {	
		$(this).toggleClass("active");
		$(".tabs_icon li a").not(this).removeClass("active");
	});

	$(".list_filter li a").click(function() {	
		$(this).toggleClass("active");
		$(".list_filter li a").not(this).removeClass("active");
	});
	
	$(".btn_info_close").hide();
	$(".btn_info_more").click(function() {	
		$(".counsel_chat_info").css("height", "220px").addClass("active");
		$(this).hide();
		$(".btn_info_close").show();
	});
	$(".btn_info_close").click(function() {	
		$(".counsel_chat_info").css("height", "100px").removeClass("active"); /*20181004*/
		$(this).hide();
		$(".btn_info_more").show();
	});

	$(".btn_request_close").hide();
	$(".btn_request_more").click(function() {	
		$(".request_info").css("height", "300px").addClass("active");
		$(this).hide();
		$(".btn_request_close").show();
	});
	$(".btn_request_close").click(function() {	
		$(".request_info").css("height", "40px").removeClass("active"); /*20181002*/
		$(this).hide();
		$(".btn_request_more").show();
	});

	$(".state_customer").click(function() {	
		$(this).toggleClass("active");
	});

	$(".layer_alarm_setting").hide();
	$(".btn_alarm_set").click(function() {	
		$(".layer_alarm_setting").show();
	});
	$(".layer_alarm_setting").mouseleave(function() {	
		$(this).hide();
	});
	$(".layer_alarm_setting a").click(function() {	
		$(this).toggleClass("active");
		$(".layer_alarm_setting a").not(this).removeClass("active");
	});

	$(".tabs_menu_content").hide();
	$(".tabs_menu_content:first").show();
	$("ul.tabs_menu li").click(function () {
		$("ul.tabs_menu li").removeClass("active");
		$(this).addClass("active");
		$(".tabs_menu_content").hide()
		var activeTab = $(this).attr("rel");
		if($(this).attr('id') !== 'closeTab' && $(this).attr('id') !== 'wngTab') {
			$("#" + activeTab).fadeIn()
		}
	});
	$(".tabs_menu_content02").hide();
	$(".tabs_menu_content02:first").show();
	$("ul.tabs_menu02 li").click(function () {
		$("ul.tabs_menu02 li").removeClass("active");
		$(this).addClass("active");
		$(".tabs_menu_content02").hide()
		var activeTab = $(this).attr("rel");
		if($(this).attr('id') !== 'closeTab' && $(this).attr('id') !== 'wngTab') {
			$("#" + activeTab).fadeIn()
		}
	});
	$(".tabs_s_content").hide();
	$(".tabs_s_content:first").show();
	$("ul.tabs_menu_s li").click(function () {
		$("ul.tabs_menu_s li").removeClass("active");
		$(this).addClass("active");
		$(".tabs_s_content").hide()
		var activeTab = $(this).attr("rel");
		if($(this).attr('id') !== 'closeTab' && $(this).attr('id') !== 'wngTab') {
			$("#" + activeTab).fadeIn()
		}
	});
	$(".tabs_s_content02").hide();
	$(".tabs_s_content02:first").show();
	$("ul.tabs_menu_s02 li").click(function () {
		$("ul.tabs_menu_s02 li").removeClass("active");
		$(this).addClass("active");
		$(".tabs_s_content02").hide()
		var activeTab = $(this).attr("rel");
		if($(this).attr('id') !== 'closeTab' && $(this).attr('id') !== 'wngTab') {
			$("#" + activeTab).fadeIn()
		}
	});

	$(".tabs_s_content03").hide();
	$(".tabs_s_content03:first").show();
	$("ul.tabs_menu_s03 li").click(function () {
		$("ul.tabs_menu_s03 li").removeClass("active");
		$(this).addClass("active");
		$(".tabs_s_content03").hide()
		var activeTab = $(this).attr("rel");
		if($(this).attr('id') !== 'closeTab' && $(this).attr('id') !== 'wngTab') {
			$("#" + activeTab).fadeIn()
		}
	});
	$(".btn_edit").click(function () {  /* 20180917 */
		$(".tabs_s_content03").hide()
		var activeTab = $(this).attr("rel");
		if($(this).attr('id') !== 'closeTab' && $(this).attr('id') !== 'wngTab') {
			$("#" + activeTab).fadeIn()
		}
	});

	$(".popup").hide();
	$(".dev_btn_etc").click(function () {
		$(".popup_etc").show();
		$(".popup").not(".popup_etc").hide();
	});
	$(".btn_id").click(function () {
		$(".popup_id").show();
		$(".popup").not(".popup_id").hide();
	});
	$(".btn_meno").click(function () {
		$(".popup_memo").show();
		$(".popup").not(".popup_memo").hide();
	});
	$(".btn_change_request").click(function () {
		$(".popup_change_request").show();
		$(".popup").not(".popup_change_request").hide();
	});
	$(".btn_check_request").click(function () {
		$(".popup_check_request").show();
		$(".popup").not(".popup_check_request").hide();
	});
	$(".event_content").click(function () {
		$(".popup_event_setting").show();
	});
	$(".btn_plus_temp").click(function () {
		$(".popup_temp_category").show();
	});
	$(".btn_del_temp").click(function () {
		$(".popup_temp_del").show();
	});
	$(".tCont.black tr td").click(function () {
		$(".popup_black").show();
	});
	$(".btn_end_modify").click(function () {
		$(".popup_end_modify").show();
	});
	$(".btn_plus_manager").click(function () {
		$(".popup_manager").show();
	});
	$(".btn_plus_admin").click(function () {
		$(".popup_admin").show();
	});
	$(".btn_division_modify").click(function () {  /* 20180910 */
		$(".popup_division").show();  
	});
	$(".link_report_title").click(function () {  /* 20180912 */
		$(".popup_counseling").show();  
	});
	$(".dev_change_cnsr_btn").click(function () {  /* 20180914 */
		$(".popup_change_staff").show();  
	});
	$(".counseling_history tr td").click(function () {  /* 20180919 */
		$(".popup_counseling").show();
	});
	$(".btn_save_knowledge").click(function () {  /* 20180919 */
		$(".popup_knowledge_save").show();
	});
	$(".btn_del_knowledge").click(function () {  /* 20180919 */
		$(".popup_knowledge_delete").show();
	});
	$(".btn_member_modify").click(function () {  /* 20180927 */
		$(".popup_member").show();
	});

	$(".btn_popup_close").click(function () {
		$('body').css("overflow", "scroll");
		$(".popup").hide();
	});

	$(".etc_plus_select li button").click(function() {	
		$(this).toggleClass("active");
		$(".etc_plus_select li button").not(this).removeClass("active");
	});
	$(".flag_select a").click(function() {	
		$(this).toggleClass("active");
		$(".flag_select a").not(this).removeClass("active");
	});

	$(".template_list li.text").hide();
	$(".template_list li.tit a").click(function() {	
		$(this).toggleClass("active");
		$(this).parent().next(".template_list li.text").slideToggle();
	});

	$(".step_list.step01 li").click(function() {	
		$(this).toggleClass("active");
		$(".step_list.step01 li").not(this).removeClass("active");
	});
	$(".step_list.step02 li").click(function() {	
		$(this).toggleClass("active");
		$(".step_list.step02 li").not(this).removeClass("active");
	});
	$(".step_list.step03 li").click(function() {	
		$(this).toggleClass("active");
		$(".step_list.step03 li").not(this).removeClass("active");
	});
	
	$(".btn_end").click(function() {	
		$("#chat_bottom .bottom_top_area, #chat_bottom .textarea_area").hide();
		$(".chatting_area #chat_body").css("height", "calc(100% - 301px)");
		$("#chat_bottom").css("border-top", "none");
	});

});

/* === title style === */            
$(function () {
    $(document).tooltip();
});

/* === fileup === */
$(document).ready(function(){
	var fileTarget = $('.filebox .upload-hidden');
	fileTarget.on('change', function(){  
	if(window.FileReader){  // modern browser
		var filename = $(this)[0].files[0].name;
	}
	else {  // old IE
		var filename = $(this).val().split('/').pop().split('\\').pop(); 
	}   		
	$(this).siblings('.upload-name').val(filename);	
	
	});
}); 

/* === snb_view === */
$(document).ready(function(){
	$(".btn_view_snb").hide();
	$(".btn_close_snb").click(function() {
		$(this).hide();
		$(".btn_view_snb").show();
		$("#snb").hide();
		$(".left_content_area").css('width', '100%');
	});

	$(".btn_view_snb").click(function() {	
		$(this).hide();
		$(".btn_close_snb").show();
		$("#snb").show();
		$(".left_content_area").css('width', 'calc(100% - 233px)');
	});	
	
	$(function() {
		$( ".datepicker" ).datepicker();
		$('.monthpicker').MonthPicker({ Button: false });/* 20181002 */
	});
});



/* === height === */
$(window).load(function(){
	$(".left_content_area").css({"height":($(document).height()) - "128" +"px"});
	$("#snb").css({"height":($(document).height()) - "128" +"px"});
    $(window).resize(function(){
		$(".left_content_area").css({"height":($(document).height()) - "128" +"px"});
		$("#snb").css({"height":($(document).height()) - "128" +"px"});
    });
});

/* === 상담하기 추가 === */
$(window).resize(function(){ 
   if (window.matchMedia('(max-width: 1024px)').matches) {  
		$(".tab_icon_content.counseling").css("display","none");
		$(".tabs_icon li a").click(function() {
			$(".tab_icon_content.counseling").css("display","block");
		});
		$(".btn_close_tabcont").click(function() {
			$(".tab_icon_content.counseling").css("display","none");
			$(".tabs_icon li a").removeClass("active");
		});
   }
   else {
		$(".tab_icon_content.counseling").css("display","block");
   }
});

/* === popup 회원등록 목록추가 === */  /* 20180927 */
$(document).ready(function(){
	$(".search_member_area").hide();
	$(".btn_check_name").click(function() {
		$(".search_member_area").show();
		$(".popup_member .inner").css("height","700px");
	});
	$(".btn_popup_close").click(function() {
		$('body').css("overflow", "scroll");
		$(".search_member_area").hide();
		$(".popup_member .inner").css("height","512px");
	});
});

/* === 전체메뉴 보기 === */ /* 20181005 */
$(document).ready(function(){
	$(".btn_all_menu").click(function() {
		$("#snb").show();
	});
	$(".btn_all_close").click(function() {
		$("#snb").hide();
	});
});
$(window).resize(function(){ 
   if (window.matchMedia('(max-width: 1024px)').matches) { 
		$("#snb").hide();
		$(".btn_all_menu").click(function() {
			$("#snb").show();
		});
		$(".btn_all_close").click(function() {
			$("#snb").hide();
		});
   }
   else {
		$("#snb").show();
   }
});
/* === popup 채팅목록 삭제 === */  /* 20181005 */
$(document).ready(function(){
	$(".poopup_chat_delete").hide();
	$(".btn_go_trash").click(function() {
		$(".poopup_chat_delete").show();
	});
	$(".btn_close_del").click(function() {
		$(".poopup_chat_delete").hide();
	}); 
});


$(document).ready(function(){
	$(".popup_chat_btn").hide();

	$(".btn_chat_plus").click(function(){
		$(".popup_chat_btn").fadeIn("300");
	});
	$(".btn_close_btn").click(function(){
		$(".popup_chat_btn").hide();	
	});	

	var h_btnBtm = $("#chat_bottom").height();
	$(".chat_btn_list").css("bottom", h_btnBtm + 30 + "px");
});

