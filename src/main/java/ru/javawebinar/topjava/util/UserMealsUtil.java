package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 450),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 11, 0), "Завтрак1", 5),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин2", 310)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println("------");
        List<UserMealWithExcess> mealsToStream = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToStream.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumCalByDays = new HashMap<>();
        for (UserMeal ue : meals) {
            sumCalByDays.merge(ue.getDateTime().toLocalDate(), ue.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
            {
                userMealWithExcessList.add(new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        sumCalByDays.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay
                ));
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = meals.stream()
                .collect(Collectors.groupingBy(c -> c.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(m -> !startTime.isAfter(m.getDateTime().toLocalTime())
                        && !endTime.isBefore(m.getDateTime().toLocalTime()))
                .map(um -> new UserMealWithExcess(um.getDateTime(),
                        um.getDescription(),
                        um.getCalories(),
                        map.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
