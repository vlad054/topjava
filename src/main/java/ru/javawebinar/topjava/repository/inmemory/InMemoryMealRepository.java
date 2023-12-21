package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repositoryMeal = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> repositoryMealToUser = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repositoryMeal.put(meal.getId(), meal);
            repositoryMealToUser.put(meal.getId(), userId);
            return meal;
        }
        // handle case: update, but not present in storage
        if (Objects.equals(repositoryMealToUser.get(meal.getId()), userId)){
            return repositoryMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        if (Objects.equals(repositoryMealToUser.get(id), userId)){
            repositoryMealToUser.remove(id);
            return repositoryMeal.remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        if (Objects.equals(repositoryMealToUser.get(id), userId)) {
            return repositoryMeal.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repositoryMeal.values().stream()
                .filter(meal -> Objects.equals(repositoryMealToUser.get(meal.getId()), userId))
                .sorted(Comparator.comparing(Meal::getDate))
                .collect(Collectors.toList());
    }
}

