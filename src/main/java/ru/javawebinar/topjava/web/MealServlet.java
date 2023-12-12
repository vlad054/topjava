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
    private final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

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
        String strId = request.getParameter("id");

        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(storage.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), CALORIES_PER_DAY);
            request.setAttribute("listMeals", mealsTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal meal = null;
        switch (action) {
            case "delete": {
                storage.delete(Integer.valueOf(strId));
                response.sendRedirect("meals");
                return;
            }
            case "edit":
            case "new": {
                if (strId == null) {
                    request.setAttribute("act", "new");
                } else {
                    meal = storage.get(Integer.valueOf(strId));
                    request.setAttribute("act", "edit");
                }
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }

        request.setAttribute("meal", meal);
        request.getRequestDispatcher("modify.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String strId = request.getParameter("id");
        String description = request.getParameter("description");
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("datetime"), dateFormater);
        int calories = Integer.parseInt(request.getParameter("calories"));

        if (strId.isEmpty()){
            storage.add(new Meal(datetime, description, calories));
        } else {
            Meal mealEdit = new Meal(datetime, description, calories);
            mealEdit.setId(Integer.valueOf(strId));
            if (storage.get(Integer.valueOf(strId)) != null) storage.edit(mealEdit);
        }

        response.sendRedirect("meals");
    }
}

