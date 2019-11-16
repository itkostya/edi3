<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <script src="<c:url value="/resources/js/jquery-3.1.1.js"/>" rel="stylesheet" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/register.js"/>" rel="stylesheet" type="text/javascript"></script>
    <link href="<c:url value="/resources/css/register.css"/>" rel="stylesheet" type="text/css">
</head>
<body>

<table>
    <tr>
        <td>
            <label><b>Email</b></label>
        </td>
        <td>
            <input type="text" placeholder="Enter Email" name="email" id="email" value="itkostya@gmail.com" required>
        </td>
    </tr>
    <tr>
        <td>
            <label><b>Password</b></label>
        </td>
        <td>
            <input type="password" placeholder="Enter Password" name="password" value="12345" id="password" required>
        </td>
    </tr>
    <tr>
        <td>
            <label><b>First Name</b></label>
        </td>
        <td>
            <input type="text" placeholder="Enter First Name" name="firstName" value="Kostya" id="firstName">
        </td>
    </tr>
    <tr>
        <td>
            <label><b>Last Name</b></label>
        </td>
        <td>
            <input type="text" placeholder="Enter Last Name" name="lastName" value="Zhurov" id="lastName">
        </td>
    </tr>
</table>

<div>
    <input type="button" value="Next" onclick="registerUser('${pageContext.request.contextPath}/register')"/>
</div>
<div>
    <label id="info_result" class="warning"></label>
</div>
</body>
</html>
