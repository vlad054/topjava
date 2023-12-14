<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<h3><a href="meals?action=edit">Add</a></h3>

<table border="1">
    <caption>Таблица еды</caption>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${listMeals}" var="meal">
    <c:set var="colorExcess" value="${meal.excess ? '#FF0000': '#008000'}"></c:set>

    <tr style="color: ${colorExcess}">
        <td>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
        </td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
        <td><a href="meals?id=${meal.id}&action=edit">Edit</a></td>
        </c:forEach>
</table>
</body>
</html>