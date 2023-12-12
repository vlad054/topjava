package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealStorageMemory;
import ru.javawebinar.topjava.storage.MealStorageInt;
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
    private MealStorageInt storage;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MealStorageMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getParameter("action");

        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("listMeals", mealsTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            log.debug("redirect to meals - list of meals");
            return;
        }

        Meal meal = null;
        String strId = request.getParameter("id");
        switch (action) {
            case "delete": {
                storage.delete(Integer.valueOf(strId));
                response.sendRedirect("meals");
                log.debug("redirect to meals - delete meal " + strId);
                return;
            }
            case "edit": {
                if (strId == null) {
                    log.debug("redirect to meals - new meal");
                } else {
                    meal = storage.get(Integer.valueOf(strId));
                    log.debug("redirect to meals - edit meal" + strId);
                }
                request.setAttribute("act", "edit");
            }
            break;
            default:
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), CALORIES_PER_DAY);
                request.setAttribute("listMeals", mealsTo);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                log.debug("redirect to meals - not typical action -  list of meals");
                return;
        }

        request.setAttribute("meal", meal);
        request.getRequestDispatcher("modifyMeal.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String strId = request.getParameter("id");
        String description = request.getParameter("description");
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"), DATE_TIME_FORMATTER);
        int calories = Integer.parseInt(request.getParameter("calories"));

        if (strId.isEmpty()) {
            log.debug("doPost - add to storage");
            storage.add(new Meal(datetime, description, calories));
        } else {
            Meal mealEdit = new Meal(datetime, description, calories);
            mealEdit.setId(Integer.valueOf(strId));
            if (storage.get(Integer.valueOf(strId)) != null) storage.edit(mealEdit);
            log.debug("doPost - edit to storage" + strId);
        }

        response.sendRedirect("meals");
    }
}

