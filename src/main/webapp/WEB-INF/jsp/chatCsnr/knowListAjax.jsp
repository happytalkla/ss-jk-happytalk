<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

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
