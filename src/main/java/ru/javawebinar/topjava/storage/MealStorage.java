package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    List<Meal> getAll();

    Meal get(int id);

    Meal add(Meal meal);

    void delete(int id);

    Meal edit(Meal meal);

}
