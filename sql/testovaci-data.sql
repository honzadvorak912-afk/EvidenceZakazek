USE evidence;

INSERT INTO zakaznici (jmeno, telefon)
VALUES ('Novák', '774 687 938');
INSERT INTO zakaznici (jmeno, telefon)
VALUES ('Kopecký', '777 555 333');
INSERT INTO zakaznici (jmeno, telefon)
VALUES ('Dvořák', '777 666 555');

INSERT INTO zakazky (zakaznik_id, popis, cena, stav, zadani, odevzdani)
VALUES (1, 'web pro restauraci', 20000, 'POPTAVKA', '2026-10-01', '2026-11-15');
INSERT INTO zakazky (zakaznik_id, popis, cena, stav, zadani, odevzdani)
VALUES (1, 'uprava webu', 8000, 'HOTOVO', '2026-08-01', '2026-09-10');

INSERT INTO zakazky (zakaznik_id, popis, cena, stav, zadani, odevzdani)
VALUES (2, 'eshop s pecivem', 50000, 'ROZPRACOVANO', '2026-09-15', '2026-10-20');

INSERT INTO zakazky (zakaznik_id, popis, cena, stav, zadani, odevzdani)
VALUES (3, 'postel', 1500, 'POPTAVKA', '2026-09-21', '2026-12-12');