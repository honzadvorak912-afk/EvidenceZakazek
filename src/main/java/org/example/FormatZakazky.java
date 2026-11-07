package org.example;

import java.time.LocalDate;
import java.util.Map;

public class FormatZakazky {
    public static String zakazkyNaRadek(Zakazka zakazka1) {
        return String.join(";", String.valueOf(zakazka1.getId()), String.valueOf(zakazka1.getZakaznikId()), zakazka1.getPopis(), String.valueOf(zakazka1.getCena()), zakazka1.getStav().toString(), zakazka1.getVytvoreni().toString(), zakazka1.getOdevzdani().toString());
    }
    public static String zakazniciNaRadek(Zakaznik zakaznik) {
        return String.join(";", zakaznik.getJmeno(), zakaznik.getTelefon(), String.valueOf(zakaznik.getId()));
    }

    public static Zakazka naZakazku(String zakazka, Map<Integer, Zakaznik> zakaznikMap) {
        String[] casti = zakazka.split(";");
        if (casti.length != 7) {
            throw new IllegalArgumentException("Poškozený řádek: " + zakazka);
        }
        Zakaznik zakaznik = zakaznikMap.get(Integer.parseInt(casti[1]));
        if (zakaznik == null) {
            throw new IllegalArgumentException(
                    "Zakázka " + casti[0] + " odkazuje na zákazníka " + casti[1] + ", který neexistuje.");
        }
        return new Zakazka(Integer.parseInt(casti[0]), zakaznik, casti[2], Integer.parseInt(casti[3]), Stav.valueOf(casti[4]), LocalDate.parse(casti[5]), LocalDate.parse(casti[6]));
    }
    public static Zakaznik naZakaznika (String zakaznik) {
        String[] casti = zakaznik.split(";");
        if (casti.length != 3) {
            throw new IllegalArgumentException("Poškozený řádek: " + zakaznik);
        }
        return new Zakaznik(casti[0], casti[1], Integer.parseInt(casti[2]));
    }
}
