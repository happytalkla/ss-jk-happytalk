<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

				<h2 class="notice_tit">
					공지사항
					<!-- <button type="button" class="btn_view_helf">도움말</button>  -->
				</h2>	
				<div class="notice_list_area">
					<ul class="notice_list">
						<c:forEach var="data" items="${noticeList }" varStatus="status">					
							<li>
								<div class="notice_head">
									<span class="notice_date">${data.reg_dt }</span>
									${data.title }
								</div>
								<div class="notice_body">
									${data.cont.replaceAll("&lt;", "<").replaceAll("&gt;", ">")}
									
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>