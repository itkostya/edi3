<%@ page import="com.levelup.core.entity.Gender" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%--<script src="<c:url value="https://code.jquery.com/jquery-3.1.1.min.js"/>" rel="stylesheet" type="text/javascript"></script>--%>
    <script src="<c:url value="/resources/js/jquery-3.1.1.js"/>" rel="stylesheet" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/register.js"/>" rel="stylesheet" type="text/javascript"></script>
    <link href="<c:url value="/resources/css/register.css"/>" rel="stylesheet" type="text/css">
</head>
<body>

<form action="${pageContext.request.contextPath}/register" method="post">

    <div>
        <label><b>Gender</b></label>
        <select name="gender" id="gender" required>
            <c:forEach var="cell" items="<%=Gender.values()%>">
                <option>${cell.name()}</option>
            </c:forEach>
        </select>
    </div>
    <div>
        <fieldset class="recipients">
            <legend>Address:</legend>

            <table>
                <tr>
                    <th>
                        Country
                    </th>
                    <td>
                        <select name="country" id="country" onchange="changeGeography('${pageContext.request.contextPath}/register/chousencountry', this.selectedIndex, 'country')">
                            <option value="-1"></option>
                            <c:forEach var="cell" items="${countryList}">
                                <option value="${cell.id}">${cell.name}</option>
                            </c:forEach>
                        </select>
                    </td>

                </tr>

                <tr>
                    <th>
                        City
                    </th>
                    <td>
                        <select name="city" id="city" disabled="disabled">
                            <option value="-1"></option>
                            <c:forEach var="cell" items="${cityList}">
                                <option value="${cell.id}">${cell.name}</option>
                            </c:forEach>
                        </select>
                    </td>

                </tr>

                <tr>
                    <th>
                        Street
                    </th>
                    <td>
                        <select name="street" id="street" disabled="disabled">
                        </select>
                    </td>

                </tr>

                <tr>
                    <th>
                        House
                    </th>
                    <td>
                        <select name="house" id="house" disabled="disabled">
                        </select>
                    </td>

                </tr>

            </table>

        </fieldset>

    </div>

</form>

<div>
    <label><b>Phone number</b></label>
    <input type="text" placeholder="Enter phone number" name="phonenumber" value="068-123-12-12" id="phonenumber"
           required>
</div>

<div><input type="button" value="Register details"
       onclick="registerDetails('${pageContext.request.contextPath}/register/details')"/></div>

<div><label id="info_result" class="warning"></label></div>

</body>
</html>
