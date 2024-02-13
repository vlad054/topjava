package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 3;

    public static final Meal meal1 = new Meal(MEAL_ID, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(MEAL_ID + 1, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 = new Meal(MEAL_ID + 2, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 = new Meal(MEAL_ID + 3, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal5 = new Meal(MEAL_ID + 4, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal6 = new Meal(MEAL_ID + 5, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед", 500);
    public static final Meal meal7 = new Meal(MEAL_ID + 6, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин", 410);

    public static Meal getUpdated() {
        Meal updated = new Meal();
        updated.setDateTime(LocalDateTime.of(2020, 1, 31, 21, 0));
        updated.setId(meal7.getId());
        updated.setCalories(200);
        updated.setDescription("New description");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }

}
