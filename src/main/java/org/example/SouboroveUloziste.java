package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SouboroveUloziste {
    private final Path cesta;

    public SouboroveUloziste (Path cesta) {
        this.cesta = cesta;
    }

    public Map<Integer, Zakazka> nacti () {
        Map<Integer, Zakazka> zakazka = new HashMap<>();
        if (!Files.exists(cesta)) {
            return zakazka;
        }
        try {
            List<String> list = Files.readAllLines(cesta);
            for (String s : list) {
                Zakazka z = FormatZakazky.naZakazku(s);
                zakazka.put(z.getId(), z);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Zakázky se nepodařilo načíst.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return zakazka;
    }
    public void uloz (Evidence evidence) {
        List<String> radky = new ArrayList<>();
        for (Zakazka z : evidence.getZakazka().values()) {
            radky.add(FormatZakazky.naRadek(z));
        }
        try {
            Files.write(cesta, radky);
        } catch (IOException e) {
            throw new IllegalArgumentException("Nepovedlo se uložit žádnou zakázku.");
        }

    }
}
