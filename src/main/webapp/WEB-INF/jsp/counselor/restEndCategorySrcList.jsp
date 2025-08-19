<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
<c:when test="${empty categoryList}"><option>검색결과가 없습니다.</option></c:when>
<c:otherwise><option>아래목록에서 선택하세요</option>
<c:forEach items="${categoryList}" var="category"><option value="${category.depth1_cd}|${category.depth2_cd}|${category.depth3_cd}|${category.depth4_cd}" 
data-ctg1-cd="${category.depth1_cd}" data-ctg2-cd="${category.depth2_cd}" data-ctg3-cd="${category.depth3_cd}" data-ctg4-cd="${category.depth4_cd}"
data-ctg1-nm="${category.depth1_cd_nm}" data-ctg2-nm="${category.depth2_cd_nm}" data-ctg3-nm="${category.depth3_cd_nm}" data-ctg4-nm="${category.depth4_cd_nm}">
${category.depth1_cd_nm} > ${category.depth2_cd_nm} > ${category.depth3_cd_nm} > ${category.depth4_cd_nm}
</option>
</c:forEach></c:otherwise>	
</c:choose>
