package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorageInt {
    List<Meal> getAll();

    Meal get(Integer id);

    Meal add(Meal meal);

    void delete(Integer id);

    Meal edit(Meal meal);

}
