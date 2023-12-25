package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int curUser = 1;

    public static int authUserId() {
        return curUser;
    }

    public static void setCurUser(int curUser) {
        SecurityUtil.curUser = curUser;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}