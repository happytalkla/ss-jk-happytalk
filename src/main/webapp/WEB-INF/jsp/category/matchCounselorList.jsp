<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

							<c:forEach var="data" items="${counselorList }" varStatus="status">
								<tr>
									<td>
										<input type="checkbox" class="checkbox_18 notext" name="memberUidArr" value="${data.member_uid }" id="match_yn_${status.index }" <c:if test="${data.match_yn eq 'Y' }">checked</c:if>>
										<label for="match_yn_${status.index }"><span></span></label>
									</td>
									<td>${data.depart_nm}</td>
									<td>${data.manager_nm }</td>
									<td><a href="javascript:void(0);" class="link_text dev_click_counselor">${data.member_nm }</a></td>
									<td>${data.member_div_nm }</td>
								</tr>
							</c:forEach>