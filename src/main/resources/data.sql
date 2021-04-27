INSERT INTO user(first_Name, last_Name)
VALUES ('Rick', 'Sanchez'), ('Takeshi', 'Kovacs'), ('Spinacz', 'z Worda');

INSERT INTO task(name, description, status)
VALUES ('Zrobić portal-Gun', 'Wytentegować ten dynks z tamtym wihajstrem i spadać', 'NOT_ASSIGNED'), ('Idź na presłuchanie', 'Kontroluj Konstrukt', 'NOT_ASSIGNED');

INSERT INTO task(name, description, user_id, status)
VALUES ('Podpowiedź', 'Daj losową, nic nie wnoszącą podpowiedź dla lamusów', 3, 'STARTED');