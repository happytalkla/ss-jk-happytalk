<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!-- ///////////////상담 템플릿 관리 페이지 -->


						<form id="tplForm" name="tplForm">
							<input type="hidden" name="schTplCtgNum" value="${schTplCtgNum }" />
							<input type="hidden" name="schDeviceType" value="${schDeviceType }" />
							<input type="hidden" name="templateEditYn" value="Y" />
							<input type="hidden" name="tplNum" value="" />
							<input type="hidden" name="oldOrgImgDelYn" />
							<input type="hidden" name="oldTplImgUrl" />
							<input type="hidden" name="oldOrgImgNm" />
							<input type="hidden" name="oldOrgImgPath" />				
							<input type="hidden" name="cnsFrtMsgImg" />				
							<input type="hidden" name="oldOrgPdfDelYn" />
							<input type="hidden" name="oldMimeType" />
							<input type="hidden" name="oldOrgPdfNm" />
							<input type="hidden" name="oldOrgPdfPath" />

							<input type="hidden" name="tplDivCd" value="${tplDivCd }" />
							<input type="hidden" name="tplPageType" value="P" />
							<div class="s_content_head tmenu02">
								<ul class="tabs_menu_s03 dev_ctgList">
									<li class="dev_category_li" data-tplCtgNum="" <c:if test="${data.tpl_ctg_num eq '' }">class="active"</c:if> >전체</li>
									<c:forEach var="data" items="${categoryList }" varStatus="status8">
										<li class="dev_category_li <c:if test="${data.tpl_ctg_num eq schTplCtgNum }">active</c:if> " data-tplCtgNum="${data.tpl_ctg_num }" >
											${data.tpl_ctg_nm }
										</li>
									</c:forEach>
								</ul>
							</div>

							<div class="tabs_s_content03 dev_tabCtgList" id="tab_list">
								<ul class="chatbot_smenu dev_deviceTypeList">
									<!-- <li><a href="javascript:void(0);" data-schDeviceType="ANDROID" <c:if test="${schDeviceType eq 'ANDROID' }">class="active"</c:if> >안드로이드</a></li>
									<li><a href="javascript:void(0);" data-schDeviceType="IPHONE" <c:if test="${schDeviceType eq 'IPHONE' }">class="active"</c:if> >아이폰</a></li>
									<li><a href="javascript:void(0);" data-schDeviceType="WEB" <c:if test="${schDeviceType eq 'WEB' }">class="active"</c:if> >웹</a></li> -->
									<li class="btn_edit dev_add_tpl_btn"><span>+ 템플릿추가</span></li>
								</ul>
								<div class="icon_info_area">
									<span class="left">* 템플릿을 카테고리 별로 확인해서 사용하세요.</span><%--
									<div class="right">
										<i class="icon_individual_s">개인템플릿</i>
										<i class="icon_group_s">상담직원 공유 템플릿</i>
									</div>--%>
								</div>
								<div class="search_area">
									<select name="schType">
										<option value="ALL">전체</option>
										<option value="KEYWORD">키워드</option>
										<option value="ANSWER">답변</option>
										<option value="QUESTION">질문</option>
									</select>
									<div class="search_area_right">
										<input type="text" name="schText" />
										<button type="button" class="btn_search dev_search_btn">검색하기</button>
									</div>
								</div>
								<div class="template_list_area tmenu02 dev_templateList">
									<c:choose>
										<c:when test="${empty templateList}">
											<p class="no_data">목록이 없습니다.</p>
										</c:when>
										<c:otherwise>
											<ul class="template_list">
												<c:forEach var="template" items="${templateList}" varStatus="status1">
													<%-- 고객질문 영역: 텍스트만 가능 --%>
													<li class="tit">
														<c:if test="${template.tpl_div_cd eq 'P'}">
															<div class="smenu_area">
																<%-- <button class="btn_modify dev_list_edit_btn" type="button" data-tplNum="${template.tpl_num}">수정하기</button>--%>
																<button class="btn_del dev_list_del_btn" type="button"  data-tplNum="${template.tpl_num}">삭제하기</button>
															</div>
														</c:if>
														<a href="javascript:void(0);" class="dev_dplListClick" data-tplNum="${template.tpl_num }"><%--
															<c:choose>
																<c:when test="${template.tpl_div_cd eq 'P' }">
																	<i class="icon_individual">개인템플릿</i>
																</c:when>
																<c:when test="${template.tpl_div_cd eq 'G' }">
																	<i class="icon_group">상담직원 공유 템플릿</i>
																</c:when>
															</c:choose>--%>
															<c:choose>
																<c:when test="${template.tpl_msg_div_cd eq 'TEXT' }">
																	<i class="icon_T">T</i>
																</c:when>
																<c:when test="${template.tpl_msg_div_cd eq 'NORMAL' }">
																	<i class="icon_N">N</i>
																</c:when>
															</c:choose>
															<div>
																<div class="text_box">
																<c:if test="${template.top_mark_yn eq 'Y'}"><i class="icon_check_top"></i></c:if>
																	 ${fn:replace(template.cstm_que, newLineChar, '<br/>')}
																</div>
															</div>
														</a>
													</li>
													<%-- 상담직원 답변 영역--%>
													<li class="text" data-tplNum="${template.tpl_num}">
														<div class="right-chat-wrap">
															<div class="bubble-wrap">
																<article class="inner-chat">
																	<div class="bubble-templet type-2">
																		<chat:template jsonContents="${template.reply_cont}" />
																	</div>
																</article>
															</div>
															<div class="tag_area">
																<c:forEach var="keyword" items="${template.templatekwdlist}">
																	<span class="tag">${keyword.kwd_nm}</span>
																</c:forEach>
															</div>
														</div>
													</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<!-- 등록 영역 -->
							<div class="tabs_s_content03 dev_tabCtgEdit" id="tedit">
								<div class="edit_scroll_area">
									<h2 class="notice_tit">상담 템플릿 등록/수정 </h2>
									<div class="setting_area">
										<table class="tCont setting">
											<caption>상담 템플릿 등록/수정 폼입니다.</caption>
											<colgroup>
												<col style="width:20%">
												<col style="width:80%">
											</colgroup>
											<tbody>
												<tr>
													<th>카테고리</th>
													<td class="textL">
														<select name="tplCtgNum">
															<c:forEach var="data" items="${categoryList }" varStatus="status6">
																<option value="${data.tpl_ctg_num }" <c:if test="${data.tpl_ctg_num eq tplCtgNum }">selected</c:if> >${data.tpl_ctg_nm }</option>
															</c:forEach>
														</select>
														<div class="category_check_area">
															<!-- <input type="checkbox" class="checkbox_18" id="category01" name="androidYn" value="Y" checked>
															<label for="category01"><span></span>안드로이드</label>
															<input type="checkbox" class="checkbox_18" id="category02" name="iphoneYn" value="Y" checked>
															<label for="category02"><span></span>아이폰</label>
															<input type="checkbox" class="checkbox_18" id="category03" name="webYn" value="Y" checked>
															<label for="category03"><span></span>웹</label> -->
															
															<input type="hidden" name="androidYn" value="Y" />
															<input type="hidden" name="iphoneYn" value="Y" />
															<input type="hidden" name="webYn" value="Y" />
														</div>
													</td>
												</tr>
												<tr>
													<th>구분</th>
													<td class="textL">
														<select id="tplMsgDivCd" name="tplMsgDivCd">
															<c:forEach var="data" items="${tplMsgDivCdList }" varStatus="status7">
																<option value="${data.cd }">${data.cd_nm }</option>
															</c:forEach>
														</select>
														<span class="icon_info_text" id="icon_info_text" title="- Text : 글,<br> - Normal : 글, 이미지, 링크 템플릿을 구성합니다." data-html="true"></span>
													</td>
												</tr>
											</tbody>
										</table>
										<p class="text_copy_tit">자주하는 질문의 답변을 저장해서 사용할 수 있습니다.</p>
										<table class="tCont setting">
											<caption>상담 템플릿 등록/수정 폼입니다.</caption>
											<colgroup>
												<col style="width:100%">
											</colgroup>
											<tbody>
												<tr>
													<td class="textL">
														<p class="form_tit">고객질문</p>
														<textarea name="cstmQue" placeholder="100자 이내로 질문 내용을 작성해주세요." class="form_text" maxlength="100"></textarea>
													</td>
												</tr>
												<tr>
													<td class="textL">
														<p class="form_tit">상담직원 답변</p>
														<textarea name="replyContText" placeholder="1000자 이내로 답변 내용을 작성해주세요." class="form_text" maxlength="1000"></textarea>
													</td>
												</tr>
												<!-- normal 선택시 -->
												<tr id="tplNormal">
													<td class="textL">
														<p class="form_tit">이미지 (이미지 타입: jpg/png/gif, 최대 2MB)</p>
														<div class="filebox">
															<label for="ex_filename01">파일불러오기</label>
															<input disabled="disabled" class="upload-name dev_file_view_name" value="">
															<input class="upload-hidden dev_file" id="ex_filename01" type="file" name="tplImgUrl">
															<button type="button" class="btn_del dev_file_del_btn">삭제</button><!--삭제버튼:불러온 파일명 바로 옆에 붙게 해주세요 -->
														</div>
														<ul class="file_img">
															<li>
																<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
																<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
																<button type="button" class="btn_del dev_btn_link_del">삭제</button>
															</li>
															<li>
																<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
																<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
																<button type="button" class="btn_del dev_btn_link_del">삭제</button>
															</li>
															<li>
																<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
																<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
																<button type="button" class="btn_del dev_btn_link_del">삭제</button>
															</li>
															<li>
																<input type="text" class="form_link_title" placeholder="최대 30자" maxlength="30" name="linkBtnNmArr"><input type="text" class="form_link" name="linkUrlArr" placeholder="http://">
																<button type="button" class="btn_link_new dev_btn_link_open">새창열기</button>
																<button type="button" class="btn_del dev_btn_link_del">삭제</button>
															</li>
														</ul>
												<p class="form_tit">파일 첨부 (pdf, 최대 2MB)</p>
												<div class="filebox">
													<label for="ex_filename02">파일불러오기</label>
													<a href=#"><input disabled="disabled" class="upload-name dev_file_pdf_view_name" value=""></a>
													<input class="upload-hidden dev_file_pdf" id="ex_filename02" type="file" name="tplPdfUrl">
													<button type="button" class="btn_del dev_file_pdf_del_btn">삭제</button><!--삭제버튼:불러온 파일명 바로 옆에 붙게 해주세요 -->
													<input type="text" style="width:100%;" placeholder="다운로드 버튼명(최대 18자)" maxlength="18" name="tplPdfBtn">
												</div>												
													</td>
												</tr>
												<!--// normal 선택시 -->
												<tr>
													<td class="textL">
														<p class="form_tit">키워드<span class="icon_info_text" title="키워드에 등록된 단어는 채팅창에 노란색으로 표시되며, <br/>채팅도중 채팅창의 키워드를 클릭하면 해당 템플릿 리스트를 볼 수 있습니다." data-html="true"></span></p>
														<input type="text" name="kwdNmArr" class="form_text" placeholder="쉼표(,)로 구분되며 공백과 특수문자 포함 최대 100자까지 입력 가능" maxlength="100" />
													</td>
												</tr>
											</tbody>
										</table>
										<div class="temp_checkbox_area">
											<%-- <input type="checkbox" class="checkbox_18" id="chat_info01" name="shareYn" value="G"
												<c:if test="${tplDivCd eq 'G'}">checked style="cursor:none;"</c:if>
											>
											<label for="chat_info01"><span></span>다른 상담직원과 공유</label> --%>
											<input type="checkbox" class="checkbox_18" id="chat_info02" name="topMarkYn" value="Y">
											<label for="chat_info02"><span></span><i class="icon_check_top"></i>최상단 노출</label>
											<input type="hidden" name="ieInput" /><!-- ie일 경우 ajax file form submit시 마지막에 체크박스가 체크되지 않으면 오류 발생. -->
										</div>

										<div class="btn_area">
											<button type="button" class="btn_go_save dev_save_tpl_btn">저장</button>
											<button type="button" class="btn_go_cancel dev_cancel_tbl_btn">취소</button>
										</div>
									</div>
								</div>
							</div>
							<!--// 등록 영역 -->
						</form>
