<%--
  Created by IntelliJ IDEA.
  User: kostya
  Date: 10/3/2016
  Time: 5:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create data</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/createdata" method="post">
        <button id = "ok" type="submit">Create countries, cities, streets</button>
    </form>
</body>
</html>
