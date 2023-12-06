package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");


        MealStorage mealStorage = new MealStorage();
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealStorage.getMeals(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("listMeals", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
//        response.sendRedirect("meals.jsp");
    }
}
