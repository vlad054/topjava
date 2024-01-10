package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repositoryMealToUser = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.mealsOtherUser.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repositoryMealToUser.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        } else {
            return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repositoryMealToUser.containsKey(userId)) {
            return repositoryMealToUser.get(userId).remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (repositoryMealToUser.containsKey(userId)) {
            return repositoryMealToUser.get(userId).get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        if (repositoryMealToUser.containsKey(userId)) {
            return repositoryMealToUser.get(userId).values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Meal> getAllByFilter(int userId) {
        if (repositoryMealToUser.containsKey(userId)) {
            return repositoryMealToUser.get(userId).values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Meal> getAllByFilter(int userId, LocalDate dateStart, LocalDate dateFinish, LocalTime timeStart, LocalTime timeFinish) {
        LocalDate localDateStart = dateStart != null ? dateStart : LocalDate.MIN;
        LocalDate localDateFinish = dateFinish != null ? dateFinish : LocalDate.MAX;
        LocalTime localTimeStart = timeStart != null ? timeStart : LocalTime.MIN;
        LocalTime localTimeFinish = timeFinish != null ? timeFinish : LocalTime.MAX;

        if (repositoryMealToUser.containsKey(userId)) {
            return repositoryMealToUser.get(userId).values().stream()
                    .filter(meal -> (DateTimeUtil.isBetween(meal.getDate(), localDateStart, localDateFinish)
                            && DateTimeUtil.isBetweenHalfOpen(meal.getTime(), localTimeStart, localTimeFinish)))
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return null;
    }

}

