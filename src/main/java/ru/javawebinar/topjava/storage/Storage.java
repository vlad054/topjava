package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface Storage {
    public List<Meal> getMeals();
    public Meal getMeal(String uuid);
    public void addMeal(LocalDateTime dateTime, String description, int calories);
    public void delMeal(String uuid);
    public void editMeal(String uuid, LocalDateTime dateTime, String description, int calories);

}
