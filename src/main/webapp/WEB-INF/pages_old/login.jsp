<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <%--<link href="<c:url value="/layout/css/login.css"/>" rel="stylesheet" type="text/css">--%>
    <%--<link href="<c:url value="/layout/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css">--%>
</head>
<body>
<div class="container">
    <form class="form-login" action="${pageContext.request.contextPath}/login" method="post">
        
        <h2 class="form-signin-heading">Please sign in</h2>

        <input class="form-control" id="inputLogin" type="text" placeholder="Enter username or email" name="login"
               required>

        <input class="form-control" id="inputPassword" type="password" placeholder="Enter password" name="pass"
               required>
        
        <div class="checkbox" style="display: none">
            <label>
                <input type="checkbox" value="remember-me"> Remember me
            </label>
        </div>
        
        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>

    </form>
</div>
</body>
</html>
