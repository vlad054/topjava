package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {

    public static final List<User> users = Arrays.asList(
            new User(1, "user1", "user1@mail.ru", "user1", Role.USER),
            new User(2, "user2", "user2@mail.ru", "user2", Role.USER)
    );

}
