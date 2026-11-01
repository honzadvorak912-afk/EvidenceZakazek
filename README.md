# Evidence zakázek

## Co to je?
Program, na kterém se učím základy Javy.

## Co umí?
- Přidat zákazníka (jméno, telefon) a vypsat seznam zákazníků.
- Zrušit zákazníka — jen pokud už nemá žádnou zakázku.
- Přidat novou zakázku se všemi potřebnými informacemi o ní, včetně kontroly platnosti zadaných údajů.
- Výpis všech zakázek, zakázek v určitém stavu, nebo zakázek po termínu odevzdání.
- Posunutí stavu zakázky.
- Zrušení zakázky.
- Součet cen hotových, ale ještě nezaplacených zakázek.
- Součet cen zaplacených zakázek.
- Kolik zakázek je po termínu.
- Konzolové menu, ze kterého se vše dá vyvolat.
- Každá zakázka i zákazník má svoje číslo, které se ukazuje ve výpisu a zadává se při rušení a posunu stavu.
- Data se ukládají do CSV souborů nebo do databáze MySQL, takže po ukončení programu zůstanou zachovaná.
- Funkční jednotkové testy.

## Úložiště
Program umí ukládat dvěma způsoby. Obě varianty splňují rozhraní `Uloz`, takže se přepínají jediným řádkem v `Main`:

```java
Uloz uloz = new SouboroveUloziste(Path.of("zakazky.csv"), Path.of("zakaznici.csv"));
// nebo
Uloz uloz = new DatabazoveUloziste("jdbc:mysql://localhost:3306/evidence", "root", "");
```

**Soubory** nepotřebují žádnou přípravu — vzniknou samy při prvním uložení.

**Databáze** potřebuje běžící MySQL server a tyhle tabulky:

```sql
CREATE DATABASE IF NOT EXISTS evidence;
USE evidence;

CREATE TABLE zakaznici (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    jmeno VARCHAR(100) NOT NULL,
    telefon VARCHAR(20)
) ENGINE=InnoDB;

CREATE TABLE zakazky (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    zakaznik_id INT NOT NULL,
    popis VARCHAR(100) NOT NULL,
    cena INT NOT NULL,
    stav VARCHAR(20) NOT NULL,
    zadani DATE NOT NULL,
    odevzdani DATE NOT NULL,
    FOREIGN KEY (zakaznik_id) REFERENCES zakaznici(id)
) ENGINE=InnoDB;
```

`ENGINE=InnoDB` je potřeba uvést výslovně — pod MyISAM se cizí klíč tiše zahodí a do tabulky `zakazky` by šlo vložit zakázku neexistujícího zákazníka.

## Jak to spustit
Je potřeba JDK 25 a Maven.

```
mvn exec:java -Dexec.mainClass=org.example.Main
```

Nebo otevřít projekt v IntelliJ a spustit třídu `org.example.Main`. Maven výstup bufferuje, takže se výzvy k zadání mohou zobrazit až po odeslání vstupu — spuštění přímo z IntelliJ je pohodlnější.

## Testy
Automatické jednotkové testy: samy zavolají metody, samy ověří výsledky a samy oznámí, co neklape.

```
mvn test
```

## Struktura

**Data a pravidla**
- `Evidence` — drží zakázky i zákazníky, přiděluje jim čísla a vynucuje pravidla
- `Zakazka` — jedna zakázka; odkazuje na svého zákazníka
- `Zakaznik` — jeden zákazník
- `Stav` — výčet stavů, kterými zakázka prochází

**Ukládání**
- `Uloz` — rozhraní úložiště
- `SouboroveUloziste` — ukládá do dvou CSV souborů
- `DatabazoveUloziste` — ukládá do MySQL
- `FormatZakazky` — převod mezi objekty a řádky CSV

**Rozhraní k uživateli**
- `Main` — vstupní bod, konzolové menu
- `ZakazkaService` — operace nad zakázkou společné všem rozhraním
- `Vystup` — rozhraní pro zobrazení zprávy uživateli
- `KonzolovyVystup` — zobrazí zprávu v konzoli
- `SberacVystup` — zprávy nezobrazuje, ukládá je do seznamu; slouží testům