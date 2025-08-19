<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<!--[if lt IE 9]>
	<script type="text/javascript" src="../js/html5shiv.js"></script>
	<script type="text/javascript" src="../js/html5shiv.printshiv.js"></script>
	<![endif]-->
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/certification.css' />">
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/loading.css' />">
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>

	<script type="text/javascript">
		// up button 클릭
		$(document).on("click", 'input[name="btn_close"]', function() {
			_close()
		});

		function _close()
		{
			// 카카오톡 인앱 창닫기 버튼
			var _ua = window.navigator.userAgent || window.navigator.vendor || window.opera;
			if (_ua.toLocaleLowerCase().indexOf("kakaotalk") > -1) {
				// 카카오 인앱 브라우저에서 종료시
				try { window.close(); } catch (e) {}
				try { self.close(); } catch (e) {}
				try {
					window.location.href = (/iPad|iPhone|iPod/.test(_ua)) ? "kakaoweb://closeBrowser" : "kakaotalk://inappbrowser/close";
				} catch (e) {}
			} else {
				// 카카오 이외의 브라우저에서 종료시
				try { window.close(); } catch (e) {}
				try { self.close(); } catch (e) {}
				alert("자동으로 닫히지 않을 경우 해당 페이지를 수동으로 닫아주세요.");
			}
		}

		$(document).on( "click", 'input[name="all_check"]', function() {
			$('input[name="checkItem[]"]').prop("checked", $(this).prop('checked'));
			
		});

		$(document).on("click", '.btn_confirm', function() {

			if ($('input[name="checkItem[]"]:checked').size() != 3) {
				alert("개인(신용)정보 수집 · 이용 제공 동의를 체크해 주시기 바랍니다.");
				return;
			}
			
			if ($('input[name="user_name"]').val().trim() == "") {
				alert("이름을 입력해 주시기 바랍니다.");
				return;
			}

			if ($('input[name="jumin_num"]').val().trim() == "") {
				alert("주민등록번호를 입력해 주시기 바랍니다.");
				return;
			}

			$('.btn_confirm').hide();
			$(".modal-loading-layer").show();

			var is_auth = (typeof($('input[name="is_auth"]:checked').val()) != "undefined") ? $('input[name="is_auth"]:checked').val() : '';
			
			$.ajax({
				url : "<c:url value='/hk_auth/identity_verification' />",
				data : {
					"uuid" : "${uuid}",
					"channel" : "${channel}",
					"user_name" : $('input[name="user_name"]').val().trim(),
					"jumin_num" : $('input[name="jumin_num"]').val().trim(),
					"is_auth" : is_auth
				},
				type : "post",
				success : function(result) {
					if (typeof(result.code) != "undefined" && result.code == '0000') {
						alert("인증이 완료 되었습니다.");
						_close();
					} else {
						alert("인증정보를 확인할수 없습니다.");
					}
					$('.btn_confirm').show();
					$(".modal-loading-layer").hide();
				},
				complete : function() {
					$('.btn_confirm').show();
					$(".modal-loading-layer").hide();
				}
			});
		});

		$(document).ready(function() {
			$(".popup1").click(function () {
				$('.modal-background1').css('display', 'block');
			});
			$(".popup2").click(function () {
				$('.modal-background2').css('display', 'block');
			});
			$(".popup-close").click(function () {
				$('.modal-background1').css('display', 'none');
				$('.modal-background2').css('display', 'none');
			});
			/*
			document.querySelectorAll(".popup1").forEach(element => element.addEventListener("click", () => {
	            document.querySelector(".modal-background1").style.display = "block";
	        }));
	        document.querySelectorAll(".popup-close").forEach(element => element.addEventListener("click", () => {
	            document.querySelector(".modal-background1").style.display = "none";
	        }));

	        document.querySelectorAll(".popup2").forEach(element => element.addEventListener("click", () => {
	            document.querySelector(".modal-background2").style.display = "block";
	        }));
	        document.querySelectorAll(".popup-close").forEach(element => element.addEventListener("click", () => {
	            document.querySelector(".modal-background2").style.display = "none";
	        }));
	        */
		});
	</script>

</head>

<body id="admin">

<div class="top">
		
		<img src="/happytalk/images/hk/logo.png" />
		<div class="step" >
			<ul class="step1">
				<li class="step_active"><img src="/happytalk/images/hk/step1.png" /><span class="step_num">1</span><span> 약관동의</span></li>
				<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">2</span></li>
				<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">3</span></li>
			</ul>
		</div>
	</div>
	
	<form name="certification_form">


	<div class="certification contents">

		<div >
			<div >
				<div class="title-btn">
					<div >
							<span style="color:white">본인인증 및 계약확인</span>
					</div>
				</div>

				<div>
					<div class="description step1" >
							<p>- 챗봇 · 채팅 서비스 이용을 위한 본인인증 페이지 입니다.</p>
							<p>- 고객정보 확인을 위해 이름과 주민등록번호를 입력해 주세요</p>

					</div>
				</div>

				<div class="step1" >
					<div class="step1-info">
						<div>개인(신용)정보 수집 · 이용 제공 동의</div>
					</div>

					<div class="check-list">
						<div class="check-info">
							<label class="checkbox-wrap check-all" ><input type="checkbox"  name="all_check" value="T" tabindex="1"><i class="check-icon" ></i><span class="check-title">모두 확인 및 동의합니다</span> </label>
						</div>
						<!--div class=""><input type="checkbox" name="" id="contract_chk" /><span>가상계좌 발급 가능 계약 목록</span></div-->
						<div class="check-info">
							<label class="checkbox-wrap"><input type="checkbox" name="checkItem[]" value="T"><i class="check-icon"></i><span class="check-title">전자금융거래 이용약관</span> <span class="point_color">(필수)</span></label>
							<a href="/happytalk/hk_auth/certification_desc" target="_blank"><i class="right-button"></i></a>
							
						</div>
						<div class="check-info">
							<label class="checkbox-wrap"><input type="checkbox" name="checkItem[]" value="T"><i class="check-icon"></i><span class="check-title">개인(신용)정보의 수집 · 이용에 관한 사항</span> <span class="point_color">(필수)</span></label>
							<i class="right-button popup1"></i>
						</div>


						<div class="check-info">
							<label class="checkbox-wrap"><input type="checkbox" name="checkItem[]" value="T"><i class="check-icon"></i><span class="check-title">고유식별정보 처리에 관한 사항</span> <span class="point_color">(필수)</span></label>
							<i class="right-button popup2"></i>
						</div>
					</div>
				</div>
				<!--레이어 팝업1-->
			    <div class="modal-background1">
			        <div class="modal-content">
			           
			            <div class="layer-title"><span>개인(신용)정보의 수집 · 이용에 관한 사항(필수사항)</span></div>
						<div class="layer-contents">
			            <p>삼성증권은 챗봇 · 채팅 상담에서 『보험계약조회 및 변경, 보험계약대출 가능금액 조회 등의 개인화 서비스』 제공을 위해 『개인정보보호법』 등에 따라 귀하의 개인정보를 다음과 같이 수집 · 이용하고자 합니다.
<br>귀하는 본 동의를 거부할 수 있으며 거부할 경우 서비스 이용이 제한 됩니다.</p>
			            <h3>개인(신용)정보의 수집 · 이용 목적</h3>
			            <p>- 챗봇 · 채팅 서비스 이용 시 본인여부 확인, 보험계약 조회, 보험계약대출 가능금액 조회
</p>			            <h3>수집 · 이용할 개인(신용)정보 내용</h3>
			            <p>- 성명, 본인인증기관의 인증 CI값,
 <span>주민등록번호, 외국인등록번호</span></p>
			            <h3>개인(신용)정보의 보유 및 이용 기간</h3>
			            <p>- <span>챗봇 · 채팅 서비스 목적 달성 시 즉시 폐기</span></p>
			            <div class="popup-close">확인</div>
			        	</div>
			            
			        </div>
			    </div>
			    <!--레이어 팝업2-->
				<div class="modal-background2">
			        <div class="modal-content">
			           
			            <div class="layer-title"><span>고유식별정보 처리에 관한 사항(필수사항)</span></div>
						<div class="layer-contents">
			            <p>귀하는 본 동의를 거부할 수 있으며 거부할 경우 서비스 이용이 제한됩니다.</p>
			            <h3>고유식별정보의 수집 · 이용 목적</h3>
			            <p>- 보험계약 조회, 보험계약대출 가능금액 조회</p>
			            <h3>수집 · 이용할 고유식별정보 내용</h3>
			            <p>- <span>주민등록번호, 외국인등록번호</span></p>
			            <h3>고유식별정보의 보유 및 이용 기간</h3>
			            <p>- <span>챗봇 · 채팅 서비스 목적 달성 시 즉시 폐기</span></p>
	</p></p></p>		            <p>당사는 『개인정보보호법』 제24조에 따라 <span>상기의 개인(신용)정보에 대한 개별동의사항</span>에 대하여 다음과 같이 귀하의 <span>고유식별정보(주민등록번호, 외국인등록번호)</span>를 처리(수집, 이용, 제공 등) 하고자 합니다.</p>
			            <div class="popup-close">확인</div>
			        	</div>
			            
			        </div>
			    </div>
			    
			    <div class="modal-loading-layer">
			        <div class="modal-content">
			            <div class="layer-title"><span>요청하신 작업을 처리중입니다.</span></div>
						<div class="layer-contents" style="text-align:center;">
						<p>요청하신 작업을 처리중입니다. 작업이 완료될때 까지 잠시만 기다려 주시기 바랍니다.</p>
			            <img src="/happytalk/images/admin/loading_bar.gif" align="center" />
			        	</div>
			        </div>
			    </div>

				<div class="step2-info">
					<div>이름</div>
					<input type="text" name="user_name" id="user_name" value=""  maxlength="19"  placeholder="이름을 입력하세요" tabindex="2"/>
				</div>
				
				<div class="step2-info">
					<div>주민등록번호</div>
					<input type="number" name="jumin_num" id="jumin_num" value="" maxlength="13" placeholder='주민등록번호 13자리를 "-"없이 입력하세요.'  tabindex="3"/>
					
					<div class="check-info" style="border-bottom:0px;">
						<label class="checkbox-wrap"><input type="checkbox" name="is_auth" id="is_auth" value="T"><i class="check-icon"></i><span class="check-title">체크시 공인인증 처리</span></label>
					</div>
					
				</div>
				
				<div class="btn btn_confirm">
					<div>
						<a name="btn_confirm" href="#">실명 확인</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	</form>
	
	<div class="footer">
		<div >
			<img src="/happytalk/images/hk/bottom.jpg" width="100%">
		</div>
	</div>
	
</body>
</html>