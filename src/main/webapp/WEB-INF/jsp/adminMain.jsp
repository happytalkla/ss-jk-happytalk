<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<title>happy talk</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<script type="text/javascript">

	</script>
</head>

<body>

	<p>hello admin web</p>

	<p><a href="<c:url value='/manage/workManage' />">휴무일 관리/근무관리</a></p>

	<p><a href="<c:url value='/set/selectSet' />">기본 설정 정보 조회</a></p>

	<p><a href="<c:url value='/set/selectMessage' />">메세지 설정 정보 조회</a></p>

</body>

</html>