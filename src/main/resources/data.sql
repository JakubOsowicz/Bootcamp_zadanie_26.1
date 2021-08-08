INSERT INTO user(email, password, first_Name, last_Name, deleted)
VALUES ('Grandpa@Rick.com', '{noop}qwe', 'Rick', 'Sanchez', 'false'),
       ('Tak@Cov.har', '{noop}qwe', 'Takeshi', 'Kovacs', 'false'),
       ('office@outlook.com', '{noop}qwe', 'Spinacz', 'z Worda', 'false'),
       ('user@user.com', '{noop}qwe', 'User', 'userowy', 'false'),
       ('bua@bua.pl', '{noop}qwe', 'Ja', 'Admin', 'false');

INSERT INTO user_role(user_id, role)
VALUES (1, 'ROLE_SUPPORT'),
       (2, 'ROLE_SUPPORT'),
       (3, 'ROLE_SUPPORT'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_ADMIN');

INSERT INTO task(name, description, status, dead_line)
VALUES ('Zrobić portal-Gun', 'Pomajstrować', 'NOT_ASSIGNED', '2021-05-10 10:00'),
       ('Idź na presłuchanie', 'Kontroluj Konstrukt', 'NOT_ASSIGNED', '2021-05-11 11:11');

INSERT INTO task(name, description, user_id, status, dead_line)
VALUES ('Podpowiedź', 'Daj oczywistą podpowiedź', 3, 'ASSIGNED', '2021-10-25 15:27'),
       ('1', 'aa', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('2', 'bb', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('3', 'cc', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('4', 'dd', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('5', 'ee', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('6', 'ff', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('7', 'gg', 5, 'ASSIGNED', '2021-05-11 11:11'),
       ('8', 'hh', 5, 'ASSIGNED', '2021-05-11 11:11');


INSERT INTO task(name, description, user_id, status, start_Date, end_Date, dead_line)
VALUES ('Spotkanko', 'Wesoły wieczorek', 4, 'STARTED', '2021-05-02 15:00', '2021-05-03 10:00', '2021-05-03 16:00')

