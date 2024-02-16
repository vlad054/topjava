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
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateMealCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(meal1.getDateTime(), "Завтрак1", 5010), USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(MealTestData.meal2.getId(), USER_ID);
        assertMatch(meal, MealTestData.meal2);
    }

    @Test
    public void getOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), ADMIN_ID));
    }

    @Test
    public void getNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_EXIST_ID, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        MealTestData.assertMatch(all, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> filtered = service.getBetweenInclusive(LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 30),
                USER_ID);
        MealTestData.assertMatch(filtered, meal3, meal2, meal1);
    }

    @Test
    public void delete() {
        int id = MealTestData.meal1.getId();
        service.delete(id, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(id, USER_ID));
    }

    @Test
    public void deleteOtherUserMeal() {
        int id = MealTestData.meal1.getId();
        assertThrows(NotFoundException.class, () -> service.delete(id, ADMIN_ID));
    }

    @Test
    public void deleteNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_EXIST_ID, USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(MealTestData.meal7.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
    }

}