package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );
    public static final List<Meal> mealsOtherUser = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Other Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Other Обед", 1000)
    );

    public static List<MealTo> getTos(MealService service, LocalDate dateStart, LocalDate dateFinish,
                                      LocalTime timeStart, LocalTime timeFinish, int caloriesPerDay) {
        List<Meal> mealsAll = service.getAll(authUserId());

        LocalDate localDateStart = dateStart != null ? dateStart : LocalDate.MIN;
        LocalDate localDateFinish = dateFinish != null ? dateFinish : LocalDate.MAX;
        LocalTime localTimeStart = timeStart != null ? timeStart : LocalTime.MIN;
        LocalTime localTimeFinish = timeFinish != null ? timeFinish : LocalTime.MAX;

        List<Meal> meals = mealsAll.stream()
                    .filter(meal -> (DateTimeUtil.isBetween(meal.getDate(), localDateStart, localDateFinish)
                            && DateTimeUtil.isBetweenHalfOpen(meal.getTime(), localTimeStart, localTimeFinish)))
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        return filterByPredicate(mealsAll, meals, caloriesPerDay, meal -> true);
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> mealsAll, Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {

        Map<LocalDate, Integer> caloriesSumByDate = mealsAll.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
