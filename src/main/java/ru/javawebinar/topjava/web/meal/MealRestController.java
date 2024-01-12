package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service, null, null, null, null, SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public Meal update(Meal meal, int id) {
        log.info("update {} with userid={}", meal, authUserId());
        assureIdConsistent(meal, id);
        return service.update(meal, authUserId());
    }

    public Meal save(Meal meal) {
        log.info("save {}", meal);
        if (meal.getId() == null) {
            return create(meal);
        }
        return update(meal, meal.getId());
    }

    public List<MealTo> getAllByFilter(LocalDate dateStart, LocalDate dateFinish, LocalTime timeStart, LocalTime timeFinish) {
        log.info("getAllByFilter");
        return MealsUtil.getTos(service,
                dateStart, dateFinish, timeStart, timeFinish,
                SecurityUtil.authUserCaloriesPerDay());
    }
}