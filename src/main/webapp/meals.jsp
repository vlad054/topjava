<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>

    <form method="get" action="meals">
        <input type="Hidden" name="action" value="filter">
        <table border="1" cellpadding="8" cellspacing="0">
            <thead>
            <tr>
                <th>Date Start:</th>
                <th>Date Finish:</th>
                <th>Time Start:</th>
                <th>Time Finish:</th>
            </tr>
            </thead>
            <tr>
                <fmt:formatDate pattern="yyyy-MM-dd" value="${sessionScope.dateStart}" var="startSessionDate"/>
                <fmt:formatDate pattern="yyyy-MM-dd" value="${sessionScope.dateFinish}" var="finishSessionDate"/>
                <fmt:formatDate pattern="HH:mm" value="${sessionScope.timeStart}" var="startSessionTime"/>
                <fmt:formatDate pattern="HH:mm" value="${sessionScope.timeFinish}" var="finishSessionTime"/>
                <td><input type="date" value="${startSessionDate}" name="dateStart"></td>
                <td><input type="date" value="${finishSessionDate}" name="dateFinish"></td>
                <td><input type="time" value="${startSessionTime}" name="timeStart"></td>
                <td><input type="time" value="${finishSessionTime}" name="timeFinish"></td>
            </tr>
        </table>
        <button type="submit">Filter</button>
    </form>

    <br><br>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>