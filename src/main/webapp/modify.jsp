<%--<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>--%>
<%--
  Created by IntelliJ IDEA.
  User: mva
  Date: 07.12.2023
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
<%--    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>--%>
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


<%--        <h3>Контакты:</h3>--%>
<%--        <c:forEach var="type" items="<%=ContactType.values()%>">--%>
<%--            <dl>--%>
<%--                <dt>${type.title}</dt>--%>
<%--                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContacts().get(type)}"></dd>--%>
<%--            </dl>--%>
<%--        </c:forEach>--%>

<%--        <h3>Секции:</h3>--%>
<%--        <c:forEach var="type" items="<%=SectionType.values()%>">--%>
<%--            <c:if test="${type.name() != 'EXPERIENCE' && type.name() != 'EDUCATION'}">--%>
<%--                <dl>--%>
<%--                <dt><b>${type.title}</b></dt>--%>
<%--                <br>--%>
<%--                <dd>--%>
<%--                    <label>--%>
<%--                        <textarea--%>
<%--                                cols="100" rows="10" name="${type.name()}" >${resume.getSections().get(type)}</textarea>--%>
<%--                    </label>--%>
<%--                </dd>--%>
<%--            </c:if>--%>

<%--            <c:if test="${type.name() == 'EXPERIENCE'}">--%>
<%--                <dt><b>${type.title}</b></dt>--%>
<%--                <br>--%>
<%--                <dd>--%>
<%--                <c:set var="sect" value="${resume.getSections().get(type)}" />--%>
<%--                <c:forEach var="company" items="${sect.getPositions()}">--%>
<%--                    <c:forEach var="period" items="${company.getPeriods()}">--%>
<%--                        <dd><label> Название компании <input type="text" title="Company Name" name="companyPNameX" value="${company.getName()}" required size=50></label></dd> <br>--%>
<%--                        <dd><label> Ссылка <input type="text" title="Company Web" name="companyPWebX" value="${company.getWebSite()}" size=50></label></dd> <br>--%>
<%--                        <dd><label> Позиция <input type="text" title="Period Name" name="periodNameX" value="${period.getName()}" required size=50></label></dd> <br>--%>
<%--                        <dd><label> Нач. <input type="text" placeholder="yyyy-mm-dd" title="Period start Date yyyy-mm-dd" name="periodStartDateX" value="${period.getStartDate()}" required size=10></label> /--%>
<%--                            <label> Кон. <input type="text" placeholder="yyyy-mm-dd" title="Period end Date yyyy-mm-dd" name="periodEndDateX" value="${empty period.getEndDate()?'Сейчас': period.getEndDate()}" size=10></label></dd> <br>--%>
<%--                        <dd><label title="Section Descripion"> Описание--%>
<%--                            <textarea--%>
<%--                                    cols="80" rows="5" name="periodDescX" >${period.getDescription()}</textarea>--%>
<%--                        </label></dd>--%>
<%--                        <br>--%>
<%--                    </c:forEach>--%>

<%--                    <br>--%>
<%--                </c:forEach>--%>

<%--                <dt><b>Новая секция - Опыт</b></dt>--%>
<%--                <dd><label> Название компании <input type="text" title="Company Name" name="companyPNameX" size=50></label></dd> <br>--%>
<%--                <dd><label> Ссылка <input type="text" title="Company Web" name="companyPWebX"  size=50></label></dd> <br>--%>
<%--                <dd><label> Позиция <input type="text" title="Period Name" name="periodNameX"  size=50></label></dd> <br>--%>
<%--                <dd><label> Нач. <input type="text" placeholder="yyyy-mm-dd" title="Period start Date yyyy-mm-dd" name="periodStartDateX"  size=10></label> /--%>
<%--                    <label> Кон. <input type="text" placeholder="yyyy-mm-dd" title="Period end Date yyyy-mm-dd" name="periodEndDateX"  size=10></label></dd> <br>--%>
<%--                <dd><label title="Section Descripion"> Описание--%>
<%--                    <textarea--%>
<%--                            cols="80" rows="5" name="periodDescX" ></textarea>--%>
<%--                </label></dd>--%>
<%--                <br>--%>
<%--                </dd>--%>
<%--            </c:if>--%>

<%--            <c:if test="${type.name() == 'EDUCATION'}">--%>
<%--                <dt><b>${type.title}</b></dt>--%>
<%--                <br>--%>
<%--                <dd>--%>
<%--                <c:set var="sect" value="${resume.getSections().get(type)}" />--%>
<%--                <c:forEach var="company" items="${sect.getPositions()}">--%>
<%--                    <c:forEach var="period" items="${company.getPeriods()}">--%>
<%--                        <dd><label> Название компании <input type="text" title="Company Name" name="companyPNameE" value="${company.getName()}" required size=50></label></dd> <br>--%>
<%--                        <dd><label> Ссылка <input type="text" title="Company Web" name="companyPWebE" value="${company.getWebSite()}" size=50></label></dd> <br>--%>
<%--                        <dd><label> Позиция <input type="text" title="Period Name" name="periodNameE" value="${period.getName()}" required size=50></label></dd> <br>--%>
<%--                        <dd><label> Нач. <input type="text" placeholder="yyyy-mm-dd" title="Period start Date yyyy-mm-dd" name="periodStartDateE" value="${period.getStartDate()}" required size=10></label> /--%>
<%--                            <label> Кон. <input type="text" placeholder="yyyy-mm-dd" title="Period end Date yyyy-mm-dd" name="periodEndDateE" value="${empty period.getEndDate()?'Сейчас': period.getEndDate()}" size=10></label></dd> <br>--%>
<%--                        <br>--%>
<%--                    </c:forEach>--%>
<%--                    <br>--%>
<%--                </c:forEach>--%>
<%--                </dd>--%>

<%--                <dt><b>Новая секция - Образование</b></dt>--%>
<%--                <dd><label> Название компании <input type="text" title="Company Name" name="companyPNameE" size=50></label></dd> <br>--%>
<%--                <dd><label> Ссылка <input type="text" title="Company Web" name="companyPWebE"  size=50></label></dd> <br>--%>
<%--                <dd><label> Позиция <input type="text" title="Period Name" name="periodNameE"  size=50></label></dd> <br>--%>
<%--                <dd><label> Нач. <input type="text" placeholder="yyyy-mm-dd" title="Period start Date yyyy-mm-dd" name="periodStartDateE"  size=10></label> /--%>
<%--                    <label> Кон. <input type="text" placeholder="yyyy-mm-dd" title="Period end Date yyyy-mm-dd" name="periodEndDateE"  size=10></label></dd> <br>--%>
<%--                <br>--%>

<%--            </c:if>--%>
<%--            </dl>--%>
<%--        </c:forEach>--%>
<%--        <br>--%>
<%--        <hr>--%>
<%--        <button type="submit">Сохранить</button>--%>
<%--        <button type="button" onclick="window.history.back()">Отменить</button>--%>
    </form>
</section>
</body>
</html>
