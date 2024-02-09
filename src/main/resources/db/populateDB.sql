TRUNCATE meals;
TRUNCATE users CASCADE ;
TRUNCATE user_role;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, TO_TIMESTAMP('2020-01-30 10:00:00','YYYY-MM-DD HH24:MI:SS'), 'Завтрак', 500),
       (100000, TO_TIMESTAMP('2020-01-30 13:00:00','YYYY-MM-DD HH24:MI:SS'), 'Обед', 1000),
       (100000, TO_TIMESTAMP('2020-01-30 20:00:00','YYYY-MM-DD HH24:MI:SS'), 'Ужин', 500),
       (100000, TO_TIMESTAMP('2020-01-31 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'Еда на граничное значение', 100),
       (100000, TO_TIMESTAMP('2020-01-31 10:00:00','YYYY-MM-DD HH24:MI:SS'), 'Завтрак', 1000),
       (100000, TO_TIMESTAMP('2020-01-31 13:00:00','YYYY-MM-DD HH24:MI:SS'), 'Обед', 500),
       (100000, TO_TIMESTAMP('2020-01-31 20:00:00','YYYY-MM-DD HH24:MI:SS'), 'Ужин', 410);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100001, TO_TIMESTAMP('2020-01-30 10:00:00','YYYY-MM-DD HH24:MI:SS'), 'Завтрак', 500),
       (100001, TO_TIMESTAMP('2020-01-30 13:00:00','YYYY-MM-DD HH24:MI:SS'), 'Обед', 1000);
