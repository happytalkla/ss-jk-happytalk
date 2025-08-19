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
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/insurance_debit_change.css' />">
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/loading.css' />">
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>

	<script type="text/javascript">

		$(document).on("click", '.btn_close', function() {
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


		function isKakaoAppCheck()
		{
			var _ua = window.navigator.userAgent || window.navigator.vendor || window.opera;

			if (_ua.toLocaleLowerCase().indexOf("kakaotalk") >= 0) {
				return (/iPad|iPhone|iPod/.test(_ua)) ? "apple" : "android";
			}

			return '';
		}

		$(document).on("click", '.btn_next', function() {
			if ($('input[name="stock_no[]"]:checked').size() == 0) {
				alert("변경하실 계약건을 선택하여 주시기 바랍니다.");
				return false;
			}

			$('.account').show();
			$('.transferDate').show();

			for (var i = 0; i < $('input[name="stock_no[]"]:checked').size(); i++) {
				if ($('input[name="stock_no[]"]:checked').eq(i).attr('data-is-no-account-no') == 'Y') {
					$('.account').hide();
					$('.transferDate').hide();
				}
			}

			$(".step_select").hide();
			$(".step_change").show();
			$(".step_complete").hide();

			if (!confirm(($('input[name="stock_no[]"]:checked').size() + "건의 계약을 선택하셨습니다. 자동이체 변경을 진행하시겠습니까?"))) {
				$(".step_select").show();
				$(".step_change").hide();
				$(".step_complete").hide();
			}
		});

		$(document).on("click", '.btn_before', function() {
			$(".step_select").show();
			$(".step_change").hide();
			$(".step_complete").hide();
		});

		var intervalPlay = null;

		$(document).on("click", '.btn_action', function() {

			if ($('input[name="is_allow"]:checked').val() != 'Y') {
				alert("[동의함] 으로 선택하셔야 자동이체 변경처리 진행이 가능합니다.");
				return false;
			}

			if ($('input[name="change_type"]:checked').val() == 'all') {

				if (!validate_account_number()) {
					return false;
				}

				if (!validate_transfer_date()) {
					return false;
				}
				
			} else if ($('input[name="change_type"]:checked').val() == 'account_number') {
				// 계좌번호 변경일 경우엔 이체일을 확인한다.
				var transDate = '';
				for (var i = 0; i < $('input[name="stock_no[]"]:checked').size(); i++) {
					if (i == 0) {
						transDate = $('input[name="stock_no[]"]:checked').eq(i).attr("data-trans-date");
					} else {
						if (transDate != $('input[name="stock_no[]"]:checked').eq(i).attr("data-trans-date"))	{
							alert("이체일이 다른 계약건이 존재합니다. 계좌번호 변경의 경우 동일 이체일인 계약건들만 선택하여 주시기 바랍니다.");
							return false;
						}
					}
				}
				// 선택된 가장 첫번째 값을 SELECT 의 값으로 지정
				$('select[name="transfer_date"]').val(transDate);

				if (!validate_account_number()) {
					return false;
				}
			} else if ($('input[name="change_type"]:checked').val() == 'transfer_date') {
				if (!validate_transfer_date()) {
					return false;
				}
			}

			if ($('select[name="is_integrated_transfer"] option:selected').val() == "") {
				alert("통합이체 여부를 선택하여 주시기 바랍니다.");
				return false;
			}

			if ($('input[name="is_allow"]:checked').val() != 'Y') {
				alert("[동의함] 으로 선택하셔야 자동이체 변경처리 진행이 가능합니다.");
				return false;
			}

			if ($('input[name="change_type"]:checked').val() != 'transfer_date') {
				// 이체일 변경이 아닌 경우
				if (intervalPlay != null) clearInterval(intervalPlay);
				
				$.ajax({
					url : "<c:url value='/hk_auth/callback_check' />",
					data :{},
					type : "post",
					success : function(result) {
						$(".modal-loading-layer").css('display','none');
						
						if (typeof(result.code) != "undefined" && result.code == '0000') {
							setAction();
						} else {
							// 공인인증 페이지 팝업 루틴
							var authUrlString = "${heungkuk_domain}/CBM/mobileAuthSignProc.do?is_validate=T&type=A&uuid=${uuid}&channel=${channel}&position=C&userid=${userid}";

							$("#authFrame").attr('src', authUrlString);
							$(".frameLayer").show();
							$(".mainLayer").hide();
							$('body').css('margin', '0px');

							intervalPlay = setInterval(setPoolingAction, 2000);
						}
					},
					complete : function() {
					}
				});
			} else {
				// 이체일만 변경시엔 공인인증을 하지 않습니다.
				setAction();
			}
		});

		function setPoolingAction()
		{
			$.ajax({
				url : "<c:url value='/hk_auth/callback_check' />",
				data :{},
				type : "post",
				success : function(result) {
					if (typeof(result.code) != "undefined" && result.code == '0000') {
						if (intervalPlay != null) clearInterval(intervalPlay);
						setAction();
					}
				},
				complete : function() {
				}
			});
		}

		function setAction()
		{
			var stock_number = [];
			
			for (i = 0; i < $('input[name="stock_no[]"]:checked').size(); i++) {
				stock_number.push($('input[name="stock_no[]"]:checked').eq(i).val());
			}

			$("form[name='insurance_debit_change_form']").serialize();

			$(".modal-loading-layer").css('display','inline-block');
			$('.btn_action').hide();
			// 변경 처리 업무
			$.ajax({
				url : "<c:url value='/hk/insurance_debit_change_action' />",
				data :$("form[name='insurance_debit_change_form']").serialize(),
				type : "post",
				success : function(result) {
					if (typeof(result.code) != "undefined" && result.code == '0000') {
						location.replace("/happytalk/hk/insurance_debit_change_complete");
					} else {
						alert(result.message);
					}
					$('.btn_action').show();
					$(".modal-loading-layer").css('display','none');
				},
				complete : function() {
					$('.btn_action').show();
					$(".modal-loading-layer").css('display','none');
				}
			});
		}

		$(document).on("click", '.account_number_validate', function() {
			// 예금주 확인 후 성공일 경우에만 확인
			$('input[name="is_validate_bank"]').val("0");
			$('input[name="account_name"]').val("");
			$('.account_name').html("");

			if ($('select[name="bank"] option:selected').val() == "") {
				alert("은행명을 선택하여 주시기 바랍니다.");
				return false;
			}

			if ($('input[name="account_number"]').val() == "") {
				alert("계좌번호를 입력하여 주시기 바랍니다.");
				return false;
			}

			$('.account_number_validate').hide();
			$(".modal-loading-layer").css('display','inline-block');

			$.ajax({
				url : "<c:url value='/hk/is_bank_validate' />",
				data : {
					"account_number" : $('input[name="account_number"]').val(),
					"bank" : $('select[name="bank"] option:selected').val()
				},
				type : "post",
				success : function(result) {
					if (typeof(result.code) != "undefined" && result.code == '0000') {
						$('input[name="account_name"]').val(result.user_name);
						$('.account_name').html(result.user_name);

						$('input[name="is_validate_bank"]').val("1");
						$('input[name="account_number"]').attr("readonly", true);
					} else {
						$('input[name="is_validate_bank"]').val("");
						$('input[name="account_number"]').attr("readonly", false);

						if (typeof(result.code) != "undefined") {
							if (result.code == "0002") {
								alert("계약자명과 예금주명이 동일한 경우에만 변경처리가 가능합니다.");
							} else if (result.code == "8888") {
								alert("본인인증 유효시간이 초과되었습니다.\n본인인증 후 10분 이내 조회가 가능합니다.\n\n예금주 확인을 진행하시려면 본인인증을 재진행해 주세요~ *^.^*");
							} else {
								alert("죄송합니다. 요청 내용이 정상적으로 완료되지 못하였습니다.\n잠시후 다시 시도하시거나, 콜센터(1688-1688)로 문의하여 주시기 바랍니다.\n※콜센터 운영시간 : 평일 오전 9시~오후6시");
							}
						} else {
							alert("죄송합니다. 요청 내용이 정상적으로 완료되지 못하였습니다.\n잠시후 다시 시도하시거나, 콜센터(1688-1688)로 문의하여 주시기 바랍니다.\n※콜센터 운영시간 : 평일 오전 9시~오후6시");
						}
					}
					$('.account_number_validate').show();
					$(".modal-loading-layer").css('display','none');
				},
				complete : function() {
					$('.account_number_validate').show();
					$(".modal-loading-layer").css('display','none');
				}
			});
		});

		function reset_validate_bank()
		{			
			$("select[name='bank']").change(function () {
				// 읽기 전용을 해제
				$('input[name="account_number"]').attr("readonly", false);
				// 인증완료 영역 삭제
				$('input[name="is_validate_bank"]').val("");
			});
		}

		function validate_account_number()
		{
			try {
				if ($('select[name="bank"] option:selected').val() == "") throw new Error("은행명을 선택하여 주시기 바랍니다.");
				if ($('input[name="account_number"]').val() == "") throw new Error("계좌번호를 입력하여 주시기 바랍니다.");
				if ($('input[name="is_validate_bank"]').val() == "") throw new Error("예금주 확인을 진행하여 주시기 바랍니다.");
			} catch (e) {
				alert(e.message);
				return false;
			}

			return true;
		}

		function validate_transfer_date()
		{
			try {
				if ($('select[name="transfer_date"] option:selected').val() == "") throw new Error("이체일자를 선택하여 주시기 바랍니다.");
			} catch (e) {
				alert(e.message);
				return false;
			}

			return true;
		}

		$(document).on("click", 'input[name="all_check"]', function() {
			$('input[name="stock_no[]"]').prop("checked", $(this).prop('checked'));
		});

		$(document).on("change", 'input[name="change_type"]', function() {
			if ($(this).val() == 'all') {
				$(".field").show();
			} else {
				$(".field").hide();
				$("." + $(this).val() + "_field").show();
			}
		});

		$(document).on("click", ".closeIframeBtn", function() {
			// 체크 이벤트 해제
			if (intervalPlay != null) clearInterval(intervalPlay);
			$(".frameLayer").hide();
			$(".mainLayer").show();
			$('body').css('margin', '16px');
		});

		$(document).ready(function() {
			$(".step_change").hide();
			$(".step_complete").hide();
			$('.modal-background1').show();

			reset_validate_bank();

			<c:choose>
			<c:when test="${empty lists }">
			<c:if test="${errorCode eq '0006'}">
				alert("고객님 반갑습니다.\n"
						+ "현재 고객님께서 당사에 유지 중인 장기보험 계약이 존재하지 않습니다. (실효 상태 계약 포함)\n"
						+ "★ [추천] 신규가입 상담을 원하시면 \"상담직원으로 전환하기\" 를 통해 채팅상담사와 자세한 대화를 나눠 보시길 추천 드립니다.");
			</c:if>
			<c:if test="${errorCode eq '0007'}">
				alert("고객님 반갑습니다."+ "\n" + 
						"현재 고객님께서는 납입방법을 자동이체로 변경 가능한 장기보험 계약이 존재하지 않습니다.\n\n" + 
						"참고로, 실효 상태 또는 납입주기가 일시납인 계약은 납입방법을 변경할 수가 없습니다.");
			</c:if>
			_close();
			</c:when>
			</c:choose>

			$(".frameLayer").hide();
			$('#authFrame').css('height', $(window).height() - 60);
		});

	</script>

</head>

<body id="admin">
<center>
<div style="max-width:700px;text-align:center;">

	<div class="modal-loading-layer">
       <div class="modal-content">
           <div class="layer-title"><span>요청하신 작업을 처리중입니다.</span></div>
		<div class="layer-contents" style="text-align:center;">
		<p>요청하신 작업을 처리중입니다. 작업이 완료될때 까지 잠시만 기다려 주시기 바랍니다.</p>
           <img src="/happytalk/images/admin/loading_bar.gif" align="center" />
       	</div>
       </div>
	</div>
	
	<div class="frameLayer" style="width:100%;">
		<iframe id="authFrame" src="" style="max-width:700px;width:100%;background:white;border:2px;"></iframe>
		<div class="btn" style="width:100%;height:40px;">
			<div class="closeIframeBtn nextStep" style="width:100%;height:40px;">
				<a href="#">닫기</a>
			</div>
		</div>
	</div>
	
	<div class="mainLayer">
		<div class="top">
			<img src="/happytalk/images/hk/logo.png" />
			<div class="step" >
				<ul class="step1 step_select">
					<li class="step_active"><img src="/happytalk/images/hk/step1.png" /><span class="step_num">1</span><span> 계약선택</span></li>
					<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">2</span></li>
					<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">3</span></li>
					<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">4</span></li>
				</ul>
				<ul  class="step2 step_change">
					<li class=""><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">1</span></li>
					<li class="step_active"><img src="/happytalk/images/hk/step1.png" /><span class="step_num">2</span><span> 정보변경</span></li>
					<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">3</span></li>
					<li><img src="/happytalk/images/hk/step1_1.png" /><span class="step_num">4</span></li>
				</ul>
			</div>
		</div>
	
		<form name='insurance_debit_change_form'>
		<div class="contents">
			<div >
				<div >
					<div class="title-btn">
						<div>
								<a href="#">보험료 자동이체 변경</a>
						</div>
					</div>
	
					<div>
						<div class="description step1 step_select" >
								<p>1) 자동이체 계좌의 예금주가 보험계약자와 동일할 경우에만 변경이 가능합니다.</p>
								<p>2) 계약 목록은 현재 정상적으로 유지중인 장기보험 계약만 조회 됩니다. (일시납 계약 제외)</p>
								<p>3) 보험료 자동이체 계좌 또는 이체일 변경을 하고자 하는 계약을 선택해 주시기 바랍니다.</p>
						</div>
						<div class="description step2 notap step_change" >
								<p>변경 항목(전체, 계좌번호, 이체일)을 선택해 주십시오</p>
						</div>
					</div>
	
					<div class="step1 step_select" >
						<div class="contract_list">
							<div class="contract_info">
								<label class="checkbox-wrap contract_all" ><input type="checkbox"  name="all_check" value="1"><i class="check-icon" ></i><span class="contract_title">변경 가능 계약목록</span></label>
							</div>
							<c:forEach var="data" items="${lists}" varStatus="status">
								<div class="contract_info">
												<label class="checkbox-wrap"><input type="checkbox"
													name="stock_no[]" 
													value="${data.TBMA003_POL_NO }"
													data-trans-date="${data.transDate }" 
													data-is-no-account-no="<c:if test="${data.accountNo eq '' or data.collectMethodName eq '카드자동이체'}">Y</c:if>"
												/><i
													class="check-icon"></i><span class="contract_title">${status.index+1})
														${data.CHAR_1 }</span> <span class="point_color">[${data.contractStatusName }]</span></label>
												<ul>
										<li>피보험자 : ${data.TBMA003_ISD_NAME }</li>
										<li>보험기간 : ${data.TBMA001_ISTAR_CONT_DATE} ~ ${data.TBMA001_IEND_CONT_DATE}</li>
										<li>실납입보험료 : ${String.format("%,d", Integer.parseInt(String.valueOf(data.TBMA001_SUM_PREM)))}원</li>
										<li>납입주기 : ${data.paymentMethodName}</li>
										<li>납입방법(이체일) : ${data.collectMethodName}${data.paymentMethodAppend}</li>
										<li>금융 기관명 : ${data.bankName}</li>
										<li>계좌번호(예금주) : ${String.valueOf(data.accountNo).replaceAll("(.{6}$)", "******")} <c:if test="${data.depositorName ne ''}">(${data.depositorName})</c:if></li>
									</ul>
								</div>
							</c:forEach>
						</div>
					</div>
	
					<div class="btn step1 step_select btn_next">
						<div class="nextStep">
								<a href="#">다음</a>
						</div>
					</div>
	
					<div class="step2 step_change">
						<div class="check-item"> 
							<ul>
								<li><label class="radio all"><input type="radio" name="change_type" id="change_type_all" value="all" checked /><span class="ico"></span><span class="radio-txt" >전체</span></label></li>
								<li><label class="radio account"><input type="radio" name="change_type" id="change_type_account_number" value="account_number" /><span class="ico"></span><span class="radio-txt">계좌번호</span></label></li>
								<li><label class="radio transferDate"><input type="radio" name="change_type" id="change_type_transfer_date" value="transfer_date" /><span class="ico"></span><span class="radio-txt">이체일</span></label></li>
							</ul>
						</div>
	
						<div class="step2-info group-transferDate field account_number_field">
							<div>은행명</div>
							<select name="bank" class="selectbox" >
								<option value="">선택</option>
								<c:forEach var="data" items="${bankList }" varStatus="status">
								<option value="${data.bankCd }">${data.bankNm }</option>
								</c:forEach>
							</select>
						</div>
	
						<div class="step2-info account-number group-transferDate field account_number_field">
							<div style="vertical-align: bottom;">계좌번호<img src="/happytalk/images/hk/popup.png" class="i-image account-layer-btn"></div>
							<div id="account-layer" class="account-layer">
								<div class="triangle"></div>
								<div class="popup-layer notap" ><p>- 각 은행의 대표 계좌번호(전화번호)는 사용할 수 없습니다. (각 은행마다 계좌번호 체계가 다르기 때문에 제어가 되지 않으니 유의하시기 바랍니다.)</p>
	 							<p>- 자동이체 계좌는 계좌의 예금주가 보험 계약자와 동일할 경우에만 변경이 가능합니다.</p></div>
							</div>
							<div class="info-account" ><input type="number" name="account_number" id="account_number"  placeholder='"-"없이 입력하세요'/></div>
							<div class="info-btn account_number_validate">
								<div>예금주 확인</div>
							</div>
							<script type="text/javascript">
								$('.account-layer-btn').click(function() {
									$('#account-layer').toggle();
								});
							</script>
						</div>
	
						<div class="step2-info group-transferDate field account_number_field">
							<div>예금주</div>
							<input type="text" name="account_name" id="account_name" readonly="readonly" value="" style="width:100%;box-sizing: border-box;"/>
							<input name="is_validate_bank" type="hidden" value="0" /><br />
						</div>
	
						<div class="step2-info group-account field transfer_date_field">
							<div>이체일</div>
							<select name="transfer_date" class="selectbox">
								<option value="">선택하세요</option>
								<option value="05">5</option>
								<option value="10">10</option>
								<option value="15">15</option>
								<option value="20">20</option>
								<option value="25">25</option>
							</select>
						</div>
	
						<div class="step2-info">
							<div>통합이체여부<img src="/happytalk/images/hk/popup.png" class="i-image combine-info combine-info-btn"></div>
							<div id="combine-layer" class="combine-layer">
							<div class="triangle" style="left:115px;"></div>
							<div class="popup-layer notap" ><p>- 통합이체 설정하실 경우 동일 계좌.이체일로 자동이체 등록된 계약의 보험료가 합산청구됩니다.</p></div>
							</div>
							<script type="text/javascript">
								$('.combine-info-btn').click(function() {
									$('#combine-layer').toggle();
								});
							</script>
							<select name="is_integrated_transfer" class="selectbox" >
								<option value="">선택하세요.</option>
								<option value="Y">예</option>
								<option value="N">아니요</option>
							</select>
						</div>
	
						<div class="description agree_box">
							<p>1) 당사 자동이체 청구일은 5, 10, 15, 20, 25일로 운영 됩니다.</p>
							<p>2) 자동이체 계좌 변경은 은행 등록까지 2영업일이 소요되므로, 이체지정일 4영업일 전까지 등록하셔야 이체일에 출금 가능합니다.</p>
							<p>3) 자동이체는 이체일 전에 미리 은행으로 청구내역을 전송하므로, 이미 청구 중인 계약은 변경전 계좌로 보험료가 청구 됩니다.</p>
							<p>4) 이체지정일에 출금되지 않았을 경우 다음 청구일에 재청구 됩니다</p>
							<p>5) 통합이체 설정하시는 경우 동일 계좌/이체일로 자동이체 등록된 계약의 보험료가 합산 청구 됩니다.</p>
	
							<div class="agree" >
								<span>위 내용에 동의하십니까?</span>
								<span><label class="checkbox-wrap"><input type="checkbox" name="is_allow" value="Y"><i class="check-icon"></i><span>동의함</span> </label></span>
							</div>
						</div>
	
						<div class="step2-info">
						</div>
					</div>
	
					<div class="step2 btn step2-btn step_change">
						<div class="btn_before">계약 다시 선택</div>
						<div class="btn_action">처리</div>
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
	</div>
</div>
</center>
</body>
</html>