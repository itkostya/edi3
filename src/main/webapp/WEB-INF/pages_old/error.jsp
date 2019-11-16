<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>

<h1>${msg}</h1>

<form action="${pageContext.request.contextPath}/welcome" method="get">
    <button type="submit">Go to main page</button>
</form>

</body>
</html>
