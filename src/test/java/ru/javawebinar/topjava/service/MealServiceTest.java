package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {//extends TestCase {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    private final int USER_ID = 100000;

    @Test
    public void create() {
        Meal created = service.create(
                new Meal(LocalDateTime.of(2020, 1, 10, 10, 0)
                        , "Завтрак", 500), 100000);
        Integer newId = created.getId();
        Meal newMeal = new Meal(MealTestData.MEAL_ID + 10, LocalDateTime.of(2020, 1, 10, 10, 0)
                , "Завтрак", 500);

        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, 100000), newMeal);
    }

    @Test
    public void duplicateMealCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(LocalDateTime.of(2020, 1, 30, 10, 0),
                        "Завтрак1", 5010), 100000));
    }

    @Test
    public void Get() {
        Meal meal = service.get(MealTestData.meal2.getId(), USER_ID);
        assertMatch(meal, MealTestData.meal2);
    }

    @Test
    public void getOtherUserMeal() {
        Meal meal = service.create(new Meal(LocalDateTime.of(2020, 1, 31, 11, 0),
                "Завтрак1", 5010), 100001);
        assertThrows(NotFoundException.class, () -> service.get(meal.getId(), USER_ID));
    }

    @Test
    public void updateOtherUserMeal() {
        Meal meal = service.create(new Meal(LocalDateTime.of(2020, 1, 31, 11, 0),
                "Завтрак1", 5010), USER_ID);
        assertThrows(NotFoundException.class, () -> service.update(
                new Meal(meal.getId(), LocalDateTime.of(2020, 1, 31, 11, 0),
                        "Завтрак1", 5020), USER_ID + 1));
    }

    @Test
    public void getNoMeal() {
        assertThrows(NotFoundException.class, () -> service.get(2000, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        MealTestData.assertMatch(all, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void BetweenInclusive() {
        List<Meal> filtered = service.getBetweenInclusive(LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 30),
                USER_ID);
        MealTestData.assertMatch(filtered, meal3, meal2, meal1);
    }

    @Test
    public void Delete() {
        int id = MealTestData.meal1.getId();
        service.delete(id, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(id, USER_ID));
    }

    @Test
    public void DeleteOtherUserMeal() {
        int id = MealTestData.meal1.getId();
        assertThrows(NotFoundException.class, () -> service.delete(id, USER_ID + 1));
    }

    @Test
    public void DeleteNotExistMeal() {
        int id = MealTestData.meal7.getId();
        assertThrows(NotFoundException.class, () -> service.delete(id + 100, USER_ID));
    }

    @Test
    public void Update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(MealTestData.meal7.getId(), USER_ID), getUpdated());
    }
}