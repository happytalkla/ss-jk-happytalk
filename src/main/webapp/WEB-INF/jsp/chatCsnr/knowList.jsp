<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

						<form id="knowForm" name="knowForm">
							<input type="hidden" name="knowNum" value="" />
							<input type="hidden" name="nowPage" value="${nowPage }" />

							<h2 class="notice_tit">
								지식화 관리 신규 등록
								<!-- <button type="button" class="btn_new_knowledge dev_know_new_btn">신규등록</button> -->
							</h2>
							<table class="tCont setting">
								<caption>지식화 관리 등록/수정 폼입니다.</caption>
								<colgroup>
									<col style="width:25%"> 
									<col style="width:75%">
								</colgroup>
								<tbody>
									<tr>
										<th class="textL">프로젝트 아이디</th>
										<td class="textL">
											<select name="botProjectId">
												<c:forEach var="data" items="${projectList }" varStatus="status">
													<option value="${data.bot_project_id }">${data.bot_project_nm }</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr>
										<td class="textL" colspan="2">
											<p class="form_tit">고객질문</p>									
											<textarea name="cstmQue" placeholder="100자 이내로 질문 내용을 작성해주세요." class="form_text" maxlength="100"></textarea>
											<!-- <p class="form_count"><b>100</b> / 500</p> -->
										</td>
									</tr>
									<tr>
										<td class="textL" colspan="2">
											<p class="form_tit">상담직원 답변</p>
											<textarea name="replyContText" placeholder="1000자 이내로 답변 내용을 작성해주세요." class="form_text" maxlength="1000"></textarea>
											<!-- <p class="form_count"><b>100</b> / 500</p> -->
										</td>
									</tr>
								</tbody>
							</table>
							<div class="btn_tmenu_area">
								<button type="button" class="btn_go_save btn_save_knowledge dev_know_save_btn">저장</button>
								<button type="button" class="btn_go_cancel dev_know_clean_btn">초기화</button>
							</div>
							<div class="search_area">
								<select name="schType">
									<option value="ANSWER">답변</option>
									<option value="QUESTION">질문</option>
								</select>
								<div class="search_area_right">
									<input type="text" name="schText"><button type="button" class="btn_search dev_know_search_btn">검색하기</button>
								</div>
							</div>
							
							<div class="template_list_area knowledge" id="dev_knowledgeList">
								<ul class="template_list dev_knowledgeList">
									<c:forEach var="data" items="${knowList }" varStatus="status">
										<li class="tit">								
											<div class="smenu_area">
												<button class="btn_modify dev_know_edit_btn" type="button" data-knowNum="${data.know_num }">수정하기</button>
												<button class="btn_del btn_del_knowledge dev_know_del_btn" type="button" data-knowNum="${data.know_num }">삭제하기</button>
											</div>
											<a href="#">
												<div class="number_box">
													<span class="no_list">${data.know_num }</span>
													<span class="no_date">${data.create_dt }</span>
													<span class="no_staff">${data.bot_project_nm }</span>
												</div>
												<div class="text_box">
													${fn:replace(data.cstm_que, newLineChar, '<br/>') }
												</div>
											</a>
										</li>
										<li class="text">
											<div class="right_area">
												<div class="text_box">
													${fn:replace(data.reply_cont_text, newLineChar, '<br/>') }
												</div>
											</div>
										</li>
									</c:forEach>
								</ul>
							</div>
						</form>