# Evidence zakázek

## Co to je?
Je to program na kterém se učím základy javy.

## Co umí?
- Přidat novou zakázku se všemi potřebnými informacemi o ni. (Kontrola platnosti informací)
- Výpis zakázek po termínu odevzdání, všech zakázek, v určitém stavu.

## Jak to spustit
Je potřeba JDK 25, Maven.
```
mvn compile
mvn exec:java -Dexec.mainClass=org.example.org.Main
```
Nebo otevřít projekt v IntelliJ a spustit třídu `org.Main`.
Maven výstup bufferuje, takže se výzvy k zadání mohou zobrazit až po odeslání vstupu. Pohodlnější je spustit třídu org.Main přímo z IntelliJ.

## Testy
Jsou to automatické jednotkové testy: samy zavolají metody, samy ověří výsledky a samy oznámí, co neklape.
```
mvn test
```

## Struktura
Program je rozdělen do 3 souborů.
- `main` - vstupní bod, 
- `evidence` - vytváření zakázky,
- `zakazka` - vytvoření recordu pro záznam zakázek.

## Co dál
- Změna stavu zakázky.
- Kolik zakázek je v jednotlivých stavech.
- Součet cen hotových, ale ještě nezaplacených zakázek.
- Součet cen zaplacených zakázek.
- Kolik zakázek je po termínu.
- Konzolové menu, ze kterého se všechno výše dá vyvolat.