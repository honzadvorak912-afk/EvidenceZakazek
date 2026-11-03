use evidence;
SELECT zakaznici.jmeno, zakazky.popis, zakazky.cena, zakazky.stav FROM zakazky JOIN zakaznici ON zakazky.zakaznik_id = zakaznici.id;
SELECT zakaznici.jmeno, zakazky.popis, zakazky.cena, zakazky.stav FROM zakazky JOIN zakaznici ON zakazky.zakaznik_id = zakaznici.id WHERE zakaznici.jmeno = 'Novák';
SELECT SUM(zakazky.cena) AS celkem, zakaznici.jmeno FROM zakazky JOIN zakaznici ON zakaznici.id = zakazky.zakaznik_id GROUP BY zakaznici.jmeno ORDER BY celkem DESC;
SELECT COUNT(*) FROM evidence.zakazky;
DROP DATABASE evidence;
SELECT zakaznici.jmeno, zakazky.popis, zakazky.cena, zakazky.stav, zakazky.zadani, zakazky.odevzdani FROM zakaznici LEFT JOIN zakazky ON zakaznici.id = zakazky.zakaznik_id;
SELECT zakaznici.jmeno, zakazky.popis, zakazky.cena, zakazky.stav, zakazky.zadani, zakazky.odevzdani FROM zakaznici JOIN zakazky ON zakaznici.id = zakazky.zakaznik_id;
SELECT zakaznici.jmeno FROM zakaznici LEFT JOIN zakazky ON zakaznici.id = zakazky.zakaznik_id WHERE zakazky.id IS NULL;
SELECT * FROM zakaznici;
SELECT zakaznici.jmeno, zakazky.popis, zakazky.cena FROM zakaznici JOIN zakazky ON zakaznici.id = zakazky.zakaznik_id
