<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<c:set var="hact" value="${meal eq null ? 'Add meal': 'Edit meal'}"></c:set>
<h3>
    ${hact}
</h3>

<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime: </dt>
            <dd><input type="datetime-local" placeholder="dd-MM-yyyy HH:mm" name="datetime" size=50 value="${meal.dateTime}"></dd>
        </dl>

        <dl>
            <dt>Description:</dt>
            <dd><input type="text" name="description" size=30 value="${meal.description}"></dd>
        </dl>

        <dl>
            <dt>Calories:</dt>
            <dd><input type="number" name="calories" size=30 value="${meal.calories}"></dd>
        </dl>

        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
