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
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/certification_confirm.css' />">
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

		$(document).on("click", 'input[name="btn_confirm"]', function() {

			if ($('input[name="user_name"]').val().trim() == "") {
				alert("이름을 입력해 주시기 바랍니다.");
				return;
			}

			if ($('input[name="jumin_num"]').val().trim() == "") {
				alert("주민등록번호를 입력해 주시기 바랍니다.");
				return;
			}

			$(".modal-loading-layer").show();

			$.ajax({
				url : "<c:url value='/hk_auth/identity_verification' />",
				data : {
					"uuid" : "${uuid}",
					"channel" : "${channel}",
					"user_name" : $('input[name="user_name"]').val().trim(),
					"jumin_num" : $('input[name="jumin_num"]').val().trim()
				},
				type : "post",
				success : function(result) {
					if (typeof(result.code) != "undefined" && result.code == '0000') {
						alert("인증이 완료 되었습니다.");
						location.href = "<c:url value='/hk_auth/certification_confirm' />";
					} else {
						alert("인증정보를 확인할수 없습니다.");
					}
					$(".modal-loading-layer").hide();
					
				},
				complete : function() {
					$(".modal-loading-layer").hide();
				}
			});
		});

		$(document).ready(function() {
			var is_session_data = "${user_name}";
			if (is_session_data == "") {
				// 세션정보가 없음
				alert("세션정보가 없습니다. 본인인증을 다시한번더 해주시기 바랍니다.");
				history.back();
			}

			//셀렉트 박스 onchange시 색상변경
			var selectTarget = jQuery('.selectbox');
		    selectTarget.change(function(){
		        jQuery(this).css("color","#000");
		    });

		});

	</script>

</head>

<body id="admin">
<div class="top">
		
		<img src="/happytalk/images/hk/logo.png" />
		<div class="step" >
			<ul  class="step2">
				<li class=""><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">1</span></li>
				<li class="step_active"><img src="/happytalk/images/hk/step1.png" /><span class="step_num">2</span><span> 휴대폰인증</span></li>
				<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">3</span></li>
			</ul>
		</div>
	</div>


	<form name='contractFrm'>
	<div class="certification contents">
	
		<div class="modal-loading-layer">
	        <div class="modal-content">
	            <div class="layer-title"><span>요청하신 작업을 처리중입니다.</span></div>
				<div class="layer-contents" style="text-align:center;">
				<p>요청하신 작업을 처리중입니다. 작업이 완료될때 까지 잠시만 기다려 주시기 바랍니다.</p>
	            <img src="/happytalk/images/admin/loading_bar.gif" align="center" />
	        	</div>
	        </div>
	    </div>

		<div >
			<div >
				<div class="title-btn">
					<div >
							<a href="javascript:void(0);">휴대폰인증</a>
					</div>
				</div>

				<div>
					<div class="description step1 notap" >
							<p>고객님 명의로 발급된 휴대폰 정보를 입력해 주시기 바랍니다.</p>
					</div>
				</div>



				<div class="step2">
					<div class="step2-info">
						<div>이름</div>
						<input type="text" name="memberNm" id="memberNm" value="${user_name}" placeholder="이름을 입력해주세요" readonly="true" />
					</div>
					<div class="step2-info">
						<div>생년월일</div>
						<input type="number" name="birthDt" id="birthDt" value="" placeholder="예) 19850302" />
						<!--${birthYearPrefix}${jumin_num.substring(0,6)}  -->
					</div>



					<div class="step2-info">
						<div>내/외국인</div>
						<div class="btn_group"> 
							<label class="labl">
							    <input type="radio" name="local" id="local" value="1" checked="checked"/>
							    <div>내국인</div>
							</label>
							<label class="labl">
							    <input type="radio" name="local" id="foreigner" value="2" />
							    <div>외국인</div>
							</label>
						</div>


					</div>
					<div class="step2-info">
						<div>성별</div>
						<div class="btn_group"> 
							<label class="labl">
							    <input type="radio" id="male" name="gender" value="1" checked="checked"/>
							    <div>남자</div>
							</label>
							<label class="labl">
							    <input type="radio" id="female" name="gender" value="2" />
							    <div>여자</div>
							</label>
						</div>
					</div>
					<div class="step2-info">
						<div>약관동의</div>
					</div>
					<div class="step1" >
						<div class="check-list">
							<div class="check-info">
								<label class="checkbox-wrap check-all" ><input type="checkbox"  name="checkAll" id="checkAll" value=""><i class="check-icon" ></i><span class="check-title">모두 확인 및 동의합니다</span> </label>
							</div>
							<!--div class=""><input type="checkbox" name="" id="contract_chk" /><span>가상계좌 발급 가능 계약 목록</span></div-->
								<div class="check-info">
									<label class="checkbox-wrap"><input type="checkbox" name="check1" id="check1" value="T"><i class="check-icon"></i><span class="check-title">개인정보 이용 및 제공 동의</span> <a href="//m.heungkukfire.co.kr/cm/LCM/agree6.do" target="_blank"><i class="right-button"></i></a></label>

								</div>
								<div class="check-info">
									<label class="checkbox-wrap"><input type="checkbox" name="check2" id="check2" value="T"><i class="check-icon"></i><span class="check-title">통신사별 이용약관 동의</span> <a href="//m.heungkukfire.co.kr/cm/LCM/agree7.do" target="_blank"><i class="right-button"></i></a></label>
								</div>
								<div class="check-info">
									<label class="checkbox-wrap"><input type="checkbox" name="check3" id="check3" value="T"><i class="check-icon"></i><span class="check-title">본인확인서비스 이용약관 동의</span> <a href="//m.heungkukfire.co.kr/cm/LCM/agree8.do" target="_blank"><i class="right-button"></i></a></label>
								</div>
								<div class="check-info">
									<label class="checkbox-wrap"><input type="checkbox" name="check4" id="check4" value="T"><i class="check-icon"></i><span class="check-title">고유식별정보 처리 동의</span> <a href="//m.heungkukfire.co.kr/cm/LCM/agree9.do" target="_blank"><i class="right-button"></i></a></label>
								</div>
							</div>
					</div>

					<div class="step2-info input-phnum">
						<div>휴대전화번호</div>
						<select title="통신사 선택" id="telecom" name="telecom" class="selectbox" style="width:100%;">	<!-- class="hide_txt" -->
							<option value="1" selected="selected">SKT</option>
							<option value="2">KT</option>
							<option value="3">LGU+</option>
							<option value="5">알뜰폰(SKT)</option>
							<option value="6">알뜰폰(KT)</option>
							<option value="7">알뜰폰(LG)</option>
						</select>
					</div>
					<div class="step2-info input-phnum">
						<select title="휴대폰번호 앞자리 선택"  id="smsMobileNo1" name="smsMobileNo1" class="first-num selectbox" style="width:30%">	<!-- class="hide_txt" -->
							<option value="010" selected="selected">010</option>
							<option value="011">011</option>
							<option value="017">017</option>
							<option value="018">018</option>
							<option value="019">019</option>
						</select>
						
						<input type="text" class="last-num" name="smsMobileNo2" id="smsMobileNo2" value=""   placeholder='"-"없이 입력하세요' />
					</div>
					<div class="input-phnum-btn" onclick="hkAuthSms('reqNum');">
						<div>
							<div class="btn-txt">인증번호 요청</div>
						</div>
					</div>

					<div class="step2-info certification-num">
						<div>인증번호</div>
						<input type="text"  id="ctifNo" name="ctifNo"   placeholder="인증번호 6자리 입력하세요"/>
						<!-- <span class="count-time ">03:00</span> -->
					</div>
					
					<div class="step2-info">
						<div class="check-info" style="border-bottom:0px;">
							<label class="checkbox-wrap"><input type="checkbox" name="is_auth" id="is_auth" value="T"><i class="check-icon"></i><span class="check-title">공인인증으로 강제지정</span></label>
						</div>
					</div>

					<div class="description certification-notice">
						<p class="point_color sms-txt">- 인증번호 입력 후 휴대폰 인증 버튼을 눌러주세요.</p>
						<p>- 인증번호는 SMS로 발송됩니다.</p>
						<p>- 인증번호 수신 후 3분 이내에 입력해 주세요</p>
						<p>- 인증번호를 수신 받지 못하신 경우 ‘재발송’ 버튼을 클릭해주시기 바랍니다.</p>
					</div>
					<div class=" btn" onclick='hkAuthSmsConfirm();'>
						<div>
								<a href="javascript:void(0);">확인</a>
						</div>
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
	
	
	<script type="text/javascript">
	
		var tid = "";
		var SetTime = 180;
		var authOk = false;
			
		$(document).ready(function(){
	
			$('#checkAll').on('click', function(){//약관 동의
				
				if( !$('#checkAll').is(':checked')){	
					$('#check1, #check2, #check3, #check4' ).prop('checked', false);
				}else{
					$('#check1, #check2, #check3, #check4' ).prop('checked', true);
				}
			});
			
			//하위 항목 체크 해제시 '전체' 체크 해제
			$('#check1, #check2, #check3, #check4' ).on('click', function(){
				if( $('#check1').is(':checked') && $('#check2').is(':checked') && $('#check3').is(':checked') && $('#check4').is(':checked') ) $('#checkAll' ).prop('checked', true);
				if( !$('#check1').is(':checked') || !$('#check2').is(':checked') || !$('#check3').is(':checked') || !$('#check4').is(':checked') ) $('#checkAll' ).prop('checked', false);
			});
			
	
		});
		
		function hkAuthSms() { // cmd값에 따라 인증번호요청 및 인증시도로 분기한다.

			try {
				if (($("input[name='check1']:checked").val()) != "T")
					throw Error("개인정보 이용 및 제공 동의 하여주시기 바랍니다.");
				if (($("input[name='check2']:checked").val()) != "T")
					throw Error("통신사별 이용약관 동의 하여주시기 바랍니다.");
				if (($("input[name='check3']:checked").val()) != "T")
					throw Error("본인확인서비스 이용약관 동의 하여주시기 바랍니다.");
				if (($("input[name='check4']:checked").val()) != "T")
					throw Error("고유식별정보 처리 동의 하여주시기 바랍니다.");
				if ($.trim($("#smsMobileNo2").val()) == '')
					throw Error("전화번호를 확인하여 주시기 바랍니다.");
				if ($.trim($("#birthDt").val()) == '')
					throw Error("생년월일을 입력하여 주시기 바랍니다.");
			} catch (e) {
				alert(e.message);
				return;
			}

			var dataReqNum = {
				"memberNm" : $.trim($('#memberNm').val()), // 이름
				"cpno" : $('#smsMobileNo1').val() + $('#smsMobileNo2').val(), // 수신휴대전화통신사번호 예) 01027501814  					
				"mvTscoClcd" : $('#telecom').val(), // 1:SK, 2:KT, 3:LG, 5:SKT알뜰폰, 6:KT알뜰폰, 7:LG알뜰폰			
				"clbkNo" : "1600-1522", // 콜백 번호					
				"birthDt" : $('#birthDt').val(), // 생년 월일					
				"frgnClcd" : $('input:radio[name=local]:checked').val(), // 내외국인 구분
				"sexClcd" : $('input:radio[name=gender]:checked').val(), // 성별
			};

			$('#ctifNo').val('');

			$(".modal-loading-layer").show();
			$.ajax({
				url : "<c:url value='/hk_auth/auth_num_request' />",
				data : dataReqNum,
				type : "post",
				success : function(result) {
					if (typeof (result.code) != "undefined"
							&& result.code == '0000') {
						alert("인증문자가 발송 되었습니다.");
						$(".count-time").addClass('active');
						$(".sms-txt").removeClass('sms-txt');
						$(".certification-num")
								.removeClass('certification-num').addClass(
										'certification-num-active')
						$(".btn-txt").text("재발송");
					} else {
						alert(result.message);
					}
					$(".modal-loading-layer").hide();
				},
				complete : function() {
					$(".modal-loading-layer").hide();
				}
			});
		}

		function hkAuthSmsConfirm() { // cmd값에 따라 인증번호요청 및 인증시도로 분기한다.

			var is_auth = (typeof($('input[name="is_auth"]:checked').val()) != "undefined") ? $('input[name="is_auth"]:checked').val() : '';
			
			var dataReqAuth = {
				"smsCtifNo" : $('#ctifNo').val(), // 휴대폰으로 받은 숫자6자리				
				"lmsSndnSno" : $('#lmsSndnSno').val(),
				"is_auth" : is_auth // 공인인증으로 처리
			// LMS발송일련번호
			};
			$(".modal-loading-layer").show();
			$.ajax({
				url : "<c:url value='/hk_auth/auth_num_confirm' />",
				data : dataReqAuth,
				type : "post",
				success : function(result) {
					if (typeof (result.code) != "undefined"
							&& result.code == '0000') {
						alert("인증되었습니다.");
						location.href = "/happytalk/hk_auth/certification_complete";
					} else {
						alert(result.message);
					}
					$(".modal-loading-layer").hide();
				},
				complete : function() {
					$(".modal-loading-layer").hide();
				}
			});
		}

		function successCallback(result) {
			$("#dimm_progress").hide();

			if (result.cmd == "respNum") { //인증번호 요청 후

				//진행중인 타이머 시작 전 정지(재요청의 경우)
				if (tid != '')
					clearInterval(tid);

				tid = setInterval('msg_time()', 1000);

				if (result.rtnState == "false") {
					clearInterval(tid);
					SetTime = 180;

					alert(result.msg);
					formReadonly(false);

				} else if (result.rtnState == "true") {

					// 0000 : 성공 
					if (result.cpnoCtifRcd == "0000") {

						$("#lmsSndnSno").val(result.lmsSndnSno);//일련번호를 받아온다.

						//alert("인증 성공 및 SMS 발송 완료.\n수신받으신 인증번호를 3분 이내에 입력하여 주세요.");
						$(".btn_gray").focus();
						$("#liMsg1").show();
						$("#ctifNo").focus();
						$("#btnSnd").hide();
						$("#btnReSnd").show();
						formReadonly(true);
					} else {
						if (result.cpnoCtifRcd == "0001") {
							alert("인증 불일치(통신사선택오류, 생년월일, 성명, 휴대폰번호 불일치, 휴대폰일시정지, 법인폰/선불폰가입자, SMS발송실패 등) ");
						} else if (result.cpnoCtifRcd == "0002") {
							alert("LGU+ 고객이 아닙니다.");
						} else if (result.cpnoCtifRcd == "0040") {
							alert("본인확인문자 차단고객입니다.");
						} else if (result.cpnoCtifRcd == "0041") {
							alert("인증문자 발송 차단고객입니다.");
						} else if (result.cpnoCtifRcd != "0000") {
							//alert("result.msg :" + result.msg + "// nresult.lmsSndnSno :" + result.cpnoCtifRcd );
						}

						//DB에 주민번호가 없는 경우(1회 인증 후 팝업을 새로고침한 경우) 창을 닫고 부모창을 새로고침 해야한다.
						/* if(result.resultCode == "50006") {
							alert(result.msg);
							window.opener.location.reload();
							self.close();
						}else{
							alert("인증번호 요청시 오류가 발생하였습니다. " + result.msg );
						} */
						clearInterval(tid);
						SetTime = 180;
						formReadonly(false);
					}
				}

			} else if (result.cmd == "respAuth") { //최종 인증 요청 후

				loadingOn();
				formReadonly(false);

				if (result.rtnState != "true") {

					if (result.ctifCfrmRcd == "0001") {
						alert("인증번호 불일치입니다.");
					} else if (result.ctifCfrmRcd == "0031") {
						alert("해당 인증 요청 확인불가입니다.");
					} else if (result.ctifCfrmRcd == "0033") {
						alert("인증고유번호 불일치입니다.");
					} else if (result.ctifCfrmRcd == "0034") {
						alert("기 인증 완료 건입니다.");
					} else {
						alert(result.msg);
					}

				} else if (result.ctifCfrmRcd == "0000"
						&& result.rtnState == "true") {

					alert("인증이 완료 되었습니다.");
					window.opener.form.birthDt.value = $('#birthDt').val();
					window.opener.mobileAuthSuccess("Y");
					self.close();
				}

			}
		}
	</script>
</body>
</html>