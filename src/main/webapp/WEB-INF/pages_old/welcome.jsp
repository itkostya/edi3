<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <script src="<c:url value="https://code.jquery.com/jquery-3.1.1.min.js"/>" rel="stylesheet" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/welcome.js"/>" rel="stylesheet" type="text/javascript"></script>
</head>
<body>
<h1>Spring MVC Hello World Example</h1>

<h2>${msg}</h2>
<button onclick="renderUser((${pageContext.request.contextPath}+'/register/getUserByEmail'))">Get User</button>

<div id="userData">

</div>
</body>
</html>
