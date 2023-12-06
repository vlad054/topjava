<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table >
    <caption>Таблица еды</caption>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:set var="colorExcess" value="#008000"></c:set>
    <c:forEach items="${listMeals}" var="meal">
        <c:if test="${meal.isExcess()}">
            <c:set var="colorExcess" value="#FF0000"></c:set>
        </c:if>

        <tr style="color: ${colorExcess}"><td>${meal.getDateTime().toLocalDate()}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>

    </c:forEach>

<%--    <tr><td>48</td><td>13,5</td><td>49⅓</td><td>31,5</td></tr>--%>
</table>
</body>
</html>