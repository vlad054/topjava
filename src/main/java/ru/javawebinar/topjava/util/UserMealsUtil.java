package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();

        for (UserMeal ue : meals) {
            map.merge(ue.getDateTime().toLocalDate(), ue.getCalories(), Integer::sum);
        }

        for (UserMeal ue : meals) {
            if (!startTime.isAfter(ue.getDateTime().toLocalTime())
                    && !endTime.isBefore(ue.getDateTime().toLocalTime())) {
                userMealWithExcessList.add(new UserMealWithExcess(ue.getDateTime(),
                        ue.getDescription(),
                        ue.getCalories(),
                        map.get(ue.getDateTime().toLocalDate()) > caloriesPerDay
                ));
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, Integer> map = meals.stream().
                collect(Collectors.groupingBy(c -> c.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream().filter(c -> !startTime.isAfter(c.getDateTime().toLocalTime())
                        && !endTime.isBefore(c.getDateTime().toLocalTime()))
                .map(c -> new UserMealWithExcess(c.getDateTime(),
                        c.getDescription(),
                        c.getCalories(),
                        map.get(c.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

    }
}
