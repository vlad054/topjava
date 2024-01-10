package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        log.info("Create context ");
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        log.info("Create mealRestController");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Integer idInt = id.isEmpty() ? null : Integer.valueOf(id);
        Meal meal = new Meal(idInt,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealRestController.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                log.info("Filter ");
                String stringDateStart = request.getParameter("dateStart");
                String stringDateFinish = request.getParameter("dateFinish");
                String stringTimeStart = request.getParameter("timeStart");
                String stringTimeFinish = request.getParameter("timeFinish");

                LocalDate dateStart = stringDateStart.isEmpty() ? null : LocalDate.parse(stringDateStart);
                LocalDate dateFinish = stringDateFinish.isEmpty() ? null : LocalDate.parse(stringDateFinish);
                LocalTime timeStart = stringTimeStart.isEmpty() ? null : LocalTime.parse(stringTimeStart);
                LocalTime timeFinish = stringTimeFinish.isEmpty() ? null : LocalTime.parse(stringTimeFinish);

                setDateAttr(session, dateStart, "dateStart");
                setDateAttr(session, dateFinish, "dateFinish");
                setTimeAttr(session, timeStart, "timeStart");
                setTimeAttr(session, timeFinish, "timeFinish");

                List<MealTo> mealsTo = mealRestController.getAllByFilter(dateStart, dateFinish, timeStart, timeFinish);

                request.setAttribute("meals",
                        mealsTo);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                List<MealTo> mealsTo1 = mealRestController.getAll();
                request.setAttribute("meals",
                        mealsTo1);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    private void setDateAttr(HttpSession session, LocalDate date, String name) {
        if (date != null) {
            session.setAttribute(name, Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            session.setAttribute(name, null);
        }
    }

    private void setTimeAttr(HttpSession session, LocalTime time, String name) {
        if (time != null) {
            session.setAttribute(name, Time.valueOf(time));
        } else {
            session.setAttribute(name, null);
        }
    }
}

