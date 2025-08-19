<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	<c:choose>
		<c:when test="${not empty member}">
			<c:set var="memberDivCd" value="${member.member_div_cd }" />
			<c:set var="name" value="${member.name }" />
			<c:set var="id" value="${member.id }" />
			<c:set var="tel" value="${member.tel }" />
			<c:set var="email" value="${member.email }" />
			<c:set var="honorPwd" value="${member.honor_pwd }" />
			<c:set var="cnsPossibleYn" value="${member.cns_possible_yn }" />
			<c:set var="upperMemberUid" value="${member.upper_member_uid }" />
			<c:set var="departCd" value="${member.depart_cd }" />
			<c:set var="departNm" value="${member.depart_nm }" />
			<c:set var="validYn" value="${member.valid_yn }" />
		</c:when>
		<c:otherwise>
			<c:set var="memberDivCd" value="C" />
			<c:set var="name" value="" />
			<c:set var="id" value="" />
			<c:set var="tel" value="" />
			<c:set var="email" value="" />
			<c:set var="cnsPossibleYn" value="" />
			<c:set var="upperMemberUid" value="" />
			<c:set var="departCd" value="" />
			<c:set var="departNm" value="" />
			<c:set var="validYn" value="N" />
		</c:otherwise>
	</c:choose>

	<div class="inner">
		<form id="memberPopForm" name="memberPopForm" method="post" action="">
			<input type="hidden" name="memberUid" value="${memberUid}" />
			<input type="hidden" name="idDupliCheck" value="N" />
			<input type="hidden" name="hidUpperMemberUid" value="${upperMemberUid }" />
			<input type="hidden" name="hidDepartCd" value="${departCd }" />
			<input type="hidden" name="departCd" value="${departCd }" />
			<input type="hidden" name="validYn" value="${validYn }" />
			<input type="hidden" name="id" value="${id}" />
			<c:if test="${rollType != 'S'}">
			<input type="hidden" name="cocId" value="${member.coc_id}" readonly="readonly">
			<input type="hidden" name="pwd" value="" readonly="readonly">
			</c:if>
			<div class="popup_head">
				<h1 class="popup_tit">회원 등록/수정</h1>
				<button type="button" class="btn_popup_close dev_close_pop">창닫기</button>
			</div>
			<div class="popup_body">
				<c:if test="${rollType == 'S'}">
				<table class="tCont setting dev_member_set">
					<caption>회원 등록/수정 폼입니다.</caption>
					<colgroup>
						<col style="width:15%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:35%">
					</colgroup>
					<tbody>
						<tr>
							<th>이름</th>
							<td class="textL" >
								<%-- <input type="hidden" name="name" value="${name }" readonly="readonly"> --%>
								<input type="text" name="name" value="<c:out value="${member.name }"/>" />
								<%-- <span class="id_text">${name }</span> --%>
							</td>
							<th>사번</th>
							<td class="textL">
								<c:choose>
									<c:when test="${not empty member}">
										<c:out value="${member.coc_id }"/>
									</c:when>
									<c:otherwise>
										<input type="text" name="cocId" value="">
										<!-- <button type="button" class="btn_check_name dev_sch_member_btn">검색</button> -->
									</c:otherwise>
								</c:choose>					
							</td>
						</tr>
						<c:choose>
							<c:when test="${not empty member}">
								<input type="hidden" id="pwd" name="pwd" value="" readonly="readonly">
							</c:when>
							<c:otherwise>
								<!-- <tr >
									<th>비밀번호</th>
									<td class="textL" colspan="3"><input type="hidden" id="pwd" name="pwd"> &nbsp;&nbsp;* 초기 비밀번호: 등록년월일 (yyyymmdd)</td>
									
									<th>비밀번호 확인</th>
									<td class="textL"><input type="password"></td>
									<input type="password" onblur="javascript:passwordCheck(this.value, this);">
									
								</tr> -->
							</c:otherwise>
						</c:choose>
						<tr>
							<th>권한 설정</th>
							<td class="textL">
								<div class="staff_radio_area">
<%-- 
									<input type="radio" class="radio_18" id="memberDivCd_S" name="memberDivCd" value="S" <c:if test="${member.member_div_cd eq 'S'}">checked</c:if> >
									<label for="memberDivCd_S"><span></span>슈퍼 관리자</label>

									<input type="radio" class="radio_18" id="memberDivCd_A" name="memberDivCd" value="A" <c:if test="${member.member_div_cd eq 'A'}">checked</c:if> >
									<label for="memberDivCd_A"><span></span>시스템 관리자</label>
 --%>
									<input type="radio" class="radio_18" id="memberDivCd_M" name="memberDivCd" value="M" <c:if test="${memberDivCd eq 'M'}">checked</c:if> >
									<label for="memberDivCd_M"><span></span>매니저</label>
									<input type="radio" class="radio_18" id="memberDivCd_C" name="memberDivCd" value="C" <c:if test="${memberDivCd eq 'C'}">checked</c:if> >
									<label for="memberDivCd_C"><span></span>상담직원</label>
									<input type="hidden" name="memberDivCd" value="${member.member_div_cd }" />
									<c:set var="memberDivCd" value="${member.member_div_cd }" />
									<c:set var="name" value="${member.name }" />
									<c:set var="id" value="${member.id }" />
									<c:set var="tel" value="${member.tel }" />
									<c:set var="email" value="${member.email }" />
									<c:set var="cnsPossibleYn" value="${member.cns_possible_yn }" />
									<c:set var="upperMemberUid" value="${member.upper_member_uid }" />
									<%-- <c:set var="departCd" value="${member.depart_cd }" />
									<c:set var="departNm" value="${member.depart_nm }" /> --%>
								</div>
							</td>
							<th class="trManagerList">담당 매니저</th>
							<td class="trManagerList textL">
								<select class="select_admin" name="upperMemberUid" value="${hidUpperMemberUid }">
								</select>
							</td>
							<th class="trDepartList">부서</th>
							<td class="trDepartList textL">
								<select class="select_depart" name="departCd" value="${departCd }">
								</select>
							</td>
						</tr>
						<tr style="display:none;">
							<th>전화번호</th>
							<td class="textL" colspan="3"><input type="text" name="tel" value="${tel }" maxlength="14"></td>
						</tr>
						<tr style="display:none;">
							<th>이메일주소</th>
							<td class="textL" colspan="3"><input type="text" name="email" value="${email }" maxlength="100"></td>
						</tr>
						<tr>
							<%-- <th>아너스넷<br>비밀번호</th>
							<td class="textL" >
								<input type="password" name="honorPwd" value="${member.honor_pwd }" />
							</td> --%>
							<th><span class="workPart">부서</span></th>
							<td  class=" textL">
								<span class="workPart depart_nm_text">${departNm }</span>
							</td>
							<th><c:if test="${not empty member}">계정상태</c:if></th>
							<td class="textL">
							<c:if test="${not empty member}">
								<c:choose>
									<c:when test="${member.valid_yn eq 'Y'}">
										계정사용
										<button type="button" class="btn_id_del dev_valid_n_btn"><i></i>계정 중지</button>
									</c:when>
									<c:otherwise>
										미인증
									</c:otherwise>
								</c:choose>
							</c:if>
							</td>
						</tr>
						<input type="hidden" name="cnsPossibleYn" value="Y" />
					</tbody>
				</table>
				</c:if>
 				<span style="font-size: 12px;color: gray;">&nbsp;&nbsp;* 초기 비밀번호: 등록년월일 (yyyymmdd)</span>
				<table class="tCont dev_category_table" style="margin-top: 10px;">
					<caption>대상지정 현황 목록입니다.</caption>
					<colgroup>
						<col style="width:11.67%">
						<col style="width:17.67%">
						<col style="width:17.67%">
						<c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">
							<col style="width:17.67%">
						</c:if>
						<col style="width:17.67%">
						<col style="width:17.67%">
					</colgroup>
					<thead>
						<tr>
							<th>인입채널</th>
							<th>대분류</th>
							<th>중분류</th>
							<c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">
								<th>소분류</th>
							</c:if>
							<th>
								
								<input type="checkbox" class="checkbox_18 notext" name="ctgNumAll" id="ctgNumAll" > 
								<label for="ctgNumAll"><span></span></label>
							배정</th>
							<th>우선</th>
						</tr>
					</thead>
					<tbody>
						<c:set var="ctgNum1" value="" />
						<c:set var="ctgNum2" value="" />
						<c:set var="ctgNum3" value="" />
						<c:set var="ctgNm1" value="" />
						<c:set var="ctgNm2" value="" />
						<c:set var="ctgNm3" value="" />
						<c:set var="channelCd" value= "" />
						<c:forEach var="data" items="${categoryList }" varStatus="status">
							<tr>
								<c:if test="${channelCd ne data.cstm_link_div_cd }">
									<c:set var="channelCd" value= "${data.cstm_link_div_cd }" />
									<td rowspan="${data.cstm_link_div_cd_count }">${data.cstm_link_div_nm }</td>
								</c:if>
								<c:if test="${ctgNum1 eq '' || ctgNum1 ne data.ctg_num1 }">
									<c:set var="ctgNum1" value="${data.ctg_num1 }" />
									<c:set var="ctgNm1" value="${data.ctg_nm1 }" />
									<c:choose>
										<c:when test="${data.ctg_dpt1_count eq '0' }">
											<td>${data.ctg_nm1 }</td>
										</c:when>
										<c:otherwise>
											<td rowspan="${data.ctg_dpt1_count }">${data.ctg_nm1 }</td>
										</c:otherwise>
									</c:choose>
								</c:if>
								<c:if test="${defaultSet.ctg_mgt_dpt eq '2' }">
									<c:if test="${ctgNum2 eq '' || empty data.ctg_num2 || ctgNum2 ne data.ctg_num2 }">
										<c:set var="ctgNum2" value="${data.ctg_num2 }" />
										<c:set var="ctgNm2" value="${data.ctg_nm2 }" />
										<c:choose>
											<c:when test="${data.ctg_dpt2_count eq '0' }">
												<td data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }">${data.ctg_nm2 }
											</c:when>
											<c:otherwise>
												<td rowspan="${data.ctg_dpt2_count }" data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }">${data.ctg_nm2 }
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:if>
								<c:if test="${defaultSet.ctg_mgt_dpt eq '3' }">
									<c:if test="${ctgNum2 eq '' || empty data.ctg_num2 || ctgNum2 ne data.ctg_num2 }">
										<c:set var="ctgNum2" value="${data.ctg_num2 }" />
										<c:set var="ctgNm2" value="${data.ctg_nm2 }" />
										<c:choose>
											<c:when test="${data.ctg_dpt2_count eq '0' }">
												<td>${data.ctg_nm2 }</td>
											</c:when>
											<c:otherwise>
												<td rowspan="${data.ctg_dpt2_count }">${data.ctg_nm2 }</td>
											</c:otherwise>
										</c:choose>
									</c:if>
									<c:if test="${ctgNum3 eq '' || empty data.ctg_num3 || ctgNum3 ne data.ctg_num3 }">
										<c:set var="ctgNum3" value="${data.ctg_num3 }" />
										<c:set var="ctgNm3" value="${data.ctg_nm3 }" />
										<c:choose>
											<c:when test="${data.ctg_dpt3_count eq '0' }">
												<td data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }" data-ctgNm3="${ctgNm3 }">
											</c:when>
											<c:otherwise>
												<td rowspan="${data.ctg_dpt3_count }" data-ctgNm1="${ctgNm1 }" data-ctgNm2="${ctgNm2 }" data-ctgNm3="${ctgNm3 }">
											</c:otherwise>
										</c:choose>
											<a href="javascript:void(0);" class="link_text dev_select_btn" data-ctg_num="${data.ctg_num3 }">${data.ctg_nm3 }</a>
										</td>
									</c:if>
								</c:if>
								<td>	<input type="checkbox" class="checkbox_18 notext" name="ctgNumArr" value="${data.ctg_num2}" id="ctgNumArr_${status.count-1}" 
										<c:forEach var="mappingList" items="${ctgMemberMapping }">
											<c:if test="${mappingList.ctg_num eq data.ctg_num2}">checked</c:if>
										</c:forEach>> 
										<label for="ctgNumArr_${status.count-1}"><span></span></label>
								</td>
								<td>
									<input type="radio" class="radio_18 no_text" id="firstYn_${status.count-1}" name="firstYn" value="${data.ctg_num2}" 
									<c:forEach var="mappingList" items="${ctgMemberMapping }">
										<c:if test="${mappingList.ctg_num eq data.ctg_num2 and mappingList.first_yn eq 'Y'}">checked</c:if>
									</c:forEach>/> 
									<label for="firstYn_${status.count-1}"><span></span></label>								
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>				
				
				<div class="popup_btn_area">
					<c:choose>
						<c:when test="${not empty member}">
							<button type="button" class="btn_black_modify dev_update_btn"><i></i>회원 수정</button>
							<button type="button" class="btn_popup_close btn_go_cancel dev_cancel_pop">취소</button>
							<c:if test="${rollType == 'S'}">
							<button type="button" class="btn_id_del dev_delete_btn"><i></i>계정삭제</button>
							</c:if>
							<button type="button" class="btn_id_pwd dev_pwd_btn"><i></i>비밀번호 초기화</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn_staff_plus dev_insert_btn"><i></i>회원 저장</button>
							<button type="button" class="btn_popup_close btn_go_cancel dev_cancel_pop">취소</button>
						</c:otherwise>
					</c:choose>
				</div>
				<c:if test="${rollType == 'S'}">
				<div class="search_member_area dev_sch_member_list">
				</div>
				</c:if>
			</div>
		</form>
	</div>
<script type="text/javascript">

		$(document).on("click", "#ctgNumAll", function() {
			if($("#ctgNumAll").prop("checked")){
				$("input[name=ctgNumArr]").prop("checked", true);
			}
			else {
				$("input[name=ctgNumArr]").prop("checked", false);
			}
		});
							</script>