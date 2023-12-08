<%--<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>--%>
<%--
  Created by IntelliJ IDEA.
  User: mva
  Date: 07.12.2023
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>

<section>
    <form method="post" action="modify" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${meal.getUuid()}">
        <dl>
            <dt>DateTime: </dt>
            <dd><input type="text" placeholder="dd-MM-yyyy HH:mm" name="datetime" size=50 value="${meal.getFormatedStringDate()}"></dd>
        </dl>

        <dl>
            <dt>Description:</dt>
            <dd><input type="text" name="description" size=30 value="${meal.getDescription()}"></dd>
        </dl>

        <dl>
            <dt>Calories:</dt>
            <dd><input type="text" name="calories" size=30 value="${meal.getCalories()}"></dd>
        </dl>

        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
