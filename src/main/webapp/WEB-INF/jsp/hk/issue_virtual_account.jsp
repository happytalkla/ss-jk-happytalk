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
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/issue_virtual_account.css' />">
	<link rel=stylesheet type="text/css" href="<c:url value='/css/hk/loading.css' />">
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript">

		// up button 클릭
		$(document).on("click", '#btn_close', function() {
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

		function moveStep(stepCode)
		{
			$(".step_select").hide();
			$(".step_change").hide();
			$(".step_complete").hide();
			$(".step_" + stepCode).show();
		}

		$(document).on("click", '#btn_next', function() {
			if ($('input[name="stock_no[]"]:checked').size() == 0) {
				alert("가상계좌를 발급하실 계약건을 선택하여 주시기 바랍니다.");
				return false;
			}

			if (confirm(($('input[name="stock_no[]"]:checked').size() + "건의 계약을 선택하셨습니다. 가상계좌를 발급하시겠습니까?"))) {
				moveStep('change');
			}
		});

		$(document).on("click", '#btn_before', function() {
			moveStep('select');
		});

		$(document).on("click", 'input[name="stock_no[]"]', function() {
			calculateSum();
		});

		function calculateSum()
		{
			var amountTotal = 0;

			for (i = 0; i < $('input[name="stock_no[]"]:checked').size(); i++) {
				amountTotal += parseInt($('input[name="stock_no[]"]:checked').eq(i).attr("date-price"));
			}

			$("#totalAmount").html(amountTotal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원");
			$("#totalCount").html($('input[name="stock_no[]"]:checked').size() + "개");
		}

		var intervalPlay = null;

		$(document).on("click", '#btn_action', function() {

			if ($('input[name="is_allow"]:checked').val() != 'T') {
				alert("[동의함] 으로 선택하셔야 보험료 납입 가상계좌 발급처리 진행이 가능합니다.");
				return false;
			}

			if ($('select[name="bank"] option:selected').val() == "") {
				alert("은행명을 선택하여 주시기 바랍니다.");
				return false;
			}

			/*
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
						window.open("${heungkuk_domain}/CBM/mobileAuthSignProc.do?uuid=${uuid}&channel=${channel}&position=C");
						intervalPlay = setInterval(setPoolingAction, 2000);
					}
				},
				complete : function() {
				}
			});
			*/;
			
			setAction();
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

			$(".modal-loading-layer").css('display','inline-block');
			
			$.ajax({
				url : "<c:url value='/hk/issue_virtual_account_action' />",
				data : {
					"stock_no" : stock_number,
					"bank_cd" : $('select[name="bank"] option:selected').val(),
					"uuid" : "${uuid}",
					"channel" : "${channel}"
				},
				type : "post",
				success : function(result) {
					if (typeof(result.code) != "undefined" && result.code == '0000') {
						// 전체 성공일 경우
						moveStep('complete');
					} else if (typeof(result.code) != "undefined" && result.code == '9999') {
						var message = [];
						/*
						var reasonMsg = {
							'A000000000' : {'preMsg' : '*신규발급 성공', 'postMsg' : ''}, 
							'BFI0000214' : {'preMsg' : '*신규발급 실패', 'postMsg' : ' //이미 발급된 가상계좌가 존재합니다. 콜센터로 문의 바랍니다.'}, 
							'C000000000' : {'preMsg' : '*기타 실패', 'postMsg' : ' //기타 시스템 오류입니다. 콜센터로 문의 바랍니다.'}
						};
						
						for (key in reasonMsg) {
							if (typeof(result.reason[key]) != "undefined" && result.reason[key] != null) {
								var reason = result.reason[key];
								message.push(reasonMsg[key].preMsg + " ("+result.reason_count[0]+"건) : " + reason.stockNo + reasonMsg[key].postMsg);
							}
						}
						*/

						
						if (typeof(result.reason['A000000000']) != "undefined" && result.reason['A000000000'] != null) {
							var reason = result.reason['A000000000'];
							message.push("*신규발급 성공 ("+result.reason_count[0]+"건) : " + reason.stockNo);
						}

						if (typeof(result.reason['BFI0000214']) != "undefined" && result.reason['BFI0000214'] != null) {
							var reason = result.reason['BFI0000214'];
							message.push("*신규발급 실패 ("+result.reason_count[1]+"건) : " + reason.stockNo + " //이미 발급된 가상계좌가 존재합니다. 콜센터로 문의 바랍니다.");
						}

						if (typeof(result.reason['C000000000']) != "undefined" && result.reason['C000000000'] != null) {
							var reason = result.reason['C000000000'];
							message.push("*기타 실패 ("+result.reason_count[2]+"건) : " + reason.stockNo + " //" + "기타 시스템 오류입니다. 콜센터로 문의 바랍니다.");
						}


						alert(message.join("\n"));

						if (result.reason_count[0] > 0) {
							moveStep('complete');
						}
					} else {
						alert(result.message);
					}
					$(".modal-loading-layer").css('display','none');
				},
				complete : function() {
					$(".modal-loading-layer").css('display','none');
				}
			});
		}

		$(document).on( "click", 'input[name="all_check"]', function() {
			$('input[name="stock_no[]"]').prop("checked", $(this).prop('checked'));
			calculateSum();
		});

		$(document).ready(function() {
			moveStep('select');
			<c:choose>
			<c:when test="${empty lists}">
			<c:if test="${errorCode eq '0006'}">
				alert("고객님 반갑습니다.\n"
					+ "현재 고객님께서 당사에 유지 중인 장기보험 계약이 존재하지 않습니다. (실효 상태 계약 포함)\n"
					+ "★ [추천] 신규가입 상담을 원하시면 \"상담직원으로 전환하기\" 를 통해 채팅상담사와 자세한 대화를 나눠 보시길 추천 드립니다.");
			</c:if>
			<c:if test="${errorCode eq '0007'}">
				alert("고객님 반갑습니다."+ "\n" + 
						"현재 고객님께서는 보험료 납입을 위한 가상계좌 발급이 가능한 장기보험 계약이 존재하지 않습니다.\n\n" + 
						"참고로, 실효 상태 또는 납입주기가 일시납인 계약은 가상계좌 발급이 불가합니다.");
			</c:if>
			_close()
			</c:when>
			</c:choose>
		});
	</script>
	
</head>

<body>

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
		    
	<input name="uuid" type="hidden" value="${uuid }" />
	<input name="channel" type="hidden" value="${channel }" />

	<div class="top">

		<img src="<c:url value='/images/hk/logo.png' />" />
		<div class="step">
			<ul class="step_select">
				<li class="step_active"><img src="<c:url value='/images/hk/step1.png' />" /><span class="step_num">1</span><span> 계약선택</span></li>
				<li><img src="<c:url value='/images/hk/step1_1.png' />" /><span class="step_num">2</span></li>
				<li><img src="<c:url value='/images/hk/step1_1.png' />" /><span class="step_num">3</span></li>
			</ul>
			<ul class="step_change">
				<li class=""><img src="<c:url value='/images/hk/step1_1.png' />" /><span class="step_num">1</span></li>
				<li class="step_active"><img src="<c:url value='/images/hk/step1.png' />" /><span class="step_num">2</span><span> 은행선택</span></li>
				<li><img src="<c:url value='/images/hk/step1_1.png' />" /><span class="step_num">3</span></li>
			</ul>
			<ul class="step_complete">
				<li><img src="<c:url value='/images/hk/step1_1.png' />" /><span class="step_num" style="position: absolute; margin-left: -9px">1</span></li>
				<li><img src="<c:url value='/images/hk/step1_1.png' />" /><span class="step_num" style="position: absolute; margin-left: -9px">2</span></li>
				<li class="step_active"><img src="<c:url value='/images/hk/step1.png' />" /><span class="step_num" style="position: absolute; margin-left: -9px">3</span><span>완료</span></li>
			</ul>
		</div>
	</div>


	<form name='contractFrm'>
		<div class="contents">
		


			<div>
				<div>
					<div class="title-btn">
						<div>
							<a href="#">보험료 납입 가상계좌 발급</a>
						</div>
					</div>

					<div>
						<div class="description step1 step_select">
							<p>1) 가상계좌는 계약별 발급만 가능하며, 1회 보험료와 상이하게 입금하시는 경우 보험료 자동 수납처리에서
								제외되오니, 해당 취급점으로 확인하시기 바랍니다.</p>
							<p>2) 발급된 가상계좌는 발급된 월에만 사용가능하며, 보험료 수납 후에는 자동 폐쇄되는 1회성 계좌입니다.</p>
							<p>3) 계약 목록은 현재 정상적으로 유지중인 장기보험 계약만 조회됩니다.</p>
							<p>4) 보험료 납입 가상계좌 발급을 원하시는 계약을 선택해 주시기 바랍니다.</p>
						</div>
						<div class="description step2 notap step_change">
							<p>선택 계약 건수와 보험료를 확인하신 후 발급은행을 선택해 주십시오.</p>
						</div>
					</div>

					<div class="step1 step_select">
						<div class="contract_list">
							<div class="contract_info">
								<label class="checkbox-wrap contract_all"><input type="checkbox" name="all_check" value="all"><i class="check-icon"></i><span class="contract_title">가상계좌 발급 가능 계약 목록</span> </label>
							</div>
							<!--div class=""><input type="checkbox" name="" id="contract_chk" /><span>가상계좌 발급 가능 계약 목록</span></div-->
							<c:forEach var="data" items="${lists }" varStatus="status">
								<div class="contract_info">
									<label class="checkbox-wrap"> <input type="checkbox"
										name="stock_no[]" value="${data.TBMA003_POL_NO }"
										date-price="${Integer.parseInt(String.valueOf(data.receiptAmt))}" />
										<i class="check-icon"></i><span class="contract_title">${status.index+1}) ${data.CHAR_1 }</span>
										<span class="point_color">[${data.contractStatusName }]</span>
									</label>
									<!--input type="checkbox" name="" id="contract_chk" /-->
									<ul>
										<li>증권번호 : ${data.TBMA003_POL_NO }</li>
										<li>피보험자 : ${data.TBMA003_ISD_NAME }</li>
										<li>보험기간 : ${data.TBMA001_ISTAR_CONT_DATE} ~ ${data.TBMA001_IEND_CONT_DATE}</li>
										<li>실납입 1회보험료 : ${String.format("%,d", Integer.parseInt(String.valueOf(data.TBMA001_SUM_PREM)))}원</li>
										<li>최종입금월(회차) : ${data.TBMA001_END_NAB_YM} (${data.TBMA001_END_NAB_NO})</li>
										<li>납입방법(이체일) : ${data.collectMethodName}${data.paymentMethodAppend}</li>
										<li class="point_color">납입대상 : ${Integer.parseInt(data.paymentTimes)}회차</li>
									</ul>
								</div>
							</c:forEach>
						</div>
					</div>

					<div class="btn step1 step_select nextStep" id="btn_next">
						<div>
							<a href="#">다음</a>
						</div>
					</div>


					<div class="step2 step_change">
						<div class="contract_list">
							<div class="">
								<span>총 선택 건수</span><span id="totalCount">0건</span>
							</div>
							<div class="">
								<span>납부 보험료</span><span class="point_color" id="totalAmount">60,728원</span>
							</div>

						</div>

						<div class="description agree_box">
							<p>1) 가상계좌는 계약별 발급만 가능하며, 1회 보험료와 상이하게 입금하시는 경우 보험료 자동 수납처리에서
								제외되오니, 해당 취급점으로 확인하시기 바랍니다.</p>
							<p>2) 발급된 가상계좌는 발급된 월에만 사용가능하며, 보험료 수납 후에는 자동 폐쇄되는 1회성 계좌입니다.</p>

							<div class="agree">
								<span>위 내용에 동의하십니까?</span> <span><label
									class="checkbox-wrap"><input type="checkbox"
										name="is_allow" value="T"><i class="check-icon"></i><span>동의함</span>
								</label></span>
							</div>
						</div>


						<div class="bank_info">
							<div>발급은행</div>

							<select name="bank" class="bank_list" id="bank_list">
								<option value="">선택하세요</option>
								<option value="003">기업은행</option>
								<option value="004">국민은행</option>
								<option value="005">외환은행</option>
								<option value="011">농협중앙회</option>
								<option value="020">우리은행</option>
								<option value="023">SC제일은행</option>
								<option value="026">신한은행</option>
								<option value="031">대구은행</option>
								<option value="032">부산은행</option>
								<option value="039">경남은행</option>
								<option value="081">하나은행</option>
							</select>

						</div>

					</div>

					<div class="step2 btn step2-btn step_change">
						<div id="btn_before">계약 다시 선택</div>
						<div id="btn_action">처리</div>
					</div>
				</div>
			</div>
		</div>

		<div class="contents step3 step_complete">
			<div class="contents-line"></div>
			<div class="step3-img">
				<div>
					<img src="<c:url value='/images/hk/complete.png' />"
						class="completeImg" />
				</div>
			</div>
			<div class="step3-text">
				<div class=" completeText">
					가상계좌 발급이<br> 정상 처리 되었습니다.
				</div>
			</div>
			<div>
				<div class=" subText">
					<p class="normal">
						ㆍ아래 <span class="bold">“확인”</span> 버튼을 눌러서 흥미봇 대화창으로 이동하신 후<br>
						<span class="point">대화창 내 <span class="bold">“발급된
								가상계좌 확인하기”</span>를 꼭~ 선택해 주세요!
						</span>
					</p>
				</div>
			</div>
			<div class=" btn" id="btn_close">
				<div>
					<a href="#">확인</a>
				</div>
			</div>
		</div>


	</form>
	<div class="footer">
		<div>
			<img src="<c:url value='/images/hk/bottom.jpg' />" width="100%">
		</div>
	</div>
</div>
</center>
</body>
</html>