# Evidence zakázek

## Co to je?
Je to program na kterém se učím základy javy.

## Co umí?
- Přidat novou zakázku se všemi potřebnými informacemi o ni. (Kontrola platnosti informací)
- Výpis zakázek po termínu odevzdání, všech zakázek, v určitém stavu.
- posunutí stavu zakázky.
- Konzolové menu ze kterého se vše dá vyvolat.
- Součet cen hotových, ale ještě nezaplacených zakázek.
- Součet cen zaplacených zakázek.
- Kolik zakázek je po termínu.
- Zrušení zakázky
- Funkční testy
- Funkční okno přes které lze program ovládat.
- Každá zakázka má svoje číslo, které se ukazuje ve výpisu a zadává při rušení a posunu stavu.

## Jak to spustit
Je potřeba JDK 25, Maven.
Konzolová verze: třída `org.example.Main`
```
mvn exec:java -Dexec.mainClass=org.example.Main
```
Okenní verze: třída `org.example.oknoEvidence`

Nebo otevřít projekt v IntelliJ a spustit třídu `org.example.Main`.
Maven výstup bufferuje, takže se výzvy k zadání mohou zobrazit až po odeslání vstupu. Pohodlnější je spustit třídu org.example.Main přímo z IntelliJ.

## Testy
Jsou to automatické jednotkové testy: samy zavolají metody, samy ověří výsledky a samy oznámí, co neklape.
```
mvn test
```

## Struktura
- `Main` - vstupní bod, 
- `Evidence` - drží všechny zakázky, přiděluje jim id a vynucuje pravidla
- `Zakazka` - třída s privátními atributy a gettery
- `Stav` - výčet stavů, kterými zakázka prochází
- `oknoEvidence` - okno přes které lze program ovládat
- `Vystup` - rozhraní pro zobrazení zprávy uživateli
- `KonzolovyVystup` - zobrazí zprávu v konzoli
- `OknovyVystup` - zobrazí zprávu v dialogovém okně
- `SberacVystup` - zprávy nikam nezobrazuje, ukládá je do seznamu; slouží testům

