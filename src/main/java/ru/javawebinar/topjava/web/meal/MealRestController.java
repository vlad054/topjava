package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<Meal> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
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

    public Meal update(Meal meal) {
        log.info("update {} with userid={}", meal, authUserId());
        assureIdConsistent(meal, meal.getId());
        return service.update(meal, authUserId());
    }

    public Meal save(Meal meal) {
        log.info("save {}", meal);
        if (meal.getId() == null) {
            return create(meal);
        }
        return update(meal);
    }

    public List<Meal> getAllByFilter(LocalDate dateStart, LocalDate dateFinish, LocalTime timeStart, LocalTime timeFinish) {
        log.info("getAllByFilter");
        LocalDate localDateStart = dateStart != null ? dateStart : LocalDate.MIN;
        LocalDate localDateFinish = dateFinish != null ? dateFinish : LocalDate.MAX;
        LocalTime localTimeStart = timeStart != null ? timeStart : LocalTime.MIN;
        LocalTime localTimeFinish = timeFinish != null ? timeFinish : LocalTime.MAX;

        return service.getAll(authUserId()).stream()
                .filter(meal -> (DateTimeUtil.isBetween(meal.getDate(), localDateStart, localDateFinish)
                        && DateTimeUtil.isBetweenHalfOpen(meal.getTime(), localTimeStart, localTimeFinish)))
                .collect(Collectors.toList());
    }
}