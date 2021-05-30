INSERT INTO user(first_Name, last_Name, role, deleted)
VALUES ('Rick', 'Sanchez', 'USER', 'false'),
       ('Takeshi', 'Kovacs', 'USER', 'false'),
       ('Spinacz', 'z Worda', 'GUEST', 'false'),
       ('Ja', 'Osobiście', 'ADMIN', 'false');

INSERT INTO task(name, description, status, dead_line)
VALUES ('Zrobić portal-Gun', 'Wytentegować ten dynks z tamtym wihajstrem i spadać', 'NOT_ASSIGNED', '2021-05-10 10:00'),
       ('Idź na presłuchanie', 'Kontroluj Konstrukt', 'NOT_ASSIGNED', '2021-05-11 11:11');

INSERT INTO task(name, description, user_id, status, dead_line)
VALUES ('Podpowiedź', 'Daj losową, nic nie wnoszącą podpowiedź dla lamusów', 3, 'ASSIGNED', '2021-10-25 15:27');

INSERT INTO task(name, description, user_id, status, start_Date, end_Date, dead_line)
VALUES ('Piwo', 'Wieczorynka w Pozań city', 4, 'STARTED', '2021-05-02 15:00', '2021-05-03 10:00', '2021-05-03 16:00')