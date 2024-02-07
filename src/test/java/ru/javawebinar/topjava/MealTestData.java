package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int Meal_ID = START_SEQ;


    public static final Meal meal301 = new Meal(Meal_ID + 3, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal302 = new Meal(Meal_ID + 4, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед", 1000);
    public static final Meal meal303 = new Meal(Meal_ID + 5, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин", 500);
    public static final Meal meal311 = new Meal(Meal_ID + 6, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal312 = new Meal(Meal_ID + 7, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal313 = new Meal(Meal_ID + 8, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед", 500);
    public static final Meal meal314 = new Meal(Meal_ID + 9, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин", 410);

    public static Meal getUpdated() {
        Meal updated = new Meal();
        updated.setDateTime(LocalDateTime.of(2020, 1, 31, 21, 0));
        updated.setId(meal314.getId());
        updated.setCalories(200);
        updated.setDescription("New description");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
