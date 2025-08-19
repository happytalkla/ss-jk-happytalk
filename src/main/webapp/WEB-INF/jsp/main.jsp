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

	hello front web

	<c:forEach var="data" items="${roomStatusList}" varStatus="status">
		<p>
			<span>cd : ${data.cd }</span>
			<span>cd_nm : ${data.cd_nm }</span>
			<span>sim_nm : ${data.sim_nm }</span>
		</p>
	</c:forEach>
</body>

</html>