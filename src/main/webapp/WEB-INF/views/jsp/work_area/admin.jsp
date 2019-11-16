<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@page import="com.edi3.web.tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="isDatabaseEmpty" type="boolean">"--%>
<%--@elvariable id="newUsersCount" type="java.lang.Integer"--%>

<html>

<head>
    <title>Welcome</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/work_area/work_area_main.css"/>" rel="stylesheet" type="text/css">
</head>

<body>

<script>

    window.onload = function () {

        <c:set value="${newUsersCount!=null && newUsersCount!=0}" var="isExistNewUsersCount"/>

    };
</script>

<div class="container form-welcome">

    <h2 class="form-welcome-heading">Administration Panel</h2>

    <form method="post" action="${pageContext.request.contextPath}/admin">

    <c:choose>
        <c:when test="${isDatabaseEmpty}">
            <div>
                <button name="param" value= "1" style="color:red"> Create categories (departments, positions, users, contractors, cost items, currencies, legal organizations, planning periods)</button>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <table>
                    <tr>
                        <td>
                            <div class="relative-with-border" id="div-category-user">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_USER_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/User.png"
                                             alt="Пользователи" class="button_dark_green"></a>
                                </div>
                                <div class="number_in_circle" ${isExistNewUsersCount ? '' : 'style="background: none;"'}
                                     onclick="window.alert('Количество новых пользователей');">
                                        ${isExistNewUsersCount ? newUsersCount : ''} </div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-position">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_POSITION_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/Position.png"
                                             alt="Должности" class="button_dark_green"></a>
                                </div>
                                <div class="number_in_circle" style="background: none; height: 24px; width: 24px; top: -71px; left: -1px; border-radius: 24px; font-size: medium;"></div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-department">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/Department.png"
                                             alt="Департаменты" class="button_dark_green"></a>
                                </div>
                                <div class="number_in_circle" style="background: none; height: 24px; width: 24px; top: -71px; left: -1px; border-radius: 24px; font-size: medium;"></div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-legal-organization">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/LegalOrganization.png"
                                             alt="Юридические лица" class="button_dark_green"></a>
                                </div>
                                <div class="number_in_circle" style="background: none; height: 24px; width: 24px; top: -71px; left: -1px; border-radius: 24px; font-size: medium;"></div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-contractor">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_CONTRACTOR_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/Contractor.png"
                                             alt="Контрагенты" class="button_dark_green"></a>
                                </div>
                                <div class="number_in_circle" style="background: none; height: 24px; width: 24px; top: -71px; left: -1px; border-radius: 24px; font-size: medium;"></div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-currency">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_CURRENCY_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/Currency.png"
                                             alt="Валюты" class="button_dark_green"></a>
                                </div>
                                <div class="number_in_circle" style="background: none; height: 24px; width: 24px; top: -71px; left: -1px; border-radius: 24px; font-size: medium;"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="relative-with-border" id="div-category-cost-item">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_COST_ITEM_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/CostItem.png"
                                             alt="Статьи затрат" class="button_dark_green"></a>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-planning-period">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/PlanningPeriod.png"
                                             alt="Периоды планирования" class="button_dark_green"></a>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-category-proposal-template">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/work_area/ProposalTemplate.png"
                                             alt="Шаблоны заявок" class="button_golden_rod"></a>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="relative-with-border" id="div-data-processors-set-roles">
                                <div>
                                    <a href='${pageContext.request.contextPath}${PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE}' target="_blank">
                                        <img src="${pageContext.request.contextPath}/resources/images/data_processors/SetRights.png"
                                             alt="Установка ролей пользователей" class="button_red"></a>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>

            </div>

        </c:otherwise>
    </c:choose>

    </form>

</div>
</body>

</html>
