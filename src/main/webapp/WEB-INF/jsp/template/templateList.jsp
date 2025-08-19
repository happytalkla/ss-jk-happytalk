<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

								<c:choose>
									<c:when test="${empty templateList }">
										<p class="no_data">목록이 없습니다.</p>
									</c:when>
									<c:otherwise>
										<div id="chat_body">
											<ul class="chat_temp_list">
												<c:forEach var="data" items="${templateList }" varStatus="status">
													<li>
														<!-- 고객 챗 영역:기본텍스트 -->
														<div class="left_area">
															<i class="icon_bot counselor">고객</i>
															<div class="top_write_info">
																<span>ID : ${data.tpl_num }</span> 
																<span>등록일 : ${data.create_dt }</span>
																<span>등록자 : ${data.member_nm }</span>
																
															</div>
															<div class="text_box">
																${data.cstm_que }
															</div>
														</div>
														<!--// 고객 챗 영역:기본텍스트 -->
				
														<!-- 상담직원 챗 영역 -->
														<div class="right-chat-wrap">
															<c:forEach var="data2" items="${data.templateMsgList }" varStatus="status">
																<div class="text_box">
																	${data2.tpl_msg }
																</div>
															</c:forEach>
															<c:forEach var="data3" items="${data.templateKwdList }" varStatus="status">
																<div class="tag_area">
																	<span class="tag">${data3.kwd_nm }</span>
																</div>
															</c:forEach>
														</div>
														<!--// 상담직원 챗 영역 -->
				
														<div class="btn_temp_area">
															<button type="button" class="btn_del_temp"><i>삭제</i></button>
															<button type="button" class="btn_write_temp"><i>쓰기</i></button>
														</div>									
													</li>
												</c:forEach>
											</ul>
										</div>
									</c:otherwise>
								</c:choose>
