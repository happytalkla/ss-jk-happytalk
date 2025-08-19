<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<div class="inner dev_review_div">
		<div class="popup_head">
			<h1 class="popup_tit">후처리 내역을 입력 하시겠습니까?</h1>
			<div id="modify_close">
			
			<button type="button" class="btn_popup_close">창닫기</button>

			</div>
		</div>
		<input type="hidden" name="linkIp" value="${customer.link_ip}" />
		<input type="hidden" name="cstmDivCd" value="${customer.cstm_div_cd}" />
		<input type="hidden" name="loginYn" value="${customer.login_yn}" />
		<input type="hidden" name="cstmDivNm" value="<c:out value="${customer.cstm_div_nm}" />"  />
		<input type="hidden" name="cocId"  value="<c:out value="${customer.coc_id}" />" />
		<input type="hidden" name="name" value="<c:out value="${customer.name}" />"  />				
		<div class="popup_body">
			<table class="tCont setting"> 
				<caption>후처리 등록 폼입니다.</caption>
				<colgroup>
					<col style="width:35%">
					<col>
				</colgroup>
				<tbody>
				<c:if test="${null ne customer.coc_id}"> 
					<tr>
						<td class="textL">
							<span class="form_tit">- 고객  ID : ${customer.coc_id}</span><span class="form_tit" style="line-height:150%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<span class="form_tit">- 고객 이름 : ${customer.name}</span>
						</td>
					</tr>
				</c:if>
					<tr>
						<td class="textL">
						
							<div>선택분류  
							<c:choose>
								<c:when test="${endReviewMode eq 'V'}">
									<select id="endCtg1" data-depth="1" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">대분류 선택</option>
										<c:forEach var="data" items="${endCtg1}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}">${data.ctg_nm}</option>
										</c:forEach>
									</select>
									<select id="endCtg2" data-depth="2" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">중분류 선택</option>
									</select>
									<select id="endCtg3" data-depth="3" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">소분류 선택</option>
									</select>
								</c:when>
								<c:otherwise>
									<select id="endCtg1" data-depth="1" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">대분류 선택</option>
										<c:forEach var="data" items="${endCtg1}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}" <c:if test="${data.ctg_num eq chatEndInfo.DEP_1_CTG_CD }">selected</c:if> >${data.ctg_nm}</option>
										</c:forEach>
									</select>
									<select id="endCtg2" data-depth="2" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">중분류 선택</option>
										<c:forEach var="data" items="${endCtg2}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}" <c:if test="${data.ctg_num eq chatEndInfo.DEP_2_CTG_CD }">selected</c:if> >${data.ctg_nm}</option>
										</c:forEach>										
									</select>
									<select id="endCtg3" data-depth="3" style="width: 25%;height:100%;">
										<option data-ctg-num="" data-ctg-nm="">소분류 선택</option>
										<c:forEach var="data" items="${endCtg3}">
										<option data-ctg-num="${data.ctg_num}" data-ctg-nm="${data.ctg_nm}" <c:if test="${data.ctg_num eq chatEndInfo.DEP_3_CTG_CD }">selected</c:if> >${data.ctg_nm}</option>
										</c:forEach>										
									</select>								
								</c:otherwise>
							</c:choose>								
							</div>														

						</td>
					</tr>
					<tr>
						<td class="textL" style="height:100px;">
							<c:choose>
							<c:when test="${endReviewMode eq 'V'}">
							<div  style="height:100px;overflow-y:auto;">
								<c:out value="${chatEndInfo.memo}" />
								</div>
							</c:when>
							<c:otherwise>
								<textarea name="memo" placeholder="메모를 작성하세요"
									class="form_text" rows='2' >${chatEndInfo.memo}</textarea>
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>

			<!-- 자동 완성 레이어 -->
			<div id="afterAutoCmp" class="text_box">
			</div>

			<div class="popup_btn_area"> 
			<c:if test="${endReviewMode ne 'V'}">
				<button type="button" class="btn_end_save">저장</button>
			</c:if>				
			
			</div>
		</div>
	</div>