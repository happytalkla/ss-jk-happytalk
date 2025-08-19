<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="https://happytalk.io/jsp/jstl/chat" prefix="chat" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

									<c:choose>
										<c:when test="${empty templateList }">
											<p class="no_data">목록이 없습니다.</p>
										</c:when>
										<c:otherwise>
											<ul class="template_list">
												<c:forEach var="data" items="${templateList}" varStatus="status1">
													<li class="tit">
														<c:if test="${data.tpl_div_cd eq 'P'}">
															<div class="smenu_area">
																<button class="btn_del dev_list_del_btn" type="button"  data-tplNum="${data.tpl_num }">삭제하기</button>
															</div>
														</c:if>
														<a href="javascript:void(0);" class="dev_dplListClick" data-tplNum="${data.tpl_num }"><%--
															<c:choose>
																<c:when test="${data.tpl_div_cd eq 'P' }">
																	<i class="icon_individual">개인템플릿 </i>
																</c:when>
																<c:when test="${data.tpl_div_cd eq 'G' }">
																	<i class="icon_group">상담직원 공유 템플릿</i>
																</c:when>
															</c:choose>--%>
															<c:choose>
																<c:when test="${data.tpl_msg_div_cd eq 'TEXT' }">
																	<i class="icon_T">T</i>
																</c:when>
																<c:when test="${data.tpl_msg_div_cd eq 'NORMAL' }">
																	<i class="icon_N">N</i>
																</c:when>
															</c:choose>
															<div class="text_box">
																<c:if test="${data.top_mark_yn eq 'Y'}"><i class="icon_check_top"></i> </c:if>
																 ${fn:replace(data.cstm_que, newLineChar, '<br/>') }
																
															</div>
														</a>
													</li>
													<%-- 상담직원 답변 영역--%>
													<li class="text" data-tplNum="${data.tpl_num}">
														<div class="right-chat-wrap">
															<div class="bubble-wrap">
																<article class="inner-chat">
																	<div class="bubble-templet type-2">
																		<chat:template jsonContents="${data.reply_cont}" />
																	</div>
																</article>
															</div>
															<div class="tag_area">
																<c:forEach var="keyword" items="${data.templatekwdlist}">
																	<span class="tag">${keyword.kwd_nm}</span>
																</c:forEach>
															</div>
														</div>
													</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
