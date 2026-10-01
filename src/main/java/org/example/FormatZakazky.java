package org.example;

import java.time.LocalDate;

public class FormatZakazky {
    public static String naRadek(Zakazka zakazka1) {
        return String.join(";", String.valueOf(zakazka1.getId()), zakazka1.getJmeno(), zakazka1.getPopis(), String.valueOf(zakazka1.getCena()), zakazka1.getStav().toString(), zakazka1.getVytvoreni().toString(), zakazka1.getOdevzdani().toString());
    }

    public static Zakazka naZakazku(String zakazka) {
        String[] casti = zakazka.split(";");
        if (casti.length != 7) {
            throw new IllegalArgumentException("Poškozený řádek: " + zakazka);
        }
        return new Zakazka(Integer.parseInt(casti[0]), casti[1], casti[2], Integer.parseInt(casti[3]), Stav.valueOf(casti[4]), LocalDate.parse(casti[5]), LocalDate.parse(casti[6]));
    }
}
