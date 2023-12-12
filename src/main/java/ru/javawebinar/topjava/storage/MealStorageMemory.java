package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealStorageMemory implements MealStorageInt {

    private Integer curId = 0;
    private List<Meal> meals = new CopyOnWriteArrayList<>();
//    private ConcurrentMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    public MealStorageMemory() {
        Init();
    }

    public void Init() {
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public List<Meal> getAll() {
        return meals;
    }

    public Meal get(Integer id) {
        return meals.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Meal add(Meal meal) {
        int id = getNextId();
        meal.setId(curId);
        meals.add(meal);
        return meal;
    }

    @Override
    public void delete(Integer id) {
        meals.remove(get(id));
        curId = curId - 1;
    }

    @Override
    public Meal edit(Meal ml) {
        delete(ml.getId());
        add(ml);
        return ml;
    }

    private Integer getNextId() {
        synchronized (meals) {
            curId = curId + 1;
            return curId;
        }
    }
}
