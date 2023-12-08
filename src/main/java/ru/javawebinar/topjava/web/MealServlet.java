package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Storage storage;
    private final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
            request.setAttribute("listMeals", mealsTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal meal = null;
        switch (action) {
            case "delete" -> {
                storage.delMeal(uuid);
                response.sendRedirect("meals");
                return;
            }
            case "new", "edit" -> {
                if (uuid == null) {
                    meal = new Meal();
                    storage.addMeal(meal);
                } else {
                    meal = storage.getMeal(uuid);
                }
            }
        }

        request.setAttribute("meal", meal);
        request.getRequestDispatcher("modify.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String description = request.getParameter("description");
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"), dateFormater);
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = storage.getMeal(uuid);

        if (meal != null) {
            storage.editMeal(uuid, datetime, description, calories);
        } else {
            storage.addMeal(datetime, description, calories);
        }

        response.sendRedirect("meals");
    }
}

