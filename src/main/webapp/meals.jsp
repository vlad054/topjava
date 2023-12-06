<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<h3><a href="modify?action=new">Add</a></h3>

<table border="1" >
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
            <td><a href="modify?uuid=${meal.getUUID()}&action=delete">Delete</a></td>
            <td><a href="modify?uuid=${meal.getUUID()}&action=edit">Edit</a></td>
        </c:forEach>
</table>
</body>
</html>