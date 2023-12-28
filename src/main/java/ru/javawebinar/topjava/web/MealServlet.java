package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletContext;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        log.info("Create context ");
        try {
            appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
            log.info("Create mealRestController");
            mealRestController = appCtx.getBean(MealRestController.class);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        meal.setId(id.isEmpty() ? null : Integer.valueOf(id));

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
                LocalDate dateStart = request.getParameter("dateStart").isEmpty() ? null : LocalDate.parse(request.getParameter("dateStart"));
                LocalDate dateFinish = request.getParameter("dateFinish").isEmpty() ? null : LocalDate.parse(request.getParameter("dateFinish"));
                LocalTime timeStart = request.getParameter("timeStart").isEmpty() ? null : LocalTime.parse(request.getParameter("timeStart"));
                LocalTime timeFinish = request.getParameter("timeFinish").isEmpty() ? null : LocalTime.parse(request.getParameter("timeFinish"));

                setDateAttr(session, dateStart, "dateStart");
                setDateAttr(session, dateFinish, "dateFinish");
                setTimeAttr(session, timeStart, "timeStart");
                setTimeAttr(session, timeFinish, "timeFinish");

                request.setAttribute("meals",
                        MealsUtil.getTos(mealRestController.getAll(),
                                mealRestController.getAllByFilter(dateStart, dateFinish, timeStart, timeFinish),
                                SecurityUtil.authUserCaloriesPerDay()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        MealsUtil.getTos(mealRestController.getAll()
                                , mealRestController.getAll()
                                , SecurityUtil.authUserCaloriesPerDay()));
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

    private void setDateAttr(HttpSession session, LocalDate date, String name){
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

