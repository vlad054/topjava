package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getUpdated;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest extends TestCase {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    private final int USER_ID = 100000;

    @Test
    public void testGet() {
        Meal meal = service.get(MealTestData.meal2.getId(), USER_ID);
        assertMatch(meal, MealTestData.meal2);
    }

    @Test
    public void testDelete() {
        int id = MealTestData.meal1.getId();
        service.delete(id, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(id, USER_ID));
    }

    @Test
    public void testUpdate() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(MealTestData.meal7.getId(), USER_ID), getUpdated());
    }
}