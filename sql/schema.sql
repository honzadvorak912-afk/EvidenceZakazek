CREATE DATABASE IF NOT EXISTS evidence;
USE evidence;
CREATE TABLE zakaznici (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    jmeno VARCHAR(100) NOT NULL,
    telefon VARCHAR(20)
);
CREATE TABLE zakazky (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    zakaznik_id INT NOT NULL,
    popis VARCHAR(100) NOT NULL,
    cena INT NOT NULL,
    stav VARCHAR(20) NOT NULL,
    zadani DATE NOT NULL,
    odevzdani DATE NOT NULL,
    FOREIGN KEY (zakaznik_id) REFERENCES zakaznici(id)
);
