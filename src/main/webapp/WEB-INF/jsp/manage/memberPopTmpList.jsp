<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

				<table class="tCont service">
					<caption>검색한 회원 목록입니다.</caption>
					<colgroup>
						<col style="width:34%">
						<col style="width:33%">
						<col style="width:33%">
					</colgroup>
					<thead>
						<tr>
							<th>부서</th>
							<th>이름</th>
							<th>아이디</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="data" items="${tmpMemberList }" varStatus="status">
							<tr class="dev_tmp_member_tr" data-id="${data.id }" data-name="${data.name }"
										data-departCd="${data.depart_cd }" data-departNm="${data.depart_nm }" data-work-area-nm="${data.work_area_nm}">
								<td>${data.work_area_nm }</td>
								<td>${data.name }</td>
								<td>${data.id }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>