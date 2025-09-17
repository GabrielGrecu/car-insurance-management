DELETE FROM insurancepolicy;
DELETE FROM car;
DELETE FROM owner;

INSERT INTO owner (id, name, email) VALUES (1, 'Ana Pop', 'ana.pop@example.com');
INSERT INTO owner (id, name, email) VALUES (2, 'Bogdan Ionescu', 'bogdan.ionescu@example.com');

INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (1, 'VIN12345', 'Dacia', 'Logan', 2018, 1);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (2, 'VIN67890', 'VW', 'Golf', 2021, 2);

INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (1, 1, 'Allianz', '2024-01-01', '2024-12-31');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (2, 1, 'Groupama', '2025-01-01', '2026-01-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (3, 2, 'Allianz', '2025-03-01', '2025-09-30');

UPDATE insurancepolicy SET end_date = CURRENT_DATE WHERE id = 1;
UPDATE insurancepolicy SET end_date = CURRENT_DATE - INTERVAL '7 days' WHERE id = 2;
SELECT setval('insurancepolicy_id_seq', (SELECT MAX(id) FROM insurancepolicy));