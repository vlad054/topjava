package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MealStorage implements Storage{

    public final int caloriesPerDay = 2000;

    private List<Meal> meals = new ArrayList<>();

    public MealStorage(){
        meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public Meal getMeal(String uuid){
        return meals.stream()
                .filter(m -> m.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addMeal(LocalDateTime dateTime, String description, int calories) {
        meals.add(new Meal(dateTime, description, calories));
    }

    @Override
    public void delMeal(String uuid) {
        meals.removeIf(m -> m.getUuid().equals(uuid));
    }

    @Override
    public void editMeal(String uuid, LocalDateTime dateTime, String description, int calories) {
        Meal meal = getMeal(uuid);
        meal.setCalories(calories);
        meal.setDateTime(dateTime);
        meal.setDescription(description);
    }
}
