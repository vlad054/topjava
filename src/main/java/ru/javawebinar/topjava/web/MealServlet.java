package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MemoryMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    public static final int CALORIES_PER_DAY = 2000;

    private MealStorage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MemoryMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getParameter("action");

        String strId = request.getParameter("id");
        switch (String.valueOf(action)) {
            case "delete": {
                log.debug("redirect to meals - delete meal {}", strId);
                storage.delete(Integer.parseInt(strId));
                response.sendRedirect("meals");
                return;
            }
            case "edit": {
                Meal meal = null;
                if (strId == null) {
                    log.debug("redirect to meals - new meal");
                } else {
                    log.debug("redirect to meals - edit meal {}", strId);
                    meal = storage.get(Integer.parseInt(strId));
                }
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("modifyMeal.jsp")
                        .forward(request, response);
            }
            break;
            default:
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
                request.setAttribute("listMeals", mealsTo);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                log.debug("redirect to meals - list of meals");
                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String strId = request.getParameter("id");
        String description = request.getParameter("description");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        int calories = Integer.parseInt(request.getParameter("calories"));

        if (strId.isEmpty()) {
            log.debug("doPost - add to storage");
            storage.add(new Meal(dateTime, description, calories));
        } else {
            Meal mealEdit = new Meal(dateTime, description, calories);
            mealEdit.setId(Integer.valueOf(strId));
            log.debug("doPost - edit to storage {}", strId);
            storage.edit(mealEdit);
        }

        response.sendRedirect("meals");
    }
}

